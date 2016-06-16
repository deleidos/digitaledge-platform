package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.events.HTTPQueueEvent;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaEntry;
	import com.deleidos.rtws.alertviz.models.HTTPQueueModel;
	import com.deleidos.rtws.alertviz.models.HTTPRequest;
	
	import flash.events.MouseEvent;
	import flash.globalization.DateTimeFormatter;
	
	import mx.collections.ArrayList;
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	import mx.validators.Validator;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class CreateAlertValueSuggestionPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:CreateAlertValueSuggestionPopUpView;
		
		//error message
		private var message:String = "The date that you entered does not appear to be in the correct format.\n\n" +
			"Would you like to change the date to:\n";
		
		//the suggested date to use
		private var newVal:String = "";
		
		public override function onRegister():void 
		{
			view.noButton.addEventListener(MouseEvent.CLICK, handleNoButton);
			view.closeButton.addEventListener(MouseEvent.CLICK, handleNoButton);
			view.yesButton.addEventListener(MouseEvent.CLICK, handleYesButton);
			view.ignoreButton.addEventListener(MouseEvent.CLICK, handleIgnoreButton);
			
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_VAL, handleSendVal);

		}
		
		public override function onRemove():void 
		{
			view.ignoreButton.removeEventListener(MouseEvent.CLICK, handleIgnoreButton);
			view.noButton.removeEventListener(MouseEvent.CLICK, handleYesButton);
			view.noButton.removeEventListener(MouseEvent.CLICK, handleNoButton);
			
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.SEND_VAL, handleSendVal);
		}
		
		
		protected function handleNoButton(event:MouseEvent):void 
		{
			closeView();
		}
		
		/**
		 * handles when the user decides to use the suggested date
		 */
		protected function handleYesButton(event:MouseEvent):void
		{
			//sends the suggested date
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.RETURN_VAL, newVal));
			closeView();
		}
		
		/**
		 * handles when the user decides to ignore the suggestion
		 */
		protected function handleIgnoreButton(event:MouseEvent):void
		{
			//send the ignore event
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.IGNORE, null));
			closeView();
		}
		
		/**
		 * handles when data is received. sets the suggestion date
		 */
		protected function handleSendVal(event:AlertCriteriaCommandEvent):void
		{
			var formatter:DateTimeFormatter = event.parameters.formatter;
			
			//tries to format the date the user entered
			newVal = formatter.format(new Date(event.parameters.expression));
			
			//if the date the user entered could not be formatted
			if(newVal == "")
			{
				//format the current date and time
				newVal = formatter.format(new Date());
			}
			
			//display error message and suggestion in the window
			view.suggestionArea.text = message + newVal;
		}
		
		private function closeView():void 
		{
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
	}
}