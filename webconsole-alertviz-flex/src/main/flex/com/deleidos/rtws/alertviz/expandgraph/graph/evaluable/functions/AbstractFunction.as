package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	/**
	 * This class is not intended to be instantiated directly, instead it provides the basic functionality
	 * of a function and classes extend and add the fundamental functionality of that function.
	 * <br><br>
	 * Steps to create a new function:
	 * <ol>
	 * <li> Determine if your function is stateful or nonstateful. A nonstateful function's value is determinstically
	 * dependent on the values of its parameters, where as a stateful function's value is not as it's value changes as 
	 * objects are added and deleted from it. Create a class and extend the appropriate subclass. </li>
	 * <li> Override and implement any functions that are needed. All functions must implement isValid.
	 * Stateful functions additionally implement alterState_add, and alterState_delete. Nonstateful functions additionally implement evaluate. </li>
	 * </ol>
	 */
	public class AbstractFunction implements IFunction
	{
		private var _constructor:Class;
		private var _name:String;
		private var _usage:String;
		protected var _parameters:Vector.<IEvaluable>;
		protected var _value:String;
		
		public function get name():String{ return _name; }
		public function get usage():String{ return _usage; }
		public function get parameters():Vector.<IEvaluable>{ return _parameters; }
		public function get value():String { return _value; }
		
		public function AbstractFunction(n:String, u:String, p:Vector.<IEvaluable> = null)
		{
			_constructor = FunctionMapper.getFunction(n);
			_name = n;
			_usage = u;
			
			if(p == null)
				_parameters = new Vector.<IEvaluable>();
			else
				_parameters = p;
			
			_value = null;
		}
		
		public function reset():IEvaluable{
			var parameters:Vector.<IEvaluable> = new Vector.<IEvaluable>();
			for each(var param:IEvaluable in _parameters)
				parameters.push(param.reset());
			
			return new _constructor(parameters);
		}
		
		public function toString():String{
			var definition:String = name + FunctionFactory.FUNCTION_START;
			
			for( var i:int = 0; i < _parameters.length; i++ ){
				if(i != 0) definition += FunctionFactory.FUNCTION_SPLITTER;
				definition += _parameters[i].toString();
			}
			
			definition += FunctionFactory.FUNCTION_END;
			
			return definition;
		}
		
		public function evaluate(o:Object):String{ 
			throw Error(name + " must override evaluate!");
		}
		
		public function addItem(o:Object):String{
			throw Error(name + " must override addItem!");
		}
		
		public function deleteItem(o:Object):String{
			throw Error(name + " must override deleteItem!");
		}
		
		public function isValid():Boolean{
			throw Error(name + " must override isValid!");
		}
		
		/** A slight modification of the built in number conversion function 
		 * <br> 1: toNum(null) == NaN
		 * <br> 2: toNum("") == NaN 
		 * <br> 3: toNum(anything else) == Number(anything else)
		 **/
		protected static function toNum(obj:*):Number{
			return obj == null || obj == "" ? Number.NaN : Number(obj);
		}
	}
}