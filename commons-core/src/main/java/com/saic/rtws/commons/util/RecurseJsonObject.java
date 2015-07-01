package com.saic.rtws.commons.util;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class RecurseJsonObject<CONTEXT> {
	
	private boolean callEnterExitObject  = true;
	private boolean callEnterExitArray   = true;
	private boolean callProcessPrimitive = true;
	private boolean includeIndexPaths    = true;
	
	public RecurseJsonObject() {
		super();
	}

	/**
	 * Controls if index paths are computed by including "[<index>]" values in path names.
	 * True means include them, false omits them. Default is true.
	 */
	public void setIncludeIndexPaths(boolean flag ) {
		includeIndexPaths = flag;
	}

	/**	Switches calls to enter/exit object methods on or off. Default is on. */
	public void setCallEnterExitObject(boolean flag ) {
		callEnterExitObject = flag;
	}
	
	/** Switches calls to enter/exit array methods on or off. Default is on. */
	public void setCallEnterExitArray(boolean flag ) {
		callEnterExitArray = flag;		
	}
	
	/** Switches calls to process primitive values on or off. Default is on. */
	public void setCallProcessPrimitive(boolean flag ) {
		callProcessPrimitive = flag;
	}

	/** Called when first entering an array. */
	public void enterArray( String key, String path, String parentPath, Object value, CONTEXT context ) {
		// Override to use
	}
	
	/** Called when exiting an array */
	public void exitArray( String key, String path, String parentPath, Object value, CONTEXT context ) {
		// Override to use
	}

	/** Called when entering an object */
	public void enterObject( String key, String path, String parentPath, Object value, CONTEXT context ) {
		// Override to use
	}
	
	/** Called when exiting an object */
	public void exitObject( String key, String path, String parentPath, Object value, CONTEXT context ) {
		// Override to use
	}
	
	/** Called when a number or string is encountered */
	public void processPrimitive( String key, String path, String parentPath, Object value, CONTEXT context ) {
		// Override to use
	}
	
	public CONTEXT recurse(JSONObject obj, CONTEXT context) {
		recurseInternal( "", "", "", obj, context);
		return context;
	}
	
	private void recurseInternal( String key, String path, String parentPath, Object value, CONTEXT context ) {
		
		if (value instanceof JSONArray) {
			
			if (callEnterExitArray) {
				enterArray( key, path, parentPath, value, context );
			}

			JSONArray jsonArray = (JSONArray)value;
			int index = 0;
			for (Object obj : jsonArray) {
				if (includeIndexPaths) {
					String nextPath = path + '[' + index + ']';	
					recurseInternal(index + "", nextPath, path, obj, context);
				}
				else {
					recurseInternal(key, path, parentPath, obj, context);
				}
				index++;
			}

			if (callEnterExitArray) {
				exitArray( key, path, parentPath, value, context );
			}
		}
		else if (value instanceof JSONObject) {
			
			if (callEnterExitObject) {
				enterObject( key, path, parentPath, value, context );
			}
			
			JSONObject jsonObject = (JSONObject)value;
			for (Iterator it = jsonObject.keys(); it.hasNext(); ) {
			    String nextKey = (String)it.next();
			    String nextPath = (path.length() == 0) ? nextKey : path + '.' + nextKey;
			    recurseInternal( nextKey, nextPath, path, jsonObject.get(nextKey), context );
			}

			if (callEnterExitObject) {
				exitObject( key, path, parentPath, value, context );
			}
		}
		else if (callProcessPrimitive) {
			
			processPrimitive( key, path, parentPath, value, context );
		}
	}
}
