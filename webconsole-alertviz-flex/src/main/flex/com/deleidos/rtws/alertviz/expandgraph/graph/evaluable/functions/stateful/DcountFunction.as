package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class DcountFunction extends StatefulFunction
	{
		public static const NAME:String = "dcount";
		public static const USAGE:String = NAME + "(p1): returns the number of distinct values of p1";
		
		private var _map:Object;
		private var _count:int;
		
		public function DcountFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
			_map = new Object();
			_count = 0;
			_value = "0";
		}
		
		override public function alterState_add(o:Object):void{
			var val:String = _parameters[0].value;
			if(_map[val] == null){
				_map[val] = 1;
				_count++;
			}else
				_map[val]++;
			
			_value = String(_count);
		}
		
		override public function alterState_delete(o:Object):void{
			var val:String = _parameters[0].value;
			if(_map[val] != null){
				_map[val]--;
				if(_map[val] == 0){
					delete _map[val];
					_count--;
				}
			}
			
			_value = String(_count);
		}
		
		override public function isValid():Boolean{
			return _parameters.length == 1;
		}
	}
}