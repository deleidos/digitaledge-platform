package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class LogFunction extends NonStatefulFunction
	{
		public static const NAME:String = "log";
		public static const USAGE:String = NAME + "(p1): returns the natural log of p1\n" +
										   NAME + "(p1, n): returns the log (base n) of p1";
		
		public function LogFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			var p1:Number = toNum(_parameters[0].evaluate(o));
			if(isNaN(p1))
				return Number.NaN.toString();
			else{
				if(_parameters.length == 1)
					return Math.log(p1).toString();
				else{
					var p2:Number = toNum(_parameters[1].evaluate(o));
					if(isNaN(p2))
						return Number.NaN.toString();
					else
						return (Math.log(p1) / Math.log(p2)).toString();	
				}
			}			
		}
		
		override public function isValid():Boolean{
			return _parameters.length == 1 || _parameters.length == 2;
		}
	}
}