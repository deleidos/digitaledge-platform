package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class AverageFunction extends StatefulFunction
	{
		public static const NAME:String = "avg";
		public static const USAGE:String = NAME + "(p1): returns the average of all values of p1 that occur";
		
		private var _sum:Number;
		private var _count:Number;
		
		public function AverageFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
			_count = 0;
			_sum = 0;
			_value = Number.NaN.toString();
		}
		
		override public function alterState_add(o:Object):void{
			var newVal:Number = toNum(_parameters[0].value);
			if(!isNaN(newVal)){
				_sum += newVal;
				_count++;
				_value = (_sum/_count).toString();
			}
		}
		
		override public function alterState_delete(o:Object):void{
			var newVal:Number = toNum(_parameters[0].value);
			if(isNaN(newVal)){
				_sum -= newVal;
				_count--;
				_value = (_sum/_count).toString();
			}
		}
		
		override public function isValid():Boolean{
			return _parameters.length == 1;
		}
	}
}