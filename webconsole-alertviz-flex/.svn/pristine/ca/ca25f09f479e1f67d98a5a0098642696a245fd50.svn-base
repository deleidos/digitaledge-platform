package com.deleidos.rtws.alertviz.googlemap.iconSprite
{
	import flash.display.Sprite;
	import flash.text.TextField;
	
	public class commentIconSprite extends Sprite
	{
		[Embed('/assets/map/comment.png')] private var commentImage:Class;
		private var commentText:String;
		
		public function commentIconSprite(label:String)
		{
			addChild(new commentImage());
			
			var labelMc:TextField = new TextField();
			labelMc.selectable = true;
			labelMc.border = false;
			labelMc.embedFonts = false;
			labelMc.mouseEnabled = false;
			labelMc.width = 126;
			labelMc.height = 130;
			labelMc.text = label;
			labelMc.x = 10;
			labelMc.y = 15;
			labelMc.maxChars = 189;
			labelMc.wordWrap = true;
			addChild(labelMc);
			cacheAsBitmap = true;
			
			commentText = label;
		}
		
		public function getText():String {
			return commentText;
		}
	}
}