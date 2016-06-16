import com.google.maps.overlays.Polyline;
import com.google.maps.overlays.PolylineOptions;

import mx.collections.ArrayList;

private function drawPath(obj:Object, num:Number):void
{

	var id:String = obj.objectIdValue;
	var lat:Number = obj.latitude[num];
	var lon:Number = obj.longitude[num];
	
	//moves the marker to it's new position and stores the markers old position
	//returns invalid value if the marker doesn't exist yet
	var coord:LatLng = moveMarker(id,lat,lon,obj);
	
	//if the old location is an invalid value then create a new marker
	if(coord.equals(new LatLng(200,200))) {
		drawPoint(obj,num,PATH_COLOR);
	}
	
	//draw a path that connects the old location to the new one
	else {
		//removes the old path from the map and save it as a variable
		var oldPath:Polyline = removePath(obj.objectIdValue, coord);
		var polyOptions:PolylineOptions;
		
		//generate a list of locations that make up the path
		var pathList:Array = new Array();
		for(var j:Number = 0; j<oldPath.getVertexCount(); j++) {
			pathList.push(oldPath.getVertex(j));
		}
		
		//Sets the new path's options the same as the old path's options
		if(pathList.length > 1){
			polyOptions = oldPath.getOptions();
		} else if(colors[obj.alertKey] != null) {
			//If there is no old path, but there is a color associated with the objects alertKey, then set the color
			polyOptions = new PolylineOptions({strokeStyle: {color:colors[obj.alertKey]} });
		} else {
			//Use default color
			polyOptions = new PolylineOptions({strokeStyle: {color:PATH_COLOR} });
		}
		
		//add new location to the path
		pathList.push(new LatLng(obj.latitude[num],obj.longitude[num]));
		
		//creates polyline
		var path:Polyline = new Polyline(pathList,polyOptions);
		
		//adds new polyline to the map and saves it's data
		map.addOverlay(path);
		paths.addItem(path);
		dict[path] = obj.objectIdValue;
		types[path] = obj.eventType;
		types[types[path]].addItem(path); 
		
		//Creates and only displays the portion of the path which is inside the AoI
		if (aoiOn()) {
			updateAoIPaths(path, polyOptions);
			path.visible = false;
		} else {
			path.visible = types[types[path]].getItemAt(0).visible;
		}
	}
}

//moves the color and img markers to a new location and returns the old location
private function moveMarker(id:String, lat:Number, lon:Number, obj:Object):LatLng {
	var moved:Boolean = false;
	var coord:LatLng = new LatLng(200,200);
	var colorMarker:Marker;
	var imgMarker:Marker;
	
	//loops through all the markers
	for(var i:Number = 0; i<markers.length; i++) {
		var marker:Marker = Marker(markers.getItemAt(i));
		
		//if the marker has the same id as the one specified
		if(dict[marker] == id || dict[dict[marker]] == id) {
			//set the old location to return
			coord = marker.getLatLng();
			
			//move the marker
			marker.setLatLng(new LatLng(lat,lon));
			
			//If the AoIs are on then it sets its visibility depending on whether its in the AoI or not
			if (aoiOn()) {
				marker.visible = false;
				for (var s:Number = 0; s < aois.length; s++) {
					if (aoiHasLatLng(Polygon(aois.getItemAt(s)), coord)) {
						marker.visible = true;
					}
				}
			}
			
			moved = true;
			
			//if the marker is an imgMarker then create and draw a cirMarker and save its data
			if(dict[dict[marker]] == id) {
				var cirMarker:Marker = new Marker(coord);
				var markerOptions:MarkerOptions = new MarkerOptions();
				markerOptions.icon = new circleIcon();
				markerOptions.iconAlignment = MarkerOptions.ALIGN_HORIZONTAL_CENTER + MarkerOptions.ALIGN_VERTICAL_CENTER;
				cirMarker.setOptions(markerOptions);
				dict[cirMarker] = dict[marker];
				types[cirMarker] = types[marker];
				var list:ArrayList = types[obj.eventType];
				list.addItem(cirMarker);
				types[obj.eventType] = list;
				objects[cirMarker] = objects[marker];
				cirMarker.addEventListener(MapMouseEvent.ROLL_OVER, showInfo);
				cirMarker.addEventListener(MapMouseEvent.CLICK, showInfo);
				cirMarker.addEventListener(MapMouseEvent.ROLL_OUT, hideInfo);
				cirMarker.visible = marker.visible;
				map.addOverlay(cirMarker);
				cirMarkers.addItem(cirMarker);
				
				objects[marker] = new ArrayList();				
				objects[marker].addItem(obj);
				imgMarker = marker;
			} else {
				colorMarker = marker;
			}
		}
	}
	
	//update data if the marker was moved.
	if(moved) {
		markers.removeItem(colorMarker);
		markers.removeItem(imgMarker);
		markers.addItem(colorMarker);
		markers.addItem(imgMarker);
		objects[obj].addItem(imgMarker);
		objects[obj].addItem(colorMarker);
	}
	
	//moves the map if the marker is supposed to be followed
	if(followMarker == true && idToFollow == id) {
		map.panTo(new LatLng(lat,lon));
	}
	return coord;
}

