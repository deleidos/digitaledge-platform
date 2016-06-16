package com.deleidos.rtws.alertviz.expandgraph.services
{
	import com.deleidos.rtws.alertviz.expandgraph.events.GraphConfigEvent;
	import com.deleidos.rtws.alertviz.expandgraph.models.GraphConfigModel;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	import com.deleidos.rtws.alertviz.services.rest.SimpleRestService;
	
	import flash.net.URLRequestMethod;
	
	import mx.rpc.events.FaultEvent;

	public class GraphConfigService extends SimpleRestService implements IGraphConfigService
	{
		[Inject]
		public var appParams:ParameterModel;
		
		[Inject]
		public var configModel:GraphConfigModel;
		
		public function getLocations():void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/graphconfig/get/locations";
			super.sendRequest(url, {httpMethod:URLRequestMethod.GET, event:GraphConfigEvent.GET_LOCATIONS}, 10);
		}
		
		public function getConfigFile(location:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/graphconfig/get/config/" +
				encodeURIComponent(location);
			super.sendRequest(url, {httpMethod:URLRequestMethod.GET, event:GraphConfigEvent.GET_CONFIG}, 10);
		}
		
		public function saveConfigFile(xml:XML, location:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/graphconfig/create/" +
				encodeURIComponent(location);
			super.sendRequest(url, {httpMethod:URLRequestMethod.POST, event:GraphConfigEvent.SAVE, body:{"key" : xml.toXMLString()}}, 10);
		}
		
		public function createFolder(location:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/graphconfig/create/" +
				encodeURIComponent(location);
			super.sendRequest(url, {httpMethod:URLRequestMethod.POST, event:GraphConfigEvent.CREATE_FOLDER}, 10);
		}
		
		public function updateConfigFile(xml:XML, location:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/graphconfig/update/" + 
				encodeURIComponent(location);
			super.sendRequest(url, {httpMethod:URLRequestMethod.PUT, event:GraphConfigEvent.UPDATE, body:{"key" : xml.toXMLString()}}, 10);
		}
		
		public function moveConfigFiles(oldLocation:String, newLocation:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/graphconfig/move/" + 
				encodeURIComponent(oldLocation) + '/' + encodeURIComponent(newLocation);
			super.sendRequest(url, {httpMethod:URLRequestMethod.PUT, event:GraphConfigEvent.MOVE}, 10);
		}
		
		public function renameConfigFile(oldLocation:String, newName:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/graphconfig/rename/" + 
				encodeURIComponent(oldLocation) + '/' + encodeURIComponent(newName);
			super.sendRequest(url, {httpMethod:URLRequestMethod.PUT, event:GraphConfigEvent.MOVE}, 10);
		}
		
		public function deleteConfigFile(location:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/graphconfig/delete/" +
				encodeURIComponent(location);
			super.sendRequest(url, {httpMethod:URLRequestMethod.DELETE, event:GraphConfigEvent.REMOVE}, 10);
		}
		
		protected override function processResponse(response:Object):void
		{
			// Extract out information about the request that was invoked
			
			var params:Object = response.requestParams;
			
			if (params == null) {
				dispatch(new GraphConfigEvent(GraphConfigEvent.SERVICE_ERROR, null));
				return;
			}
			
			var data:Array;
			if ((params.event == GraphConfigEvent.SAVE ||
				 params.event == GraphConfigEvent.CREATE_FOLDER ||
				 params.event == GraphConfigEvent.UPDATE ||
				 params.event == GraphConfigEvent.MOVE ||
				 params.event == GraphConfigEvent.RENAME ||
				 params.event == GraphConfigEvent.REMOVE)) {
				
				var header:Object = response.standardHeader;
				if (header == null || parseInt(header.code) != 200) {
					dispatch(new GraphConfigEvent(GraphConfigEvent.SERVICE_ERROR, null));
				} else {
					data = response.responseBody;
					if (params.event == GraphConfigEvent.SAVE || params.event == GraphConfigEvent.CREATE_FOLDER) {
						configModel.addItem(data[0].value);
					}else if (params.event == GraphConfigEvent.UPDATE) {
						//Do nothing
					}else if (params.event == GraphConfigEvent.MOVE) {
						configModel.moveItem(data[0].value, data[1].value);
					}else if (params.event == GraphConfigEvent.RENAME) {
						configModel.renameItem(data[0].value, data[1].value);
					}else if(params.event == GraphConfigEvent.REMOVE) {
						configModel.deleteItem(data[0].value);
					}
					dispatch(new GraphConfigEvent(GraphConfigEvent.SERVICE_COMPLETE, {"source":params.event}));
				}
			}
			else if (params.event == GraphConfigEvent.GET_CONFIG || params.event == GraphConfigEvent.GET_LOCATIONS) {
				var returnObj:Object = null;
				if(params.event == GraphConfigEvent.GET_LOCATIONS){
					data = response.data;
					configModel.clear();
					for (var i:int = 0; i < data.length; i++)
						configModel.addItem(data[i]);
				}else if(params.event == GraphConfigEvent.GET_CONFIG){
					returnObj = response.data;
				}
				
				dispatch(new GraphConfigEvent(GraphConfigEvent.SERVICE_COMPLETE, {"source":params.event, "output":returnObj}));
			}
		}
		
		protected override function processError(response:Object):void 
		{
			dispatch(new GraphConfigEvent(GraphConfigEvent.SERVICE_ERROR, null));
		}
	}
}