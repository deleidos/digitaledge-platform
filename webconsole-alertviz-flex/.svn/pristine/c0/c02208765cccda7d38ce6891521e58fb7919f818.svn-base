package com.deleidos.rtws.alertviz.expandgraph.graph.properties
{

	public class Key
	{
		
		private var _header:String;
		private var _keyValues:Array;
		public function get keyValues():Array{
			return _keyValues;
		}
		
		public function Key(header:String)
		{
			_header = header;
			_keyValues = new Array();
		}
		
		public function add(value:String):void{
			_keyValues.push(value);
		}
		
		public function toString():String{
			var keyString:String = _header;
			for each(var val:String in _keyValues)
				keyString += ";" + val;
			
			return keyString;
		}
	}
}