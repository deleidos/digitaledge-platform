//catches a color change event
public function handleColorChangeEvent(filterKey:String, color:uint):void {
	setColor(filterKey, color);
}

//changes the color of all overlays that have the specified alertKey to the specified color
private function setColor(alertKey:String, newColor:uint):void
{				
	//sets the color to correspond to the alertKey
	colors[alertKey] = newColor;
	
	//gets the list of all colorMarkers that have the specified alertKey
	var list:ArrayList = ArrayList(keys[alertKey]);
	if (list != null) {
		for(var k:Number = 0; k<list.length; k++) {
			var marker:Marker = Marker(list.getItemAt(k));
			
			//sets the color markers color
			marker.setOptions(new MarkerOptions({fillStyle: {color: newColor}}));
			
			//if there is a path at the marker, sets the paths color
			for(var i:Number = 0; i<paths.length; i++) {
				var path:Polyline = Polyline(paths.getItemAt(i));
				if(path.getVertex(path.getVertexCount() - 1).equals(marker.getLatLng())) {
					path.setOptions(new PolylineOptions({strokeStyle: {color:newColor}}));
				}
			}
			
			//if there is a LOB at the marker, sets the LOB's color
			for(var j:Number = 0; j<lobs.length; j++) {
				var lob:Polygon = Polygon(lobs.getItemAt(j));
				if(lob.getOuterVertex(0).equals(marker.getLatLng())) {
					lob.setOptions(new PolygonOptions({strokeStyle: {color:newColor}, fillStyle: {color:newColor}}));
				}
			}
		}
	}
}