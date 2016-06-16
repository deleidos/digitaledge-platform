package com.deleidos.rtws.alertviz.utils
{
	import com.adobe.serialization.json.JSON;
	
	import flash.net.URLRequestHeader;
	import flash.net.URLRequestMethod;
	
	import mx.rpc.AsyncToken;
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	import mx.utils.ArrayUtil;
	
	public class HttpUtilities
	{
		public static function sendRequest(url:String, requestParams:Object, responder:IResponder, timeout:int):HTTPService {
			
			var httpService:HTTPService = new HTTPService();

			httpService.url = url;
			httpService.requestTimeout = timeout;
			
			var body:Object = null;
			
			if (requestParams == null || requestParams.httpMethod == null || requestParams.httpMethod == URLRequestMethod.GET) {
				httpService.method = URLRequestMethod.GET;
			} else {
				httpService.method = URLRequestMethod.POST;
				
				if (requestParams.httpMethod != URLRequestMethod.POST) {
					httpService.headers = {"X-HTTP-Method-Override" : requestParams.httpMethod};
				}
				
				// Need to send a dummy object or else the http request
				// will become a GET request
				body = requestParams.body == null ? {"key":"value"} : requestParams.body;
			}
			
			var token:mx.rpc.AsyncToken = httpService.send(body);
			token.addResponder(responder);
			token.requestParams = requestParams;
			
			return httpService;
		}
		
		public static function getResponse(event:ResultEvent):Object {
			var response:Object = new Object();
			try{
				response.data = JSON.decode(event.message.body.toString());
				response.dataType = "json";
			}catch( e:Error ){
				try{
					response.dataType = "xml";
					response.data = new XML(event.message.body.toString());
				}catch( e:Error ){
					response.dataType = "string";
					response.data = event.message.body.toString();
				}
			}
			
			response.requestParams = event.token.requestParams;
			
			return response;
		}
		
		public static function getErrorResponse(event:FaultEvent):Object {
			var response:Object = new Object();
			response.requestParams = event.token.requestParams;
			return response;
		}
	}
}