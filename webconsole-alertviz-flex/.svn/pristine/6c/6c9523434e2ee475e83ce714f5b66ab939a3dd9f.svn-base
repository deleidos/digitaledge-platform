package com.deleidos.rtws.alertviz.expandgraph.graph
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.properties.CustomKey;
	import com.deleidos.rtws.alertviz.expandgraph.graph.properties.Key;
	import com.deleidos.rtws.alertviz.expandgraph.graph.properties.Properties;
	
	/**
	 *  
	 */
	public class DrillDownGraph
	{
		private var _nodes:Object;
		/** Map of Node Key -> Node */
		public function get nodes():Object{
			return _nodes;
		}
		
		private var _edgesOut:Object;
		/** Map of source Node Key -> destination Node Key -> Edge object */
		public function get edgesOut():Object{
			return _edgesOut;
		}
		
		private var _edgesIn:Object;
		/** Map of destination Node Key -> source Node Key -> Edge object */
		public function get edgesIn():Object{
			return _edgesIn;
		}
		
		private var _roots:Vector.<Node>;
		/** Roots for data to progagate through */
		public function get roots():Vector.<Node>{
			return _roots;
		}
		
		private var _graphSettings:GraphSettings;
		/** Settings that dictate how the graph will expand and be colored/labeled/sized. */
		public function get graphSettings():GraphSettings{
			return _graphSettings;
		}
		public function set graphSettings(value:GraphSettings):void{
			_graphSettings = value;
			refresh();
		}
		
		private var _mostRecentEdges:Object;
		public function get mostRecentEdges():Object{
			return _mostRecentEdges;
		}
		
		public function DrillDownGraph(g:GraphSettings)
		{
			graphSettings = g;
		}
		
		/** Applies any changes in the graph settings to the entire graph  */
		private function refresh():void{
			if(graphSettings == null){
				clear();
			}else{
				//Remember the keys of every node that was expanded so we can re-expand them after recreating the graph
				var expandedNodes:Object = new Object();
				for(var nodeKey:String in _nodes){
					if(_nodes[nodeKey].isExpanded)
						expandedNodes[nodeKey] = true;
				}
				
				_nodes = new Object();
				_edgesOut = new Object();
				_edgesIn = new Object();
				
				var oldData:Vector.<Object> = _roots == null || _roots.length == 0 ? new Vector.<Object>() : _roots[0].data;
				
				//Create the root nodes
				_roots = new Vector.<Node>();
				for(var id:String in graphSettings.properties){
					var newRootNodeProperties:Properties = graphSettings.getNodeProperty(id, 0);
					var rootKey:String = newRootNodeProperties.key.value.toString();
					
					var root:Node = new Node(id, 0, rootKey , newRootNodeProperties);
					_roots.push(root);
					_nodes[rootKey] = root;
					_edgesOut[rootKey] = new Object();
					_edgesIn[rootKey] = new Object();
				}
				
				//Orthogonally connect the root nodes
				for(var i:int = 0; i < _roots.length; i++){
					var fromNode:Node = _roots[i];
					var rootEdgeProperties:Properties = graphSettings.getEdgeProperty(fromNode.rootID, 0);
					for(var j:int = i + 1; j < _roots.length; j++){
						
						var toNode:Node = _roots[j];
						
						var edge:Edge = new Edge(fromNode, toNode, rootEdgeProperties);
						
						(_edgesOut[fromNode.key])[toNode.key] = edge;
						(_edgesIn[toNode.key])[fromNode.key] = edge;
					}
				}
				
				for each(var obj:Object in oldData)
					add(obj);
				
				reexpand(expandedNodes);
				
				_mostRecentEdges = new Object();
			}
		}
		
		private function reexpand(expandedNodes:Object, source:Node = null):void{
			if(source == null){
				for each( var root:Node in _roots )
					reexpand(expandedNodes, root);
			}else{
				if(expandedNodes[source.key]){
					expand(source);
					for each( var childEdge:Edge in _edgesOut[source.key] )	
						reexpand(expandedNodes, childEdge.to);
				}
			}
		}
		
		/** Adds the data to the graph, updating nodes and edges and creating them if necessary */
		public function add(data:Object):void{
			//Each propagation returns a Node representing the 
			//leaf node of the propagation
			var chains:Vector.<Node> = new Vector.<Node>();
			for each(var root:Node in _roots)
				chains.push(propagate(data, root));
			
			var newEdges:Object = new Object();
			//Every leaf node in the chains list is supposed to be connected to
			//every other leaf node in the chains list.
			//We must check if an edge exists between them, and if not, add one
			for(var i:int = 0; i < chains.length; i++){
				var fromNode:Node = chains[i];
				for(var j:int = i+1; j < chains.length; j++){
					var toNode:Node = chains[j];
					
					//See if an edge exists
					var curEdge:Edge = (_edgesOut[fromNode.key])[toNode.key];
					if(curEdge == null){ //If not, add one
						curEdge = new Edge(fromNode, toNode, graphSettings.getEdgeProperty(fromNode.rootID, fromNode.depth));
						(_edgesOut[fromNode.key])[toNode.key] = curEdge;
						(_edgesIn[toNode.key])[fromNode.key] = curEdge;
						curEdge.add(data);
					}
					
					if(newEdges[fromNode.key] == null)
						newEdges[fromNode.key] = new Object();
					
					(newEdges[fromNode.key])[toNode.key] = curEdge;
				}
			}
			
			_mostRecentEdges = newEdges;
		}
		
		/** Helps the add function in propagating the data through the tree */
		private function propagate(data:Object, source:Node):Node{
			//update current node
			source.add(data);
			
			//update current node's edges
			for each(var sourceEdge:Edge in _edgesOut[source.key]){
				var targetKey:CustomKey = sourceEdge.to.properties.key;
				if(sourceEdge.to.key == targetKey.evaluate(data).toString()) 
					sourceEdge.add(data);
			}
			
			if(source.isExpanded){ //we need to add the data to the appropriate child
				var childProperties:Properties = graphSettings.getNodeProperty(source.rootID, source.depth+1);
				if(childProperties == null) //the node does not expand anymore
					return source;
				
				var childKey:String = childProperties.key.evaluate(data).toString();
				var parentToChildEdge:Edge = (_edgesOut[source.key])[childKey];
				if(parentToChildEdge == null){ //if that child does not exist yet, create it
					var child:Node = new Node(source.rootID, source.depth+1, childKey, childProperties);
					var edge:Edge = new Edge(source, child, graphSettings.getEdgeProperty(source.rootID, source.depth));
					
					child.add(data);
					edge.add(data);
					
					_nodes[childKey] = child;
					_edgesOut[childKey] = new Object();
					_edgesIn[childKey] = new Object();
					
					(_edgesOut[source.key])[childKey] = edge;
					
					return child;
				}else //if that child already exists, continue the propagation
					return propagate(data, parentToChildEdge.to);
			}else //if the source is not expanded, our propagation ends
				return source;
		}
		
		/** Splits the node into its specified subsets, redirecting any edges connected to the node to its children */
		public function expand(source:Node):void{
			if(source == null) return;
			if(!source.isExpanded){
				var childProperties:Properties = graphSettings.getNodeProperty(source.rootID, source.depth+1);
				if(childProperties == null) //node cannot be expanded, so we ignore the command
					return;
				
				//Step 1: Split the data in the source node by the child key
				var childData:Object = new Object(); //Associates child key -> child data
				var childKey:String;
				for each(var o:Object in source.data){
					childKey = childProperties.key.evaluate(o).toString();
					if(childData[childKey] == null)
						childData[childKey] = new Vector.<Object>();
					
					childData[childKey].push(o);
				}
				
				//Step 2: Split the data in each of the source node's outbound edges by the child key
				var childEdgeOutData:Object = new Object(); //Associates child key -> child edge target -> child edge data
				var sourceEdgeKey:String;
				var sourceEdge:Edge;
				var edgeData:Object;
				for(sourceEdgeKey in _edgesOut[source.key]){
					sourceEdge = (_edgesOut[source.key])[sourceEdgeKey];
					for each(edgeData in sourceEdge.data){
						childKey = childProperties.key.evaluate(edgeData).toString();
						if(childEdgeOutData[childKey] == null)
							childEdgeOutData[childKey] = new Object();
						
						if((childEdgeOutData[childKey])[sourceEdge.to.key] == null)
							(childEdgeOutData[childKey])[sourceEdge.to.key] = new Vector.<Object>();
						
						(childEdgeOutData[childKey])[sourceEdge.to.key].push(edgeData);
					}
				}
				
				//Step 3: Split the data in each of the source node's inbound edges by the child key
				var childEdgeInData:Object = new Object(); //Associates child key -> child edge target -> child edge data
				for(sourceEdgeKey in _edgesIn[source.key]){
					sourceEdge = (_edgesIn[source.key])[sourceEdgeKey];
					for each(edgeData in sourceEdge.data){
						childKey = childProperties.key.evaluate(edgeData).toString();
						if(childEdgeInData[childKey] == null)
							childEdgeInData[childKey] = new Object();
						
						if((childEdgeInData[childKey])[sourceEdge.from.key] == null)
							(childEdgeInData[childKey])[sourceEdge.from.key] = new Vector.<Object>();
						
						(childEdgeInData[childKey])[sourceEdge.from.key].push(edgeData);
					}
				}
				
				
				//Step 4: Create all the children with their edges based on the split data
				var childNodes:Vector.<Node> = new Vector.<Node>();
				var newEdge:Edge;
				for(childKey in childData){
					var tmpChildData:Vector.<Object> = childData[childKey];
					var newChild:Node = new Node(source.rootID, source.depth+1, childKey, childProperties);
					
					newChild.addAll(tmpChildData); //Adds all the child data to the child
					
					//Now add all the edges to that child
					var childEdgeProperties:Properties = graphSettings.getEdgeProperty(source.rootID, source.depth+1);
					if(childEdgeProperties == null) //should never happen, so we throw an error
						throw Error("Missing edge properties. Abort.");
					
					_edgesOut[childKey] = new Object();
					var targetKey:String;
					var targetNode:Node;
					for(targetKey in childEdgeOutData[childKey]){
						targetNode = _nodes[targetKey];
						newEdge = new Edge(newChild, targetNode, childEdgeProperties);
						newEdge.addAll((childEdgeOutData[childKey])[targetKey]);
						(_edgesOut[childKey])[targetKey] = newEdge;
						(_edgesIn[targetKey])[childKey] = newEdge;
					}
					
					_edgesIn[childKey] = new Object();
					for(targetKey in childEdgeInData[childKey]){
						targetNode = _nodes[targetKey];
						newEdge = new Edge(targetNode, newChild, graphSettings.getEdgeProperty(targetNode.rootID, targetNode.depth));
						newEdge.addAll((childEdgeInData[childKey])[targetKey]);
						(_edgesIn[childKey])[targetKey] = newEdge;
						(_edgesOut[targetKey])[childKey] = newEdge;
					}
					
					_nodes[childKey] = newChild;
					childNodes.push(newChild);
				}
				
				//Step 5: Remove all the edge data from the source node
				for(var outEdgeKey:String in _edgesOut[source.key])
					delete (_edgesIn[outEdgeKey])[source.key];
				
				for(var inEdgeKey:String in _edgesIn[source.key])
					delete (_edgesOut[inEdgeKey])[source.key];
				
				_edgesIn[source.key] = new Object();
				_edgesOut[source.key] = new Object();
				
				//Step 6: Add edges from the source node to its children
				var sourceEdgeProperties:Properties = graphSettings.getEdgeProperty(source.rootID, source.depth);
				for each(var child:Node in childNodes){
					newEdge = new Edge(source, child, sourceEdgeProperties);
					newEdge.addAll(child.data);
					(_edgesOut[source.key])[child.key] = newEdge;
				}
				source.isExpanded = true;
			}
		}
		
		/** Removes all children of the targeted node and redirects all of its childrens edges to itself */
		public function collapse(source:Node):void{
			if(source == null) return;
			if(source.isExpanded){
				//collapse children and get the outbound and inbound edges of the leaf children
				var edgesOut:Vector.<Edge> = new Vector.<Edge>();
				var edgesIn:Vector.<Edge> = new Vector.<Edge>();
				for each(var childEdge:Edge in _edgesOut[source.key])
					propagateCollapse(childEdge.to, edgesOut, edgesIn);
				
				_edgesOut[source.key] = new Object(); //fresh start
				
				//collate any edge data with the same target node
				var edgeOutData:Object = new Object();
				var edge:Edge;
				for each(edge in edgesOut){
					if(edgeOutData[edge.to.key] == null)
						edgeOutData[edge.to.key] = new Vector.<Object>();
					
					edgeOutData[edge.to.key] = edgeOutData[edge.to.key].concat(edge.data);
				}
				
				var edgeInData:Object = new Object();
				for each(edge in edgesIn){
					if(edgeInData[edge.from.key] == null)
						edgeInData[edge.from.key] = new Vector.<Object>();
					
					edgeInData[edge.from.key] = edgeInData[edge.from.key].concat(edge.data);
				}
				
				//create new edges from and into the source for all the collated data
				var sourceEdgeProps:Properties = graphSettings.getEdgeProperty(source.rootID, source.depth);
				var targetKey:String;
				var targetNode:Node;
				var newEdge:Edge;
				for(targetKey in edgeOutData){
					targetNode = _nodes[targetKey];
					newEdge = new Edge(source, targetNode, sourceEdgeProps);
					newEdge.addAll(edgeOutData[targetKey]);
					(_edgesOut[source.key])[targetKey] = newEdge;
					(_edgesIn[targetKey])[source.key] = newEdge;
				}
				
				for(targetKey in edgeInData){
					targetNode = _nodes[targetKey];
					newEdge = new Edge(targetNode, source, graphSettings.getEdgeProperty(targetNode.rootID, targetNode.depth));
					newEdge.addAll(edgeInData[targetKey]);
					(_edgesOut[targetKey])[source.key] = newEdge;
					(_edgesIn[source.key])[targetKey] = newEdge;
				}
				
				source.isExpanded = false;
			}
		}
		
		private function propagateCollapse(source:Node, edgesOut:Vector.<Edge>, edgesIn:Vector.<Edge>):void{
			if(source.isExpanded){ //propagate to outbound edges
				for each(var sourceEdge:Edge in _edgesOut[source.key])
					propagateCollapse(sourceEdge.to, edgesOut, edgesIn);
			}else{
				var targetKey:String;
				for(targetKey in _edgesOut[source.key]){
					edgesOut.push((_edgesOut[source.key])[targetKey]);
					delete (_edgesIn[targetKey])[source.key];
				}
					
				for(targetKey in _edgesIn[source.key]){
					edgesIn.push((_edgesIn[source.key])[targetKey]);
					delete (_edgesOut[targetKey])[source.key];
				}
			}
			
			delete _edgesOut[source.key];
			delete _edgesIn[source.key];
			delete _nodes[source.key];
		}
		
		/** Toggles the expansion/collapse of the target node **/
		public function toggle(source:Node):void{
			if(source == null) return;
			if(source.isExpanded)
				collapse(source);
			else
				expand(source);
		}
		
		/** Removes the item from all the appropriate nodes **/
		public function removeItem(item:Object):void{
			for each( var node:Node in _roots )
				removeItemH(item, node);
		}
		
		private function removeItemH(item:Object, node:Node):void{
			node.deleteItem(item);
			for( var childKey:String in _edgesOut[node.key] ){
				var childEdge:Edge = (_edgesOut[node.key])[childKey];
				
				childEdge.deleteItem(item);
				if(node.isExpanded) //we only want to propagate the removal to the node's children
					removeItemH(item, childEdge.to); 
				if(childEdge.size() == 0) 
					delete (_edgesOut[node.key])[childKey];
			}
			
			if(node.size() == 0 && node.depth != 0){ //delete references to an empty node, unless it is a root node
				delete _edgesOut[node.key];
				delete _edgesIn[node.key];
				delete _nodes[node.key];
			}
		}
		
		/** Returns all nodes that contain a data point where matcherFunction(data point) == itemToMatch **/
		public function getNodes(itemToMatch:Object, matcherFunction:Function):Vector.<Node>{
			var nodes:Vector.<Node> = new Vector.<Node>();
			for each( var root:Node in roots ){
				var node:Node = getNodeH(root, itemToMatch, matcherFunction);
				if(node != null) nodes.push(node);
			}
				
			return nodes;
		}
		
		private function getNodeH(node:Node, itemToMatch:Object, matcherFunction:Function):Node{
			if(node.isExpanded){
				//edges out from an expanded node will only be connected to that node's children
				for each( var edgeOut:Edge in _edgesOut[node.key] ){ 
					var finalNode:Node = getNodeH(edgeOut.to, itemToMatch, matcherFunction);
					if(finalNode != null) return finalNode;
				}
				return null;
			}else
				return node.contains(itemToMatch, matcherFunction) ? node : null;
		}
		
		public function size():uint{
			return roots.length == 0 ? 0 : roots[0].data.length;
		}
		
		public function clear():void{
			_nodes = new Object();
			_edgesOut = new Object();
			_edgesIn = new Object();
			_roots = new Vector.<Node>();
			_mostRecentEdges = new Object();
			_graphSettings = null;
		}
		
	}
}