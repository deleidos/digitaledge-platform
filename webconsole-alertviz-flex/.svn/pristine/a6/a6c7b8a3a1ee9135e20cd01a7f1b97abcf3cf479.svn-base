package com.deleidos.rtws.alertviz.services.repository.rest
{
	import com.deleidos.rtws.alertviz.events.HTTPQueueEvent;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	import com.deleidos.rtws.alertviz.services.rest.SimpleRestService;
	import com.deleidos.rtws.alertviz.utils.HttpUtilities;
	import com.deleidos.rtws.commons.tenantutils.model.properties.SystemAppPropertiesModel;
	
	import flash.events.Event;
	import flash.events.HTTPStatusEvent;
	import flash.events.IOErrorEvent;
	import flash.events.SecurityErrorEvent;
	import flash.events.TextEvent;
	import flash.events.TimerEvent;
	import flash.net.URLLoader;
	import flash.net.URLLoaderDataFormat;
	import flash.net.URLRequest;
	import flash.net.URLRequestMethod;
	import flash.net.URLStream;
	import flash.net.URLVariables;
	import flash.utils.ByteArray;
	import flash.utils.Timer;
	
	import mx.rpc.Fault;
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import org.robotlegs.mvcs.Actor;
	
	public class RepositoryRestService extends SimpleRestService implements IResponder
	{
		[Inject]
		public var appParams:ParameterModel;
		
		[Inject] public var appPropsModel:SystemAppPropertiesModel;
		
		protected var _urlLoader:URLLoader = null;
		protected var _timer:Timer = null;
		
		protected const CONTEXT_PATH:String = "proxy/rtws/repo/rest/content";
		
		private var _requestParams:Object;
		public function get requestParams():Object{ return _requestParams; }
	
		public function RepositoryRestService()
		{
			super();
		}
		
		private function makeAccessString():String {
			return "?userId=" + encodeURIComponent(appPropsModel.tenantId);
		}
		
		/**
		 * Lists the contents of the given path.
		 */
		public function sendListContentRequest(rp:Object, visibility:String, path:String):void {
			var url:String = 
				CONTEXT_PATH + "/list/" + 
				encodeURIComponent(appPropsModel.tenantId) + "/" + 
				encodeURIComponent(visibility) + "/" + 
				encodeURIComponent(path) + 
				makeAccessString();
			_requestParams = rp;
			super.sendRequest(url, _requestParams, appParams.serverTimeout);
		}

		/**
		 * Retrieve the contents of the given file.
		 */
		public function sendRetrieveContentRequest(rp:Object, visibility:String, path:String, filename:String):void {
			super.sendCustomRequest(executeRetrieveContentRequest, [rp, visibility, path, filename]);
		}
	
		protected function executeRetrieveContentRequest(rp:Object, visibility:String, path:String, filename:String):void{
			var url:String = 
				CONTEXT_PATH + "/retrieve/" + 
				encodeURIComponent(filename) + "/" + 
				encodeURIComponent(visibility) + "/" + 
				encodeURIComponent(path) + 
				makeAccessString();
			var urlRequest:URLRequest = new URLRequest(url);
			_requestParams = rp;
			
			_urlLoader = new URLLoader(urlRequest);
			_urlLoader.addEventListener(Event.COMPLETE, downloadComplete);
			_urlLoader.addEventListener(IOErrorEvent.IO_ERROR, errorHandler); 
			_urlLoader.addEventListener(SecurityErrorEvent.SECURITY_ERROR, errorHandler);
			_urlLoader.dataFormat = URLLoaderDataFormat.BINARY;
			
			_timer = new Timer(appParams.serverTimeout*1000);
			_timer.addEventListener(TimerEvent.TIMER, urlLoaderRequestTimeout);
			
			_urlLoader.load(urlRequest);
			_timer.start();
			
			_commsFailure = false;
		}

		// Override this as needed
		override protected function processError(response:Object):void {
		}
		
		// Override this as needed
		override protected function processResponse(response:Object):void {
		}
		
		/************ Listeners for URLLoader *****************/
		
		protected function downloadComplete(event:Event):void {
			_timer.reset();
			_timer.removeEventListener(TimerEvent.TIMER, urlLoaderRequestTimeout);
			processResponse(_urlLoader.data);
			_commsFailure = false;
			super.completeCustomRequest();
		}
		
		protected function urlLoaderRequestTimeout(event:Event):void {
			_urlLoader.removeEventListener(Event.COMPLETE, downloadComplete);
			_urlLoader.removeEventListener(IOErrorEvent.IO_ERROR, errorHandler); 
			_urlLoader.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, errorHandler);
			_urlLoader.close();
			_timer.reset();
			_requestParams.errorMsg = "Timeout while waiting for the server";
			processError(event);
			_commsFailure = true;
			super.completeCustomRequest();
		}
		
		protected function errorHandler(event:TextEvent):void {
			_timer.reset();
			_timer.removeEventListener(TimerEvent.TIMER, urlLoaderRequestTimeout);
			_requestParams.errorMsg = event.text;
			processError(event);
			_commsFailure = true;
			super.completeCustomRequest();
		}
		
	}
}
