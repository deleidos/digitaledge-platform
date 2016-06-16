package com.deleidos.rtws.alertviz.views.popups
{	
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.events.CommandEvent;
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	import com.deleidos.rtws.alertviz.events.repository.ModelServiceEvent;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	import com.deleidos.rtws.alertviz.services.repository.RepositoryConstants;
	import com.deleidos.rtws.commons.model.auth.IUser;
	import com.deleidos.rtws.commons.tenantutils.model.properties.SystemAppPropertiesModel;
	
	import flash.events.Event;
	
	import mx.managers.PopUpManager;
	
	import org.robotlegs.mvcs.Mediator;
	
	public class InitPopUpViewMediator extends Mediator
	{
		[Inject] public var appParams:ParameterModel;
		
		[Inject] public var propsModel:SystemAppPropertiesModel;
		
		[Inject] public var userModel:IUser;
		
		[Inject] public var view:InitPopUpView;
		
		/** Stores a list of events we want to fire sequentially (order matters). */
		private var queue:Array = new Array();
		
		/** Stores a list of events we want to fire before the view is close. */
		private var closeQ:Array = new Array();
		
		private var maxRetries:int = 3;
		
		private var retrieveAlertCriteriaAttempts:int = 0;
		
		private var retrieveWatchListAttempts:int = 0;
		
		private var retrieveModelDirAttempts:int = 0;
		
		public override function onRegister():void 
		{
			// Initialize legacy models with data from common services/model
			appParams.username = userModel.username;
			appParams.tenantName = propsModel.tenantId;
			appParams.systemName = propsModel.systemFqdn;
			
			
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.REFRESH_SUCCESS, handleSuccess);
			eventMap.mapListener(eventDispatcher, AlertCriteriaCommandEvent.REFRESH_ERROR, handleError);
			
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.REFRESH_SUCCESS, handleSuccess);
			eventMap.mapListener(eventDispatcher, WatchListCommandEvent.REFERSH_ERROR, handleError);	
			
			eventMap.mapListener(eventDispatcher, ModelServiceEvent.MODEL_DIR_LOADED, handleSuccess);
			eventMap.mapListener(eventDispatcher, ModelServiceEvent.MODEL_DIR_LOAD_FAIL, handleError);
			
			queue.push(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.REFRESH, null));
			queue.push(new WatchListCommandEvent(WatchListCommandEvent.REFRESH, null));
			queue.push(new ModelServiceEvent(ModelServiceEvent.LOAD_MODEL_DIR, {tenant:null, visibility:RepositoryConstants.PUBLIC}, null));
			
			closeQ.push(new CommandEvent(CommandEvent.QUEUE_LIMIT_CHANGE, {queueLimit:appParams.alertQueueLimit}));
			
			dequeueAndFire();
		}
		
		public override function onRemove():void 
		{
			eventMap.unmapListener(eventDispatcher, WatchListCommandEvent.REFERSH_ERROR, handleError);
			eventMap.unmapListener(eventDispatcher, WatchListCommandEvent.REFRESH_SUCCESS, handleSuccess);
			
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.REFRESH_ERROR, handleError);
			eventMap.unmapListener(eventDispatcher, AlertCriteriaCommandEvent.REFRESH_SUCCESS, handleSuccess);
			
			eventMap.unmapListener(eventDispatcher, ModelServiceEvent.MODEL_DIR_LOADED, handleSuccess);
			eventMap.unmapListener(eventDispatcher, ModelServiceEvent.MODEL_DIR_LOAD_FAIL, handleError);
		}
		
		protected function dequeueAndFire():void 
		{
			if (queue.length > 0) {
				var event:Event = queue.shift();
				dispatch(event);
			} else {
				closeView();
			}
		}
		
		protected function handleSuccess(event:Event):void 
		{
			dequeueAndFire();
		}
		
		protected function handleError(event:Event):void 
		{
			if (event is AlertCriteriaCommandEvent) {
				if (retrieveAlertCriteriaAttempts < maxRetries) {
					queue.push(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.REFRESH, null));
					retrieveAlertCriteriaAttempts++;
				}
			}
			else if (event is WatchListCommandEvent) {
				if (retrieveWatchListAttempts < maxRetries) {
					queue.push(new WatchListCommandEvent(WatchListCommandEvent.REFRESH, null));
					retrieveWatchListAttempts++;
				}
			}else if (event is ModelServiceEvent) {
				if (retrieveModelDirAttempts < maxRetries) {
					queue.push(new ModelServiceEvent(ModelServiceEvent.LOAD_MODEL_DIR, {tenant:null, visibility:RepositoryConstants.PUBLIC}, null));
					retrieveModelDirAttempts++;
				}
			}
			
			dequeueAndFire();
		}
		
		private function closeView():void 
		{
			while (closeQ.length > 0) {
				var event:Event = closeQ.pop();
				dispatch(event);
			}
			
			PopUpManager.removePopUp(view);
			mediatorMap.removeMediator(this);
		}
	}
}