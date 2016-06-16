package com.deleidos.rtws.alertviz.expandgraph.graph.properties
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.Literal;

	public class Properties
	{
		
		public static var DEFAULT_WEIGHT:String = "10";
		public static var DEFAULT_COLOR:String = "0xCCCCCC";
		public static var DEFAULT_LABEL:String = "";
		
		/** key evaluation method + value */
		private var _key:CustomKey;
		public function get key():CustomKey{
			return _key;
		}
		public function set key(key:CustomKey):void{
			_key = key;
		}
		
		/** weight evaluation method */
		private var _weight:IEvaluable;
		public function get weight():IEvaluable{
			return _weight;
		}
		public function set weight(weight:IEvaluable):void{
			_weight = weight;
		}
		
		/** coloring evaluation method */
		private var _coloring:IEvaluable;
		public function get coloring():IEvaluable{
			return _coloring;
		}
		public function set coloring(coloring:IEvaluable):void{
			_coloring = coloring;
		}
		
		/** label evaluation method */
		private var _label:IEvaluable;
		public function get label():IEvaluable{
			return _label;
		}
		public function set label(label:IEvaluable):void{
			_label = label;
		}
		
		public function Properties(key:CustomKey, weight:IEvaluable, coloring:IEvaluable, label:IEvaluable){
			_key = key;
			_weight = weight == null ? new Literal(DEFAULT_WEIGHT) : weight;
			_coloring = coloring == null ? new Literal(DEFAULT_COLOR) : coloring;
			_label = label == null ? new Literal(DEFAULT_LABEL) : label;
		}
		
		/** 
		 * Returns a new Properties object where all values are reset */
		public function getDefault():Properties{
			return new Properties(key.reset(),weight.reset(),coloring.reset(),label.reset());
		}
		
		/** 
		 * Updates the weight, coloring, and label values
		 */
		public function addItem(o:Object):void{
			_weight.addItem(o);
			_coloring.addItem(o);
			_label.addItem(o);
		}
		
		/** 
		 * Updates the weight, coloring, and label values
		 */
		public function deleteItem(o:Object):void{
			_weight.deleteItem(o);
			_coloring.deleteItem(o);
			_label.deleteItem(o);
		}
		
		public function getXMLData():XML{
			var propXML:XML = <properties/>;
		
			var weightXML:XML = <weight/>;
			weightXML.appendChild(weight.value);
			
			var coloringXML:XML = <coloring/>;
			coloringXML.appendChild(coloring.value);
			
			var labelXML:XML = <label/>;
			labelXML.appendChild(label.value);
			
			propXML.appendChild(weightXML);
			propXML.appendChild(coloringXML);
			propXML.appendChild(labelXML);
			
			return propXML;
		}
		
	}
}