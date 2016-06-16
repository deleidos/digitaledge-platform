package com.deleidos.rtws.alertviz.utils.repository
{
	import com.adobe.serialization.json.JSON;
	import com.deleidos.rtws.alertviz.json.OrderedJson;
	import com.deleidos.rtws.alertviz.models.repository.DataModelDirectory;
	import com.deleidos.rtws.alertviz.models.repository.DataModelName;
	import com.deleidos.rtws.alertviz.models.repository.InputModel;
	import com.deleidos.rtws.alertviz.models.repository.ModelFieldData;
	import com.deleidos.rtws.alertviz.services.repository.DataModelZipFile;
	
	public class LoadSaveUtils
	{

		/** Enumerates properites that are shared by all processors */
		private static const predefinedProperties:Object =
			{ removeParameters:"", override:"", extractPath:"", parameters:"" };  
		
		/**
		 * Load the data models from the xml string into the data model directory. 
		 */
		public static function populateDataModelDirectory(dmDir:DataModelDirectory, xml:XML, visibility:String):void {
			
			// Structure of the response has a list of files that contains all the
			// data model filesnames.
			var fileList:XMLList = xml.descendants("file");
			var dataModelNames:Array = new Array();
			var dataModelVersions:Array = new Array();
			for each (var entry:XML in fileList) {
				// Parse each filename into its name and version,
				// and then organize the data into a list of names pointing
				// to a list of versions.
				var dmFilename:String = entry.toString();
				var dmName:DataModelName = new DataModelName(dmFilename, visibility);
				var versionList:Array = dataModelVersions[dmName.name];
				if (versionList == null) {
					dataModelVersions[dmName.name] = versionList = new Array();
					dataModelNames.push(dmName.name);
				}
				versionList.push(dmName.version);
			}
			
			dataModelNames.sort();
			
			// Now add all this to the data model directory
			dmDir.clear();
			for (var key:String in dataModelNames) {
				var name:String = dataModelNames[key];
				dmDir.addDataModelEntry(name, visibility, dataModelVersions[name]);
			}
			
			dmDir.loaded = true;
		}
		
		/**
		 * Loads the data model from the data model zip file (dmZipFile).
		 * Loads the input model, translations, data source parameters, and enrichments.
		 * Returns an array of messages.
		 */
		public static function loadDataModelZip(
									dataModelName:DataModelName,
									inputModel:InputModel,
									dmZipFile:DataModelZipFile):Array
		{
			var messages:Array = new Array();
			
			// Clear whatever is there now
			inputModel.clear();
			
			// Get the canonical JSON and convert it to an XML list, then load it 
			var canonicalJSON:String = dmZipFile.getCanonicalModel();
			if (canonicalJSON == null) {
				messages.push("Input model json file is missing.");
				return messages;
			}
			
			var obj : Object = OrderedJson.decode(canonicalJSON);
			var inputModelXMLList:XMLList = JsonXMLConversions.canonicalJSONtoXML(obj);
			inputModel.loadCanonicalModel(dataModelName, inputModelXMLList);
			inputModel.dataModelIsNew = false;
			// var s:String = JsonXMLConversions.canonicalXMLtoJSON(inputModelXMLList);
			
			// Convert data source params json to an array
			var dataSrcJsonText:String = dmZipFile.getDataSourceParams();
			var mappingArray:Array = new Array();
			if (dataSrcJsonText == null) {
				messages.push("Data source parameter file missing.");
			}
			else {
				mappingArray = (JSON.decode(dataSrcJsonText) as Array);
			}
				
			// Convert json text to an object then to an xmllist
			var translations:Array = dmZipFile.getAllTranslationNames();
			for each (var name:String in translations) {
				// Find data source parameters for this translation
				var xlateJson:String = dmZipFile.getTranslationMapping(name);
				var xlateObj:Object = OrderedJson.decode(xlateJson);
				var mappingXMLList:XMLList = JsonXMLConversions.mappingJSONtoXML(xlateObj, inputModelXMLList);
				var notFound:Boolean = true;
				
				// s = JsonXMLConversions.mappingXMLtoJSON(mappingXMLList);
				for each (var mapping:Object in mappingArray) {
					if (name == mapping.name) {
						notFound = false;
						
						inputModel.loadNextDataSource(
							mapping.name,
							mapping.parser,
							mapping.inputFields,
							mapping.parserParams,
							mappingXMLList);
						
						break;
					}
				}
				
				if (notFound) {
					//if (dataSrcJsonText != null) {
						messages.push("No data source parameters found for " + name + ".");
					//}
					inputModel.loadNextDataSource(
						name,
						"UnknownParser",
						new Array(),
						new Array(),
						mappingXMLList);
				}
			}
			
			// var s2:String = JsonXMLConversions.buildEnrichmentJSON(enrichModel.chosenEnrichments.source);
			
			return messages;
		}
		
		public static function loadModelFieldData(dataModelName:DataModelName,
												  modelFieldData:ModelFieldData,
												  dmZipFile:DataModelZipFile):Array{
			var messages:Array = new Array();
			
			// Clear whatever is there now
			modelFieldData.clear();
			
			// Get the field JSON and convert it to an Array, then load it 
			var fieldJSON:String = dmZipFile.getEnrichmentModel();
			if (fieldJSON == null) {
				fieldJSON = dmZipFile.getCanonicalModel();
				if(fieldJSON == null){
					messages.push("Field data is missing for " + dataModelName.name + "." );
					return messages;
				}
			}
			
			var json:Array = OrderedJson.decode(fieldJSON) as Array;
			modelFieldData.loadFieldData(json, dataModelName);
			
			return messages;
		}
	}
}
