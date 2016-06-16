package com.deleidos.rtws.alertviz.models
{
	/**
	 * This class represent a HTTP request.
	 */ 
	public class HTTPRequest
	{
		/** The service function to invoke */
		private var _request:Function;
		
		/** Parameters for the HTTP request */
		private var _parameters:Array;
		
		/**
		 * Constructor.
		 */
		public function HTTPRequest(request:Function, parameters:Array)
		{
			_request = request;
			_parameters = parameters;
		}
		
		/**
		 * Invokes the service function passing in the request 
		 * parameters.
		 */ 
		public function execute():void
		{
			_request.apply(null, _parameters);
		}
	}
}