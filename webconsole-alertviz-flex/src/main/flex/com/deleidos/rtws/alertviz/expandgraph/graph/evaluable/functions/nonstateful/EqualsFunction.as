package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class EqualsFunction extends NonStatefulFunction
	{
		public static const NAME:String = "equals";
		public static const USAGE:String = NAME + "(p1, p2, p3, p4): if p1 equals p2 , p3 is returned, otherwise p4";
		
		public function EqualsFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			var p1:String = _parameters[0].evaluate(o);
			var p2:String = _parameters[1].evaluate(o);
			var p3:String = _parameters[2].evaluate(o);
			var p4:String = _parameters[3].evaluate(o);
			return p1 == p2 ? p3 : p4;
		}
		
		override public function isValid():Boolean{
			return _parameters.length == 4;
		}
	}
}