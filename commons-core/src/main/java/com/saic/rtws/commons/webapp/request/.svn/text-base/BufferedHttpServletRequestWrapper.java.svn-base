package com.saic.rtws.commons.webapp.request;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.collections.map.MultiValueMap;

/**
 * This wrapper buffers the body of an HttpServletRequest, if one exists, so
 * that it may be read multiple times by chained filters or servlets.
 * 
 * @author moserm
 */
public class BufferedHttpServletRequestWrapper extends HttpServletRequestWrapper {

	// HTTP request body gets stored here, so it can be read more than once
	private byte[] buffer = null;

	private MultiValueMap parameters = null;

	public BufferedHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);

		if (request.getContentLength() > 0) {
			try {
				InputStream in = request.getInputStream();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();

				int num;
				byte[] data = new byte[8192];
				while ((num = in.read(data, 0, data.length)) != -1) {
					baos.write(data, 0, num);
				}
				baos.flush();

				buffer = baos.toByteArray();
			} catch (Exception ignore) {}
		}
		else {
			buffer = new byte[0];
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public String getParameter(String name) {
		if (buffer != null && buffer.length > 0) {
			if (parameters == null) {
				extractParameters();
			}
			List l = (List) parameters.get(name);
			Object o = null;
			if (l != null) o = l.get(0);
			if (o != null) return o.toString(); 
			return null;
		}
		return super.getParameter(name);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map getParameterMap() {
		if (buffer != null && buffer.length > 0) {
			if (parameters == null) {
				extractParameters();
			}
			// build a copy of parameters as a Map<Object, String[]>
			HashMap map = new HashMap();
			for (Object key : parameters.keySet()) {
				List l = (List) parameters.get(key);
				String[] value = new String[l.size()];
				for (int i=0; i < l.size(); i++) {
					Object o = l.get(i);
					if (o != null) value[i] = o.toString();
				}
				map.put(key, value);
			}
			return Collections.unmodifiableMap(map);
		}
		return super.getParameterMap();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Enumeration getParameterNames() {
		if (buffer != null && buffer.length > 0) {
			if (parameters == null) {
				extractParameters();
			}
			return Collections.enumeration(parameters.keySet());
		}
		return super.getParameterNames();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public String[] getParameterValues(String name) {
		if (buffer != null && buffer.length > 0) {
			if (parameters == null) {
				extractParameters();
			}
			Collection vals = parameters.getCollection(name);
			if (vals == null) {
				return null;
			}
			return (String[])vals.toArray(new String[vals.size()]);
		}
		return super.getParameterValues(name);
	}

	/*
	 * Poor man's parameter extraction from the buffered HTTP request body and the request URI
	 */
	private void extractParameters() {
		try {
			if (parameters == null) {
				parameters = new MultiValueMap();
			}

			String bufferStr = new String(buffer, "UTF-8");
			extractParametersFromString(bufferStr);

			bufferStr = getQueryString();
			extractParametersFromString(bufferStr);
		}
		catch (Exception ignore) {}
	}

	private void extractParametersFromString(String s) {
		try {
			String keyValues[] = s.split("&");
			for (String keyVal : keyValues) {
				int eqSign = keyVal.indexOf('=');
				if (eqSign >= 0) {
					String key = URLDecoder.decode(keyVal.substring(0, eqSign), "UTF-8");
					String value = URLDecoder.decode(keyVal.substring(eqSign+1), "UTF-8");

					parameters.put(key, value);
				}
			}
		} catch (Exception ignore) {}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		return new BufferedServletInputStream(buffer);
	}

	/**
	 * A ServletInputStream that reads from the buffered byte array.
	 */
	public class BufferedServletInputStream extends ServletInputStream {
		private ByteArrayInputStream bais;

		public BufferedServletInputStream(byte[] theBuffer) {
			bais = new ByteArrayInputStream(theBuffer);
		}

		@Override
		public int read() {
			return bais.read();
		}
	}
}
