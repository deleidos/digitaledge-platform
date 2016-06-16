package com.deleidos.rtws.systemcfg.beans.servercluster.jms;

import java.util.Set;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.deleidos.rtws.systemcfg.beans.jms.JmsQueue;
import com.deleidos.rtws.systemcfg.beans.jms.JmsTopic;
import com.deleidos.rtws.systemcfg.beans.servercluster.ServerClusterImpl;

@JsonIgnoreProperties(ignoreUnknown=true)
public class JmsServerCluster extends ServerClusterImpl {
	private Set<JmsQueue> queues;
	private Set<JmsTopic> topics;
	
	public Set<JmsQueue> getQueues() {
		return queues;
	}
	public void setQueues(Set<JmsQueue> queues) {
		this.queues = queues;
	}
	public Set<JmsTopic> getTopics() {
		return topics;
	}
	public void setTopics(Set<JmsTopic> topics) {
		this.topics = topics;
	}
}
