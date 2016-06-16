package com.deleidos.rtws.alertviz.views
{	
	import com.deleidos.rtws.alertviz.events.ApplicationCommandEvent;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	import com.deleidos.rtws.alertviz.views.popups.InfoPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.SettingsPopUpView;
	
	import flash.events.MouseEvent;
	import flash.net.URLRequest;
	import flash.net.navigateToURL;
	
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;

	public class MenuViewMediator extends Mediator
	{
		[Inject]
		public var view:MenuView;
		
		[Inject]
		public var paramModel:ParameterModel;
		
		public override function onRegister():void 
		{
			view.clearButton.addEventListener(MouseEvent.CLICK, handleClearButtonEvent);
			view.settingsButton.addEventListener(MouseEvent.CLICK, handleSettingsButtonEvent);
			view.infoButton.addEventListener(MouseEvent.CLICK, handleInfoButtonEvent);
		}
		
		protected function handleClearButtonEvent(event:MouseEvent):void
		{
			dispatch(new ApplicationCommandEvent(ApplicationCommandEvent.CLEAR, null));
		}
		
		protected function handleSettingsButtonEvent(event:MouseEvent):void
		{
			var popup:SettingsPopUpView = new SettingsPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
		}
		
		protected function handleInfoButtonEvent(event:MouseEvent):void
		{
			var popup:InfoPopUpView = new InfoPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
		}
	}
}