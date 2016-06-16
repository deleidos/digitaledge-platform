package com.deleidos.rtws.alertviz.utils
{
	public class FieldNameMinimizer
	{
		public static const FIELD_NAME:String = "fieldName";
		public static const DISPLAY_NAME:String = "displayName";
		
		private var root:FieldNode;
		
		public function FieldNameMinimizer()
		{
			root = new FieldNode();
		}
		
		public function add(fieldName:String):void{
			root.addName(fieldName);
		}
		
		public function getFieldNames():Array{
			return root.getMinNames();
		}
		
	}
}
import com.deleidos.rtws.alertviz.utils.FieldNameMinimizer;
import com.deleidos.rtws.alertviz.utils.FlattenedObject;

/**
 * Creates a graph where nodes can be thought of as a reverse depth first traversal
 * of delimited field names. For example, if the delimiter is a hyphen, the field name
 * "geo-source-latitude" would be converted into three nodes with key values
 * "latitude" -> "source" -> "geo". By adding a second field like "geo-destination-latitude",
 * the "latitude" node will have two children:
 * "latitude" -> "source" -> "geo"
 * 		 	  -> "destination" -> "geo"
 * 
 * The minimal non-ambiguous name for a field is then defined as the first node in that
 * field name's traversal that has no children that branch into more than one child.
 * In the above example "latitude" is ambiguous as that refers to both "geo-source-latitude"
 * and "geo-destination-latitude", so we continue the traversal to create "source.latitude"
 * and "destination.latitude" as the minimal display names. 
 * 
 * There is additional logic in place to handle the special case of when a field name cannot be
 * disambiguated. For example, taking the two fields "id" and "event-device-id", "id" cannot be made
 * more specific as it's entire name is already used, so the end result would be two display names:
 * "id" which refers to the field "id" and "device-id" which refers to the field "event-device-id".
 */
class FieldNode{
	
	public var name:String;
	public var key:String;
	public var children:Object;
	public var childLength:int;
	
	public function FieldNode(k:String = ""){
		name = null;
		key = k;
		childLength = 0;
		children = new Object();
	}
	
	public function addName(keyName:String, fullName:String = null):void{
		if(fullName == null) fullName = keyName;
	
		if(key == keyName)
			name = fullName;
		else{
			var cutOff:int = keyName.lastIndexOf(FlattenedObject.DELIMITER);
			var next:String = cutOff == -1 ? keyName : keyName.substring(cutOff+1);
			var remainder:String = cutOff == -1 ? keyName : keyName.substring(0, cutOff);
			
			var child:FieldNode = children[next];
			if(child == null){ 
				child = new FieldNode(next);
				children[next] = child;
				childLength++;
			}
			
			child.addName(remainder, fullName);	
		}
	}
	
	public function getMinNames(init:Array = null, suffix:String = null):Array{
		if(init == null) init = new Array();
		
		var newKey:String = key + (suffix == null || suffix == "" ? "" : FlattenedObject.DELIMITER + suffix);
		var child:FieldNode;
		if(branches()){
			for each(child in children)
				child.getMinNames(init, newKey);
		}else{
			var cur:String = name;
			var recursiveChildren:Object = children;
			while(cur == null){
				for each(child in recursiveChildren){
					//should only iterate once
					cur = child.name;
					recursiveChildren = child.children;
				}
			} 
			init.push(makeMinName(cur, newKey));
		}
		return init;
	}
	
	private function branches():Boolean{
		if(childLength == 0) return false;
		else if(childLength > 1 || (childLength == 1 && name != null)) return true;
		else{
			for each(var child:FieldNode in children)
				return child.branches(); //should only iterate 1 time
		}
		return false; //should never be reached
	}
	
	private static function makeMinName(fieldName:String, displayName:String):Object{
		var obj:Object = new Object();
		obj[FieldNameMinimizer.FIELD_NAME] = fieldName;
		obj[FieldNameMinimizer.DISPLAY_NAME] = displayName;
		return obj;
	}
}