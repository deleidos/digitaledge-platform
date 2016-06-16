import com.google.maps.LatLng;

import flash.geom.Point;

//A degree multiplied by this value gives the degree value in radians
private const RADIAN_MULTIPLIER:Number = Math.PI / 180;

//draws an ellipse on the map
private function drawEllipse(obj:Object, num:Number):void
{
	var id:String = obj.objectIdValue;
	var lat:Number = obj.latitude[num];
	var lon:Number = obj.longitude[num];
	
	// For real data
	var xRadius:Number = obj.xRadius[num];
	var yRadius:Number = obj.yRadius[num];
	var angle:Number = obj.bearing[num];
	//
	
	/* for generating random ellipses
	var xRadius:Number = (Math.random() * 900000) + 100000;
	var yRadius:Number = (Math.random() * 900000) + 100000;
	var angle:Number = Math.random() * 360;
	*/
	
	var center:LatLng = new LatLng(lat, lon);
	
	//removes the old ellipse from the map
	removeEllipse(obj.objectIdValue);
	var polyOptions:PolygonOptions;
	var pointList:Array = new Array();
		
	if(colors[obj.alertKey] != null)
	{
		polyOptions = new PolygonOptions({strokeStyle: {color:colors[obj.alertKey]}, fillStyle: {color:colors[obj.alertKey], alpha:0.75} });
	}
			
	//use default color
	else
	{
		polyOptions = new PolygonOptions({strokeStyle: {color:ELLIPSE_COLOR}, fillStyle: {color:ELLIPSE_COLOR, alpha:0.75} });
	}
		
	//For more round-looking ellipses, increase the steps variable
	var steps:int = 36;
	
	var angleChangePerStep:Number = 360 / steps;
	var latInRadians:Number = center.latRadians();
	var lonInRadians:Number = center.lngRadians();
	
	for (var i:Number = 0; i < 360; i += angleChangePerStep)
	{
		var radians:Number = i * RADIAN_MULTIPLIER;
		
		//formula for the radius of an ellipse is
		//r(angle) = a*b / sqrt((b*cos(angle))^2 + (a*sin(angle))^2) 
		var radius:Number = (xRadius * yRadius) / Math.sqrt( Math.pow((yRadius * Math.cos(radians)), 2) + Math.pow((xRadius * Math.sin(radians)), 2));
		
		//radius of a unit ellipse at angle a is the same as that of a unit rotated ellipse of angle b at angle (a+b)%360
		var actualAngle:Number = (i + angle) % 360;
		
		//find the LatLng that corresponds to a location of radius meters away from the center at a certain angle
		//this LatLng corresponds to a location on the ellipse
		var newLatLng:LatLng = getLatLng(latInRadians, lonInRadians, radius, actualAngle);
		
		pointList.push(newLatLng);
	}

		
	//creates polygon, adds it to the map and saves it's data
	var ellipse:Polygon = new Polygon(pointList,polyOptions);
	map.addOverlay(ellipse);
	ellipses.addItem(ellipse);
	dict[ellipse] = obj.objectIdValue;
	types[ellipse] = obj.eventType;
	types[types[ellipse]].addItem(ellipse);
		
	//If the AoIs are on then it sets its visibility depending on whether its in the AoI or not
	if (aoiOn()) {
		ellipse.visible = false;
		for (var s:Number = 0; s < aois.length; s++) {
			//TODO: currently only shows the ellipse if it's center is in the AoI
			//perhaps it would be better to check if the ellipse intersects the AoI
			//to determine if it should appear on the map?
			if (aoiHasLatLng(Polygon(aois.getItemAt(s)), center)) { 
				ellipse.visible = true;
			}
		}
	}else
		ellipse.visible = types[types[ellipse]].getItemAt(0).visible;
}

//removes an ellipse from the map and returns it
private function removeEllipse(id:String):Polygon
{
	for(var i:Number = 0; i<ellipses.length; i++)
	{
		var ellipse:Polygon = Polygon(ellipses.getItemAt(i));
		if(dict[ellipse] == id)
		{
			map.removeOverlay(ellipse);
			ellipses.removeItem(ellipse);
			types[types[ellipse]].removeItem(ellipse);
			delete dict[ellipse];
			delete types[ellipse];
			return ellipse;
		}
	}
	return null;
}

//Get the LatLng that corresponds to a point of distance d meters from a lat long with a bearing (in degrees)
private function getLatLng(latInRadians:Number, lonInRadians:Number, d:Number, bearing:Number):LatLng{
	var angularDistance:Number = d / LatLng.EARTH_RADIUS;
	bearing *= RADIAN_MULTIPLIER;
 	
	var newLat:Number = Math.asin( Math.sin(latInRadians) * Math.cos(angularDistance) +
								   (Math.cos(latInRadians) * Math.sin(angularDistance) * Math.cos(bearing)) );
	
	var newLon:Number = lonInRadians + Math.atan2( Math.sin(bearing) * Math.sin(angularDistance) * Math.cos(latInRadians),
												   Math.cos(angularDistance) - Math.sin(latInRadians) * Math.sin(newLat)
												 );

	return LatLng.fromRadians(newLat, newLon);
}