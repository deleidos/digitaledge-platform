package com.deleidos.rtws.alertviz.views
{
	import com.deleidos.rtws.alertviz.events.AlertDataCommandEvent;
	import com.deleidos.rtws.alertviz.googlemap.events.MapViewEvent;
	import com.deleidos.rtws.alertviz.models.Alert;
	import com.deleidos.rtws.alertviz.models.AlertDataModel;
	
	import flash.events.Event;
	import flash.net.URLLoader;
	import flash.net.URLRequest;
	
	import mx.collections.ArrayList;
	import mx.events.CollectionEvent;
	import mx.events.CollectionEventKind;
	
	import org.robotlegs.mvcs.Mediator;

	/**
	 * The mediator for the alert data view.
	 */ 
	public class AlertDataViewMediator extends Mediator
	{
		[Inject] public var view:AlertDataView;
		
		[Inject] public var model:AlertDataModel;
		
		private var columnOrderStr:String = new String();
		private var columnOrder:Array = new Array();
		
		/**
		 * Called by the robotleg framework when this mediator is registered.
		 * It contains a list of events this mediator is listening on and binds
		 * the alert model to the data provider in the view.
		 */ 
		public override function onRegister():void
		{
			view.alerts = model.alertDataList;
			
			//loads config file
			var loader:URLLoader = new URLLoader();
			loader.load(new URLRequest("columnOrderConfig.txt"));
			loader.addEventListener(Event.COMPLETE, function(e:Event):void {
				columnOrderStr = e.target.data;
				columnOrder = columnOrderStr.split("\n");
				
				view.columnOrderStr = e.target.data;
			});
			
			model.alertDataList.addEventListener(CollectionEvent.COLLECTION_CHANGE, handleModelChange);
			
			addViewListener(AlertDataCommandEvent.DESELECTED, dispatch);
			addViewListener(AlertDataCommandEvent.SELECTED, dispatch);
			
			addContextListener(AlertDataCommandEvent.DESELECTED, onAlertDeselected);
			addContextListener(AlertDataCommandEvent.SELECTED, onAlertSelected);
			
			// TODO Remove after switch map to use the standard events
			addContextListener(MapViewEvent.SELECTED, handleMapViewSelected);
		}
		
		private function handleModelChange(event:CollectionEvent):void
		{
			switch (event.kind)
			{
				case CollectionEventKind.ADD :
					if(model.trackingOn) {
						var scrollTargets:Vector.<Object> = new Vector.<Object>();
						
						if(event.items != null && event.items.length > 0) {
							
							var currEntry:Object = null;
							for(var index:int=event.items.length - 1; index >= 0; index--) {
								currEntry = event.items[event.items.length];
								if(currEntry != null && currEntry.hasOwnProperty("ALERT CRITERIA KEY") && currEntry["ALERT CRITERIA KEY"] == model.trackingFilterKey){
									scrollTargets.push(currEntry);
								}
							}
						}
						
						if(scrollTargets.length > 0)
						{
							view.scrollToAlerts(scrollTargets);
						}
					}
					break;
				default:
					break;
			}
		}
		
		private function onAlertSelected(event:AlertDataCommandEvent):void
		{
			var selectionTargets:ArrayList = new ArrayList();
			
			if(event.alert != null) {
				var selectionTarget:Object = findTabularModelData(event.alert);
				if(selectionTarget != null) {
					selectionTargets.addItem(selectionTarget);
				}
			}
			
			view.selectAlerts(selectionTargets);
		}
		
		private function onAlertDeselected(event:AlertDataCommandEvent):void
		{
			var deselectionTargets:ArrayList = new ArrayList();
			
			if(event.alert != null) {
				var deselectionTarget:Object = findTabularModelData(event.alert);
				if(deselectionTarget != null) {
					deselectionTargets.addItem(deselectionTarget);
				}
			}
			
			view.deselectAlerts(deselectionTargets);
		}
		
		private function handleMapViewSelected(event:MapViewEvent):void
		{
			if(event.parameters != null && event.parameters.hasOwnProperty("uid") && event.parameters.hasOwnProperty("filterKey")) {
				var selectionTargets:ArrayList = new ArrayList();
				
				var selectionTarget:Object = findTabularModelDataByKeys(event.parameters["uid"], event.parameters["filterKey"]);
				if(selectionTarget != null) {
					selectionTargets.addItem(selectionTarget);
				}
				
				view.selectAlerts(selectionTargets);
			}
		}
		
		/** Mapping methods to translate events betwen the tabular data model and the rest of the system.  Don't shoot the messenger */
		private function findTabularModelData(alert:Alert):Object {
			var result:Object = null;
			
			if(model.alertDataList != null) {
				for (var index:int = (model.alertDataList.length - 1); index >= 0; index--) {
					var entry:Object = model.alertDataList.getItemAt(index);
					if(entry != null && entry[AlertDataModel.ORIGINAL_ALERT_PROP_NAME] === alert) {
						result = entry;
						break;
					}
				}
			}
			
			return result;
		}
		
		private function findTabularModelDataByKeys(uuid:String, filterKey:String):Object {
			var result:Object = null;
			
			if(model.alertDataList != null) {
				for (var index:int = (model.alertDataList.length - 1); index >= 0; index--) {
					var entry:Object = model.alertDataList.getItemAt(index);
					if(entry != null && entry['UUID'] == uuid && entry['ALERT CRITERIA KEY'] == filterKey) {
						result = entry;
						break;
					}
				}
			}
			
			return result;
		}
		/** End Mapping methods */
	}
}