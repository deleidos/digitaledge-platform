package com.deleidos.rtws.alertviz.views
{	
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	import com.deleidos.rtws.alertviz.models.WatchListEntry;
	import com.deleidos.rtws.alertviz.models.WatchListModel;
	import com.deleidos.rtws.alertviz.views.popups.DeleteWatchListEntryPopUpView;
	
	import flash.events.MouseEvent;
	
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.PropertyChangeEvent;
	import mx.events.PropertyChangeEventKind;
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;

	/**
	 * The mediator for the watch list view.
	 */ 
	public class WatchListViewMediator extends Mediator
	{
		[Inject] public var view:WatchListView;
		
		[Inject] public var model:WatchListModel;
		
		/**
		 * Called by the robotlegs framework when this mediator is registered.
		 * It contains a list of events this mediator is listening on and binds
		 * the watch list model to the data provider in the view.
		 */
		public override function onRegister():void
		{
			view.dataProvider = model.alertDefList;
			
			addViewListener(WatchListCommandEvent.TRACK_OFF_REQUEST, dispatch);
			addViewListener(WatchListCommandEvent.TRACK_ON_REQUEST, dispatch);
			addViewListener(PropertyChangeEvent.PROPERTY_CHANGE, onPropertyChangeEvent);
			addViewListener(WatchListCommandEvent.REMOVE, onRemoveRequest);
			
			// Handles subscribing to the channel.  This is not a wise place to put this, but hey, I don't have time to fix everything.
			model.alertDefList.addEventListener(CollectionEvent.COLLECTION_CHANGE, handleModelChange);
		}
		
		/**
		 * This is a event handler method that gets called when the user changes
		 * the color of a watch list entry using the color picker component.
		 */
		protected function onPropertyChangeEvent(event:PropertyChangeEvent):void
		{
			if(event != null && event.kind == PropertyChangeEventKind.UPDATE && event.source != null && event.source is WatchListEntry) {
				var watchListEntry:WatchListEntry = (event.source as WatchListEntry);
				
				if(event.property == "alertColor") {
					dispatch(new WatchListCommandEvent(WatchListCommandEvent.COLOR_CHANGE, {key: watchListEntry.alertDefKey, color: event.newValue}));
				}
			}
		}
		
		/**
		 * This is an event handler method that gets called when the user
		 * removes an entry from the watch list.
		 */ 
		protected function onRemoveRequest(event:WatchListCommandEvent):void
		{
			if(event.parameters != null && event.parameters.hasOwnProperty("key") && model.alertDefList != null) {
				var removeList:Array = new Array();
				var itemToRemove:Object = null;
				
				for (var i:int = 0; i < model.alertDefList.length; i++) {
					var currWatchListEntry:WatchListEntry = (model.alertDefList[i] as WatchListEntry);
					
					if(currWatchListEntry != null && currWatchListEntry.alertDefKey === event.parameters["key"])
					{
						itemToRemove = new Object();
						itemToRemove['key'] = currWatchListEntry.alertDefKey;
						itemToRemove['name'] = currWatchListEntry.alertDefName;
						
						removeList.push(itemToRemove);
					}
				}
				
				if(removeList.length > 0){
					var popup:DeleteWatchListEntryPopUpView = new DeleteWatchListEntryPopUpView();
					
					popup.removeList = removeList;
					
					PopUpManager.addPopUp(popup, contextView, true);
					PopUpManager.centerPopUp(popup);
					
					mediatorMap.createMediator(popup);
				}
			}
		}
		
		/**
		 * This is an event handler method that gets called when the watch list
		 * collection changes.
		 */
		protected function handleModelChange(event:CollectionEvent):void
		{
			switch (event.kind)
			{
				
				// A new entry was added into watch list collection. 
				// Create a consumer that connects to the blazeDS.
				// Dispatch a color change event to let everyone know
				// to uses the color for all alerts that matches this
				// watch list entry.
				
				case CollectionEventKind.ADD :
					var itemAdded:WatchListEntry = WatchListEntry(event.items[0]);
					model.addConsumer(itemAdded.alertDefKey);
					dispatch(new WatchListCommandEvent(WatchListCommandEvent.COLOR_CHANGE, {key:itemAdded.alertDefKey, color:itemAdded.alertColor}));
					break;
				
				// An entry was removed from the watch list collection. 
				// Disconnect the consumer from blazeDS.
				
				case CollectionEventKind.REMOVE :
					var itemRemoved:WatchListEntry = WatchListEntry(event.items[0]);
					model.removeConsumer(itemRemoved.alertDefKey);
					break;
			}
		}
	}
}