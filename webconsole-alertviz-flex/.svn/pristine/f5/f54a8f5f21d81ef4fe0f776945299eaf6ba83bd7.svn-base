package com.deleidos.rtws.alertviz.models
{
	
	import flexlib.scheduling.scheduleClasses.IScheduleEntry;
	import flexlib.scheduling.scheduleClasses.SimpleScheduleEntry;

	/**
	 * Stores the alert data and inherits from the schedule entry so
	 * the scheduler knows how to draw the tick mark.
	 */ 
	public class TimelineEntry extends SimpleScheduleEntry
	{	
		/** The width of the tick mark */
		private var _tickWidth			:int = 10;
		
		/** The alert date */
		private var _alertDate			:Date; 
		
		/** The background color of the tick mark */
		private var _backgroundColor	:uint;
		
		/** The content of the alert */
		private var _alert				:Alert;
		
		/** 
		 * Retrieve the alert name.
		 */ 
		public function get alertName():String
		{
			return _alert.criteria.name;
		}
		
		/** 
		 * Retrieve the alert definition key.
		 */ 
		public function get filterKey():String
		{
			return _alert.criteria.key;
		}
		
		/**
		 * Retrieve the alert data.
		 */ 
		public function get alert():Alert 
		{
			return _alert;
		}
		
		/**
		 * Set the alert data.
		 */ 
		public function set alert(value:Alert):void 
		{
			_alert = value;
			this.label = value.criteria.name;
		}
		/** 
		 * Retrieve the tick mark background color.
		 */ 
		public function get backgroundColor():uint
		{
			return _backgroundColor;
		}
		
		/** 
		 * Set the tick mark background color.
		 */
		public function set backgroundColor(value:uint):void
		{
			_backgroundColor = value;
		}
		
		/**
		 * Retrieve the alert date.
		 */  
		public function get alertDate():Date
		{
			return _alertDate;
		}
		
		/**
		 * Set the alert date.
		 */ 
		public function set alertDate(value:Date):void
		{
			_alertDate = value;
		}
		
		/**
		 * Retrieve the width of the tick mark.
		 */
		public function get tickWidth():int
		{
			return _tickWidth;
		}
		
		/**
		 * Sets the tick mark's starting and ending point on the timeline.
		 * 
		 * The start date is set to be 3 mins 25 seconds before the alert date.
		 * The end date is set to be 3 mins 25 seconds after the alert date.
		 */ 
		public function adjustStartEndDate(offset:Number):void
		{
			this.startDate = new Date(alertDate.getTime() - offset);
			this.endDate = new Date(alertDate.getTime() + offset);
		}
	}
}