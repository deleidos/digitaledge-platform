package com.deleidos.rtws.alertviz.commands
{
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	import com.deleidos.rtws.alertviz.views.popups.InitPopUpView;
	import com.deleidos.rtws.commons.command.theme.ThemeOpRequestEvent;
	import com.deleidos.rtws.commons.command.user.UserOpRequestEvent;
	import com.deleidos.rtws.commons.event.OperationStatusEvent;
	import com.deleidos.rtws.commons.event.properties.PropertiesEvent;
	import com.deleidos.rtws.commons.model.theme.RtwsThemes;
	import com.deleidos.rtws.commons.service.theme.ThemeServiceStatusEvent;
	import com.deleidos.rtws.commons.service.user.UserServiceStatusEvent;
	import com.deleidos.rtws.commons.util.batchoperation.BatchOperationController;
	import com.deleidos.rtws.commons.util.batchoperation.BatchOperationStatusEvent;
	import com.deleidos.rtws.commons.util.batchoperation.config.DefaultOperationConfig;
	import com.deleidos.rtws.commons.util.batchoperation.monitor.TrackableOperationMonitor;
	
	import flash.system.Security;
	
	import mx.collections.ArrayCollection;
	import mx.core.FlexGlobals;
	import mx.managers.PopUpManager;
	import mx.utils.UIDUtil;
	
	import org.robotlegs.mvcs.Command;
	
	/**
	 * This is the application startup command. A particular event
	 * can be mapped to this command in the context file and when
	 * the event.
	 */ 
	public class StartupCommand extends Command
	{	
		[Inject] public var paramModel:ParameterModel;
		
		private var _startupController:BatchOperationController;
		
		/**
		 * This method gets called when the context event startup complete
		 * event is dispatched.
		 */ 
		public override function execute():void 
		{
			// Set the default application properties
			paramModel.alertQueueLimit = 250;
			
			// Extract the alertviz application url.
			var url:String = FlexGlobals.topLevelApplication.url;
			var urlTokens:Array = url.split("/");
			paramModel.selfPrefixUrl = url.replace(urlTokens[urlTokens.length - 1], "");
			
			this.commandMap.detain(this);
			
			var currOpTrackingToken:String = null;
			
			var startupSyncOpConfigs:ArrayCollection = new ArrayCollection();
			
			// Load Properties
			currOpTrackingToken = UIDUtil.createUID();
			var loadPropsConfig:DefaultOperationConfig =
				new DefaultOperationConfig(
					new PropertiesEvent(PropertiesEvent.FETCH_REMOTE_PROPS_REQUEST, null, currOpTrackingToken),
					new TrackableOperationMonitor(eventDispatcher, PropertiesEvent.FETCH_REMOTE_PROPS_STATUS, currOpTrackingToken),
					15000, 2, true);
			startupSyncOpConfigs.addItem(loadPropsConfig);
			
			// Load user data
			currOpTrackingToken = UIDUtil.createUID();
			var loadUserConfig:DefaultOperationConfig =
				new DefaultOperationConfig(
					new UserOpRequestEvent(UserOpRequestEvent.LOAD_USER_INFO, currOpTrackingToken),
					new TrackableOperationMonitor(eventDispatcher, UserServiceStatusEvent.STATUS_UPDATE, currOpTrackingToken),
					15000, 2, true);
			startupSyncOpConfigs.addItem(loadUserConfig);
			
			var startupAsyncOpConfigs:ArrayCollection = new ArrayCollection();
			var asyncLoadConfig:DefaultOperationConfig;
			
			// Load the theme
			currOpTrackingToken = UIDUtil.createUID();
			asyncLoadConfig =
				new DefaultOperationConfig(
					new ThemeOpRequestEvent(ThemeOpRequestEvent.LOAD_THEME, RtwsThemes.SAIC_BLUE, null, currOpTrackingToken),
					new TrackableOperationMonitor(eventDispatcher, ThemeServiceStatusEvent.THEME_SERVICE_STATUS_EVENT_TYPE, currOpTrackingToken));
			setAsyncOpParams(asyncLoadConfig);
			startupAsyncOpConfigs.addItem(asyncLoadConfig);
			
			_startupController = new BatchOperationController(eventDispatcher, startupSyncOpConfigs, startupAsyncOpConfigs, AlertViz.STARTUP_TRACKING_TOKEN);
			eventDispatcher.addEventListener(BatchOperationStatusEvent.BATCH_OPERATION, onStartupStatusEvent);
			_startupController.start();
		}
		
		/**
		 * Launch a popup that goes and initialize the application.
		 */ 
		private function performLegacyInitialization():void {
			
			var popup:InitPopUpView = new InitPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
		}
		
		private function setAsyncOpParams(opConfig:DefaultOperationConfig):void 
		{
			opConfig.timeoutMillis = 15000;
			opConfig.maxAttempts = 2;
			opConfig.failBatchOnOpFailure = true;
		}
		
		private function onStartupStatusEvent(event:BatchOperationStatusEvent):void
		{
			if(event != null && AlertViz.STARTUP_TRACKING_TOKEN === event.trackingToken && event.isComplete()) 
			{
				eventDispatcher.removeEventListener(BatchOperationStatusEvent.BATCH_OPERATION, onStartupStatusEvent);
				
				if(event.subType == OperationStatusEvent.SUB_TYPE_SUCCESS) {
					performLegacyInitialization();
				}
				
				commandMap.release(this);
			}
		}
	}
}