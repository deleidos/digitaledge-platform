import com.adobe.serialization.json.JSON;
import com.google.maps.LatLng;
import com.google.maps.controls.ControlPosition;
import com.google.maps.controls.MapTypeControl;
import com.google.maps.controls.NavigationControl;
import com.google.maps.controls.NavigationControlOptions;
import com.google.maps.controls.ScaleControl;
import com.google.maps.overlays.Marker;
import com.google.maps.overlays.Polyline;
import com.google.maps.overlays.PolylineOptions;
import com.deleidos.rtws.alertviz.models.Alert;
import com.deleidos.rtws.commons.util.NumberUtils;

import flash.events.Event;
import flash.net.URLLoader;
import flash.net.URLRequest;
import flash.text.TextField;

import mx.collections.ArrayList;
import mx.core.FlexGlobals;

private var restoreWidth:Number = 0;
private var restoreHeight:Number = 0;

private function onMapReady(event:Event):void {
	//load config file
	var loader:URLLoader = new URLLoader();
	/**
	 * Note: This config file used to be configurable via flash vars.  Instead of that,
	 * we will make it data model (including version) based and pulled from the systems
	 * repo if we are going to persue AlertViz long term and as an official app.
	 */
	loader.load(new URLRequest("config.txt"));
	loader.addEventListener(Event.COMPLETE, saveConfig);
}

//saves the config file as a JSONObject
private function saveConfig(e:Event):void
{
	var txtFld:TextField = new TextField();
	txtFld.text = e.target.data;
	var stringConfig:String = "";
	for(var i:Number = 0; i<txtFld.numLines; i++)
	{
		var line:String = txtFld.getLineText(i);
		stringConfig += line;		
	}
	config = JSON.decode(stringConfig);
}

/* 
 * Transforms the alert object into the dynamic object utilized by the map code.  This is a stop gap until the rest
 * of the map code can be refactored to share a common model object with the rest of the app.
 */
public function addAlertToMap(alert:Alert):void {
	var myAlert:Object = getAlertObject(alert.record, alert.model, alert.id, alert.criteria.name, alert.criteria.key);
	addAlert(myAlert);
}

private function parseCoord(obj:Object):Number {
	var result:Number = NaN;
	
	if(obj != null) {
		if(obj is Number) {
			result = Number(obj);
		}
		else if(obj is String) {
			result = NumberUtils.parseNumber(obj as String);
		}
	}
	
	return result;
}

//adds an alert to the map
private function addAlert(myAlert:Object):void {	
	objects[myAlert] = new ArrayList();
	
	//checks to see if the alert is valid (does not have any null latitudes or longitudes)
	var validGeos:Boolean = false;
	
	if(myAlert.hasOwnProperty("latitude") && myAlert.hasOwnProperty("longitude")) {
		
		var latitudes:Array = (myAlert["latitude"] as Array);
		var longitudes:Array = (myAlert["longitude"] as Array);
		
		if(latitudes != null && longitudes != null && latitudes.length > 0 && latitudes.length == longitudes.length) {
			validGeos = true;
			
			for(var z:Number = 0; z < latitudes.length; z++)
			{
				var tmpLat:Number = parseCoord(latitudes[z]);
				
				if(isNaN(tmpLat)) {
					validGeos = false;
					break;
				}
				
				latitudes[z] = tmpLat;
			}
			
			if(validGeos) {
				for(var y:Number = 0; y < longitudes.length; y++)
				{
					var tmpLon:Number = parseCoord(longitudes[y]);
					
					if(isNaN(tmpLon)) {
						validGeos = false;
						break;
					}
					
					longitudes[y] = tmpLon;
				}
			}
		}
	}
	
	//only puts alert on map if it is valid
	if(validGeos)
	{
		var contains:Boolean = false;
	
		//Adds the event type of the JSON object to the event type list if it doesn't already exist.
		for(var i:Number = 0; i<eventTypes.length; i++) {
			if(myAlert.eventType == eventTypes.getItemAt(i)) {
				contains = true;
			}
		}
		if(!contains) {
			eventTypes.addItem(myAlert.eventType);
			types[myAlert.eventType] = new ArrayList();
			
			//adds the event type to the toggle button
			toggleMenu.dataProvider.addItem({label:"Toggle " + myAlert.eventType});
		}
		
		contains = false;
		
		//adds the alert key of the JSON object to the alertKeys list if it doesn't already exist
		for(var j:Number = 0; j<alertKeys.length; j++)
		{
			if(myAlert.alertKey == alertKeys.getItemAt(j))
			{
				contains = true;
			}
		}
		if(!contains)
		{
			alertKeys.addItem(myAlert.alertKey);
			keys[myAlert.alertKey] = new ArrayList();
		}
		
		//draws the alert on the map
		shapeKeys(myAlert);
		
		//moves the map to focus on the alert if the alertKey is supposed to be followed
		if(follow == true && myAlert.alertKey == alertKeyToFollow)
		{
			map.panTo(new LatLng(myAlert.latitude[myAlert.NumGeos - 1], myAlert.longitude[myAlert.NumGeos - 1]));
		}
		
		//add the alert to the list of objects on the map
		objectsOnMap.addItem(myAlert);
		
		//deletes items from the map until it is under the limit
		while(objectsOnMap.length > queueLimit)
		{
			deleteItemFromMap();
		}
	}
}

