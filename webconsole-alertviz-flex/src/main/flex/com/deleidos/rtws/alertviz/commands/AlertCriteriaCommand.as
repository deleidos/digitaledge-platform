package com.deleidos.rtws.alertviz.commands
{
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaEntry;
	import com.deleidos.rtws.alertviz.services.IAlertCriteriaService;
	
	import org.robotlegs.mvcs.Command;

	/**
	 * This class contains the logic to handle alert criteria command events.
	 */ 
	public class AlertCriteriaCommand extends Command
	{
		[Inject]
		public var event:AlertCriteriaCommandEvent;
		
		[Inject]
		public var alertCriteriaService:IAlertCriteriaService;
		
		/**
		 * This method is executed when a alert criteria command event is dispatched.
		 */
		public override function execute():void 
		{
			if (event.type == AlertCriteriaCommandEvent.REFRESH) 
			{
				alertCriteriaService.retrieveAlertCriterias();
			}
			
			if (event.type == AlertCriteriaCommandEvent.CREATE) 
			{
				var createEntry:AlertCriteriaEntry = event.parameters.entry;
				
				alertCriteriaService.createAlertCriteria(
					createEntry.alertDefName, createEntry.alertDefModel,
					createEntry.alertDefDesc, createEntry.alertDef
				);
			}
			
			if (event.type == AlertCriteriaCommandEvent.UPDATE) 
			{
				var updateEntry:AlertCriteriaEntry = event.parameters.entry;
				
				alertCriteriaService.updateAlertCriteria(
					updateEntry.alertDefKey, updateEntry.alertDefName, 
					updateEntry.alertDefModel, updateEntry.alertDefDesc, 
					updateEntry.alertDef
				);
			}
			
			if (event.type == AlertCriteriaCommandEvent.REMOVE) 
			{
				alertCriteriaService.deleteAlertCriteria(event.parameters.key);
			}
		}
	}
}