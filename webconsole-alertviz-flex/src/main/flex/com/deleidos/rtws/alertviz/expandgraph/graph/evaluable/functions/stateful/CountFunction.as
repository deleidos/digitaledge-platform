package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class CountFunction extends StatefulFunction
	{
		public static const NAME:String = "count";
		public static const USAGE:String = NAME + "(): returns the number of times an event has occured\n" +
			NAME + "(p1): returns the number of times an event has occured where p1 was not null";
		
		private var _count:int;
		
		public function CountFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
			_count = 0;
			_value = "0";
		}
		
		override public function alterState_add(o:Object):void{
			if(_parameters.length == 0)
				_count++;
			else if(_parameters.length == 1){
				if(_parameters[0].value != null)
					_count++;
			}
			
			_value = String(_count);
		}
		
		override public function alterState_delete(o:Object):void{
			if(_parameters.length == 0)
				_count--;
			else if(_parameters.length == 1){
				if(_parameters[0].value != null)
					_count--;
			}
			
			_value = String(_count);
		}
		
		override public function isValid():Boolean{
			return _parameters.length <= 1;
		}
	}
}