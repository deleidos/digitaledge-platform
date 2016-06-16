package com.deleidos.rtws.alertviz.expandgraph.views
{
	import com.deleidos.rtws.alertviz.expandgraph.events.FunctionEditEvent;
	import com.deleidos.rtws.alertviz.expandgraph.graph.GraphSettings;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful.BaseFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.FunctionFactory;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.IFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.Literal;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.ObjectReference;
	
	import flash.display.DisplayObjectContainer;
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	import mx.containers.Canvas;
	import mx.containers.HBox;
	import mx.containers.VBox;
	import mx.controls.Button;
	import mx.controls.ComboBox;
	import mx.controls.Image;
	import mx.controls.Label;
	import mx.controls.Spacer;
	import mx.controls.TextInput;
	import mx.core.ScrollPolicy;
	import mx.core.UIComponent;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;

	public class FunctionEditorView extends VBox
	{
		[Embed(source="/com/deleidos/rtws/alertviz/expandgraph/views/assets/expand.png")]
		private static const expandIcon:Class;
		
		[Embed(source="/com/deleidos/rtws/alertviz/expandgraph/views/assets/collapse.png")]
		private static const collapseIcon:Class;
		
		[Embed(source="/com/deleidos/rtws/alertviz/expandgraph/views/assets/DeleteRed.png")]
		private static const deleteIcon:Class;
		
		[Embed(source="/com/deleidos/rtws/alertviz/expandgraph/views/assets/add.png")]
		private static const addIcon:Class;
		
		private static const CONST:String = "Constant";
		private static const REF:String = "Reference";
		private static const FUNC:String = "Function";
		private static const OPTIONS:Array = [CONST, REF, FUNC];
		
		//the xml data to be edited
		private var xml:XML;
		
		//the function represented by the xml data
		private var base:IFunction;
		
		//maps UIComponents to what functions they are responsible for
		//this way UI events can access and edit functions
		private var paramDict:Dictionary;
		
		//keeps track of invalid functions in the menu
		//the view cannot be saved while there are invalid functions
		private var funcDict:Dictionary;
		private var numInvalid:int;
		
		//the text box where the final result is displayed
		private var functionInput:TextInput;
		
		//stores any changes to the final result so the user 
		//can undo and redo changes they've made
		private var undoRedo:Vector.<String>;
		private var undoRedoIndex:int;
		
		//popup used when a user selects the function option in a combo box
		private var functionSelectPopup:FunctionMenu;
		private var functionSelect:Array;
		
		//popup used when a user selects the reference option in a combo box
		private var referenceSelectPopup:ReferenceMenu;
		private var referenceSelect:Array;
		
		//used to revert a user's change to a combo box if
		//the user cancels the action
		private var currentCombo:ComboBox;
		private var currentComboSelection:Object;
		
		//stores data regarding the current model's fields
		//this is used to populate reference popups with options
		public var fieldData:ArrayCollection;
		
		public function FunctionEditorView(data:XML)
		{
			xml = data;
			
			undoRedo = new Vector.<String>();
			undoRedo.push(xml.@[GraphSettings.DISPLAY_VALUE]);
			undoRedoIndex = 0;
			base = FunctionFactory.parse(xml.@[GraphSettings.DISPLAY_VALUE], GraphSettings.HEADER);
			this.percentHeight = 100;
			this.percentWidth = 100;
			this.verticalScrollPolicy = ScrollPolicy.OFF;
			this.horizontalScrollPolicy = ScrollPolicy.OFF;
			this.addEventListener(KeyboardEvent.KEY_DOWN, onKeyDown);
			
			paramDict = new Dictionary();
			funcDict = new Dictionary();
			
			refresh();
		}
		
		//Creates the UI
		protected function loadAll():void{
			this.removeAllChildren();
			var functionString:String = base.toString();

			var canvas:Canvas = new Canvas();
			canvas.percentHeight = 100;
			canvas.percentWidth = 100;
			var menuArea:VBox = new VBox();
			menuArea.percentHeight = 100;
			menuArea.percentWidth = 100;
			menuArea.setStyle("borderStyle", "solid");
			menuArea.setStyle("verticalAlign", "top");
			canvas.addChild(menuArea);
			addChild(canvas);
			
			var editBox:HBox = new HBox();
			editBox.percentWidth = 100;
			editBox.setStyle("horizontalAlign", "center");
			
			functionInput = new TextInput();
			functionInput.editable = false;
			functionInput.percentWidth = 100;
			functionInput.text = functionString;
			editBox.addChild(functionInput);
			addChild(editBox);
			
			paramDict = new Dictionary();
			funcDict = new Dictionary();
			numInvalid = 0;
			
			populateMenu(base.parameters[0], menuArea, base, 0);
			if(numInvalid == 0)
				dispatchEvent(new FunctionEditEvent(FunctionEditEvent.ALL_VALID));
			else
				dispatchEvent(new FunctionEditEvent(FunctionEditEvent.FUNCTION_INVALIDATED));
		}
		
		//generates the UI that allows for the user to modify a function
		protected function populateMenu(eval:IEvaluable, menu:VBox, parentFunction:IEvaluable, parameterIndex:Number, spacerWidth:Number = 0):void{
			var subMenuWSpacer:HBox = new HBox();
			subMenuWSpacer.percentWidth = 100;
			
			var subMenu:VBox = new VBox();
			subMenu.percentWidth = 100;
			
			var mainBox:HBox = new HBox();
			if(spacerWidth != 0){
				var spacer:Spacer = new Spacer();
				spacer.width = spacerWidth;
				spacer.percentHeight = 100;
				subMenuWSpacer.addChild(spacer);
			}
			subMenuWSpacer.addChild(subMenu);
			paramDict[mainBox] = [parentFunction, parameterIndex];
			if(eval is Literal){
				//A literal is represented by a text field populated
				//with that literal's value
				//To modify a literal, the text is simply changed
				var i1:TextInput = new TextInput();
				i1.text = eval.value;
				i1.addEventListener(Event.CHANGE, onTextChange);
				
				mainBox.addChild(i1);
				
				if(parentFunction != base) //can't delete the root element
					mainBox.addChild(deleteButton());
				
				mainBox.addChild(makeDropDownMenu(CONST));
				subMenu.addChild(mainBox);
			}else if(eval is ObjectReference){
				//An object reference is represented by a button whose label
				//is the toString() of that reference
				//To modify a reference, the button is clicked and a menu pops up
				//to allow for choosing a different reference
				var i2:Button = new Button();
				i2.label = eval.toString();
				i2.addEventListener(MouseEvent.CLICK, createReferenceSelectPopup);
				
				mainBox.addChild(i2);
				
				if(parentFunction != base) //can't delete the root element
					mainBox.addChild(deleteButton());
				
				mainBox.addChild(makeDropDownMenu(REF));
				subMenu.addChild(mainBox);
			}else if(eval is IFunction){
				//A function is represented by a button whose label is
				//the toString() of that function
				//A function's parameters are represented by an indented
				//list of components under the function button
				//To modify a function, either the function's button is clicked
				//and a menu pops up to allow for choosing a different function,
				//or one of the function's parameters are modified
				var func:IFunction = eval as IFunction;
				var i3:Button = new Button();
				i3.label = func.name;
				i3.toolTip = func.usage;
				i3.addEventListener(MouseEvent.CLICK, createFunctionSelectPopup);
				
				mainBox.addChild(makeMinimizeButton());
				mainBox.addChild(i3);
				
				if(parentFunction != base) //can't delete the root element
					mainBox.addChild(deleteButton());
				mainBox.addChild(addButton());
				
				mainBox.addChild(makeDropDownMenu(FUNC));
				subMenu.addChild(mainBox);
				subMenu.setStyle("borderStyle", "solid");
				
				//If a function is invalid, the component containing it is colored red
				//to tell the user where there is an error.
				if(funcDict[func] == null && !func.isValid()){
					subMenu.setStyle("backgroundColor", "0xEE6A50");
					i3.setStyle("color", "0xEE6A50");
					funcDict[func] = func;
					numInvalid++;
				}else if(funcDict[func] != null && func.isValid()){
					subMenu.setStyle("backgroundColor", "0xFFFFFF");
					i3.setStyle("color", "0x0B333C");
					delete funcDict[func];
					numInvalid--;
				}
				
				//Recursively generate the UI for the function's parameters
				var i:int = 0;
				for each (var f:IEvaluable in func.parameters)
					populateMenu(f, subMenu, eval, (i++), 16);
			}else
				throw new Error("Unknown type of IEvaluable encountered: " + eval);
			
			menu.addChild(subMenuWSpacer);
		}
		
		/* Generic UI functions */
		
		protected function deleteButton():UIComponent{
			var img:Image = new Image();
			img.source = deleteIcon;
			img.toolTip = "Delete";
			img.percentHeight = 100;
			img.setStyle("verticalAlign", "middle");
			img.addEventListener(MouseEvent.CLICK, onDeleteClick);
			return img;
		}
		
		protected function onDeleteClick(event:MouseEvent):void{
			var arr:Array = paramDict[event.target.parent];
			(arr[0] as IFunction).parameters.splice(arr[1],1);
			
			updateFunctionInput(true);
		}
		
		protected function makeDropDownMenu(start:String):UIComponent{
			var combo:ComboBox = new ComboBox();
			combo.dataProvider = OPTIONS;
			combo.selectedItem = start;
			combo.addEventListener(Event.OPEN, storeSelection);
			combo.addEventListener(Event.CHANGE, onComboChange);
			combo.toolTip = "Value Type";
			return combo;
		}
		
		protected function storeSelection(event:Event):void{
			currentCombo = (event.target as ComboBox);
			currentComboSelection = currentCombo.selectedItem;
		}
		
		protected function onComboChange(event:Event):void{
			var arr:Array = paramDict[event.target.parent];
			var selection:String = (event.target as ComboBox).selectedLabel;
			if(selection == CONST){
				(arr[0] as IFunction).parameters.splice(arr[1], 1, new Literal(""));
				updateFunctionInput(true);
			}else if(selection == FUNC){
				createFunctionSelectPopup(event);
			}else if(selection == REF){
				createReferenceSelectPopup(event);
			}
		}
		
		/* End Generic UI Functions */
		
		/* Literal UI-related Functions */
		
		//when the text representing a literal is changed, replace the literal
		//it was representing with a new one
		protected function onTextChange(event:Event):void{
			var arr:Array = paramDict[event.target.parent];
			if(arr != null){				
				var literal:IEvaluable = new Literal((event.target as TextInput).text);
				(arr[0] as IFunction).parameters.splice(arr[1], 1, literal);
				updateFunctionInput(false);
			}else
				throw Error("Text input without bounded function found: " + event.target); 
		}
		
		/* End Literal UI-related Functions */
		
		/* Object Reference UI-related Functions */
		
		protected function createReferenceSelectPopup(event:Event):void{
			var arr:Array = paramDict[event.target.parent];
			
			referenceSelect = arr;
			referenceSelectPopup = new ReferenceMenu();
			referenceSelectPopup.fieldData = fieldData;
			referenceSelectPopup.addEventListener(CloseEvent.CLOSE, onReferenceSelectCompletion);
			
			PopUpManager.addPopUp(referenceSelectPopup, this, true);
			PopUpManager.centerPopUp(referenceSelectPopup);
		}
		
		protected function onReferenceSelectCompletion(event:CloseEvent):void{
			PopUpManager.removePopUp(referenceSelectPopup);
			if(referenceSelectPopup.returnValue != null){
				var ref:ObjectReference = referenceSelectPopup.returnValue;
				
				(referenceSelect[0] as IFunction).parameters.splice(referenceSelect[1], 1, ref);
				updateFunctionInput(true);
			}else{
				currentCombo.selectedItem = currentComboSelection;
				currentCombo = null;
				currentComboSelection = null;
			}
			referenceSelectPopup = null;
			referenceSelect = null;
		}
		
		/* End Object Reference UI-related Functions */
		
		/* IFunction UI-related Functions */
		
		protected function makeMinimizeButton():UIComponent{
			var img:Image = new Image();
			img.toolTip = "Hide Parameters";
			img.source = collapseIcon;
			img.addEventListener(MouseEvent.CLICK, onMinimizeClick);
			return img;
		}
		
		protected function onMinimizeClick(event:MouseEvent):void{
			var tmp:Image = event.target as Image;
			var target:UIComponent = event.target.parent.parent as UIComponent;
			var visibility:Boolean;
			if(tmp.source == collapseIcon){
				visibility = false;
				tmp.toolTip = "Show Parameters";
				tmp.source = expandIcon;
			}else{
				visibility = true;
				tmp.toolTip = "Hide Parameters";
				tmp.source = collapseIcon;
			}
			
			//start index at 1 to avoid hiding the root child
			for(var i:int = 1; i < target.numChildren; i++){
				var child:UIComponent = target.getChildAt(i) as UIComponent;
				child.includeInLayout = visibility;
				child.setVisible(visibility);
			}
		}
		
		protected function addButton():UIComponent{
			var img:Image = new Image();
			img.source = addIcon;
			img.toolTip = "Add Parameter";
			img.percentHeight = 100;
			img.setStyle("verticalAlign", "middle");
			img.addEventListener(MouseEvent.CLICK, onAddClick);
			return img;
		}
		
		protected function onAddClick(event:MouseEvent):void{
			var arr:Array = paramDict[event.target.parent];
			((arr[0] as IFunction).parameters[arr[1]] as IFunction).parameters.push(new Literal(""));
			
			updateFunctionInput(true);
		}
		
		protected function createFunctionSelectPopup(event:Event):void{
			var arr:Array = paramDict[event.target.parent];
			
			functionSelect = arr;
			functionSelectPopup = new FunctionMenu();
			functionSelectPopup.addEventListener(CloseEvent.CLOSE, onFunctionSelectCompletion);
			
			PopUpManager.addPopUp(functionSelectPopup, this, true);
			PopUpManager.centerPopUp(functionSelectPopup);
		}
		
		protected function onFunctionSelectCompletion(event:CloseEvent):void{
			PopUpManager.removePopUp(functionSelectPopup);
			if(functionSelectPopup.returnValue != null){
				var f:IFunction = new functionSelectPopup.returnValue();
				while(!f.isValid())
					f.parameters.push(new Literal(""));
				
				(functionSelect[0] as IFunction).parameters.splice(functionSelect[1], 1, f);
				updateFunctionInput(true);
			}else{
				currentCombo.selectedItem = currentComboSelection;
				currentCombo = null;
				currentComboSelection = null;
			}
				
			functionSelectPopup = null;
			functionSelect = null;
		}
		
		/* End IFunction UI-related Functions */
		
		//Ensures the functionInput text field is in sync with the function being modified
		protected function updateFunctionInput(r:Boolean):void{
			functionInput.text = base.toString();
			
			if(undoRedoIndex != undoRedo.length-1)
				undoRedo = undoRedo.slice(0,undoRedoIndex+1);
			
			undoRedo.push(functionInput.text);
			undoRedoIndex++;
			
			//If a significant change has been made, it will be necessary to add/remove UI components,
			//so for now we'll just reload everything
			//There are several noticeable disadvantages to doing it this way: 
			//1) the user's clicks on minimize/maximize buttons are not remembered
			//2) the user's scroll position is not remembered (only matters for large functions)
			//3) this is an intensive operation and for larger functions may take a noticeable amount of time (>1 sec?)
			if(r) refresh();
		}
		
		//Ctrl+Z, Ctrl+Y functionality
		protected function onKeyDown(event:KeyboardEvent):void{
			if(event.keyCode == Keyboard.Z && event.ctrlKey && undoRedoIndex != 0){
				undoRedoIndex--;
				base = FunctionFactory.parse(undoRedo[undoRedoIndex], GraphSettings.HEADER);
				refresh();
			}else if(event.keyCode == Keyboard.Y && event.ctrlKey && undoRedoIndex != undoRedo.length-1){
				undoRedoIndex++;
				base = FunctionFactory.parse(undoRedo[undoRedoIndex], GraphSettings.HEADER);
				refresh();
			}
		}
		
		//Recreates the entire UI
		protected function refresh():void{
			loadAll();
			if(focusManager) focusManager.setFocus(functionInput);
		}
		
		public function save():void{
			if(numInvalid != 0)
				throw Error("Attempted to save invalid function: " + base.toString());
			
			xml.@[GraphSettings.DISPLAY_VALUE] = functionInput.text;
		}
	}
}