//calls functions to draw objects on the map depending on what type of object the JSON describes
private function shapeKeys(obj:Object):void {
	for(var i:Number = 0; i<obj.NumGeos; i++) {
		if(obj.shapeKey[i] == "Point") {
			drawPoint(obj, i, POINT_COLOR);
		} else if(obj.shapeKey[i] == "Path") {
			drawPath(obj, i);
		} else if(obj.shapeKey[i] == "LOB") {
			drawLOB(obj, i);
		} else if(obj.shapeKey[i] == "Ellipse"){
			drawEllipse(obj, i);
		}
	}
}

//event listener that sets the queue limit when the user changes it in alertViz
public function handleQueueLimitChangeEvent(newLimit:Number):void {
	queueLimit = newLimit;
}

//turns following on or off for the specified alertKey
public function setFollow(alertType:String, alertKey:String):void
{
	if(alertType.search("Off") != -1)
	{
		follow = false;
	}
	if(alertType.search("On") != -1)
	{
		follow = true;
	}
	alertKeyToFollow = alertKey;
}

//deletes the oldest item on the map
private function deleteItemFromMap():void
{
	//object to be deleted
	var obj:Object = objectsOnMap.removeItemAt(0);
	
	//loops through all the geos
	for(var i:Number = 0; i<obj.NumGeos; i++) 
	{
		var imgMarker:Marker;
		var colorMarker:Marker;
		if(obj.shapeKey[i] == "Point") 
		{
			//Finds the colorMarker and imgMarker associated with the point that is being deleted
			imgMarker = objects[obj].getItemAt(i*2);
			colorMarker = objects[obj].getItemAt((i*2) + 1);
			
			//deletes data for the point from the dictionary
			objects[imgMarker].removeItemAt(0);
			
			//removes the marker if nothing is left there
			if(objects[imgMarker].length == 0)
			{
				deleteMarker(obj,colorMarker,imgMarker);
			}
		} 
		else if(obj.shapeKey[i] == "Path") 
		{
			var list:ArrayList = objects[obj];		
			//Finds the colorMarker and imgMarker associated with the point that is being deleted
			imgMarker = objects[obj].getItemAt(i*2);
			colorMarker = objects[obj].getItemAt((i*2) + 1);
			
			//removes path to delete a cirMarker from
			var path:Polyline = removePath(obj.objectIdValue, colorMarker.getLatLng());
			var options:PolylineOptions = path.getOptions();
			
			//generates list of latLng's that make up the path
			var pathList:Array = new Array();
			for(var j:Number = 0; j<path.getVertexCount(); j++)
			{
				pathList.push(path.getVertex(j));
			}
			
			//removes the marker if nothing is left there
			if(pathList.length == 1)
			{
				deleteMarker(obj,colorMarker,imgMarker);
			}
			else
			{
				//draws new path and stores data
				pathList = pathList.reverse();
				pathList.pop();
				pathList = pathList.reverse();
				var newPath:Polyline = new Polyline(pathList,options);
				map.addOverlay(newPath);
				paths.addItem(newPath);
				dict[newPath] = obj.objectIdValue;
				types[newPath] = obj.eventType;
				types[types[newPath]].addItem(newPath);
				newPath.visible = path.visible;
				
				//removes cirMarker and its data
				var cirMarker:Marker = Marker(cirMarkers.removeItemAt(0));
				map.removeOverlay(cirMarker);
				types[obj.eventType].removeItem(cirMarker);
				delete objects[cirMarker];
				delete dict[cirMarker];
				delete types[cirMarker];
			}
		}
		else if(obj.shapeKey[i] == "Ellipse")
		{
			removeEllipse(obj.objectIdValue);
		}
		else if(obj.shapeKey[i] == "LOB") 
		{
			deleteOldestLOB(obj);
		}
	}
	delete objects[obj];
	
}

//removes a color marker and img marker and deletes all of it's data
private function deleteMarker(obj:Object, colorMarker:Marker, imgMarker:Marker):void
{
	var type:String = obj.eventType;
	map.removeOverlay(colorMarker);
	map.removeOverlay(imgMarker);
	markers.removeItem(colorMarker);
	markers.removeItem(imgMarker);
	delete dict[colorMarker];
	delete dict[imgMarker];
	delete types[colorMarker];
	delete types[imgMarker];
	delete objects[imgMarker];
	types[type].removeItem(colorMarker);
	types[type].removeItem(imgMarker);
	keys[obj.alertKey].removeItem(colorMarker);
}

public function resizeMap(eventName:String, width:Number, height:Number):void {	
	if (eventName == 'resizeEnd') {
		this.map.width = width - 2;
		this.map.height = height - 29;
		restoreWidth = width;
		restoreHeight = height;
	} else if (eventName == 'maximize') {
		this.map.width = width - 2;
		this.map.height = height - 29;
	} else if (eventName == 'restore') {
		this.map.width = restoreWidth - 2;
		this.map.height = restoreHeight - 29;
	}
}