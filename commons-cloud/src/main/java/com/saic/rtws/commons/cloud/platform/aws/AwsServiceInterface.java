package com.saic.rtws.commons.cloud.platform.aws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.Address;
import com.amazonaws.services.ec2.model.AllocateAddressRequest;
import com.amazonaws.services.ec2.model.AssociateAddressRequest;
import com.amazonaws.services.ec2.model.AssociateRouteTableRequest;
import com.amazonaws.services.ec2.model.AssociateRouteTableResult;
import com.amazonaws.services.ec2.model.AttachInternetGatewayRequest;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateInternetGatewayResult;
import com.amazonaws.services.ec2.model.CreateNetworkAclEntryRequest;
import com.amazonaws.services.ec2.model.CreateNetworkAclRequest;
import com.amazonaws.services.ec2.model.CreateNetworkAclResult;
import com.amazonaws.services.ec2.model.CreateRouteRequest;
import com.amazonaws.services.ec2.model.CreateRouteTableRequest;
import com.amazonaws.services.ec2.model.CreateRouteTableResult;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupResult;
import com.amazonaws.services.ec2.model.CreateSubnetRequest;
import com.amazonaws.services.ec2.model.CreateSubnetResult;
import com.amazonaws.services.ec2.model.CreateVpcRequest;
import com.amazonaws.services.ec2.model.CreateVpcResult;
import com.amazonaws.services.ec2.model.DeleteInternetGatewayRequest;
import com.amazonaws.services.ec2.model.DeleteNetworkAclEntryRequest;
import com.amazonaws.services.ec2.model.DeleteNetworkAclRequest;
import com.amazonaws.services.ec2.model.DeleteRouteRequest;
import com.amazonaws.services.ec2.model.DeleteRouteTableRequest;
import com.amazonaws.services.ec2.model.DeleteSecurityGroupRequest;
import com.amazonaws.services.ec2.model.DeleteSubnetRequest;
import com.amazonaws.services.ec2.model.DeleteVpcRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.DescribeInternetGatewaysResult;
import com.amazonaws.services.ec2.model.DescribeNetworkAclsResult;
import com.amazonaws.services.ec2.model.DescribeRouteTablesRequest;
import com.amazonaws.services.ec2.model.DescribeRouteTablesResult;
import com.amazonaws.services.ec2.model.DescribeSubnetsRequest;
import com.amazonaws.services.ec2.model.DescribeSubnetsResult;
import com.amazonaws.services.ec2.model.DescribeVpcsRequest;
import com.amazonaws.services.ec2.model.DescribeVpcsResult;
import com.amazonaws.services.ec2.model.DetachInternetGatewayRequest;
import com.amazonaws.services.ec2.model.DisassociateRouteTableRequest;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InternetGateway;
import com.amazonaws.services.ec2.model.InternetGatewayAttachment;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.ModifyInstanceAttributeRequest;
import com.amazonaws.services.ec2.model.NetworkAcl;
import com.amazonaws.services.ec2.model.NetworkAclEntry;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.ReplaceNetworkAclAssociationRequest;
import com.amazonaws.services.ec2.model.ReplaceNetworkAclAssociationResult;
import com.amazonaws.services.ec2.model.ReplaceNetworkAclEntryRequest;
import com.amazonaws.services.ec2.model.ReplaceRouteRequest;
import com.amazonaws.services.ec2.model.ReplaceRouteTableAssociationRequest;
import com.amazonaws.services.ec2.model.ReplaceRouteTableAssociationResult;
import com.amazonaws.services.ec2.model.RevokeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.RunInstancesRequest;
import com.amazonaws.services.ec2.model.RunInstancesResult;
import com.amazonaws.services.ec2.model.UserIdGroupPair;
import com.amazonaws.services.ec2.model.Vpc;
import com.saic.rtws.commons.cloud.beans.FirewallGroup;
import com.saic.rtws.commons.cloud.beans.Process;
import com.saic.rtws.commons.cloud.beans.ProcessDefinition;
import com.saic.rtws.commons.cloud.beans.vpc.NetworkACL;
import com.saic.rtws.commons.cloud.beans.vpc.NetworkACLEntry;
import com.saic.rtws.commons.cloud.beans.vpc.PortRange;
import com.saic.rtws.commons.cloud.beans.vpc.Route;
import com.saic.rtws.commons.cloud.beans.vpc.RouteTable;
import com.saic.rtws.commons.cloud.beans.vpc.Subnet;

/**
 * An extension of the SimpleAwsServiceImpl that will work with standard AWS EC2
 * but will also work with Virtual Private Cloud (VPC) functions of AWS.
 */
public class AwsServiceInterface extends SimpleAwsServiceImpl {

	public AwsServiceInterface() {
		super();
	}

	/**
	 * Create a firewall group (aka security group)
	 */
	@Override
	public String createFirewallGroup(String name, String description, String vpcId) {

		String groupId = null;
		Collection<FirewallGroup> existingGroups = listFirewallGroups();
		for (FirewallGroup eGrps : existingGroups) {
			if (vpcId != null && eGrps.getVpcId() != null &&
				vpcId.equals(eGrps.getVpcId()) && eGrps.getName().equals(name)){
				groupId = eGrps.getId();
				break;
			}
			
			if (vpcId == null && eGrps.getVpcId() == null && eGrps.getName().equals(name)) {
				groupId = eGrps.getId();
				break;
			}
		}

		if (groupId == null) {
			AmazonEC2Client client = getConnectionFactory().getAmazonEC2Client();
			CreateSecurityGroupRequest request = new CreateSecurityGroupRequest(name, description);
			if (StringUtils.isNotBlank(vpcId)) {
				request.setVpcId(vpcId);
			}
			CreateSecurityGroupResult result = client.createSecurityGroup(request);
			groupId = result.getGroupId();
		}

		return groupId;

	}

