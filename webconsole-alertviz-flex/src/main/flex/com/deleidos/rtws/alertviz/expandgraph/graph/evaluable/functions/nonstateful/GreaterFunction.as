package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class GreaterFunction extends NonStatefulFunction
	{
		public static const NAME:String = "greater";
		public static const USAGE:String = NAME + "(p1, p2, ..., pn): returns the greatest parameter";
		
		public function GreaterFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			var max:Number = Number.NEGATIVE_INFINITY;
			for each(var param:IEvaluable in _parameters){
				var tmp:Number = toNum(param.evaluate(o));
				if(!isNaN(tmp) && tmp > max)
					max = tmp;
			}
			return max == Number.NEGATIVE_INFINITY ? Number.NaN.toString() : max.toString();
		}
		
		override public function isValid():Boolean{
			return _parameters.length >= 2;
		}
		
		
	}
}