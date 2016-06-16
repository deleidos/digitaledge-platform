package com.deleidos.rtws.alertviz.expandgraph.events
{
	import flash.events.Event;

	public class NodeEvent extends Event
	{
		
		private static const PREFIX:String = "nodeEvent.";
		public static const ADD_ITEM:String = PREFIX + "addItem";
		public static const DELETE_ITEM:String = PREFIX + "deleteItem";
		
		private var _nodeID:String;
		private var _item:Object;
		
		public function NodeEvent(type:String, nodeID:String, item:Object, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_nodeID = nodeID;
			_item = item;
		}
		
		public function get nodeID():String
		{
			return _nodeID;	
		}
		
		public function get item():Object
		{
			return _item;	
		}
		
		public override function clone():Event
		{
			return new NodeEvent(type, _nodeID, _item, bubbles, cancelable);
		}
	}
}