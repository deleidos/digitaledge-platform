package com.deleidos.rtws.alertviz.utils.repository
{
	import com.adobe.serialization.json.JSON;
	import com.deleidos.rtws.alertviz.json.JsonItem;
	import com.deleidos.rtws.alertviz.json.JsonLiteral;
	import com.deleidos.rtws.alertviz.json.JsonObject;
	import com.deleidos.rtws.alertviz.json.OrderedJson;
	import com.deleidos.rtws.alertviz.models.repository.InputModel;
	
	import mx.collections.ArrayCollection;
	
	public class JsonXMLConversions
	{
		public function JsonXMLConversions()
		{
		}

		/* Given a json string, covert it to an actionscript object */
		public static function stringToJsonObject(s:String):Object {
			// return JSON.decode(s);
			return OrderedJson.decode(s);
		}

		/**
		 * Process the given data mapping JSON and return an XMLList object suitable for use
		 * in the list views. 
		 */
		public static function mappingJSONtoXML(jsonObj:Object, canonicalXMLList:XMLList):XMLList {
			// traverse jsonObj, build path variable as you go
			// Get type from canonical model
			// on a child node, set @xlate attribute
			// traverse arrays and objects
			var xml:XML = <root />;
			walkMappingJson(canonicalXMLList, null, jsonObj, xml);
			return xml.field as XMLList;
		}
		
		protected static function walkMappingJson(
			canonicalXMLList:XMLList, parent:String, jsonObject:Object, result:XML):int
		{
			var elementCount:int = 0;
			
			for each (var element:Object in jsonObject) {
				
				var name:String = null;
				var value:Object = element;
				if (value is JsonItem) {
					var item:JsonItem = value as JsonItem;
					name = item.name;
					value = item.value;
				}
				else {
					name = elementCount.toString();
				}
				
				var xml :XML = <field />;
				xml.@fieldname = name;
				
				if (parent != null) {
					if (name.search("^[0-9]+$") >= 0)
						xml.@path = parent + "[" + name + "]";
					else
						xml.@path = parent + "." + name;
				}
				else {
					xml.@path = name;
				}
				
				if (value is String) {
					// xml.@type = item;
					// Lookup type in input model
					// xml.@type = the type found
					// TODO: xml.@path could have a non-zero array reference in it
					var path:String = xml.@path;
					path = path.replace(/\[[0-9]*\]/g,"[0]");
					var inputXML:XML = XMLUtilities.findField(canonicalXMLList, path);
					xml.@type = inputXML.@type;
					xml.@xlate = value;
					result.appendChild(xml);
				}
				else {
					if (value is JsonObject) {
						xml.@type = "OBJECT";
						walkMappingJson(canonicalXMLList, xml.@path, value, xml)						
					}
					else {
						xml.@type = "ARRAY";
						if (walkMappingJson(canonicalXMLList, xml.@path, value, xml) == 0) {
							// var marker:XML = <marker fieldname="(no elements)" type="NONE"/>;
							var marker:XML = InputModel.arrayMarker.copy();
							xml.appendChild(marker);
						}
					}
					
					result.appendChild(xml);						
				}
				
				elementCount++;
			}
			
			return elementCount;
		}
		
		/*
		 * Process a canonical model as an object and return an XMLList representation of it
		 * suitable for display. This is NOT a generalized JSON to XML translator - it is very
		 * specific to this use case. A heirarchical XMLList object is returned where each
		 * field is represetned as follows:
		 * <field fieldname="?" path="?" type="?">
		 * where fieldname is the name of the JSON field, path is the fully qualified path
		 * to the field, and type is the data model data type.
		 */
		public static function canonicalJSONtoXML(jsonObj:Object):XMLList {
			var xml:XML = <root />;
			walkCanonicalJson(null, jsonObj, xml);
			return xml.field as XMLList;
		}
		
		protected static function walkCanonicalJson(parent:String, jsonObject:Object, result:XML):void
		{
			var elementCount:int = 0;
			for each (var element:Object in jsonObject) {

				var name:String = null;
				var value:Object = element;
				if (value is JsonItem) {
					var item:JsonItem = value as JsonItem;
					name = item.name;
					value = item.value;
				}
				else {
					name = elementCount.toString();
				}
				
				var xml :XML = <field />;
				xml.@fieldname = name;
				
				if (parent != null) {
					if (name.search("^[0-9]+$") >= 0)
						xml.@path = parent + "[" + name + "]";
					else
						xml.@path = parent + "." + name;
				}
				else {
					xml.@path = name;
				}
				
				if (value is String) {
					xml.@type = value;
					result.appendChild(xml);
				}
				else {
					if (value is JsonObject) {
						xml.@type = "OBJECT";
					}
					else { 
						xml.@type = "ARRAY";
					}
					
					walkCanonicalJson(xml.@path, value, xml);
					
					result.appendChild(xml);						
				}
				
				elementCount++;
			}
		}
		
		public static function canonicalXMLtoJSON(xmlList:XMLList):String {
			return XMLtoJSON(xmlList, false);
		}
		
		public static function mappingXMLtoJSON(xmlList:XMLList):String {
			return XMLtoJSON(xmlList, true);
		}
		
		private static function XMLtoJSON(xmlList:XMLList, useXlate:Boolean):String {
			var jsonString:String = "{ ";
			var first:Boolean = true;
			for each (var xml:XML in xmlList) {
				var x:String = walkXML(xml,useXlate);
				if (!first) {
					jsonString += ", ";
				}
				jsonString += x;
				first = false;
			}
			jsonString += " }";
			return jsonString;			
		}
		
		private static function walkXML(xmlList:XML, useXlate:Boolean):String {
			var result:String = "";
			for each (var field:XML in xmlList) {
				var fieldname:String = field.@fieldname;
				var fieldtype:String = field.@type;
				var path:String = field.@path;

				if ((fieldname != InputModel.XLATE_ARRAY_MARKER) &&
					(fieldname.search(/^[0-9]+$/) < 0)) {
					result += '"' + fieldname + "\" : ";
				}
				
				if (fieldtype == "OBJECT") {
					result = result + "{ ";
				}
				else if (fieldtype == "ARRAY") {
					result = result + "[ ";
				}
				else if (!useXlate) {
					result = result + "\"" + fieldtype + "\"";
				}
				else if (fieldname != InputModel.XLATE_ARRAY_MARKER) {
					result = result + "\"" + field.@xlate + "\"";
				}
				
				// trace(fieldname + " " + path + " " + fieldtype);
				if (field.hasComplexContent()) {
					var childList:XMLList = field.children();
					var first:Boolean = true;
					for each (var xml:XML in childList) {
						if (!first) {
							result = result + ", ";
						}
						result = result + walkXML(xml,useXlate);
						first = false;
					}
					
					if (fieldtype == "OBJECT") {
						result = result + " }";
					}
					else if (fieldtype == "ARRAY") {
						result = result + " ]";
					}
				}
			}	
			return result;
		}		
		
		/**
		 * Build the JSON that lists each data source, its parser, input fields, and parser parameters.
		 * The resulting JSON, looks something like this:
		 * 
		 * [
		 *	{
		 *		"name": "tpchcsv",
		 *		"parser": "CsvParser",
		 *		"inputFields": [
		 *			"LINNUM",
		 *			"LSTAT",
		 *			"ICMNT",
		 *			"OKEY",
		 *			"OSTAT",
		 *			"OPRICE",
		 * 			"Q"
		 *		],
		 *		"parserPrams": {
		 *			"delimiter": ",",
		 *			"defaultaccesslabel": "UNCLASSIFIED",
		 *			"defaultsource": "UNKNOWN",
		 *			"stoponmissingfield": "false"
		 *		}
		 *	}
		 * ]
		 */
		public static function buildDataSourcesJSON(dataSources:Array):String {
			var json:String = "[ ";
			var q:String = "\"";
			var first:Boolean = true;
			
			for each (var dataSource:Object in dataSources) {
				var name:String = dataSource.name;
				var parser:String = dataSource.parser;
				var inputFields:Array = (dataSource.inputFields as ArrayCollection).source;
				var parserParams:Array = (dataSource.parserParams as ArrayCollection).source;
				
				// Need to create an intermediate object
				// we don't want to store the mapping here
				var obj:Object = new Object();
				obj.name   = dataSource.name;
				obj.parser = dataSource.parser;
				obj.inputFields  = inputFields;
				obj.parserParams = parserParams;
				
				if (!first) {
					json = json + ", ";
				}
				
				json += JSON.encode(obj);
				first = false;
			}
			
			json = json + " ]"
			
			return json;
		}

		/**
		 * Build a JSON string that contains the definition of each chosen enrichment. 
		 */
		public static function buildEnrichmentJSON(enrichments:Array):String {
			var json:String = "[ ";
			var q:String = "\"";
			var first:Boolean = true;
			
			for each (var enrichment:Object in enrichments) {
				if (!first) {
					json = json + ", ";
				}
				
				json += JSON.encode(enrichment);
				first = false;
			}
			
			json = json + " ]";
			
			return json;
		}
	}	
}
