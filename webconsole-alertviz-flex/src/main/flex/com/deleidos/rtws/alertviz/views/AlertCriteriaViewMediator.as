package com.deleidos.rtws.alertviz.views
{	
	import com.deleidos.rtws.alertviz.events.AlertCriteriaCommandEvent;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaEntry;
	import com.deleidos.rtws.alertviz.models.AlertCriteriaModel;
	import com.deleidos.rtws.alertviz.views.popups.AlertMessagePopUpView;
	import com.deleidos.rtws.alertviz.views.popups.CreateAlertCriteriaPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.CreateWatchListEntryPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.DeleteAlertCriteriaPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.RefreshAlertCriteriaPopUpView;
	import com.deleidos.rtws.alertviz.views.popups.UpdateAlertCriteriaPopUpView;
	
	import flash.events.KeyboardEvent;
	import flash.events.MouseEvent;
	import flash.ui.Keyboard;
	
	import mx.controls.Alert;
	import mx.managers.PopUpManager;
	import mx.skins.spark.EditableComboBoxSkin;
	
	import org.robotlegs.mvcs.Mediator;
	
	import spark.events.GridEvent;

	public class AlertCriteriaViewMediator extends Mediator
	{
		[Inject] public var view:AlertCriteriaView;
		
		[Inject] public var alertCritModel:AlertCriteriaModel;
		
		public override function onRegister():void
		{	
			setViewAlertDefsFromModel();
			
			view.alertCriteriaDataGrid.addEventListener(KeyboardEvent.KEY_UP, onDataGridKeyUp);
			view.alertCriteriaDataGrid.addEventListener(GridEvent.GRID_DOUBLE_CLICK, handleDataGridDoubleClick);
			
			view.watchButton.addEventListener(MouseEvent.CLICK, handleWatchButtonClick);
			view.createButton.addEventListener(MouseEvent.CLICK, handleCreateButtonClick);
			view.editButton.addEventListener(MouseEvent.CLICK, handleEditButtonClick);
			view.removeButton.addEventListener(MouseEvent.CLICK, handleRemoveButtonClick);
			view.refreshButton.addEventListener(MouseEvent.CLICK, handleRefreshButtonClick);

			addContextListener(AlertCriteriaCommandEvent.REFRESH_SUCCESS, onAlertDefChangeEvent);
			addContextListener(AlertCriteriaCommandEvent.CREATE_SUCCESS, onAlertDefChangeEvent);
			addContextListener(AlertCriteriaCommandEvent.UPDATE_SUCCESS, onAlertDefChangeEvent);
			addContextListener(AlertCriteriaCommandEvent.REMOVE_SUCCESS, onAlertDefChangeEvent);
		}
		
		private function onAlertDefChangeEvent(event:AlertCriteriaCommandEvent):void {
			setViewAlertDefsFromModel();
		}
		
		private function setViewAlertDefsFromModel():void {
			view.alertDefs = alertCritModel.alertDefList;
		}
		
		protected function handleDataGridDoubleClick(event:GridEvent):void
		{
			if(event != null && event.item != null && event.item is AlertCriteriaEntry) {
				var alertDefToEdit:AlertCriteriaEntry = (event.item as AlertCriteriaEntry);
				if(alertDefToEdit != null) {
					editAlertDef(alertDefToEdit);
				}
			}
		}
		
		private function onDataGridKeyUp(event:KeyboardEvent):void
		{
			if(event != null)
			{
				if(event.keyCode == Keyboard.ENTER)
				{
					editSelectedAlertDefs();
				}
				else if(event.keyCode == Keyboard.DELETE)
				{
					deleteSelectedAlertDefs();
				}
			}
		}
		
		protected function handleWatchButtonClick(event:MouseEvent):void
		{
			var selectedAlertDefs:Vector.<AlertCriteriaEntry> = getSelectedAlertDefs();
			
			if (selectedAlertDefs.length > 0) {
				var currItemToAdd:Object = null;
				var watchList:Array = new Array();
				
				var currAlertDef:AlertCriteriaEntry = null;
				for(var index:int=0; index < selectedAlertDefs.length; index++) {
					currAlertDef = selectedAlertDefs[index];
					
					currItemToAdd = new Object();
					currItemToAdd['key'] = currAlertDef.alertDefKey;
					currItemToAdd['name'] = currAlertDef.alertDefName;
					
					watchList.push(currItemToAdd);
				}
				
				var popup:CreateWatchListEntryPopUpView = new CreateWatchListEntryPopUpView();
				popup.watchList = watchList;
				
				PopUpManager.addPopUp(popup, contextView, true);
				PopUpManager.centerPopUp(popup);
				
				mediatorMap.createMediator(popup);
			} else {
				var popup2:AlertMessagePopUpView = new AlertMessagePopUpView();
				
				PopUpManager.addPopUp(popup2, contextView, true);
				PopUpManager.centerPopUp(popup2);
				
				mediatorMap.createMediator(popup2);
				
				
				var title:String = "Please Make a Selection";
				var message:String = "Please select oneor more Alert Definitions to watch";
				dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.ALERT_MESSAGE, {title:title, message:message}));
			}
		}
		
		protected function handleCreateButtonClick(event:MouseEvent):void
		{
			var popup:CreateAlertCriteriaPopUpView = new CreateAlertCriteriaPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
		}
		
		protected function handleEditButtonClick(event:MouseEvent):void
		{
			editSelectedAlertDefs();
		}
		
		protected function handleRemoveButtonClick(event:MouseEvent):void
		{
			deleteSelectedAlertDefs();
		}
		
		protected function handleRefreshButtonClick(event:MouseEvent):void
		{
			var popup:RefreshAlertCriteriaPopUpView = new RefreshAlertCriteriaPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
		}
		
		protected function editSelectedAlertDefs():void
		{
			var selectedAlertDefs:Vector.<AlertCriteriaEntry> = getSelectedAlertDefs();
			
			if(selectedAlertDefs.length == 1) {
				editAlertDef(selectedAlertDefs[0]);
			} else {
				Alert.show("Please select one Alert Definition to edit", "Please Make a Selection");
			}
		}
		
		private function editAlertDef(alertDef:AlertCriteriaEntry):void {
			var popup:CreateAlertCriteriaPopUpView = new CreateAlertCriteriaPopUpView();
			
			PopUpManager.addPopUp(popup, contextView, true);
			PopUpManager.centerPopUp(popup);
			
			mediatorMap.createMediator(popup);
			
			dispatch(new AlertCriteriaCommandEvent(AlertCriteriaCommandEvent.SEND_DEF, alertDef));
		}
		
		private function deleteSelectedAlertDefs():void {
			var selectedAlertDefs:Vector.<AlertCriteriaEntry> = getSelectedAlertDefs();
			
			if (selectedAlertDefs.length > 0) {
				var currItemToRemove:Object = null;
				var removeList:Array = new Array();
				
				var currAlertDef:AlertCriteriaEntry = null;
				for(var index:int=0; index < selectedAlertDefs.length; index++) {
					currAlertDef = selectedAlertDefs[index];
					
					currItemToRemove = new Object();
					currItemToRemove['key'] = currAlertDef.alertDefKey;
					currItemToRemove['name'] = currAlertDef.alertDefName;
					
					removeList.push(currItemToRemove);
				}
				
				var popup:DeleteAlertCriteriaPopUpView = new DeleteAlertCriteriaPopUpView();
				popup.removeList = removeList;
				
				PopUpManager.addPopUp(popup, contextView, true);
				PopUpManager.centerPopUp(popup);
				
				mediatorMap.createMediator(popup);
			} else {
				Alert.show("Please select one or more Alert Definitions to remove", "Please Make a Selection");
			}
		}
		
		private function getSelectedAlertDefs():Vector.<AlertCriteriaEntry> {
			var result:Vector.<AlertCriteriaEntry> = new Vector.<AlertCriteriaEntry>();
			
			var selectedItems:Vector.<Object> = view.alertCriteriaDataGrid.selectedItems;
			if(selectedItems != null) {
				var currObj:Object = null;
				for(var index:int=0; index < selectedItems.length; index++) {
					currObj = selectedItems[index];
					if(currObj != null && (currObj is AlertCriteriaEntry)) {
						result.push(currObj);
					}
				}
			}
			
			return result;
		}
	}
}