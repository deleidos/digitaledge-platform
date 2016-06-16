package com.deleidos.rtws.alertviz.services.repository
{
	import flash.utils.ByteArray;
	import flash.utils.IDataInput;
	
	import mx.utils.Base64Decoder;
	
	import nochump.util.zip.ZipEntry;
	import nochump.util.zip.ZipFile;
	import nochump.util.zip.ZipOutput;

	public class DataModelZipFile
	{
		public static var CANONICAL_PREFIX:String  = "canonical";
		public static var XLATE_PREFIX:String      = "xlate";
		public static var DATASRC_PREFIX:String    = "datasources";
		public static var ENRICHMENT_PREFIX:String = "enriched";
		public static var ENRICHCFG_PREFIX:String  = "enrichcfg";
		
		public static var JSON_TYPE:String = ".json";
		
		private var fullZipEntries:Array;
		private var zipOut:ZipOutput;
		
		/**
		 * Create a data model zip file. If input is null, then the class assumes you are
		 * creating a new zip file. If input is not null, it assumes you are reading an
		 * existing zip file.
		 */
		public function DataModelZipFile(input:IDataInput=null)
		{
			fullZipEntries = new Array();
			if (input == null) {
				zipOut = new ZipOutput();				
			}
			else {
				var zipFile:ZipFile = new ZipFile(input);
				for each (var zipEntry:ZipEntry in zipFile.entries) {
					var fullZipEntry:Object = new Object();
					fullZipEntry.zentry = zipEntry;
					fullZipEntry.contents = zipFile.getInput(zipEntry);
					fullZipEntries[zipEntry.name] = fullZipEntry;
				}
			}
		}

		/** Convert a string to a byte array */
		private function toByteArray(s:String):ByteArray {
			var result:ByteArray = new ByteArray();
			result.writeMultiByte(s, "iso-8859-1");
			return result;
		}

		/** Put a zip entry from a string into the zip file */
		private function putStringEntry(filename:String, s:String):void {
			var ze:ZipEntry = new ZipEntry(filename);
			zipOut.putNextEntry(ze);
			zipOut.write(toByteArray(s));
			zipOut.closeEntry();						
		}
		
		/** Get the contents of the named item from the zip file */
		public function getContents(name:String):ByteArray {
			if (fullZipEntries[name] == null) {
				return null;
			}
			return fullZipEntries[name].contents;
		}
		
		/** Complete the zip file */
		public function finishZip():void {
			zipOut.finish();
		}

		/** Completes creation of the zip file and returns the bytes for it */
		public function getZipFileData():ByteArray {
			return zipOut.byteArray;
		}
		
		// Put and Get the canonical model
		
		public function getCanonicalModel():String {
			var filename:String = CANONICAL_PREFIX + JSON_TYPE;
			var data:ByteArray = getContents(filename);
			return (data == null) ? (null) : data.toString();
		}
		
		public function putCanonicalModel(json:String):void {
			var filename:String = CANONICAL_PREFIX + JSON_TYPE;
			putStringEntry(filename, json);
		}

		// Put and Get a translation mapping
		
		public function getTranslationMapping(inputFormatName:String):String {
			var filename:String = XLATE_PREFIX + "_" + inputFormatName + JSON_TYPE;
			var data:ByteArray = getContents(filename);
			return (data == null) ? (null) : data.toString();
		}
		
		public function putTranslationMapping(inputFormatName:String, json:String):void {
			var filename:String = XLATE_PREFIX + "_" + inputFormatName + JSON_TYPE;
			putStringEntry(filename, json);
		}
		
		public function getAllTranslationNames():Array {
			var result:Array = new Array();
			for (var name:String in fullZipEntries) {
				if (name.substr(0, XLATE_PREFIX.length) == XLATE_PREFIX) {
					var formatName:String = name.substr(XLATE_PREFIX.length+1).replace(JSON_TYPE,"");
					result.push(formatName);
				}
			}
			return result;
		}

		// Put and Get data source parameters
		
		public function getDataSourceParams():String {
			var filename:String = DATASRC_PREFIX + JSON_TYPE;
			var data:ByteArray = getContents(filename);
			return (data == null) ? (null) : data.toString();
		}
		
		public function putDataSourceParams(json:String):void {
			var filename:String = DATASRC_PREFIX + JSON_TYPE;
			putStringEntry(filename, json);
		}

		// Put and Get enrichment model (deprecated)
		
		public function getEnrichmentModel():String {
			var filename:String = ENRICHMENT_PREFIX + JSON_TYPE;
			var data:ByteArray = getContents(filename);
			return (data == null) ? (null) : data.toString();
		}
		
		public function putEnrichmentModel(json:String):void {
			var filename:String = ENRICHMENT_PREFIX + JSON_TYPE;
			putStringEntry(filename, json);
		}
		
		// Put and Get enrichment configuration

		public function getEnrichmentConfig():String {
			var filename:String = ENRICHCFG_PREFIX + JSON_TYPE;
			var data:ByteArray = getContents(filename);
			return (data == null) ? (null) : data.toString();
		}
		
		public function putEnrichmentConfig(json:String):void {
			var filename:String = ENRICHCFG_PREFIX + JSON_TYPE;
			putStringEntry(filename, json);
		}
		
		// Procedure to make a zip file:
		// 
		// import flash.utils.ByteArray;
		// import nochump.util.zip.*;
		//
		// file info
		// var fileName:String = "helloworld.bin";
		// var fileData:ByteArray = new ByteArray();
		// fileData.writeUTF("Hello World!");
		//
		// var zipOut:ZipOutput = new ZipOutput();
		// Add entry to zip
		// var ze:ZipEntry = new ZipEntry(fileName);
		// zipOut.putNextEntry(ze);
		// zipOut.write(fileData);
		// zipOut.closeEntry();
		// end the zip
		// zipOut.finish();
		// access the zip data
		// var zipData:ByteArray = zipOut.byteArray;
	}
}