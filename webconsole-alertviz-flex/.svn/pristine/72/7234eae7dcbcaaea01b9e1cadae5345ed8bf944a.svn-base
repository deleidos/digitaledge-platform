package com.deleidos.rtws.alertviz.models
{
	import com.adobe.serialization.json.JSON;

	/**
	 * This class represent an alert that was consumed.
	 */ 
	public class Alert
	{
		/** The decoded alert. */
		private var _alert:Object;
		
		/** The alert criteria that is associated with this alert. */
		private var _criteria:AlertCriteria;
		
		/**
		 * Constructor.
		 */ 
		public function Alert(alert:Object)
		{
			_alert = alert;
			_criteria = new AlertCriteria(alert.filter);
		}
		
		/**
		 * Retrieves the uuid value from the alert.
		 */ 
		public function get id():String 
		{
			return record.standardHeader.uuid;
		}
		
		/**
		 * Retrieves the model from the alert.
		 */ 
		public function get model():String
		{
			return record.standardHeader.modelName;
		}
		
		/**
		 * Retrieves the decoded alert record.
		 */ 
		public function get record():Object{
			return _alert.record;
		}
		
		/**
		 * Retrieve the alert criteria associated with the alert.
		 */ 
		public function get criteria():AlertCriteria
		{
			return _criteria;	
		}
		
		/**
		 * Retrieve the alert in its consumed json form. 
		 */ 
		public function toString():String
		{
			return JSON.encode(_alert);
		}
	}
}