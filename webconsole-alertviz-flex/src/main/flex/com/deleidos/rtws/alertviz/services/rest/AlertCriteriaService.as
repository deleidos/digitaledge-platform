package com.deleidos.rtws.alertviz.services.rest
{
	import com.adobe.serialization.json.JSON;
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaModel;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	import com.deleidos.rtws.alertviz.models.WatchListModel;
	import com.deleidos.rtws.alertviz.services.IAlertCriteriaService;
	
	import flash.net.URLRequestMethod;
	
	import mx.rpc.events.FaultEvent;
	
	public class AlertCriteriaService extends SimpleRestService implements IAlertCriteriaService
	{	
		[Inject]
		public var appParams:ParameterModel;
		
		[Inject]
		public var alertDefModel:AlertCriteriaModel;
		
		[Inject]
		public var watchListModel:WatchListModel;
		
		public function retrieveAlertCriterias():void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/filters/all";			
			super.sendRequest(url, {httpMethod:URLRequestMethod.GET, event:AlertCriteriaCommandEvent.REFRESH}, 10);
		}
		
		public function createAlertCriteria(name:String, model:String, desc:String, def:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/filter/" + 
				encodeURIComponent(name) + '/' + encodeURIComponent(model);
			super.sendRequest(url, {httpMethod:URLRequestMethod.POST, event:AlertCriteriaCommandEvent.CREATE, body:{"jsonDefinition" : def}}, 10);
		}
			
		public function updateAlertCriteria(key:String, name:String, model:String, desc:String, def:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/filter/" + 
				encodeURIComponent(key) + '/' + encodeURIComponent(name) + '/' + encodeURIComponent(model);
			super.sendRequest(url, {httpMethod:URLRequestMethod.PUT, event:AlertCriteriaCommandEvent.UPDATE, key:key, body:{"jsonDefinition" : def}}, 10);
		}
			
		public function deleteAlertCriteria(key:String):void
		{
			var url:String = appParams.selfPrefixUrl + "proxy/rtws/alertsapi/json/filter/" + encodeURIComponent(key);
			super.sendRequest(url, {httpMethod:URLRequestMethod.DELETE, event:AlertCriteriaCommandEvent.REMOVE, key:key}, 10);
		}
		
		protected override function processResponse(response:Object):void
		{
			var params:Object = response.requestParams;
			
			if (params != null && validCommandEvent(params)) {
				var data:Object = response.data;
				if (params.event == AlertCriteriaCommandEvent.REFRESH) {
					alertDefModel.clear();
					
					for (var i:int = 0; i < data.length; i++)
					{
						var alertDef:Object = data[i];
						
						alertDefModel.addAlertDef(
							alertDef.key, 
							alertDef.name, 
							alertDef.model, 
							alertDef.description,
							JSON.encode(alertDef.definition));
					}
					
					dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.REFRESH_SUCCESS, null));
				} else {
					var header:Object = data.standardHeader;
			
					if (header == null || parseInt(header.code) != 200) {
						dispatchErrorEvent(params);
					} else {
						if (params.event == AlertCriteriaCommandEvent.CREATE) {
							alertDefModel.addAlertDef(
								data.responseBody.key,
								data.responseBody.name,
								data.responseBody.model,
								data.responseBody.description,
								JSON.encode(data.responseBody.definition));	
						
							dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.CREATE_SUCCESS, null));
						}
					
						if (params.event == AlertCriteriaCommandEvent.UPDATE) {
							alertDefModel.updateAlertDef(
								data.responseBody.key,
								data.responseBody.name,
								data.responseBody.model,
								data.responseBody.description,
								JSON.encode(data.responseBody.definition));
						
							dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.UPDATE_SUCCESS, null));
						}
					
						if (params.event == AlertCriteriaCommandEvent.REMOVE) {
							alertDefModel.removeAlertDef(params.key);
						
							dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.REMOVE_SUCCESS, null));
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
			return params.event == AlertCriteriaCommandEvent.CREATE || params.event == AlertCriteriaCommandEvent.UPDATE || 
				params.event == AlertCriteriaCommandEvent.REMOVE || params.event == AlertCriteriaCommandEvent.REFRESH;
		}
		
		private function dispatchErrorEvent(params:Object):void
		{
			if (params.event == AlertCriteriaCommandEvent.CREATE) {
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.CREATE_ERROR, null));
			} 
			else if (params.event == AlertCriteriaCommandEvent.UPDATE) {
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.UPDATE_ERROR, null));
			} 
			else if (params.event == AlertCriteriaCommandEvent.REMOVE) {
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.REMOVE_ERROR, null));
			} 
			else if (params.event == AlertCriteriaCommandEvent.REFRESH) {
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.REFRESH_ERROR, null));
			}
		}
	}
}