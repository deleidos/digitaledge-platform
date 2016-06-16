package com.deleidos.rtws.alertviz.models.repository
{
	import com.adobe.serialization.json.JSON;
	import com.deleidos.rtws.alertviz.json.JsonItem;
	import com.deleidos.rtws.alertviz.json.JsonObject;
	import com.deleidos.rtws.alertviz.json.OrderedJson;
	
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;
	
	import org.robotlegs.mvcs.Actor;

	public class ModelFieldData extends Actor
	{
		
		public static const NAME_FIELD:String = "name";
		
		private var _data:ArrayCollection;
		public function get data():ArrayCollection{ return _data; }
		
		private var _modelName:DataModelName;
		public function get modelName():DataModelName{ return _modelName; }
		
		private var _jsonObj:Object;
		public function get jsonObj():Object{ return _jsonObj; }
		
		public function ModelFieldData()
		{
			clear();
		}
		
		public function clear():void{
			_data = new ArrayCollection();
			_modelName = null;
		}
		
		public function loadFieldData(enrichmentJSON:Array, name:DataModelName):void{
			_data = new ArrayCollection();
			for each(var item:JsonItem in enrichmentJSON)
				_data.addItem(loadFieldDataH(item.name, item.value));
			_modelName = name;
			_jsonObj = JSON.decode(OrderedJson.encode(enrichmentJSON));
		}
		
		private function loadFieldDataH(name:String, data:Object, parent:FieldData = null):FieldData{
			var field:FieldData = new FieldData(name, parent);
			if(data is JsonObject){
				for each(var item:JsonItem in data)
					field.addChild(loadFieldDataH(item.name, item.value, field));
			}else if(data is Array){
				for(var i:int = 0; i < data.length; i++)
					field.addChild(loadFieldDataH(i.toString(), data[i], field));
			}			
			return field;
		}
	}
}