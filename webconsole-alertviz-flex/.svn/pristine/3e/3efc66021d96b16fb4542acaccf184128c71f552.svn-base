package com.deleidos.rtws.alertviz.renderers
{
	import com.deleidos.rtws.commons.util.StringUtils;
	
	import flash.globalization.DateTimeStyle;
	
	import flexlib.scheduling.timelineClasses.ITimeDescriptor;
	import flexlib.scheduling.timelineClasses.ITimelineEntryRenderer;
	
	import mx.collections.ArrayList;
	import mx.controls.Label;
	
	import spark.formatters.DateTimeFormatter;
	import spark.globalization.LastOperationStatus;
	
	/**
	 * This class is used to render the timeline entries.
	 */ 
	public class RtwsTimelineEntryRenderer extends Label implements ITimelineEntryRenderer
	{
		private static const LONG_DT_FORMATTER:DateTimeFormatter = new DateTimeFormatter();
		private static const STATIC_INIT_COMPLETE:Boolean = staticInit();
		
		private static const DEFAULT_LABEL_DATE_TIME_PATTERN:String = "hh:mm a";
		private static const PATTERN_CONVERSIONS:Array = [
			{ "mxTokenPattern": new RegExp("Y", "g"), "sparkToken": "y"},
			{ "mxTokenPattern": new RegExp("D", "g"), "sparkToken": "d"},
			{ "mxTokenPattern": new RegExp("A", "g"), "sparkToken": "a"},
			{ "mxTokenPattern": new RegExp("J", "g"), "sparkToken": "H"},
			{ "mxTokenPattern": new RegExp("H", "g"), "sparkToken": "k"},
			{ "mxTokenPattern": new RegExp("L", "g"), "sparkToken": "h"},
			{ "mxTokenPattern": new RegExp("N", "g"), "sparkToken": "m"},
			{ "mxTokenPattern": new RegExp("S", "g"), "sparkToken": "s"},
			{ "mxTokenPattern": new RegExp("Q", "g"), "sparkToken": "S"}
		];
		private static const PATTERN_CONVERSION_CACHE:ArrayList = new ArrayList();
		
		private static function staticInit():Boolean {
			LONG_DT_FORMATTER.useUTC = true;
			LONG_DT_FORMATTER.dateStyle = DateTimeStyle.LONG;
			
			return true;
		}
		
		private var _dateTimeFormatter:DateTimeFormatter = null;
		
		/**
		 * Constructor.
		 */ 
		public function RtwsTimelineEntryRenderer()
		{
			super();
			setStyle("textAlign", "center");
			setStyle("fontSize", "12");
		}
		
		/**
		 * This method gets called by the timeline component
		 * internally to format the timeline entry before it
		 * gets displayed.
		 */ 
		public override function set data(value:Object):void
		{
			var newText:String = "";
			var newToolTip:String = "";
			
			var entry:ITimeDescriptor = (value as ITimeDescriptor);
			if(entry != null && entry.date != null) {
			
				var patternConversion:Object = null;
				if(entry.description != null && (entry.description is String) && StringUtils.isNotBlank(String(entry.description))) {
					var mxPattern:String = (entry.description as String);
					
					for(var index:int=0; index < PATTERN_CONVERSION_CACHE.length; index++) {
						var currCacheConversion:Object = PATTERN_CONVERSION_CACHE.getItemAt(index);
						
						if(currCacheConversion["mxPattern"] == mxPattern) {
							patternConversion = currCacheConversion;
							break;
						}
					}
					
					if(patternConversion == null) {
						patternConversion = {"mxPattern": mxPattern, "sparkPattern": convertMxToSparkDateTimePattern(mxPattern)};
						PATTERN_CONVERSION_CACHE.addItem(patternConversion);
					}
				}
				
				var patternToUse:String = null;
				if(patternConversion != null) {
					patternToUse = patternConversion["sparkPattern"];
				}
				else {
					patternToUse = DEFAULT_LABEL_DATE_TIME_PATTERN;
				}
				
				if(_dateTimeFormatter == null || _dateTimeFormatter.dateTimePattern != patternToUse) {
					if(_dateTimeFormatter == null) {
						_dateTimeFormatter = new DateTimeFormatter();
						_dateTimeFormatter.useUTC = true;
					}
					
					_dateTimeFormatter.dateTimePattern = patternToUse;
				}
				
				newText = _dateTimeFormatter.format(entry.date);
				if(patternConversion != null && _dateTimeFormatter.lastOperationStatus != LastOperationStatus.NO_ERROR) {
					// The pattern conversion resulted in an invalid pattern ... set it to the valid default
					patternConversion["sparkPattern"] = DEFAULT_LABEL_DATE_TIME_PATTERN;
					_dateTimeFormatter.dateTimePattern = DEFAULT_LABEL_DATE_TIME_PATTERN;
					newText = _dateTimeFormatter.format(entry.date);
				}
				newToolTip = LONG_DT_FORMATTER.format(entry.date);
			}
			
			this.text = newText;
			this.toolTip = newToolTip;
		}
		
		private static function convertMxToSparkDateTimePattern(mxPattern:String):String {
			var result:String = null;
			
			if(mxPattern == null) {
				return null;
			}
			else
			{
				result = mxPattern;
				for(var index:int=0; index < PATTERN_CONVERSIONS.length; index++) {
					result = result.replace(PATTERN_CONVERSIONS[index]["mxTokenPattern"], PATTERN_CONVERSIONS[index]["sparkToken"]);
				}
			}
			
			return result;
		}
	}
}