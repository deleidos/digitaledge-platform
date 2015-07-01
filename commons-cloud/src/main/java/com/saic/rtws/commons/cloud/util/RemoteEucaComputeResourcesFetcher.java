package com.saic.rtws.commons.cloud.util;

import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.saic.rtws.commons.cloud.CloudProvider;
import com.saic.rtws.commons.cloud.environment.monitor.representations.Instance;
import com.saic.rtws.commons.cloud.environment.monitor.representations.InstanceTypeDetails;
import com.saic.rtws.commons.jersey.config.JerseyClientConfig;
import com.sun.jersey.api.client.Client;

public class RemoteEucaComputeResourcesFetcher implements ComputeResourcesFetcher {

	private Logger logger = Logger.getLogger(getClass());

	private URL url;

	public RemoteEucaComputeResourcesFetcher(URL url) {
		super();
		this.url = url;
	}

	@Override
	public List<Instance> compute() {
		List<Instance> instances = new LinkedList<Instance>();
		Client client = null;

		try {
			client = Client.create(JerseyClientConfig.getInstance().getInternalConfig());

			String response = client.resource(url.toString()).path("retrive").path(CloudProvider.EUCA.toString()).path("resources").get(String.class);
			if (response != null) {
				JSONObject json = JSONObject.fromObject(response);
				JSONArray jsonArr = json.getJSONArray("instances");
				for (Object object : jsonArr) {
					JSONObject obj = (JSONObject) object;
					JSONObject typeDetailsObj = obj.getJSONObject("typeDetails");
					Instance instance = new Instance(obj.getString("type"), obj.getInt("free"), obj.getInt("used"), obj.getInt("max"));
					InstanceTypeDetails details = new InstanceTypeDetails(typeDetailsObj.getInt("cpus"), typeDetailsObj.getInt("memoryInMb"), 0);
					instance.setTypeDetails(details);
					instances.add(instance);
				}
			}
		} catch (JSONException e) {
			logger.error(e);
		} finally {

			if (client != null)
				client.destroy();
		}

		return instances;
	}
}
