package com.deleidos.rtws.alertviz.models
{
	import mx.collections.ArrayCollection;
	
	import org.robotlegs.mvcs.Actor;
	
	/**
	 * This is the model for the alert criteria view. It stores
	 * a collection of alerts criterias to be displayed in the 
	 * datagrid componenet.
	 */ 
	public class AlertCriteriaModel extends Actor
	{
		/** The collection contains a list alert criterias. */
		private var _alertDefList:ArrayCollection;
		
		/**
		 * Constructor.
		 */ 
		public function AlertCriteriaModel()
		{
			_alertDefList = new ArrayCollection();
		}
		
		/**
		 * Retrieve the alert criteria list.
		 */ 
		public function get alertDefList():ArrayCollection
		{
			return _alertDefList;	
		}
		
		/**
		 * Clears the alert criteria list.
		 */ 
		public function clear():void
		{
			_alertDefList.removeAll();
		}
		
		/**
		 * Find the alert criteria given the criteria key.
		 */ 
		public function findAlertDef(key:String):AlertCriteriaEntry
		{
			var entry:AlertCriteriaEntry = null;
			
			for (var i:int = 0; i < _alertDefList.length; i++) 
			{
				entry = AlertCriteriaEntry(_alertDefList.getItemAt(i));
				if (entry.alertDefKey == key) 
				{
					return entry;
				}
			}
			
			return null;
		}
		
		/**
		 * Add an alert criteria to the list.
		 */ 
		public function addAlertDef(key:String, name:String, model:String, desc:String, def:String):void
		{			
			var entry:AlertCriteriaEntry = new AlertCriteriaEntry(key, name, model, desc, def);
			_alertDefList.addItem(entry);
		}
		
		/**
		 * Updates a alert criteria.
		 */ 
		public function updateAlertDef(key:String, name:String, model:String, desc:String, def:String):void
		{
			var entry:AlertCriteriaEntry = null;

			for (var i:int = 0; i < _alertDefList.length; i++) 
			{
				entry = AlertCriteriaEntry(_alertDefList.getItemAt(i));
				if (entry.alertDefKey == key) 
				{
					break;
				}
			}
			
			if (entry != null) 
			{
				entry.alertDefName = name;
				entry.alertDefModel = model;
				entry.alertDefDesc = desc;
				entry.alertDef = def;
			}			
		}
		
		/**
		 * Removes a alert criteria from the list.
		 */ 
		public function removeAlertDef(key:String):void
		{
			var index:int = -1;
			
			for (var i:int = 0; i < _alertDefList.length; i++) 
			{
				var entry:AlertCriteriaEntry = AlertCriteriaEntry(_alertDefList.getItemAt(i));
				if (entry.alertDefKey == key) 
				{
					index = i;
					break;
				}
			}
			
			if (index != -1) 
			{
				_alertDefList.removeItemAt(index);
			}
		}
	}
}