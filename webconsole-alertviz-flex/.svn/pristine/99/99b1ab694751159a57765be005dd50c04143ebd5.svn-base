package com.deleidos.rtws.alertviz.utils.repository
{
	import com.deleidos.rtws.commons.util.StringUtils;
	
	import mx.collections.XMLListCollection;

	public class XMLUtilities
	{
		/**
		 * Find the given field path (dot notation) in the hierarchical XML list and return it.
		 * Otherwise return null.
		 */
		public static function findField(xmlList:XMLList, path:String):XML {
			path = path.replace(/\[\]/g, "[0]");
			path = path.replace(/\]/g,"");
			path = path.replace(/\[/g,".");
			var paths:Array = path.split(".");
			var children:XMLList = xmlList;
			var current:XML = null;
			var index:int = 0;
			do {
				var i:int = 0;
				while (i<children.length()) {
					if (children[i].@fieldname == paths[index]) {
						current = children[i];
						break;
					}
					i++;
				}
				if (i >= children.length()) {
					return(null); // not found
				}
				index++;
				children = current.children();
			} while ((children != null) && (index < paths.length));
			return current;
		}
		
		/**
		 * Given a comma delimited list of paths, returns an array of XML objects
		 * of each path.
		 */
		public static function findFields(xmlList:XMLList, pathListString:String):Array {
			var paths:Array = pathListString.split(",");
			var result:Array = new Array();
			for each (var path:String in paths) {
				var xml:XML = XMLUtilities.findField(xmlList,StringUtils.trim(path));
				if (xml != null) {
					result.push(xml);
				}
			}
			return result;			
		}
		
		/**
		 * Deletes the XML object from its parent.
		 */
		public static function deleteNode(container:XMLList, xmlToDelete:XML):Boolean
		{
			var parent:XML = xmlToDelete.parent();
			var cn:XMLList;

			if (parent == null) {
				cn = container;
			}
			else {
				cn = XMLList(parent).children();
			}
				
			for ( var i:Number = 0 ; i < cn.length() ; i++ ) {
				if ( cn[i] == xmlToDelete ) {
					delete cn[i];       
					return true;
				}
			}    			
			return false;			
		}

		/**
		 * Returns the dot delimited path for name given selection.
		 * Assumes the fields contain a path attribute.
		 */
		public static function fieldPath(selection:XML, name:String):String {
			var path : String;
			if (selection == null) {
				// No selection - root name
				path = name;
			}
			else {
				// If the path of the parent is the null string, name goes in root
				// otherwise use parent's path and add in name
				var parent : XML = selection.parent();
				if (parent == null) {
					path = name;
				}
				else {
					var parentPath : String = parent.@path;
					path = (parentPath.length == 0) ? name : (parentPath + "." + name);
				}
			}
			return path;
		}

		/**
		 * Updates the given field's name and path attribute.
		 */
		public static function updateFieldName(field:XML, newName:String):XML {
			var path:String = field.@path;
			var pattern:RegExp = new RegExp(field.@fieldname + "$");
			path = path.replace(pattern, newName);
			field.@fieldname = newName;		
			field.@path = path;
			return field;
		}
		
		/**
		 * Special insertion for an XMLListCollection with items that reference a root node
		 * called "root". If the selecction is null, or its parent is null, just add the
		 * node to the end of the top level. Otherwise, if the parent is pointing to the
		 * root node, add the node at the top level at the right insertion point. Otherwise,
		 * add it within the XML object itself at the right place.
		 */
		public static function insertChildAfter(model:XMLListCollection, selection:XML, field:XML):void {
			if (selection == null) {
				model.addItem(field);
			}
			else if ((selection.parent() == null) || (selection.parent().name() == "root")) {
				var index:int = model.getItemIndex(selection) + 1;
				if (index >= 0) {
					model.addItemAt(field, index);
				}
				else {
					model.addItem(field);					
				}
			}
			else {
				selection.parent().insertChildAfter(selection, field);
			}
		}
		
		/**
		 * Deletes a node in an XML list collection.
		 */
		public static function deleteInXMLListCollection(model:XMLListCollection, field:XML):void {
			if ((field.parent() == null) || (field.parent().name() == "root")) {
				model.removeItemAt(model.getItemIndex(field));
			}
			else {
				XMLUtilities.deleteNode(model.source, field);
			}
		}		
	}
}
