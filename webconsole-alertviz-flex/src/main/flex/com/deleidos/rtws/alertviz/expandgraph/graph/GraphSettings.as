package com.deleidos.rtws.alertviz.expandgraph.graph
{
	
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.functions.FunctionFactory;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.IEvaluable;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.Literal;
	import com.deleidos.rtws.alertviz.expandgraph.graph.evaluable.ObjectReference;
	import com.deleidos.rtws.alertviz.expandgraph.graph.properties.CustomKey;
	import com.deleidos.rtws.alertviz.expandgraph.graph.properties.GraphProperties;
	import com.deleidos.rtws.alertviz.expandgraph.graph.properties.Properties;
	import com.deleidos.rtws.alertviz.utils.OrderedObject;

	public class GraphSettings
	{
		
		//This set of consts determines the XML tags used in the creation and interpreting
		//of the graph settings configuration file
		public static const SETTINGS:String = "settings";
		public static const CHAIN:String = "chain";
		public static const ROOT:String = "root";
		public static const NODE:String = "node";
		
		public static const KEY:String = "key";
		public static const NODE_PROPERTIES:String = "nodeProperties";
		public static const EDGE_PROPERTIES:String = "edgeProperties";
		public static const WEIGHT:String = "weight";
		public static const COLOR:String = "color";
		public static const LABEL:String = "label";
		
		public static const VALUE:String = "value";
		public static const DISPLAY_VALUE:String = "name";
		
		public static const MODEL:String = "model";
		
		public static const HEADER:ObjectReference = new ObjectReference("record");
		
		private static const DATA_MODEL:ObjectReference = new ObjectReference("record.standardHeader.modelName");
		public static function getDataModel(alert:Object):String{
			return DATA_MODEL.evaluate(alert);
		}
		
		private var _dataModel:String;
		/** The name of the data model for these settings */
		public function get dataModel():String{
			return _dataModel;
		}
		/*
		public function set dataModel(dataModel:String):void{
			_dataModel = dataModel;
		}
		*/
		
		private var _properties:OrderedObject;
		/** Maps root ID -> vector of graph properties */
		public function get properties():Object{
			return _properties;
		}
		/*
		public function set properties(properties:Object):void{
			var xmlProps:XML = properties as XML;
			if(xmlProps == null){
				_properties = properties as OrderedObject;
				if(_properties == null) //Make our own OrderedObject
					_properties = new OrderedObject(properties);
			}else{
				_dataModel = xmlProps.@[MODEL] == null ? "" : xmlProps.@[MODEL];
				_properties = convertFromXML(xmlProps);	
			}
		}
		*/
		
		public function GraphSettings(properties:OrderedObject, dataModel:String){
			_properties = properties;
			_dataModel = dataModel;
		}
		
		/** Returns the number of root nodes a graph using these settings would have */
		public function numRoots():uint{
			var length:uint = 0;
			for(var key:String in properties)
				length++;
			
			return length;
		}
		
		/** Returns the Property object describing a node with the given rootID and depth */
		public function getNodeProperty(rootID:String, depth:uint):Properties{
			if(rootID >= _properties.length || depth >= _properties[rootID].length)
				return null;

			return (_properties[rootID])[depth].nodeProperties;
		}
		
		/** Returns the Property object describing an edge where the originating node has the given rootID and depth */
		public function getEdgeProperty(rootID:String, depth:uint):Properties{
			if(_properties[rootID] == null || depth >= _properties[rootID].length)
				return null;
			
			return (_properties[rootID])[depth].edgeProperties;
		}
		
		public static function fromXML(xml:XML):GraphSettings{
			if(xml.localName() != SETTINGS) return null;
			
			var obj:OrderedObject = new OrderedObject();
			var model:String = getModelFromXML(xml);

			var rootIndex:int = 0;
			for each( var chain:XML in xml.children() ){
				if(chain.localName() != CHAIN) continue;
				
				var rootName:String = chain.@[DISPLAY_VALUE];
				var keyList:Vector.<IEvaluable> = new Vector.<IEvaluable>();
				var nodes:Vector.<GraphProperties> = new Vector.<GraphProperties>();
				var isRoot:Boolean = true;
				
				//get expansions
				for each( var node:XML in chain.children() ){ 
					if(node.localName() != ROOT && node.localName() != NODE) continue;
					var nodeName:String = node.@[DISPLAY_VALUE];
					
					if(isRoot)
						isRoot = false;
					else
						keyList.push(FunctionFactory.parse(node[KEY][VALUE].@[DISPLAY_VALUE], HEADER));
					
					var tmp:Vector.<IEvaluable> = new Vector.<IEvaluable>();
					for each( var val:IEvaluable in keyList )
						tmp.push(val.reset()); //deep clone
	
					var customKey:CustomKey = new CustomKey(rootIndex.toString(), tmp);
					
					var nodePropertyWeight:IEvaluable = FunctionFactory.parse(node[NODE_PROPERTIES][WEIGHT][VALUE].@[DISPLAY_VALUE], HEADER);
					var nodePropertyColor:IEvaluable = FunctionFactory.parse(node[NODE_PROPERTIES][COLOR][VALUE].@[DISPLAY_VALUE], HEADER);
					var nodePropertyLabel:IEvaluable = FunctionFactory.parse(node[NODE_PROPERTIES][LABEL][VALUE].@[DISPLAY_VALUE], HEADER);
					var nodeProperty:Properties = new Properties(customKey, nodePropertyWeight, nodePropertyColor, nodePropertyLabel);
					
					var edgePropertyWeight:IEvaluable = FunctionFactory.parse(node[EDGE_PROPERTIES][WEIGHT][VALUE].@[DISPLAY_VALUE], HEADER);
					var edgePropertyColor:IEvaluable = FunctionFactory.parse(node[EDGE_PROPERTIES][COLOR][VALUE].@[DISPLAY_VALUE], HEADER);
					var edgePropertyLabel:IEvaluable = FunctionFactory.parse(node[EDGE_PROPERTIES][LABEL][VALUE].@[DISPLAY_VALUE], HEADER);
					var edgeProperty:Properties = new Properties(customKey, edgePropertyWeight, edgePropertyColor, edgePropertyLabel);
					
					nodes.push(new GraphProperties(nodeName, nodeProperty, edgeProperty));
				}
				
				obj[rootName] = nodes;
				rootIndex++;
			}
			
			return new GraphSettings(obj, model);
		}
		
		public function toXML():XML{
			var xml:XML = new XML("<" + GraphSettings.SETTINGS + "/>");
			if(_dataModel) xml.@[MODEL] = _dataModel;
			for(var rootID:String in properties){
				var chain:Vector.<GraphProperties> = properties[rootID];
				var chainXML:XML = makeXML(CHAIN, rootID);
				var isRoot:Boolean = true;
				for each(var prop:GraphProperties in chain){
					var nodeXML:XML;
					
					var nodeProp:Properties = prop.nodeProperties as Properties;
					var edgeProp:Properties = prop.edgeProperties as Properties;
					
					if(isRoot){
						nodeXML = makeXML(ROOT, prop.name);
						isRoot = false;
					}else{
						nodeXML = makeXML(NODE, prop.name);
						
						var keys:Vector.<String> = nodeProp.key.getDefinition();
						var keyXML:XML = makeXML(KEY, "Key", keys[keys.length-1]);
						
						nodeXML.appendChild(keyXML);
					}
					
					var nodePropertiesXML:XML = makeXML(NODE_PROPERTIES, "Node Properties");
					var npd:Array = [ nodeProp.weight.toString(), nodeProp.coloring.toString(), nodeProp.label.toString() ];
					nodePropertiesXML.appendChild(makeXML(WEIGHT, "Weight", npd[0]));
					nodePropertiesXML.appendChild(makeXML(COLOR, "Color", npd[1]));
					nodePropertiesXML.appendChild(makeXML(LABEL, "Label", npd[2]));
					
					var edgePropertiesXML:XML = makeXML(EDGE_PROPERTIES, "Edge Properties");
					var epd:Array = [ edgeProp.weight.toString(), edgeProp.coloring.toString(), edgeProp.label.toString() ];
					edgePropertiesXML.appendChild(makeXML(WEIGHT, "Weight", epd[0]));
					edgePropertiesXML.appendChild(makeXML(COLOR, "Color", epd[1]));
					edgePropertiesXML.appendChild(makeXML(LABEL, "Label", epd[2]));
					
					nodeXML.appendChild(nodePropertiesXML);
					nodeXML.appendChild(edgePropertiesXML);
					
					chainXML.appendChild(nodeXML);
				}
				xml.appendChild(chainXML);
			}
			return xml;
		}
		
		public static function getDefaultNode():XML{
			var defaultLiteral:Literal = new Literal("");
			return makeXML(NODE, "New Node")
					.appendChild(makeXML(KEY, "Key", defaultLiteral.toString()))
					.appendChild(makeXML(NODE_PROPERTIES, "Node Properties")
							.appendChild(makeXML(WEIGHT, "Weight", defaultLiteral.toString()))
							.appendChild(makeXML(COLOR, "Color", defaultLiteral.toString()))
							.appendChild(makeXML(LABEL, "Label", defaultLiteral.toString())))
					.appendChild(makeXML(EDGE_PROPERTIES, "Edge Properties")
							.appendChild(makeXML(WEIGHT, "Weight", defaultLiteral.toString()))
							.appendChild(makeXML(COLOR, "Color", defaultLiteral.toString()))
							.appendChild(makeXML(LABEL, "Label", defaultLiteral.toString())));
		}
		
		public static function getDefaultChain():XML{
			var defaultLiteral:Literal = new Literal("");
			return makeXML(CHAIN, "New Chain")
					.appendChild(makeXML(ROOT, "Root")
							.appendChild(makeXML(NODE_PROPERTIES, "Node Properties")
								.appendChild(makeXML(WEIGHT, "Weight", defaultLiteral.toString()))
								.appendChild(makeXML(COLOR, "Color", defaultLiteral.toString()))
								.appendChild(makeXML(LABEL, "Label", defaultLiteral.toString())))
							.appendChild(makeXML(EDGE_PROPERTIES, "Edge Properties")
								.appendChild(makeXML(WEIGHT, "Weight", defaultLiteral.toString()))
								.appendChild(makeXML(COLOR, "Color", defaultLiteral.toString()))
								.appendChild(makeXML(LABEL, "Label", defaultLiteral.toString()))));
		}
		
		protected static function makeXML( name:String, value:String = null, innerValue:String = null):XML{
			if(name == null)
				return null;

			var xml:XML = new XML("<" + name + "/>");
			if(value != null) xml.@[DISPLAY_VALUE] = value;
			if(innerValue != null) xml.appendChild(makeXML(VALUE, innerValue));
			
			return xml;
		}
		
		public static function getModelFromXML(xml:XML):String{
			return "@" + MODEL in xml ? xml.@[MODEL] : null;
		}
	}
}