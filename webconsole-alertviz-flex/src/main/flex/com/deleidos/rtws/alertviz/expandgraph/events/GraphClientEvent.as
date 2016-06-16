package com.deleidos.rtws.alertviz.expandgraph.events
{
	import flash.events.Event;

	public class GraphClientEvent extends Event
	{
		public static const CLICK:String = "nodeClick";
		public static const DOUBLE_CLICK:String = "nodeDoubleClick";
		public static const DRAG_START:String = "nodeDragStart";
		public static const DRAG_END:String = "nodeDragEnd";
			
		public var node:String;
			
		public function GraphClientEvent(type:String, node:String)
		{
			super(type);
			this.node = node;
		}
			
		public override function clone():Event
		{
			return new GraphClientEvent(type,node);
		}
			
	}
}