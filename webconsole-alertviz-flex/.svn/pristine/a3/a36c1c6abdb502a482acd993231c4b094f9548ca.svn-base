package org.un.cava.birdeye.ravis.assets.icons.primitives
{
	import flash.display.GradientType;
	import flash.display.InterpolationMethod;
	import flash.display.SpreadMethod;

	public class HighlightedCircle extends Circle
	{
		private static const HIGHLIGHT_THICKNESS:Number = 5;
		private static const HIGHLIGHT_SPACING:Number = 3;
		public static const HIGHLIGHT_SIZE:Number = HIGHLIGHT_THICKNESS + HIGHLIGHT_SPACING;
		
		private var _highlightColor:int;	
		
		public function HighlightedCircle(highlightColor:int = 0x000000){
			_highlightColor = highlightColor;
		}
		
		override public function set width(value:Number):void{
			super.width = value + HIGHLIGHT_SIZE;
		}
		
		override public function set height(value:Number):void{
			super.height = value + HIGHLIGHT_SIZE;
		}
		
		override protected function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void {
			/* clear the existing drawing */
			graphics.clear();
			
			/* we want a gradient filled circle */
			graphics.beginGradientFill(GradientType.RADIAL,
				[0xffffff, color],
				[1, 1],
				[0, 127], 
				null,
				SpreadMethod.PAD,
				InterpolationMethod.RGB,
				0.75);
			/* now we draw the circle, it's radius is half the size of the height of the UIComponent,
			* thus it will always fill a square one. However, if we resize the width the circle
			* might be too large...? 
			*/
			graphics.drawCircle(unscaledWidth / 2, unscaledHeight / 2, (unscaledHeight - HIGHLIGHT_SIZE) / 2);
			/* done */
			graphics.endFill();
			
			graphics.lineStyle(HIGHLIGHT_THICKNESS, _highlightColor);
			graphics.drawCircle(unscaledWidth / 2, unscaledHeight / 2, unscaledHeight / 2);
		}
	}
}