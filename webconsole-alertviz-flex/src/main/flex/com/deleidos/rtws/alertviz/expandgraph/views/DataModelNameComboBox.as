package com.deleidos.rtws.alertviz.expandgraph.views
{
	import com.deleidos.rtws.alertviz.models.repository.DataModelDirectory;
	import com.deleidos.rtws.alertviz.models.repository.DataModelName;
	
	import mx.collections.ArrayCollection;
	import mx.collections.Sort;
	import mx.collections.SortField;
	import mx.controls.ComboBox;

	/**
	 * A combo box that displays the names of the latest data models.
	 */
	public class DataModelNameComboBox extends ComboBox
	{
		private var _dmDirectory:DataModelDirectory;
		
		public function DataModelNameComboBox(){
			labelFunction = modelName;
		}
		
		public function get dmDirectory():DataModelDirectory{
			return _dmDirectory;
		}
		
		public function set dmDirectory(value:DataModelDirectory):void{
			_dmDirectory = value;
			refresh();
		}
		
		/**
		 * Syncs the combo box with the data model directory.
		 */
		public function refresh():void{
			var data:ArrayCollection = new ArrayCollection(_dmDirectory.dataModelNames);
			var srt:Sort = new Sort();
			srt.fields = [new SortField("name",true)];
			data.sort = srt;
			data.filterFunction = isLatestModel;
			data.refresh();
			dataProvider = data;
		}
		
		protected function modelName(item:Object):String{
			var name:String = (item as DataModelName).name;
			return name;
		}
		
		protected function isLatestModel(item:Object):Boolean{
			return (item as DataModelName).latest;
		}
		
		public function selectModelByName(value:String):void{
			for each(var model:DataModelName in dataProvider){
				if(model.name == value){
					super.selectedItem = model;
					return;
				}
			}
			super.selectedIndex = -1;
			//throw Error("No model found by the name: " + value);
		}
		
		public function get selectedModel():DataModelName{
			return super.selectedItem as DataModelName;
		}
		
		public function set selectedModel(value:DataModelName):void{
			super.selectedItem = value;
		}
	}
}