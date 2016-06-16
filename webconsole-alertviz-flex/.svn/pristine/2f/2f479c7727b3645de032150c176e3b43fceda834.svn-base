package com.deleidos.rtws.alertviz.expandgraph.commands
{
	import com.deleidos.rtws.alertviz.expandgraph.graph.IPGraphSettings;
	
	import org.robotlegs.base.ContextEvent;
	import org.robotlegs.mvcs.Command;
	import com.deleidos.rtws.alertviz.expandgraph.services.CyberTestService;
	import com.deleidos.rtws.alertviz.expandgraph.models.GraphModel;

	public class GraphStartupCommand extends Command
	{
		[Inject]
		public var event:ContextEvent;
		
		[Inject]
		public var model:GraphModel;
		
		//[Inject]
		//public var service:CyberTestService;
		
		override public function execute():void{
			if(event.type == ContextEvent.STARTUP_COMPLETE){
				//Loads the default network graph settings
				//model.graphSettings = new IPGraphSettings();
				
				//Runs the data simulator
				//service.load();
			}
		}
	}
}