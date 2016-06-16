package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class AddFunction extends NonStatefulFunction
	{
		public static const NAME:String = "add";
		public static const USAGE:String = NAME + "(p1, p2, ..., pn): returns the addition of the parameters";
		
		public function AddFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			var sum:Number = 0;
			for each(var param:IEvaluable in _parameters){
				var newNum:Number = toNum(param.evaluate(o));
				if(isNaN(newNum))
					return Number.NaN.toString();
				else
					sum += newNum;
			}
			return sum.toString();
		}
		
		override public function isValid():Boolean{
			return _parameters.length >= 2;
		}
	}
}