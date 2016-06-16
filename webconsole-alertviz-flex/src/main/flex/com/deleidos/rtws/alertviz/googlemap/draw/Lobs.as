//draws a Line of Bearing
private function drawLOB(obj:Object, num:Number):void
{
	//sets color for the lob to be drawn
	var color:uint = LOB_COLOR;
	if(colors[obj.alertKey] != null)
	{
		color = colors[obj.alertKey];
	}
	
	//location of the base of the lob
	var lat1:Number = obj.latitude[num];
	var lon1:Number = obj.longitude[num];
	var coord1:LatLng = new LatLng(lat1,lon1);
	
	//checks to see if there is already lobs drawn at the base location
	var added:Boolean = false;
	var colorMarker:Marker;
	var imgMarker:Marker;
	for(var i:Number = 0; i<markers.length; i++) {
		var marker:Marker = Marker(markers.getItemAt(i));
		if(marker.getLatLng().equals(coord1) && obj.eventType == types[marker]) {
			if(dict[marker] != "[object Marker]") {
				added = true;
				color = uint(marker.getOptions().fillStyle.color);
				colorMarker = marker;
			} else {
				var objList:ArrayList = ArrayList(objects[marker]);
				objList.addItem(obj);
				objects[marker] = objList;
				imgMarker = marker;
			}
		}
	}
	
	//moves color and img markers to the back of the list of markers because they were updated.
	if(added) {
		markers.removeItem(colorMarker);
		markers.removeItem(imgMarker);
		markers.addItem(colorMarker);
		markers.addItem(imgMarker);
	} else {
		//draws a point at the base location because there is not already one there
		drawPoint(obj,num,LOB_COLOR);
	}
	
	var bearing:Number = obj.bearing[num];
	
	//finds the coordinates of the wedge that will be drawn
	var lat2:Number = lat1 + (0.10171 * Math.cos(bearing * (Math.PI / 180)));
	var lon2:Number = lon1 + (0.11891 * Math.sin(bearing * (Math.PI / 180)));
	var coord2:LatLng = new LatLng(lat2,lon2);
		
	var lat3:Number = lat1 + (0.10171 * Math.cos((bearing-2) * (Math.PI / 180)));
	var lon3:Number = lon1 + (0.11891 * Math.sin((bearing-2) * (Math.PI / 180)));
	var coord3:LatLng = new LatLng(lat3,lon3);
	
	var lat4:Number = lat1 + (0.10171 * Math.cos((bearing+2) * (Math.PI / 180)));
	var lon4:Number = lon1 + (0.11891 * Math.sin((bearing+2) * (Math.PI / 180)));
	var coord4:LatLng = new LatLng(lat4,lon4);
	
	//sets options for the wedge
	var polyOptions:PolygonOptions = new PolygonOptions({strokeStyle: {thickness:0, alpha:0.5, color:color}, fillStyle: {color:color, alpha:0.5} });
	
	//creates the polygon, adds it to the map, and saves it's data
	var lob:Polygon = new Polygon(new Array(coord1,coord3,coord2,coord4), polyOptions);
	lobs.addItem(lob);
	map.addOverlay(lob);
	types[lob] = obj.eventType;
	types[types[lob]].addItem(lob);
		
	//If the AoIs are on then it sets its visibility depending on whether its in the AoI or not
	if (aoiOn()) {
		lob.visible = false;
		for (var s:Number = 0; s < aois.length; s++) {
			if (aoiHasLatLng(Polygon(aois.getItemAt(s)), coord1)) {
				lob.visible = true;
			}
		}
	} else {
		lob.visible = types[types[lob]].getItemAt(0).visible;
	}
	
	objects[obj].addItem(lob);
}

private function deleteOldestLOB(obj:Object):void
{
	//oldest lob to be deleted
	var oldLOB:Polygon = Polygon(lobs.removeItemAt(0));
	var type:String = types[oldLOB];
	var latLng:LatLng = oldLOB.getOuterVertex(0);
	
	//deletes data for the oldest lob
	map.removeOverlay(oldLOB);
	types[types[oldLOB]].removeItem(oldLOB);
	delete types[oldLOB];
	
	//Finds the colorMarker and imgMarker associated with the oldest lob that is being deleted
	var colorMarker:Marker;
	var imgMarker:Marker;
	for(var j:Number = 0; j<markers.length; j++)
	{
		var tempMark:Marker = Marker(markers.getItemAt(j));
		if(tempMark.getLatLng().equals(latLng) && type == types[tempMark])
		{
			if(dict[tempMark] != "[object Marker]")
			{
				colorMarker = tempMark;
			}
			else
			{
				imgMarker = tempMark;
			}
		}
	}
	
	//deletes data for the oldest lob from the dictionary
	objects[imgMarker].removeItemAt(0);
	
	//removes the marker if nothing is left there
	if(objects[imgMarker].length == 0)
	{
		deleteMarker(obj,colorMarker,imgMarker);
	}
}