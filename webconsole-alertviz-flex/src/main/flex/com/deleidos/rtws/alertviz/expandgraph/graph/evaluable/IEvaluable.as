package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable
{
	/**
	 * An object representing a variable state. The object stands in relation to input depending on it's internal state. The internal
	 * state can be modified by adding or deleting objects.
	 */
	public interface IEvaluable
	{
		/** Represents the current state of the IEvaluable. **/
		function get value():String;
		
		/** Represents the relation the IEvaluable has to the input. **/
		function evaluate(o:Object):String;
		
		/** Alters the internal state of the IEvaluable depending on the input. Returns the altered state. **/
		function addItem(o:Object):String;
		
		/** Alters the internal state of the IEvaluable depending on the input. Returns the altered state. **/
		function deleteItem(o:Object):String;
		
		/** Returns a deep copy of the IEvaluable that behaves as if nothing has ever been added or deleted from it. **/
		function reset():IEvaluable;
		
		/** Returns a string such that if the FunctionFactory class parsed it, it would produce the same result as the reset function **/
		function toString():String;
	}
}