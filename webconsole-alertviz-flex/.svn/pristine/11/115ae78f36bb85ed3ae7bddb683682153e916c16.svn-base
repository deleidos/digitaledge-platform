package com.deleidos.rtws.alertviz.expandgraph.views.renderers
{
	import mx.containers.HBox;
	import mx.controls.Button;
	import mx.controls.Label;
	import mx.controls.Spacer;
	import mx.controls.listClasses.BaseListData;
	import mx.controls.listClasses.IDropInListItemRenderer;
	import mx.controls.listClasses.IListItemRenderer;
	import mx.core.UIComponent;
	import mx.core.UITextField;

	public class FunctionMenuItemRenderer extends HBox implements IListItemRenderer, IDropInListItemRenderer{
		// IListItemRenderer implementation
		private var _data:Object;
		
		private var _name:Label;
		private var _usage:Button;
		
		override public function get data():Object{
			return _data;
		}
		
		override public function set data(d:Object):void{
			_data = d;
			if(_data != null){
				_name.text = _data["functionName"] == null ? "" : _data["functionName"];
				_usage.toolTip = _data["functionUsage"] == null ? "" : _data["functionUsage"];
				visible = _data.visible;
			}else{
				visible = false;
			}
		}
		
		//IDropInListItemRenderer implementation:
		private var _listData:BaseListData;
		
		// Make the listData property bindable.
		[Bindable("dataChange")]
		
		public function get listData():BaseListData{
			return _listData;
		}
		
		public function set listData(value:BaseListData):void{
			_listData = value;
		}
		
		//create your children here
		override protected function createChildren():void{
			super.createChildren();
			
			_name= new Label();
			
			var spacer:Spacer = new Spacer();
			spacer.percentWidth = 100;
			
			_usage = new Button();
			_usage.enabled = false;
			_usage.label = "?";
			
			addChild(_name);
			addChild(spacer);
			addChild(_usage);
		}
	}
}