//removes a path from the map and returns the path that was removed
private function removePath(id:String, coord:LatLng):Polyline {
	for(var i:Number = 0; i<paths.length; i++) {
		var path:Polyline = Polyline(paths.getItemAt(i));
		if(dict[path] == id) {
			map.removeOverlay(path);
			paths.removeItem(path);
			types[types[path]].removeItem(path);
			delete dict[path];
			delete types[path];
			return path;
		}
	}
	return new Polyline(new Array(coord));
}
//Updates all of the short AoI paths
private function updateAoIPaths(path:Polyline, polyOptions:PolylineOptions):void {
	//Finds the index in aoiPaths which has the ArrayList of short AoI paths which are associated with the full path
	var index:Number = findIndexofAoIPaths(path);
	var tempList:ArrayList;
	if (index != -1) {
		tempList = ArrayList(aoiPaths.getItemAt(index));
		aoiPaths.removeItemAt(index);
		for (var a:Number = 0; a < tempList.length; a++) {
			map.removeOverlay(Polyline(tempList.getItemAt(a)));
		}
	}
	tempList = new ArrayList();
	
	//Creates and only displays the portion of the path which is inside the AoI
	for (a = 0; a < aois.length; a++) {
		var tempPathList:Array = new Array();
		var prevIndex:Number = -2;
		for (var s:Number = 0; s < path.getVertexCount(); s++) {
			if (aoiHasLatLng(Polygon(aois.getItemAt(a)), path.getVertex(s))) {
				tempPathList.push(path.getVertex(s));
				prevIndex = s;
			} else if (prevIndex == s-1){
				prevIndex = -2;
				tempList.addItem(new Polyline(tempPathList, polyOptions));
				map.addOverlay(Polyline(tempList.getItemAt(tempList.length - 1)));
				tempPathList = new Array();
			}
		}
		if (prevIndex == path.getVertexCount() - 1) {
			tempList.addItem(new Polyline(tempPathList, polyOptions));
			map.addOverlay(Polyline(tempList.getItemAt(tempList.length - 1)));
		}
		//tempList.addItem(new Polyline(tempPathList, polyOptions));
		//map.addOverlay(Polyline(tempList.getItemAt(tempList.length - 1)));
	}
	
	if (tempList.length > 0) {
		aoiPaths.addItem(tempList);
	}
}

private function findIndexofAoIPaths(path:Polyline):Number {
	for (var a:Number = 0; a < aoiPaths.length; a++) {
		var tempAoi:Polyline = Polyline(ArrayList(aoiPaths.getItemAt(a)).getItemAt(0));
		var startIndex:Number = -1;
		if (tempAoi.getVertexCount() != 0) {
			for (var s:Number = 0; s < path.getVertexCount(); s++) {
				if (tempAoi.getVertex(0).equals(path.getVertex(s))) {
					startIndex = s;
					break;
				}
			}
			var containsAll:Boolean = true;
			s = 0;
			while (startIndex != -1 && s < tempAoi.getVertexCount() && startIndex < path.getVertexCount() && containsAll) {
				if (tempAoi.getVertex(s).equals(path.getVertex(startIndex))) {
					s++;
					startIndex++;
				} else {
					containsAll = false;
				}
			}
			
			if (containsAll && startIndex != -1) {
				return a;
			}
		}
	}
	return -1;
}