package com.deleidos.rtws.alertviz.models.repository
{
	import mx.collections.ArrayCollection;
	import mx.collections.XMLListCollection;

	public class FieldData
	{
		private var _name:String;
		public function get name():String{ 
			return _name; 
		}
		
		private var _children:ArrayCollection;
		public function get children():ArrayCollection{ 
			return _children; 
		}
		
		private var _parent:FieldData;
		public function parent():FieldData{ 
			return _parent; 
		}
		
		public function get isLeaf():Boolean{ 
			return _children == null; 
		}
		
		public function FieldData(name:String, parent:FieldData = null)
		{
			_name = name;
			_parent = parent;
		}
		
		public function addChild(child:FieldData):void{
			if(_children == null) _children = new ArrayCollection();
			_children.addItem(child);
		}
		
		public function fullName():String{
			if(_parent == null)
				return _name;
			else
				return _parent.fullName() + "." + _name;
		}
		
		
	}
}