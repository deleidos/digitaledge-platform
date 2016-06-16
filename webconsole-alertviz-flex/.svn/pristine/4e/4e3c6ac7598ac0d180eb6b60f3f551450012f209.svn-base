package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class SumFunction extends StatefulFunction
	{
		public static const NAME:String = "sum";
		public static const USAGE:String = NAME + "(p1): returns the sum of the parameter";
		
		private var _sum:Number;
		
		public function SumFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
			_sum = 0;
			_value = "0";
		}
		
		override public function alterState_add(o:Object):void{
			for each(var param:IEvaluable in _parameters){
				var newNum:Number = toNum(param.value);
				if(!isNaN(newNum))
					_sum += newNum;
			}
			
			_value = _sum.toString();
		}
		
		override public function alterState_delete(o:Object):void{
			for each(var param:IEvaluable in _parameters){
				var newNum:Number = toNum(param.value);
				if(!isNaN(newNum))
					_sum -= newNum;
			}
			
			_value = _sum.toString();
		}
		
		override public function isValid():Boolean{
			return _parameters.length == 1;
		}
	}
}