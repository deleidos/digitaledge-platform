package org.un.cava.birdeye.ravis.graphLayout.visual
{
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.core.UIComponent;
	
	import org.un.cava.birdeye.ravis.utils.events.HoverMenuEvent;
	
	public class HoverMenu extends HBox
	{
		private var _toggle:Array;
		private var _icons:Array;
		private var _tooltips:Array;
		private var _node:IVisualNode;
		
		public function HoverMenu( icons:Array, tooltips:Array, node:IVisualNode ){
			_toggle = new Array();
			_icons = icons;
			_tooltips = tooltips;
			_node = node;
			
			for( var index:int = 0; index < icons.length; index++ ){
				_toggle.push(0);
				addElement(generateButton(index));
			}
		}
		
		public function toggle( index:int ):void{
			if(index >= 0 && index < _icons.length && _icons[index] is Array){
				var toggleIndex:int = (_toggle[index] + 1) % _icons[index].length;
				_toggle[index] = toggleIndex;
				
				removeElementAt(index);
				addElementAt(generateButton(index, toggleIndex), index);
			}
		}
		
		private function generateButton( index:int, toggleIndex:int = 0 ):Button{
			
			var button:Button = new Button();
			
			if(_icons[index] is Array){
				button.setStyle("icon", _icons[index][toggleIndex]);
				button.setStyle("buttonIndex", index); //cheapo way of passing the index as a parameter to the click listener
				button.toolTip = _tooltips[index][toggleIndex];
			}else{
				button.setStyle("icon", _icons[index]);
				button.setStyle("buttonIndex", index); //cheapo way of passing the index as a parameter to the click listener
				button.toolTip = _tooltips[index];
			}
			
			var tmp:HoverMenu = this;
			button.useHandCursor = false;
			button.addEventListener(MouseEvent.CLICK, 
				function onClick(event:MouseEvent):void{
					var i:int = (event.target as UIComponent).getStyle("buttonIndex");
					tmp.dispatchEvent(new HoverMenuEvent(HoverMenuEvent.ITEM_CLICK, _node, i, _toggle[i]));
					toggle(i);
				}
			);
			
			return button;
		}
		
	}
}