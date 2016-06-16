package com.deleidos.rtws.alertviz.expandgraph.events
{
	import flash.events.Event;
	import flash.events.KeyboardEvent;

	public class ClientKeyboardEvent extends Event
	{
	
		private static var WRAPPER:String = "WRAPPER";
		public static var KEY_DOWN:String = WRAPPER + KeyboardEvent.KEY_DOWN;
		public static var KEY_UP:String = WRAPPER + KeyboardEvent.KEY_UP;
		
		public var keyCode:uint;
		
		public function ClientKeyboardEvent(type:String, k:uint)
		{
			super(type);
			keyCode = k;
		}
		
		public override function clone():Event{
			return new ClientKeyboardEvent(this.type, this.keyCode);
		}
	}
}