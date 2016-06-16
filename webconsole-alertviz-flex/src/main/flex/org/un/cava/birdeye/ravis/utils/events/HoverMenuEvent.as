package org.un.cava.birdeye.ravis.utils.events
{
	import flash.events.Event;
	
	import org.un.cava.birdeye.ravis.graphLayout.visual.IVisualNode;

	public class HoverMenuEvent extends Event
	{
		public static const ITEM_CLICK:String = "hoverItemClick";
		
		private var _node:IVisualNode;
		private var _index:int;
		private var _toggleIndex:int;
		
		public function HoverMenuEvent(type:String, node:IVisualNode, index:int = -1, toggleIndex:int = 0 )
		{
			super(type);
			_node = node;
			_index = index;
			_toggleIndex = toggleIndex;
		}
		
		/** Returns the node associated with the hover menu that was clicked **/
		public function get node():IVisualNode{
			return _node;
		}
		
		/** Returns the index of the item in the hover menu that fired the event **/
		public function get index():int{
			return _index;
		}
		
		/** Returns the state in which the button is in. **/
		public function get toggleIndex():int{
			return _toggleIndex;
		}
		
		public override function clone():Event{
			return new HoverMenuEvent(this.type, _node, _index);
		}
	}
}