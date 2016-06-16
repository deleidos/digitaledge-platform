package com.deleidos.rtws.ext.parser;

import java.io.InputStream;

import net.sf.json.JSONObject;

import com.deleidos.rtws.commons.exception.InitializationException;
import com.deleidos.rtws.commons.util.Initializable;
import com.deleidos.rtws.core.framework.parser.AbstractParser;
import com.deleidos.rtws.core.framework.parser.ParsePipelineException;

public class NoOpParser extends AbstractParser implements Initializable {

	@Override
	public void setInputStream(InputStream fileIoObj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void parseHeaders() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject parse() throws ParsePipelineException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initialize() throws InitializationException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
