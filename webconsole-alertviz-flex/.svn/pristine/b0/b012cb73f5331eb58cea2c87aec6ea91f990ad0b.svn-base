package com.deleidos.rtws.alertviz.services.rest
{
	import com.adobe.serialization.json.JSON;
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaEntry;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaModel;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	import com.deleidos.rtws.alertviz.models.WatchListEntry;
	import com.deleidos.rtws.alertviz.models.WatchListModel;
	import com.deleidos.rtws.alertviz.services.IWatchListService;
	
	import flash.net.URLRequestMethod;
	
	import mx.rpc.events.FaultEvent;
	
	public class WatchListService extends SimpleRestService implements IWatchListService
	{	
		[Inject]
		public var appParams:ParameterModel;
		
		[Inject]
		public var watchListModel:WatchListModel;
		
		[Inject]
		public var alertCritModel:AlertCriteriaModel;
		
		public function retrieveWatchList():void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/watchlist/retrieve/byuser/" + appParams.username;			
			super.sendRequest(url, {httpMethod:URLRequestMethod.GET, event:WatchListCommandEvent.REFRESH}, 10);
		}
		
		public function createWatchListEntry(key:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/watchlist/add/byuser/" + appParams.username + '/' + key + '/' + WatchListModel.DEFAULT_COLOR;
			super.sendRequest(url, {httpMethod:URLRequestMethod.POST, event:WatchListCommandEvent.CREATE, key:key}, 10);
		}
		
		public function changeWatchListEntryColor(key:String, color:uint):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/watchlist/update/byuser/" + appParams.username + '/' + key + '/' + color;
			super.sendRequest(url, {httpMethod:URLRequestMethod.PUT, event:WatchListCommandEvent.COLOR_CHANGE, key:key, context: {"color": color}}, 10);
		}
		
		public function deleteWatchListEntry(key:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/watchlist/delete/byuser/" + appParams.username + '/' + key;			
			super.sendRequest(url, {httpMethod:URLRequestMethod.DELETE, event:WatchListCommandEvent.REMOVE, key:key}, 10);
		}
		
		protected override function processResponse(response:Object):void
		{
			var params:Object = response.requestParams;
			
			if (params != null && validCommandEvent(params)) {
				var data:Object = response.data;
				if (params.event == WatchListCommandEvent.REFRESH) {
					for (var i:int = 0; i < data.length; i++) {
						var alertDef:Object = data[i];
						
						// Add the alert def if it doesn't already exists in the
						// watch list because we don't want to add duplicates
						
						if (watchListModel.findAlertDef(alertDef.key) == null) {
							watchListModel.addAlertDef(
								alertDef.key, 
								alertDef.name, 
								alertDef.model, 
								alertDef.description,
								JSON.encode(alertDef.definition),
								alertDef.color);
							
						}
					}
					
					dispatch(new WatchListCommandEvent(WatchListCommandEvent.REFRESH_SUCCESS, null));
				} else {
				
					var header:Object = data.standardHeader;
				
					if (header == null || parseInt(header.code) != 200) {
						dispatchErrorEvent(params);
					} else {
						if (params.event == WatchListCommandEvent.CREATE) {
						
							var entry:AlertCriteriaEntry = alertCritModel.findAlertDef(params.key);
						
							watchListModel.addAlertDef(
								entry.alertDefKey,
								entry.alertDefName,
								entry.alertDefModel,
								entry.alertDefDesc,
								entry.alertDef);
						
							dispatch(new WatchListCommandEvent(WatchListCommandEvent.CREATE_SUCCESS, null));
						}
					
						if (params.event == WatchListCommandEvent.REMOVE) {
							watchListModel.removeAlertDef(params.key);
						
							dispatch(new WatchListCommandEvent(WatchListCommandEvent.REMOVE_SUCCESS, null));
						}
						
						if (params.event == WatchListCommandEvent.COLOR_CHANGE) {
							var entryToUpdate:WatchListEntry = null;
							if(params.hasOwnProperty("key")) {
								entryToUpdate = watchListModel.findAlertDef(params["key"] as String);
							}
							
							if(entryToUpdate != null) {
								entryToUpdate.alertColor = params["context"]["color"];
								dispatch(new WatchListCommandEvent(WatchListCommandEvent.COLOR_CHANGE_SUCCESS, null));
							}
							else {
								dispatch(new WatchListCommandEvent(WatchListCommandEvent.COLOR_CHANGE_ERROR, null));
							}
						}
					} 
				}
			}
		}
		
		protected override function processError(response:Object):void 
		{
			dispatchErrorEvent(response.requestParams);
		}
		
		private function validCommandEvent(params:Object):Boolean
		{
			return params.event == WatchListCommandEvent.CREATE || params.event == WatchListCommandEvent.COLOR_CHANGE || 
				   params.event == WatchListCommandEvent.REMOVE || params.event == WatchListCommandEvent.REFRESH;
		}
		
		private function dispatchErrorEvent(params:Object):void
		{
			if (params.event == WatchListCommandEvent.CREATE) {
				dispatch(new WatchListCommandEvent(WatchListCommandEvent.CREATE_ERROR, null));
			} 
			else if (params.event == WatchListCommandEvent.COLOR_CHANGE) {
				dispatch(new WatchListCommandEvent(WatchListCommandEvent.COLOR_CHANGE_ERROR, null));
			} 
			else if (params.event == WatchListCommandEvent.REMOVE) {
				dispatch(new WatchListCommandEvent(WatchListCommandEvent.REMOVE_ERROR, null));
			} 
			else if (params.event == WatchListCommandEvent.REFRESH) {
				dispatch(new WatchListCommandEvent(WatchListCommandEvent.REFERSH_ERROR, null));
			}
		}
	}
}