package com.deleidos.rtws.alertviz.views
{
	import com.adobe.utils.StringUtil;
	import com.deleidos.rtws.commons.event.OperationStatusEvent;
	import com.deleidos.rtws.commons.util.batchoperation.BatchOperationStatusEvent;
	
	import flash.events.Event;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class AppLaunchStatusDisplayMediator extends Mediator
	{
		[Inject]
		public var view:AppLaunchStatusDisplay;
		
		public function AppLaunchStatusDisplayMediator()
		{
			super();
		}
		
		override public function onRegister():void
		{
			super.onRegister();
			
			addContextListener(BatchOperationStatusEvent.BATCH_OPERATION, onStartupBatchStatusEvent);
		}
		
		private function onStartupBatchStatusEvent(event:BatchOperationStatusEvent):void
		{
			if(event == null || AlertViz.STARTUP_TRACKING_TOKEN !== event.trackingToken)
			{
				return;
			}
			
			if(event.isComplete())
			{
				if(event.isUnsuccessful())
				{
					view.currentState = "launchFailure";
					view.statusText.text = "Load Failed. Please notify your system administrator";
				}
				else
				{
					view.currentState = "launchComplete";
					view.statusText.text = "App Load Complete";
				}
				
				this.view.progressSpinner.stop();
				this.eventMap.unmapListener(eventDispatcher, BatchOperationStatusEvent.BATCH_OPERATION, onStartupBatchStatusEvent);
			}
			else
			{
				view.currentState = "launchInProgress";
				view.statusText.text = "Please wait. Loading ...";
			}
		}
	}
}