package com.deleidos.rtws.alertviz.expandgraph.commands
{
	import com.deleidos.rtws.alertviz.expandgraph.events.GraphConfigEvent;
	import com.deleidos.rtws.alertviz.expandgraph.services.IGraphConfigService;
	
	import org.robotlegs.mvcs.Command;

	public class GraphConfigCommand extends Command
	{
		
		[Inject]
		public var event:GraphConfigEvent;
		
		[Inject]
		public var graphConfigService:IGraphConfigService;
		
		override public function execute():void{
			if (event.type == GraphConfigEvent.GET_LOCATIONS)
				graphConfigService.getLocations();
			else{
				var xml:XML = event.parameters.xml as XML;
				var location:String = event.parameters.location as String;
				if (event.type == GraphConfigEvent.GET_CONFIG) 
					graphConfigService.getConfigFile(location);
				
				else if (event.type == GraphConfigEvent.SAVE) 
					graphConfigService.saveConfigFile(xml, location);
				
				else if (event.type == GraphConfigEvent.CREATE_FOLDER) 
					graphConfigService.createFolder(location);
				
				else if (event.type == GraphConfigEvent.UPDATE)				
					graphConfigService.updateConfigFile(xml, location);
				
				else if (event.type == GraphConfigEvent.REMOVE)
					graphConfigService.deleteConfigFile(location);
				
				else if (event.type == GraphConfigEvent.MOVE)
					graphConfigService.moveConfigFiles(event.parameters.oldLocation, event.parameters.newLocation);
				
				else if (event.type == GraphConfigEvent.RENAME)
					graphConfigService.renameConfigFile(event.parameters.oldLocation, event.parameters.newName);
			}
		}
	}
}