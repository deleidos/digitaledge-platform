package com.deleidos.rtws.alertviz.views
{
	import com.deleidos.rtws.alertviz.events.ApplicationCommandEvent;
	import com.deleidos.rtws.commons.util.batchoperation.BatchOperationStatusEvent;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class AlertVizMediator extends Mediator
	{
		[Inject] public var view:AlertViz;
		
		public function AlertVizMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			
			addViewListener(ApplicationCommandEvent.SHUTDOWN, dispatch);
			
			addContextListener(BatchOperationStatusEvent.BATCH_OPERATION, onStartupBatchStatusEvent);
		}
		
		private function onStartupBatchStatusEvent(event:BatchOperationStatusEvent):void
		{
			if(event != null && event.isComplete() && AlertViz.STARTUP_TRACKING_TOKEN === event.trackingToken)
			{
				view.launchStatus = !event.isUnsuccessful();
			}
		}
	}
}