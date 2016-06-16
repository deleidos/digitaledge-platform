package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class RGBColorFunction extends NonStatefulFunction
	{
		public static const NAME:String = "rgbcolor";
		public static const USAGE:String = NAME + "(p1): returns the hexadecimal color represented by p1 where p1 is a hexadecimal color string or one of [grey,black,red,greem,blue]";
		
		private static const DEFAULT_COLOR:String = "0x7F7F7F";
		
		public function RGBColorFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			var param:String = _parameters[0].evaluate(o);
			if(param == null)
				return DEFAULT_COLOR;
			
			param = param.toLocaleLowerCase();
			if(param == "grey")
				return "0x7F7F7F";
			else if(param == "black")
				return "0x000000";
			else if(param == "red")
				return "0xFF0000";
			else if(param == "green")
				return "0x00FF00";
			else if(param == "blue")
				return "0x0000FF";
			else if(param == "yellow")
				return "0xFFFF00";
			
			if(param.length == 6) //most likely 6 hexadecimal characters
				param = "0x" + param;
			
			if(param.length == 8){ //check for validity
				var p1:String = param.substr(0,2);
				var p2:Number = parseInt(param.substr(2,4),16);
				var p3:Number = parseInt(param.substr(4,6),16);
				var p4:Number = parseInt(param.substr(6,8),16);
				
				if(p1=="0x" &&
					(!isNaN(p2) && p2 >= 0 && p2 <= 255) &&
					(!isNaN(p3) && p3 >= 0 && p3 <= 255) &&
					(!isNaN(p4) && p4 >= 0 && p4 <= 255)
				)
					return param;
			}
				
			return DEFAULT_COLOR;	
		}
		
		override public function isValid():Boolean{
			return _parameters.length == 1;
		}
	}
}