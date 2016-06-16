import com.google.maps.LatLng;
import com.google.maps.MapMouseEvent;
import com.google.maps.overlays.Marker;
import com.deleidos.rtws.alertviz.models.Alert;
import com.deleidos.rtws.commons.util.NumberUtils;

import flash.events.Event;

public function handleItemDoubleClickEvent(alertType:String, filterKey:String):void {
	map.closeInfoWindow();
	followBtn.label = "Follow";
	followMarker = false;
	setFollow(alertType, filterKey);
}

//event handler for when the user clicks on the follow button on the map
private function followBtn_clickHandler(e:Event):void
{
	map.closeInfoWindow();
	if(followBtn.label == "Follow")
	{
		if (follow) {
			dispatchEvent(new MapViewEvent(MapViewEvent.OVERRIDE, alertKeyToFollow, true));
			alertKeyToFollow = "";
		}
		aoiPop.enabled = false;
		commentPop.enabled = false;
		togglePop.enabled = false;
		followBtn.label = "Cancel";
		follow = false;
		inputArea.text = "Click on the marker you would like to follow.";
		for(var i:Number = 0; i<markers.length; i++)
		{
			var marker:Marker = Marker(markers.getItemAt(i));
			marker.removeEventListener(MapMouseEvent.CLICK, showInfo);
			marker.addEventListener(MapMouseEvent.CLICK, followThisMarker);
		}
	}
	else if(followBtn.label == "Cancel")
	{
		for(var j:Number = 0; j<markers.length; j++)
		{
			var marker2:Marker = Marker(markers.getItemAt(j));
			marker2.addEventListener(MapMouseEvent.CLICK, showInfo);
			marker2.removeEventListener(MapMouseEvent.CLICK, followThisMarker);
		}
		inputArea.text = "";
		followBtn.label = "Follow";
		aoiPop.enabled = true;
		commentPop.enabled = true;
		togglePop.enabled = true;
	}
	else if(followBtn.label == "Stop Following")
	{
		followMarker = false;
		followBtn.label = "Follow";
	}
}

//called when user clicks on a marker after clicking on the follow button on the map
private function followThisMarker(e:MapMouseEvent):void
{
	aoiPop.enabled = true;
	commentPop.enabled = true;
	togglePop.enabled = true;
	idToFollow = dict[dict[e.target]];
	followMarker = true;
	for(var k:Number = 0; k<markers.length; k++)
	{
		var marker3:Marker = Marker(markers.getItemAt(k));
		marker3.addEventListener(MapMouseEvent.CLICK, showInfo);
		marker3.removeEventListener(MapMouseEvent.CLICK, followThisMarker);
	}
	inputArea.text = "";
	followBtn.label = "Stop Following";
	if(objects[e.target].getItemAt(0).shapeKey[0] != "Path")
	{
		map.panTo(e.target.getLatLng());
		followMarker = false;
		followBtn.label = "Follow";
	}
}

//event handler for when a user clicks on an alert in the timeline
public function handleMouseClickEvent(alert:Alert):String {
	followBtn.label = "Follow";
	
	var myAlert:Object = getAlertObject(alert.record, alert.model, alert.id, alert.criteria.name, alert.criteria.key);
	
	//checks to see if the alert is valid (does not have any null latitudes or longitudes)
	var alertCenterPoint:LatLng = null;
	
	var latitudes:Array = (myAlert.hasOwnProperty("latitude") ? (myAlert["latitude"] as Array) : null);
	var longitudes:Array = (myAlert.hasOwnProperty("longitude") ? (myAlert["longitude"] as Array) : null);
	
	if(latitudes != null && longitudes != null) {
		var latLngIndex:int = (Math.min(latitudes.length, longitudes.length) - 1);
		
		var currLat:Number = NaN;
		var currLng:Number = NaN;
		while(alertCenterPoint == null && latLngIndex >= 0) {
			currLat = NumberUtils.parseNumber(latitudes[latLngIndex]);
			currLng = NumberUtils.parseNumber(longitudes[latLngIndex]);
			
			if(isNaN(currLat) == false && isNaN(currLng) == false) {
				alertCenterPoint = new LatLng(currLat, currLng);
			}
			
			latLngIndex--;
		}
	}
	
	//only shows the info for the alert if it is valid (on the map)
	if(alertCenterPoint != null)
	{
		map.setCenter(alertCenterPoint, 16);
		showInfoOnClick(myAlert);
		follow = false;
		followMarker = false;
	}
	else
	{
		inputArea.text = "*** WARNING ***\nThe selected alert does not have enough information to display on the map."
	}
	return alertKeyToFollow;
}