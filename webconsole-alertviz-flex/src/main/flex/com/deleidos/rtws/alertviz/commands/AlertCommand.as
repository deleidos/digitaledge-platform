package com.deleidos.rtws.alertviz.commands
{
	import com.deleidos.rtws.alertviz.events.AlertCommandEvent;
	import com.deleidos.rtws.alertviz.models.Alert;
	import com.deleidos.rtws.alertviz.models.AlertDataModel;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	import com.deleidos.rtws.alertviz.models.TimelineEntry;
	import com.deleidos.rtws.alertviz.models.TimelineModel;
	import com.deleidos.rtws.alertviz.models.WatchListModel;
	import com.deleidos.rtws.commons.util.NumberUtils;
	
	import org.robotlegs.mvcs.Command;
	
	import spark.formatters.DateTimeFormatter;

	/**
	 * This class contains the logic to handle alert command events.
	 */ 
	public class AlertCommand extends Command
	{
		[Inject]
		public var event:AlertCommandEvent;
		
		[Inject]
		public var timelineModel:TimelineModel;
		
		[Inject]
		public var watchListModel:WatchListModel;
		
		[Inject]
		public var alertDataModel:AlertDataModel;
		
		[Inject]
		public var paramModel:ParameterModel;
		
		private var gmtDateFormatter:DateTimeFormatter
		private var gmtTimeFormatter:DateTimeFormatter
		private var gmtDateTimeFormatter:DateTimeFormatter
		
		public function AlertCommand()
		{
			gmtDateFormatter = new DateTimeFormatter();
			gmtDateFormatter.dateTimePattern = "MM/dd/yyyy Z";
			gmtDateFormatter.useUTC = true;
			
			gmtTimeFormatter = new DateTimeFormatter();
			gmtTimeFormatter.dateTimePattern = "hh:mm a Z";
			gmtTimeFormatter.useUTC = true;
			
			gmtDateTimeFormatter = new DateTimeFormatter();
			gmtDateTimeFormatter.dateTimePattern = "MM/dd/yyyy hh:mm a Z";
			gmtDateTimeFormatter.useUTC = true;
		}
		
		/**
		 * This method is executed when a alert command event is dispatched.
		 */ 
		public override function execute():void 
		{
			if (event.type == AlertCommandEvent.ADD)
			{
				// Parses the json alert that was received
				
				var alert:Alert = event.alert;
				var alertDate:Date = null;
				
				if(event.alert.record != null && event.alert.record.hasOwnProperty("timestamp_ms")) {
					var timestamp:Number = NaN;
					
					if(event.alert.record["timestamp_ms"] is Number) {
						timestamp = (event.alert.record["timestamp_ms"] as Number);
					}
					else if(event.alert.record["timestamp_ms"] is String) {
						var millisStr:String = (event.alert.record["timestamp_ms"] as String);
						timestamp = NumberUtils.parseNumber(millisStr);
					}
					
					if(isNaN(timestamp) == false) {
						try{
							alertDate = new Date(timestamp);
						}
						catch(error:Error) {
							alertDate = null;
						}
					}
				}
				
				
				if(alertDate == null) {
					alertDate = new Date();
					alertDate.time += (Math.round(Math.random() * 5000) * (Math.random() >= .5 ? 1 : -1));
				}
			
				// Create an entry for the timeline model. This model is for the
				// timeline and scheduler view.
				
				var entry:TimelineEntry = new TimelineEntry();
				entry = new TimelineEntry();
			
				entry.alertDate = alertDate;
				entry.backgroundColor = watchListModel.getWatchListEntryColor(alert.criteria.key);
				entry.alert = alert;
				
				timelineModel.adjustModelToQueueLimit(paramModel.alertQueueLimit);	
				timelineModel.add(entry);
				
				// Create an entry for the alert data model. This model is for the
				// alert data textual view.
				
				alertDataModel.adjustModelToQueueLimit(paramModel.alertQueueLimit);
				var extractedValues:Object = extractJsonChildNodes(alert.record);
				extractedValues['ALERT DATE'] = gmtDateFormatter.format(alertDate);
				extractedValues['ALERT TIME'] = gmtTimeFormatter.format(alertDate);
				extractedValues['ALERT TIMESTAMP'] = gmtDateTimeFormatter.format(alertDate);
				extractedValues['ALERT CRITERIA'] = alert.criteria.name;
				extractedValues['ALERT CRITERIA KEY'] = alert.criteria.key;
				extractedValues[AlertDataModel.ORIGINAL_ALERT_PROP_NAME] = alert;
				alertDataModel.add(extractedValues);
			}
		}
		
		/**
		 * Extracts all the child node of the alert json record.
		 */ 
		private function extractJsonChildNodes(record:Object):Object
		{
			var children:Object = new Object();
			
			extractJsonChildNodesHelper(record, children);
			
			return children;
		}
		
		/**
		 * A helper method to recursively extract the child nodes of the alert json record.
		 */ 
		private function extractJsonChildNodesHelper(record:Object, children:Object):void
		{
			for (var key:Object in record)
			{
				if (record[key] is String || record[key] is Number) 
				{
					var header:String = camelCaseToText(String(key));
					
					children[header] = record[key];
				}
				else 
				{
					extractJsonChildNodesHelper(record[key], children);
				}
			}
		}
		
		/**
		 * Converts a camel case string to a space delimited upper case string.
		 * 
		 * ex1: path 	    --> PATH
		 * ex2: modelName   --> MODEL NAME
		 * ex3: object1Name --> OBJECT1 NAME
		 */ 
		private function camelCaseToText(value:String):String
		{
			var header:String = new String();
			var re:RegExp = /[A-Z]/g;
			
			var i:int = 0;
			var result:Object = re.exec(value);
			while (result != null)
			{
				var part:String;
				
				if (result.index != 0)
				{
					part = value.substring(i, result.index);
					header += part + " ";
				}
				
				i = result.index;
				result = re.exec(value);
			}
			
			part = value.substring(i, value.length);
			header += part;
			
			return header.toUpperCase();
		}
	}
}