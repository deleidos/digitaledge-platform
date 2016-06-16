package com.deleidos.rtws.alertviz.expandgraph.models
{
	import mx.collections.XMLListCollection;
	import mx.events.ItemClickEvent;
	
	import org.robotlegs.mvcs.Actor;

	/** Handles inserting, deleting, moving, and renaming of graph configuration file names. Can be converted
	 * to XML to reflect the tree structure of the file system. **/
	public class GraphConfigModel extends Actor
	{
		//Tree-like structure for storing the config files
		private var root:Node = new Node();
		
		public static const NAME:String = "name";
		
		public function addItem(directory:String):Boolean{
			return root.addItem( directory.split("\\") );
		}
		
		public function deleteItem(directory:String):Boolean{
			return root.deleteItem( directory.split("\\") );
		}
		
		public function renameItem(directory:String, newName:String):Boolean{
			var item:Node = root.getItem(directory.split("\\"));
			if(item == null) return false;
			
			if(item.parent != null){
				item.parent.children[item.name] = null;
				item.parent.children[newName] = item;
			}
			item.name = newName;
			return true;
		}
		
		public function moveItem(oldDirectory:String, newDirectory:String):Boolean{
			var oldPrefix:Array = oldDirectory.split("\\");
			var oldName:String = oldPrefix.pop();
			
			var oldObj:Node = root.getItem(oldPrefix);
			if(oldObj == null)
				return false;
			
			var newPrefix:Array = newDirectory.split("\\");
			var newName:String = newPrefix.pop();
			
			var newObj:Node = root.getItem(newPrefix);
			if(newObj == null)
				return false;
			
			if(newObj.isItem) return false;
			newObj.children[newName] = oldObj[oldName];
			oldObj.children[oldName] = null;
			
			return true;
		}
		
		public function itemExists(directory:String):Boolean{
			return root.getItem( directory.split("\\") ) != null;
		}
		
		/**
		 * Returns whether or not the given path is valid for insertion.
		 * A false value implies a non folder item exists in the path.
		 * Ex: if folder\test is an item, folder\test\test is invalid, but
		 * if folder\test is a folder, folder\test\test is valid
		 */
		public function validInsertionPath(directory:String):Boolean{
			var curPath:Array = new Array();
			var dir:Array = directory.split("\\");
			
			for each( var path:String in dir ){
				curPath.push(path);
				if(root.getItem(curPath) != null) //implies an item (not a folder), exists in that location
					return false;
			}
			
			//make sure there isn't a folder with the same name as the insertion point
			//ex: trying to create folder\test when folder\test\ exists
			if(dir[dir.length-1] != ""){ //we only care if we're trying to add an item
				return (root.getItem(dir.concat("")) == null);
			}else
				return true; 
		}
		
		public function fileStructureToXMLList():XMLList{
			return buildList(root);
		}
		
		private function buildList( node:Node ):XMLList{
			var list:Array = new Array();
			if(!node.isItem){
				for (var name:String in node.children ){
					var newChild:XML;
					if(node.children[name].isItem)
						newChild = new XML("<item " + NAME + "=\"" + name + "\" isBranch=\"false\"/>");
					else{
						newChild = new XML("<folder " + NAME + "=\"" + name + "\" isBranch=\"true\"/>");
						for each(var child:XML in buildList(node.children[name]))
							newChild.appendChild(child);	
					}
					
					list.push(newChild);
				}
			}
			
			//folders first, then items
			list.sortOn(["@isBranch", "@" + NAME], [Array.DESCENDING | Array.CASEINSENSITIVE, Array.CASEINSENSITIVE]);
			
			var xmlList:XMLListCollection = new XMLListCollection();
			for each( var item:XML in list )
				xmlList.addItem(item);
			
			return xmlList.source;
		}
		
		public function clear():void{
			root = new Node();
		}
	}
}

class Node{
	
	public var parent:Node;
	public var children:Object;
	public var name:String; //value such that parent.children[name] == this
	
	public var isItem:Boolean;
	
	public function Node(parent:Node = null, isItem:Boolean = false, name:String = null){
		this.parent = parent;
		this.name = name;
		this.isItem = isItem;
		children = new Object();
	}
	public function addItem(directory:Array):Boolean{
		return addItemH(directory.concat()); //performs a shallow copy so the original array is not affected
	}
	private function addItemH(directory:Array):Boolean{
		if(isItem) return false;
		
		var next:String = directory.shift();
		if(directory.length == 0){
			if(next == "") return true;
			children[next] = new Node(this, true, next);
			return true;
		}else{
			if(children[next] == null)
				children[next] = new Node(this, false, next);
			return children[next].addItemH(directory);
		}
	}
	
	public function deleteItem(directory:Array):Boolean{
		return deleteItemH(directory.concat());
	}
	private function deleteItemH(directory:Array):Boolean{
		if(isItem) return false;
		
		var next:String = directory.shift();
		if(children[next] == null)
			return false;
		else{
			if(directory.length == 0 || (directory.length == 1 && directory[0] == "")){
				delete children[next];
				return true;
			}else
				return children[next].deleteItemH(directory);
		}
	}
	
	public function getItem(directory:Array):Node{
		return getItemH(directory.concat());
	}
	private function getItemH(directory:Array):Node{
		var next:String = directory.shift();
		if(directory.length == 0){
			if(next == "") //looking for folder
				return isItem ? null : this;
			else if(children[next] == null)
				return null;
			else
				return children[next].isItem ? children[next] : null;
		}
			
		if(children[next] == null)
			return null;
		else
			return children[next].getItemH(directory);
	}
}