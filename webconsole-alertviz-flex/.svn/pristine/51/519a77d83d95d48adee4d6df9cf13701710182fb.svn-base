package com.deleidos.rtws.alertviz.services.rest
{
	import com.deleidos.rtws.alertviz.events.HTTPQueueEvent;
	import com.deleidos.rtws.alertviz.utils.HttpUtilities;
	
	import mx.rpc.IResponder;
	import mx.rpc.events.FaultEvent;
	import mx.rpc.events.ResultEvent;
	import mx.rpc.http.HTTPService;
	
	import org.robotlegs.mvcs.Actor;
	
	/**
	 * A generic class used to submit HTTP request.
	 */ 
	public class SimpleRestService extends Actor implements IResponder
	{
		/** The Flex HTTP service object */
		protected var _httpService:HTTPService = null;
		
		/** The time value used by polling logic */
		protected var _time:Date = null;
		
		/** The tick value used by polling logic */
		protected var _tick:int = -1;
		
		/** Communication failure flag */
		protected var _commsFailure:Boolean = false;
		
		/**
		 * This method prepares to send a HTTP request to the targeted URL.
		 */
		public function sendRequest(url:String, requestParams:Object, timeoutSecs:int, time:Date = null, tick:int = -1):void
		{
			dispatch(new HTTPQueueEvent(HTTPQueueEvent.QUEUE, executeRequest, [url, requestParams, timeoutSecs, time, tick]));
		}
		
		/**
		 * This method submits a HTTP request to the targeted URL.
		 */
		private function executeRequest(url:String, requestParams:Object, timeoutSecs:int, time:Date = null, tick:int = -1):void{
			_time = time;
			_tick = tick;
			_httpService = HttpUtilities.sendRequest(url, requestParams, this, timeoutSecs);
		}

		/**
		 * Retrieves the flag indicating if the communication failed.
		 */ 
		public function commsFailure():Boolean 
		{
			return _commsFailure;
		}
		
		/**
		 * This handler is invoked when the HTTP request fails.
		 */ 
		public function fault(info:Object):void 
		{
			_commsFailure = true;
			_httpService = null;
			
			var event:FaultEvent = info as FaultEvent;
			var response:Object = HttpUtilities.getErrorResponse(event);
			processError(response);
			dispatch(new HTTPQueueEvent(HTTPQueueEvent.COMPLETE));
		}
		
		/**
		 * This handler is invoked when the HTTP request succeeds.
		 */ 
		public function result(info:Object):void {
			_commsFailure = false;
			_httpService = null;
			
			var event : ResultEvent = info as ResultEvent;
			var response:Object = HttpUtilities.getResponse(event);
			processResponse(response);
			dispatch(new HTTPQueueEvent(HTTPQueueEvent.COMPLETE));
		}
		
		/**
		 * Allows a class to execute custom HTTP requests while taking
		 * advantage of SimpleRestService's built-in queueing system.
		 * NOTE: A class that uses this function MUST call completeCustomRequest()
		 * when finished with its task.
		 */
		public function sendCustomRequest(func:Function, params:Array):void{
			dispatch(new HTTPQueueEvent(HTTPQueueEvent.QUEUE, func, params));
		}
		
		public function completeCustomRequest():void{
			dispatch(new HTTPQueueEvent(HTTPQueueEvent.COMPLETE));
		}
		
		/**
		 * Placeholder method for subclass to inherit to handle processing
		 * HTTP request failures.
		 */ 
		protected function processError(response:Object):void {
		}
		
		/**
		 * Placeholder method for subclass to inherit to handle processing
		 * HTTP request results.
		 */ 
		protected function processResponse(response:Object):void {
		}
	}
}