package com.saic.rtws.commons.util.expression;

import net.sf.json.JSONObject;

public interface Matcher {
	public boolean matches(Object value,JSONObject fullRecord);
}
