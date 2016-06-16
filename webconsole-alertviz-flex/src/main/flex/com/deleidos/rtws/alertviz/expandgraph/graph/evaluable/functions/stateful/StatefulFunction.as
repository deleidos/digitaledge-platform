package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.stateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.AbstractFunction;

	/**
	 * A stateful function is one where the value is not determinstically dependent on the values of its parameters
	 * due to some internal state that is modified as events are added and deleted.
	 */
	public class StatefulFunction extends AbstractFunction
	{
		public function StatefulFunction(n:String, u:String, p:Vector.<IEvaluable> = null)
		{
			super(n, u, p);
		}
		
		override public function evaluate(o:Object):String{ 
			return _value;
		}
		
		override public function addItem(o:Object):String{
			for each(var param:IEvaluable in _parameters)
				param.addItem(o);
			
			alterState_add(o);
			return _value;
		}
		
		override public function deleteItem(o:Object):String{
			for each(var param:IEvaluable in _parameters)
				param.deleteItem(o);
			
			alterState_delete(o);
			return _value;
		}
		
		public function alterState_add(o:Object):void{
			throw Error(name + " must override alterState_add!");
		}
		
		public function alterState_delete(o:Object):void{
			throw Error(name + " must override alterState_delete!");
		}
	}
}