	/**
	 * Create a firewall rule (aka security rule) using a ip range as the
	 * source.
	 */
	@Override
	public void createFirewallRuleByIpPermission(String groupName,
			String protocol, int fromPort, int toPort, String ipRange) {
		AmazonEC2Client client = null;
		try {
			client = getConnectionFactory().getAmazonEC2Client();

			String firewallGroupId = getFirewallGroupIdByName(groupName);

			IpPermission permission = new IpPermission();
			permission.withIpProtocol(protocol)
				.withFromPort(fromPort)
				.withToPort(toPort)
				.withIpRanges(ipRange);

			AuthorizeSecurityGroupIngressRequest request = new AuthorizeSecurityGroupIngressRequest();
			request.withGroupId(firewallGroupId).withIpPermissions(permission);

			client.authorizeSecurityGroupIngress(request);
		} catch (AmazonServiceException e) {
			if (!e.getErrorCode().equals("InvalidPermission.Duplicate")) {
				throw e;
			}
		} finally {
			if (client != null)
				client.shutdown();
		}

	}

	/**
	 * Create a firewall rule (aka security rule) using another security group
	 * as the source.
	 */
	@Override
	public void createFirewallRuleByUserIdGroup(String groupName,
			String protocol, int fromPort, int toPort, String userId,
			String userIdGroupName) {
		AmazonEC2Client client = null;
		try {
			client = getConnectionFactory().getAmazonEC2Client();

			String firewallGroupId = getFirewallGroupIdByName(groupName);
			String userIdGroupId = getFirewallGroupIdByName(userIdGroupName);

			UserIdGroupPair pair = new UserIdGroupPair();
			pair.setUserId(userId);
			
			if (userIdGroupId.startsWith("sg-")) 
			{
				pair.setGroupId(userIdGroupId);
			}
			else
			{
				pair.setGroupName(userIdGroupName);
			}

			IpPermission permission = new IpPermission();
			permission.setIpProtocol(protocol);
			permission.setFromPort(fromPort);
			permission.setToPort(toPort);
			permission.setUserIdGroupPairs(Collections.singletonList(pair));

			AuthorizeSecurityGroupIngressRequest request = new AuthorizeSecurityGroupIngressRequest();
			request.withGroupId(firewallGroupId).withIpPermissions(permission);

			client.authorizeSecurityGroupIngress(request);

		} catch (AmazonServiceException e) {
			if (!e.getErrorCode().equals("InvalidPermission.Duplicate")) {
				throw e;
			}
		} finally {
			if (client != null)
				client.shutdown();
		}

	}

	/**
	 * Delete a firewall group (aka security group)
	 */
	@Override
	public void deleteFirewallGroup(String name) {

		AmazonEC2Client client = getConnectionFactory().getAmazonEC2Client();

		String firewallGroupId = getFirewallGroupIdByName(name);

		DeleteSecurityGroupRequest deleteSecurityGroupRequest =
				new DeleteSecurityGroupRequest().withGroupId(firewallGroupId);
		client.deleteSecurityGroup(deleteSecurityGroupRequest);

	}

	/**
	 * Delete a firewall rule (aka security rule) using a ip range as the
	 * source.
	 */
	@Override
	public void deleteFirewallRuleByIpPermission(String groupName,
			String protocol, int fromPort, int toPort, String ipRange) {

		AmazonEC2Client client = getConnectionFactory().getAmazonEC2Client();

		String firewallGroupId = getFirewallGroupIdByName(groupName);

		IpPermission permission = new IpPermission();
		permission.withIpProtocol(protocol)
			.withFromPort(fromPort)
			.withToPort(toPort)
			.withIpRanges(ipRange);

		RevokeSecurityGroupIngressRequest request = new RevokeSecurityGroupIngressRequest();
		request.withGroupId(firewallGroupId).withIpPermissions(permission);

		client.revokeSecurityGroupIngress(request);

	}

	/**
	 * Delete a firewall rule (aka security rule) using another security group
	 * as the source.
	 */
	@Override
	public void deleteFirewallRuleByUserIdGroup(String groupName,
			String protocol, int fromPort, int toPort, String userId,
			String userIdGroupName) {

		AmazonEC2Client client = getConnectionFactory().getAmazonEC2Client();

		String firewallGroupId = getFirewallGroupIdByName(groupName);
		String userIdGroupId = getFirewallGroupIdByName(userIdGroupName);

		UserIdGroupPair pair = new UserIdGroupPair();
		pair.setUserId(userId);
		pair.setGroupId(userIdGroupId);

		IpPermission permission = new IpPermission();
		permission.setIpProtocol(protocol);
		permission.setFromPort(fromPort);
		permission.setToPort(toPort);
		permission.setUserIdGroupPairs(Collections.singletonList(pair));

		RevokeSecurityGroupIngressRequest request = new RevokeSecurityGroupIngressRequest();
		request.withGroupId(firewallGroupId).withIpPermissions(permission);

		client.revokeSecurityGroupIngress(request);

	}


	
	/**
	 * Requests EC2 to allocate a new Elastic IP address to the given account.
	 */
	protected String allocateAddress(AmazonEC2Client client, Process process) {
		String newPublicIp;
		if (StringUtils.isNotBlank(process.getVpcId()) || StringUtils.isNotBlank(process.getSubnetId())) {
			AllocateAddressRequest request = new AllocateAddressRequest().withDomain("vpc");
			newPublicIp = client.allocateAddress(request).getPublicIp();
		}
		else {
			newPublicIp = client.allocateAddress().getPublicIp();
		}
		return newPublicIp;
	}

