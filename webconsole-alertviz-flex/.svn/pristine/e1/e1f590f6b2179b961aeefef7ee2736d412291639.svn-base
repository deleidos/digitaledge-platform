package com.deleidos.rtws.alertviz.services.repository.rest
{
	import com.deleidos.rtws.alertviz.events.repository.ModelServiceEvent;
	import com.deleidos.rtws.alertviz.models.repository.DataModelDirectory;
	import com.deleidos.rtws.alertviz.models.repository.DataModelName;
	import com.deleidos.rtws.alertviz.models.repository.InputModel;
	import com.deleidos.rtws.alertviz.models.repository.ModelFieldData;
	import com.deleidos.rtws.alertviz.services.repository.DataModelZipFile;
	import com.deleidos.rtws.alertviz.services.repository.IDataModelServices;
	import com.deleidos.rtws.alertviz.utils.repository.LoadSaveUtils;
	
	import flash.events.Event;
	import flash.events.IOErrorEvent;
	import flash.utils.ByteArray;
	
	import mx.rpc.events.FaultEvent;
	
	public class DataModelServices extends RepositoryRestService implements IDataModelServices
	{
		[Inject]
		public var dmDirectory:DataModelDirectory;
		
		[Inject]
		public var fieldData:ModelFieldData;
		
		private static var LOAD_MODEL_DIR:String = "LoadModelDirectory";
		private static var LOAD_MODEL:String     = "LoadModel";
		
		public function loadModelDirectory(visibility:String):void
		{
			var requestParams:Object = new Object();
			requestParams.operation = LOAD_MODEL_DIR;
			requestParams.errorMsg = "Could not load the data model directory";
			requestParams.event = new ModelServiceEvent(ModelServiceEvent.MODEL_DIR_LOADED, null, null);
			requestParams.visibility = visibility;
			super.sendListContentRequest(requestParams, visibility, "models");
		}
		
		public function load(dataModelName:DataModelName):void
		{
			var requestParams:Object = new Object();
			requestParams.operation = LOAD_MODEL;
			requestParams.dataModelName = dataModelName;
			requestParams.errorMsg = "Could not retrieve data for \"" + dataModelName.name + "\".";
			requestParams.event = new ModelServiceEvent(ModelServiceEvent.MODEL_LOADED, dataModelName, null);
			super.sendRetrieveContentRequest(requestParams, dataModelName.visibility, "models", dataModelName.getZipFilename());
		}
		
		override protected function processError(response:Object):void {
			if(requestParams.operation == LOAD_MODEL_DIR)
				dispatch(new ModelServiceEvent(ModelServiceEvent.MODEL_DIR_LOAD_FAIL, requestParams.errorMsg, null));
			else if(requestParams.operation == LOAD_MODEL)
				dispatch(new ModelServiceEvent(ModelServiceEvent.MODEL_LOAD_FAIL, requestParams.errorMsg, null));
		}
		
		private function buildErrorsAndWarnings(messages:Array):String {
			var oneMsg:String = "";
			var index:int = 1;
			for each (var msg:String in messages) {
				oneMsg += (index + ". " + msg + "\n");
				index++;
			}
			return oneMsg;
		}
		
		override protected function processResponse(response:Object):void {
			if (requestParams.operation == LOAD_MODEL) {
				//
				// The data bytes retrieved from the server should be a zip file.
				//
				var zipBytes:ByteArray = response as ByteArray;
				var dmZipFile:DataModelZipFile = new DataModelZipFile(zipBytes);
				var messages:Array = LoadSaveUtils.loadModelFieldData(requestParams.dataModelName, fieldData, dmZipFile);
				if (messages.length > 0)
					dispatch(new ModelServiceEvent(ModelServiceEvent.MODEL_LOAD_FAIL, buildErrorsAndWarnings(messages), null));
				else
					dispatch(requestParams.event);
			}
			else if (requestParams.operation == LOAD_MODEL_DIR) {
				LoadSaveUtils.populateDataModelDirectory(dmDirectory, response.data, requestParams.visibility);
				dispatch(requestParams.event);
			}
		}
	}
}