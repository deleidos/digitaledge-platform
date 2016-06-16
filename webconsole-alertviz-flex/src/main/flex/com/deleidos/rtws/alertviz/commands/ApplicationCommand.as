package com.deleidos.rtws.alertviz.commands
{	
	import com.deleidos.rtws.alertviz.events.ApplicationCommandEvent;
	import com.deleidos.rtws.alertviz.models.AlertDataModel;
	import com.deleidos.rtws.alertviz.models.TimelineModel;
	import com.deleidos.rtws.alertviz.models.WatchListModel;
	
	import org.robotlegs.mvcs.Command;

	public class ApplicationCommand extends Command
	{
		[Inject] public var event:ApplicationCommandEvent;
		[Inject] public var watchListModel:WatchListModel;
		[Inject] public var alertsModel:AlertDataModel;
		[Inject] public var timelineModel:TimelineModel;
		
		public override function execute():void {
			if(event.type == ApplicationCommandEvent.CLEAR) {
				
				// Legacy code did this on data clear.  Not sure why, but leaving until I get approval to remove. 
				watchListModel.turnOffTrackingForAll();
				
				// Removes data and tracking settings.  Same comment for tracking here as above.
				alertsModel.clear();
				timelineModel.clear();
			}
			else if(event.type == ApplicationCommandEvent.SHUTDOWN) {
				watchListModel.disconnectAll();
			}
		}
	}
}