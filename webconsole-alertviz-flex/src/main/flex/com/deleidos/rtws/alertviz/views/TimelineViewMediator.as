package com.deleidos.rtws.alertviz.views
{
	import com.deleidos.rtws.alertviz.events.AlertDataCommandEvent;
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	import com.deleidos.rtws.alertviz.models.Alert;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	import com.deleidos.rtws.alertviz.models.TimelineEntry;
	import com.deleidos.rtws.alertviz.models.TimelineModel;
	import com.deleidos.rtws.alertviz.renderers.RtwsTimelineEntryRenderer;
	
	import flash.events.MouseEvent;
	
	import flexlib.scheduling.scheduleClasses.LayoutScrollEvent;
	import flexlib.scheduling.util.DateUtil;
	
	import mx.core.ClassFactory;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	import mx.events.ScrollEvent;
	import mx.events.ScrollEventDirection;
	
	import org.robotlegs.mvcs.Mediator;

	/**
	 * The mediator for the timeline view.
	 */ 
	public class TimelineViewMediator extends Mediator
	{
		[Inject] public var view:TimelineView;
		
		[Inject] public var model:TimelineModel;
		
		[Inject] public var parameterModel:ParameterModel;
		
		private var _prevTimelineSelection:TimelineEntry = null;
		
		/**
		 * Called by the robotleg framework when this mediator is registered.
		 * It contains a list of events this mediator is listening on and binds
		 * the alert model to the data provider in the view.
		 */
		public override function onRegister():void
		{
			view.alertsTimeline.addEventListener(ScrollEvent.SCROLL, handleScrollEvent);
			view.alertsSchedulerViewer.addEventListener(LayoutScrollEvent.PIXEL_SCROLL, handlePixelScrollEvent);
			
			view.alertsSchedulerViewer.addEventListener(MouseEvent.CLICK, handleMouseClickEvent);

			view.backButton.addEventListener(MouseEvent.CLICK, handleBackButtonEvent);
			view.forwardButton.addEventListener(MouseEvent.CLICK, handleForwardButtonEvent);
			
			view.zoomInButton.addEventListener(MouseEvent.CLICK, handleZoomInEvent);
			view.zoomOutButton.addEventListener(MouseEvent.CLICK, handleZoomOutEvent);
			
			model.alertList.addEventListener(CollectionEvent.COLLECTION_CHANGE, handleModelChange);
			
			addContextListener(WatchListCommandEvent.COLOR_CHANGE, handleColorChangeEvent);
			addContextListener(AlertDataCommandEvent.SELECTED, handleAlertDataSelectedEvent);
			addContextListener(AlertDataCommandEvent.DESELECTED, handleAlertDataDeselectedEvent);
			
			/* 
			 * Set the timeline renderer. This is what the timeline uses to plot the times.
			 * Has to be assigned here as the timeline component doesn't handle assignment
			 * at startup time correctly
			 */
			view.alertsTimeline.itemRenderer = new ClassFactory(RtwsTimelineEntryRenderer);
			
			view.startDate = roundDateToNearestMinute(new Date(), model.currZoom);
			view.endDate = new Date(view.startDate.getTime() + model.getZoomTimeDuration());
			
			view.dataProvider = model.alertList;
		}

		/**
		 * This is an event handler method that gets call when the
		 * scroll event gets caught and it sets the scheduler's
		 * x axis position.
		 */ 
		protected function handleScrollEvent(event:ScrollEvent):void 
		{
			view.alertsSchedulerViewer.xPosition = event.position;
		}
		
		/**
		 * This is an event handler method that gets called when the
		 * layout scroll event gets caught and it sets the timeline's
		 * x axis position.
		 */
		protected function handlePixelScrollEvent(event:LayoutScrollEvent):void
		{
			if(event.direction == ScrollEventDirection.HORIZONTAL)
			{
				view.alertsTimeline.xPosition = event.position;
			}
		}
		
		/**
		 * This is an event handler method that gets called when the user
		 * clicks on an area of the scheduler view. If a tick mark was
		 * selected, an event will be fired to let all interested parties
		 * know what alert was selected.
		 */
		protected function handleMouseClickEvent(event:MouseEvent):void
		{
			var currSelection:TimelineEntry = (view.alertsSchedulerViewer.selectedItem as TimelineEntry);
			
			if(currSelection !== _prevTimelineSelection) {
				if(_prevTimelineSelection != null) {
					dispatch(new AlertDataCommandEvent(AlertDataCommandEvent.DESELECTED, _prevTimelineSelection.alert));
				}
				
				if(currSelection != null) {
					dispatch(new AlertDataCommandEvent(AlertDataCommandEvent.SELECTED, currSelection.alert));
				}
			}
			
			_prevTimelineSelection = currSelection;
		}
		
		/**
		 * This event handler is called when the timeline model changes. If tracking is
		 * turned on, the handler will locate the most recent alert and pan to it.
		 */
		protected function handleModelChange(event:CollectionEvent):void
		{
			switch (event.kind)
			{
				case CollectionEventKind.ADD :
					var entry:TimelineEntry = event.items[0];
					
					if (model.trackingOn && entry.filterKey == model.trackingFilterKey) {
						moveToEntry(entry, 400);
					} else if (!model.trackingOn && parameterModel.timelineAutopan) {
						moveToEntry(entry, 400);
					}
					
					break;
				
				default:
					break;
			}
		}
		
		/**
		 * This is an event handler method that gets called when the user
		 * selects an alert from the alert data view. The handler will
		 * move to the entry in the timeline and select it.
		 */ 
		protected function handleAlertDataSelectedEvent(event:AlertDataCommandEvent):void
		{
			var entry:TimelineEntry = findTimelineEntry(event.alert);
			
			if(entry != null)
			{
				view.alertsSchedulerViewer.selectedItem = entry;
				_prevTimelineSelection = entry;
				moveToEntry(entry, 400);
			}
		}
		
		private function handleAlertDataDeselectedEvent(event:AlertDataCommandEvent):void
		{
			var entry:TimelineEntry = findTimelineEntry(event.alert);
			
			if(entry != null && view.alertsSchedulerViewer.selectedItem === entry)
			{
				view.alertsSchedulerViewer.selectedItem = null;
				_prevTimelineSelection = null;
			}
		}
		
		/**
		 * This is an event handler method that gets called when the user
		 * clicks on the back button. It will adjust the start and end
		 * time of the timeline and play an animation to let the user know
		 * that the action was executed. 
		 */
		protected function handleBackButtonEvent(event:MouseEvent):void
		{
			view.startDate = new Date(view.startDate.getTime() - model.getZoomTimeDuration());
			view.endDate = new Date(view.endDate.getTime() - model.getZoomTimeDuration());
			
			view.dataProvider.refresh();
			
			// Reset the xoffset to the initial value
			// and go to the start time of the schedule
			// viewer.
			
			view.alertsSchedulerViewer.xOffset = 50;
			
			view.alertsSchedulerViewer.gotoTime(0);
			
			view.anim.play();
		}
		
		/**
		 * This is an event handler method that gets called when the user
		 * clicks on the forward button. It will increment the start and end
		 * time of the timeline by one day and play an animation to let
		 * the user know that the action was executed. 
		 */
		protected function handleForwardButtonEvent(event:MouseEvent):void
		{
			view.startDate = new Date(view.startDate.getTime() + model.getZoomTimeDuration());
			view.endDate = new Date(view.endDate.getTime() + model.getZoomTimeDuration());
			
			view.dataProvider.refresh();
			
			// Reset the xoffset to the initial value
			// and go to the start time of the schedule
			// viewer.
			
			view.alertsSchedulerViewer.xOffset = 50;
			
			view.alertsSchedulerViewer.gotoTime(0);
			
			view.anim.play();
		}
		
		/**
		 * This is an event handler method that gets called when the user
		 * clicks on the zoom out button. It will adjust the start
		 * and end time accordingly and refresh the timeline.
		 */ 
		protected function handleZoomOutEvent(event:MouseEvent):void
		{
			if (model.currZoom == 1)
			{
				model.currZoom = 5;
			}
			else if (model.currZoom == 5)
			{
				model.currZoom = 15;
			}	
			else if (model.currZoom == 15) 
			{
				model.currZoom = 30;
			}
			else if (model.currZoom == 30)
			{
				model.currZoom = 60;
			}
			
			view.endDate = new Date(view.startDate.getTime() + model.getZoomTimeDuration());
			
			view.dataProvider.refresh();
			
			// Reset the xoffset to the initial value
			// and go to the start time of the schedule
			// viewer.
			
			view.alertsSchedulerViewer.xOffset = 50;
			
			view.alertsSchedulerViewer.gotoTime(0);
			
			view.anim.play();
		}
		
		/**
		 * This is an event handler method that gets called when the user
		 * clicks on the zoom in button. It will adjust the start
		 * and end time accordingly and refresh the timeline.
		 */ 
		protected function handleZoomInEvent(event:MouseEvent):void
		{
			if (model.currZoom == 60) 
			{
				model.currZoom = 30;
			}
			else if (model.currZoom == 30)
			{
				model.currZoom = 15;
			}
			else if (model.currZoom == 15)
			{
				model.currZoom = 5;	
			}
			else if (model.currZoom == 5)
			{
				model.currZoom = 1;
			}
			
			view.endDate = new Date(view.startDate.getTime() + model.getZoomTimeDuration());
			
			view.dataProvider.refresh();
			
			// Reset the xoffset to the initial value
			// and go to the start time of the schedule
			// viewer.
			
			view.alertsSchedulerViewer.xOffset = 50;
			
			view.alertsSchedulerViewer.gotoTime(0);
			
			view.anim.play();
		}
		
		/**
		 * This is a event handler method that gets called when this mediator
		 * catches the WatchListCommandEvent.COLOR_CHANGE event. It cycles
		 * through all the alerts and changes the background color of the tick
		 * mark to match that of what the user selected.
		 */
		protected function handleColorChangeEvent(event:WatchListCommandEvent):void
		{
			for (var i:int = 0; i < model.alertList.length; i++)
			{
				var entry:TimelineEntry = TimelineEntry(model.alertList.getItemAt(i));
				if (event.parameters.key == entry.filterKey) {
					entry.backgroundColor = event.parameters.color;
				}
			}
			
			view.dataProvider.refresh();
		}
		
		/**
		 * This helper method takes the date given and round it to
		 * the nearest N minute in the past.
		 * 
		 * If minutes is 60, round to nearest hour.
		 * If minutes is 30, round to nearest half hour.
		 * If minutes is 15, round to nearest quarter hour.
		 */ 
		private function roundDateToNearestMinute(date:Date, minute:int):Date
		{
			var dt:Date = new Date(date.getTime());
			
			if (minute == 60) {
				dt.setMinutes(0);
				dt.setSeconds(0);
				dt.setMilliseconds(0);
			}
			
			if (minute >= 1 && minute <= 59) {
				var mins:int = Math.floor(dt.getMinutes() / minute) * minute;
				
				dt.setMinutes(mins);
				dt.setSeconds(0);
				dt.setMilliseconds(0);
			}

			return dt;
		}
		
		/**
		 * Locate the entry on the timeline given the alert's uuid and criteria key.
		 */ 
		private function findTimelineEntry(alert:Alert):TimelineEntry
		{
			for (var i:int = 0; i < model.alertList.length; i++)
			{
				var entry:TimelineEntry = TimelineEntry(model.alertList.getItemAt(i));
				if (entry.alert === alert)
				{
					return entry;
				}
			}
			
			return null;
		}
		
		/**
		 * Adjust the timeline and the scheduler view and move to the given
		 * timeline entry. The xoffset parameter is the number of pixel from the
		 * left of the timeline we want to display the given entry. The internal
		 * of the method will readjust the xoffset using the entry's date so it
		 * can be displayed nicely in the timeline.
		 */ 
		private function moveToEntry(entry:TimelineEntry, xoffset:Number = 100):void
		{			
			// Calculate the new start time for the timeline and do some nifty calculation
			// to get the entry specific xoffset. The timeline is designed to be in 100 pixel
			// blocks and setting the xoffset to a value like 400 will move the target entry
			// to the right by 4 blocks. Its recommended to set the xoffset to a value divisible
			// by 100.
			
			var date:Date = null;
			
			switch(model.currZoom)
			{
				case 60:
					date = new Date(roundDateToNearestMinute(entry.alertDate, 60).getTime() - ((xoffset / 100) * DateUtil.HOUR_IN_MILLISECONDS));
					
					var hourZoomTotalMillis:Number = (entry.alertDate.getMinutes() * 60 * 1000) + (entry.alertDate.getSeconds() * 1000) + entry.alertDate.getMilliseconds();
					
					xoffset = xoffset + ((hourZoomTotalMillis / DateUtil.HOUR_IN_MILLISECONDS) * 100);
					
					break;
				
				case 30:
					date = new Date(roundDateToNearestMinute(entry.alertDate, 30).getTime() - ((xoffset / 100) * 30 * DateUtil.MINUTE_IN_MILLISECONDS));
					
					var halfHourZoomTotalMillis:Number = ((entry.alertDate.getMinutes() % 30) * 60 * 1000) + (entry.alertDate.getSeconds() * 1000) + entry.alertDate.getMilliseconds();
					
					xoffset = xoffset + (halfHourZoomTotalMillis / (DateUtil.HOUR_IN_MILLISECONDS / 2) * 100);
					
					break;
				
				case 15:
					if (entry.alertDate.getMinutes() < 15) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 60).getTime() - ((xoffset / 100) * 15 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 15 && entry.alertDate.getMinutes() < 30) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 15).getTime() - ((xoffset / 100) * 15 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 30 && entry.alertDate.getMinutes() < 45) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 30).getTime() - ((xoffset / 100) * 15 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 45).getTime() - ((xoffset / 100) * 15 * DateUtil.MINUTE_IN_MILLISECONDS));
					}

					var quarterHourZoomTotalMillis:Number = ((entry.alertDate.getMinutes() % 15) * 60 * 1000) + (entry.alertDate.getSeconds() * 1000) + entry.alertDate.getMilliseconds();
					
					xoffset = xoffset + (quarterHourZoomTotalMillis / (DateUtil.HOUR_IN_MILLISECONDS / 4) * 100);
					
					break;
				
				case 5:
					if (entry.alertDate.getMinutes() < 5) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 60).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 5 && entry.alertDate.getMinutes() < 10) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 5).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 10 && entry.alertDate.getMinutes() < 15) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 10).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 15 && entry.alertDate.getMinutes() < 20) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 15).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 20 && entry.alertDate.getMinutes() < 25) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 20).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 25 && entry.alertDate.getMinutes() < 30) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 25).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 30 && entry.alertDate.getMinutes() < 35) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 30).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 35 && entry.alertDate.getMinutes() < 40) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 35).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 40 && entry.alertDate.getMinutes() < 45) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 40).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 45 && entry.alertDate.getMinutes() < 50) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 45).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else if (entry.alertDate.getMinutes() >= 50 && entry.alertDate.getMinutes() < 55) {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 50).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					else {
						date = new Date(roundDateToNearestMinute(entry.alertDate, 55).getTime() - ((xoffset / 100) * 5 * DateUtil.MINUTE_IN_MILLISECONDS));
					}
					
					var fiveMinuteZoomTotalMillis:Number = ((entry.alertDate.getMinutes() % 5) * 60 * 1000) + (entry.alertDate.getSeconds() * 1000) + entry.alertDate.getMilliseconds();
					
					xoffset = xoffset + (fiveMinuteZoomTotalMillis / (DateUtil.HOUR_IN_MILLISECONDS / 12) * 100);
					
					break;
				
				case 1:
					date = new Date(roundDateToNearestMinute(entry.alertDate, 1).getTime() - ((xoffset / 100) * DateUtil.MINUTE_IN_MILLISECONDS));
					
					var minuteZoomTotalMillis:Number = (entry.alertDate.getSeconds() * 1000) + entry.alertDate.getMilliseconds();
					
					xoffset = xoffset + ((minuteZoomTotalMillis / DateUtil.MINUTE_IN_MILLISECONDS) * 100);
						
					break;
			}
			
			// Entry is not within the current timeframe. Adjust the timeline before moving to
			// the selected entry. The adjusted start date has already been calculated above.
			// This check here is to animate the timeline to let the user know that the timeline
			// has been changed.
			
			if (entry.alertDate <= view.startDate || entry.alertDate >= view.endDate) {
				view.anim.play();
			}
			
			// Adjust the start and end date of the timeline and refresh the data provider
			// to plot the entries onto the timeline.
			
			view.startDate = date;
			view.endDate = new Date(view.startDate.getTime() + model.getZoomTimeDuration());
			
			view.dataProvider.refresh();
			
			// Set the xoffset position and move to the target entry.

			view.alertsSchedulerViewer.xOffset = xoffset;
			
			view.alertsSchedulerViewer.moveToEntry(entry);
			
			view.alertsSchedulerViewer.selectedItem = entry;
			
			// Move the draggable canvas to position (0,0) because
			// the user could of dragged the canvas to the right side.

			view.alertsDragCanvas.move(0, 0);
			
			view.alertsDragCanvas.horizontalScrollPosition = 0;
		}
	}
}