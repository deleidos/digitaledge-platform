package com.saic.rtws.commons.metrics;

import java.util.HashMap;

public class MetricsMap {

	private HashMap<MetricsKey, Number> metricsMap = new HashMap<MetricsKey, Number>();
	
	public void addMetric(MetricsKey key) {
		addMetric(key, 0);
	}
	
	public void addMetric(MetricsKey key, Integer initialValue) {
		metricsMap.put(key, initialValue);
	}
	
	public void addMetric(MetricsKey key, Double initialValue) {
		metricsMap.put(key, initialValue);
	}
	
	public void addMetric(MetricsKey key, Float initialValue) {
		metricsMap.put(key, initialValue);
	}
	
	public void addMetric(MetricsKey key, Long initialValue) {
		metricsMap.put(key, initialValue);
	}
	
	public void removeMetric(MetricsKey key) {
		metricsMap.remove(key);
	}
	
	public void increment(MetricsKey key) {
		Number value = metricsMap.get(key);
		
		if(value != null) {
			if(value.getClass() == Integer.class) {
				metricsMap.put(key, value.intValue() + 1);
			}
			else if(value.getClass() == Double.class) {
				metricsMap.put(key, value.doubleValue() + 1D);
			}
			else if(value.getClass() == Float.class) {
				metricsMap.put(key, value.floatValue() + 1F);
			}
			else if(value.getClass() == Long.class) {
				metricsMap.put(key, value.longValue() + 1L);
			}
		}
	}
	
	public void deccrement(MetricsKey key) {
		Number value = metricsMap.get(key);
		
		if(value != null) {
			if(value.getClass() == Integer.class) {
				metricsMap.put(key, value.intValue() - 1);
			}
			else if(value.getClass() == Double.class) {
				metricsMap.put(key, value.doubleValue() - 1D);
			}
			else if(value.getClass() == Float.class) {
				metricsMap.put(key, value.floatValue() - 1F);
			}
			else if(value.getClass() == Long.class) {
				metricsMap.put(key, value.longValue() - 1L);
			}
		}
	}
	
	public Number getMetric(MetricsKey key) {
		return metricsMap.get(key); 
	}
}
