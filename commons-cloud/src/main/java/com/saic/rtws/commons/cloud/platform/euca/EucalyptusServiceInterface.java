package com.saic.rtws.commons.cloud.platform.euca;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.RevokeSecurityGroupIngressRequest;
import com.saic.rtws.commons.cloud.platform.aws.AwsConnectionFactory;
import com.saic.rtws.commons.cloud.platform.aws.SimpleAwsServiceImpl;
import com.saic.rtws.commons.config.UserDataProperties;

public class EucalyptusServiceInterface extends SimpleAwsServiceImpl {

	private static Logger log = Logger.getLogger(EucalyptusServiceInterface.class);

	@Override
	public void createFirewallRuleByUserIdGroup(String groupName, String protocol, int fromPort, int toPort, String userId,
			String userIdGroupId) {

		AwsConnectionFactory factory = getConnectionFactory();

		AmazonEC2Client client = null;
		try {
			client = factory.getAmazonEC2Client();

			AuthorizeSecurityGroupIngressRequest request = new AuthorizeSecurityGroupIngressRequest();

			request.withGroupName(groupName)
				.withIpProtocol(protocol)
				.withFromPort(fromPort)
				.withToPort(toPort)
				.withSourceSecurityGroupOwnerId(userId)
				.withSourceSecurityGroupName(userIdGroupId);

			client.authorizeSecurityGroupIngress(request);

		} catch (AmazonServiceException e) {
			if (!e.getErrorCode().equals("InvalidPermission.Duplicate")) {
				throw e;
			}
		} finally {
			if (client != null) {
				client.shutdown();
			}
		}

	}

	@Override
	public void deleteFirewallRuleByUserIdGroup(String groupName, String protocol, int fromPort, int toPort, String userId,
			String userIdGroupId) {

		AwsConnectionFactory factory = getConnectionFactory();
		AmazonEC2Client client = factory.getAmazonEC2Client();

		RevokeSecurityGroupIngressRequest request = new RevokeSecurityGroupIngressRequest();

		request.withGroupName(groupName)
			.withIpProtocol(protocol)
			.withFromPort(fromPort)
			.withToPort(toPort)
			.withSourceSecurityGroupOwnerId(userId)
			.withSourceSecurityGroupName(userIdGroupId);

		client.revokeSecurityGroupIngress(request);

	}
	
	@Override
	public String getUserData(String id) {

		//TODO:Fix for EUCA, service call not supported, tmp fix
		
		String userdata = String.format("%s=%s;", UserDataProperties.RTWS_SW_VERSION, UserDataProperties.getInstance().getString(UserDataProperties.RTWS_SW_VERSION));
		
		return userdata;

	}

	@Override
	public void setUserData(String id, String userData) {

		//TODO:Fix for EUCA, service call not supported, tmp fix

	}

}
