package com.deleidos.rtws.alertviz.expandgraph.commands
{
	import com.adobe.serialization.json.JSON;
	import com.deleidos.rtws.alertviz.events.AlertCommandEvent;
	import com.deleidos.rtws.alertviz.expandgraph.models.GraphModel;
	
	import org.robotlegs.mvcs.Command;

	public class GraphAlertCommand extends Command
	{
		[Inject]
		public var event:AlertCommandEvent;
			
		[Inject]
		public var model:GraphModel;

		override public function execute():void{
			model.add(event.alert);
		}
		
	}
}