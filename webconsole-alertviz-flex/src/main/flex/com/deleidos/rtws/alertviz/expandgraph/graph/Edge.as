package com.deleidos.rtws.alertviz.expandgraph.graph
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.properties.Properties;
	
	public class Edge
	{
		private var _from:Node;
		/** What node the edge comes from */
		public function get from():Node{
			return _from;
		}
		
		private var _to:Node;
		/** What node the edge connects to */
		public function get to():Node{
			return _to;
		}
		
		private var _properties:Properties;
		/** Edge properties */
		public function get properties():Properties{
			return _properties;
		}
		
		private var _data:Vector.<Object>;
		/** 
		 * Data used to calculate weights/colorings/labels. Each data point must contain the same value for the edge key.
		 */
		public function get data():Vector.<Object>{
			return _data;
		}
		
		public function Edge(from:Node, to:Node, properties:Properties){
			_from = from;
			_to = to;
			_properties = properties.getDefault();
			_data = new Vector.<Object>();
		}
		
		public function add(datapoint:Object):void{
			_data.push(datapoint);
			_properties.addItem(datapoint);
		}
		
		public function addAll(datapoints:Vector.<Object>):void{
			for each(var datapoint:Object in datapoints){
				_data.push(datapoint);
				_properties.addItem(datapoint);
			}
		}
		
		public function deleteItem(item:Object):void{
			for (var i:int = 0; i < _data.length; i++){
				var datapoint:Object = _data[i];
				if(item == datapoint){
					_properties.deleteItem(datapoint);
					_data.splice(i,1);
					i--;
				}
			}
		}
		
		public function size():uint{
			return _data.length;
		}
		
		public function equals(edge:Edge):Boolean{
			return (_from.key == edge.from.key) && (_to.key == edge.to.key);
		}
	}
}