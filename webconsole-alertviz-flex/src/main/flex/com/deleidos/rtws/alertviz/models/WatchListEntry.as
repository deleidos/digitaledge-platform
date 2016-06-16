package com.deleidos.rtws.alertviz.models
{
	/**
	 * Represent a single item in the watch list model.
	 */
	public class WatchListEntry
	{
		/** Flag indicating if tracking is turned off or on */
		[Bindable]
		private var _alertTracking	:Boolean;
		
		/** The color for alerts that corresponds to this alert definition */
		[Bindable]
		private var _alertColor		:uint;
		
		/** The alert definition key */
		[Bindable]
		private var _alertDefKey	:String;
		
		/** The alert definition name */
		[Bindable]
		private var _alertDefName	:String
		
		/** The alert definition model */
		[Bindable]
		private var _alertDefModel	:String;
		
		/** The alert definition description */
		[Bindable]
		private var _alertDefDesc	:String;
		
		/** The alert definition */
		[Bindable]
		private var _alertDef		:String;
		
		/**
		 * Constructor.
		 */ 
		public function WatchListEntry(
			alertDefKey:String, alertDefName:String, alertDefModel:String, 
			alertDefDesc:String, alertDef:String, alertColor:uint)
		{
			_alertTracking = false;
			_alertColor = alertColor;
			_alertDefKey = alertDefKey;
			_alertDefName = alertDefName;
			_alertDefModel = alertDefModel;
			_alertDefDesc = alertDefDesc;
			_alertDef = alertDef;
		}
		
		/**
		 * Retrieve the alert definition key.
		 */ 
		public function get alertDefKey():String
		{
			return _alertDefKey;
		}
		
		/**
		 * Set the alert definition key.
		 */
		public function set alertDefKey(value:String):void
		{
			_alertDefKey = value;
		}
		
		/**
		 * Retrieve the alert definition name.
		 */
		public function get alertDefName():String
		{
			return _alertDefName;
		}
		
		/**
		 * Set the alert definition name.
		 */
		public function set alertDefName(value:String):void
		{
			_alertDefName = value;
		}
		
		/**
		 * Retrieve the alert definition model.
		 */
		public function get alertDefModel():String
		{
			return _alertDefModel;
		}
		
		/**
		 * Set the alert definition model.
		 */
		public function set alertDefModel(value:String):void
		{
			_alertDefModel = value;
		}
		
		/**
		 * Retrieve the alert definition description.
		 */
		public function get alertDefDesc():String
		{
			return _alertDefDesc;
		}
		
		/**
		 * Set the alert definition description.
		 */
		public function set alertDefDesc(value:String):void
		{
			_alertDefDesc = value;
		}
		
		/**
		 * Retrieve the alert definition.
		 */
		public function get alertDef():String
		{
			return _alertDef;
		}
		
		/**
		 * Set the alert definition.
		 */
		public function set alertDef(value:String):void
		{
			_alertDef = value;
		}
		
		/**
		 * Retrieve the alert tracking flag.
		 */
		public function get alertTracking():Boolean
		{
			return _alertTracking;
		}
		
		/**
		 * Set the alert tracking flag.
		 */
		public function set alertTracking(value:Boolean):void
		{
			_alertTracking = value;
		}
		
		/**
		 * Retrieve the alert definition color.
		 */
		public function get alertColor():uint
		{
			return _alertColor;
		}
		
		/**
		 * Set the alert definition color.
		 */
		public function set alertColor(value:uint):void
		{
			_alertColor = value;
		}
	}
}