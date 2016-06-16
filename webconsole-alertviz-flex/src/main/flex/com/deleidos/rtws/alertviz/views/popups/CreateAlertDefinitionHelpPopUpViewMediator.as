package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.events.HTTPQueueEvent;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaEntry;
	import com.deleidos.rtws.alertviz.models.HTTPQueueModel;
	import com.deleidos.rtws.alertviz.models.HTTPRequest;
	
	import flash.events.MouseEvent;
	
	import mx.collections.ArrayList;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.validators.Validator;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class CreateAlertDefinitionHelpPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:CreateAlertDefinitionHelpPopUpView;
				
		public override function onRegister():void 
		{
			view.cancelButton.addEventListener(MouseEvent.CLICK, handleCloseButton);
			view.closeButton.addEventListener(MouseEvent.CLICK, handleCloseButton);
			
			//inserts the instructions for using the "@" symbol
			view.fieldLabel.text = "Protip: Using ' @ ' in the value input will allow you to compare two fields!";
		}
		
		public override function onRemove():void 
		{
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleCloseButton);
			view.closeButton.removeEventListener(MouseEvent.CLICK, handleCloseButton);
		}
		
		
		protected function handleCloseButton(event:MouseEvent):void 
		{
			closeView();
		}
		
		private function closeView():void 
		{
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
	}
}