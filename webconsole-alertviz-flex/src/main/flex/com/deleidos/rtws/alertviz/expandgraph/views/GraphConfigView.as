package com.deleidos.rtws.alertviz.expandgraph.views
{
	import com.deleidos.rtws.alertviz.expandgraph.events.FunctionEditEvent;
	import com.deleidos.rtws.alertviz.expandgraph.graph.GraphSettings;
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	import flash.utils.Dictionary;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	import mx.containers.Form;
	import mx.containers.HBox;
	import mx.containers.TabNavigator;
	import mx.containers.TitleWindow;
	import mx.controls.Button;
	import mx.core.INavigatorContent;
	import mx.core.UIComponent;
	import mx.events.CloseEvent;
	import mx.events.IndexChangedEvent;
	
	import org.un.cava.birdeye.ravis.graphLayout.layout.ForceDirectedLayouter;

	/**
	 * A tool that is used to alter the configuration file used by the graph component.
	 */
	public class GraphConfigView extends TitleWindow
	{
		//each FunctionEditorView corresponds to the editor tool for a function
		//one XML object may correspond to many different functions
		private var functionViews:Vector.<FunctionEditorView>;
		
		//forces the XML to update based on user changes
		private var saveButton:Button;
		private var invalidated:Dictionary;
		private var numInvalid:int;
		
		public function GraphConfigView(element:XML)
		{
			functionViews = new Vector.<FunctionEditorView>();
			
			this.width = 600;
			this.height = 800;
			if(element.localName() == GraphSettings.VALUE)
				this.title = "Edit Menu";
			else
				this.title = element.@[GraphSettings.DISPLAY_VALUE] + " Edit Menu";
			
			this.showCloseButton = true;
			
			saveButton = new Button();
			saveButton.label = "Save";
			saveButton.addEventListener(MouseEvent.CLICK, onSave);
			
			var cancelButton:Button = new Button();
			cancelButton.label = "Cancel";
			cancelButton.addEventListener(MouseEvent.CLICK, onCancel);
			
			var finishBox:HBox = new HBox();
			finishBox.percentWidth = 100;
			finishBox.setStyle("horizontalAlign", "center");
			
			invalidated = new Dictionary();
			numInvalid = 0;
			
			this.addChild(generateSubView(element));
			finishBox.addChild(saveButton);
			finishBox.addChild(cancelButton);
			this.addChild(finishBox);
		}
		
		/** Forwards the data needed to instantiate a ReferenceView to it's children **/
		public function set fieldData(value:ArrayCollection):void{
			for each(var f:FunctionEditorView in functionViews)
				f.fieldData = value;
		}
		
		protected function onValidation(event:FunctionEditEvent):void{
			if(invalidated[event.target] != null){
				delete invalidated[event.target];
				numInvalid--;
				if(numInvalid == 0)
					saveButton.enabled = true;
			}
		}
		
		protected function onInvalidation(event:FunctionEditEvent):void{
			if(invalidated[event.target] == null){
				invalidated[event.target] = event.target;
				numInvalid++;
				saveButton.enabled = false;
			}
		}
		
		protected function onSave(event:MouseEvent):void{
			var f:FunctionEditorView;
			for each(f in functionViews)
				f.save();
					
			this.dispatchEvent(new CloseEvent(CloseEvent.CLOSE));
		}
		
		protected function onCancel(event:MouseEvent):void{
			this.dispatchEvent(new CloseEvent(CloseEvent.CLOSE));
		}
		
		private function generateSubView(element:XML):UIComponent{
			if(element.localName() == GraphSettings.VALUE){
				var view:FunctionEditorView = new FunctionEditorView(element);
				functionViews.push(view);
				view.addEventListener(FunctionEditEvent.ALL_VALID, onValidation);				
				view.addEventListener(FunctionEditEvent.FUNCTION_INVALIDATED, onInvalidation);
				return view;
			}else if(element.children().length() == 1 && element.children()[0].localName() == GraphSettings.VALUE)
				return generateSubView(element.children()[0]);
			else{
				var subView:TabNavigator = new TabNavigator();
				for each(var child:XML in element.children()){
					var form:Form = new Form();
					form.label = child.@[GraphSettings.DISPLAY_VALUE];
					form.addChild(generateSubView(child));
					subView.addChild(form);
				}
				subView.percentWidth = 100;
				subView.percentHeight = 100;
				return subView;
			}
		}
		
		/* When there is a lot of data to be loaded, the view takes a long time to load up (5+ seconds). This is
		 * some work on a view that postpones the generating of a function editor until the user selects that specific section
		private var loadAsYouGo:Dictionary = new Dictionary();
		private function generateSubView(element:XML):UIComponent{
			if(element.localName() == GraphSettings.VALUE){ //should only occur when editing leaf-most values directly
				return createFunctionEditor(element);
			}else if(element.children().length() == 1 && element.children()[0].localName() == GraphSettings.VALUE)
				return generateSubView(element.children()[0]);
			else{
				var subView:TabNavigator = new TabNavigator();
				for each(var child:XML in element.children()){
					var form:Form = new Form();
					form.label = child.@[GraphSettings.DISPLAY_VALUE];
					
					if(child.children().length() == 1 && child.children()[0].localName() == GraphSettings.VALUE)
						loadAsYouGo[form] = child.children()[0];
					else
						form.addChild(generateSubView(child));
					
					subView.addChild(form);
					subView.addEventListener(IndexChangedEvent.CHANGE, initFunctionEditor);
				}
				subView.percentWidth = 100;
				subView.percentHeight = 100;
				return subView;
			}
		}
		
		private function initFunctionEditor(event:IndexChangedEvent):void{
			var form:Form = event.relatedObject as Form;
			var xml:XML = loadAsYouGo[form];
			if(xml != null){
				delete loadAsYouGo[form];
				form.addChild(createFunctionEditor(xml));
			}
		}
		
		private function createFunctionEditor(xml:XML):FunctionEditorView{
			var view:FunctionEditorView = new FunctionEditorView(xml);
			functionViews.push(view);
			view.addEventListener(FunctionEditEvent.ALL_VALID, onValidation);				
			view.addEventListener(FunctionEditEvent.FUNCTION_INVALIDATED, onInvalidation);
			return view;
		}
		*/
	}
}