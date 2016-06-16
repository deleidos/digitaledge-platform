package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class MultiplyFunction extends NonStatefulFunction
	{
		public static const NAME:String = "multiply";
		public static const USAGE:String = NAME + "(p1, p2, ..., pn): returns the multiplication of the parameters\n";
		
		public function MultiplyFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			var product:Number = 1;
			for each(var param:IEvaluable in _parameters){
				var newNum:Number = toNum(param.evaluate(o));
				if(isNaN(newNum))
					return Number.NaN.toString();
				else
					product *= newNum;
			}
			return product.toString();
		}
		
		override public function isValid():Boolean{
			return _parameters.length >= 2;
		}
	}
}