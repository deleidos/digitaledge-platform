package com.deleidos.rtws.alertviz.models
{
	import com.adobe.serialization.json.JSON;

	/**
	 * This class represents an alert criteria associated to an
	 * alert that was consumed.
	 */
	public class AlertCriteria
	{
		/** The decoded alert criteria */
		private var _alertDefinition:Object;
	
		/**
		 * Constructor.
		 */ 
		public function AlertCriteria(alertDefinition:Object)
		{
			_alertDefinition = alertDefinition;
		}
		
		/**
		 * Retrieve the  alert criteria key.
		 */ 
		public function get key():String
		{
			return _alertDefinition.key;
		}
		
		/**
		 * Retrieve the  alert criteria name.
		 */ 
		public function get name():String
		{
			return _alertDefinition.name;
		}
		
		/**
		 * Retrieve the  alert criteria model.
		 */ 
		public function get model():String
		{
			return _alertDefinition.model;
		}
		
		/**
		 * Retrieve the  alert criteria description.
		 */ 
		public function get description():String
		{
			return _alertDefinition.description;
		}
		
		/**
		 * Retrieve the  alert criteria definition.
		 */ 
		public function get definition():Object
		{
			return _alertDefinition.definition;
		}
		
		/**
		 * Retrieve the  alert criteria definition in json format.
		 */ 
		public function get definitionString():String
		{
			return JSON.encode(_alertDefinition.definition);
		}
		
		/**
		 * Retrieve the alert criteria in json format.
		 */ 
		public function toString():String{
			return JSON.encode(_alertDefinition);
		}
	}
}