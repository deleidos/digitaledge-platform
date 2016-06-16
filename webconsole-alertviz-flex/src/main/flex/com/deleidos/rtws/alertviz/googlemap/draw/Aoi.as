import com.google.maps.LatLng;
import com.google.maps.LatLngBounds;
import com.google.maps.MapEvent;
import com.google.maps.MapMouseEvent;
import com.google.maps.overlays.Marker;
import com.google.maps.overlays.Polygon;
import com.google.maps.overlays.Polyline;

import mx.collections.ArrayList;

protected var aoiMenu:Menu = new Menu();

//Initializes the AoI button
protected function initAoiPop():void {
	aoiMenu.dataProvider = [{label:AOI_DRAW}, {label:AOI_FINISH}, {label:AOI_EDIT}, {label:AOI_RESET}];
	aoiMenu.addEventListener(MenuEvent.CHANGE, aoiPop_changeHandler);
	aoiPop.popUp = aoiMenu;
}

protected function aoiPop_changeHandler(e:MenuEvent):void {
	aoiPop.label = e.label;
	aoiPop_clickHandler(new MouseEvent(MouseEvent.CLICK));
}

//Event handler for when the user clicks on the AoI button
protected function aoiPop_clickHandler(event:MouseEvent):void {
	commentPop.enabled = false;
	
	if (aoiPop.label == AOI_DRAW) {
		aoiMarkers = new ArrayList();
		for(var i:Number = 0; i < aois.length; i++) {
			aois.getItemAt(i).visible = true;
		}
		inputArea.text = "Click the map where vertices of the AoI should go. When finished click the 'Finish Drawing' button.";
		map.addEventListener(MapMouseEvent.CLICK, mapClick1);
		aoiPop.enabled = false;
	} else if (aoiPop.label == AOI_FINISH) {
		for (var a:Number = 0; a < aoiMarkers.length; a++) {
			aoiMarkers.getItemAt(a).visible = false;
		}
		map.removeEventListener(MapMouseEvent.CLICK, mapClick3);
		inputArea.text = "";
		aoiPop.label = AOI_EDIT;
		commentPop.enabled = true;
		aoiMenu.addEventListener(MenuEvent.CHANGE, aoiPop_changeHandler);
		displayMarkersin();
		for (a = 0; a < paths.length; a++) {
			updateAoIPaths(Polyline(paths.getItemAt(a)), Polyline(paths.getItemAt(a)).getOptions());
		}
	} else if (aoiPop.label == AOI_EDIT) {
		if(aois.length > 1) {
			inputArea.text = "Click inside the AoI you would like to edit.";
			map.addEventListener(MapMouseEvent.CLICK, selectAoi);
			aoiPop.enabled = false;
		} else {
			aoiArea = Polygon(aois.getItemAt(0));
			getVertices();
			inputArea.text = "Drag the AoI's markers to the intended position. When finished click the 'Finish Drawing' button. ";
			aoiPop.label = AOI_FINISH;
		}
		aoiMenu.removeEventListener(MenuEvent.CHANGE, aoiPop_changeHandler);
	} else if (aoiPop.label == AOI_RESET) {
		if(aois.length > 1) {
			inputArea.text = "Click inside the AoI you would like to delete.";
			map.addEventListener(MapMouseEvent.CLICK, deleteAoi);
			aoiPop.enabled = false;		
		} else {
			types["AoI"].removeItem(aoiArea);
			map.removeOverlay(aoiArea);
			aois.removeAll();
			aoiArea = null;
			aoiMarkers = new ArrayList();
			aoiPop.label = AOI_DRAW;
			commentPop.enabled = true;
			for (a = 0; a < paths.length; a++) {
				updateAoIPaths(Polyline(paths.getItemAt(a)), Polyline(paths.getItemAt(a)).getOptions());
			}
			setVisibilityAllMarkers(true);
		}
	} else {
		inputArea.text = "There is no AoI to finish, edit, or reset.";
		commentPop.enabled = true;
	}
}

