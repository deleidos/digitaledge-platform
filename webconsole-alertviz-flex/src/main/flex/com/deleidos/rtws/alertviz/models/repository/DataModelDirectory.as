package com.deleidos.rtws.alertviz.models.repository
{
	import com.deleidos.rtws.alertviz.services.repository.RepositoryConstants;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	
	import org.robotlegs.mvcs.Actor;
	
	public class DataModelDirectory extends Actor
	{
		private var _curDataModelName : DataModelName;
		private var _dataModelList    : XMLListCollection;
		private var _dataModelNames   : Array;
		private var _loaded			  : Boolean = false;
		
		public function DataModelDirectory()
		{
			_curDataModelName = new DataModelName("Untitled",RepositoryConstants.PRIVATE,"v0.0");
			_dataModelList = new XMLListCollection();
			_dataModelNames = new Array();
		}
		
		/**
		 * Removes all data model info from the directory
		 */
		public function clear():void {
			_dataModelList.removeAll();
			_dataModelNames = new Array();
		}

		/**
		 * Return the list of data models.
		 */
		public function get dataModelNames():Array {
			return _dataModelNames;
		}

		/**
		 * Adds a data model entry to the directory
		 * name - Name of the data model
		 * versionStrings - Array of version strings
		 */
		public function addDataModelEntry(name:String, visibility:String, versionStrings:Array):void {
			var dmRoot:XML = <datamodel/>;
			dmRoot.@name = name;
			versionStrings.sort(versionSort);
			for (var i:int = 0; i < versionStrings.length; i++) {
				var versionString:String = versionStrings[i];
				var latest:Boolean = (i == versionStrings.length -1);
				var versionLeaf:XML = <version/>;
				versionLeaf.@name = name;
				versionLeaf.@version = versionString;
				versionLeaf.@latest = latest;
				dmRoot.appendChild(versionLeaf);
				_dataModelNames.push(new DataModelName(name, visibility, versionString, latest));
			}
			
			_dataModelList.addItem(dmRoot);
		}
		
		protected function versionSort(o1:Object, o2:Object):Number{
			var tmp1:Number = Number((o1 as String).replace(/[^0-9.]/g, "")); //remove letters
			var tmp2:Number = Number((o2 as String).replace(/[^0-9.]/g, "")); 
			if(isNaN(tmp1) || isNaN(tmp2))
				return (o1 as String) < (o2 as String) ? -1 : ((o1 as String) == (o2 as String) ? 0 : 1);
			else
				return tmp1 < tmp2 ? -1 : (tmp1 == tmp2 ? 0 : 1);
		}
		
		public function get dataModelList():XMLListCollection {
			return _dataModelList;
		}
		
		public function get loaded():Boolean{
			return _loaded;
		}
		
		public function set loaded(value:Boolean):void{
			_loaded = value;
		}
	}
}