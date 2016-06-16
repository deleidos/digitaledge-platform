package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class ConcatFunction extends NonStatefulFunction
	{
		public static const NAME:String = "concat";
		public static const USAGE:String = NAME + "(p1, p2, ..., pn): returns a string that is a concatenation of all the values";
		
		public function ConcatFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			var finalString:String = "";
			for each(var param:IEvaluable in _parameters){
				var newVal:String = param.evaluate(o);
				if(newVal != null)
					finalString += newVal;
			}
			return finalString;
		}
		
		override public function isValid():Boolean{
			return _parameters.length >= 2;
		}
	}
}