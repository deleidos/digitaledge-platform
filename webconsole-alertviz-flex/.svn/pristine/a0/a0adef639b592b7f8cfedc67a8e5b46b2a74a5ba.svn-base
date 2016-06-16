package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	/** A wrapper class that allows any IEvaluable object to be interpreted as a function. */
	public class BaseFunction extends NonStatefulFunction
	{
		public static const NAME:String = "";
		public static const USAGE:String = "The top level text can be formatted several ways:\n" +
			"1) As a literal value (Format: any characters surrounded by a quote character)\n" +
			"2) As an object reference (Format: {path1.path2.path3...} where each path is some text)\n"+
			"3) As a function (Format: name(parameter1,parameter2,...) where the name is some text and each parameter follows one of these three formats)";
		
		public function BaseFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			return _parameters[0].evaluate(o);
		}
		
		override public function isValid():Boolean{
			return _parameters.length == 1;
		}
		
		override public function toString():String{
			return _parameters[0].toString();
		}
	}
}