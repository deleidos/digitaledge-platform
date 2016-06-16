package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	
	import flash.events.MouseEvent;
	
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * The mediator for the create watch list entry popup view.
	 */  
	public class CreateWatchListEntryPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:CreateWatchListEntryPopUpView;
		
		/**
		 * This method gets called when the mediator is registered with the robotleg framework.
		 */ 
		public override function onRegister():void 
		{
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.CREATE_SUCCESS, handleCreateSuccess);
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.CREATE_ERROR, handleCreateError);	

			fireNextEvent();
		}
		
		/**
		 * This method gets called when the mediator gets removed from the robotleg framework.
		 */ 
		public override function onRemove():void 
		{
			eventMap.unmapListener(eventDispatcher, WatchListCommandEvent.CREATE_ERROR, handleCreateError);
			eventMap.unmapListener(eventDispatcher, WatchListCommandEvent.CREATE_SUCCESS, handleCreateSuccess);
		}
		
		/**
		 * This event handler is called when the watch list create command finished
		 * successfully.
		 */ 
		protected function handleCreateSuccess(event:WatchListCommandEvent):void
		{
			fireNextEvent();
		}
		
		/**
		 * This event handler is called when the watch list create command finished
		 * unsuccessfully.
		 */
		protected function handleCreateError(event:WatchListCommandEvent):void
		{
			fireNextEvent();
		}
		
		/**
		 * Check to see if there are more work to be done. If so
		 * fire off the next create event else close the popup view.
		 */ 
		private function fireNextEvent():void 
		{
			if (view.watchList.length > 0) 
			{
				var item:Object = view.watchList.pop();
				
				var key:String = item['key'];
				var name:String = item['name'];
				
				dispatch(new WatchListCommandEvent(WatchListCommandEvent.CREATE, {key:key}));
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