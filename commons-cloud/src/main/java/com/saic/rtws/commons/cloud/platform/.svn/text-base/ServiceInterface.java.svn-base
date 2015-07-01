package com.saic.rtws.commons.cloud.platform;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.amazonaws.services.ec2.model.AttachVolumeResult;
import com.amazonaws.services.ec2.model.CreateVolumeResult;
import com.amazonaws.services.ec2.model.Instance;
import com.saic.rtws.commons.cloud.ProcessState;
import com.saic.rtws.commons.cloud.beans.FirewallGroup;
import com.saic.rtws.commons.cloud.beans.Process;
import com.saic.rtws.commons.cloud.beans.ProcessDefinition;
import com.saic.rtws.commons.cloud.beans.Snapshot;
import com.saic.rtws.commons.cloud.beans.Volume;
import com.saic.rtws.commons.cloud.beans.vpc.NetworkACL;
import com.saic.rtws.commons.cloud.beans.vpc.PortRange;
import com.saic.rtws.commons.cloud.beans.vpc.RouteTable;
import com.saic.rtws.commons.cloud.beans.vpc.Subnet;

@XmlJavaTypeAdapter(ServiceInterface.TypeAdapter.class)
public interface ServiceInterface {

	public Process self();

	public Process self(Process process);

	public ProcessState status(Process bean);

	public String getUserData(String id);

	public void setUserData(String id, String userData);
	
	public List<String> getAvailabilityZones();

	public Map<String, List<String>> getRegionAzoneMapping();

	public String create(ProcessDefinition bean);

	public void create(ProcessDefinition bean, Process process);

	public void start(String id);

	public void stop(String id);

	public void terminate(Process bean, boolean retain, boolean wait);

	public void terminateInstance(String id);

	public void monitorProcessStateChange(Collection<Process> processes,
			boolean timeOutInfinity);

	public String bind(Process process, String previousPersistentIpAddress,
			String templatePersistentIpAddress);

	public void assignAttributes(String id, Properties attributes);

	public void assignAttributes(Process process, Properties attributes);

	public void forceVolumesDetach(Volume [] volumes);
	
	public void forceVolumeDetach(Volume volume);
	
	public void deleteVolumes(Volume [] volumes);

	public void deleteVolume(Volume volume);

	public CreateVolumeResult createVolume(Volume volume, String zone);

	public List<Snapshot> listAllSnapshots();

	public Snapshot createSnapshot(String volumeId, String description);

	public void deleteSnapshot(String snapshotId);

	public AttachVolumeResult attachVolume(CreateVolumeResult volumeResult,
			String instanceId, String mountDevice, String prefixNum);

	public AttachVolumeResult attachVolume(Volume volume, String instanceId);

	public Instance describeInstance(String instanceId);

	public Process listProcess(String id);
	
	public Process listProcessWithoutVolumes(String id);
	
	public Collection<Process> listProcessWithoutVolumes(List<String> ids);

	public Collection<Process> listAllProcesses();
	
	public List<Instance> listAllRunningInstances();

	public Map<String, Process> listProcessById();
	
	public Map<String, Process> listProcessByIdWithoutVolumes();

	public Collection<Volume> listVolumes(List<String> volumeIds);
	
	public Map<String, Volume> listVolumesById(List<String> volumeIds);
	
	public Collection<Volume> listAllVolumes();

	public Map<String, Volume> listAllVolumesById();

	public Volume listVolume(String volumeId);

	public Collection<FirewallGroup> listFirewallGroups();
	
	public Map<String, FirewallGroup> listFirewallGroupsById();
	
	public Map<String, FirewallGroup> listFirewallGroupsByName();
	
	public String createFirewallGroup(String name, String description, String vpcId);

	public void deleteFirewallGroup(String name);
	
	public void deleteFirewallGroup(String name, String vpcId);

	public void createFirewallRuleByIpPermissionWithGroupId(String groupId,
			String protocol, int fromPort, int toPort, String ipRange);
	
	public void createFirewallRuleByIpPermission(String groupName,
			String protocol, int fromPort, int toPort, String ipRange);

	public void createFirewallRuleByUserIdGroupWithGroupId(String groupId,
			String protocol, int fromPort, int toPort, String userId,
			String userIdGroupName);
	
	public void createFirewallRuleByUserIdGroup(String groupName,
			String protocol, int fromPort, int toPort, String userId,
			String userIdGroupName);

