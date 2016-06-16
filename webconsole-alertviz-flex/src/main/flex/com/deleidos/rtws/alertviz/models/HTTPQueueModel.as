package com.deleidos.rtws.alertviz.models
{
	import org.robotlegs.mvcs.Actor;

	/**
	 * This model stores a list of HTTP requests to be dispatched.
	 */ 
	public class HTTPQueueModel extends Actor
	{
		/** A queue of HTTP request waiting to be fired */
		private var _queue:Vector.<HTTPRequest> = new Vector.<HTTPRequest>();
		
		private var _inactive:Boolean = true;
		
		/**  
		 * Removes the oldest (aka first) HTTP request from the queue. 
		 */
		public function dequeue():HTTPRequest
		{
			return _queue.length == 0 ? null : _queue.shift();
		}
		
		/**
		 * Adds a HTTP request to the back of the queue. 
		 */
		public function enqueue(request:HTTPRequest):void
		{
			_queue.push(request);
		}
		
		public function get inactive():Boolean{
			return _inactive;
		}
		
		public function set inactive(value:Boolean):void{
			_inactive = value;
		}

	}
}