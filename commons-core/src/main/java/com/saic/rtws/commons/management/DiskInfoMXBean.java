package com.saic.rtws.commons.management;

import java.util.Map;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * DiskInfoBean is a standard MBean, so this interface is not currently used.
 * 
 * @author rainerr
 *
 */

@XmlJavaTypeAdapter(DiskInfoMXBean.TypeAdapter.class)
public interface DiskInfoMXBean {
	public long getUsableSpace();
	public long getFreeSpace();
	public long getTotalSpace();
	public long getSpaceUsed();
	public double getPercentSpaceUsed();
	public double getPercentSpaceAvailable();
	public Map<String, Double> fetch();
	
	static class TypeAdapter extends XmlAdapter<Object, DiskInfoMXBean> {
		public DiskInfoMXBean unmarshal(Object element) { return (DiskInfoMXBean)element; }
		public Object marshal(DiskInfoMXBean bean) { return bean; }
	}
}
