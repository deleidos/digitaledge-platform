package com.saic.rtws.commons.management;

import java.util.Properties;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlJavaTypeAdapter(ManagedBean.TypeAdapter.class)
public interface ManagedBean {
	public void buildObjectNameKeys(Properties keys);

	static class TypeAdapter extends XmlAdapter<Object, ManagedBean> {
		public ManagedBean unmarshal(Object element) { return (ManagedBean)element; }
		public Object marshal(ManagedBean bean) { return bean; }
	}
}
