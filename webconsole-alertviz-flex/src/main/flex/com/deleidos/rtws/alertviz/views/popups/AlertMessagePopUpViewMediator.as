package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	
	import flash.events.MouseEvent;
	
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class AlertMessagePopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:AlertMessagePopUpView;
		
		public override function onRegister():void 
		{
			view.okButton.addEventListener(MouseEvent.CLICK, handleCloseButton);
			view.closeButton.addEventListener(MouseEvent.CLICK, handleCloseButton);
			
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.ALERT_MESSAGE, handleAlertMessage);
		}
		
		public override function onRemove():void 
		{
			view.okButton.removeEventListener(MouseEvent.CLICK, handleCloseButton);
			view.closeButton.removeEventListener(MouseEvent.CLICK, handleCloseButton);
		}
		
		
		protected function handleCloseButton(event:MouseEvent):void 
		{
			closeView();
		}
		
		protected function handleAlertMessage(event:AlertCriteriaCommandEvent):void
		{
			var title:String = event.parameters.title;
			var message:String = event.parameters.message;
			
			if(title != null)
			{
				view.title = title;
			}
			
			if(message != null)
			{
				view.message.text = message;
			}
		}
		
		private function closeView():void 
		{
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
	}
}