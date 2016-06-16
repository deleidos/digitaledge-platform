package com.deleidos.rtws.alertviz.expandgraph.commands
{
	import org.robotlegs.mvcs.Command;
	import org.un.cava.birdeye.ravis.graphLayout.visual.events.VisualNodeEvent;
	import com.deleidos.rtws.alertviz.expandgraph.events.GraphClientEvent;
	import com.deleidos.rtws.alertviz.expandgraph.models.GraphModel;
	
	public class GraphClientCommand extends Command
	{
		[Inject]
		public var event:GraphClientEvent;
		
		[Inject]
		public var model:GraphModel;
		
		override public function execute():void{
			if(event.type == GraphClientEvent.CLICK){
				model.toggle(event.node);
			}
		}
	}
}