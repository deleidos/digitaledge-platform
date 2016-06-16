import com.google.maps.InfoWindowOptions;
import com.google.maps.LatLng;
import com.google.maps.MapEvent;
import com.google.maps.MapMouseEvent;
import com.google.maps.overlays.Marker;
import com.deleidos.rtws.alertviz.googlemap.controls.CustomInfoWindow;
import com.deleidos.rtws.alertviz.googlemap.events.MapViewEvent;

import flash.geom.Point;

import mx.collections.ArrayList;

private static const CALLOUT_ALERT_INFO_WINDOW_WIDTH:int = 355;
private static const CALLOUT_ALERT_INFO_WINDOW_HEIGHT:int = 200;

private function showInfo(e:MapMouseEvent):void
{
	//Enters (Displays Infowindow) if nothing is being followed or something is being followed and the marker was clicked
	if (((follow || followMarker) && e.type == MapMouseEvent.CLICK) || (!follow && !followMarker))
	{
		//Dispatches and event back to the WatchListView if we were following and it was cancelled by clicking a marker
		if (follow || followMarker){
			dispatchEvent(new MapViewEvent(MapViewEvent.OVERRIDE, alertKeyToFollow, true));
		}
		//Sets all values which are associated with following to a non-following state
		follow = false; followMarker = false;
		alertKeyToFollow = "";
		idToFollow = "";
		
		var alertsToShow:ArrayList = null;
		
		// Look up the alerts associated with this location
		var marker:Marker = (e.target as Marker);
		if(marker != null) {
			alertsToShow = (objects[marker] as ArrayList);
		}
		
		if(alertsToShow == null || alertsToShow.length == 0) {
			return;
		}
		
		var infoWindowOptions:InfoWindowOptions = new InfoWindowOptions(InfoWindowOptions.getDefaultOptions());
		
		//sets the base of the infoWindow to draw right above the marker
		if(!(e.target.getOptions().icon is circleIcon))
		{
			infoWindowOptions.pointOffset = new Point(0,-33);
		}
		else
		{
			infoWindowOptions.pointOffset = new Point(0,-5);
		}
		
		infoWindowOptions.drawDefaultFrame = true;
		infoWindowOptions.hasCloseButton = false;
		
		infoWindowOptions.width = CALLOUT_ALERT_INFO_WINDOW_WIDTH;
		infoWindowOptions.height = CALLOUT_ALERT_INFO_WINDOW_HEIGHT;
		
		infoWindowOptions.tailAlign = InfoWindowOptions.ALIGN_RIGHT;
		infoWindowOptions.tailWidth = 45;
		infoWindowOptions.tailHeight = 70;
		infoWindowOptions.tailOffset = 30;
		
		var alertDetails:CustomInfoWindow = new CustomInfoWindow();
		alertDetails.width = CALLOUT_ALERT_INFO_WINDOW_WIDTH;
		alertDetails.maxWidth = CALLOUT_ALERT_INFO_WINDOW_WIDTH;
		alertDetails.height = CALLOUT_ALERT_INFO_WINDOW_HEIGHT;
		alertDetails.maxHeight = CALLOUT_ALERT_INFO_WINDOW_HEIGHT;
		alertDetails.generate(alertsToShow);
		infoWindowOptions.customContent = alertDetails;
		
		//if the user clicked the marker make the info window stay up on the screen
		if(e.type == MapMouseEvent.CLICK)
		{
			inputArea.text = "";
			followBtn.label = "Follow";
			var marker:Marker = Marker(e.target);
			if(oldMarker != marker)
			{
				oldMarker = marker;
				marker.removeEventListener(MapMouseEvent.ROLL_OUT, hideInfo);
				infoWindowOptions.hasCloseButton = true;
				map.openInfoWindow(e.latLng, infoWindowOptions);
				map.disableScrollWheelZoom();
				map.addEventListener(MapEvent.INFOWINDOW_CLOSED, function restore(event:MapEvent):void {
					e.target.addEventListener(MapMouseEvent.ROLL_OUT, hideInfo);
					map.removeEventListener(MapEvent.INFOWINDOW_CLOSED, restore);
					oldMarker = null;
					map.enableScrollWheelZoom();
				});
			}
			
			var selectedAlert:Object = ArrayList(objects[e.target]).getItemAt(0);
			dispatchEvent(new MapViewEvent(MapViewEvent.SELECTED, {uid:selectedAlert.uuid, filterKey:selectedAlert.alertKey}, true));
		}
		else
		{
			map.openInfoWindow(e.latLng, infoWindowOptions);
		}
	} else {
		inputArea.text = "Can not display Info Window while following. Click on marker to display Info Window and stop following";
	}
}

//closes the info window and restores settings that got disabled
private function hideInfo(e:MapMouseEvent):void
{
	map.closeInfoWindow();
	map.enableScrollWheelZoom();
	if (followBtn.label == "Cancel") {
		inputArea.text = "Click on the marker you would like to follow.";
	} else {
		inputArea.text = "";
	}
}

//only shows the info for the alert that is clicked in the timeline, will not make an accordion
private function showInfoOnClick(obj:Object):void
{
	inputArea.text = "";
	
	var infoWindowOptions:InfoWindowOptions = new InfoWindowOptions(InfoWindowOptions.getDefaultOptions());
	
	if(obj.shapeKey[obj.NumGeos - 1] == "Path")
	{
		infoWindowOptions.pointOffset = new Point(0,-5);
	}
	else
	{
		infoWindowOptions.pointOffset = new Point(0,-33);
	}
	
	infoWindowOptions.drawDefaultFrame = true;
	infoWindowOptions.hasCloseButton = true;
	
	
	infoWindowOptions.width = CALLOUT_ALERT_INFO_WINDOW_WIDTH;
	infoWindowOptions.height = CALLOUT_ALERT_INFO_WINDOW_HEIGHT;
	
	infoWindowOptions.tailAlign = InfoWindowOptions.ALIGN_RIGHT;
	infoWindowOptions.tailWidth = 45;
	infoWindowOptions.tailHeight = 70;
	infoWindowOptions.tailOffset = 30;
	
	var alertDetails:CustomInfoWindow = new CustomInfoWindow();
	alertDetails.width = CALLOUT_ALERT_INFO_WINDOW_WIDTH;
	alertDetails.maxWidth = CALLOUT_ALERT_INFO_WINDOW_WIDTH;
	alertDetails.height = CALLOUT_ALERT_INFO_WINDOW_HEIGHT;
	alertDetails.maxHeight = CALLOUT_ALERT_INFO_WINDOW_HEIGHT;
	alertDetails.generate(new ArrayList([obj]));
	infoWindowOptions.customContent = alertDetails;
	
	map.openInfoWindow(new LatLng(obj.latitude[obj.NumGeos - 1], obj.longitude[obj.NumGeos - 1]),infoWindowOptions);
	map.disableScrollWheelZoom();
	map.addEventListener(MapEvent.INFOWINDOW_CLOSED, function restore(event:MapEvent):void {
		map.removeEventListener(MapEvent.INFOWINDOW_CLOSED, restore);
		map.enableScrollWheelZoom();
	});	
}