package com.deleidos.rtws.alertviz
{
	import com.deleidos.rtws.alertviz.commands.AlertCommand;
	import com.deleidos.rtws.alertviz.commands.AlertCriteriaCommand;
	import com.deleidos.rtws.alertviz.commands.ApplicationCommand;
	import com.deleidos.rtws.alertviz.commands.HTTPQueueCommand;
	import com.deleidos.rtws.alertviz.commands.StartupCommand;
	import com.deleidos.rtws.alertviz.commands.WatchListCommand;
	import com.deleidos.rtws.alertviz.commands.repository.LoadDataModelDirectoryCommand;
	import com.deleidos.rtws.alertviz.commands.repository.LoadModelCommand;
	import com.deleidos.rtws.alertviz.events.AlertCommandEvent;
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.events.ApplicationCommandEvent;
	import com.deleidos.rtws.alertviz.events.HTTPQueueEvent;
	import com.deleidos.rtws.alertviz.events.WatchListCommandEvent;
	import com.deleidos.rtws.alertviz.events.repository.ModelServiceEvent;
	import com.deleidos.rtws.alertviz.expandgraph.commands.ClientKeyboardCommand;
	import com.deleidos.rtws.alertviz.expandgraph.commands.GraphAlertCommand;
	import com.deleidos.rtws.alertviz.expandgraph.commands.GraphClientCommand;
	import com.deleidos.rtws.alertviz.expandgraph.commands.GraphConfigCommand;
	import com.deleidos.rtws.alertviz.expandgraph.events.ClientKeyboardEvent;
	import com.deleidos.rtws.alertviz.expandgraph.events.GraphClientEvent;
	import com.deleidos.rtws.alertviz.expandgraph.events.GraphConfigEvent;
	import com.deleidos.rtws.alertviz.expandgraph.models.GraphConfigModel;
	import com.deleidos.rtws.alertviz.expandgraph.models.GraphModel;
	import com.deleidos.rtws.alertviz.expandgraph.services.GraphConfigService;
	import com.deleidos.rtws.alertviz.expandgraph.services.IGraphConfigService;
	import com.deleidos.rtws.alertviz.expandgraph.views.GraphDataMediator;
	import com.deleidos.rtws.alertviz.expandgraph.views.GraphDataView;
	import com.deleidos.rtws.alertviz.expandgraph.views.GraphDesignView;
	import com.deleidos.rtws.alertviz.expandgraph.views.GraphDesignViewMediator;
	import com.deleidos.rtws.alertviz.expandgraph.views.GraphMediator;
	import com.deleidos.rtws.alertviz.expandgraph.views.GraphView;
	import com.deleidos.rtws.alertviz.googlemap.MapView;
	import com.deleidos.rtws.alertviz.googlemap.MapViewMediator;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaModel;
	import com.deleidos.rtws.alertviz.models.AlertDataModel;
	import com.deleidos.rtws.alertviz.models.AlertVizPropertiesModel;
	import com.deleidos.rtws.alertviz.models.HTTPQueueModel;
	import com.deleidos.rtws.alertviz.models.ParameterModel;
	import com.deleidos.rtws.alertviz.models.TimelineModel;
	import com.deleidos.rtws.alertviz.models.WatchListModel;
	import com.deleidos.rtws.alertviz.models.repository.DataModelDirectory;
	import com.deleidos.rtws.alertviz.models.repository.ModelFieldData;
	import com.deleidos.rtws.alertviz.services.IAlertCriteriaService;
	import com.deleidos.rtws.alertviz.services.IWatchListService;
	import com.deleidos.rtws.alertviz.services.repository.IDataModelServices;
	import com.deleidos.rtws.alertviz.services.repository.rest.DataModelServices;
	import com.deleidos.rtws.alertviz.services.rest.AlertCriteriaService;
	import com.deleidos.rtws.alertviz.services.rest.WatchListService;
	import com.deleidos.rtws.alertviz.views.AlertCriteriaView;
	import com.deleidos.rtws.alertviz.views.AlertCriteriaViewMediator;
	import com.deleidos.rtws.alertviz.views.AlertDataView;
	import com.deleidos.rtws.alertviz.views.AlertDataViewMediator;
	import com.deleidos.rtws.alertviz.views.AlertVizMediator;
	import com.deleidos.rtws.alertviz.views.AppLaunchStatusDisplay;
	import com.deleidos.rtws.alertviz.views.AppLaunchStatusDisplayMediator;
	import com.deleidos.rtws.alertviz.views.MenuView;
	import com.deleidos.rtws.alertviz.views.MenuViewMediator;
	import com.deleidos.rtws.alertviz.views.TimelineView;
	import com.deleidos.rtws.alertviz.views.TimelineViewMediator;
	import com.deleidos.rtws.alertviz.views.VisualizationView;
	import com.deleidos.rtws.alertviz.views.WatchListView;
	import com.deleidos.rtws.alertviz.views.WatchListViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.AlertMessagePopUpView;
	import com.deleidos.rtws.alertviz.views.popups.AlertMessagePopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.BrowseDataModelPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.BrowseDataModelPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.CreateAlertCriteriaPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.CreateAlertCriteriaPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.CreateAlertDefinitionHelpPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.CreateAlertDefinitionHelpPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.CreateAlertDefinitionPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.CreateAlertDefinitionPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.CreateAlertValueSuggestionPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.CreateAlertValueSuggestionPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.CreateWatchListEntryPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.CreateWatchListEntryPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.DeleteAlertCriteriaPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.DeleteAlertCriteriaPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.DeleteWatchListEntryPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.DeleteWatchListEntryPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.FormatHelpPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.FormatHelpPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.InfoPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.InfoPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.InitPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.InitPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.RefreshAlertCriteriaPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.RefreshAlertCriteriaPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.SettingsPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.SettingsPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.TestWildCardPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.TestWildCardPopUpViewMediator;
	import com.deleidos.rtws.alertviz.views.popups.UpdateAlertCriteriaPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.UpdateAlertCriteriaPopUpViewMediator;
	import com.deleidos.rtws.commons.command.properties.LoadRemotePropsIntoModelCommand;
	import com.deleidos.rtws.commons.command.theme.ThemeOpRequestCommand;
	import com.deleidos.rtws.commons.command.theme.ThemeOpRequestEvent;
	import com.deleidos.rtws.commons.command.user.UserOpRequestCommand;
	import com.deleidos.rtws.commons.command.user.UserOpRequestEvent;
	import com.deleidos.rtws.commons.event.properties.PropertiesEvent;
	import com.deleidos.rtws.commons.model.auth.IUser;
	import com.deleidos.rtws.commons.model.properties.IAppPropertiesModel;
	import com.deleidos.rtws.commons.service.properties.IPropertiesService;
	import com.deleidos.rtws.commons.service.properties.PropertiesService;
	import com.deleidos.rtws.commons.service.theme.IThemeService;
	import com.deleidos.rtws.commons.service.theme.ThemeService;
	import com.deleidos.rtws.commons.service.user.IUserServices;
	import com.deleidos.rtws.commons.service.user.UserServices;
	import com.deleidos.rtws.commons.tenantutils.model.alerting.AlertSubscriber;
	import com.deleidos.rtws.commons.tenantutils.model.properties.SystemAppPropertiesModel;
	import com.deleidos.rtws.commons.view.component.UserDisplay;
	import com.deleidos.rtws.commons.view.component.userDisplayClasses.IUserDisplay;
	import com.deleidos.rtws.commons.view.component.userDisplayClasses.UserDisplayMediator;
	import com.deleidos.rtws.commons.view.help.AboutDialog;
	import com.deleidos.rtws.commons.view.help.AboutDialogMediator;
	
	import flash.display.DisplayObjectContainer;
	
	import org.robotlegs.base.ContextEvent;
	import org.robotlegs.mvcs.Context;
	
	public class AlertVizContext extends Context
	{
		public function AlertVizContext(contextView:DisplayObjectContainer = null, autoStartup:Boolean = true)
		{
			super(contextView, autoStartup);
		}
	
		public override function startup():void
		{
			// Inject model references as singletons
			
			// Properties Model
			var propsModel:AlertVizPropertiesModel = new AlertVizPropertiesModel();
			injector.mapValue(IAppPropertiesModel, propsModel);
			injector.mapValue(SystemAppPropertiesModel, propsModel);
			injector.mapValue(AlertVizPropertiesModel, propsModel);
			
			// User Model
			var user:AlertSubscriber = new AlertSubscriber();
			injector.mapValue(IUser, user);
			injector.mapValue(AlertSubscriber, user);
			
			injector.mapSingleton(ParameterModel);
			
			injector.mapSingleton(WatchListModel);
			injector.mapSingleton(AlertCriteriaModel);
			injector.mapSingleton(AlertDataModel);
			injector.mapSingleton(TimelineModel);
			injector.mapSingleton(HTTPQueueModel);
			
			// Graph Component Models
			injector.mapSingleton(GraphModel);
			injector.mapSingleton(GraphConfigModel);
			// Repository Models
			injector.mapSingleton(DataModelDirectory);
			injector.mapSingleton(ModelFieldData); 
			
			// Services
			injector.mapClass(IPropertiesService, PropertiesService);
			injector.mapClass(IUserServices, UserServices);
			injector.mapClass(IThemeService, ThemeService);
			
			injector.mapSingletonOf(IWatchListService, WatchListService);
			injector.mapSingletonOf(IAlertCriteriaService, AlertCriteriaService);

			// Graph Component Services
			injector.mapSingletonOf(IGraphConfigService, GraphConfigService);
			// Repository Services
			injector.mapSingletonOf(IDataModelServices, DataModelServices);
			
			// Map views to mediators
			mediatorMap.mapView(AlertViz, AlertVizMediator);
			mediatorMap.mapView(AppLaunchStatusDisplay, AppLaunchStatusDisplayMediator);
			
			mediatorMap.mapView(UserDisplay, UserDisplayMediator, IUserDisplay);
			mediatorMap.mapView(AboutDialog, AboutDialogMediator);
			
			mediatorMap.mapView(TimelineView, TimelineViewMediator);
			mediatorMap.mapView(MenuView, MenuViewMediator);
			mediatorMap.mapView(WatchListView, WatchListViewMediator);
			mediatorMap.mapView(AlertCriteriaView, AlertCriteriaViewMediator);
			mediatorMap.mapView(AlertDataView, AlertDataViewMediator);
			mediatorMap.mapView(MapView, MapViewMediator);
			// Graph Component Views
			mediatorMap.mapView(GraphView, GraphMediator);
			mediatorMap.mapView(GraphDataView, GraphDataMediator);

			// Popups must have auto-mediation turned off - the mediator must be created
			// manually at popup creation time
			mediatorMap.mapView(InitPopUpView, InitPopUpViewMediator, null, false, false);
			mediatorMap.mapView(InfoPopUpView, InfoPopUpViewMediator, null, false, false);
			mediatorMap.mapView(SettingsPopUpView, SettingsPopUpViewMediator, null, false, false);
			mediatorMap.mapView(CreateWatchListEntryPopUpView, CreateWatchListEntryPopUpViewMediator, null, false, false);
			mediatorMap.mapView(DeleteWatchListEntryPopUpView, DeleteWatchListEntryPopUpViewMediator, null, false, false);
			mediatorMap.mapView(CreateAlertCriteriaPopUpView, CreateAlertCriteriaPopUpViewMediator, null, false, false);
			mediatorMap.mapView(UpdateAlertCriteriaPopUpView, UpdateAlertCriteriaPopUpViewMediator, null, false, false);
			mediatorMap.mapView(DeleteAlertCriteriaPopUpView, DeleteAlertCriteriaPopUpViewMediator, null, false, false);
			mediatorMap.mapView(RefreshAlertCriteriaPopUpView, RefreshAlertCriteriaPopUpViewMediator, null, false, false);
			mediatorMap.mapView(CreateAlertDefinitionPopUpView, CreateAlertDefinitionPopUpViewMediator, null, false, false);
			mediatorMap.mapView(CreateAlertDefinitionHelpPopUpView, CreateAlertDefinitionHelpPopUpViewMediator, null, false, false);
			mediatorMap.mapView(BrowseDataModelPopUpView, BrowseDataModelPopUpViewMediator, null, false, false);
			mediatorMap.mapView(CreateAlertValueSuggestionPopUpView, CreateAlertValueSuggestionPopUpViewMediator, null, false, false);
			mediatorMap.mapView(FormatHelpPopUpView, FormatHelpPopUpViewMediator, null, false, false);
			mediatorMap.mapView(TestWildCardPopUpView, TestWildCardPopUpViewMediator, null, false, false);
			mediatorMap.mapView(AlertMessagePopUpView, AlertMessagePopUpViewMediator, null, false, false);

			// Graph Components Popups
			mediatorMap.mapView(GraphDesignView, GraphDesignViewMediator, null, false, false);
			
			// Map events to commands
			
			// Start-up and global events
			commandMap.mapEvent(PropertiesEvent.FETCH_REMOTE_PROPS_REQUEST, LoadRemotePropsIntoModelCommand);
			commandMap.mapEvent(UserOpRequestEvent.LOAD_USER_INFO, UserOpRequestCommand);
			commandMap.mapEvent(ThemeOpRequestEvent.LOAD_THEME, ThemeOpRequestCommand);
			// TODO commandMap.mapEvent(ClientSessionTimerEvent.RESTART_SESSION_TIMER, RestartClientSessionTimerCommand);
			
			commandMap.mapEvent(ApplicationCommandEvent.SHUTDOWN, ApplicationCommand);
			commandMap.mapEvent(ApplicationCommandEvent.CLEAR, ApplicationCommand);
			
			commandMap.mapEvent(WatchListCommandEvent.REFRESH, WatchListCommand, WatchListCommandEvent);
			commandMap.mapEvent(WatchListCommandEvent.CREATE, WatchListCommand, WatchListCommandEvent);
			commandMap.mapEvent(WatchListCommandEvent.COLOR_CHANGE, WatchListCommand, WatchListCommandEvent);
			commandMap.mapEvent(WatchListCommandEvent.REMOVE, WatchListCommand, WatchListCommandEvent);
			commandMap.mapEvent(WatchListCommandEvent.TRACK_OFF_REQUEST, WatchListCommand, WatchListCommandEvent);
			commandMap.mapEvent(WatchListCommandEvent.TRACK_ON_REQUEST, WatchListCommand, WatchListCommandEvent);
			
			commandMap.mapEvent(AlertCriteriaCommandEvent.REFRESH, AlertCriteriaCommand, AlertCriteriaCommandEvent);
			commandMap.mapEvent(AlertCriteriaCommandEvent.CREATE, AlertCriteriaCommand, AlertCriteriaCommandEvent);
			commandMap.mapEvent(AlertCriteriaCommandEvent.UPDATE, AlertCriteriaCommand, AlertCriteriaCommandEvent);
			commandMap.mapEvent(AlertCriteriaCommandEvent.REMOVE, AlertCriteriaCommand, AlertCriteriaCommandEvent);
			
			commandMap.mapEvent(AlertCommandEvent.ADD, AlertCommand, AlertCommandEvent);
			
			commandMap.mapEvent(HTTPQueueEvent.QUEUE, HTTPQueueCommand, HTTPQueueEvent);
			commandMap.mapEvent(HTTPQueueEvent.COMPLETE, HTTPQueueCommand, HTTPQueueEvent);
			
			// Graph Component Events
			commandMap.mapEvent(AlertCommandEvent.ADD, GraphAlertCommand, AlertCommandEvent);
			commandMap.mapEvent(GraphClientEvent.CLICK, GraphClientCommand, GraphClientEvent);
			commandMap.mapEvent(GraphClientEvent.DOUBLE_CLICK, GraphClientCommand, GraphClientEvent);
			commandMap.mapEvent(ClientKeyboardEvent.KEY_DOWN, ClientKeyboardCommand, ClientKeyboardEvent);
			commandMap.mapEvent(ClientKeyboardEvent.KEY_UP, ClientKeyboardCommand, ClientKeyboardEvent);
			commandMap.mapEvent(GraphConfigEvent.GET_CONFIG, GraphConfigCommand, GraphConfigEvent);
			commandMap.mapEvent(GraphConfigEvent.GET_LOCATIONS, GraphConfigCommand, GraphConfigEvent);
			commandMap.mapEvent(GraphConfigEvent.SAVE, GraphConfigCommand, GraphConfigEvent);
			commandMap.mapEvent(GraphConfigEvent.CREATE_FOLDER, GraphConfigCommand, GraphConfigEvent);
			commandMap.mapEvent(GraphConfigEvent.UPDATE, GraphConfigCommand, GraphConfigEvent);
			commandMap.mapEvent(GraphConfigEvent.REMOVE, GraphConfigCommand, GraphConfigEvent);
			
			// Repository Events
			commandMap.mapEvent(ModelServiceEvent.LOAD_MODEL_DIR, LoadDataModelDirectoryCommand, ModelServiceEvent);
			commandMap.mapEvent(ModelServiceEvent.LOAD_MODEL, LoadModelCommand, ModelServiceEvent);

			
			mediatorMap.createMediator(contextView as AlertViz);
			
			commandMap.mapEvent(ContextEvent.STARTUP_COMPLETE, StartupCommand, ContextEvent, true);
			
			super.startup();
		}
	}
}