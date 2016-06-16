package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaEntry;
	
	import flash.events.MouseEvent;
	
	import mx.controls.Alert;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.validators.Validator;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class UpdateAlertCriteriaPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:UpdateAlertCriteriaPopUpView;
		
		public override function onRegister():void 
		{
			view.updateButton.addEventListener(MouseEvent.CLICK, handleUpdateButton);
			view.cancelButton.addEventListener(MouseEvent.CLICK, handleCancelButton);

			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.UPDATE_SUCCESS, handleSuccess);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.UPDATE_ERROR, handleError);
		}
		
		public override function onRemove():void 
		{
			view.updateButton.removeEventListener(MouseEvent.CLICK, handleUpdateButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleCancelButton);
			
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.UPDATE_SUCCESS, handleSuccess);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.UPDATE_ERROR, handleError);
		}
		
		protected function handleSuccess(event:AlertCriteriaCommandEvent):void
		{
			closeView();
		}
		
		protected function handleError(event:AlertCriteriaCommandEvent):void
		{
			view.updateButton.enabled = true;
			
			var popup:AlertMessagePopUpView = new AlertMessagePopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
			
			
			var title:String = "HTTP Service Error";
			var message:String = "Unable to update alert criteria";
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.ALERT_MESSAGE, {title:title, message:message}));
		}
		
		protected function handleUpdateButton(event:MouseEvent):void 
		{
			var validatorErrorArr:Array = Validator.validateAll(view.validators);
			var isFormValid:Boolean = (validatorErrorArr.length == 0);
			
			if (isFormValid)
			{
				var entry:AlertCriteriaEntry = new AlertCriteriaEntry(view.alertKeyTextInput.text, view.alertNameTextInput.text, 
					view.alertModelTextInput.text, view.alertDescTextInput.text, view.alertDefTextArea.text);
			
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.UPDATE, {entry:entry}));
			
				view.updateButton.enabled = false;
			}
		}
		
		protected function handleCancelButton(event:MouseEvent):void 
		{
			closeView();
		}
		
		private function closeView():void {
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
	}
}