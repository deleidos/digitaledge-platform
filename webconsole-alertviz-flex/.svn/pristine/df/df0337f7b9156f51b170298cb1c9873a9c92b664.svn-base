package com.deleidos.rtws.alertviz.utils
{
	import flash.utils.Proxy;
	import flash.utils.flash_proxy;
	
	import mx.utils.ObjectUtil;

	dynamic public class FlattenedObject extends Object
	{
		
		public static const DELIMITER:String = "-";
		
		public function FlattenedObject(obj:Object = null){
			super();
			if(obj != null) populate(obj);
		}
		
		private function populate(obj:Object, prefix:String = null):void{
			var props:Object = ObjectUtil.getClassInfo(obj).properties;
			for each(var q:Object in props){
				var key:String;
				if(q is QName) key = (q as QName).localName;
				else key = q.toString();
				
				var value:Object = obj[key];
				var newPrefix:String = prefix == null ? key : prefix + DELIMITER + key;
				if(ObjectUtil.isSimple(value) && !(value is Array))
					this[newPrefix] = value;
				else
					populate(value, newPrefix);
			}
		}
		
		public function toString():String{
			var str:String = "{";
			for(var key:String in this){
				str += "\"" + key + "\":\"" + this[key] + "\" ";
			}
			str += "}";
			return str;
		}
	}
}