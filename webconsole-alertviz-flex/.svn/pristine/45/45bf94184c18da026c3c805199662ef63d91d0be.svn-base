package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class MinFunction extends StatefulFunction
	{
		public static const NAME:String = "min";
		public static const USAGE:String = NAME + "(p1): returns the minimum p1 value encountered";
		
		//TODO: Use a heap data structure, there is no default one in AS
		private var _numbers:Array;
		
		public function MinFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
			_numbers = new Array();
			_value = Number.NaN.toString();
		}
		
		override public function alterState_add(o:Object):void{
			var newVal:Number = toNum(_parameters[0].value);
			if(!isNaN(newVal)){
				_numbers.push(newVal);
				_numbers.sort(Array.NUMERIC);
				
				_value = _numbers[0].toString();
			}
		}
		
		override public function alterState_delete(o:Object):void{
			var newVal:Number = toNum(_parameters[0].value);
			if(!isNaN(newVal)){
				for ( var i:int = 0; i < _numbers.length; i++ ){
					if(_numbers[i] == newVal){
						delete _numbers[i];
						break;
					}
				}
				
				_value = _numbers.length == 0 ? Number.NaN.toString() : _numbers[0];
			}
		}
		
		override public function isValid():Boolean{
			return _parameters.length == 1;
		}
	}
}