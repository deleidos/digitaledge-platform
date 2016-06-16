package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class MapFunction extends NonStatefulFunction
	{
		public static const NAME:String = "map";
		public static const USAGE:String = NAME + "(p1, key1, value1, ... , keyn, valuen): finds the key that equals p1 and returns the value of that key";
		
		public function MapFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{			
			var map:Object = new Object();
			for(var i:int = 1; i < _parameters.length-1; i++){
				var newKey:String = _parameters[i].evaluate(o);
				var newVal:String = _parameters[i+1].evaluate(o);
				map[newKey] = newVal;
			}
			
			return map[_parameters[0].evaluate(o)];
		}
		
		override public function isValid():Boolean{
			return _parameters.length >= 3 && _parameters.length % 2 == 1;
		}
	}
}