package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.events.HTTPQueueEvent;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaEntry;
	import com.deleidos.rtws.alertviz.models.AlertDefinition;
	import com.deleidos.rtws.alertviz.models.HTTPQueueModel;
	import com.deleidos.rtws.alertviz.models.HTTPRequest;
	import com.deleidos.rtws.alertviz.models.repository.DataModelName;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.events.TextEvent;
	import flash.globalization.DateTimeFormatter;
	import flash.globalization.LocaleID;
	
	import mx.collections.ArrayCollection;
	import mx.collections.ArrayList;
	import mx.controls.Button;
	import mx.controls.ComboBox;
	import mx.controls.TextInput;
	import mx.core.UIComponent;
	import mx.events.CloseEvent;
	import mx.events.ValidationResultEvent;
	import mx.formatters.DateFormatter;
	import mx.managers.PopUpManager;
	import mx.messaging.messages.ErrorMessage;
	import mx.validators.NumberValidator;
	import mx.validators.StringValidator;
	import mx.validators.Validator;
	
	import org.robotlegs.mvcs.Mediator;
	
	import spark.components.DropDownList;
	
	public class CreateAlertDefinitionPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:CreateAlertDefinitionPopUpView;
		
		//used to dynamically create the dropdowns for the BETWEEN operator
		private var leftInclusive:DropDownList;
		private var rightInclusive:DropDownList;
		private var oldDefinition:AlertDefinition;
		private var dataModel:DataModelName;
						
		public override function onRegister():void 
		{
			//cycles through all the states so that every component gets created and is not null
			view.currentState = "datestring";
			view.currentState = "default";
						
			view.addButton.addEventListener(MouseEvent.CLICK, handleAddButton);
			view.helpButton.addEventListener(MouseEvent.CLICK, handleHelpButton);
			view.formatHelpButton.addEventListener(MouseEvent.CLICK, handleFormatHelpButton);
			view.browseButton.addEventListener(MouseEvent.CLICK, handleBrowseButton);
			view.testButton.addEventListener(MouseEvent.CLICK, handleTestButton);
			view.operatorTextInput.addEventListener(Event.CHANGE, handleOperatorChange);
			view.cancelButton.addEventListener(MouseEvent.CLICK, handleCancelButton);
			view.closeButton.addEventListener(MouseEvent.CLICK, handleCancelButton);
			
			view.valueTextInput.addEventListener(TextEvent.TEXT_INPUT, handleTextInput);
						
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_DEF, handleSendDef);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_PATH, handleSendPath);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.RETURN_VAL, handleReturnVal);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.BROWSE_CANCEL, handleBrowseCancel);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.IGNORE, handleIgnore);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_MODEL, handleSendModel);
			
			view.operatorTextInput.selectedIndex = 0;
		}
		
		public override function onRemove():void 
		{
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.IGNORE, handleIgnore);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.BROWSE_CANCEL, handleBrowseCancel);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.RETURN_VAL, handleReturnVal);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_PATH, handleSendPath);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_DEF, handleSendDef);
			
			view.valueTextInput.removeEventListener(TextEvent.TEXT_INPUT, handleTextInput);
			
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleCancelButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleOperatorChange);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleTestButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleBrowseButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleFormatHelpButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleHelpButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleAddButton);
		}
		
		/**
		 * validates and dispatches an event to add an alert definition to the Data Grid
		 */
		protected function handleAddButton(event:MouseEvent):void 
		{
			var isFormValid:Boolean = true;
			var isDateValid:Boolean = true;
			
			//clear all of the validators
			for each (var validator:Validator in view.validators)
			{                     
				validator.dispatchEvent(new ValidationResultEvent(ValidationResultEvent.VALID));
			}
			
			//datestring validation
			if(view.dateValueValidator.enabled && view.operatorTextInput.selectedItem as String != "LIKE" && view.valueTextInput.text.substring(0,1) != "@" && event.type != "ignore")
			{				
				var dateResult:ValidationResultEvent = view.dateValueValidator.validate();
				isFormValid = isFormValid && (dateResult.type == ValidationResultEvent.VALID);
				isDateValid = (dateResult.type == ValidationResultEvent.VALID)
			}
			
			//number validation
			if(view.numberValueValidator.enabled && view.valueTextInput.text.substring(0,1) != "@")
			{
				var numResult:ValidationResultEvent = view.numberValueValidator.validate();
				isFormValid = isFormValid && (numResult.type == ValidationResultEvent.VALID);
			}
			
			//comma separation validation
			if(view.commaSeparatedValidator.enabled)
			{
				var commaResult:ValidationResultEvent = view.commaSeparatedValidator.validate();
				isFormValid = isFormValid && (commaResult.type == ValidationResultEvent.VALID);
			}
			
			//value validation
			var valResult:ValidationResultEvent = view.valueValidator.validate();
			isFormValid = isFormValid && (valResult.type == ValidationResultEvent.VALID);			
			
			if (isFormValid) 
			{
				//create a new AlertDefinition to add to the DataGrid
				var entry:AlertDefinition = new AlertDefinition();
				entry.field = view.fieldTextInput.text;
				entry.operator = view.operatorTextInput.selectedItem as String;
				if(view.operatorTextInput.selectedItem as String == "BETWEEN")
				{
					entry.value = leftInclusive.selectedItem + view.valueTextInput.text + rightInclusive.selectedItem;
				}
				else
				{
					entry.value = view.valueTextInput.text;
				}
				entry.type = view.typeLabel.text;
				if(view.addButton.label == "EDIT")
				{
					entry.wasEdited = true;
				}
				entry.model = dataModel.name;
				
				//send the AlertDefinition back to the DataGrid
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.ADD_DEF, {newDef:entry, oldDef:oldDefinition}));
				closeView();
			}
			
			//if the data is invalid, create the suggestion box
			else if (isDateValid == false)
			{
				var popup:CreateAlertValueSuggestionPopUpView = new CreateAlertValueSuggestionPopUpView();
				
				PopUpManager.addPopUp(popup, contextView, true);
				PopUpManager.centerPopUp(popup);
				
				mediatorMap.createMediator(popup);
				
				var formatter:DateTimeFormatter = new DateTimeFormatter(LocaleID.DEFAULT);
				formatter.setDateTimePattern(view.typeLabel.text.substring(11));
				
				var toSend:Object = new Object();
				toSend.expression = view.valueTextInput.text;
				toSend.formatter = formatter;
				
				//sends user input and datestring format to suggestion box
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.SEND_VAL, toSend));
			}
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
		 * fills in the data when an alert definition is being edited
		 */
		protected function handleSendDef(event:AlertCriteriaCommandEvent):void
		{
			//remove the listener that listens for the data model because the model is contained in the
			//event parameter
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_MODEL, handleSendModel);
			
			//closes the browse window that is automatically opened when this window is created
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.BROWSE_CANCEL, false));
			
			oldDefinition = event.parameters.alertDef as AlertDefinition;
			
			view.fieldTextInput.text = event.parameters.alertDef.field;
			
			for(var i:int = 0; i<view.array.length; i++)
			{
				if(view.array.getItemAt(i) == event.parameters.alertDef.operator)
				{
					view.operatorTextInput.selectedIndex = i;
				}
			}
			
			//configure validators and settings for specific operators
			handleOperatorChange(new Event(Event.CHANGE));
			
			//sets the values of the BETWEEN drop down boxes
			if(view.operatorTextInput.selectedItem as String == "BETWEEN")
			{
				var value:String = event.parameters.alertDef.value;
				view.valueTextInput.text = value.substring(1,value.length - 1);
				leftInclusive.selectedItem = value.substring(0,1);
				rightInclusive.selectedItem = value.substring(value.length - 1, value.length);
			}
			else
			{
				view.valueTextInput.text = event.parameters.alertDef.value;
			}
			
			dataModel = event.parameters.dataModel;
			view.typeLabel.text = event.parameters.alertDef.type;
			if(view.typeLabel.text == null)
			{
				handleBrowseButton(null); //sets view.typeLabel.text
			}
			else
			{			
				var entry:Object = new Object();
				entry.path = event.parameters.alertDef.field;
				entry.type = event.parameters.alertDef.type;
				
				//configure the validators
				handleSendPath(new AlertCriteriaCommandEvent("", entry));
			}
			
			//change the ADD button to an EDIT button
			view.addButton.label = "EDIT";
		}
		
		/**
		 * configures validators when the field is changed
		 */
		protected function handleSendPath(event:AlertCriteriaCommandEvent):void
		{
			//clear all validators
			for each (var validator:Validator in view.validators)
			{                     
				validator.dispatchEvent(new ValidationResultEvent(ValidationResultEvent.VALID));
			}
			
			//if the field changed, clear the value
			if(view.fieldTextInput.text != event.parameters.path && view.fieldTextInput.text != "")
			{
				view.valueTextInput.text = "";
			}
			
			//change the field
			view.fieldTextInput.text = event.parameters.path;
			
			//validate the field and allow the value to be edited
			if(view.fieldTextInput.text != "")
			{
				view.valueTextInput.editable = true;
			}
			
			//set the type label. this is an invisible component
			var type:String = event.parameters.type;
			view.typeLabel.text = type;
			
			//configures validators for the string type
			if(type == "string")
			{
				view.currentState = "default";
				view.height = 215;
				view.numberValueValidator.enabled = false;
				view.dateValueValidator.enabled = false;
				view.valueValidator.enabled = true;
				if(view.operatorTextInput.selectedItem as String == "IN")
				{
					view.commaSeparatedValidator.expression = "^\\s*.+[.\\s]*(,\\s*.+[.\\s]*)*$";
					view.commaSeparatedValidator.noMatchError = "Must be comma separated list";
				}
				if(view.operatorTextInput.selectedItem as String == "BETWEEN")
				{
					view.commaSeparatedValidator.expression = "^\\s*.+[.\\s]*,\\s*.+[.\\s]*$";
					view.commaSeparatedValidator.noMatchError = "Must be two values separated by a comma";
				}
			}
			
			//configures validators for the number type
			if(type == "number")
			{
				view.currentState = "default";
				view.height = 215;
				if(view.operatorTextInput.selectedItem as String == "IN")
				{
					view.commaSeparatedValidator.expression = "^\\s*(-?[0-9]+(\.[0-9]+)?|@.+)\\s*(,\\s*(-?[0-9]+(\.[0-9]+)?|@.+)\\s*)*$";
					view.commaSeparatedValidator.noMatchError = "Must be comma separated list of numbers";
				}
				if(view.operatorTextInput.selectedItem as String == "BETWEEN")
				{
					view.commaSeparatedValidator.expression = "^\\s*(-?[0-9]+(\.[0-9]+)?|@.+)\\s*,\\s*(-?[0-9]+(\.[0-9]+)?|@.+)\\s*$";
					view.commaSeparatedValidator.noMatchError = "Must be two numbers separated by a comma";
				}
				if(view.operatorTextInput.selectedItem as String != "IN" && view.operatorTextInput.selectedItem as String != "BETWEEN")
				{
					view.numberValueValidator.enabled = true;
					view.dateValueValidator.enabled = false;
					view.valueValidator.enabled = false;
					if(view.valueTextInput.text != "")
					{
						view.numberValueValidator.validate();
					}
				}
			}
			
			//configures validators for datestring type
			if(type.indexOf("datestring") != -1)
			{
				view.currentState = "datestring";
				
				//gets the regex expression for the datestring
				view.dateValueValidator.expression = getDatestringRegex();
								
				if(view.operatorTextInput.selectedItem as String != "IN" && view.operatorTextInput.selectedItem as String != "BETWEEN")
				{
					view.numberValueValidator.enabled = false;
					view.dateValueValidator.enabled = true;
					view.valueValidator.enabled = false;
				}
				if(view.operatorTextInput.selectedItem as String == "IN")
				{
					var datesRegex:String = getDatestringRegex();
					view.commaSeparatedValidator.expression = "^\\s*(" + datesRegex + "|@.+)\\s*(,\\s*(" + datesRegex + "|@.+)\\s*)*$";
					view.commaSeparatedValidator.noMatchError = "Must be comma separated list of formatted dates";
				}
				if(view.operatorTextInput.selectedItem as String == "BETWEEN")
				{
					var dateRegex:String = getDatestringRegex();
					view.commaSeparatedValidator.expression = "^\\s*(" + dateRegex + "|@.+)\\s*,\\s*(" + dateRegex + "|@.+)\\s*$";
					view.commaSeparatedValidator.noMatchError = "Must be two formatted dates separated by a comma";
				}
				
				var dateString:Array = type.split(" ");
				var dateFormat:String = "";
				var formatter:DateTimeFormatter = new DateTimeFormatter(LocaleID.DEFAULT);
				
				for(var i:int = 1; i<dateString.length; i++)
				{
					dateFormat += dateString[i] + " ";
				}
				dateFormat = dateFormat.substring(0,dateFormat.length - 1);
				formatter.setDateTimePattern(dateFormat);
				view.formatLabel.htmlText = "<b>Format:</b> " + dateFormat + " | <b>Current Time Example:</b> " + formatter.format(new Date());
				view.height = 240;
			}
		}
		
		/**
		 * creates the Operator Help window
		 */
		protected function handleHelpButton(event:MouseEvent):void
		{
			var popup:CreateAlertDefinitionHelpPopUpView = new CreateAlertDefinitionHelpPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
		}
		
		/**
		 * creates the Browse Data Model window
		 */
		protected function handleBrowseButton(event:MouseEvent):void
		{
			var popup:BrowseDataModelPopUpView = new BrowseDataModelPopUpView();
			
			if(event != null)
			{
				PopUpManager.addPopUp(popup, contextView, true);
				PopUpManager.centerPopUp(popup);
			}
			
			mediatorMap.createMediator(popup);
			
			//sends the model so it knows what model to open and load
			var field:String = "";
			if(event == null)
			{
				field = view.fieldTextInput.text;
			}
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.SEND_MODEL, {model:dataModel,path:field}));
		}
		
		/**
		 * handles when values are returned from other pop-up windows
		 */
		protected function handleReturnVal(event:AlertCriteriaCommandEvent):void
		{
			//this value is either sent from the test wildcard window or the date format suggestion window
			//happens when the user wants to use their wildcard expression or the suggested date
			if(event.parameters is String)
			{
				view.valueTextInput.text = event.parameters.toString();
			}
			
			//when a value is returned from the browse data model window
			//when the user wants to compare two fields
			else
			{
				var index:int = view.valueTextInput.selectionAnchorPosition;
				
				//the user typed "@" and hit cancel button on browse window
				if(event.parameters is Boolean)
				{
					//fixes cursor position
					view.valueTextInput.selectRange(index, index);
				}
				
				//the user typed "@" and accepted a datafield in the data model
				else
				{
					//inputs the datafield in the value input and fixes cursor position
					view.valueTextInput.text = view.valueTextInput.text.slice(0,index) + event.parameters.path + view.valueTextInput.text.slice(index);
					view.valueTextInput.selectRange(index + event.parameters.path.length, index + event.parameters.path.length);
				}
			}
			
			//clears the date validator
			view.dateValueValidator.dispatchEvent(new ValidationResultEvent(ValidationResultEvent.VALID));
		}
		
		/**
		 * creates the Datestring format help window
		 */
		protected function handleFormatHelpButton(event:MouseEvent):void
		{
			var popup:FormatHelpPopUpView = new FormatHelpPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
		}
		
		/**
		 * configures validators when operator is changed
		 * adds and removes dropdown boxes for BETWEEN operator
		 * enables/disables test wildcard button for LIKE operator
		 */
		protected function handleOperatorChange(event:Event):void
		{
			//clear the validators
			for each (var validator:Validator in view.validators)
			{                     
				validator.dispatchEvent(new ValidationResultEvent(ValidationResultEvent.VALID));
			}
			
			//create the comboboxes if the operator was changed to BETWEEN
			//enables/disables/configures different validators
			if(view.operatorTextInput.selectedItem as String == "BETWEEN")
			{
				leftInclusive = new DropDownList();
				leftInclusive.dataProvider = new ArrayCollection(new Array("(","["));
				leftInclusive.requireSelection = true;
				leftInclusive.width = 50;
				
				rightInclusive = new DropDownList();
				rightInclusive.dataProvider = new ArrayCollection(new Array(")","]"));
				rightInclusive.requireSelection = true;
				rightInclusive.width = 50;
				
				view.valueBox.addElementAt(leftInclusive, 0);
				view.valueBox.addElement(rightInclusive);
				
				view.numberValueValidator.enabled = false;
				view.dateValueValidator.enabled = false;
				view.commaSeparatedValidator.enabled = true;
				if(view.typeLabel.text == "number")
				{
					view.commaSeparatedValidator.expression = "^\\s*(-?[0-9]+(\.[0-9]+)?|@.+)\\s*,\\s*(-?[0-9]+(\.[0-9]+)?|@.+)\\s*$";
					view.commaSeparatedValidator.noMatchError = "Must be two numbers separated by a comma";
				}
				if(view.typeLabel.text == "string")
				{
					view.commaSeparatedValidator.expression = "^\\s*.+[.\\s]*,\\s*.+[.\\s]*$";
					view.commaSeparatedValidator.noMatchError = "Must be two values separated by a comma";
				}
				else
				{
					var dateRegex:String = getDatestringRegex();
					view.commaSeparatedValidator.expression = "^\\s*(" + dateRegex + "|@.+)\\s*,\\s*(" + dateRegex + "|@.+)\\s*$";
					view.commaSeparatedValidator.noMatchError = "Must be two formatted dates separated by a comma";	
				}
			}
			
			//removes comboboxes (if they exist) if the operator is not BETWEEN
			else if(view.valueBox.numElements == 3)
			{
				view.valueBox.removeElementAt(0);
				view.valueBox.removeElementAt(1);
			}
			
			//enables/disables/configures different validators
			if(view.operatorTextInput.selectedItem as String == "IN")
			{
				view.numberValueValidator.enabled = false;
				view.dateValueValidator.enabled = false;
				view.commaSeparatedValidator.enabled = true;
				if(view.typeLabel.text == "number")
				{
					view.commaSeparatedValidator.expression = "^\\s*(-?[0-9]+(\.[0-9]+)?|@.+)\\s*(,\\s*(-?[0-9]+(\.[0-9]+)?|@.+)\\s*)*$";
					view.commaSeparatedValidator.noMatchError = "Must be comma separated list of numbers";
				}
				if(view.typeLabel.text == "string")
				{
					view.commaSeparatedValidator.expression = "^\\s*.+[.\\s]*(,\\s*.+[.\\s]*)*$";
					view.commaSeparatedValidator.noMatchError = "Must be comma separated list";
				}
				else
				{
					var datesRegex:String = getDatestringRegex();
					view.commaSeparatedValidator.expression = "^\\s*(" + datesRegex + "|@.+)\\s*(,\\s*(" + datesRegex + "|@.+)\\s*)*$";
					view.commaSeparatedValidator.noMatchError = "Must be comma separated list of formatted dates";
				}
			}
			
			//enables/disables/configures different validators
			if(view.operatorTextInput.selectedItem as String != "IN" && view.operatorTextInput.selectedItem as String != "BETWEEN")
			{
				view.commaSeparatedValidator.enabled = false;
				var entry:Object = new Object();
				entry.path = view.fieldTextInput.text;
				entry.type = view.typeLabel.text;
				
				//configure validators
				handleSendPath(new AlertCriteriaCommandEvent("", entry));
			}
			
			//enable/disable the test wildcard button
			if(view.operatorTextInput.selectedItem as String == "LIKE")
			{
				view.testButton.visible = true;
			}
			else
			{
				view.testButton.visible = false;
			}
		}
		
		/**
		 * creates the Test wildcard window
		 */
		protected function handleTestButton(event:MouseEvent):void
		{
			var popup:TestWildCardPopUpView = new TestWildCardPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
		}
		
		/**
		 * called whenever the value text is changed. detects and handles when user inputs the "@" symbol
		 */
		protected function handleTextInput(event:TextEvent):void
		{
			//if the user typed "@"
			if(event.text == "@")
			{
				//opens the browse window
				handleBrowseButton(new MouseEvent(MouseEvent.CLICK));
				var type:String = view.typeLabel.text;
				
				//sends the type so the browser can filter the available options
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.SEND_VAL, type));
			}
		}
		
		/**
		 * 
		 */
		private function getDatestringRegex():String
		{
			//the datestring format
			var format:String = view.typeLabel.text.substring(11);
			
			//the reges to return
			var regex:String = "^";
			var lastChar:String = "";
			var count:int = 0;
			
			//specifies when the format is quoted and when it isn't
			var quoted:Boolean = false;
			
			//looks for repeated letters such as MM or DD or YYYY
			for(var i:int = 0; i<format.length; i++)
			{
				//enters the loop
				if(lastChar == "")
				{
					lastChar = format.charAt(i);
					count = 1;
				}
				
				//if the current character is different than the last character
				else if(format.charAt(i) != lastChar)
				{
					//if the character is  a single quote and the number of single quotes is odd,
					//then the "quoted" status needs to be reversed
					if(lastChar == "'" && count%2==1)
					{
						quoted = !quoted;
					}
					
					//add regex for the specific character and count
					regex += lookupRegex(lastChar, count, quoted);
					
					//get the new current character and set count
					lastChar = format.charAt(i);
					count = 1;
				}
				
				//if the current character matches the last character, add to the count
				else
				{
					count++;
				}
			}
						
			//lookup the regex for the last character and count
			regex += lookupRegex(lastChar, count, quoted);
			regex += "$"
			
			return regex;
		}
		
		/**
		 * looks up the regex for a specified character and count
		 * example: MM has the character M and count 2
		 * example: YYYY has the character Y and count 4
		 */
		private function lookupRegex(char:String, count:int, quoted:Boolean):String
		{
			//handes special characters
			switch(char)
			{
				case "'":
					for(var j:int = 1; j<Math.floor(count/2); j++)
					{
						char += char;
					}
					return char;
					
				case "[":
					return "\\[";
					
				case "\\":
					return "\\\\";
					
				case "^":
					return "\\^";
					
				case "$":
					return "\\$";
					
				case ".":
					return "\\.";
					
				case "|":
					return "\\|";
					
				case "?":
					return "\\?";
					
				case "*":
					return "\\*";
					
				case "+":
					return "\\+";
					
				case "(":
					return "\\(";
					
				case ")":
					return "\\)";
					
				case "{":
					return "\\{";
					
				case "}":
					return "\\}";
			}
			
			//handle unquoted characters
			if(quoted == false)
			{
				switch(char)
				{
					case "G":
						return "(.*)"
						
					case "y":
						if(count == 1)
							return "([0-9]+)";
						if(count == 2)
							return "([0-9]{2})";
						if(count == 4)
							return "([0-9]{4})";
						
					case "M":
						if(count == 1)
							return "([1-9]|1[0-2])";
						if(count == 2)
							return "(0[1-9]|1[0-2])";
						if(count == 3)
							return "(Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec)";
						if(count == 4)
							return "(January|February|March|April|May|June|July|August|September|October|November|December)";
						
					case "d":
						if(count == 1)
							return "([1-9]|[1-2][0-9]|3[0-1])";
						if(count == 2)
							return "(0[1-9]|[1-2][0-9]|3[0-1])";
						
					case "E":
						if(count < 4)
							return "(Mon|Tue|Wed|Thu|Fri|Sun|Sat)";
						if(count == 4)
							return "(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday|Sunday)";
						
					case "w":
						if(count == 1)
							return "([1-9]|[1-4][0-9]|5[0-2])";
						if(count == 2)
							return "(0[1-9]|[1-4][0-9]|5[0-2])";
						
					case "W":
						return "([1-5])";
						
					case "D":
						if(count == 1)
							return "([1-9]|[1-9][0-9]|[1-2][0-9][0-9]|3[0-5][0-9]|36[0-6])";
						if(count == 2)
							return "(0[1-9]|[1-9][0-9]|[1-2][0-9][0-9]|3[0-5][0-9]|36[0-6])";
						if(count == 3)
							return "(00[1-9]|0[1-9][0-9]|[1-2][0-9][0-9]|3[0-5][0-9]|36[0-6])";
						
					case "F":
						return "([1-5])";
						
					case "a":
						return "(AM|PM)";
						
					case "h":
						if(count == 1)
							return "([1-9]|1[0-2])";
						if(count == 2)
							return "(0[1-9]|1[0-2])";
						
					case "H":
						if(count == 1)
							return "([0-9]|1[0-9]|2[0-3])";
						if(count == 2)
							return "(0[0-9]|1[0-9]|2[0-3])";
						
					case "K":
						if(count == 1)
							return "([0-9]|1[0-1])";
						if(count == 2)
							return "(0[0-9]|1[0-1])";
						
					case "k":
						if(count == 1)
							return "([1-9]|1[0-9]|2[0-4])";
						if(count == 2)
							return "(0[1-9]|1[0-9]|2[0-4])";
						
					case "m":
						if(count == 1)
							return "([0-9]|[1-5][0-9])";
						if(count == 2)
							return "(0[0-9]|[1-5][0-9])";
						
					case "s":
						if(count == 1)
							return "([0-9]|[1-5][0-9])";
						if(count == 2)
							return "(0[0-9]|[1-5][0-9])";
						
					case "S":
						if(count == 1)
							return "([0-9]+)";
						if(count == 2)
							return "([0-9]{2, })";
						if(count == 3)
							return "([0-9]{3, })";
						if(count == 4)
							return "([0-9]{4, })";
						if(count == 5)
							return "([0-9]{5, })";
						
					case "z":
						return "(.*)";
						
					case "Z":
						return "(.*)";
				}
			}
			
			//handle when characters are quoted
			for(var i:int = 1; i<count; i++)
			{
				char += char;
			}
			return char;
		}
		
		/**
		 * handles when the cancel button is clicked on the browse data model window
		 */
		protected function handleBrowseCancel(event:AlertCriteriaCommandEvent):void
		{
			//closes this window if the user fails to select a field. a field must always be selected
			//so that validators are configured in order to complete the rest of the alert definition
			if(view.fieldTextInput.text == "" && event.parameters == true)
			{
				closeView();
			}
		}
		
		/**
		 * handles when the user clicks ignore in the datestring suggestion box
		 */
		protected function handleIgnore(event:AlertCriteriaCommandEvent):void
		{
			//clicks the add button and tells it to turn off the datestring validator
			handleAddButton(new MouseEvent("ignore"));
		}
		
		/**
		 * handles when the data model is received
		 */
		protected function handleSendModel(event:AlertCriteriaCommandEvent):void
		{
			//sets the model label which is invisible
			dataModel = event.parameters as DataModelName;
			
			//unmaps the listener because the same event will get fired when the browse window is created
			//and we don't want this method to execute again
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_MODEL, handleSendModel);
			
			//open the browse window
			handleBrowseButton(new MouseEvent(MouseEvent.CLICK));
		}
	}
}