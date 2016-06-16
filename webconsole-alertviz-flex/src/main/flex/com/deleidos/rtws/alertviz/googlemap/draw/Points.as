import com.google.maps.LatLngBounds;
import com.google.maps.overlays.Marker;

private function drawPoint(obj:Object, num:Number, color:uint):void {
	//Sets color for the point
	if(colors[obj.alertKey] != null) {
		color = colors[obj.alertKey];
	}
	
	//Checks to see if there is already a point located at the location for the new point
	//If there is already a point located there, it updates the information that will go into
	//	the info window so an accordion will be made.
	var added:Boolean = false;
	var colorMarker:Marker;
	var imgMarker:Marker;
	for(var i:Number = 0; i<markers.length; i++) {
		var thisMarker:Marker = Marker(markers.getItemAt(i));
		if(thisMarker.getLatLng().equals(new LatLng(obj.latitude[num],obj.longitude[num])) && types[thisMarker] == obj.eventType) {
			if(dict[thisMarker].toString() == "[object Marker]") {
				added = true;
				var objList:ArrayList = objects[thisMarker];
				objList.addItem(obj);
				objects[thisMarker] = objList;
				imgMarker = thisMarker;
			} else {
				colorMarker = thisMarker;
			}
		}
	} 
	
	//Moves data around
	if(added) {
		markers.removeItem(colorMarker);
		markers.removeItem(imgMarker);
		markers.addItem(colorMarker);
		markers.addItem(imgMarker);
		if(obj.shapeKey[num] == "Point" || obj.shapeKey[num] == "Path")  {
			objects[obj].addItem(imgMarker);
			objects[obj].addItem(colorMarker);
		}
	} else {
		//Draws a new point
		//Creates colorMarker, adds it to the map, and saves it's data
		var marker:Marker = new Marker(new LatLng(obj.latitude[num],obj.longitude[num]));
		marker.setOptions(new MarkerOptions({fillStyle: {color: color} }));
		map.addOverlay(marker);
		marker.visible = true;			
		markers.addItem(marker);
		dict[marker] = obj.objectIdValue;
		types[marker] = obj.eventType;
		
		//creates imgMarker, adds it to the map, and saves it's data
		var markerImg:Marker = new Marker(new LatLng(obj.latitude[num],obj.longitude[num]));
		markerImg.setOptions(getMarkerOptions(obj));
		map.addOverlay(markerImg);
		markers.addItem(markerImg);
		dict[markerImg] = marker;
		types[markerImg] = obj.eventType;
		
		objects[markerImg] = new ArrayList();		
		objects[markerImg].addItem(obj);
		markerImg.addEventListener(MapMouseEvent.ROLL_OVER, showInfo);
		markerImg.addEventListener(MapMouseEvent.CLICK, showInfo);
		markerImg.addEventListener(MapMouseEvent.ROLL_OUT, hideInfo);
		
		var list:ArrayList = types[obj.eventType];
		list.addItem(marker);
		list.addItem(markerImg);
		types[obj.eventType] = list;
		
		keys[obj.alertKey].addItem(marker);
		
		//sets visibility of the markers
		//If the AoIs are on then it sets its visibility depending on whether its in the AoI or not
		if (aoiOn()) {
			marker.visible = false;
			markerImg.visible = false;
			for (var s:Number = 0; s < aois.length; s++) {
				if (aoiHasLatLng(Polygon(aois.getItemAt(s)), marker.getLatLng())) {
					marker.visible = true;
					markerImg.visible = true;
				}
			}
		} else {
			marker.visible = list.getItemAt(0).visible;
			markerImg.visible = list.getItemAt(0).visible;
		}
		
		if(obj.shapeKey[num] == "Point" || obj.shapeKey[num] == "Path")  {
			objects[obj].addItem(markerImg);
			objects[obj].addItem(marker);
		}
	}
}

//sets the marker image to whatever is specified in the JSONObject
private function getMarkerOptions(obj:Object):MarkerOptions {
	var options:MarkerOptions = new MarkerOptions();
	switch(obj.markerImg) {
		case "car" :
			options.icon = new carIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,2);
			break;
		case "radar" :
			options.icon = new radarIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,1);
			break;
		case "camera" :
			options.icon = new cameraIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,1);
			break;
		case "plane" :
			options.icon = new planeIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,2);
			break;
		case "boat" :
			options.icon = new boatIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,2);
			break;
		case "bus" :
			options.icon = new busIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,2);
			break;
		case "helicopter" :
			options.icon = new helicopterIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,2);
			break;
		case "person" :
			options.icon = new personIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,2);
			break;
		case "question" :
			options.icon = new questionIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,2);
			break;
		case "subway" :
			options.icon = new subwayIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,2);
			break;
		case "train" :
			options.icon = new trainIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,2);
			break;
		default :
			options.icon = new defaultIcon();
			options.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_BOTTOM;
			options.iconOffset = new Point(0,2);
			break;
	}
	return options;
}