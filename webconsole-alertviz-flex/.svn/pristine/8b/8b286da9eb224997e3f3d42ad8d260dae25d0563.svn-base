package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.FunctionFactory;

	public class Literal implements IEvaluable
	{
		
		private var _literal:String;
		public function get value():String{
			return _literal;
		}
		
		public function Literal(s:String)
		{
			_literal = s;
		}
		
		public function evaluate(o:Object):String{
			return _literal;
		}
		
		public function addItem(o:Object):String{
			return _literal;
		}
		
		public function deleteItem(o:Object):String{
			return _literal;
		}
		
		public function reset():IEvaluable{
			return this; //Literal is immutable
		}
		
		public function toString():String{
			var str:String = _literal;
			for each(var p:String in FunctionFactory.SPECIAL)
				str = str.replace(new RegExp("\\" + p, "g"), "\\" + p);
			return FunctionFactory.LITERAL_QUOTE + str + FunctionFactory.LITERAL_QUOTE;
		}
	}
}