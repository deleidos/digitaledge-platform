package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	
	import flash.events.MouseEvent;
	
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.validators.Validator;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * The mediator for the delete watch list entry popup view.
	 */ 
	public class DeleteWatchListEntryPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:DeleteWatchListEntryPopUpView;
		
		/**
		 * This method gets called when the mediator is registered with the robotleg framework.
		 */
		public override function onRegister():void 
		{
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.REMOVE_SUCCESS, handleRemoveSuccess);
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.REMOVE_ERROR, handleRemoveError);	
			
			fireNextEvent();
		}
		
		/**
		 * This method gets called when the mediator gets removed from the robotleg framework.
		 */ 
		public override function onRemove():void 
		{
			eventMap.unmapListener(eventDispatcher, WatchListCommandEvent.REMOVE_ERROR, handleRemoveError);
			eventMap.unmapListener(eventDispatcher, WatchListCommandEvent.REMOVE_SUCCESS, handleRemoveSuccess);
		}
		
		/**
		 * This event handler is called when the watch list remove command finished
		 * successfully.
		 */ 
		protected function handleRemoveSuccess(event:WatchListCommandEvent):void
		{
			fireNextEvent();
		}
		
		/**
		 * This event handler is called when the watch list remove command finished
		 * unsuccessfully.
		 */
		protected function handleRemoveError(event:WatchListCommandEvent):void
		{
			fireNextEvent();
		}
		
		/**
		 * Check to see if there are more work to be done. If so
		 * fire off the next remove event else close the popup view.
		 */ 
		private function fireNextEvent():void 
		{
			if (view.removeList.length > 0) 
			{
				var item:Object = view.removeList.pop();
				
				var key:String = item['key'];
				var name:String = item['name'];
				
				dispatch(new WatchListCommandEvent(WatchListCommandEvent.REMOVE, {key:key}));
			} else {
				closeView();	
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
	}
}