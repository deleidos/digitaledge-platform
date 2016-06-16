package com.deleidos.rtws.alertviz.commands
{
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	import com.deleidos.rtws.alertviz.models.AlertDataModel;
	import com.deleidos.rtws.alertviz.models.TimelineModel;
	import com.deleidos.rtws.alertviz.models.WatchListEntry;
	import com.deleidos.rtws.alertviz.models.WatchListModel;
	import com.deleidos.rtws.alertviz.services.IWatchListService;
	import com.deleidos.rtws.commons.util.StringUtils;
	
	import org.robotlegs.mvcs.Command;

	public class WatchListCommand extends Command
	{
		[Inject] public var event:WatchListCommandEvent;
		
		[Inject] public var watchListService:IWatchListService;
		
		[Inject] public var watchListModel:WatchListModel;
		[Inject] public var alertsModel:AlertDataModel;
		[Inject] public var timelineModel:TimelineModel;
		
		public override function execute():void {
			if (event.type == WatchListCommandEvent.REFRESH) {
				watchListService.retrieveWatchList();
			}
			else if (event.type == WatchListCommandEvent.CREATE) {
				watchListService.createWatchListEntry(event.parameters.key);
			}
			else if (event.type == WatchListCommandEvent.COLOR_CHANGE) {
				watchListService.changeWatchListEntryColor(event.parameters.key, event.parameters.color);
			}
			else if (event.type == WatchListCommandEvent.REMOVE) {
				watchListService.deleteWatchListEntry(event.parameters.key);
			}
			else if(event.type == WatchListCommandEvent.TRACK_OFF_REQUEST) {
				if(event.parameters.hasOwnProperty("filterKey")) {
					var subscriptionKeyToUntrack:String = (event.parameters["filterKey"] as String);
					
					if(StringUtils.isNotBlank(subscriptionKeyToUntrack)) {
						watchListModel.turnOffTrackingAlertDef(subscriptionKeyToUntrack);
						
						alertsModel.trackingOn = false;
						alertsModel.trackingFilterKey = null;
						
						timelineModel.trackingOn = false;
						timelineModel.trackingFilterKey = null;
					}
				}
			}
			else if(event.type == WatchListCommandEvent.TRACK_ON_REQUEST) {
				if(event.parameters.hasOwnProperty("filterKey")) {
					var subscriptionKeyToTrack:String = (event.parameters["filterKey"] as String);
					
					if(StringUtils.isNotBlank(subscriptionKeyToTrack)) {
						watchListModel.turnOffTrackingForAll();
						watchListModel.turnOnTrackingAlertDef(subscriptionKeyToTrack);
						
						alertsModel.trackingOn = true;
						alertsModel.trackingFilterKey = subscriptionKeyToTrack;
						
						timelineModel.trackingOn = true;
						timelineModel.trackingFilterKey = subscriptionKeyToTrack;
					}
				}
			}
		}
	}
}