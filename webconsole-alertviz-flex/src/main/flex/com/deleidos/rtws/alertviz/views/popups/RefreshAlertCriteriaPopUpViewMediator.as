package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * The mediator for the refresh alert criteria list popup view.
	 */ 
	public class RefreshAlertCriteriaPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:RefreshAlertCriteriaPopUpView;
		
		/**
		 * This method gets called when the mediator is registered with the robotleg framework.
		 */
		public override function onRegister():void 
		{
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.REFRESH_SUCCESS, handleSuccess);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.REFRESH_ERROR, handleError);	
			
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.REFRESH, null));
		}
		
		/**
		 * This method gets called when the mediator gets removed from the robotleg framework.
		 */ 
		public override function onRemove():void 
		{
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.REFRESH_ERROR, handleError);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.REFRESH_SUCCESS, handleSuccess);
		}
		
		/**
		 * This event handler is called when the alert criteria list refresh command finished
		 * successfully.
		 */ 
		protected function handleSuccess(event:AlertCriteriaCommandEvent):void
		{			
			closeView();
		}
		
		/**
		 * This event handler is called when the alert criteria list refresh command finished
		 * unsuccessfully.
		 */
		protected function handleError(event:AlertCriteriaCommandEvent):void
		{
			closeView();
		}
		
		/**
		 * Closes this popup view.
		 */ 
		private function closeView():void 
		{
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
	}
}