package com.deleidos.rtws.alertviz.commands.repository
{
	import com.deleidos.rtws.alertviz.events.repository.ModelServiceEvent;
	import com.deleidos.rtws.alertviz.models.repository.DataModelDirectory;
	import com.deleidos.rtws.alertviz.models.repository.DataModelName;
	import com.deleidos.rtws.alertviz.services.repository.IDataModelServices;
	
	import org.robotlegs.mvcs.Command;
	
	public class LoadModelCommand extends Command
	{
		[Inject]
		public var event : ModelServiceEvent;
		
		[Inject]
		public var dmService : IDataModelServices;
		
		override public function execute():void
		{
			var loadParams:Object = event.parameters;
			// loadParams.dataModelName
			// loadParams.tenant
			dmService.load(loadParams.dataModelName);
		}
	}
}