	/**
	 * Requests that EC2 assign the given Elastic IP address to the given AMI
	 * instance. Will fail if the elastic IP has not been reserved or the id is
	 * invalid.
	 */
	protected void associateAddress(AmazonEC2Client client, String id,
			String address) {
		Address addr = describeAddress(new Address().withPublicIp(address));
		if (addr != null) {
			AssociateAddressRequest request = new AssociateAddressRequest();
			request.setInstanceId(id);
			if (addr.getAllocationId() != null) {
				request.setAllocationId(addr.getAllocationId());
			}
			else {
				request.setPublicIp(address);
			}
			client.associateAddress(request);
		}
	}

	/**
	 * Lookup a firewall group ID (security group ID) from its name.
	 */
	protected String getFirewallGroupIdByName(String fwGroupName) {
		FirewallGroup fwGroup = listFirewallGroupsByName().get(fwGroupName);
		if (fwGroup != null) {
			return fwGroup.getId();
		}
		return fwGroupName;
	}
	
	/**
	 * Populates the given Process object with meta-data about the given AMI
	 * instance.
	 * 
	 * Note that EC2 returns a public IP address value regardless of whether the
	 * instance has an elastic address; however it does not give any way to tell
	 * whether the address is static or dynamic. So don't count on that value
	 * being the same after a restart.  This is NOT TRUE in a VPC, where the only
	 * public IP address is an elastic one.
	 * 
	 * Note that EC2 occasionally returns bogus results, such as a response that
	 * does not include a private IP address. Since this is essentially invalid,
	 * and the clients of this service need to count on valid addressing
	 * information being provided, it is necessary to repeat the request until
	 * the results make sense. It is not clear what causes this, or how long the
	 * condition generally persists, but it seems to iron itself out eventually.
	 * 
	 * @param client
	 *            The connection over which to send the request.
	 * @param id
	 *            The instance ID of the instance to be described.
	 * @param process
	 *            The object to be populated.
	 */
	protected String describeInstance(AmazonEC2Client client, String id, Process process) {

		String state = null;

		do {

			DescribeInstancesRequest request = new DescribeInstancesRequest();
			request.setInstanceIds(Collections.singleton(id));

			DescribeInstancesResult result = client.describeInstances(request);
			Instance instance = extractSingleInstance(result);

			if (instance != null) {
				process.setId(instance.getInstanceId());
				process.setPrivateDnsName(instance.getPrivateDnsName());
				process.setPublicDnsName(instance.getPublicDnsName());
				process.setPrivateIpAddress(resolveAddress(
						instance.getPrivateDnsName(),
						instance.getPrivateIpAddress()));
				process.setPublicIpAddress(resolveAddress(
						instance.getPublicDnsName(),
						instance.getPublicIpAddress()));
				process.setVpcId(instance.getVpcId());
				process.setSubnetId(instance.getSubnetId());
				process.setImageId(instance.getImageId());
				state = instance.getState().getName();
			} else {
				// Technically this shouldn't happen; if you tried to describe an instance
				// that doesn't exist, amazon should throw an exception. But just in case
				// it instead returns a result with zero entries, treat it as an error.
				throw new AmazonServiceException("Instance does not exist '" + id + "'.");
			}

		} while (isInitializing(state) && process.getPrivateIpAddress() == null);

		return state;

	}
	
	/**
	 * Requests that EC2 create a new AMI instance. It will be created in a VPC
	 * subnet if one is specified in the template.
	 * 
	 * @param client
	 *            The connection over which to send the request.
	 * @param template
	 *            The information needed to create the new instance.
	 * 
	 * @return The AMI ID of the new instance.
	 */
	protected String runInstance(AmazonEC2Client client,
			ProcessDefinition template) {

		RunInstancesRequest request = new RunInstancesRequest();
		request.setImageId(template.getImage());
		request.setInstanceType(template.getType());
		request.setPlacement(new Placement().withAvailabilityZone(template.getZone()));
		request.setKeyName(template.getKey());
		request.setKernelId(template.getKernel());
		request.setRamdiskId(template.getRamdisk());
		
		if (template.getSecurity() != null) {
			request.setSecurityGroups(Collections.singletonList(template.getSecurity()));
		} else {
			request.setSecurityGroupIds(Collections.singletonList(template.getSecurityId()));
		}
		
		request.setMinCount(1);
		request.setMaxCount(1);
		request.setUserData(buildUserData(template.getProperties()));
		if (StringUtils.isNotBlank(template.getSubnet())) {
			request.setSubnetId(template.getSubnet());
		}

		RunInstancesResult result = client.runInstances(request);
		Instance instance = extractSingleInstance(result);

		return instance.getInstanceId();

	}
	
