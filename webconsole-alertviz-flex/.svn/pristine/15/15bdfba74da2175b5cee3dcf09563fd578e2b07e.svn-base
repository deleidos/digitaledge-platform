package com.deleidos.rtws.alertviz.models
{
	import com.adobe.serialization.json.JSON;
	import com.deleidos.rtws.alertviz.events.AlertCommandEvent;
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	
	import mx.collections.ArrayCollection;
	import mx.messaging.ChannelSet;
	import mx.messaging.Consumer;
	import mx.messaging.config.ServerConfig;
	import mx.messaging.events.MessageEvent;
	
	import org.robotlegs.mvcs.Actor;
	
	/**
	 * This is the data model for the watch list view.
	 */ 
	public class WatchListModel extends Actor
	{
		/** The name of the alert queue in blazeDS */
		public static const ALERT_DESTINATION_NAME	:String = "alert-data-feed";

		/** The header key uses to filter out messages on the alert queue */
		public static const SELECTOR_PROPERTY_NAME	:String = "AlertCritKey";
		
		/** The default color of the alert definition on the watch list */
		public static const DEFAULT_COLOR			:uint = 0xFF0000;
		
		/** Stores a collection of alert defintions that is on watch */
		private var _alertDefList:ArrayCollection;
		
		/** An associative array that stores a list of consumers */
		private var _consumers:Object;
		
		/** A set of channels to uses to connect to blazeDS, configured in services-config.xml file */
		private var _channelSet:ChannelSet;
		
		/**
		 * Constructor.
		 */ 
		public function WatchListModel()
		{
			_consumers = new Object();
			_alertDefList = new ArrayCollection();
			_channelSet = ServerConfig.getChannelSet(ALERT_DESTINATION_NAME);
		}
		
		/**
		 * Retrieve the alert definition collection.
		 */ 
		public function get alertDefList():ArrayCollection
		{
			return _alertDefList;	
		}
		
		/**
		 * Given the alert definition key and retrieve the alert
		 * definition color. If the alert defintion is not present
		 * return the default color.
		 */ 
		public function getWatchListEntryColor(key:String):uint 
		{
			var entry:WatchListEntry = null;
			
			for (var i:int = 0; i < _alertDefList.length; i++) 
			{
				entry = WatchListEntry(_alertDefList.getItemAt(i));
				if (entry.alertDefKey == key) 
				{
					return entry.alertColor;
				}
			}
			
			return DEFAULT_COLOR;
		}
		
		/**
		 * Clear the alert definition collections and disconnect
		 * all consumer from blazeDS.
		 */
		public function clear():void
		{
			for (var i:int = 0; i < _alertDefList.length; i++) 
			{
				var entry:WatchListEntry = WatchListEntry(_alertDefList.getItemAt(i));
				this.removeConsumer(entry.alertDefKey);
			}
			
			_alertDefList.removeAll();
		}
		
		/**
		 * Retrieve the alert definition (watch list entry) given
		 * the alert definition key.
		 */ 
		public function findAlertDef(key:String):WatchListEntry
		{
			var entry:WatchListEntry = null;
			
			for (var i:int = 0; i < _alertDefList.length; i++) 
			{
				entry = WatchListEntry(_alertDefList.getItemAt(i));
				if (entry.alertDefKey == key) 
				{
					return entry;
				}
			}
			
			return null;
		}
		
		/**
		 * Set the tracking flag to true for the given alert
		 * criteria key.
		 */ 
		public function turnOnTrackingAlertDef(key:String):void
		{
			setAlertDefTracking(key, true);
		}
		
		/**
		 * Set the tracking flag to false for the given alert
		 * criteria key.
		 */ 
		public function turnOffTrackingAlertDef(key:String):void
		{
			setAlertDefTracking(key, false);
		}
		
		public function setAlertDefTracking(key:String, newTrackingValue:Boolean):void {
			var entry:WatchListEntry = null;
			
			for (var i:int = 0; i < _alertDefList.length; i++) 
			{
				// Being defensive with the cast since legacy code exposed the collection to the outside world
				entry = (_alertDefList.getItemAt(i) as WatchListEntry);
				if(entry != null && entry.alertDefKey == key && entry.alertTracking != newTrackingValue) 
				{
					entry.alertTracking = newTrackingValue;
					dispatch(new WatchListCommandEvent((newTrackingValue ? WatchListCommandEvent.TRACK_ON : WatchListCommandEvent.TRACK_OFF), {filterKey: entry.alertDefKey}));
				}
			}
		}
		
		/**
		 * Set the tracking flag to false for all alert criteria.
		 */ 
		public function turnOffTrackingForAll():void
		{
			var entry:WatchListEntry = null;
			
			for(var i:int = 0; i < _alertDefList.length; i++) 
			{
				// Being defensive with the cast since legacy code exposed the collection to the outside world
				entry = (_alertDefList.getItemAt(i) as WatchListEntry);
				if(entry != null && entry.alertTracking == true) 
				{
					entry.alertTracking = false;
					dispatch(new WatchListCommandEvent(WatchListCommandEvent.TRACK_OFF, {filterKey: entry.alertDefKey}));
				}
			}
		}
		
		/**
		 * Adds a alert definition to the collection.
		 */ 
		public function addAlertDef(key:String, name:String, model:String, desc:String, def:String, color:uint = DEFAULT_COLOR):void
		{			
			var entry:WatchListEntry = 
				new WatchListEntry(key, name, model, desc, def, color);
			
			_alertDefList.addItem(entry);
		}
		
		/**
		 * Removes a alert definition from the collection.
		 * We don't need to manually remove the consumer
		 * here because a listener is watching the alert
		 * definition collection and will remove the consumer
		 * when a delete collection event is fired.
		 */ 
		public function removeAlertDef(key:String):Boolean
		{
			var success:Boolean = false;
			
			var index:int = -1;
			for (var i:int = 0; i < _alertDefList.length; i++) 
			{
				var entry:WatchListEntry = WatchListEntry(_alertDefList.getItemAt(i));
				
				if (entry.alertDefKey == key) 
				{
					index = i;
					break;
				}
			}
			
			if (index != -1) 
			{
				_alertDefList.removeItemAt(index);
				success = true;
			}
			
			return success;
		}
		
		/**
		 * Given the alert definition key, create a new consumer
		 * to listen on alerts that matches this definition. The
		 * consumer created will establish a connection to blazeDS
		 * using the different channels configured in the
		 * services-config.xml file.
		 */ 
		public function addConsumer(key:String):void
		{
			if (! _consumers[key]) {
				var consumer:Consumer = createConsumer(key);
				consumer.subscribe();
				_consumers[key] = consumer;
			}
		}
		
		/**
		 * Given the alert definition key remove a consumer. Doing
		 * so will disconnect the consumer from blazeDS and stop
		 * the application from listening alerts on the queue.
		 */ 
		public function removeConsumer(key:String):void
		{
			if (_consumers[key] != null)
			{
				var consumer:Consumer = _consumers[key];
				consumer.unsubscribe();
				consumer.channelSet.disconnectAll();
				_consumers[key] = null;
			}
		}
		
		/**
		 * Disconnect all active subscribed consumers from blazeDS.
		 */ 
		public function disconnectAll():void
		{
			for (var i:int = 0; i < _alertDefList.length; i++) 
			{
				var entry:WatchListEntry = WatchListEntry(_alertDefList.getItemAt(i));
				removeConsumer(entry.alertDefKey);
			}
		}
		
		/**
		 * This is an event handler that is fired when a new message,
		 * in our case an alert is sent to a consumer. This handler
		 * simply dispatches an event notifying everyone interested
		 * of the new message.
		 */  
		protected function messageHandler(event:MessageEvent):void
		{
			var message:Alert = new Alert(JSON.decode(event.message.body as String));
			dispatch(new AlertCommandEvent(AlertCommandEvent.ADD, message));
		}
		
		/**
		 * Create a new consumer and set the header selector
		 * to look for alerts that matches the alert definition
		 * key.
		 */ 
		private function createConsumer(key:String):Consumer
		{
			var consumer:Consumer = new Consumer();
			consumer.destination = ALERT_DESTINATION_NAME;
			consumer.selector = SELECTOR_PROPERTY_NAME + " = '" + key + "'";
			consumer.channelSet = new ChannelSet(_channelSet.channelIds);
			consumer.addEventListener(MessageEvent.MESSAGE, messageHandler);
			
			return consumer;
		}
	}
}