package com.deleidos.rtws.alertviz.models.repository
{
	import com.deleidos.rtws.alertviz.events.repository.InputModelEditEvent;
	import com.deleidos.rtws.alertviz.services.repository.RepositoryConstants;
	import com.deleidos.rtws.alertviz.utils.repository.JsonXMLConversions;
	import com.deleidos.rtws.alertviz.utils.repository.ValidationUtils;
	import com.deleidos.rtws.alertviz.utils.repository.XMLUtilities;
	import com.deleidos.rtws.commons.util.StringUtils;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	
	import org.robotlegs.mvcs.Actor;

	/*
	 * The InputModel consists of the following:
	 * 1) CanonicalModel     - Fields, data types, and thier structure
	 * 2) dataSourceNameList - List of available data sources
	 * 3) SelectedDataSource - Currently selected data source 
	 * The canonical model fields, types, structure, and the CURRENT translation
	 * are represented using an XML structure where each element has the form:
	 *     <field @name @path @type @xlate>
	 * If a field does not have a translation, the its xlate attribute is missing.
	 */
	public class InputModel extends Actor
	{
		public static const TYPE_STRING     :String = "string";
		public static const TYPE_NUMBER     :String = "number";
		public static const TYPE_DATESTRING :String = "datestring";
		public static const TYPE_OBJECT     :String = "OBJECT";
		public static const TYPE_ARRAY      :String = "ARRAY";
		public static const TYPE_NONE       :String = "NONE";

		public static const XLATE_GET       :String = "get";
		public static const XLATE_CONVERT   :String = "convert";
		public static const XLATE_SCRIPT    :String = "script";
		public static const XLATE_CONSTANT  :String = "=";
		public static const XLATE_NONE      :String = "none";
		
		public static var xlateKeywords:Array = [ XLATE_GET, XLATE_CONVERT, XLATE_SCRIPT ];
		
		private var _dataModelName      : DataModelName;
		private var _canonicalModel     : XMLListCollection;
		private var _dataSourceNameList : ArrayCollection;
		private var _selectedDataSrcObj : Object;
		private var _dataSources        : Array;
		private var _dataModelIsNew     : Boolean;

		// The marker strings should agree on the following two lines
		public static const XLATE_ARRAY_MARKER : String = "(no elements)";
		public static const arrayMarker:XML = <marker fieldname="(no elements)" type="NONE"/>;
		
		public function InputModel()
		{
			clear();
		}
		
		/**
		 * Clear the input model, and set the new flag to true.
		 */
		public function clear():void {
			_canonicalModel = new XMLListCollection();
			_selectedDataSrcObj = null;
			_dataSourceNameList = new ArrayCollection();
			_dataModelName = new DataModelName("Untitled", RepositoryConstants.PRIVATE, "v0.0");
			_dataModelIsNew = true;
			clearDataSources();
		}
		
		/* Return the canonical model. This model enumerates fields and thier data types */
		public function get canonicalModel():XMLListCollection {
			return _canonicalModel;
		}

		/* The name of the current data model */
		public function get dataModelName():DataModelName {
			return _dataModelName;
		}
		
		public function set dataModelName(value:DataModelName):void {
			_dataModelName = value;
		}

		/* Get the object describing the currently selected data source. */
		public function get selectedDataSource():Object {
			return _selectedDataSrcObj;
		}
		
		/* Get the data source name list */
		public function get dataSourceNameList():ArrayCollection {
			return _dataSourceNameList;
		}
		
		public function get dataSourcesList():Array {
			return _dataSources;
		}
		
		/* Set the data source name list */
		public function set dataSourceNameList(value:ArrayCollection):void {
			_dataSourceNameList = value;
		}
		
		public function set dataModelIsNew(value : Boolean):void {
			// TODO: Put new flag into object?
			_dataModelIsNew = value;
		}
		
		public function get dataModelIsNew():Boolean {
			// TODO: Put new flag into object?
			return _dataModelIsNew;
		}
		
		/**
		 * Returns the XML object within the input model that matches the given path.
		 */
		public function getCanonicalField(path:String):XML {
			return XMLUtilities.findField(_canonicalModel.source, path);
		}

		/**
		 * Removes all data sources
		 */
		public function clearDataSources():void {
			_dataSources = new Array();
			_dataSourceNameList.removeAll();
			_selectedDataSrcObj = null;			
		}

		// Create a blank input mapping
		private static function createBlankMapping(inputModel:XMLList):XMLListCollection {
			var result:XML = <root />;
			for each (var xml:XML in inputModel) {
				recurseBlankInputMapping(result,xml);
			}
			return new XMLListCollection(result.field as XMLList);
		}
		
		// Recursive function to creaet a blank input mapping
		private static function recurseBlankInputMapping(result:XML, sourceXML:XML):void {
			for each (var field:XML in sourceXML) {
				var mapping:XML = <field />;
				mapping.@fieldname = field.@fieldname;
				mapping.@type = field.@type;
				mapping.@path = field.@path;
				// trace(field.@fieldname);
				if (field.hasComplexContent()) {
					if (field.@type == "OBJECT") {
						var childList:XMLList = field.children();
						for each (var xml:XML in childList) {
							recurseBlankInputMapping(mapping,xml);
						}
					}
					else {
						mapping.appendChild(arrayMarker.copy());
					}
					mapping.@xlate = field.@type;
				}
				else {
					mapping.@xlate = XLATE_NONE;
				}
				result.appendChild(mapping);
			}	
		}
		
		/**
		 * Adds a data source. If mapping==null, then a blank mapping is created
		 */
		public function addDataSource(name:String, parser:String, inputFields:ArrayCollection, parserParams:ArrayCollection, mapping:XMLListCollection):int {
			var dataSource:Object = new Object();
			dataSource.name = name;
			dataSource.parser = parser;
			dataSource.inputFields = inputFields;
			dataSource.parserParams = parserParams;
			dataSource.mapping = mapping;
			if (mapping == null) {
				dataSource.mapping = createBlankMapping(_canonicalModel.source);
			}
			_dataSources[name] = dataSource;
			_dataSourceNameList.addItem(name);
			return (_dataSourceNameList.getItemIndex(name));
		}
		
		/**
		 * Edits the currently selected data source with the passed values.
		 */
		public function editDataSource(name:String, parser:String, inputFields:ArrayCollection, parserParams:ArrayCollection):void {
			if (_selectedDataSrcObj != null) {
				// TODO: If you rename the input source, the _dataSourceNameList has to be updated too
				// _selectedDataSrcObj.name = name;
				_selectedDataSrcObj.parser = parser;
				_selectedDataSrcObj.inputFields = inputFields;
				_selectedDataSrcObj.parserParams = parserParams;
				// dispatch(new InputModelEditEvent(InputModelEditEvent.MODEL_SELECT_DATASRC, null, index, dataSource));
			}
		}

		/**
		 * Removes the given data source
		 */
		public function removeDataSource(index:int):void {
			var name:String = _dataSourceNameList.getItemAt(index) as String;
			var dataSource:Object = _dataSources[name];
			delete _dataSources[name];
			_dataSourceNameList.removeItemAt(index);
			if (_dataSourceNameList.length == 0) {
				// All data sources are deleted
				clearDataSources();
				dispatch(new InputModelEditEvent(InputModelEditEvent.MODEL_SELECT_DATASRC, null, -1, null));
			}
			else if (_selectedDataSrcObj == dataSource) {
				// Deleted the currently selected data source
				selectDataSource(index);
			}
		}

		/**
		 * Selects the given data source
		 */
		public function selectDataSource(index:int):void {
			var name:String = _dataSourceNameList.getItemAt(index) as String;
			var dataSource:Object = _dataSources[name];
			if (dataSource != null) {
				_selectedDataSrcObj = dataSource;
				dispatch(new InputModelEditEvent(InputModelEditEvent.MODEL_SELECT_DATASRC, null, index, dataSource));
			}
		}

		/**
		 * Return an array collection of the objects allowed to be array elements.
		 */
		public function get arrayElementTypeList():ArrayCollection {
			var list:Array = new Array();
			list.push(TYPE_STRING);
			list.push(TYPE_NUMBER);
			list.push(TYPE_OBJECT);
			return new ArrayCollection(list);
		}
		
		/**
		 * Return the path string for the given name combined with the selection
		 */
		public function fieldPath(selection:XML, name:String):String {
			return XMLUtilities.fieldPath(selection, name);
		}
		
		/**
		 * Create a field.
		 */
		public function createField(name:String, path:String, type:String, xlate:String=null) : XML {
			var field : XML = <field />;
			field.@fieldname = name;
			field.@path = path;
			field.@type = type;
			if (xlate != null) {
				field.@xlate = xlate;
			}
			return field;
		}
		
		/**
		 * Insert a field into the canonical input model
		 */
		private function canonicalInsertField(selection:XML,
											  model:XMLListCollection,
											  field:XML,
											  type:String,
											  childName:String,
											  elementType:String):void
		{
			XMLUtilities.insertChildAfter(model, selection, field);
			
			// Now figure out what to do with objects and arrays 
			
			var newPath:String = field.@path;
			var objectChild:XML = null;
			if (type == TYPE_OBJECT) {
				// Insert the child field (string for now)
				newPath = newPath + "." + childName;
				objectChild = createField(childName, newPath, InputModel.TYPE_STRING);
				field.appendChild(objectChild);
			}
			else if (type == TYPE_ARRAY) {
				// Arrays have an index field beneath the array field that matches the
				// user selected type
				newPath += "[0]";
				var arrayParent : XML = createField("0", newPath, elementType);
				field.appendChild(arrayParent);
				
				// If the array element type is an object, add a child node to it
				if (elementType == InputModel.TYPE_OBJECT) {
					newPath = newPath + "." + childName;
					objectChild = createField(childName, newPath, InputModel.TYPE_STRING);
					arrayParent.appendChild(objectChild);
				}				
			}
			
		}
		
		/**
		 * Callback function to process the insertion of each item into the data model mapping 
		 */ 
		private function processInsertMapping(dataModelName:String, field:XML, path:String, model:XMLListCollection, params:Object):void {
			
			var fieldPrototype:XML = params.fieldPrototype;
			var newField:XML = fieldPrototype.copy();
			
			newField.@path = XMLUtilities.fieldPath(field, newField.@fieldname);
			
			// trace("Insert " + newField.@path + " at " + newField.@path);
			
			XMLUtilities.insertChildAfter(model, field, newField);
			
			/*
			if (atRoot)
				model.addItem(newField);
			else if (field != null) {
				//
				// THE BELOW IS NOT WORKING RELIABLY
				//
				insertChildAfter(model, field, newField);
				// var parent:XML = field.parent();
				// var result:XML = parent.insertChildAfter(field, newField);
				//field.parent().insertChildAfter(field, newField);
			}
			*/
			
			if (params.type == TYPE_OBJECT) {
				// Adds the child too
				var childMapping:XML = <field />;
				childMapping.@fieldname = params.childName;
				childMapping.@type = TYPE_STRING;
				childMapping.@path = newField.@path + "." + params.childName;
				childMapping.@xlate = XLATE_NONE;
				newField.appendChild(childMapping);
			}
			else if (params.type == TYPE_ARRAY) {
				// Adds the no elements marker
				// var marker:XML = <marker fieldname="(no elements)" type="NONE"/>;
				var marker:XML = arrayMarker.copy();
				newField.appendChild(marker);
			}			
		}

		/**
		 * Inserts the item into the input model and all data source mappings.
		 */
		private function insertFieldInternal(selection:XML, field:XML, type:String, childName:String, elementType:String):void {
			
			// var selectionParent:XML = (selection == null) ? (null) : selection.parent();
			// var appendItem:Boolean = (selection == null) || (selection.parent() == null);
			
			// Insert into canonical input model
			canonicalInsertField(selection, _canonicalModel, field, type, childName, elementType);
			
			var insertPath:String = (selection == null) ? (null) : selection.@path;
			
			// Create a blank mapping for this field
			var newField:XML = <field />;
			newField.@fieldname = field.@fieldname;
			newField.@type = field.@type;
			newField.@path = field.@path;
			newField.@xlate = XLATE_NONE;

			// Create extra parameters for insertion
			var params:Object = new Object();
			params.fieldPrototype = newField;		
			params.type = type;
			params.childName = childName;
			params.elementType = elementType;

			// Insert into all data sources
			InputModelUtilities.processDataSources(insertPath, _dataSources, processInsertMapping, params);			
		}
		
		/**
		 * Insert a field after the selection. Performs validation and then delegates the
		 * operation to insertFieldInternal.
		 * 
		 * selection   - currently selected field in the canonical model - insertion point
		 * field       - the field to insert into the canonical model
		 * type        - the type of field being inserted (see TYPE_* constants above)
		 * childName   - For TYPE_ARRAY and TYPE_OBJECT, the name of the child element
		 * elementType - For TYPE_ARRAY, the type of the array elements (TYPE_STRING, TYPE_NUMBER, TYPE_OBJECT)
		 */
		public function insertField(selection:XML, field:XML, type:String, childName:String, elementType:String):Boolean {

			var insertValid:Boolean = false;
			
			if (selection == null) {
				insertValid = true;
			}
			else {
				var selectionIsIndex:Boolean = (selection.@fieldname == "0");
				var selectionParent:XML = selection.parent();
				var selectionParentIsIndex:Boolean = false;
				var selectionParentIsObject:Boolean = true;
				
				if (selectionParent != null) {
					selectionParentIsIndex = (selectionParent.@fieldname == "0");
					selectionParentIsObject = (selectionParent.@type == TYPE_OBJECT);					
				}
				
				// Rules:
				// 1) You cannot insert into the array index
				// 2) You cannot insert any more fields into an array of numbers or strings,
				//    but you can insert more fields into an array of object
				
				insertValid = true;					
				if (selectionIsIndex || (selectionParentIsIndex && !selectionParentIsObject)) {
					insertValid = false;					
				}
			}
			
			if (insertValid) {
				insertFieldInternal(selection, field, type, childName, elementType);				
			}
			
			return insertValid;
		}		

		/**
		 * Removes the field from a data mapping model.
		 */
		private function processDeleteMapping(dataModelName:String, field:XML, path:String, model:XMLListCollection, params:Object):void {
			/*
			if ((field.parent() == null) || (field.parent().name() == "root")) {
				model.removeItemAt(model.getItemIndex(field));
			}
			else {
				XMLUtilities.deleteNode(model.source, field);
			}
			*/
			
			XMLUtilities.deleteInXMLListCollection(model, field);
		}
			
		/**
		 * Removes the field from both the input model and all data source mappings.  
		 */
		public function removeField(field:XML):void {

			if (field != null) {
				var fieldPath:String = field.@path;
				
				/*
				if ((field.parent() == null) || (field.parent().name() == "root")) {
					_canonicalModel.removeItemAt(_canonicalModel.getItemIndex(field));
				}
				else {
					XMLUtilities.deleteNode(_canonicalModel.source, field);
				}
				*/
				
				XMLUtilities.deleteInXMLListCollection(_canonicalModel, field);
				
				InputModelUtilities.processDataSources(fieldPath, _dataSources, processDeleteMapping, null);
			}
		}
		
		private function processMappingReferences(dataModelName:String, field:XML, path:String, model:XMLListCollection, result:Array):void {
			result[dataModelName] += 1;
		}
		
		public function computeMappingReferences(selection:XML):Array {
			var result:Array = new Array();
			for each (var dataSource:Object in _dataSources) {
				result[dataSource.name] = 0;
			}
			if (selection != null) {
				InputModelUtilities.processDataSources(selection.@path, _dataSources, processMappingReferences, result);
			}
			return result;
		}

		/** Updates the field names and paths to the new fieldname */
		private function updateFieldnames(dataModelName:String, field:XML, path:String, model:XMLListCollection, newFieldName:String):void {
			field.@fieldname = newFieldName;
			field.@path = StringUtils.beforeLast(field.@path, ".") + "." + newFieldName;			
		}
		
		/**
		 * Updates the input model fieldname in both the input model and the data mappings.
		 */
		public function updateFieldname(field:XML, newFieldname:String):void {
			var oldFieldPath:String = field.@path;
			
			field.@path = StringUtils.beforeLast(field.@path, ".") + "." + newFieldname;
			field.@fieldname = newFieldname;
			
			InputModelUtilities.processDataSources(oldFieldPath, _dataSources, updateFieldnames, newFieldname);
		}
		
		/**
		 * Updates the input model type.
		 */
		public function updateDataType(field:XML, newDataType:String):void {
			// Nothing to do right now - perhaps in the future this would perform some validation 
		}

		/**
		 * Parses a translation directive and returns an object with dynamically assigned
		 * fields of parsed values. Returns null if there is a syntax error with the directive.
		 */
		public function xlateParse(xlateString:String):Object {
			var validateRegex:RegExp;
			var result:Object = null;
			var keyword:String = xlateKeyword(xlateString);
			var params:String;
			var comma:int;
			
			if (keyword == XLATE_GET) {
				// get(field[,default])
				validateRegex = ValidationUtils.getFieldRegEx;
				if (xlateString.match(validateRegex)) {
					result = new Object();
					params = xlateString.substring(XLATE_GET.length+1, xlateString.length-1);
					var values:Array = params.split(",");
					comma = params.indexOf(",");
					result.keyword = keyword;
					result.fieldName    = StringUtils.trim((comma >= 0) ? params.slice(0,comma) : params);
					result.defaultValue = StringUtils.trim((comma >= 0) ? params.slice(comma+1) : "");
				}				
			}
			else if (keyword == XLATE_CONSTANT) {
				// =value
				validateRegex = ValidationUtils.setConstantRegEx;
				if (xlateString.match(validateRegex)) {
					result = new Object();
					result.keyword = keyword;
					result.constant = xlateString.substr(1);
				}
			}
			else if (keyword == XLATE_SCRIPT) {
				// script(name[,field]*)
				validateRegex = ValidationUtils.runScriptRegEx;
				if (xlateString.match(validateRegex)) {
					result = new Object();
					params = xlateString.substring(XLATE_SCRIPT.length+1, xlateString.length-1);
					comma = params.indexOf(",");
					result.keyword = keyword;
					result.scriptName = StringUtils.trim((comma >= 0) ? params.slice(0,comma) : params);
					result.arguments  = StringUtils.trim((comma >= 0) ? params.slice(comma+1) : "");
				}
			}
			else if (keyword == XLATE_CONVERT) {
				// convert(field1[+field2]*,<format>)
				validateRegex = ValidationUtils.doConvertRegEx;
				if (xlateString.match(validateRegex)) {
					result = new Object();
					params = xlateString.substring(XLATE_CONVERT.length+1, xlateString.length-1);
					comma = params.indexOf(",");
					result.keyword = keyword;
					result.fieldNames = StringUtils.trim(params.slice(0,comma));
					result.dateFormat = StringUtils.trim(params.slice(comma+1));
				}
			}
			
			return result;
		}

		/**
		 * Examines the translation string and returns the translation keyword
		 * it corresponds to or null if no match. 
		 */
		public function xlateKeyword(xlateString:String):String {
			if (xlateString.charAt(0) == XLATE_CONSTANT)
				return XLATE_CONSTANT;
			else {
				var keyword:String = StringUtils.beforeFirst(xlateString,"(");
				keyword = StringUtils.trim(keyword.toLowerCase());
				for each (var kw:String in xlateKeywords) {
					if (keyword == kw) {
						return kw;
					}
				}
				return null;
			}
		}
		
		/**
		 * Set the translation of the selected field to get a field from the source.
		 */
		public function xlateGetField(field:XML, fieldName:String, defaultValue:String):void {
			if ((defaultValue == null) || (defaultValue.length == 0)) {
				field.@xlate = XLATE_GET + "(" + fieldName + ")";				
			}
			else {
				field.@xlate = XLATE_GET + "(" + fieldName + "," + defaultValue + ")";				
			}			
		}

		/**
		 * Set the translation of the selected field to a constant value.
		 */
		public function xlateUseConstant(field:XML, constant:String):void {
			field.@xlate = XLATE_CONSTANT + constant;			
		}
		
		/**
		 * Set the translation of the selected field to a date conversion.
		 */
		public function xlateConvertDate(field:XML, inputFields:String, formatString:String):void {
			field.@xlate = XLATE_CONVERT + "(" + inputFields + ", " + formatString + ")";
		}
		
		/**
		 * Set the translation of the selected field to call a script.
		 */
		public function xlateCallScript(field:XML, scriptName:String, inputFields:String):void {
			if ((inputFields == null) || (inputFields.length == 0)) {
				// Ask Richard about this, can a script have no input parameters?
				field.@xlate = XLATE_SCRIPT + "(" + scriptName + ")";					
			}
			else {
				field.@xlate = XLATE_SCRIPT + "(" + scriptName + ", " + inputFields + ")";							
			}			
		}

		/**
		 * Replace the path prefix on all fields in the XML tree
		 */
		private static function replacePathPrefix(oldPathPrefix:String, newPathPrefix:String, sourceXML:XML):void {
			for each (var field:XML in sourceXML) {
				var path:String = field.@path;
				path = path.replace(oldPathPrefix, newPathPrefix);
				field.@path = path;
				if (field.hasComplexContent()) {
					var childList:XMLList = field.children();
					for each (var xml:XML in childList) {
						replacePathPrefix(oldPathPrefix, newPathPrefix, xml);
					}
				}
			}
		}

		/**
		 * Adds an input mapping array element to the selected array.
		 */
		public function xlateAddArrayElement(arrayField:XML):void {
			var arrayFieldPath:String = arrayField.@path;
			var canonicalPath:String = arrayFieldPath.replace(/\[[0-9*]\]/g,"[0]");
			var inputModelField:XML = getCanonicalField(canonicalPath);
			if (inputModelField != null) {
				// Check to see if the translation mapping has a marker field only,
				// meaning this array has not yet been mapped. If so, remove it.
				var childList:XMLListCollection = new XMLListCollection(arrayField.children());
				var numChildren:int = childList.length;
				var firstElement:XML = childList.getItemAt(0) as XML;
				var name:String = firstElement.name();
				if (name == "marker") {
					XMLUtilities.deleteNode(childList.source, firstElement);
					numChildren--;
				}

				// Now create a blank translation of the single element in
				// the input model
				var inputModelElement:XML = inputModelField.children()[0];
				var newChildRoot:XML = <root />;
				recurseBlankInputMapping(newChildRoot, inputModelElement);

				// Rename the path expression
				var newChild:XML = (newChildRoot.field as XMLList)[0];
				var oldPath:String = newChild.@path;
				var newPath:String = arrayField.@path + "[" + numChildren.toString() + "]";
				replacePathPrefix(oldPath, newPath, newChild);

				// Rename the field name from 0 to whatever the next index is
				newChild.@fieldname = numChildren.toString();

				arrayField.appendChild(newChild);
				// trace(newChild);
			}
		}

		/**
		 * Deletes the last input mapping array element to the selected array.
		 */
		public function xlateRemoveArrayElement(arrayField:XML):void {
			var inputModelField:XML = getCanonicalField(arrayField.@path);
			if (inputModelField != null) {
				var childList:XMLListCollection = new XMLListCollection(arrayField.children());
				var numChildren:int = childList.length;
				var targetElement:XML = childList.getItemAt(numChildren-1) as XML;
				var name:String = targetElement.name();
				if (name != "marker") {
					XMLUtilities.deleteNode(childList.source, targetElement);
					numChildren--;
					if (numChildren == 0) {
						// Last element has been removed - put back to the marker
						arrayField.appendChild(arrayMarker.copy());
					}
				}				
			}
		}
		
		/** Sets the canonical model and name */
		public function loadCanonicalModel(dataModelName:DataModelName, modelData:XMLList):void {
			_dataModelName = dataModelName;
			_canonicalModel = new XMLListCollection(modelData);
			_dataModelIsNew = false;
		}

		/** Sets the next data source adding to the overall set of data sources */ 
		public function loadNextDataSource(name:String,
										   parser:String,
										   inputFields:Array,
										   parserParams:Array,
										   mapping:XMLList):void {
			var dataSource:Object = new Object();
			dataSource.name = name;
			dataSource.parser = parser;
			dataSource.inputFields = new ArrayCollection(inputFields);
			dataSource.parserParams = new ArrayCollection(parserParams);
			dataSource.mapping = new XMLListCollection(mapping);
			_dataSources[name] = dataSource;
			_dataSourceNameList.addItem(name);
		}		
	}
}