	@Override
	public Collection<String> getVpcIds() {
		DescribeVpcsResult result = getConnectionFactory().getAmazonEC2Client().describeVpcs();
		ArrayList<String> vpcIds = new ArrayList<String>();
		for (Vpc vpc : result.getVpcs()) {
			vpcIds.add(vpc.getVpcId());
		}
		return vpcIds;
	}
	
	@Override
	public Map<String, String> getVpcsCidrBlock(List<String> vpcIds) {
		
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DescribeVpcsRequest request = new DescribeVpcsRequest();
		request.setVpcIds(vpcIds);
		
		DescribeVpcsResult result = client.describeVpcs(request);
		
		Map<String, String> cidrBlocks = new HashMap<String, String>();
		for (Vpc vpc : result.getVpcs()) {
			cidrBlocks.put(vpc.getVpcId(), vpc.getCidrBlock());
		}
		
		return cidrBlocks;
		
	}

	/**
	 * Associates a subnet with a route table.
	 * 
	 * @param routeTableId {@link String} route table id
	 * @param subnetId {@link String} subnet id
	 * @return {@link String} association id
	 */
	public String associateRouteTable(String routeTableId, String subnetId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		AssociateRouteTableRequest associateRouteTableRequest = new AssociateRouteTableRequest();
		associateRouteTableRequest.setRouteTableId(routeTableId);
		associateRouteTableRequest.setSubnetId(subnetId);
		
		AssociateRouteTableResult associateRouteTableResult = client.associateRouteTable(associateRouteTableRequest);
		
		return associateRouteTableResult.getAssociationId();
	}
	
	/**
	 * Attaches an Internet Gateway to a VPC.
	 * 
	 * @param internetGatewayId {@link String} id
	 * @param vpcId {@link String} id
	 */
	public void attachInternetGateway(String internetGatewayId, String vpcId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		AttachInternetGatewayRequest attachInternetGatewayRequest = new AttachInternetGatewayRequest();
		
		attachInternetGatewayRequest.setInternetGatewayId(internetGatewayId);
		attachInternetGatewayRequest.setVpcId(vpcId);
		
		client.attachInternetGateway(attachInternetGatewayRequest);
	}
	
	/**
	 * Detaches an Internet Gateway to a VPC.
	 * 
	 * @param internetGatewayId {@link String} id
	 * @param vpcId {@link String} id
	 */
	public void detachInternetGateway(String internetGatewayId, String vpcId) {
		
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DetachInternetGatewayRequest detachInternetGatewayRequest = new DetachInternetGatewayRequest();
		
		detachInternetGatewayRequest.setInternetGatewayId(internetGatewayId);
		detachInternetGatewayRequest.setVpcId(vpcId);
		
		client.detachInternetGateway(detachInternetGatewayRequest);
		
	}
	
	/**
	 * Create a VPC.
	 * 
	 * @param cidrBlock {@link String} CIDR notation block
	 * @param instanceTenancy {@link String} instance tenancy
	 * @return {@link String} vpc id
	 */
	public String createVPC(String cidrBlock, String instanceTenancy){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		CreateVpcRequest createVpcRequest = new CreateVpcRequest();
		
		createVpcRequest.setCidrBlock(cidrBlock);  //cidr block notation
		createVpcRequest.setInstanceTenancy(instanceTenancy);  //options:  Default or Dedicated
		
		CreateVpcResult createVpcResult = client.createVpc(createVpcRequest);
		
		return createVpcResult.getVpc().getVpcId();
	}
	
	@Override
	public Map<String, String> getSubnetsCidrBlock(List<String> subnetIds) {
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DescribeSubnetsRequest request = new DescribeSubnetsRequest();
		request.setSubnetIds(subnetIds);
		
		DescribeSubnetsResult result = client.describeSubnets(request);
		
		Map<String, String> cidrBlocks = new HashMap<String, String>();
		for (com.amazonaws.services.ec2.model.Subnet subnet : result.getSubnets()) {
			cidrBlocks.put(subnet.getSubnetId(), subnet.getCidrBlock());
		}
		
		return cidrBlocks;
	}
	
	/**
	 * Creates a subnet in an existing VPC.
	 * 
	 * @param availabilityZone {@link String} availability zone
	 * @param cidrBlock {@link String} cidr block notation
	 * @param vpcId {@link String} VPC Id
	 * @return {@link String} subnet Id
	 */
	public String createSubnet(String availabilityZone, String cidrBlock, String vpcId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		CreateSubnetRequest createSubnetRequest = new CreateSubnetRequest();
		
		createSubnetRequest.setAvailabilityZone(availabilityZone);
		createSubnetRequest.setCidrBlock(cidrBlock);
		createSubnetRequest.setVpcId(vpcId);
		
		CreateSubnetResult createSubnetResult = client.createSubnet(createSubnetRequest);
		
		return createSubnetResult.getSubnet().getSubnetId();
	}
	
