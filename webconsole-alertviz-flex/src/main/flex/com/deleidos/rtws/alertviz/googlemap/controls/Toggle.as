import com.google.maps.LatLngBounds;
import com.google.maps.overlays.Marker;
import com.google.maps.overlays.Polygon;
import com.google.maps.overlays.Polyline;

import mx.collections.ArrayList;

private function initTogglePop():void 
{
	toggleMenu.dataProvider = new ArrayCollection();
	
	//Adds the options to toggle comments and AoIs to the toggle button
	toggleMenu.dataProvider.addItem({label:"Toggle Comments"});
	toggleMenu.dataProvider.addItem({label:"Toggle AoI"});
	
	//Creates lists that comments and AoIs will be added to in order to toggle them on or off
	types["Comments"] = new ArrayList();
	types["AoI"] = new ArrayList();
	
	//Adds event listener for when the toggle button is clicked on
	toggleMenu.addEventListener(MenuEvent.CHANGE, function(e:MenuEvent):void { 
		togglePop.label = e.label;
		togglePop_clickHandler(new MouseEvent(MouseEvent.CLICK));
	});
	togglePop.popUp = toggleMenu;
}

//Event handler for when the toggle button is clicked
private function togglePop_clickHandler(event:MouseEvent):void
{
	if (togglePop.label != "Toggle Markers") {
		var label:String = togglePop.label;
		
		//Gets the list of things with the event type that corresponds to the users selection
		var list:ArrayList = types[label.substring(7)];
		
		if (label == "Toggle AoI") {
			//Loops through the list of AoIs and reverses the visibility
			for(var a:Number = 0; a < list.length; a++) {
				list.getItemAt(a).visible = !(list.getItemAt(a).visible);
			}
			//Displays only the markers and lobs within the AoIs when its visibility is set to true
			if (aoiOn()) {
				setVisibilityAllMarkers(false);
				displayMarkersin();
			} else {
				setVisibilityAllMarkers(true);
			}
		} else {
			//Loops through the list and reverses the visibility of the objects
			if (label != "Toggle Comments" && aoiOn()) {
				setVisibilityAllMarkers(true);
				for (s = 0; s < aois.length; s++) {
					aois.getItemAt(s).visible = false;
				}
			}
			for (var s:Number = 0; s < list.length; s++) {
				list.getItemAt(s).visible = !(list.getItemAt(s).visible);
			}
		}
	}
}

private function setVisibilityAllMarkers(bool:Boolean):void
{
	//Sets the visibility for all the color markers and image markers
	for (var a:Number = 0; a < markers.length; a++) {
		Marker(markers.getItemAt(a)).visible = bool;
	}
	//Sets the visibility for all the circle markers
	for (a = 0; a < cirMarkers.length; a++) {
		Marker(cirMarkers.getItemAt(a)).visible = bool;
	}
	//Sets the visibility for all the paths
	for (a = 0; a < paths.length; a++) {
		Polyline(paths.getItemAt(a)).visible = bool;
	}
	//Sets the visibility for all the aoiPaths
	for (a = 0; a < aoiPaths.length; a++) {
		var tempList:ArrayList = ArrayList(aoiPaths.getItemAt(a));
		for (var s:Number = 0; s < tempList.length; s++) {
			Polyline(tempList.getItemAt(s)).visible = false;
		}
	}
	//Sets the visibility for all the lobs
	for (a = 0; a < lobs.length; a++) {
		Polygon(lobs.getItemAt(a)).visible = bool;
	}
}

private function displayMarkersin():void {
	//Shows or hides color markers and image markers
	for (var a:Number = 0; a < markers.length; a++) {
		var tempMarker:Marker = Marker(markers.getItemAt(a));
		tempMarker.visible = false;
		for (var s:Number = 0; s < aois.length; s++) {
			if (aoiHasLatLng(Polygon(aois.getItemAt(s)), tempMarker.getLatLng())) {
				tempMarker.visible = true;
			}	
		}
	}
	//Shows the short AoI paths
	for (a = 0; a < aoiPaths.length; a++) {
		var tempList:ArrayList = ArrayList(aoiPaths.getItemAt(a));
		for (s = 0; s < tempList.length; s++) {
			Polyline(tempList.getItemAt(s)).visible = true;
		}
	}
	//Shows or hides circle markers
	for (a = 0; a < cirMarkers.length; a++) {
		tempMarker = Marker(cirMarkers.getItemAt(a));
		tempMarker.visible = false;
		for (s = 0; s < aois.length; s++) {
			if (aoiHasLatLng(Polygon(aois.getItemAt(s)), tempMarker.getLatLng())) {
				tempMarker.visible = true;
			}
		}
	}
	//Shows or hides lobs
	for (a = 0; a < lobs.length; a++) {
		var tempLob:Polygon = Polygon(lobs.getItemAt(a));
		tempLob.visible = false;
		for (s = 0; s < aois.length; s++) {
			if (aoiHasLatLng(Polygon(aois.getItemAt(s)), tempLob.getOuterVertex(0))) {
				tempLob.visible = true;
			}
		}
	}
}

private function aoiOn():Boolean {
	if (aois.length > 0 && aois.getItemAt(0).visible) {
		return true;
	} else {
		return false;
	}

}