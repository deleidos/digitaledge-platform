package com.deleidos.rtws.alertviz.expandgraph.graph
{
	import com.deleidos.rtws.alertviz.expandgraph.events.NodeEvent;
	import com.deleidos.rtws.alertviz.expandgraph.graph.properties.Key;
	import com.deleidos.rtws.alertviz.expandgraph.graph.properties.Properties;
	
	import flash.events.EventDispatcher;
	
	import mx.utils.ObjectUtil;

	public class Node extends EventDispatcher
	{
		
		private var _rootID:String;
		/** The node's root node ID */
		public function get rootID():String{
			return _rootID;
		}
		
		private var _depth:int;
		/** The depth of the node from the root node */
		public function get depth():int{
			return _depth;
		}
		
		private var _key:String = null;
		/** Node key */
		public function get key():String{
			return _key;
		}
	
		private var _properties:Properties;
		/** Node properties */
		public function get properties():Properties{
			return _properties;
		}
		
		private var _isExpanded:Boolean = false;
		/** Says whether or not the node is expanded */
		public function get isExpanded():Boolean{
			return _isExpanded;
		}
		public function set isExpanded(b:Boolean):void{
			_isExpanded = b;
		}
		
		private var _data:Vector.<Object>;
		/** 
		 * Data used to calculate weights/colorings/labels.
		 * Each data point must contain the same value for the node key.
		 */
		public function get data():Vector.<Object>{
			return _data;
		}
		
		
		public function Node(rootID:String, depth:uint, key:String, nProperties:Properties)
		{
			_rootID = rootID;
			_depth = depth;
			_key = key;
			_properties = nProperties.getDefault();
			_data = new Vector.<Object>();
		}
		
		/**
		 * Adding a data point adds it to the node's data and updates the node properties
		 */
		public function add(datapoint:Object):void{
			_data.push(datapoint);
			_properties.addItem(datapoint);
			dispatchEvent(new NodeEvent(NodeEvent.ADD_ITEM, _key, datapoint));
		}
		
		public function addAll(datapoints:Vector.<Object>):void{
			for each(var datapoint:Object in datapoints){
				_data.push(datapoint);
				_properties.addItem(datapoint);
				dispatchEvent(new NodeEvent(NodeEvent.ADD_ITEM, _key, datapoint));
			}
		}
		
		public function deleteItem(item:Object):void{
			for (var i:int = 0; i < _data.length; i++){
				var datapoint:Object = _data[i];
				if(item === datapoint){
					_properties.deleteItem(datapoint);
					_data.splice(i, 1);
					i--;
					dispatchEvent(new NodeEvent(NodeEvent.DELETE_ITEM, _key, datapoint));
				}
			}
		}
		
		public function size():uint{
			return _data.length;
		}
		
		public function equals(node:Node):Boolean{
			return _key == node._key;
		}
		
		public function contains(itemToMatch:Object, matcherFunction:Function):Boolean{
			for each( var obj:Object in _data ){ 
				if(ObjectUtil.compare(matcherFunction(obj), itemToMatch) == 0)
					return true;
			}
			return false;
		}
		
	}
}