	public String getInternetGatewayId(String vpcId) {
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DescribeInternetGatewaysResult result = client.describeInternetGateways();
		
		for (InternetGateway gateway : result.getInternetGateways()) {
			for (InternetGatewayAttachment attachment : gateway.getAttachments()) {
				if (vpcId.equals(attachment.getVpcId())) {
					return gateway.getInternetGatewayId();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Creates a new Internet Gateway that can be attached to a VPC.
	 * 
	 * @return {@link String} internetGatewayId
	 */
	public String createInternetGateway(){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		CreateInternetGatewayResult createInternetGatewayResult = client.createInternetGateway();
		
		return createInternetGatewayResult.getInternetGateway().getInternetGatewayId();
	}
	
	/**
	 * Creates a new Network ACL in a VPC.
	 * 
	 * @param vpcId {@link String} VPC Id
	 * @return {@link String} networkAclId
	 */
	public String createNetworkAcl(String vpcId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		CreateNetworkAclRequest createNetworkAclRequest = new CreateNetworkAclRequest();
		
		createNetworkAclRequest.setVpcId(vpcId);
		
		CreateNetworkAclResult createNetworkAclResult = client.createNetworkAcl(createNetworkAclRequest);
		
		return createNetworkAclResult.getNetworkAcl().getNetworkAclId();
	}
	
	/**
	 * Creates a rule in a network ACL with the provided rule number.
	 * @param cidrBlock {@link String} cidrBlock notation
	 * @param egress {@link boolean} egress traffic true, ingress traffic false
	 * @param networkAclId {@link String} networkAclId
	 * @param portRange {@link PortRange} portRange
	 * @param protocol {@link String} protocol
	 * @param ruleAction {@link ruleAction} possible values allow or deny
	 * @param ruleNumber {@link ruleNumber} ruleNumber to assign to entry
	 */
	public void createNetworkAclEntry(String cidrBlock, boolean egress, String networkAclId, PortRange portRange, String protocol,
											String ruleAction, int ruleNumber){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		CreateNetworkAclEntryRequest createNetworkAclEntryRequest = new CreateNetworkAclEntryRequest();
		
		createNetworkAclEntryRequest.setCidrBlock(cidrBlock);
		createNetworkAclEntryRequest.setEgress(egress);
		createNetworkAclEntryRequest.setNetworkAclId(networkAclId);
		
		com.amazonaws.services.ec2.model.PortRange awsPortRange = new com.amazonaws.services.ec2.model.PortRange();
		awsPortRange.setFrom(portRange.getFrom());
		awsPortRange.setTo(portRange.getTo());
		
		createNetworkAclEntryRequest.setPortRange(awsPortRange);
		createNetworkAclEntryRequest.setProtocol(protocol);
		createNetworkAclEntryRequest.setRuleAction(ruleAction);
		createNetworkAclEntryRequest.setRuleNumber(ruleNumber);
		
		client.createNetworkAclEntry(createNetworkAclEntryRequest);
	}
	
	/**
	 * Creates a new route within a route table in a VPC.
	 * 
	 * @param destinationCidrBlock {@link String} designation cidr block
	 * @param instanceId {@link String} The ID of a NAT instance in your VPC. You must provide either GatewayId or InstanceId, but not both.
	 * @param gatewayId {@link String} The ID of a NAT instance in your VPC. You must provide either GatewayId or InstanceId, but not both.
	 * @param routeTableId {@link String} route table id
	 */
	public void createRoute(String destinationCidrBlock, String instanceId, String gatewayId, String routeTableId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		CreateRouteRequest createRouteRequest = new CreateRouteRequest();
		createRouteRequest.setDestinationCidrBlock(destinationCidrBlock);
		
		//The ID of a NAT instance in your VPC. You must provide either GatewayId or InstanceId, but not both.
		if(StringUtils.isNotBlank(instanceId) == true){
			createRouteRequest.setInstanceId(instanceId);
		}
		
		if(StringUtils.isNotBlank(gatewayId) == true){
			createRouteRequest.setGatewayId(gatewayId);
		}
		
		createRouteRequest.setRouteTableId(routeTableId);
		
		client.createRoute(createRouteRequest);
	}
	
	/**
	 * Creates a route table within a VPC.
	 * 
	 * @param vpcId {@link String} vpc id
	 * @return{@link String} route table id
	 */
	public String createRouteTable(String vpcId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		CreateRouteTableRequest createRouteTableRequest = new CreateRouteTableRequest();
		
		createRouteTableRequest.setVpcId(vpcId);
		
		CreateRouteTableResult createRouteTableResult = client.createRouteTable(createRouteTableRequest);
		
		return createRouteTableResult.getRouteTable().getRouteTableId();
	}
	
	/**
	 * Deletes an internet gateway from an AWS account.
	 * 
	 * @param internetGatewayId {@link String} internetGatewayId
	 */
	public void deleteInternetGateway(String internetGatewayId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DeleteInternetGatewayRequest deleteInternetGatewayRequest = new DeleteInternetGatewayRequest();
		
		deleteInternetGatewayRequest.setInternetGatewayId(internetGatewayId);
		
		client.deleteInternetGateway(deleteInternetGatewayRequest);
	}
	
	/**
	 * Delete a network ACL from a VPC.
	 * 
	 * @param networkAclId {@link String} networkAclId
	 */
	public void deleteNetworkAcl(String networkAclId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DeleteNetworkAclRequest deleteNetworkAclRequest = new DeleteNetworkAclRequest();
		
		deleteNetworkAclRequest.setNetworkAclId(networkAclId);
		
		client.deleteNetworkAcl(deleteNetworkAclRequest);
	}
	
	/**
	 * Deletes an ingress or egress rule from a network ACL.
	 * 
	 * @param networkAclId {@link String} networkAclId
	 * @param ruleNumber {@link int} ruleNumber
	 */
	public void deleteNetworkAclEntry(String networkAclId, int ruleNumber){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DeleteNetworkAclEntryRequest deleteNetworkAclEntryRequest = new DeleteNetworkAclEntryRequest();
		
		deleteNetworkAclEntryRequest.setNetworkAclId(networkAclId);
		deleteNetworkAclEntryRequest.setRuleNumber(ruleNumber);
		
		client.deleteNetworkAclEntry(deleteNetworkAclEntryRequest);
	}
	
	/**
	 * Deletes a route from a route table within a VPC.
	 * 
	 * @param destinationCidrBlock {@link String} desination cidr block
	 * @param routeTableId {@link String} route table id
	 */
	public void deleteRoute(String destinationCidrBlock, String routeTableId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DeleteRouteRequest deleteRouteRequest = new DeleteRouteRequest();
		deleteRouteRequest.setDestinationCidrBlock(destinationCidrBlock);
		deleteRouteRequest.setRouteTableId(routeTableId);
		
		client.deleteRoute(deleteRouteRequest);
	}
	
	/**
	 * Deletes a route table from a VPC.
	 * 
	 * @param routeTableId {@link String} route table id
	 */
	public void deleteRouteTable(String routeTableId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DeleteRouteTableRequest deleteRouteTableRequest = new DeleteRouteTableRequest();
		deleteRouteTableRequest.setRouteTableId(routeTableId);
		
		client.deleteRouteTable(deleteRouteTableRequest);
	}
	
	/**
	 * Deletes a subnet from a VPC.
	 * 
	 * @param subnetId {@link String} subnet id
	 */
	public void deleteSubnet(String subnetId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DeleteSubnetRequest deleteSubnetRequest = new DeleteSubnetRequest();
		deleteSubnetRequest.setSubnetId(subnetId);
		
		client.deleteSubnet(deleteSubnetRequest);
	}
	
	/**
	 * Deletes a VPC.
	 * 
	 * @param vpcId {@link String} vpc id
	 */
	public void deleteVpc(String vpcId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DeleteVpcRequest deleteVpcRequest = new DeleteVpcRequest();
		
		deleteVpcRequest.setVpcId(vpcId);
		
		client.deleteVpc(deleteVpcRequest);
	}
	
	/**
	 * Gives information about network acls within a vpc.
	 * 
	 * @return {@link List} of {@link NetworkACL} list of network ACLs
	 */
	public List<NetworkACL> describeNetworkAcls(){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DescribeNetworkAclsResult describeNetworkAclsResult = client.describeNetworkAcls();
		
		List<NetworkACL> networkAcls = new ArrayList<NetworkACL>();
		for(NetworkAcl networkAcl : describeNetworkAclsResult.getNetworkAcls()){
			NetworkACL networkACL = new NetworkACL();
			networkACL.setNetworkACLId(networkAcl.getNetworkAclId());
			networkACL.setVpcId(networkAcl.getVpcId());
			
			for(NetworkAclEntry networkAclEntry : networkAcl.getEntries()){
				NetworkACLEntry networkACLEntry = new NetworkACLEntry();
				networkACLEntry.setCidrBlock(networkAclEntry.getCidrBlock());
				networkACLEntry.setEgress(networkAclEntry.getEgress());
				
				com.saic.rtws.commons.cloud.beans.vpc.PortRange portRange = new com.saic.rtws.commons.cloud.beans.vpc.PortRange();
				portRange.setFrom(networkAclEntry.getPortRange().getFrom());
				portRange.setTo(networkAclEntry.getPortRange().getTo());
				networkACLEntry.setPortRange(portRange);
				
				networkACLEntry.setProtocol(networkAclEntry.getProtocol());
				networkACLEntry.setRuleAction(networkAclEntry.getRuleAction());
				networkACLEntry.setRuleNumber(networkAclEntry.getRuleNumber());
				
				networkACL.addNetworkACLEntry(networkACLEntry);
			}
			
			networkAcls.add(networkACL);
		}
		
		return networkAcls;
	}
	
	/**
	 * Gives information about all route tables.
	 * 
	 * @return {@link List} of {@link RouteTable}
	 */
	public List<RouteTable> describeRouteTables(){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DescribeRouteTablesResult describeRouteTablesResult = client.describeRouteTables();
		
		List<RouteTable> routeTables = new ArrayList<RouteTable>();
		for(com.amazonaws.services.ec2.model.RouteTable awsRouteTable : describeRouteTablesResult.getRouteTables()){
			RouteTable routeTable = new RouteTable();
			routeTable.setRouteTableId(awsRouteTable.getRouteTableId());
			for(com.amazonaws.services.ec2.model.Route awsRoute : awsRouteTable.getRoutes()){
				Route route = new Route();
				route.setDestinationCidrBlock(awsRoute.getDestinationCidrBlock());
				route.setGatewayId(awsRoute.getGatewayId());
				route.setInstanceId(awsRoute.getInstanceId());
				route.setState(awsRoute.getState());
				
				routeTable.addRoute(route);
			}
			
			for (com.amazonaws.services.ec2.model.RouteTableAssociation awsRouteTableAssociation : awsRouteTable.getAssociations()) {
				routeTable.addAssociationId(awsRouteTableAssociation.getRouteTableAssociationId());
			}
			
			routeTables.add(routeTable);
		}
		
		return routeTables;
	}
	
	/**
	 * Gives information about all route tables for a given vpc.
	 * 
	 * @return {@link List} of {@link RouteTable}
	 */
	public List<RouteTable> describeRouteTables(String vpcId, Boolean onlyMain) {
		
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		ArrayList<Filter> filters = new ArrayList<Filter>();
		
		Filter filter1 = new Filter();
		filter1.setName("vpc-id");
		filter1.setValues(Collections.singleton(vpcId));
		filters.add(filter1);
		
		if (onlyMain) {
			Filter filter2 = new Filter();
			filter2.setName("association.main");
			filter2.setValues(Collections.singleton(onlyMain.toString()));
			filters.add(filter2);
		}
		
		DescribeRouteTablesResult result = client.describeRouteTables(
				new DescribeRouteTablesRequest().withFilters(filters));
		
		List<RouteTable> routeTables = new ArrayList<RouteTable>();
		for(com.amazonaws.services.ec2.model.RouteTable awsRouteTable : result.getRouteTables()){
			RouteTable routeTable = new RouteTable();
			routeTable.setRouteTableId(awsRouteTable.getRouteTableId());
			for(com.amazonaws.services.ec2.model.Route awsRoute : awsRouteTable.getRoutes()){
				Route route = new Route();
				route.setDestinationCidrBlock(awsRoute.getDestinationCidrBlock());
				route.setGatewayId(awsRoute.getGatewayId());
				route.setInstanceId(awsRoute.getInstanceId());
				route.setState(awsRoute.getState());
				
				routeTable.addRoute(route);
			}
			
			for (com.amazonaws.services.ec2.model.RouteTableAssociation awsRouteTableAssociation : awsRouteTable.getAssociations()) {
				routeTable.addAssociationId(awsRouteTableAssociation.getRouteTableAssociationId());
			}
			
			routeTables.add(routeTable);
		}

		return routeTables;
		
	}
	
	/**
	 * Gives information about subnets within AWS account.
	 * 
	 * @return {@link List} of {@link Subnet}
	 */
	public List<Subnet> describeSubnets(){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DescribeSubnetsResult describeSubnetsResult = client.describeSubnets();
		
		List<Subnet> subnets = new ArrayList<Subnet>();
		for(com.amazonaws.services.ec2.model.Subnet awsSubnet : describeSubnetsResult.getSubnets()){
			Subnet subnet = new Subnet();
			subnet.setAvailabilityZone(awsSubnet.getAvailabilityZone());
			subnet.setAvailableIpAddressCount(awsSubnet.getAvailableIpAddressCount());
			subnet.setCidrBlock(awsSubnet.getCidrBlock());
			subnet.setState(awsSubnet.getState());
			subnet.setSubnetId(awsSubnet.getSubnetId());
			subnet.setVpcId(awsSubnet.getVpcId());
			
			subnets.add(subnet);
		}
		
		return subnets;
	}
	
	/**
	 * Gives information about VPCs within AWS account.
	 * 
	 * @return {@link List} of {@link com.saic.rtws.commons.cloud.beans.vpc.Vpc}
	 */
	public List<com.saic.rtws.commons.cloud.beans.vpc.Vpc> describeVpcs(){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DescribeVpcsResult describeVpcResult = client.describeVpcs();
		
		List<com.saic.rtws.commons.cloud.beans.vpc.Vpc> vpcs = new ArrayList<com.saic.rtws.commons.cloud.beans.vpc.Vpc>();
		for(Vpc awsVpc : describeVpcResult.getVpcs()){
			com.saic.rtws.commons.cloud.beans.vpc.Vpc vpc = new com.saic.rtws.commons.cloud.beans.vpc.Vpc();
			vpc.setCidrBlock(awsVpc.getCidrBlock());
			vpc.setDhcpOptionsId(awsVpc.getDhcpOptionsId());
			vpc.setInstanceTenancy(awsVpc.getInstanceTenancy());
			vpc.setState(awsVpc.getState());
			vpc.setVpcId(awsVpc.getVpcId());
			
			vpcs.add(vpc);
		}
		
		return vpcs;
	}
	
	/**
	 * Gives information about a specific VPC within AWS account.
	 * 
	 * @return {@link List} of {@link com.saic.rtws.commons.cloud.beans.vpc.Vpc}
	 */
	public com.saic.rtws.commons.cloud.beans.vpc.Vpc describeVpc(String vpcId) {
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DescribeVpcsRequest request = new DescribeVpcsRequest();
		request.setVpcIds(Collections.singletonList(vpcId));
		
		DescribeVpcsResult describeVpcResult = client.describeVpcs(request);
		
		com.saic.rtws.commons.cloud.beans.vpc.Vpc vpc = null;
		if (describeVpcResult.getVpcs().size() > 0) {
			Vpc awsVpc = describeVpcResult.getVpcs().get(0);
			
			vpc = new com.saic.rtws.commons.cloud.beans.vpc.Vpc();
			vpc.setCidrBlock(awsVpc.getCidrBlock());
			vpc.setDhcpOptionsId(awsVpc.getDhcpOptionsId());
			vpc.setInstanceTenancy(awsVpc.getInstanceTenancy());
			vpc.setState(awsVpc.getState());
			vpc.setVpcId(awsVpc.getVpcId());
		}
		
		return vpc;
	}
	
	/**
	 * Disassociates a subnet from a route table.
	 * 
	 * @param associationId {@link String} association id
	 */
	public void disassociateRouteTable(String associationId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		DisassociateRouteTableRequest disassociateRouteTableRequest = new DisassociateRouteTableRequest();
		disassociateRouteTableRequest.setAssociationId(associationId);
		
		client.disassociateRouteTable(disassociateRouteTableRequest);
	}
	
	/**
	 * Changes which network ACL a subnet is associated with.
	 * @param associationId {@link String} the current association between subnet and network acl
	 * @param networkAclId {@link String} the id of the network ACL to associate with the subnet
	 * @return {@link String} the new association id between the acl and subnet
	 */
	public String replaceNetworkAclAssociation(String associationId, String networkAclId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		ReplaceNetworkAclAssociationRequest replaceNetworkAclAssociationRequest = new ReplaceNetworkAclAssociationRequest();
		replaceNetworkAclAssociationRequest.setAssociationId(associationId);
		replaceNetworkAclAssociationRequest.setNetworkAclId(networkAclId);
		
		ReplaceNetworkAclAssociationResult replaceNetworkAclAssociationResult = client.replaceNetworkAclAssociation(replaceNetworkAclAssociationRequest);
		return replaceNetworkAclAssociationResult.getNewAssociationId();
	}
	
	/**
	 * Replace an entry in a network ACL.
	 * 
	 * @param cidrBlock {@link String} the cidr range of the rule
	 * @param egress {@link boolean} egress traffic true, ingress traffic false
	 * @param networkAclId {@link String} id of the network acl where the entry will be replaced
	 * @param portRange {@link PortRange} a port range
	 * @param protocol {@link String} protocol
	 * @param ruleAction {@link String} possile values Allow or Deny
	 * @param ruleNumber int value rule number of the entry to replace
	 */
	public void replaceNetworkAclEntry(String cidrBlock, boolean egress, String networkAclId, PortRange portRange, String protocol,
											String ruleAction, int ruleNumber){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		ReplaceNetworkAclEntryRequest replaceNetworkAclEntryRequest = new ReplaceNetworkAclEntryRequest();
		replaceNetworkAclEntryRequest.setCidrBlock(cidrBlock);
		replaceNetworkAclEntryRequest.setEgress(egress);
		replaceNetworkAclEntryRequest.setNetworkAclId(networkAclId);
		
		com.amazonaws.services.ec2.model.PortRange awsPortRange = new com.amazonaws.services.ec2.model.PortRange();
		awsPortRange.setFrom(portRange.getFrom());
		awsPortRange.setTo(portRange.getTo());
		
		replaceNetworkAclEntryRequest.setPortRange(awsPortRange);
		replaceNetworkAclEntryRequest.setProtocol(protocol);
		replaceNetworkAclEntryRequest.setRuleAction(ruleAction);
		replaceNetworkAclEntryRequest.setRuleNumber(ruleNumber);
		
		client.replaceNetworkAclEntry(replaceNetworkAclEntryRequest);
	}
	
	/**
	 * Replaces an existing route within a route table in a VPC.
	 * 
	 * @param destinationCidrBlock {@link String} destination cidr block
	 * @param gatewayId {@link String} the ID of a VPN or Internet gateway attached to your VPC
	 * @param instanceId {@link String} the ID of a NAT instance in your VPC
	 * @param routeTableId {@link String} the ID of the route table where the route will be replaced
	 */
	public void replaceRoute(String destinationCidrBlock, String gatewayId, String instanceId, String routeTableId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		ReplaceRouteRequest replaceRouteRequest = new ReplaceRouteRequest();
		replaceRouteRequest.setDestinationCidrBlock(destinationCidrBlock);
		replaceRouteRequest.setGatewayId(gatewayId);
		replaceRouteRequest.setInstanceId(instanceId);
		replaceRouteRequest.setRouteTableId(routeTableId);
		
		client.replaceRoute(replaceRouteRequest);
	}
	
	/**
	 * Changes a route table associated with a given subnet in a VPC.
	 * 
	 * @param associationId {@link String} id of the association between route table and subnet
	 * @param routeTableId {@link String} id of the new route table to associate to the subnet
	 * @return {@link String} new association id between route table and subnet
	 */
	public String replaceRouteTableAssociation(String associationId, String routeTableId){
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		ReplaceRouteTableAssociationRequest replaceRouteTableAssociationRequest = new ReplaceRouteTableAssociationRequest();
		replaceRouteTableAssociationRequest.setAssociationId(associationId);
		replaceRouteTableAssociationRequest.setRouteTableId(routeTableId);
		
		ReplaceRouteTableAssociationResult replaceRouteTableAssociationResult = client.replaceRouteTableAssociation(replaceRouteTableAssociationRequest);
		
		return replaceRouteTableAssociationResult.getNewAssociationId();
	}
	
	@Override
	public void setSrcDestCheckAttribute(String instanceId, Boolean value) {
		AmazonEC2Client client = factory.getAmazonEC2Client();
		
		ModifyInstanceAttributeRequest request = new ModifyInstanceAttributeRequest();
		request.setAttribute("sourceDestCheck");
		request.setValue(value.toString());
		request.setInstanceId(instanceId);
		
		client.modifyInstanceAttribute(request);
	}
	
	@Override
	public String allocateElasticAddress() {
		AmazonEC2Client client = factory.getAmazonEC2Client();
		AllocateAddressRequest request = new AllocateAddressRequest().withDomain("vpc");
		return client.allocateAddress(request).getPublicIp();
	}
}
