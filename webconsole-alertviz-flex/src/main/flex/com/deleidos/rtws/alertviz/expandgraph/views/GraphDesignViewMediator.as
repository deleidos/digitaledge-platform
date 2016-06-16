package com.deleidos.rtws.alertviz.expandgraph.views
{
	import com.deleidos.rtws.alertviz.expandgraph.models.GraphConfigModel;
	import com.deleidos.rtws.alertviz.expandgraph.events.GraphConfigEvent;
	import com.deleidos.rtws.alertviz.expandgraph.events.PopupEvent;
	
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	import flash.ui.Mouse;
	
	import flexlib.scheduling.scheduleClasses.schedule_internal;
	
	import mx.collections.XMLListCollection;
	import mx.containers.TitleWindow;
	import mx.core.UIComponent;
	import mx.events.CloseEvent;
	import mx.events.ListEvent;
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;

	public class GraphDesignViewMediator extends Mediator
	{
		[Inject]
		public var view:GraphDesignView;
		
		[Inject]
		public var model:GraphConfigModel;
		
		[Embed(source="/assets/graph/imgs/loading.png")] 
		public var loadingIcon:Class; 
		
		protected var lastFocus:UIComponent;
		
		protected function designTreeLabelFunction(item:Object):String{
			if(item.@[GraphConfigModel.NAME] == null)
				return "";
			else
				return item.@[GraphConfigModel.NAME];
		}
		
		public override function onRegister():void {
			eventMap.mapListener(eventDispatcher, GraphConfigEvent.SERVICE_COMPLETE, onServiceComplete);
			eventMap.mapListener(eventDispatcher, GraphConfigEvent.SERVICE_ERROR, onServiceError);
			
			view.designTree.addEventListener(ListEvent.CHANGE, onItemChange);;
			
			view.loadButton.addEventListener(MouseEvent.CLICK, loadConfigFile);
			view.saveButton.addEventListener(MouseEvent.CLICK, saveConfigFile);
			view.newFolderButton.addEventListener(MouseEvent.CLICK, makeNewFolder);
			view.deleteButton.addEventListener(MouseEvent.CLICK, deleteConfigFiles);
			view.cancelButton.addEventListener(MouseEvent.CLICK, closeView);
			view.designTree.addEventListener(ListEvent.CHANGE, function anon():void{ lastFocus = view.designTree });
			view.fileNameInput.addEventListener(MouseEvent.CLICK, function anon():void{ lastFocus = view.fileNameInput });
			
			view.addEventListener(KeyboardEvent.KEY_UP, onKeyboardEvent);
			
			view.designTree.labelFunction = designTreeLabelFunction;
			view.designData.addItem(new XML("<node " + GraphConfigModel.NAME + "=\"Loading...\"/>"));
			view.designTree.setItemIcon(view.designTree.dataProvider.getItemAt(0), loadingIcon, null);
			
			dispatch(new GraphConfigEvent(GraphConfigEvent.GET_LOCATIONS, null));
		}
		
		public override function onRemove():void {
			eventMap.unmapListener(eventDispatcher, GraphConfigEvent.SERVICE_COMPLETE, onServiceComplete);
			eventMap.unmapListener(eventDispatcher, GraphConfigEvent.SERVICE_ERROR, onServiceError);
			
			view.loadButton.removeEventListener(MouseEvent.CLICK, loadConfigFile);
			view.saveButton.removeEventListener(MouseEvent.CLICK, saveConfigFile);
			view.newFolderButton.removeEventListener(MouseEvent.CLICK, makeNewFolder);
			view.deleteButton.removeEventListener(MouseEvent.CLICK, deleteConfigFiles);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, closeView);
			view.designTree.removeEventListener(ListEvent.CHANGE, function anon():void{ lastFocus = view.designTree });
			view.fileNameInput.removeEventListener(MouseEvent.CLICK, function anon():void{ lastFocus = view.fileNameInput });
			
			view.removeEventListener(KeyboardEvent.KEY_UP, onKeyboardEvent);
		}
		
		public function onItemChange( event:ListEvent ):void{
			var text:String = "";
			var selectedItems:Array = view.designTree.selectedItems;
			for (var i:int = 0; i < selectedItems.length-1; i++  ){
				text += getFileName(selectedItems[i]) + ",";
			}
			if(selectedItems.length != 0)
				text += getFileName(selectedItems[selectedItems.length-1]);
			
			view.fileNameInput.text = text;
		}
		
		protected function getFileName( node:XML ):String{
			if(node == null) return null;
			var name:String = node.@[GraphConfigModel.NAME] + (node.@isBranch == "true" ? "\\" : "");
			
			var parent:XML = node.parent();
			while (parent != null) {
				name = parent.@[GraphConfigModel.NAME] + "\\" + name;
				parent = parent.parent();
			}
			
			return name;
		}
		
		public function onKeyboardEvent( event:KeyboardEvent ):void{
			if(event.keyCode == Keyboard.DELETE){
				this.deleteConfigFiles(null);
			}
		}
		
		public function onServiceComplete( event:GraphConfigEvent ):void{
			if(event.parameters.source == GraphConfigEvent.SAVE || event.parameters.source == GraphConfigEvent.GET_CONFIG || event.parameters.source == GraphConfigEvent.UPDATE){ //We're done
				view.importXML = event.parameters.output;
				closeView();
			}
			
			view.errorLabel.text = "";
			var openedItems:Object = view.designTree.openItems;
			var openedItemNames:Object = new Object();
			var child:XML;
			for each(child in openedItems)
				openedItemNames[getFileName(child)] = "true";
			
			view.designData = new XMLListCollection();
			for each(child in model.fileStructureToXMLList())
				view.designData.addItem(child);
				
			view.designTree.validateNow();
			
			//reopens any open nodes after the data has been refreshed
			for each(child in view.designData)
				openAll(child, openedItemNames, getFileName);
		}
		
		public function openAll(node:XML, items:Object, matcherFunction:Function):void{
			if(items[matcherFunction(node)] != null)
				view.designTree.expandItem(node, true);
			
			for each(var child:XML in node.children())
				openAll(child, items, matcherFunction);
		}
		
		public function onServiceError( event:GraphConfigEvent ):void{
			view.errorLabel.text = "A server error occured while processing your request.";
		}
		
		public function onPopupCancel( event:PopupEvent ):void{
			view.errorLabel.text = "";
		}
		
		//Used for storing locations for callbacks
		protected var placeholderLocation:String;
		protected var deletionItems:Array;
		
		//--Start code handling loading files
		
		public function loadConfigFile( event:MouseEvent ):void{
			var locations:Array = view.fileNameInput.text.split(",");
			if(locations.length > 1)
				view.errorLabel.text = "You can only load one file.";
			else{
				var location:String = locations[0];
				if(location.charAt(location.length-1) == '\\')
					view.errorLabel.text = "You cannot load a folder.";
				else if(!model.itemExists(location))
					view.errorLabel.text = location + " does not exist.";
				else{
					view.errorLabel.text = "Loading...";
					dispatch(new GraphConfigEvent(GraphConfigEvent.GET_CONFIG,{"location":location}));
				}
			}
		}
		
		//--End code handling loading files
		
		//--Start code handling saving files
		
		public function saveConfigFile( event:MouseEvent ):void{
			var locations:Array = view.fileNameInput.text.split(",");
			if(view.exportXML == null)
				view.errorLabel.text = "There is no configuration file to save.";
			else if(locations.length > 1)
				view.errorLabel.text = "You can only save to one location.";
			else{
				var location:String = locations[0];
				if(location == "" || location.charAt(location.length-1) == '\\'){
					placeholderLocation = location;
					view.doInputPopup("New File", "Please enter a new file name.", "New File" , onSavePopupSuccess, onPopupCancel);	
				}else
					saveConfigFileValidate(location);
			}
		}
		
		protected function saveConfigFileValidate(location:String):void{
			if(location == "")
				view.errorLabel.text = "Youre file name cannot be blank.";
			else if(location.indexOf(",") != -1)
				view.errorLabel.text = "Your file name cannot contain commas.";
			else if(location.charAt(location.length-1) == '\\')
				view.errorLabel.text = "Please input a file name, " + location + " is a folder.";
			else if(model.itemExists(location)){
				placeholderLocation = location;
				view.doConfirmPopup("Overwrite?", location + " exists. Do you wish to overwrite it?", onSavePopupOverwriteSuccess, onPopupCancel);
			}else if(!model.validInsertionPath(location)){
				view.errorLabel.text = location + " is not a valid location.";
			}else{
				view.errorLabel.text = "Saving...";
				dispatch(new GraphConfigEvent(GraphConfigEvent.SAVE,{"location":location, "xml":view.exportXML}));
			}
		}
		
		protected function onSavePopupSuccess( event:PopupEvent ):void{
			saveConfigFileValidate(placeholderLocation + event.result);
		}
		
		protected function onSavePopupOverwriteSuccess( event:PopupEvent ):void{
			view.errorLabel.text = "Updating...";
			dispatch(new GraphConfigEvent(GraphConfigEvent.UPDATE,{"location":placeholderLocation, "xml":view.exportXML}));
		}
		
		//--End code handling saving files
		
		//--Start code handing making new folders
		
		public function makeNewFolder( event:MouseEvent ):void{
			var locations:Array = view.fileNameInput.text.split(",");
			if(locations.length > 1)
				view.errorLabel.text = "You can only create a folder in one location.";
			else{
				var location:String = locations[0];
				if(location == ""){
					placeholderLocation = location;
					view.doInputPopup("New Folder", "Please enter a new folder name.", "New Folder" , onFolderPopupSuccess, onPopupCancel);	
				}else if(model.itemExists(location)){
					if(location.charAt(location.length-1) == "\\")
						placeholderLocation = location;
					else
						placeholderLocation = location.substring(0, location.lastIndexOf("\\")+1);
					
					view.doInputPopup("New Folder", "Please enter a new folder name.", "New Folder" , onFolderPopupSuccess, onPopupCancel);	
				}else
					makeFolderValidate(location);
			}
		}
		
		protected function makeFolderValidate(location:String):void{
			if(location == "")
				view.errorLabel.text = "Your folder name cannot be blank.";
			else if(location.indexOf(",") != -1)
				view.errorLabel.text = "Your folder name cannot contain commas.";
			else{
				if(location.charAt(location.length-1) != '\\')
					location += '\\'; //Make it a folder path
				
				if(model.itemExists(location)){
					view.errorLabel.text = "A folder or file already exists at " + location;
				}else if(!model.validInsertionPath(location)){
					view.errorLabel.text = location + " is not a valid location.";
				}else{
					view.errorLabel.text = "Making folder...";
					dispatch(new GraphConfigEvent(GraphConfigEvent.CREATE_FOLDER,{"location":location}));
				}
			}
		}
		
		protected function onFolderPopupSuccess( event:PopupEvent ):void{			
			makeFolderValidate(placeholderLocation + event.result);
		}
		
		//--End code handling making new folders
		
		//--Start code handling deleting files/folders
		public function deleteConfigFiles( event:MouseEvent ):void{
			var locations:Array = view.fileNameInput.text.split(",");
			var success:Boolean = true;
			var promptString:String = "Are you sure you want to delete the following items?";
			deletionItems = new Array();
			for each( var location:String in locations ){
				if(!model.itemExists(location)){
					view.errorLabel.text = location + " does not exist.";
					success = false;
					break;
				}else{
					promptString += "\n" + location;
					deletionItems.push(location);
				}
			}
			
			if(success)
				view.doConfirmPopup("Delete?", promptString, onDeletePopupSuccess, onPopupCancel);	
		}
		
		protected function deleteConfigFilesValidate( items:Array ):void{
			view.errorLabel.text = "Deleting...";
			for each( var item:String in items )
				dispatch(new GraphConfigEvent(GraphConfigEvent.REMOVE,{"location":item}));
		}
		
		protected function onDeletePopupSuccess( event:PopupEvent ):void{
			deleteConfigFilesValidate(deletionItems);
		}
		
		//--End code handling deleting files/folders
		
		public function closeView( event:Event = null ):void{
			view.dispatchEvent(new CloseEvent(CloseEvent.CLOSE));
		}
	}
}