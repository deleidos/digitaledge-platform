package com.deleidos.rtws.alertviz.models
{
	import flexlib.scheduling.util.DateUtil;
	
	import mx.collections.ArrayCollection;
	
	import org.robotlegs.mvcs.Actor;
	
	/**
	 * This model stores a collection of alerts.
	 */ 
	public class TimelineModel extends Actor
	{
		/** A collection of alert entries. */
		private var _alertList:ArrayCollection;
		
		/** Flag indicating if tracking is enabled. */
		private var _trackingOn:Boolean = false;
		
		/** The alert criteria that is being tracked. */
		private var _trackingFilterKey:String;
		
		/** The current zoom level */
		private var _currZoom:int;
		
		/** This associative array stores a list of zoom levels (hourly, half-hourly, and quarter-hourly) */
		private var _zoomLevel:Object = 
			{
				60:DateUtil.DAY_IN_MILLISECONDS,
				30:DateUtil.HOUR_IN_MILLISECONDS * 12,
				15:DateUtil.HOUR_IN_MILLISECONDS * 6,
				5:DateUtil.HOUR_IN_MILLISECONDS * 2,
				1:DateUtil.MINUTE_IN_MILLISECONDS * 24
			};
		
		/**
		 * Constructor.
		 */ 
		public function TimelineModel()
		{
			_alertList = new ArrayCollection(new Array());
			_trackingOn = false;
			_currZoom = 1;
		}
		
		/**
		 * Retrieve the list of alerts.
		 */ 
		public function get alertList():ArrayCollection
		{
			return _alertList;	
		}
		
		/**
		 * Adds an alert to the collection.
		 */ 
		public function add(entry:TimelineEntry):void
		{
			entry.adjustStartEndDate(getTimelineEntryOffset());
			
			_alertList.addItem(entry);
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
		 * Retrieve the current zoom level.
		 */ 
		public function get currZoom():int
		{
			return _currZoom;
		}
		
		/**
		 * Set the current zoom level.
		 */ 
		public function set currZoom(value:int):void
		{
			_currZoom = value;
			
			adjustTimelineEntryToZoomLevel();
		}
		
		/**
		 * Retrieve the duration of the time range of the current
		 * zoom level.
		 */ 
		public function getZoomTimeDuration():Number
		{
			return _zoomLevel[_currZoom];
		}
		
		/**
		 * Clears the list of alerts.
		 */ 
		public function clear():void
		{
			_trackingOn = false;
			_trackingFilterKey = null;
			_alertList.removeAll();
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
			
			if (_alertList.length >= limit) 
			{
				var diff:int = _alertList.length - limit;
				
				// Remove the difference so that we are at the queue limit
				
				for (var i:int = 0; i < diff; i++) {
					_alertList.removeItemAt(0);
				}
				
				// Remove one more to make room for the new entry
				
				if (_alertList.length > 0) {
					_alertList.removeItemAt(0);
				}
			}
		}
		
		/**
		 * 
		 */ 
		private function getTimelineEntryOffset():Number
		{
			var offset:Number = (3.25 * 60 * 1000);
			
			if (_currZoom == 60) {
				return offset;
			}
			else if (_currZoom == 30) {
				return offset / 2;
			}
			else if (_currZoom == 15) {
				return offset / 4;
			}
			else if (_currZoom == 5) {
				return offset / 12;
			}
			else {
				return offset / 60;
			}
		}
		
		/**
		 * 
		 */ 
		private function adjustTimelineEntryToZoomLevel():void
		{
			var offset:Number = getTimelineEntryOffset();
			
			for (var i:int = 0; i < _alertList.length; i++)
			{
				var entry:TimelineEntry = TimelineEntry(_alertList.getItemAt(i));
				entry.adjustStartEndDate(offset);
			}
		}
	}
}