	public void deleteFirewallRuleByIpPermissionWithGroupId(String groupId,
			String protocol, int fromPort, int toPort, String source);
	
	public void deleteFirewallRuleByIpPermission(String groupName,
			String protocol, int fromPort, int toPort, String source);

	public void deleteFirewallRuleByUserIdGroupWithGroupId(String groupId,
			String protocol, int fromPort, int toPort, String userId,
			String userIdGroupName);
	
	public void deleteFirewallRuleByUserIdGroup(String groupName,
			String protocol, int fromPort, int toPort, String userId,
			String userIdGroupName);

	public String getInstanceId();

	public List<String> getExpectedVolumeIds();

	public List<String> getExpectedVolumeIds(String instanceId);
	
	public boolean waitForVolumeAvailability(List<String> volumeIds, boolean checkIndefinitely);
	
	public boolean waitForVolumeAttachment(List<String> volumeIds, boolean checkIndefinitely);
	
	public boolean waitForVolumeDetachment(List<String> volumeIds, boolean checkIndefinitely);
	
	public Collection<String> getVpcIds();
	
	public Map<String, String> getVpcsCidrBlock(List<String> vpcIds);
	
	public String associateRouteTable(String routeTableId, String subnetId);
	
	public void attachInternetGateway(String internetGatewayId, String vpcId);
	
	public void detachInternetGateway(String internetGatewayId, String vpcId);
	
	public String createVPC(String cidrBlock, String instanceTenancy);
	
	public String createSubnet(String availabilityZone, String cidrBlock, String vpcId);
	
	public Map<String, String> getSubnetsCidrBlock(List<String> subnetIds);
	
	public String createInternetGateway();
	
	public String getInternetGatewayId(String vpcId);
	
	public String createNetworkAcl(String vpcId);
	
	public void deleteInternetGateway(String internetGatewayId);
	
	public void createNetworkAclEntry(String cidrBlock, boolean egress, String networkAclId, PortRange portRange, String protocol,
			String ruleAction, int ruleNumber);
	
	public void createRoute(String destinationCidrBlock, String instanceId, String gatewayId, String routeTableId);
	
	public String createRouteTable(String vpcId);
	
	public void deleteNetworkAcl(String networkAclId);
	
	public void deleteNetworkAclEntry(String networkAclId, int ruleNumber);
	
	public void deleteRoute(String destinationCidrBlock, String routeTableId);
	
	public void deleteRouteTable(String routeTableId);
	
	public void deleteSubnet(String subnetId);
	
	public void deleteVpc(String vpcId);
	
	public List<NetworkACL> describeNetworkAcls();
	
	public List<RouteTable> describeRouteTables();
	
	public List<RouteTable> describeRouteTables(String vpcId, Boolean onlyMain);
	
	public List<Subnet> describeSubnets();
	
	public List<com.saic.rtws.commons.cloud.beans.vpc.Vpc> describeVpcs();
	
	public com.saic.rtws.commons.cloud.beans.vpc.Vpc describeVpc(String vpcId);
	
	public void disassociateRouteTable(String associationId);
	
	public String replaceNetworkAclAssociation(String associationId, String networkAclId);
	
	public void replaceNetworkAclEntry(String cidrBlock, boolean egress, String networkAclId, PortRange portRange, String protocol,
			String ruleAction, int ruleNumber);
	
	public void replaceRoute(String destinationCidrBlock, String gatewayId, String instanceId, String routeTableId);
	
	public String replaceRouteTableAssociation(String associationId, String routeTableId);
	
	public void setSrcDestCheckAttribute(String instanceId, Boolean value);
	
	public Map<String, String> describeElasticAddresses();
	
	public String allocateElasticAddress();
	
	public String allocateElasticAddress(boolean vpcEnabled);
	
	public String associateElasticAddress(String instanceId, String elasticAddress);
	
	public void disassociateElasticAddress(String associationId);
	
	public void releaseElasticAddress(String releasableValue);
	
	public void shareAmi(String amiId, List<String> accountIds);
	
	static class TypeAdapter extends XmlAdapter<Object, ServiceInterface> {
		public ServiceInterface unmarshal(Object element) {
			return (ServiceInterface) element;
		}

		public Object marshal(ServiceInterface bean) {
			return bean;
		}
	}
	
	public String retrieveServiceEndpoint();
	
}
