package com.deleidos.rtws.alertviz.json
{
	public class JsonItem
	{
		private var _name:String;
		private var _value:Object;
		
		public function JsonItem(name:String, value:Object) {
			_name = name;
			_value = value;
		}
		
		public function get name():String {
			return _name;
		}
		
		public function get value():Object {
			return _value;
		}
	}
}
