package com.saic.rtws.commons.util;

import javax.xml.bind.DatatypeConverter;

import net.sf.json.JSONObject;

public class FilenamePreservingCompoundMessageGenerator implements CompoundMessageGenerator<String> {

	@Override
	public String generate(final byte[] filename, final byte[] data) {
		JSONObject msg = new JSONObject();
		msg.accumulate("filename", DatatypeConverter.printBase64Binary(filename));
		msg.accumulate("content", DatatypeConverter.printBase64Binary(data));

		return msg.toString();
	}

	@Override
	public String generate(final byte[] data) {
		throw new UnsupportedOperationException("This class only supports compund payloads assuming a file's name and it's content.");
	}

}
