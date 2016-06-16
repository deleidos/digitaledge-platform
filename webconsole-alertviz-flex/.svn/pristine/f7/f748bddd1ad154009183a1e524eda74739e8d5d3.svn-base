/*
 * The MIT License
 *
 * Copyright (c) 2008
 * United Nations Office at Geneva
 * Center for Advanced Visual Analytics
 * http://cava.unog.ch
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.un.cava.birdeye.ravis.components.renderers.nodes {
	
	import flash.events.Event;
	import flash.events.MouseEvent;
	
	import mx.core.UIComponent;
	import mx.effects.Zoom;
	
	import org.un.cava.birdeye.ravis.assets.icons.primitives.Circle;
	import org.un.cava.birdeye.ravis.assets.icons.primitives.HighlightedCircle;
	import org.un.cava.birdeye.ravis.components.renderers.RendererIconFactory;
	import org.un.cava.birdeye.ravis.graphLayout.visual.events.VisualGraphEvent;
	import org.un.cava.birdeye.ravis.graphLayout.visual.events.VisualNodeEvent;
	import org.un.cava.birdeye.ravis.utils.LogUtil;
	import org.un.cava.birdeye.ravis.utils.events.HoverMenuEvent;
	import org.un.cava.birdeye.ravis.utils.events.VGraphEvent;
	
	/**
	 * Other version of the simple circle that changes in size
	 * */
	public class SizeByValueNodeRenderer extends EffectBaseNodeRenderer  {
	
		private static const _LOG:String = "components.renderers.nodes.SizeByValueNodeRenderer";

		private var _hoverMenu:UIComponent;
		
		private var _circle:UIComponent;
		
		/**
		 * Default constructor
		 * */
		public function SizeByValueNodeRenderer() {
			
			super(); // ensures that _zoom is already initialised
			
			/* we want a different zoom factor here in the
			 * renderer */
			(reffects.effect as Zoom).zoomHeightTo = 1.3;
			(reffects.effect as Zoom).zoomWidthTo = 1.3;
			
			this.addEventListener(VisualNodeEvent.DATA_UPDATED, updateComponent);
			this.addEventListener(MouseEvent.ROLL_OVER, toggleHoverMenuVisibility);
			this.addEventListener(MouseEvent.ROLL_OUT, toggleHoverMenuVisibility);
		}
		
		public function get isHighlighted():Boolean{
			return _circle is HighlightedCircle;
		}
		
		public function get circle():UIComponent{
			return _circle;
		}
		
		public function set circle(newCircle:UIComponent):void{
			var index:int = -1;
			
			if(_circle != null){
				index = nodeComponent.getChildIndex(_circle);
				nodeComponent.removeChild(_circle);
			}
			
			_circle = newCircle;
				
			if(index == -1)
				nodeComponent.addChild(_circle);
			else
				nodeComponent.addChildAt(_circle, index);
			
			//Make buffer space around the circle
			nodeComponent.width = _circle.width + 25;
			nodeComponent.height = _circle.height + 25;
		}
	
		/**
		 * @inheritDoc
		 * */
		override protected function initComponent(e:Event):void {
			
			/* initialize the upper part of the renderer */
			initTopPart();
			
			/* initialize the node */
			initNode();
			 
			/* now the link button */
			initLabel();
		}
		
		override protected function initNode():void{
			super.initNode();
			
			/* add a primitive circle
			* as well the XML should be checked before */
			if(circle == null){
				circle = RendererIconFactory.createIcon("primitive::circle",
					this.data.data.@nodeSize,
					int(this.data.data.@nodeColor));
				circle.toolTip = this.data.data.@name; // needs check
			}
			
			//LogUtil.debug(_LOG, "COLOR of component is: "+int(this.data.data.@nodeColor));
			
			/* now add the filters to the circle */
			reffects.addDSFilters(circle);
		}
		
		public function updateComponent( e:Event ):void{
			if(circle != null){
				var c:int = int(this.data.data.@nodeColor);
				(circle as Circle).color = c;
				circle.setStyle("color", c);
				circle.width = this.data.data.@nodeSize;
				circle.height = this.data.data.@nodeSize;
				circle.toolTip = this.data.data.@name;
				
				nodeComponent.width = _circle.width + 25;
				nodeComponent.height = _circle.height + 25;
			}
			if(lb) lb.label = this.data.data.@name;
			if(ll) ll.text = this.data.data.@name;
			
			dispatchEvent(new VisualNodeEvent(VisualNodeEvent.UPDATE_COMPLETE, null));
		}
		
		public function highlight():void{
			circle = RendererIconFactory.createIcon("primitive::highlightcircle",
				this.data.data.@nodeSize,
				int(this.data.data.@nodeColor));
			circle.toolTip = this.data.data.@name; // needs check
			dispatchEvent(new VisualNodeEvent(VisualNodeEvent.UPDATE_COMPLETE, null));
		}
		
		public function unhighlight():void{
			circle = RendererIconFactory.createIcon("primitive::circle",
				this.data.data.@nodeSize,
				int(this.data.data.@nodeColor));
			circle.toolTip = this.data.data.@name; // needs check
			dispatchEvent(new VisualNodeEvent(VisualNodeEvent.UPDATE_COMPLETE, null));
		}
		
		public function set hoverMenu(hoverMenu:UIComponent):void{
			if(hoverMenu || _hoverMenu){
				var visible:Boolean = _hoverMenu ? _hoverMenu.visible : false;
				if(_hoverMenu) this.removeChild(_hoverMenu);
				if(hoverMenu){
					hoverMenu.setVisible(visible);
					this.addChild(hoverMenu);
				}
				_hoverMenu = hoverMenu;
			}
		}
		
		protected function toggleHoverMenuVisibility( event:MouseEvent ):void{
			if(!_hoverMenu) return;
			
			if(event.type == MouseEvent.ROLL_OVER){
				_hoverMenu.setVisible(true);
			}else if(event.type == MouseEvent.ROLL_OUT){
				_hoverMenu.setVisible(false);
			}
		}
		
	}
}
