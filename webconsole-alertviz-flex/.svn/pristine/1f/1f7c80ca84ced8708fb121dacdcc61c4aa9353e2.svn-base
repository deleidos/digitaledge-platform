package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.CommandEvent;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	
	import flash.events.MouseEvent;
	
	import mx.events.CloseEvent;
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;
	
	/**
	 * The mediator for the settings popup view.
	 */
	public class SettingsPopUpViewMediator extends Mediator
	{
		[Inject]
		public var view:SettingsPopUpView;
		
		[Inject]
		public var paramModel:ParameterModel;
		
		/**
		 * Called by the robotleg framework when this mediator is registered.
		 */
		public override function onRegister():void 
		{
			view.updateButton.addEventListener(MouseEvent.CLICK, handleUpdateButton);
			view.cancelButton.addEventListener(MouseEvent.CLICK, handleCancelButton);
			view.closeButton.addEventListener(MouseEvent.CLICK, handleCancelButton);
			
			// Fill in the input fields in the pop up window
			
			view.alertQueueLimitTextInput.text = String(paramModel.alertQueueLimit);
			view.timelineAutopanCheckBox.selected = paramModel.timelineAutopan;
		}
		
		/**
		 * This method gets called when the mediator gets removed from the robotleg framework.
		 */ 
		public override function onRemove():void 
		{
			view.updateButton.removeEventListener(MouseEvent.CLICK, handleUpdateButton);
			view.cancelButton.removeEventListener(MouseEvent.CLICK, handleCancelButton);
		}
		
		/**
		 * This event handler is called with the user clicks the update button. This handler
		 * will fire off two events, one to notify the other views that the queue limit
		 * has changed and the other to notify the other views a view format was selected.
		 */ 
		protected function handleUpdateButton(event:MouseEvent):void 
		{
			paramModel.alertQueueLimit = parseInt(view.alertQueueLimitTextInput.text);
			paramModel.timelineAutopan = view.timelineAutopanCheckBox.selected;
			
			dispatch(new CommandEvent(CommandEvent.QUEUE_LIMIT_CHANGE, {queueLimit:paramModel.alertQueueLimit}));
			
			closeView();
		}
		
		/**
		 * This event handler is called when the user clicks the cancel button.The handler will
		 * close the settings popup window.
		 */ 
		protected function handleCancelButton(event:MouseEvent):void 
		{
			closeView();
		}
		
		/**
		 * Closes the settings popup window.
		 */ 
		private function closeView():void
		{
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
	}
}