package org.un.cava.birdeye.ravis.utils
{
	import flashx.textLayout.container.ScrollPolicy;
	
	import mx.containers.VBox;

	//Fixes a Flash bug where introduction of the vertical scroll bar does not
	//resize the component, making the scroll bar overlap with the children of the container
	public class VBoxWorkaround extends VBox
	{
		
		public override function validateDisplayList():void
		{
			super.validateDisplayList();
			
			if(null != verticalScrollBar && verticalScrollBar.maxScrollPosition == 0
				&& verticalScrollPolicy != ScrollPolicy.AUTO)
			{
				verticalScrollPolicy = ScrollPolicy.AUTO;
			}
				
			else if(null != verticalScrollBar)
			{
				verticalScrollPolicy = ScrollPolicy.ON;
			}
		}
	}
}