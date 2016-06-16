package com.deleidos.rtws.alertviz.googlemap
{
	import com.deleidos.rtws.alertviz.events.AlertCommandEvent;
	import com.deleidos.rtws.alertviz.events.AlertDataCommandEvent;
	import com.deleidos.rtws.alertviz.events.ApplicationCommandEvent;
	import com.deleidos.rtws.alertviz.events.CommandEvent;
	import com.deleidos.rtws.alertviz.events.TimelineCommandEvent;
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	import com.deleidos.rtws.alertviz.googlemap.events.MapViewEvent;
	import com.deleidos.rtws.alertviz.models.AlertVizPropertiesModel;
	
	import mx.events.ResizeEvent;
	
	import org.robotlegs.mvcs.Mediator;

	/**
	 * The mediator for the map view.
	 */
	public class MapViewMediator extends Mediator
	{
		[Inject] public var propsModel:AlertVizPropertiesModel;
		
		[Inject]
		public var view:MapView;
		
		/**
		 * Called by the robotleg framework when this mediator is registered.
		 */
		public override function onRegister():void
		{
			view.apiKey = propsModel.googleMapsApiKey;
			view.sensorEnabled = propsModel.googleMapsSensorEnabled;
			
			addViewListener(MapViewEvent.MAP_READY, onMapReady);
		}
		
		private function onMapReady(event:MapViewEvent):void {
			// Add the listeners here once the map has been created.
			
			// Listens for events dispatched through robotlegs from other views in AlertViz
			eventMap.mapListener(eventDispatcher, AlertCommandEvent.ADD, onAlertAdded);
			eventMap.mapListener(eventDispatcher, ApplicationCommandEvent.CLEAR, handleClearEvent);
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.COLOR_CHANGE, forwardColorChangeEvent);
			eventMap.mapListener(eventDispatcher, CommandEvent.QUEUE_LIMIT_CHANGE, forwardQueueLimitChangeEvent);
			eventMap.mapListener(eventDispatcher, TimelineCommandEvent.SELECTED, forwardTimelineClickEvent);
			eventMap.mapListener(eventDispatcher, AlertDataCommandEvent.SELECTED, forwardAlertDataClickEvent);
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.TRACK_ON, forwardItemDoubleClick);
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.TRACK_OFF, forwardItemDoubleClick);
			
			// Listens for events dispatched from somewhere in the MapView
			view.addEventListener(ResizeEvent.RESIZE, handleResizeEvent);
			view.addEventListener(MapViewEvent.OVERRIDE, forwardMapOverrideEvent);
			view.addEventListener(MapViewEvent.SELECTED, forwardSelectedEvent);
		}
		
		/**
		 * Catches the resize event on the view and adjust the
		 * width and height of the map appropriately.
		 */ 
		protected function handleResizeEvent(event:ResizeEvent):void
		{
			view.map.width = view.width;
			view.map.height = view.height;
		}
		
		/**
		 * Catches the alert to add a record to the map and calls the appropriate function.
		 */
		private function onAlertAdded(event:AlertCommandEvent):void 
		{
			view.addAlertToMap(event.alert);
		}
		
		/**
		 * Handles the situation when a user clears the application data.
		 */ 
		public function handleClearEvent(event:ApplicationCommandEvent):void
		{
			view.clearMapData();
		}
		
		/**
		 * Catches the alert to change the color of markers and calls the appropriate function.
		 */ 
		public function forwardColorChangeEvent(alert:WatchListCommandEvent):void 
		{
			view.handleColorChangeEvent(alert.parameters.key, alert.parameters.color);
		}
		
		/**
		 * Catches the alert to change the number of records on the map at a time and calls the appropriate function.
		 */ 
		public function forwardQueueLimitChangeEvent(alert:CommandEvent):void 
		{
			view.handleQueueLimitChangeEvent(alert.parameters.queueLimit);
		}
		
		/**
		 * Catches the alert to zoom into a marker and calls the appropriate function.
		 */ 
		public function forwardTimelineClickEvent(timelineEvent:TimelineCommandEvent):void 
		{
			var filterKey:String = view.handleMouseClickEvent(timelineEvent.alert);
			dispatch(new MapViewEvent(MapViewEvent.OVERRIDE, {filterKey:filterKey}));
		}
		
		/**
		 * Catches the alert to zoom into a marker and calls the appropriate function.
		 */ 
		public function forwardAlertDataClickEvent(alertDataEvent:AlertDataCommandEvent):void 
		{
			var filterKey:String = view.handleMouseClickEvent(alertDataEvent.alert);
			dispatch(new MapViewEvent(MapViewEvent.OVERRIDE, {filterKey:filterKey}));
		}
		
		/**
		 * Catches the alert to start or stop following a alert criteria and calls the appropriate function.
		 */ 
		public function forwardItemDoubleClick(alert:WatchListCommandEvent):void 
		{
			view.handleItemDoubleClickEvent(alert.type, alert.parameters.filterKey);
		}
		
		/**
		 * Catches the event where the user wants to follow a single marker. This method will
		 * forward an event to the other views to turn off tracking.
		 */ 
		public function forwardMapOverrideEvent(event:MapViewEvent):void 
		{
			dispatch(new MapViewEvent(MapViewEvent.OVERRIDE, {filterKey:event.parameters}));
		}
		
		/**
		 * Catches the event where the user selects an alert on the map and forwards it to the other views.
		 */ 
		public function forwardSelectedEvent(event:MapViewEvent):void
		{
			dispatch(new MapViewEvent(MapViewEvent.SELECTED, event.parameters));
		}
	}
}