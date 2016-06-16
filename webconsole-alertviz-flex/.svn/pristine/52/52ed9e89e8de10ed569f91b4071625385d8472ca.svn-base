package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.AbstractFunction;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	/**
	 * A nonstateful function is one where the value is determinstically dependent on the values of its parameters.
	 */
	public class NonStatefulFunction extends AbstractFunction
	{
		public function NonStatefulFunction(n:String, u:String, p:Vector.<IEvaluable> = null)
		{
			super(n, u, p);
		}
		
		override public function evaluate(o:Object):String{ 
			throw Error(name + " must override evaluate!");
		}
		
		override public function addItem(o:Object):String{
			for each(var param:IEvaluable in _parameters)
			param.addItem(o);
			
			_value = evaluate(o);
			return _value;
		}
		
		override public function deleteItem(o:Object):String{
			for each(var param:IEvaluable in _parameters)
			param.deleteItem(o);
			
			_value = evaluate(o);
			return _value;
		}
	}
}