package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class LesserFunction extends NonStatefulFunction
	{
		public static const NAME:String = "lesser";
		public static const USAGE:String = NAME + "(p1, p2, ..., pn): returns the smallest parameter";
		
		public function LesserFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			var min:Number = Number.POSITIVE_INFINITY;
			for each(var param:IEvaluable in _parameters){
				var tmp:Number = toNum(param.evaluate(o));
				if(!isNaN(tmp) && tmp < min)
					min = tmp;
			}
			return min == Number.POSITIVE_INFINITY ? Number.NaN.toString() : min.toString();
		}
		
		override public function isValid():Boolean{
			return _parameters.length >= 2;
		}
		
		
	}
}