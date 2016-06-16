package com.deleidos.rtws.alertviz.events.repository
{
	import flash.events.Event;
	
	public class InputModelEditEvent extends Event
	{
		public static const EDIT_KEY             :String = "inputedit";
		public static const MODEL_INSERT_FIELD   :String = EDIT_KEY + ".model.insertField";
		public static const MODEL_FIELD_INSERTED :String = EDIT_KEY + ".model.fieldInserted"; 
		public static const MODEL_DELETE_FIELD   :String = EDIT_KEY + ".model.deleteField";
		public static const MODEL_UPDATE_FIELD   :String = EDIT_KEY + ".model.updateField";
		public static const MODEL_INSERT_OBJECT  :String = EDIT_KEY + ".model.insertObject";
		public static const MODEL_INSERT_ARRAY   :String = EDIT_KEY + ".model.insertArray";
		public static const MODEL_SELECT_DATASRC :String = EDIT_KEY + ".model.selectDataSrc";
		public static const MODEL_ADD_DATASRC    :String = EDIT_KEY + ".model.addDataSrc";
		public static const MODEL_EDIT_DATASRC   :String = EDIT_KEY + ".model.editDataSrc";
		public static const MODEL_REMOVE_DATASRC :String = EDIT_KEY + ".model.removeDataSrc";
		public static const MODEL_MAP_GET_FIELD  :String = EDIT_KEY + ".model.mapGetField";
		public static const MODEL_MAP_USE_CONST  :String = EDIT_KEY + ".model.mapUseConstant";
		public static const MODEL_MAP_CONV_DATE  :String = EDIT_KEY + ".model.convDate";
		public static const MODEL_MAP_USE_SCRIPT :String = EDIT_KEY + ".model.useScript";
		public static const MODEL_MAP_ADD_ELEMENT:String = EDIT_KEY + ".model.addElement";
		public static const MODEL_MAP_DEL_ELEMENT:String = EDIT_KEY + ".model.delElement";
		
		public static const UPDATE_NAME  :String = "updateName";
		public static const UPDATE_TYPE  :String = "updateType";
		
		private var _currentItem   :XML;
		private var _selectedIndex :int;
		
		/*
		 * Parameter mappings:
		 *
		 * MODEL_INSERT_FIELD:	  type, formatString (if field is a datestring)
		 * MODEL_FIELD_INSERTED:  (not used)
		 * MODEL_DELETE_FIELD:    (not used)
		 * MODEL_UPDATE_FIELD:    TODO
		 * MODEL_INSERT_OBJECT:   type (equal to InputModel.OBJECT)
		 * MODEL_INSERT_ARRAY:    type (equal to InputModel.ARRAY), elementType (of elements)
		 * MODEL_SELECT_DATASRC:  parameters object points to data source object
		 * MODEL_ADD_DATASRC:     parameters object points to data source object with name and input fields
		 * MODEL_MAP_GET_FIELD:   fieldNames (only 1), defaultValue (optional)
		 * MODEL_MAP_USE_CONST:   constant
		 * MODEL_MAP_CONV_DATE:   fieldNames (separated by +), formatString
		 * MODEL_MAP_USE_SCRIPT:  fieldNames (separated by comma), scriptName
		 * MODEL_MAP_ADD_ELEMENT: (not used)
		 * MODEL_MAP_DEL_ELEMENT: (not used)
		 */
		private var _parameters    :Object;
		
		public function InputModelEditEvent(type:String, currentItem:XML, selectedIndex:int, parameters:Object, bubbles:Boolean=false, cancelable:Boolean=false)
		{
			super(type, bubbles, cancelable);
			_currentItem  = currentItem;
			_selectedIndex = selectedIndex;
			_parameters = parameters;
		}
		
		public function get currentItem():XML {
			return _currentItem;
		}
		
		public function get selectedIndex():int {
			return _selectedIndex;
		}
		
		public function get parameters():Object {
			return _parameters;
		}
		
		public function set parameters(value:Object):void {
			_parameters = value;
		}
		
		public override function clone():Event
		{
			return new InputModelEditEvent( type, _currentItem, _selectedIndex, _parameters, bubbles, cancelable );
		}
	}
}
