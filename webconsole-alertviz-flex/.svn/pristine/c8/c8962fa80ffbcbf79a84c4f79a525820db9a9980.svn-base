package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaEntry;
	
	import flash.events.MouseEvent;
	
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.validators.Validator;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * The mediator for the delete alert criteria list popup view.
	 */ 
	public class DeleteAlertCriteriaPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:DeleteAlertCriteriaPopUpView;
		
		/**
		 * This method gets called when the mediator is registered with the robotleg framework.
		 */
		public override function onRegister():void 
		{
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.REMOVE_SUCCESS, handleSuccess);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.REMOVE_ERROR, handleError);	

			fireRemoveEvent();
		}
		
		/**
		 * This method gets called when the mediator gets removed from the robotleg framework.
		 */ 
		public override function onRemove():void 
		{
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.REMOVE_ERROR, handleError);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.REMOVE_SUCCESS, handleSuccess);
		}
		
		/**
		 * This event handler is called when the alert criteria list remove command finished
		 * successfully.
		 */ 
		protected function handleSuccess(event:AlertCriteriaCommandEvent):void
		{			
			fireNextEvent();
		}
		
		/**
		 * This event handler is called when the alert criteria list remove command finished
		 * unsuccessfully.
		 */
		protected function handleError(event:AlertCriteriaCommandEvent):void
		{
			fireNextEvent(); 
		}
		
		/**
		 * Check to see if there are more work to be done. If so
		 * fire off the next remove event else close the popup view.
		 */ 
		private function fireNextEvent():void 
		{
			if (! fireRemoveEvent()) 
			{
				if (view.removeList.length == 0) 
				{
					closeView();
				}
			}
		}
		
		/**
		 * Closes this popup view.
		 */ 
		private function closeView():void 
		{
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
		
		/**
		 * Checks the remove alert criteria queue and fires off a 
		 * remove event if an item exist.
		 */
		private function fireRemoveEvent():Boolean 
		{
			if (view.removeList.length > 0) 
			{
				var item:Object = view.removeList.pop();
			
				var key:String = item['key'];
				var name:String = item['name'];
			
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.REMOVE, {key:key}));
				
				return true;
			}
			
			return false;
		}
	}
}