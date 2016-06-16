package com.deleidos.rtws.alertviz.models.repository
{
	import com.deleidos.rtws.alertviz.utils.repository.XMLUtilities;
	
	import mx.collections.XMLListCollection;
	
	public class InputModelUtilities
	{
		/**
		 * Process one field into the path location specified in the mapping model.
		 * dataModelName  - Name of the data model
		 * path           - path in the DATA SOURCE MAPPING
		 * model          - Data source mapping model to be processed
		 * processFunc    - Function to call for each field in each data source mapping
		 * params         - Extra parameters to be passed to processFunc on each data source mapping field
		 */
		private static function processOneField(
										 dataModelName:String,
										 path:String,
										 model:XMLListCollection,
										 processFunc:Function,
										 params:Object):Boolean
		{
			var insertField:XML = XMLUtilities.findField(model.source, path);
			var success:Boolean = (insertField != null);
			
			if (success) {
				processFunc(dataModelName, insertField, path, model, params);
			}
						
			return success;
		}
		
		/**
		 * Replaces the patterns <<N>> where N is a nesting level with the
		 * integer counts in the passed array.
		 */
		private static function makePath(pathPattern:String, counts:Array):String {
			var result:String = pathPattern;
			for (var i:int = 0; i<counts.length; i++) {
				var search:String  = "<<" + i.toString() + ">>";
				var replace:String = counts[i].toString();
				result = result.replace(search, replace);
			}
			return result;
		}
		
		/**
		 * Insert an empty field mapping into the given mapping model
		 * dataModelName           - Name of the data source
		 * inputModelPath - the path string in the input model that should apply to all data mappings
		 * model          - Data source mapping model to be processed
		 * processFunc    - Function to call for each field in each data source mapping
		 * params         - Extra parameters to be passed to processFunc on each data source mapping field
		 */
		private static function processDataSource(
									dataModelName:String,
									inputModelPath:String,
									model:XMLListCollection,
									processFunc:Function,
									params:Object):void
		{
			// Count the number of array references in the path
			var arrayReferences:int = inputModelPath.split("[").length - 1;
			if (arrayReferences > 0) {
				
				// All array elements that contain the path to the new field must
				// be updated. First, build a count array for all array nesting
				// levels and initialize to zero
				var counts:Array = new Array(arrayReferences);
				for (var i:int=0; i<counts.length; i++) {
					counts[i] = 0;
				}
				
				// Now, create a path string with the array indicies "[0]" replaced
				// with a nesting string, <<N>> where N is the nesting level
				var pathPattern:String = inputModelPath;
				var nesting:int = 0;
				while (pathPattern.indexOf("[0]") >= 0) {
					pathPattern = pathPattern.replace("[0]", "[<<"+nesting.toString()+">>]");
					nesting++;
				}
				
				// Now loop through all combinations of arrays, processing the field
				// if the array path exists
				var incrementLevel:int = counts.length-1;
				while (incrementLevel >= 0) {
					var arrayPath:String = makePath(pathPattern, counts);
					trace("arrayPath = " + arrayPath);
					if (processOneField(dataModelName, arrayPath, model, processFunc, params)) {
						// Path existed
						incrementLevel = counts.length - 1;
						counts[incrementLevel] += 1;
					}
					else {
						// Path did not exist - increment the next level up
						counts[incrementLevel] = 0;
						incrementLevel--;
						if (incrementLevel >= 0) {
							counts[incrementLevel] += 1;
						}
					}
				}
			}
			else {
				// No array references - the simple case
				processOneField(dataModelName, inputModelPath, model, processFunc, params);				
			}
		}
		
		/**
		 * Processes all data source references that correspond to the inputModelPath string in the input model.
		 * The function processFunc will be called for every reference, including all array references.
		 * Parameters:
		 * inputModelPath - the path string in the input model that should apply to all data mappings
		 * dataSources    - array of all data source mappings
		 * processFunc    - Function to call for each field in each data source mapping
		 * params         - Extra parameters to be passed to processFunc on each data source mapping field
		 */
		public static function processDataSources(inputModelPath:String, dataSources:Array, processFunc:Function, params:Object):void {
			
			// For every data source
			for each (var dataSource:Object in dataSources) {
				
				// Get the mapping model for this source
				var mappingModel:XMLListCollection = dataSource.mapping as XMLListCollection;
				
				// Process the data source
				processDataSource(dataSource.name, inputModelPath, mappingModel, processFunc, params);				
			}
		}
	}
}