//Deletes a AoI by asking the user to click inside of it
private function deleteAoi(e:MapMouseEvent):void {
	var tempList:Array = new Array();
	for(var i:Number = 0; i < aois.length; i++) {
		if(aoiHasLatLng(Polygon(aois.getItemAt(i)), e.latLng)) {
			tempList.push(aois.getItemAt(i));
		}
	}
	
	if(tempList.length == 0) {
		inputArea.text = "There is no AoI at that point.";
	} else if(tempList.length > 1) {
		inputArea.text = "Please click on only one AoI.";
	} else {
		inputArea.text = "";
		aoiArea = tempList[0];
		aoiArea.visible = false;
		aois.removeItem(aoiArea);
		types["AoI"].removeItem(aoiArea);
		map.removeOverlay(aoiArea);
		aoiArea = Polygon(aois.getItemAt(0));
		aoiPop.label = AOI_EDIT;
		commentPop.enabled = true;
		aoiPop.enabled = true;
		map.removeEventListener(MapMouseEvent.CLICK, deleteAoi);
		//Updates the short AoI paths
		for (var a:Number = 0; a < paths.length; a++) {
			updateAoIPaths(Polyline(paths.getItemAt(a)), Polyline(paths.getItemAt(a)).getOptions());
		}
		if (aoiOn()) {
			displayMarkersin();
		} else {
			setVisibilityAllMarkers(true);
		}
	}
}

//Creates the first 2 Marker for the AoI
private function mapClick1(e:MapMouseEvent):void {
	var markerOption:MarkerOptions = new MarkerOptions();
	markerOption.icon = new penIcon();
	markerOption.draggable = true;
	markerOption.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
	var tempMarker:Marker = new Marker(e.latLng, markerOption);
	aoiMarkers.addItem(tempMarker);
	map.addOverlay(tempMarker);
	
	if (aoiMarkers.length == 2) {
		map.removeEventListener(MapMouseEvent.CLICK, mapClick1);
		map.addEventListener(MapMouseEvent.CLICK, mapClick2);	
	}
}


//Creates the third Marker for the AoI and creates the polygon
private function mapClick2(e:MapMouseEvent):void {
	var markerOption:MarkerOptions = new MarkerOptions();
	markerOption.icon = new penIcon();
	markerOption.draggable = true;
	markerOption.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
	var tempMarker:Marker = new Marker(e.latLng, markerOption);
	tempMarker.addEventListener(MapEvent.OVERLAY_MOVED, updateArea);
	aoiMarkers.getItemAt(0).addEventListener(MapEvent.OVERLAY_MOVED, updateArea);
	aoiMarkers.getItemAt(1).addEventListener(MapEvent.OVERLAY_MOVED, updateArea);
	aoiMarkers.addItem(tempMarker);
	map.addOverlay(tempMarker);
	
	var geoArray:Array = new Array();
	for (var a:Number = 0; a < aoiMarkers.length; a++) {
		geoArray.push(aoiMarkers.getItemAt(a).getLatLng());
	}
	aoiArea = new Polygon(geoArray);
	var options:PolygonOptions = new PolygonOptions({fillStyle:{color:aoiColors[0]}, strokeStyle:{color:aoiColors[1]}});
	aoiArea.setOptions(options);
	map.addOverlay(aoiArea);
	aois.addItem(aoiArea);
	types["AoI"].addItem(aoiArea);	
	map.removeEventListener(MapMouseEvent.CLICK, mapClick2);
	map.addEventListener(MapMouseEvent.CLICK, mapClick3);
	
	aoiPop.label = AOI_FINISH;
	aoiPop.enabled = true;
	
	aoiMenu.removeEventListener(MenuEvent.CHANGE, aoiPop_changeHandler);
}

