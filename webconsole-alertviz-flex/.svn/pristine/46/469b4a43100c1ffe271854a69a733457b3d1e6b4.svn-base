package org.un.cava.birdeye.ravis.graphLayout.visual
{
	import flash.events.MouseEvent;
	import flash.text.TextField;
	
	import flashx.textLayout.container.ScrollPolicy;
	
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.Label;
	import mx.core.UIComponent;
	
	import org.un.cava.birdeye.ravis.components.renderers.RendererIconFactory;
	import org.un.cava.birdeye.ravis.components.renderers.nodes.SizeByValueNodeRenderer;
	import org.un.cava.birdeye.ravis.graphLayout.visual.events.VisualNodeEvent;

	public class HideBar extends HBox
	{
		[Embed('/org/un/cava/birdeye/ravis/assets/icons/ui/showIcon.png')]
		private static const SHOW_ICON:Class;
		private static const NAME_WIDTH:Number = 100;
		private static const FITBOX:TextField = new TextField();
		
		private var _vg:VisualGraph;
		private var _vn:IVisualNode;
		public function get vn():IVisualNode{ return _vn; }
		private var _id:int;
		
		private var _idLabel:Label;
		private var _icon:UIComponent;
		private var _name:Label;
		private var _unhide:Button;
		
		public function HideBar(vg:VisualGraph, vn:IVisualNode, id:int){
			_vg = vg;
			_vn = vn;
			_id = id;
			
			horizontalScrollPolicy = ScrollPolicy.OFF;
			verticalScrollPolicy = ScrollPolicy.OFF;
			setStyle("borderStyle","solid");
			setStyle("verticalAlign", "middle");
			
			_idLabel = new Label();
			_idLabel.text = stringify(id);
			
			var view:SizeByValueNodeRenderer = (vn.view as SizeByValueNodeRenderer);
			if(view.isHighlighted)
				_icon = RendererIconFactory.createIcon("primitive::highlightcircle", 16, view.data.data.@nodeColor);
			else
				_icon = RendererIconFactory.createIcon("primitive::circle", 16, view.data.data.@nodeColor);
			
			_name = new Label();
			_name.text = fittedText(String(view.data.data.@name));
			_name.width = NAME_WIDTH;
			
			_unhide = new Button();
			_unhide.setStyle("icon", SHOW_ICON);
			_unhide.addEventListener(MouseEvent.CLICK, unhideNode);
			
			addElement(_idLabel);
			addElement(_icon);
			addElement(_name);
			addElement(_unhide);
			
			view.addEventListener(VisualNodeEvent.DATA_UPDATED, updateBar);
		}
		
		public function decrementIndex():void{
			_id--;
			_idLabel.text = stringify(_id);
		}
		
		protected function updateBar(event:VisualNodeEvent):void{
			var view:SizeByValueNodeRenderer = (vn.view as SizeByValueNodeRenderer);
			if(view.isHighlighted)
				_icon = RendererIconFactory.createIcon("primitive::highlightcircle", 16, view.data.data.@nodeColor);
			else
				_icon = RendererIconFactory.createIcon("primitive::circle", 16, view.data.data.@nodeColor);
			
			if(_name.text != String(view.data.data.@name))
				_name.text = fittedText(String(view.data.data.@name));
		}
		
		protected function unhideNode(event:MouseEvent):void{
			_unhide.removeEventListener(MouseEvent.CLICK, unhideNode);
			removeAllChildren();
			
			_idLabel = null;
			_icon = null;
			_name = null;
			_unhide = null;
			
			_vg.unhideNode(_vn);
		}
		
		protected static function fittedText(text:String):String{
			var fit:String = text;
			var index:int = 1;
			while(textWidth(fit) > NAME_WIDTH)
				fit = text.substring(0,text.length-(index++)) + "...";
			
			return fit;
		}
		
		protected static function stringify(id:int):String{
			if(id < 10)
				return "0" + id;
			else
				return id + "";
		}
		
		protected static function textWidth(text:String):Number{
			FITBOX.text = text;
			return FITBOX.textWidth;
		}
	}
}