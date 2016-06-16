package com.deleidos.rtws.alertviz.commands
{
	import com.deleidos.rtws.alertviz.events.HTTPQueueEvent;
	import com.deleidos.rtws.alertviz.models.HTTPQueueModel;
	import com.deleidos.rtws.alertviz.models.HTTPRequest;
	
	import org.robotlegs.mvcs.Command;

	/**
	 * This class contains the logic to handle HTTP queue events.
	 */ 
	public class HTTPQueueCommand extends Command
	{
		[Inject]
		public var event:HTTPQueueEvent;
		
		[Inject]
		public var queue:HTTPQueueModel;
		
		/**
		 * This method is executed when a HTTP queue event is dispatched.
		 */ 
		public override function execute():void 
		{	
			if (event.type == HTTPQueueEvent.QUEUE) 
			{
				// Adds a HTTP request to the queue
				
				queue.enqueue(new HTTPRequest(event.request, event.parameters));
			}
			
			if(queue.inactive || event.type == HTTPQueueEvent.COMPLETE)
			{
				// Removes a HTTP request from the queue and
				// fire off the service
				
				var next:HTTPRequest = queue.dequeue();
				
				if (next != null)
				{
					next.execute();
					queue.inactive = false;
				}
				else 
					queue.inactive = true;
			}
		}
	}
}