package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class MonochromeColorFunction extends NonStatefulFunction
	{
		public static const NAME:String = "monocolor";
		public static const USAGE:String = NAME + "(p1): returns the hexadecimal color that is p1% black and (100-p1)% white, flooring the value of p1 if it less than 0 or more than 100\n" +
			NAME + "(p1,p2,p3): returns the hexadecimal color that represents the location of p1 on a scale of p2 to p3. ex: monocolor(50,40,60) = color that is 50% black, 50% white (grey)";
		
		private static const DEFAULT_COLOR:String = "0x7F7F7F";
		
		public function MonochromeColorFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			var val:Number;
			var hexval:String;
			if(_parameters.length == 1){
				val = toNum(_parameters[0].evaluate(o));
				if(isNaN(val))
					return DEFAULT_COLOR;
				else if(val < 0)
					val = 0;
				else if(val > 100)
					val = 100;
				
				//1 -> white, 0 -> black
				val = 1 - (val / 100);
				val = Math.floor(val * 255);
				
				hexval = val.toString(16).toLocaleUpperCase();
				if(hexval.length == 1)
					hexval = "0" + hexval;
				
				return "0x" + hexval + hexval + hexval;
			}else if(_parameters.length == 3){
				val = toNum(_parameters[0].evaluate(o));
				var min:Number = toNum(_parameters[1].evaluate(o));
				var max:Number = toNum(_parameters[2].evaluate(o));
				if(isNaN(val) || isNaN(min) || isNaN(max))
					return DEFAULT_COLOR;
				else if(val < min)
					val = min;
				else if(val > max)
					val = max;
				
				//1 -> white, 0 -> black
				if(min == max)
					val == 0;
				else
					val = 1 - ((val - min)/((val - min) + (max - val)));
				
				val = Math.floor(val * 255);
				
				hexval = val.toString(16).toLocaleUpperCase();
				if(hexval.length == 1)
					hexval = "0" + hexval;
				
				return "0x" + hexval + hexval + hexval;
			}else
				return DEFAULT_COLOR;
		}
	
		override public function isValid():Boolean{
			return _parameters.length == 1 || _parameters.length == 3;
		}
	}
}