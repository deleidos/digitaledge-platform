package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	/**
	 * Any IEvaluable that performs operations over other IEvaluable objects will implement IFunction.
	 */
	public interface IFunction extends IEvaluable
	{
		/** A unique string that identifies your function **/
		function get name():String;
		
		/** Any information you wish to give to users about how to use your function
		 * should be set here **/
		function get usage():String;
		
		/** All the parameters your function will operate over are here **/
		function get parameters():Vector.<IEvaluable>;
		
		/**
		 * Returns true if all assumptions that are needed to make
		 * the function work properly are satisfied.
		 * Note: Currently, com.deleidos.rtws.alertviz.expandgraph.views.FunctionEditorView.as
		 * relies on the fact that all functions exclusively use the simple validation
		 * technique of checking to see if the number of parameters are correct. If any
		 * additional validation is added to a function, the above class must be modified.
		 **/
		function isValid():Boolean;
	}
}