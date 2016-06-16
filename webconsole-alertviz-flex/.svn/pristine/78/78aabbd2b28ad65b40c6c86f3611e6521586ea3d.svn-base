package com.deleidos.rtws.alertviz.expandgraph.models
{
	import com.deleidos.rtws.alertviz.expandgraph.events.GraphChangedEvent;
	import com.deleidos.rtws.alertviz.expandgraph.graph.DrillDownGraph;
	import com.deleidos.rtws.alertviz.expandgraph.graph.GraphSettings;
	import com.deleidos.rtws.alertviz.expandgraph.graph.Node;
	import com.deleidos.rtws.alertviz.models.Alert;
	
	import mx.collections.ArrayList;
	import mx.utils.ObjectUtil;
	
	import org.robotlegs.mvcs.Actor;
	
	/**
	 * This model handles the underlying graph data structure and provides extra functionality
	 * such as event firing when alerts are added as well as an additional layer of data filters.
	 */
	public class GraphModel extends Actor
	{
		/**
		 * Description of how to connect the data.
		 */
		public function get graphSettings():GraphSettings{
			return _graph ? _graph.graphSettings : null;
		}
		public function set graphSettings(g:GraphSettings):void{
			if(_graph != null){
				_graph.graphSettings = g;
				dispatch(new GraphChangedEvent(GraphChangedEvent.NEW_SETTINGS));
			}else
				_graph = new DrillDownGraph(g);
			
			updateVisibility();
		}
		
		private var _graph:DrillDownGraph;
		/**
		 * Graph object responsible for manipulating the data
		 * to be displayed.
		 */
		public function get graph():DrillDownGraph{
			return _graph;
		}
		
		private var _maxSize:uint = uint.MAX_VALUE;
		public function get maxSize():uint{
			return _maxSize;
		}
		public function set maxSize(value:uint):void{
			_maxSize = value;
			if(_curSize > _maxSize){
				var diff:int = _curSize - _maxSize;
				for(var i:int = 0; i < diff; i++){
					if(_data[i].visible)
						graph.removeItem(_data[i].alert);
				}
				
				_data.splice(0, diff);
				_curSize = _maxSize;
			}
		}
		
		private var _silent:Boolean;
		/** Determines if the graph view is updated when an event is added or not */
		public function get silent():Boolean{
			return _silent;
		}
		public function set silent(value:Boolean):void{
			_silent = value;
		}
		
		private function matchesModel(alert:Alert):Boolean{
			return graphSettings ? alert.model == graphSettings.dataModel : false;
		}
		
		private function matchesCriteria(alert:Alert):Boolean{
			if(!alert) return false;
			if(_filters.length == 0) return true; //if there are no criteria, then the alert automatically passes
			
			for each( var criteriaKey:String in _filters )
				if (alert.criteria.key == criteriaKey) return true;
				
			return false; 
		}
		
		private var _filters:Vector.<String> = new Vector.<String>();
		private var _curSize:uint;
		private var _data:Vector.<AlertWrapper> = new Vector.<AlertWrapper>();
		
		/**
		 * Adds an alert to the underlying graph.
		 */
		public function add(alert:Alert, silent:Boolean = false):void{
			if(_maxSize != 0){
				if(_curSize == _maxSize){ //delete oldest element
					var aw:AlertWrapper = _data.shift();
					if(aw.visible)
						graph.removeItem(aw.alert);
				}else
					_curSize++;
				
				if(graph == null || !matchesModel(alert) || !matchesCriteria(alert))
					_data.push(new AlertWrapper(alert, false));
				else{
					_data.push(new AlertWrapper(alert, true));
					_graph.add(alert);
					if(!_silent && !silent) //keeps the visual graph from updating, useful for when there is bulk loading of data, such as after a refresh
						dispatch(new GraphChangedEvent(GraphChangedEvent.NEW_EVENT));
				}
			}
		}
		
		/**
		 * Adds a criteria key to the list of filtering keys. An alert's criteria key must match at 
		 * least one of the filtering keys in order to be displayed.
		 */
		public function addFilter(key:String):void{
			_filters.push(key);
			updateVisibility();
		}
		
		/**
		 * Removes a criteria key to the list of filtering keys. An alert's criteria key must match at 
		 * least one of the filtering keys in order to be displayed.
		 */
		public function deleteFilter(key:String):void{
			for (var i:int = 0; i < _filters.length; i++ ){
				if(_filters[i] == key)
					_filters.splice(i, 1);
			}
			
			updateVisibility();
		}

		private function updateVisibility():void{
			if(graph != null){
				for each(var aw:AlertWrapper in _data){
					var visible:Boolean = matchesModel(aw.alert) && matchesCriteria(aw.alert);
					if(aw.visible && !visible) //alert was visible and is now not after update
						graph.removeItem(aw.alert);
					else if(!aw.visible && visible) //alert wasn't visible and now is after update
						graph.add(aw.alert);
					
					aw.visible = visible;
				}
				dispatch(new GraphChangedEvent(GraphChangedEvent.NEW_EVENT));
			}
		}
		
		/**
		 * Expands or collapses the node specified by the key string.
		 */
		public function toggle(keyString:String):void{
			if(graph == null)
				return;
			
			_graph.toggle(_graph.nodes[keyString]);
			dispatch(new GraphChangedEvent(GraphChangedEvent.TOGGLE));
		}
		
		public function clear():void
		{
			_silent = false;
			_graph = null;
			_filters = new Vector.<String>();
			_data = new Vector.<AlertWrapper>();
		}
	}
}
import com.deleidos.rtws.alertviz.models.Alert;

class AlertWrapper{
	
	public var alert:Alert;
	public var visible:Boolean;
	
	public function AlertWrapper(alert:Alert, visible:Boolean){
		this.alert = alert;
		this.visible = visible;
	}
}