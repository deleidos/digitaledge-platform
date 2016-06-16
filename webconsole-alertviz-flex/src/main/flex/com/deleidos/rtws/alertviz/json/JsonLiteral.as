package com.deleidos.rtws.alertviz.json
{
	import com.deleidos.rtws.alertviz.json.JsonLiteral;
	
	public class JsonLiteral
	{
		public static var jsonTrue:JsonLiteral  = new JsonLiteral("true");
		public static var jsonFalse:JsonLiteral = new JsonLiteral("false");
		public static var jsonNull:JsonLiteral  = new JsonLiteral("null");
		
		private var _label:String;
		
		public function JsonLiteral(label:String) {
			this._label = label;
		}
		
		public function get label():String {
			return this._label;
		}		
	}
}
