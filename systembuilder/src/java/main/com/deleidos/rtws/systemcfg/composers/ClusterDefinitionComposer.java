package com.deleidos.rtws.systemcfg.composers;

import java.io.InputStream;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import javax.xml.bind.MarshalException;

import com.deleidos.rtws.commons.cloud.beans.ProcessDefinition;
import com.deleidos.rtws.master.core.beans.ClusterDefinition;
import com.deleidos.rtws.master.core.beans.ProcessAllocationPolicy;
import com.deleidos.rtws.master.core.beans.ProcessGroup;
import com.deleidos.rtws.systemcfg.beans.SystemConfig;
import com.deleidos.rtws.systemcfg.beans.servercluster.ServerCluster;
import com.deleidos.rtws.systemcfg.beans.servercluster.policy.AllocationPolicy;
import com.deleidos.rtws.systemcfg.beans.servercluster.serverconfig.IaasServerConfig;

public class ClusterDefinitionComposer extends AbstractDefinitionComposer implements DefinitionComposer {
	
	private final static String subZone = "c";
	private File defaultInputFile;
	private ClusterDefinition clusterDefinition;
	
	public void initialize() {
		serializer.initialize();
	}
	
	
	public void writeFile(String version, String fileName) {
		
	}
	
	public void loadDefaults(InputStream stream) throws MarshalException {
		clusterDefinition = serializer.createObject(defaultInputFile, ClusterDefinition.class);
	}
	
	public String compose(SystemConfig systemConfig) {
		clusterDefinition.setDomainName(systemConfig.getDomain());
		if (systemConfig.getServerClusters() != null) { 
			int groupSize = systemConfig.getServerClusters().size();
			if(groupSize > 0) {
				ProcessGroup[] groups = new ProcessGroup[groupSize];
				List<ServerCluster> clusters = systemConfig.getServerClusters();
				for(int i=0; i < groupSize; i++) {
					ServerCluster serverCluster = clusters.get(i);
					ProcessGroup group = new ProcessGroup();
					group.setName(serverCluster.getName());
					
					ProcessDefinition definition = new ProcessDefinition();
					IaasServerConfig config = serverCluster.getIaasServerConfig();  
					definition.setImage(config.getImageId());
					definition.setType(config.getSize().toString());
					definition.setZone(config.getPreferredRegion() + "-" + config.getPreferredZone() + subZone);
					
					ProcessAllocationPolicy processPolicy = new ProcessAllocationPolicy();
					AllocationPolicy allocationPolicy = serverCluster.getAllocationPolicy();
					processPolicy.setMin(allocationPolicy.getMaxServers());
					processPolicy.setMax(allocationPolicy.getMaxServers());
					processPolicy.setFqdn(serverCluster.getClusterSubDomain() + "." + systemConfig.getDomain());
					
					group.setAllocationPolicy(processPolicy);
					group.setDefinition(definition);
					groups[i] = group;
				}	
				clusterDefinition.setProcessGroups(groups);
			}	
		}
		
		try {
			serializer.createDefinitionFile(clusterDefinition, new File("temp"));
		} catch(MarshalException me) {
			System.out.println("Marshal Error: " + me.getMessage());
		}
		return "done";
	}
	
	public void dispose() {
		serializer.dispose();
	}
	
	public void setDefaultInputFile(File defaultInputFile) {
		this.defaultInputFile = defaultInputFile;
	}
	
	public File getDefaultInputFile() {
		return defaultInputFile;
	}
}
