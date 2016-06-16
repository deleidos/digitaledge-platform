import com.adobe.serialization.json.JSON;
import com.deleidos.rtws.alertviz.models.Alert;

import flash.events.MouseEvent;
import flash.net.URLRequest;
import flash.net.URLRequestMethod;
import flash.net.navigateToURL;

import mx.core.FlexGlobals;

/**
 * This event handler fires when the user clicks on the launch google earth button.
 */ 
protected function earthBtn_clickHandler(event:MouseEvent):void 
{	
	// Creates http request data
	var obj:Object = {"data" : "alertviz"};
	
	// Sends get request with the request data to the google earth servlet
	// This initializes the servlet for google earth startup
	
	/** 
	 * Note: This will eventually need to be pulled from AlertVizPropertiesModel.
	 * 
	 * Alternatively, this feature may be OBE'd completely depending on what it does.
	 * Just commenting out for now as this feature is not being deployed in our demo app.
	 * The push to Google Earth feature needs some rearchitecting in general.
	 */
	httpService.url = "GoogleEarthJSON/kml/setup";
	httpService.method = URLRequestMethod.GET;
	//httpService.send(obj);
	
	// Opens Link.kml in google earth
	/** 
	 * Note: This will eventually need to be user based and dynamically generated
	 * if this app and feature are to be delivered.
	 */
	navigateToURL(new URLRequest("Link.kml"));
}

/**
 * Adds an incoming alert to the map and to google earth
 */ 
public function forwardAlert(alert:Alert):void 
{
	// Adds alert to the map
	
	var myAlert:Object = getAlertObject(alert.record, alert.model, alert.id, alert.criteria.name, alert.criteria.key);
	addAlert(myAlert);
	
	// Creates request data object that contains the alert to be drawn
	
	var JSONString:Object = {"data" : JSON.encode(myAlert)};
	/** Note: See above.  This should be pulled from AlertVizPropertiesModel or may be OBE'd */
	httpService.url = "GoogleEarthJSON/kml/setup";
	httpService.method = URLRequestMethod.POST;
	
	// Sends POST request
	
	httpService.send(JSONString);
}