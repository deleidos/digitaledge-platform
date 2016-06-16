package com.deleidos.rtws.alertviz.models
{
	import mx.collections.ArrayCollection;
	
	import org.robotlegs.mvcs.Actor;

	/**
	 * This is the model for the alert data view. It stores
	 * a collection of alerts to be displayed in a datagrid
	 * component.
	 */ 
	public class AlertDataModel extends Actor
	{
		public static const ORIGINAL_ALERT_PROP_NAME:String = "com.deleidos.rtws.alertviz.origAlert";
		
		/** The collection contains a list alerts. */
		private var _alertDataList:ArrayCollection;
		
		/** Flag indicating if tracking is enabled */
		private var _trackingOn:Boolean;
		
		/** The alert criteria that is being tracked */
		private var _trackingFilterKey:String;
		
		/**
		 * Constructor.
		 */ 
		public function AlertDataModel()
		{
			_alertDataList = new ArrayCollection(new Array());
			trackingOn = false;
		}
		
		/**
		 * Retrieve the alert data list.
		 */ 
		public function get alertDataList():ArrayCollection
		{
			return _alertDataList;
		}
		
		/**
		 * Add an entry to the alert data list.
		 */ 
		public function add(value:Object):void
		{
			_alertDataList.addItemAt(value, 0);
		}
		
		/**
		 * Retrieve the tracking on flag.
		 */ 
		public function get trackingOn():Boolean
		{
			return _trackingOn;
		}
		
		/**
		 * Set the tracking on flag.
		 */ 
		public function set trackingOn(value:Boolean):void
		{
			_trackingOn = value;
		}
		
		/**
		 * Retrieve the tracking filter key.
		 */
		public function get trackingFilterKey():String
		{
			return _trackingFilterKey;
		}
		
		/**
		 * Set the tracking filter key.
		 */ 
		public function set trackingFilterKey(value:String):void
		{
			_trackingFilterKey = value;
		}
		
		/**
		 * Clears the model.
		 */ 
		public function clear():void
		{
			_trackingOn = false;
			_trackingFilterKey = null;
			_alertDataList.removeAll();
		}
		
		/**
		 * Adjusts the alert collection size to the given limit.
		 * If the limit is equal to the collection size, the oldest
		 * alert will be removed. If the limit is less than the 
		 * collection size, the oldest n elements (difference) 
		 * will be removed.
		 */ 
		public function adjustModelToQueueLimit(limit:int):void
		{
			// Honor the alert queue limit by removing the oldest
			// entries before adding the new entry
			
			if (_alertDataList.length >= limit) 
			{
				var diff:int = _alertDataList.length - limit;
				
				// Remove the difference so that we are at the queue limit
				
				for (var i:int = 0; i < diff; i++) {
					_alertDataList.removeItemAt(_alertDataList.length - 1);
				}
				
				// Remove one more to make room for the new entry
				
				if (_alertDataList.length > 0) {
					_alertDataList.removeItemAt(_alertDataList.length - 1);
				}
			}
		}
	}
}