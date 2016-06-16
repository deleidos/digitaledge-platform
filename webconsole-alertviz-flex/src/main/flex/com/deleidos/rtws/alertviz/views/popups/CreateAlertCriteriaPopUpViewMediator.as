package com.deleidos.rtws.alertviz.views.popups
{	
	import com.adobe.serialization.json.JSON;
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.events.HTTPQueueEvent;
	import com.deleidos.rtws.alertviz.events.repository.ModelServiceEvent;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaEntry;
	import com.deleidos.rtws.alertviz.models.AlertDefinition;
	import com.deleidos.rtws.alertviz.models.HTTPQueueModel;
	import com.deleidos.rtws.alertviz.models.HTTPRequest;
	import com.deleidos.rtws.alertviz.models.repository.DataModelDirectory;
	import com.deleidos.rtws.alertviz.models.repository.DataModelName;
	import com.deleidos.rtws.alertviz.services.repository.RepositoryConstants;
	import com.deleidos.rtws.commons.model.properties.AppPropertiesModel;
	import com.deleidos.rtws.commons.model.properties.IAppPropertiesModel;
	import com.deleidos.rtws.commons.tenantutils.model.properties.SystemAppPropertiesModel;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.collections.XMLListCollection;
	import mx.controls.Alert;
	import mx.core.FlexGlobals;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.validators.Validator;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class CreateAlertCriteriaPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:CreateAlertCriteriaPopUpView;
		
		[Inject] public var appPropsModel:SystemAppPropertiesModel;
		
		[Inject]
		public var dmDirectory:DataModelDirectory;
		
		//data provider for the Data Grid
		private var criterion:ArrayCollection = new ArrayCollection();
		
		private var alertKey:String = null;
		
		private var alertDef:String = "";
		
		private var alertModel:String = "";
		
		
		public override function onRegister():void 
		{			
			view.createButton.addEventListener(MouseEvent.CLICK, handleCreateButton);
			view.cancelButton.addEventListener(MouseEvent.CLICK, handleCancelButton);
			view.closeButton.addEventListener(MouseEvent.CLICK, handleCancelButton);
			view.addDefButton.addEventListener(MouseEvent.CLICK, handleAddDefButton);
			view.addEventListener(AlertCriteriaCommandEvent.EDIT_DEF, handleEditDef);
			view.addEventListener(AlertCriteriaCommandEvent.REMOVE_DEF, handleRemoveDef);
			view.addEventListener(AlertCriteriaCommandEvent.CHANGE_MODEL, handleChangeModel);
			
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_DEF, handleSendDef);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.CREATE_SUCCESS, handleSuccess);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.CREATE_ERROR, handleError);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.ADD_DEF, handleAddDef);
			
			view.dataGroup.dataProvider = criterion;
			
			//shouldn't need to dispatch this event because it is already dispatched in InitPopUpViewMediator
			//dispatch(new ModelServiceEvent(ModelServiceEvent.LOAD_MODEL_DIR, {tenant:null, visibility:RepositoryConstants.PUBLIC}, null));
			//eventMap.mapListener(eventDispatcher, ModelServiceEvent.MODEL_DIR_LOADED, populateModels);
			
			populateModels();
		}
		
		public override function onRemove():void 
		{
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.ADD_DEF, handleAddDef);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.CREATE_ERROR, handleError);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.CREATE_SUCCESS, handleSuccess);
			
			view.removeEventListener(AlertCriteriaCommandEvent.CHANGE_MODEL, handleChangeModel);
			view.removeEventListener(AlertCriteriaCommandEvent.REMOVE_DEF, handleRemoveDef);
			view.removeEventListener(AlertCriteriaCommandEvent.EDIT_DEF, handleEditDef);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleAddDefButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleCancelButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleCreateButton);
		}
		
		/**
		 * populates the data model drop down list
		 */
		protected function populateModels():void
		{			
			//the data model list that was loaded into the directory
			var fileList:Array = dmDirectory.dataModelNames;
			
			//get each data model name and push it on the array
			for each (var entry:DataModelName in fileList) 
			{
				view.array.addItem(entry);
			}
			
			view.alertModelTextInput.selectedIndex = 0;
			
			if(view.array.length > 0)
			{
				view.addDefButton.enabled = true;
			}
		}
		
		protected function handleSuccess(event:AlertCriteriaCommandEvent):void
		{
			closeView();
		}
		
		protected function handleError(event:AlertCriteriaCommandEvent):void
		{
			view.createButton.enabled = true;
			
			var popup:AlertMessagePopUpView = new AlertMessagePopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
			
			
			var title:String = "Creation Failed";
			var message:String = "Unable to create alert criteria";
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.ALERT_MESSAGE, {title:title, message:message}));
		}
		
		/**
		 * handles when the CREATE button is clicked
		 */
		protected function handleCreateButton(event:MouseEvent):void 
		{
			var def:String = "{}";
			
			//for each definition in the DataGrid
			for(var i:int = 0; i<criterion.length; i++)
			{
				var criteria:Object = criterion.getItemAt(i);
				
				//convert object to a JSON string
				var newDef:String = convertToJSON(criteria.field,criteria.operator,criteria.value,criteria.type);
				
				if(def == "{}")
				{
					def = def.substring(0, def.length - 1) + newDef + "}";
				}
				else
				{
					//adds comma
					def = def.substring(0, def.length - 1) + ", " + newDef + "}";
				}
			}
			
			//invisible text area in the window
			alertDef = def;
			
			var validatorErrorArr:Array = Validator.validateAll(view.validators);
			var isFormValid:Boolean = (validatorErrorArr.length == 0);
			
			if (isFormValid) {				
				var entry:AlertCriteriaEntry = new AlertCriteriaEntry(alertKey, view.alertNameTextInput.text, 
					view.alertModelTextInput.selectedItem.toString(), view.alertDescTextInput.text, alertDef);
				
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.CREATE, {entry:entry}));
				
				view.createButton.enabled = false;
			}
		}
		
		/**
		 * converts user's data into a JSON string alert definition
		 */
		private function convertToJSON(field:String, operator:String, value:String, type:String):String
		{
			var out:String = "";
			var end:ArrayList = new ArrayList();
			
			var list:Array = field.split(".");
			for(var i:int = 0; i < list.length; i++)
			{
				var isArray:Boolean = false;
				var entry:String = list[i];
				if(entry.substring(entry.length - 2, entry.length) == "[]")
				{
					entry = entry.substring(0, entry.length - 2);
					isArray = true;
				}
				out += "\"" + entry + "\":";				
				if(isArray)
				{
					out += "[";
					end.addItemAt("]",0);
				}
				
				if(i < list.length - 1)
				{
					out += "{";
					end.addItemAt("}",0);
				}
			}
			
			out += "\"" + operator + " ";
			
			if(operator == "IN")
			{
				value = "(" + value + ")";
			}
			
			if(operator == "BETWEEN" || operator == "IN")
			{
				var valList:Array = value.substring(1, value.length - 1).split(",");
				out += value.substring(0,1);
				for(var k:int = 0; k<valList.length; k++)
				{
					out += "'" + valList[k] + "',";
				}
				out = out.substring(0, out.length - 1);
				out += value.substring(value.length-1, value.length);
			}
			else
			{
				if(type != "number")
				{
					out += "'" + value + "'";
				}
				else
				{
					out += value;
				}
			}
			
			out += "\"";
			
			for(var j:int = 0; j<end.length; j++)
			{
				out += end.getItemAt(j);
			}
			
			return out;
		}
		
		protected function handleCancelButton(event:MouseEvent):void 
		{
			closeView();
		}
		
		private function closeView():void 
		{
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
		
		/**
		 * handles when the Add Definition button is clicked
		 */
		protected function handleAddDefButton(event:MouseEvent):void
		{
			var popup:CreateAlertDefinitionPopUpView = new CreateAlertDefinitionPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
			
			//sends the name of the data model
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.SEND_MODEL, view.alertModelTextInput.selectedItem));
		}
		
		/**
		 * handles when an Alert Definition is received
		 * adds definition to the data grid
		 */
		protected function handleAddDef(event:AlertCriteriaCommandEvent):void
		{			
			//if this definition was edited
			if(event.parameters.newDef.wasEdited == true)
			{
				//remove the old definition
				criterion.removeItemAt(criterion.getItemIndex(event.parameters.oldDef));
				criterion.refresh();
			}
			
			//add the new definition
			criterion.addItem(event.parameters.newDef);
			view.alertDefTextInput.text = "valid";
			view.alertDefValidator.validate();
		}
		
		/**
		 * handles when the user chooses to edit an existing alert criteria
		 */
		protected function handleSendDef(event:AlertCriteriaCommandEvent):void
		{
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_DEF, handleSendDef);
			
			view.title = "Edit Alert Criteria";
			view.createButton.label = "EDIT";
			var alertDef:AlertCriteriaEntry = event.parameters as AlertCriteriaEntry;
			alertKey = alertDef.alertDefKey;
			view.alertNameTextInput.text = alertDef.alertDefName;			
			alertModel = alertDef.alertDefModel;
			
			//selects appropriate model when user is editing an alert criteria
			for(var i:int = 0; i<view.alertModelTextInput.dataProvider.length; i++)
			{
				var model:String = view.alertModelTextInput.dataProvider.getItemAt(i).toString();
				if(model.indexOf(alertModel) != -1)
				{
					view.alertModelTextInput.selectedItem = view.alertModelTextInput.dataProvider.getItemAt(i);
				}
			}
			
			view.alertDescTextInput.text = alertDef.alertDefDesc;
			var obj:Object = JSON.decode(alertDef.alertDef);
			criterion.addAll(generateCriteria(obj, new AlertDefinition()));			
		}
		
		private function generateCriteria(obj:Object, def:AlertDefinition):ArrayCollection
		{
			var array:ArrayCollection = new ArrayCollection();
			if(obj is Array)
			{
				//change the object to the array's first (and only) element	
				obj = obj[0];
			}
			
			//for every criteria in the object
			for(var property:String in obj)
			{				
				//name of the data field
				if(def.field != null)
				{
					def.field += "." + property;
				}
				else
				{
					def.field = property;
				}
				
				//if the value is an array, add "[]" to the end of the name
				if(obj[property] is Array)
				{
					def.field += "[]";
				}
				
				//if the value is not a string, then this data field has sub-fields
				if(!(obj[property] is String))
				{					
					//recursive call to this method to construct the an array of sub-fields
					//there should only ever be one element in the returned array
					def = generateCriteria(obj[property], def).getItemAt(0) as AlertDefinition;
				}
					
				//store the value of this data field
				else
				{
					var str:String = obj[property] as String;
					str.replace(" ","");
					var operators:Array = ["=","!=","LIKE",">","<",">=","<=","BETWEEN","IN"];
					for(var i:int = 0; i<operators.length; i++)
					{
						if(str.indexOf(operators[i]) == 0)
						{
							def.operator = operators[i];
						}
					}
					def.value = str.substring(def.operator.length);
					if(def.value.charAt(0) == " ")
					{
						def.value = def.value.substring(1);
					}
					if(def.value.charAt(def.value.length-1) == "'")
					{
						def.value = def.value.substring(1, def.value.length - 1);
					}
				}
				def.model = alertModel;
				array.addItem(def);
				def = new AlertDefinition();
			}
			return array;
		}
		
		/**
		 * handles when the edit button is clicked for an alert definition
		 */
		protected function handleEditDef(event:AlertCriteriaCommandEvent):void
		{
			var popup:CreateAlertDefinitionPopUpView = new CreateAlertDefinitionPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
			
			var obj:Object = {};
			obj.alertDef = event.parameters as AlertDefinition;
			obj.dataModel = view.alertModelTextInput.selectedItem;			
			
			//send the existing definitions data to be filled in
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.SEND_DEF, obj));
		}
		
		/**
		 * handles when the remove button is clicked for an alert definition
		 */
		protected function handleRemoveDef(event:AlertCriteriaCommandEvent):void
		{
			criterion.removeItemAt(criterion.getItemIndex(event.parameters));
			criterion.refresh();
			if(criterion.length == 0)
			{
				view.alertDefTextInput.text = "";
			}
			view.alertDefValidator.validate();
		}
		
		/**
		 * handles when the data model is changed
		 */
		protected function handleChangeModel(event:AlertCriteriaCommandEvent):void
		{
			criterion.removeAll();
			criterion.refresh();
			view.alertDefTextInput.text = "";
			view.alertDefValidator.validate();
		}
	}
}