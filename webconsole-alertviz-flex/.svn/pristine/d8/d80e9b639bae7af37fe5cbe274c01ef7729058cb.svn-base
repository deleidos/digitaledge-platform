package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.CommandEvent;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	
	import flash.events.MouseEvent;
	
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class InfoPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:InfoPopUpView;
		
		[Inject]
		public var paramModel:ParameterModel;
		
		public override function onRegister():void 
		{
			view.okButton.addEventListener(MouseEvent.CLICK, handleOkButton);
			view.closeButton.addEventListener(MouseEvent.CLICK, handleOkButton);
			
			// Fill in the input fields in the pop up window
			
			view.usernameLabel.text = paramModel.username;
			view.systemNameLabel.text = paramModel.systemName;
		}
		
		public override function onRemove():void 
		{
			view.okButton.removeEventListener(MouseEvent.CLICK, handleOkButton);
		}
		
		protected function handleOkButton(event:MouseEvent):void 
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