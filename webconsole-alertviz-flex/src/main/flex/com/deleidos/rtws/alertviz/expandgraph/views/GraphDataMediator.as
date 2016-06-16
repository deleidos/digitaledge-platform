package com.deleidos.rtws.alertviz.expandgraph.views
{
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.events.ApplicationCommandEvent;
	import com.deleidos.rtws.alertviz.events.repository.ModelServiceEvent;
	import com.deleidos.rtws.alertviz.expandgraph.events.GraphChangedEvent;
	import com.deleidos.rtws.alertviz.expandgraph.graph.GraphSettings;
	import com.deleidos.rtws.alertviz.expandgraph.graph.Node;
	import com.deleidos.rtws.alertviz.expandgraph.models.GraphModel;
	import com.deleidos.rtws.alertviz.models.repository.DataModelDirectory;
	import com.deleidos.rtws.alertviz.models.repository.DataModelName;
	import com.deleidos.rtws.alertviz.models.repository.ModelFieldData;
	import com.deleidos.rtws.alertviz.services.repository.RepositoryConstants;
	import com.deleidos.rtws.alertviz.services.repository.rest.DataModelServices;
	import com.deleidos.rtws.alertviz.utils.repository.LoadSaveUtils;
	import com.deleidos.rtws.alertviz.views.popups.AlertMessagePopUpView;
	
	import flash.events.ContextMenuEvent;
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.events.MouseEvent;
	import flash.net.FileFilter;
	import flash.net.FileReference;
	import flash.ui.ContextMenu;
	import flash.ui.ContextMenuItem;
	
	import mx.collections.XMLListCollection;
	import mx.controls.Alert;
	import mx.controls.TextInput;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.controls.treeClasses.TreeItemRenderer;
	import mx.core.FlexGlobals;
	import mx.events.CloseEvent;
	import mx.events.ListEvent;
	import mx.events.TreeEvent;
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;

	public class GraphDataMediator extends Mediator
	{
		[Inject]
		public var view:GraphDataView;
		
		[Inject]
		public var dataModel:GraphModel;
		
		[Inject]
		public var dmDirectory:DataModelDirectory;
		
		[Inject]
		public var fieldData:ModelFieldData;
		
		private var designDefaultContext:ContextMenu;
		private var designOtherContext:ContextMenu;
		private var designContexts:Object;
		
		private var registeredRenderers:Object;
		private var registeredRollovers:Object;
		private var mostRecentRollover:Object;
		private var currentRollover:Object;
		
		private var ignoreTreeEvents:Boolean;
		
		private var designPopup:GraphDesignView;
		private var editPopup:GraphConfigView;
		
		private var mostRecentDataModel:DataModelName;
		private var mostRecentDataModelLoaded:Boolean;

		public override function onRegister():void {			
			registeredRenderers = new Object();
			registeredRollovers = new Object();
			mostRecentRollover = new Object();
			ignoreTreeEvents = false;
			mostRecentDataModelLoaded = false;
			
			generateContextMenus();
			
			view.designTree.addEventListener(ListEvent.ITEM_ROLL_OVER, itemRolledOverHack);
			view.designTree.addEventListener(TreeEvent.ITEM_OPENING, resetIDs);
			
			//view.designTree.addEventListener(MouseEvent.MOUSE_OUT, treeRolledOut);
			view.designTree.addEventListener(MouseEvent.MOUSE_OVER, treeRolledOver);
			view.designTree.addEventListener(ListEvent.ITEM_EDIT_BEGIN, itemBeingEdited);
			view.designTree.addEventListener(ListEvent.ITEM_EDIT_END, itemEditDone);
			
			view.designTree.labelFunction = designTreeLabelFunction;
			view.openDesignView.addEventListener(MouseEvent.CLICK, onOpenDesignClick);
			view.modelNameCombo.addEventListener(ListEvent.CHANGE, onModelNameChange);
			
			eventMap.mapListener(eventDispatcher, ApplicationCommandEvent.CLEAR, onClear);
			eventMap.mapListener(eventDispatcher, ModelServiceEvent.MODEL_DIR_LOADED, onModelDirectoryLoad);
			eventMap.mapListener(eventDispatcher, ModelServiceEvent.MODEL_LOADED, onModelLoaded);
			eventMap.mapListener(eventDispatcher, ModelServiceEvent.MODEL_LOAD_FAIL, onModelLoadFail);
			
			view.modelNameCombo.prompt = "Select...";
			view.modelNameCombo.dmDirectory = dmDirectory;
			refreshView();
		}
		
		public override function onRemove():void {		
			view.designTree.removeEventListener(ListEvent.ITEM_ROLL_OVER, itemRolledOverHack);
			view.designTree.removeEventListener(TreeEvent.ITEM_OPENING, resetIDs);
			
			//view.designTree.removeEventListener(MouseEvent.MOUSE_OUT, treeRolledOut);
			view.designTree.removeEventListener(MouseEvent.MOUSE_OVER, treeRolledOver);
			view.designTree.removeEventListener(ListEvent.ITEM_EDIT_BEGIN, itemBeingEdited);
			view.designTree.removeEventListener(ListEvent.ITEM_EDIT_END, itemEditDone);
			
			view.openDesignView.removeEventListener(MouseEvent.CLICK, onOpenDesignClick);
			
			eventMap.unmapListener(eventDispatcher, ApplicationCommandEvent.CLEAR, onClear);
			eventMap.unmapListener(eventDispatcher, ModelServiceEvent.MODEL_DIR_LOADED, onModelDirectoryLoad);
		}
		
		private function generateContextMenus():void{
			var myMenu:ContextMenu;
			var menuItem1:ContextMenuItem;
			var menuItem2:ContextMenuItem;
			var menuItem3:ContextMenuItem;
			var menuItem4:ContextMenuItem;
			
			designContexts = new Object();
			
			myMenu = new ContextMenu();
			myMenu.addEventListener(ContextMenuEvent.MENU_SELECT, onContextMenuOpen);
			myMenu.hideBuiltInItems();
			menuItem1 = new ContextMenuItem("Add Chain");
			menuItem1.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, addRootSelected);
			myMenu.customItems.push(menuItem1);
			designDefaultContext = myMenu;
			
			myMenu = new ContextMenu();
			myMenu.addEventListener(ContextMenuEvent.MENU_SELECT, onContextMenuOpen);
			myMenu.hideBuiltInItems();
			menuItem2 = new ContextMenuItem("Edit Children");
			menuItem1 = new ContextMenuItem("Add Child");
			menuItem3 = new ContextMenuItem("Rename");
			menuItem4 = new ContextMenuItem("Delete Chain");
			menuItem1.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, addNodeSelected);
			menuItem2.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, editValueSelected);
			menuItem3.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, renameSelected);
			menuItem4.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, deleteSelected);
			myMenu.customItems.push(menuItem1, menuItem2, menuItem3, menuItem4);
			designContexts[GraphSettings.CHAIN] = myMenu;
			
			myMenu = new ContextMenu();
			myMenu.addEventListener(ContextMenuEvent.MENU_SELECT, onContextMenuOpen);
			myMenu.hideBuiltInItems();
			menuItem1 = new ContextMenuItem("Edit Children");
			menuItem2 = new ContextMenuItem("Rename");
			menuItem1.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, editValueSelected);
			menuItem2.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, renameSelected);
			myMenu.customItems.push(menuItem1, menuItem2);
			designContexts[GraphSettings.ROOT] = myMenu;
			
			myMenu = new ContextMenu();
			myMenu.addEventListener(ContextMenuEvent.MENU_SELECT, onContextMenuOpen);
			myMenu.hideBuiltInItems();
			menuItem1 = new ContextMenuItem("Edit Children");
			menuItem2 = new ContextMenuItem("Rename");
			menuItem3 = new ContextMenuItem("Delete Node");
			menuItem1.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, editValueSelected);
			menuItem2.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, renameSelected);
			menuItem3.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, deleteSelected);
			myMenu.customItems.push(menuItem1, menuItem2, menuItem3);
			designContexts[GraphSettings.NODE] = myMenu;
			
			myMenu = new ContextMenu();
			myMenu.addEventListener(ContextMenuEvent.MENU_SELECT, onContextMenuOpen);
			myMenu.hideBuiltInItems();
			menuItem1 = new ContextMenuItem("Edit Children");
			menuItem1.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, editValueSelected);
			myMenu.customItems.push(menuItem1);
			designOtherContext = myMenu;
			
			myMenu = new ContextMenu();
			myMenu.addEventListener(ContextMenuEvent.MENU_SELECT, onContextMenuOpen);
			myMenu.hideBuiltInItems();
			menuItem1 = new ContextMenuItem("Edit");
			menuItem1.addEventListener(ContextMenuEvent.MENU_ITEM_SELECT, editValueSelected);
			myMenu.customItems.push(menuItem1);
			designContexts[GraphSettings.VALUE] = myMenu;			
		}
		
		private function onModelNameChange(event:Event):void{
			mostRecentDataModel = view.modelNameCombo.selectedModel;
			if(mostRecentDataModel != null){
				mostRecentDataModelLoaded = false;
				dispatch(new ModelServiceEvent(ModelServiceEvent.LOAD_MODEL, {"dataModelName":mostRecentDataModel}, null));
			}
		}
		
		private function onModelLoaded(event:ModelServiceEvent):void{
			var model:DataModelName = event.parameters as DataModelName;
			if(mostRecentDataModel == model){
				mostRecentDataModelLoaded = true;
				if(editPopup != null)
					editPopup.fieldData = fieldData.data;
			}
		}
		
		private function onModelLoadFail(event:ModelServiceEvent):void{
			var popup:AlertMessagePopUpView = new AlertMessagePopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
			
			
			var title:String = "Model Load Failure";
			var message:String = event.parameters.toString();
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.ALERT_MESSAGE, {title:title, message:message}));

		}
		
		private function onModelDirectoryLoad(event:Event):void{
			view.modelNameCombo.refresh();
			if(dataModel.graphSettings != null)
				view.modelNameCombo.selectModelByName(dataModel.graphSettings.dataModel);
		}
		
		public function designTreeLabelFunction(item:Object):String{		
			if(item.@[GraphSettings.DISPLAY_VALUE] == null)
				return "[No Value]";
			else
				return item.@[GraphSettings.DISPLAY_VALUE];
		}
		
		private function onOpenDesignClick( event:MouseEvent ):void{
			designPopup = new GraphDesignView();
			
			designPopup.exportXML = dataModel.graphSettings == null ? null : dataModel.graphSettings.toXML();
			if(designPopup.exportXML != null){
				if(view.modelNameCombo.selectedModel)
					designPopup.exportXML.@[GraphSettings.MODEL] = view.modelNameCombo.selectedModel.name;
				else 
					delete designPopup.exportXML.@[GraphSettings.MODEL];
			}
			
			designPopup.addEventListener(CloseEvent.CLOSE, onImportExportCompletion);
			
			PopUpManager.addPopUp(designPopup, view.parent.parent, true);
			PopUpManager.centerPopUp(designPopup);
			
			mediatorMap.createMediator(designPopup);
		}
		
		protected function onClear(event:ApplicationCommandEvent):void
		{
			view.designData = new XMLListCollection();
			view.modelNameCombo.selectedIndex = -1;
		}
		
		protected function onImportExportCompletion( event:CloseEvent ):void{
			var xml:XML = designPopup.importXML;
			if(xml != null){
				dataModel.graphSettings = GraphSettings.fromXML(xml);
				refreshView();
			}
			mediatorMap.removeMediatorByView(designPopup);
			PopUpManager.removePopUp(designPopup);
			designPopup = null;
		}
		
		public function onContextMenuOpen( event:ContextMenuEvent ):void{
			//Needed because mostRecentRollover can change after the context menu has been opened
			currentRollover = mostRecentRollover;
		}
		
		public function resetIDs( event:TreeEvent ):void{
			registeredRenderers = new Object();
		}
		
		public function addRootSelected( event:ContextMenuEvent ):void{
			view.designData.addItem(GraphSettings.getDefaultChain());
			refreshModel();
			//TODO: Popup that populates the fields for this new chain
		}
		
		public function addNodeSelected( event:ContextMenuEvent ):void{
			var targetData:XML = view.designTree.indexToItemRenderer(view.designTree.indicesToIndex(currentRollover["rowIndex"], currentRollover["columnIndex"])).data as XML;
			targetData.appendChild(GraphSettings.getDefaultNode());
			refreshModel();
			//TODO: Popup that populates the fields for this new node
		}
		
		public function deleteSelected( event:ContextMenuEvent ):void{
			var targetData:XML = view.designTree.indexToItemRenderer(view.designTree.indicesToIndex(currentRollover["rowIndex"], currentRollover["columnIndex"])).data as XML;
			
			//NOTE! This function will not work as intended under one circumstance: if the target XML is not unique in the xml list
			//Since this function is only called when deleting nodes however, it is unlikely that this would ever occur
			//as it would be rather useless to have two nodes with the exact same properties
			//One workaround to this is adding a unique ID tag to the xml nodes upon creation
			if(!deleteTarget(view.designData, targetData))
				throw Error("Requested delete operation could not be completed!"); //should never happen
			
			refreshModel();
		}
		
		//Needed to handle root node deletions
		private function deleteTarget( xml:XMLListCollection, target:XML ):Boolean{
			for(var i:int = 0; i < xml.length; i++){
				if(xml[i] == target){
					xml.removeItemAt(i);
					return true;
				}else if(deleteTargetH(xml[i].children(), target))
					return true;
			}
			return false;
		}
		
		private function deleteTargetH( xml:XMLList, target:XML ):Boolean{
			for(var i:int = 0; i < xml.length(); i++){
				if(xml[i] == target){
					delete xml[i];
					return true;
				}else if(deleteTargetH(xml[i].children(), target))
					return true;
			}
			return false;
		}
		
		private function editValueSelected( event:ContextMenuEvent ):void{
			createFunctionEditPopup(view.designTree.indexToItemRenderer(view.designTree.indicesToIndex(currentRollover["rowIndex"], currentRollover["columnIndex"])).data as XML);
		}
		
		private function createFunctionEditPopup(targetData:XML):void{
			editPopup = new GraphConfigView(targetData);
			if(mostRecentDataModelLoaded) editPopup.fieldData = fieldData.data;
			
			editPopup.addEventListener(CloseEvent.CLOSE, onFunctionEditCompletion);
			
			PopUpManager.addPopUp(editPopup, view.parent.parent, true);
			PopUpManager.centerPopUp(editPopup);
		}
		
		private function onFunctionEditCompletion( event:CloseEvent ):void{
			refreshModel();
			PopUpManager.removePopUp(editPopup);
			editPopup = null;
		}
		
		public function renameSelected( event:ContextMenuEvent ):void{
			view.designTree.editable = true;
			view.designTree.editedItemPosition = currentRollover;
		}
		
		public function itemBeingEdited( event:ListEvent ):void{
			view.designTree.editable = false;
		}
		
		public function itemEditDone( event:ListEvent ):void{
			var newText:String = (view.designTree.itemEditorInstance as TextInput).text;
			var editableObj:Object = view.designTree.editedItemRenderer.data;
			editableObj.@name = newText;
			
			refreshModel();
			view.designTree.destroyItemEditor();
			event.preventDefault();
		}
		
		public function itemRolledOverHack( event:ListEvent ):void{
			//hack because ListEvent.ITEM_ROLL_OVER is bugged
			var dataKey:String = "(" + event.itemRenderer.x + " " + event.itemRenderer.y + ")";
			if(!registeredRenderers[dataKey]){
				event.itemRenderer.addEventListener(MouseEvent.MOUSE_OVER, itemRolledOver);
				event.itemRenderer.addEventListener(MouseEvent.MOUSE_OUT, itemRolledOut);
					
				registeredRenderers[dataKey] = true;
				registeredRollovers[dataKey] = new Object();
				(registeredRollovers[dataKey])["rowIndex"] = event.rowIndex;
				(registeredRollovers[dataKey])["columnIndex"] = event.columnIndex;
					
				itemRolledOver(null, TreeItemRenderer(event.itemRenderer));	
			}
		}
		
		public function itemRolledOver( event:MouseEvent, renderer:TreeItemRenderer = null ):void{
			if(renderer == null) renderer = TreeItemRenderer(event.currentTarget);
			
			var menu:ContextMenu = designContexts[(renderer.data as XML).localName()];
			if(menu == null) view.contextMenu = designOtherContext;
			else view.contextMenu = menu;
			
			mostRecentRollover = registeredRollovers["(" + renderer.x + " " + renderer.y + ")"];
			ignoreTreeEvents = true;
		}
		
		public function itemRolledOut( event:MouseEvent ):void{	
			view.contextMenu = designDefaultContext;
			ignoreTreeEvents = false;
		}
		
		public function treeRolledOver( event:MouseEvent ):void{
			if(!ignoreTreeEvents){ 
				view.contextMenu = designDefaultContext;
			}
		}
		
		/*
		public function treeRolledOut( event:MouseEvent ):void{
			if(!ignoreTreeEvents) FlexGlobals.topLevelApplication.contextMenu = defaultContext;
		}
		*/
		
		private function refreshView():void{
			if(dataModel.graphSettings == null){
				view.designData.removeAll();
				view.modelNameCombo.selectedIndex = -1;
			}else{
				var xml:XML = dataModel.graphSettings.toXML();
				var modelName:String = GraphSettings.getModelFromXML(xml);
				if(view.modelNameCombo.selectedIndex == -1 || view.modelNameCombo.selectedModel.name != modelName){
					if(modelName == null)
						view.modelNameCombo.selectedIndex = -1;
					else{
						view.modelNameCombo.selectModelByName(modelName);
						onModelNameChange(null); //force the new model data to be loaded
					}
				}
				view.designData = new XMLListCollection();
				for each( var child:XML in xml.children() )
					view.designData.addItem(child);
			}
		}
		
		private function refreshModel():void{
			var tmpXML:XML = new XML("<" + GraphSettings.SETTINGS + "/>");
			if(view.modelNameCombo.selectedModel) tmpXML.@[GraphSettings.MODEL] = view.modelNameCombo.selectedModel.name;
			for each( var child:XML in view.designData)
				tmpXML.appendChild(child);
			
			dataModel.graphSettings = GraphSettings.fromXML(tmpXML);
		}
		
	}
}