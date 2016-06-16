package com.deleidos.rtws.alertviz.views.popups
{	
	import com.adobe.serialization.json.JSON;
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.events.HTTPQueueEvent;
	import com.deleidos.rtws.alertviz.events.repository.ModelServiceEvent;
	import com.deleidos.rtws.alertviz.json.OrderedJson;
	import com.deleidos.rtws.alertviz.json.ParseString;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaEntry;
	import com.deleidos.rtws.alertviz.models.AlertDefinition;
	import com.deleidos.rtws.alertviz.models.HTTPQueueModel;
	import com.deleidos.rtws.alertviz.models.HTTPRequest;
	import com.deleidos.rtws.alertviz.models.repository.DataModelName;
	import com.deleidos.rtws.alertviz.models.repository.ModelFieldData;
	import com.deleidos.rtws.alertviz.services.repository.DataModelZipFile;
	import com.deleidos.rtws.commons.tenantutils.model.properties.SystemAppPropertiesModel;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.text.TextField;
	import flash.utils.ByteArray;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.controls.TextInput;
	import mx.core.FlexGlobals;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.validators.Validator;
	
	import nochump.util.zip.ZipEntry;
	import nochump.util.zip.ZipFile;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class BrowseDataModelPopUpViewMediator extends Mediator
	{
		public var jsonObj:Object;
		
		[Inject]
		public var dataModel:ModelFieldData;
		
		[Inject]
		public var view:BrowseDataModelPopUpView;
		
		[Inject] public var appPropsModel:SystemAppPropertiesModel;
		
		//describes the type to filter by if the user typed "@"
		private var type:String = "null";
		
		private var path:String = "";
		
		private var possibleFields:ArrayCollection = new ArrayCollection();
				
		public override function onRegister():void 
		{
			view.openButton.addEventListener(MouseEvent.CLICK, handleOpenButton);
			view.cancelButton.addEventListener(MouseEvent.CLICK, handleCancelButton);
			view.tree.addEventListener(MouseEvent.DOUBLE_CLICK, handleDoubleClick);
			view.closeButton.addEventListener(MouseEvent.CLICK, handleCancelButton);
			
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_VAL, handleSendVal);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_MODEL, handleSendModel);
		}
		
		public override function onRemove():void 
		{			
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_MODEL, handleSendModel);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.BROWSE_CANCEL, handleBrowseCancel);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_VAL, handleSendVal);
			
			view.tree.removeEventListener(MouseEvent.DOUBLE_CLICK, handleDoubleClick);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleCancelButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleOpenButton);
		}
		
		/**
		 * makes a heirarchial object for the data tree to be able to display
		 */
		private function makeHierarchicalObj(obj:Object, hObj:Array):Array
		{
			if(obj is Array)
			{
				//change the object to the array's first (and only) element	
				obj = obj[0];
			}

			//for every data field in the object
			for(var property:String in obj)
			{
				var tempObj:Object = new Object();
				
				//name of the data field
				tempObj.label = property;
				
				//if the value is an array, add "[]" to the end of the name
				if(obj[property] is Array)
				{
					tempObj.label += "[]";
				}
				
				//if the value is not a string, then this data field has children
				if(!(obj[property] is String))
				{
					tempObj.children = new Array();
					
					//recursive call to this method to construct the children for this object
					tempObj.children = makeHierarchicalObj(obj[property], tempObj.children);
				}
				
				//store the value of this data field
				else
				{
					tempObj.data = obj[property];
					if(path != "")
					{
						var list:Array = path.split(".");
						if(list[list.length-1] == property)
						{
							possibleFields.addItem(tempObj);
						}
					}
				}
				
				//push the object onto an array representing the entire heirarchial object
				if(type == "null")
				{
					hObj.push(tempObj);
				}
				
				//filter the object by type
				else if(tempObj.data == type)
				{
					hObj.push(tempObj);
				}
				
				//if the object has more than zero children
				else if(tempObj.hasOwnProperty("children"))
				{
					if(tempObj.children.length > 0)
					{
						hObj.push(tempObj)
					}
				}
			}
			
			//SORTING ALGORITHM
			//create arrays that will store object with children and objects without children
			var childList:Array = new Array();
			var regList:Array = new Array();
			
			//loop through the heirarchial array
			for(var j:int = 0; j<hObj.length; j++)
			{
				//if the object has children
				if(hObj[j].children != null)
				{
					//push it to the child array
					childList.push(hObj[j]);
				}
				
				//if the object does not have children
				else
				{
					//push it to the regular array
					regList.push(hObj[j]);
				}
			}
			
			//sort both arrays
			childList.sortOn("label", Array.CASEINSENSITIVE);
			regList.sortOn("label", Array.CASEINSENSITIVE);
			
			//clear the heirarchial array
			hObj = new Array();
			
			//set the heirarchial array to the sorted list
			hObj = hObj.concat(childList,regList);
			return hObj;
		}
		
		/**
		 * executes when the OPEN button is clicked
		 * builds the file path the user selected
		 */
		protected function handleOpenButton(event:MouseEvent):void 
		{
			//list that stores the file path
			var list:ArrayList = new ArrayList();
			
			//add the selected item to the list
			list.addItem(view.tree.selectedItem);
			
			//save the current selected item and its data type
			var currentItem:Object = view.tree.selectedItem;
			var dataType:String = currentItem.data;
			
			//loop through and get all the parents of the selected item
			while(view.tree.getParentItem(currentItem) != null)
			{
				//add the parent to the beginning of the list
				list.addItemAt(view.tree.getParentItem(currentItem), 0);
				
				//set the current item as the parent
				currentItem = view.tree.getParentItem(currentItem);
			}
			
			//path to return
			var path:String = "";
			
			//for each item in the file list
			for(var i:int = 0; i<list.length; i++)
			{
				//separate the items with a "."
				path += list.getItemAt(i).label + "."
			}
			
			//remove the last "." from the path
			path = path.substring(0, path.length - 1);
			
			//create an object with a path and data type to return
			var obj:Object = new Object();
			obj.path = path;
			obj.type = dataType;
			
			//dispatch the correct event containing the path and data type
			//if the user typed "@" and this will be in the value
			if(type != "null")
			{
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.RETURN_VAL, obj));
			}
			
			//if this will be in the field
			else
			{
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.SEND_PATH, obj));
			}
			closeView();
		}
		
		/**
		 * handles when the data type is sent in order to filter the users choices
		 */
		protected function handleSendVal(event:AlertCriteriaCommandEvent):void
		{
			type = event.parameters.toString();
		}
		
		/**
		 * handles when the cancel button is clicked
		 */
		protected function handleCancelButton(event:MouseEvent):void 
		{
			//if the user typed the "@" symbol then clicked cancel
			if(type != "null")
			{
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.RETURN_VAL, false));
			}
			
			//if the user clicked cancel before selecting a field
			else
			{
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.BROWSE_CANCEL, true));
			}
			closeView();
		}
		
		private function closeView():void 
		{
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
		
		/**
		 * handles when a user double clicks on an item in the tree
		 */
		protected function handleDoubleClick(event:MouseEvent):void
		{
			var node:Object = view.tree.selectedItem;
			var isOpen:Boolean = view.tree.isItemOpen(node);
			
			//opens or closes the folder
			view.tree.expandItem(node, !isOpen);
			
			//if the item that was double clicked was not a folder
			if(node.children == null)
			{
				//clicks the open button for the user
				handleOpenButton(new MouseEvent(MouseEvent.CLICK));
			}
		}
		
		/**
		 * executes when a BROWSE_CANCEL event is caught
		 * closes the window
		 */
		protected function handleBrowseCancel(event:AlertCriteriaCommandEvent):void
		{
			closeView();
		}
		
		/**
		 * handles when a data model name is received
		 */
		protected function handleSendModel(event:AlertCriteriaCommandEvent):void
		{
			//the data model file name
			var dmFilename:DataModelName = event.parameters.model;
			
			if(event.parameters.path != "")
			{
				path = event.parameters.path.toString();
			}
			else
			{
				eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.BROWSE_CANCEL, handleBrowseCancel);
			}
			
			dispatch(new ModelServiceEvent(ModelServiceEvent.LOAD_MODEL, {"dataModelName":dmFilename}, null));
			eventMap.mapListener(eventDispatcher, ModelServiceEvent.MODEL_LOADED, saveFile);
		}
		
		/**
		 * executes once the ReSt call to retrieve the contents of a data model has finished loading
		 */
		protected function saveFile(e:Event):void
		{
			//makes the heirarchial object for the data tree to display
			var hObj:Array = new Array();
			var obj:Object = dataModel.jsonObj;
			hObj = makeHierarchicalObj(obj, hObj);
			
			
			
			//sets the data tree's data provider to the heirarchial array
			view.tree.dataProvider = hObj;
			
			if(possibleFields.length != 0)
			{
				//expand all folders so that getParentItem function doesn't return null
				var array:ArrayCollection = view.tree.dataProvider as ArrayCollection;
				
				for(var j:int = 0; j<array.length; j++)
				{
					view.tree.selectedItem = array.getItemAt(j);
					view.tree.validateNow();
					view.tree.expandChildrenOf(view.tree.selectedItem, true);
					view.tree.validateNow();
				}

				//out of all possible matches, selects the correct one
				for(var i:int = 0; i<possibleFields.length; i++)
				{
					var possField:Object = possibleFields.getItemAt(i);
					var pathName:String = possField.label;
					view.tree.selectedItem = possField;
					while(view.tree.getParentItem(view.tree.selectedItem) != null)
					{
						var parent:Object = view.tree.getParentItem(view.tree.selectedItem);
						pathName = parent.label + "." + pathName;
						view.tree.selectedItem = parent;
					}
					if(path == pathName)
					{
						view.tree.selectedItem = possField;
					}
				}
				
				//opens the field which will set all data values (specifically the type)
				handleOpenButton(null);
			}
		}	
		
	}
}