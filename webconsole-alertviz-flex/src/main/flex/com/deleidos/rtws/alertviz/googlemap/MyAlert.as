
import com.deleidos.rtws.commons.util.StringUtils;

import flash.utils.Dictionary;

import mx.controls.Menu;

private function getAlertObject(record:Object, model:String, uuid:String, alertName:String, alertKey:String):Object
{
	//finds the object in the config file that corresponds to the data model of the alert
	var modelMappingCfg:Object = null;
	if(config != null) {
		for(var element:String in config)
		{
			if(element == model)
			{
				modelMappingCfg = config[element];
				break;
			}
		}
	}
	
	//creates the object to return
	var myAlert:Object = new Object();
	myAlert["uuid"] = uuid;
	myAlert["alertName"] = alertName;
	myAlert["alertKey"] = alertKey;
	
	if(modelMappingCfg != null) {
		
		var currExtractVal:Object = null;
		for (var currKey:String in modelMappingCfg) {
			try {
				currExtractVal = extractPath(record, modelMappingCfg[currKey]);
				myAlert[currKey] = currExtractVal;
			}
			catch(argumentError:ArgumentError) {
				// The mapping is invalid.
				// NiceToHave Flag this as invalid to skip future processing on this field
			}
			catch(error:Error) {
				// NiceToHave Throw other types of errors to differentiate not found from a null value returned from the lookup
			}
		}
		
		/* Currently, the extrinsic property contains the object/array of data to display in the map.
		 * Leaving this in for backwards compatability.  Eventually, this will be OBE'd
		 */
		if(myAlert.hasOwnProperty("extrinsic") == false) {
			myAlert["extrinsic"] = record;
		}
	}
	
	return myAlert;
}

/**
 * Extracts data from srcObj
 * Usage:
 * 
 * Constants are indicated by = (e.g "myConstantField": "=3")
 * Nested paths can be indicated by . separators (e.g. "My Nested Data": "parent.child")
 * Arrays can be indicated by using [] after a field name or by declaring a field of array type
 * e.g.
 * 	"My Array Field 1": "grandParnet.parent.children[]"
 *  "My Array Field 2": [<anyPath>, ... <anyPath>]
 * [Note:] Nested arrays will work, but result in arrays of arrays.
 * 		 
 * ToDo: Right now, no distinction between a null value and an unreachable path ... need to throw an exception instead.
 */
private function extractPath(srcObj:Object, path:Object):Object
{
	if(srcObj == null) {
		throw new ArgumentError("Source Object cannot be null");
	}
	if(path == null) {
		// Throwing null to support legacy concept of setting null.  This will be OBE when certain fields (and a mapping entry) are no longer required.
		return null;
	}
	else if(path is Array) {
		var pathArray:Array = (path as Array);
		
		var result:Array = new Array();
		
		for(var pathIndex:int=0; pathIndex < pathArray.length; pathIndex++) {
			result.push(extractPath(srcObj, pathArray[pathIndex]));
		}
		
		return result;
	}
	else if(path is String) {
		
		var pathStr:String = String(path);
		
		if(StringUtils.isBlank(pathStr)) {
			throw new ArgumentError("Path cannot be empty");
		}
		
		// Constants indicated by a leading = or > char.  (Legacy support for ">")
		if(StringUtils.beginsWith(pathStr, ">") || StringUtils.beginsWith(pathStr, "=")) {
			
			if(pathStr.length < 2) {
				throw new ArgumentError("No Constant value provided.");
			}
			
			return pathStr.substr(1); 
		}
		
		
		var currPathToken:String = null;
		var remainingPath:String = null;
		
		var nextPathSeparatorIndex:int = pathStr.indexOf(".");
		if(nextPathSeparatorIndex >= 0) {
			if(nextPathSeparatorIndex == 0 || nextPathSeparatorIndex == (pathStr.length - 1)) {
				throw new ArgumentError("Path cannot contain empty segments.");
			}
			
			currPathToken = pathStr.substring(0, nextPathSeparatorIndex);
			remainingPath = pathStr.substr(nextPathSeparatorIndex+1);
		}
		else
		{
			currPathToken = pathStr;
			
			if(StringUtils.isBlank(currPathToken)) {
				throw new ArgumentError("Path cannot contain empty segments.");
			}
		}
		
		var fieldName:String = currPathToken;
		var isFieldArray:Boolean = false;
		if((isFieldArray = StringUtils.endsWith(fieldName, "[]") == true)) {
			fieldName = fieldName.substring(0, fieldName.length - 2);
		}
		
		if(srcObj.hasOwnProperty(fieldName) && srcObj[fieldName] != null) {
			if(remainingPath == null) {
				return srcObj[fieldName];
			}
			else if(isFieldArray) {
				var fieldValueArray:Array = srcObj[fieldName] as Array;
				
				if(fieldValueArray == null) {
					return null;
				}
				else {
					var result:Array = new Array();
					
					for(var fieldArrayIndex:int=0; fieldArrayIndex < fieldValueArray.length; fieldArrayIndex++) {
						result.push(extractPath(fieldValueArray[fieldArrayIndex], remainingPath));
					}
					
					return result;
				}
			}
			else {
				return extractPath(srcObj[fieldName], remainingPath);
			}
		}
		else {
			return null;
		}
	}
	else {
		throw new ArgumentError("Unknown path type");
	}
}

//clears all the data structures that the map uses
public function clearMapData():void
{
	commentList.removeAll();
	objectsOnMap.removeAll();
	eventTypes.removeAll();
	markers.removeAll();
	paths.removeAll();
	ellipses.removeAll();
	lobs.removeAll();
	cirMarkers.removeAll();
	alertKeys.removeAll();
	dict = new Dictionary();
	types = new Dictionary();
	objects = new Dictionary();
	keys = new Dictionary();
	toggleMenu = new Menu();
	initTogglePop();
	oldMarker = null;
	follow = false;
	followMarker = false;
	alertKeyToFollow = "";
	aoiMarkers.removeAll();
	aoiArea = null;
	aois.removeAll();
	aoiPaths.removeAll();
	aoiPop.label = AOI_DRAW;
	this.map.clearOverlays();
	this.map.setCenter(US_CENTER_POINT, US_ZOOM_LEVEL);
}
