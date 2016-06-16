package com.deleidos.rtws.alertviz.expandgraph.views
{
	import com.deleidos.rtws.alertviz.events.ApplicationCommandEvent;
	import com.deleidos.rtws.alertviz.events.CommandEvent;
	import com.deleidos.rtws.alertviz.events.TimelineCommandEvent;
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	import com.deleidos.rtws.alertviz.expandgraph.events.ClientKeyboardEvent;
	import com.deleidos.rtws.alertviz.expandgraph.events.GraphChangedEvent;
	import com.deleidos.rtws.alertviz.expandgraph.events.GraphClientEvent;
	import com.deleidos.rtws.alertviz.expandgraph.events.NodeEvent;
	import com.deleidos.rtws.alertviz.expandgraph.graph.Edge;
	import com.deleidos.rtws.alertviz.expandgraph.graph.Node;
	import com.deleidos.rtws.alertviz.expandgraph.models.GraphModel;
	
	import flash.events.Event;
	import flash.events.KeyboardEvent;
	
	import mx.core.ClassFactory;
	import mx.events.CloseEvent;
	import mx.events.FlexEvent;
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;
	import org.un.cava.birdeye.ravis.components.renderers.edgeLabels.BaseEdgeLabelRenderer;
	import org.un.cava.birdeye.ravis.components.renderers.nodes.SizeByValueNodeRenderer;
	import org.un.cava.birdeye.ravis.graphLayout.data.Graph;
	import org.un.cava.birdeye.ravis.graphLayout.data.INode;
	import org.un.cava.birdeye.ravis.graphLayout.layout.*;
	import org.un.cava.birdeye.ravis.graphLayout.visual.IVisualEdge;
	import org.un.cava.birdeye.ravis.graphLayout.visual.IVisualNode;
	import org.un.cava.birdeye.ravis.graphLayout.visual.edgeRenderers.FlowEdgeRenderer;
	import org.un.cava.birdeye.ravis.graphLayout.visual.events.VisualNodeEvent;
	import org.un.cava.birdeye.ravis.utils.events.HoverMenuEvent;
	
	public class GraphMediator extends Mediator
	{
		[Inject]
		public var view:GraphView;
		
		[Inject]
		public var dataModel:GraphModel;
		
		private var viewIDToModelNodeID:Object = new Object();
		private var viewIDToModelEdgeID:Object = new Object();
		
		private var modelIDToNodeViewID:Object = new Object();
		private var modelIDToEdgeViewID:Object = new Object();
		
		private var nodeSnapshot:Object = new Object();
		private var edgeSnapshot:Object = new Object();	
		private var highlightSnapshot:Object = new Object();
		
		private var selectedSnapshot:Object = new Object();
		private var selectionParameters:Object = new Object();
		
		private var nodeInfoPopups:Object = new Object();

		private static const HIGHLIGHT_COLOR:String = "0x000000";
		private static const DEFAULT_NODE_LABEL:String = "[No Value]";
		private static const DEFAULT_EDGE_LABEL:String = "";
		
		public override function onRegister():void {
			view.vgraph.addEventListener(VisualNodeEvent.CLICK, handleNodeClick);
			view.vgraph.addEventListener(VisualNodeEvent.DOUBLE_CLICK, handleNodeDoubleClick);
			view.vgraph.addEventListener(VisualNodeEvent.MORE_INFO_REQUESTED, handleNodeInfoRequest);
			view.addEventListener(ClientKeyboardEvent.KEY_DOWN, handleKeyboardEvent);
			view.addEventListener(ClientKeyboardEvent.KEY_UP, handleKeyboardEvent);
			
			eventMap.mapListener(eventDispatcher, GraphChangedEvent.NEW_EVENT, updateView);
			eventMap.mapListener(eventDispatcher, GraphChangedEvent.NEW_SETTINGS, updateView);
			eventMap.mapListener(eventDispatcher, GraphChangedEvent.TOGGLE, updateView);
			eventMap.mapListener(eventDispatcher, GraphChangedEvent.REFRESH, updateView);
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.TRACK_ON, onFocusOn);
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.TRACK_OFF, onFocusOff);
			eventMap.mapListener(eventDispatcher, TimelineCommandEvent.SELECTED, onAlertSelect);
			eventMap.mapListener(eventDispatcher, TimelineCommandEvent.DESELECTED, onAlertDeselect);
			eventMap.mapListener(eventDispatcher, ApplicationCommandEvent.CLEAR, onClear);
			eventMap.mapListener(eventDispatcher, CommandEvent.QUEUE_LIMIT_CHANGE, onQueueLimitChange);
		}
		
		public override function onRemove():void {
			view.vgraph.removeEventListener(VisualNodeEvent.CLICK, handleNodeClick);
			view.vgraph.removeEventListener(VisualNodeEvent.DOUBLE_CLICK, handleNodeDoubleClick);
			view.vgraph.removeEventListener(VisualNodeEvent.MORE_INFO_REQUESTED, handleNodeInfoRequest);
			view.removeEventListener(ClientKeyboardEvent.KEY_DOWN, handleKeyboardEvent);
			view.removeEventListener(ClientKeyboardEvent.KEY_UP, handleKeyboardEvent);
			
			eventMap.unmapListener(eventDispatcher, GraphChangedEvent.NEW_EVENT, updateView);
			eventMap.unmapListener(eventDispatcher, GraphChangedEvent.NEW_SETTINGS, updateView);
			eventMap.unmapListener(eventDispatcher, GraphChangedEvent.TOGGLE, updateView);
			eventMap.unmapListener(eventDispatcher, GraphChangedEvent.REFRESH, updateView);
			eventMap.unmapListener(eventDispatcher, WatchListCommandEvent.TRACK_ON, onFocusOn);
			eventMap.unmapListener(eventDispatcher, WatchListCommandEvent.TRACK_OFF, onFocusOff);
			eventMap.unmapListener(eventDispatcher, TimelineCommandEvent.SELECTED, onAlertSelect);
			eventMap.unmapListener(eventDispatcher, TimelineCommandEvent.DESELECTED, onAlertDeselect);
			eventMap.unmapListener(eventDispatcher, ApplicationCommandEvent.CLEAR, onClear);
			eventMap.unmapListener(eventDispatcher, CommandEvent.QUEUE_LIMIT_CHANGE, onQueueLimitChange);
		}
		
		protected function onQueueLimitChange( event:CommandEvent ):void{
			dataModel.maxSize = event.parameters.queueLimit;
		}
		
		protected function onClear( event:ApplicationCommandEvent ):void{
			clear();
		}
		
		protected function hideAllChildren( node:Node ):void{
			view.vgraph.silentHideNode(view.vgraph.graph.nodeByStringId(node.key).vnode);
			if(node.isExpanded){
				var children:Object = dataModel.graph.edgesOut[node.key];
				for each( var edge:Edge in children )
					hideAllChildren(edge.to);
			}
		}
		
		protected function handleKeyboardEvent( event:ClientKeyboardEvent ):void{
			dispatch(event);
		}
		
		protected function handleNodeClick( event:VisualNodeEvent ):void {
			dispatch(new GraphClientEvent(GraphClientEvent.CLICK, event.node.stringid));
		}
		
		protected function handleNodeDoubleClick( event:VisualNodeEvent ):void {
			view.vgraph.currentRootVNode = event.node.vnode;
			view.vgraph.draw();
			
			dispatch(new GraphClientEvent(GraphClientEvent.DOUBLE_CLICK, event.node.stringid));
		}
		
		protected function handleNodeInfoRequest( event:VisualNodeEvent ):void {
			var nodeID:String = event.node.stringid;
			var node:Node = dataModel.graph.nodes[nodeID];
			if(!nodeInfoPopups[node.key]){
				var nodeInfo:NodeInfoView = new NodeInfoView();
				nodeInfoPopups[node.key] = nodeInfo;
				nodeInfo.node = node;
				
				nodeInfo.addEventListener(CloseEvent.CLOSE, onCloseNodeInfo);
				
				PopUpManager.addPopUp(nodeInfo, view, false);
			}
		}
		
		protected function onCloseNodeInfo( event:CloseEvent ):void{
			closeInfoPopup((event.target as NodeInfoView).node.key);
		}
		
		protected function closeInfoPopup(key:String):void{
			var view:NodeInfoView = nodeInfoPopups[key];
			if(view){
				delete nodeInfoPopups[key];
				PopUpManager.removePopUp(view);
			}
		}
		
		protected function onFocusOn( event:WatchListCommandEvent ):void{
			dataModel.addFilter(event.parameters['filterKey']);
		}
		
		protected function onFocusOff( event:WatchListCommandEvent ):void{
			dataModel.deleteFilter(event.parameters['filterKey']);
		}
		
		protected function onAlertSelect( event:TimelineCommandEvent ):void{
			if (dataModel.graph == null) {
				return;
			}
			
			var selectionFunction:Function = dataModel.graph.getNodes;
			var selectionParams:Array = 
				[
					event.alert.record,
					function matcher(item:Object):Object{
						return item.record;	
					}
				];
			var nodes:Vector.<Node> = selectionFunction.apply(null, selectionParams);
			for each( var node:Node in nodes )
				selectionParameters[node.key] = [selectionFunction, selectionParams];
		
			highlightNodes(nodes);
		}
		
		protected function onAlertDeselect( event:TimelineCommandEvent ):void{
			for ( var rendererKey:String in selectedSnapshot ){
				selectedSnapshot[rendererKey].unhighlight();
				delete selectedSnapshot[rendererKey];
			}
		}
		
		protected function highlightNodes( nodes:Vector.<Node> ):void{
			onAlertDeselect(null); //deselect everything before we select a new item
			
			for each( var node:Node in nodes ) {
				var gnode:INode = view.vgraph.graph.nodeByStringId(node.key);
				if(gnode){
					var vnode:IVisualNode = gnode.vnode;
					
					//TODO: make highlight a function in IVisualNode
					var workaround:SizeByValueNodeRenderer = (vnode.view as SizeByValueNodeRenderer);
					if(workaround){
						workaround.highlight();
						selectedSnapshot[node.key] = workaround;
					}
				}
			}
		}
		
		public function updateView( event:GraphChangedEvent ):void{			
			//look for differences between the current state of the model and the snapshot of the model
			//add objects that are not in the snapshot and update or delete objects that are
			//this is done to prevent flickering and other issues that arise when simply resetting the
			//view and adding the entire graph back into it
			
			var nodes:Object = dataModel.graph.nodes;
			var curRoot:IVisualNode = view.vgraph.currentRootVNode;
			
			var newNodeSnapshot:Object = new Object();
			var newEdgeSnapshot:Object = new Object();
			var newHighlightSnapshot:Object = dataModel.graph.mostRecentEdges;
			
			var id:String;
			var label:String;
			var nodeData:XML;
			
			//Handles the updating of the nodes
			for(id in nodes){
				var newNode:Node = nodes[id];
				if(newNode.data.length != 0){ //if there is no data in a node, we don't want to add it
					newNodeSnapshot[id] = nodes[id];
					label = newNode.properties.label.value;
					if(nodes[id] != null && nodeSnapshot[id] == null){ //node is not in snapshot, so add it to the view
						if(newNode.data.length != 0){
							//create a view node with the properties of the corresponding node in the model
							nodeData = <Node/>;
							nodeData.@name = label == null ? DEFAULT_NODE_LABEL : label;
							nodeData.@nodeColor = newNode.properties.coloring.value;
							nodeData.@nodeSize = newNode.properties.weight.value;
							
							var visualNode:IVisualNode = view.vgraph.createNode(
													newNode.key, 
													nodeData
												 ); 
							visualNode.isVisible = true;
						}
					}else if(nodes[id] != null && nodeSnapshot[id] != null){ //node is in snapshot, so update it
							var node:IVisualNode = view.vgraph.graph.nodeByStringId(newNode.key).vnode;
							nodeData = <Node/>;
							nodeData.@name = label == null ? DEFAULT_NODE_LABEL : label;
							nodeData.@nodeColor = newNode.properties.coloring.value;
							nodeData.@nodeSize = newNode.properties.weight.value;
							
							//update the properties of the view node with those in the model
							node.data = nodeData;
							
							//deletion from the snapshot indicates a node exists and has been updated
							//any nodes remaining in the snapshot never reached this point, which means they
							//do not exist in the model
							delete nodeSnapshot[id];
					}
				}
			}
			
			var edges:Object = dataModel.graph.edgesOut;
			var fromID:String;
			var toID:String;
			var edgeData:XML;
			var innerEdgeData:XML;
			
			//Handles the updating of the edges
			for(fromID in edges){
				if(nodes[fromID].data.length != 0){ //If a node has no data in it, we don't want to display it or its edges
					newEdgeSnapshot[fromID] = new Object();
					for(toID in edges[fromID]){
						if(nodes[toID].data.length != 0){
							(newEdgeSnapshot[fromID])[toID] = (edges[fromID])[toID];
							if((edges[fromID])[toID] != null && edgeSnapshot[fromID] == null) 
								edgeSnapshot[fromID] = new Object();
							
							var newEdge:Edge = (edges[fromID])[toID];
							var visualedge:IVisualEdge;
							label = newEdge.properties.label.value;
							if(newEdge != null && (edgeSnapshot[fromID])[toID] == null){ //edge is not in snapshot, so add it to the view
								
								visualedge = view.vgraph.linkNodes(
														view.vgraph.graph.nodeByStringId(newEdge.from.key).vnode, 
														view.vgraph.graph.nodeByStringId(newEdge.to.key).vnode
													);
								
								edgeData = <Edge/>;
								edgeData.@edgeLabel = label == null ? DEFAULT_EDGE_LABEL : label;
								edgeData.@color = newEdge.properties.coloring.value
								visualedge.data = edgeData;
								
								innerEdgeData = <Edge/>;
								innerEdgeData.@flow = newEdge.properties.weight.value;
								visualedge.edge.data = innerEdgeData;
							}else if(newEdge != null && (edgeSnapshot[fromID])[toID] != null){ //edge is in snapshot, so update it
								
								visualedge = view.vgraph.graph.getEdge(
															view.vgraph.graph.nodeByStringId(newEdge.from.key),
															view.vgraph.graph.nodeByStringId(newEdge.to.key)
													).vedge;
								
								edgeData = <Edge/>;
								edgeData.@edgeLabel = label == null ? DEFAULT_EDGE_LABEL : label;
								edgeData.@color = newEdge.properties.coloring.value
								visualedge.data = edgeData;
								
								innerEdgeData = <Edge/>;
								innerEdgeData.@flow = newEdge.properties.weight.value;
								visualedge.edge.data = innerEdgeData;
								
								delete (edgeSnapshot[fromID])[toID];
							}
							
							//Highlight any edges that are in the most recent edges list
							if(newHighlightSnapshot != null && newHighlightSnapshot[fromID] != null && (newHighlightSnapshot[fromID])[toID] != null)
								visualedge.data.@color = HIGHLIGHT_COLOR;
						}
					}

					
				}
			}
			
			//the remaining edges in the snapshot are edges in the view that are not in the model, so delete them
			for(fromID in edgeSnapshot){
				for(toID in edgeSnapshot[fromID]){
					var fromNode:IVisualNode = view.vgraph.graph.nodeByStringId(fromID).vnode;
					var toNode:IVisualNode = view.vgraph.graph.nodeByStringId(toID).vnode;
					view.vgraph.unlinkNodes(fromNode,toNode);
				}
			}
			
			//the remaining nodes in the snapshot are nodes in the view that are not in the model, so delete them
			for(id in nodeSnapshot){
				view.vgraph.removeNode(view.vgraph.graph.nodeByStringId(id).vnode);
			}
			
			//set the root node to the stored value because creating a node by default sets it to the root (and we don't want that)
			if(curRoot != null && view.vgraph.graph.nodeById(curRoot.id) != null)
				view.vgraph.currentRootVNode = curRoot; 
			
			//check selected nodes to see if they have been expanded or collapsed, and update the selection if they have
			for(id in selectedSnapshot){
				var selectedNode:Node = dataModel.graph.nodes[id];
				if(selectedNode == null || selectedNode.isExpanded){
					var arr:Array = selectionParameters[id];
					delete selectionParameters[id];
					
					var selectedNodes:Vector.<Node> = arr[0].apply(null, arr[1]);
					for each( var n:Node in selectedNodes )
						selectionParameters[n.key] = arr;
					
					highlightNodes(selectedNodes);
				}
			}
			
			//set the snapshot to the new set of nodes which
			//now accurately represent the model
			nodeSnapshot = newNodeSnapshot;
			
			//set the snapshot to the new set of edges which
			//now accurately represent the model
			edgeSnapshot = newEdgeSnapshot;
			
			//set the snapshot to the new highlighted edges
			highlightSnapshot = newHighlightSnapshot;
			
			view.vgraph.draw();
		}

		private function clear():void {
			dataModel.clear();
			
			viewIDToModelNodeID = new Object();
			viewIDToModelEdgeID = new Object();
			
			modelIDToNodeViewID = new Object();
			modelIDToEdgeViewID = new Object();
			
			nodeSnapshot = new Object();
			edgeSnapshot = new Object();	
			highlightSnapshot = new Object();
			
			selectedSnapshot = new Object();
			selectionParameters = new Object();
			
			for(var key:String in nodeInfoPopups)
				closeInfoPopup(key);
			nodeInfoPopups = new Object();
			
			view.vgraph.graph = new Graph("ExpandGraph", true);
			view.vgraph.draw();
		}
		
	}
}