private function mapClick3(e:MapMouseEvent):void {
	var markerOption:MarkerOptions = new MarkerOptions();
	markerOption.icon = new penIcon();
	markerOption.draggable = true;
	markerOption.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
	var tempMarker:Marker = new Marker(e.latLng, markerOption);
	tempMarker.addEventListener(MapEvent.OVERLAY_MOVED, updateArea);
	aoiMarkers.addItem(tempMarker);
	map.addOverlay(tempMarker);
	
	updateArea(new MapEvent(MapEvent.OVERLAY_ADDED, ""));
}

//Lets the user select a AoI by clicking inside of it
private function selectAoi(e:MapMouseEvent):void {
	var tempList:Array = new Array();
	for(var i:Number = 0; i < aois.length; i++) {
		if(aoiHasLatLng(Polygon(aois.getItemAt(i)), e.latLng)) {
			tempList.push(aois.getItemAt(i));
		}
	}
	
	if(tempList.length == 0) {
		inputArea.text = "There is no AoI at that point.";
	} else if(tempList.length > 1) {
		inputArea.text = "Please click on only one AoI.";
	} else {
		inputArea.text = "Drag the AoI's markers to the intended position. When finished click the 'Finish Drawing' button.";
		aoiArea = tempList[0];
		getVertices();
		map.removeEventListener(MapMouseEvent.CLICK, selectAoi);
		aoiPop.enabled = true;
		aoiPop.label = AOI_FINISH;
	}
}

//Repopulates aoiMarkers with the selected AoI's vertices
private function getVertices():void {
	aoiMarkers = new ArrayList();
	for (var a:Number = 0; a < aoiArea.getVertexCount(0) - 1; a++) {
		var markerOption:MarkerOptions = new MarkerOptions();
		markerOption.icon = new penIcon();
		markerOption.draggable = true;
		markerOption.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
		var tempMarker:Marker = new Marker(aoiArea.getVertex(0, a), markerOption);
		tempMarker.addEventListener(MapEvent.OVERLAY_MOVED, updateArea);
		aoiMarkers.addItem(tempMarker);
		map.addOverlay(tempMarker);
	}
}

//Updates the Polygon after a Marker has been dragged
private function updateArea(e:MapEvent):void {
	aois.removeItem(aoiArea);
	types["AoI"].removeItem(aoiArea);
	map.removeOverlay(aoiArea);
	
	var geoArray:Array = new Array();
	for (var a:Number = 0; a < aoiMarkers.length; a++) {
		geoArray.push(aoiMarkers.getItemAt(a).getLatLng());
	}
	aoiArea = new Polygon(geoArray);
	var options:PolygonOptions = new PolygonOptions({fillStyle:{color:aoiColors[0]}, strokeStyle:{color:aoiColors[1]}});
	aoiArea.setOptions(options);
	map.addOverlay(aoiArea);
	aois.addItem(aoiArea);
	types["AoI"].addItem(aoiArea);
}

//Returns true if the LatLng is in the polygon, false if not (This function works better than LatLngBounds.containsLatLng)
private function aoiHasLatLng(area:Polygon, latlng:LatLng):Boolean {
	var bounds:LatLngBounds = area.getLatLngBounds();
	if(bounds != null && !bounds.containsLatLng(latlng)) {
		return false;
	}
	
	// Raycast point in polygon method
	var numPoints:Number = area.getVertexCount(0);
	var inPoly:Boolean = false;
	var j:Number = numPoints - 1;
	
	for(var i:Number = 0; i < numPoints; i++) {
		var vertex1:LatLng = area.getVertex(0, i);
		var vertex2:LatLng = area.getVertex(0, j);
		
		if (vertex1.lng() < latlng.lng() && vertex2.lng() >= latlng.lng() || vertex2.lng() < latlng.lng() && vertex1.lng() >= latlng.lng()) {
			if (vertex1.lat() + (latlng.lng() - vertex1.lng()) / (vertex2.lng() - vertex1.lng()) * (vertex2.lat() - vertex1.lat()) < latlng.lat()) {
				inPoly = !inPoly;
			}
		}
		
		j = i;
	}
	
	return inPoly;
}