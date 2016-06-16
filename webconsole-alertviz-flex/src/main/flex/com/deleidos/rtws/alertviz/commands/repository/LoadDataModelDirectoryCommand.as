package com.deleidos.rtws.alertviz.commands.repository
{
	import com.deleidos.rtws.alertviz.events.repository.ModelServiceEvent;
	import com.deleidos.rtws.alertviz.services.repository.IDataModelServices;
	
	import org.robotlegs.mvcs.Command;
	
	public class LoadDataModelDirectoryCommand extends Command
	{
		[Inject]
		public var event : ModelServiceEvent;
		
		[Inject]
		public var service : IDataModelServices;
		
		override public function execute():void
		{
			var dirParams:Object = event.parameters;
			// dirParams.tenant
			// dirParams.visibility
			service.loadModelDirectory(dirParams.visibility);
		}
	}
}