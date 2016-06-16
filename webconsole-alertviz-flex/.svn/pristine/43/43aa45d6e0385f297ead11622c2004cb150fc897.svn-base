package org.un.cava.birdeye.ravis.graphLayout.visual
{
	import flash.events.MouseEvent;
	
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.core.UIComponent;
	
	import org.un.cava.birdeye.ravis.utils.events.HoverMenuEvent;

	public class HoverMenuFactory
	{
		[Embed(source="/org/un/cava/birdeye/ravis/assets/icons/ui/hideIcon.png")]
		private static const hideIcon:Class;
		
		[Embed(source="/org/un/cava/birdeye/ravis/assets/icons/ui/anchorIcon.png")]
		private static const anchorIcon:Class;
		
		[Embed(source="/org/un/cava/birdeye/ravis/assets/icons/ui/deanchorIcon.png")]
		private static const deanchorIcon:Class;
		
		[Embed(source="/org/un/cava/birdeye/ravis/assets/icons/ui/centerIcon.png")]
		private static const centerIcon:Class;
		
		[Embed(source="/org/un/cava/birdeye/ravis/assets/icons/ui/listicon.png")]
		private static const listIcon:Class;
		
		public static const HIDE:int = 0;
		public static const ANCHOR:int = 1;
		public static const CENTER:int = 2;
		public static const LIST:int = 3;
		
		public static const ANCHOR_CLICK:int = 0;
		public static const DEANCHOR_CLICK:int = 1;
		
		public static function getInstance(node:IVisualNode):UIComponent{
			var icons:Array = new Array(3);
			icons[HIDE] = hideIcon;
			icons[ANCHOR] = new Array(2);
			icons[ANCHOR][ANCHOR_CLICK] = anchorIcon;
			icons[ANCHOR][DEANCHOR_CLICK] = deanchorIcon;
			icons[CENTER] = centerIcon;
			icons[LIST] = listIcon;
			
			var tooltips:Array = new Array(3);
			tooltips[HIDE] = "Hide";
			tooltips[ANCHOR] = new Array(2);
			tooltips[ANCHOR][ANCHOR_CLICK] = "Anchor";
			tooltips[ANCHOR][DEANCHOR_CLICK] = "De-Anchor";
			tooltips[CENTER] = "Center";
			tooltips[LIST] = "Show Data";
			
			return new HoverMenu(icons, tooltips, node);
		}
	}
}