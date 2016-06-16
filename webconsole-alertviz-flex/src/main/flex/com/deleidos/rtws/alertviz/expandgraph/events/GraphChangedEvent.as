package com.deleidos.rtws.alertviz.expandgraph.events
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.Node;
	import flash.events.Event;
	
	public class GraphChangedEvent extends Event
	{
		public static const TOGGLE:String = "gce_nodeToggle";
		public static const NEW_EVENT:String = "gce_newEvent";
		public static const NEW_SETTINGS:String = "gce_newSettings";
		public static const REFRESH:String = "gce_refresh";
		
		public function GraphChangedEvent(type:String)
		{
			super(type);
		}
		
		public override function clone():Event
		{
			return new GraphChangedEvent(type);
		}
		
	}
}