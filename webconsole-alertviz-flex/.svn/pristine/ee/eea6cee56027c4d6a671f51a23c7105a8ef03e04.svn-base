package com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.nonstateful
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;

	public class ColorMapFunction extends NonStatefulFunction
	{
		public static const NAME:String = "colormap";
		public static const USAGE:String = NAME + "(p1, minval, maxval, c1, c2, ... , cn): finds the value of p1 relative to minval and maxval and maps its relative position to the color space defined by the sequence of colors c1 ... cn";
		
		private static const DEFAULT_COLOR:String = "0x7F7F7F";
		
		public function ColorMapFunction(parameters:Vector.<IEvaluable> = null)
		{
			super(NAME, USAGE, parameters);
		}
		
		override public function evaluate(o:Object):String{
			var value:Number = toNum(_parameters[0].evaluate(o));
			var min:Number = toNum(_parameters[1].evaluate(o));
			var max:Number = toNum(_parameters[2].evaluate(o));
			
			if(isNaN(value) || isNaN(min) || isNaN(max))
				return DEFAULT_COLOR;
			
			if(min > max){ //swap
				var tmp1:Number = max;
				max = min;
				min = tmp1;
			}
			
			//floor or ceiling the value to the min or max
			value = value < min ? min : (value > max ? max : value);
			
			//get a list of valid rgb colors
			var colorSpaces:Array = new Array();
			for(var i:int = 3; i < _parameters.length; i++){
				var tmp2:Array = toRGB(_parameters[i].evaluate(o));
				if(tmp2 == null) return DEFAULT_COLOR;
				colorSpaces.push(tmp2);
			}
			
			var absolutePosition:Number = (value - min) / (max - min);
			var relativeIndex:uint = uint(absolutePosition * (colorSpaces.length-1));
			
			if(absolutePosition == 0)
				return toHex(colorSpaces[0]);
			else if(absolutePosition == 1)
				return toHex(colorSpaces[colorSpaces.length-1]);
			
			var minColorSpace:Array = colorSpaces[relativeIndex];
			var maxColorSpace:Array = colorSpaces[relativeIndex+1];
			var relativeMin:Number = ((max - min) / (colorSpaces.length-1)) * relativeIndex;
			var relativeMax:Number = ((max - min) / (colorSpaces.length-1)) * (relativeIndex+1);
			var relativePosition:Number = (value - relativeMin) / (relativeMax - relativeMin);
			
			var minRed:Number = minColorSpace[0];
			var minGreen:Number = minColorSpace[1];
			var minBlue:Number = minColorSpace[2];
			
			var maxRed:Number = maxColorSpace[0];
			var maxGreen:Number = maxColorSpace[1];
			var maxBlue:Number = maxColorSpace[2];
			
			var newRed:Number = Math.floor( minRed + ((maxRed - minRed) * relativePosition) );
			var newGreen:Number = Math.floor( minGreen + ((maxGreen - minGreen) * relativePosition) );
			var newBlue:Number = Math.floor( minBlue + ((maxBlue - minBlue) * relativePosition) );
		
			return toHex(new Array(newRed, newGreen, newBlue));
		}
		
		protected function toRGB(color:String):Array{
			if(color == null) return null;
			
			if(color.length == 6) //most likely 6 hexadecimal characters
				color = "0x" + color;
			
			if(color.length == 8){ //check for validity
				var p1:String = color.substring(0,2);
				var p2:Number = parseInt(color.substring(2,4),16);
				var p3:Number = parseInt(color.substring(4,6),16);
				var p4:Number = parseInt(color.substring(6,8),16);
				
				if(p1=="0x" &&
					(!isNaN(p2) && p2 >= 0 && p2 <= 255) &&
					(!isNaN(p3) && p3 >= 0 && p3 <= 255) &&
					(!isNaN(p4) && p4 >= 0 && p4 <= 255)
				)
					return [p2,p3,p4];
				else
					return null;
			}else
				return null;
		}
		
		protected function toHex(rgb:Array):String{
			if(rgb == null || rgb.length != 3)
				return DEFAULT_COLOR;
			
		 	var p1:String = ((rgb[0] as Number).toString(16));
			if(p1.length == 1)
				p1 = "0" + p1;
			
			var p2:String = (rgb[1] as Number).toString(16);
			if(p2.length == 1)
				p2 = "0" + p2;
			
			var p3:String = (rgb[2] as Number).toString(16);
			if(p3.length == 1)
				p3 = "0" + p3;
			
			return "0x"+p1+p2+p3;
		}
		
		override public function isValid():Boolean{
			return _parameters.length >= 5;
		}
	}
}