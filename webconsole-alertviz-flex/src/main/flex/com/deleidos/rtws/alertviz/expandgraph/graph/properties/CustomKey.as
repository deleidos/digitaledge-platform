package com.deleidos.rtws.alertviz.expandgraph.graph.properties
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;
	
	public class CustomKey
	{
		
		private var _header:String;
		private var _functionList:Vector.<IEvaluable>;
		
		private var _value:Key;
		public function get value():Key{
			return _value;
		}
		public function set value(value:Key):void{
			_value = value;
		}
		
		public function CustomKey(header:String, functionList:Vector.<IEvaluable>)
		{
			_header = header;
			_functionList = functionList;
			_value = new Key(_header);
		}
		
		public function evaluate(o:Object):Key{
			var newKey:Key = new Key(_header);
			for each(var val:IEvaluable in _functionList)
				newKey.add(val.evaluate(o));
			
			return newKey;
		}
		
		public function reset():CustomKey{
			var funcList:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			for each(var val:IEvaluable in _functionList)
				funcList.push(val.reset());
			
			return new CustomKey(_header, funcList);
		}
		
		public function getDefinition():Vector.<String>{
			var definition:Vector.<String> = new Vector.<String>;
			definition.push(_header);
			
			for each( var func:IEvaluable in _functionList)
				definition.push(func.toString());
				
			return definition;
		}
	}
}