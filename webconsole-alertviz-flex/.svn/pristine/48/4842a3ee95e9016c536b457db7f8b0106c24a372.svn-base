package com.deleidos.rtws.alertviz.models.repository
{
	public class DataModelName
	{
		private var _name       : String;
		private var _version    : String;
		private var _visibility : String;
		private var _latest		: Boolean;
		
		/**
		 * Construct a data model name. If version is null, the name
		 * is expected to be a data model filename, which will be parsed.
		 * Data model filenames are of the form: <modelname>_<version>.zip
		 * where the .zip is optional.
		 */
		public function DataModelName(name:String, visibility:String, version:String=null, latest:Boolean = false) {
			if (version == null) {
				var uscore:int = name.lastIndexOf("_");
				_name = name.substr(0,uscore);
				name = name.replace(/\.zip$/, "");
				_version = uscore == -1 ? "" : name.substr(uscore+1);
			}
			else {
				_name = name;
				_version = version;				
			}
			_visibility = visibility;
			_latest = latest;
		}
		
		public function set name(value:String):void {
			_name = value;
		}
		
		public function get name():String {
			return _name;
		}
		
		public function set version(value:String):void {
			_version = value;
		}
		
		public function get version():String {
			return _version;
		}
		
		public function set visibility(value:String):void {
			_visibility = value;
		}
		
		public function set latest(value:Boolean):void {
			_latest = value;
		}
		
		public function get latest():Boolean {
			return _latest;
		}
		
		public function get visibility():String {
			return _visibility;
		}
		
		public function getZipFilename():String {
			return _name + "_" + _version + ".zip";
		}
		
		public function toString():String {
			return _name + "_" + _version + " (" + visibility + ")";
		}
		
		public function equals(other:DataModelName):Boolean {
			return ((this._name == other._name) &&
				    (this._version == other._version) &&
					(this._visibility == other._visibility));
		}
	}
}
