package com.deleidos.rtws.alertviz.renderers
{
	import com.deleidos.rtws.alertviz.models.TimelineEntry;
	
	import flash.globalization.DateTimeStyle;
	
	import flexlib.scheduling.scheduleClasses.IScheduleEntry;
	import flexlib.scheduling.scheduleClasses.renderers.AbstractSolidScheduleEntryRenderer;
	
	import spark.formatters.DateTimeFormatter;
		
	public class TickScheduleEntryRenderer extends AbstractSolidScheduleEntryRenderer
	{
		private var formatter:DateTimeFormatter;
		
		public function TickScheduleEntryRenderer()
		{
			super();
			
			formatter = new DateTimeFormatter();
			formatter.useUTC = true;
			formatter.dateStyle = DateTimeStyle.LONG
			
			this.verticalScrollPolicy = "off";
			this.horizontalScrollPolicy = "off";
		}
			
		public override function set data(value:Object):void
		{
			super.data = value;
				
			entry = value as IScheduleEntry;
			var content:TimelineEntry = TimelineEntry(entry);
			
			setStyle("backgroundColor", content.backgroundColor);
			
			toolTip = content.label + " @ " + formatter.format(content.alertDate);
		}
		
		protected override function updateDisplayList(unscaledWidth:Number, unscaledHeight:Number):void
		{
			var alertEntry:TimelineEntry = TimelineEntry(entry);
			super.updateDisplayList(alertEntry.tickWidth, unscaledHeight);
		}
	}
}