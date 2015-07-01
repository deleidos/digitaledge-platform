package com.saic.rtws.commons.cloud.platform.aws;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.saic.rtws.commons.net.email.EmailSenderInterface;

public class AwsSimpleEmailSender implements EmailSenderInterface {

	Logger logger = Logger.getLogger(AwsSimpleEmailSender.class);

	private AwsConnectionFactory awsConnectionFactory;

	/**
	 * Configure the AwsSimpleEmailSender
	 * 
	 * @param awsConnectionFactory
	 *            factory to connect to Amazon Web Services
	 */
	public void configure(AwsConnectionFactory awsConnectionFactory) {
		this.awsConnectionFactory = awsConnectionFactory;
	}

	/**
	 * Send email using Amazon Simple Email Service
	 * 
	 * @param from
	 *            email address of the sender
	 * @param recipients
	 *            array of email addresses for the recipients
	 * @param subject
	 *            email subject
	 * @param msgBody
	 *            email body
	 */
	public boolean sendEmail(String from, String[] recipients, String subject,
			String msgBody) {

		AmazonSimpleEmailServiceClient client = awsConnectionFactory.getAmazonSimpleEmailServiceClient();

		List<String> toAddresses = new ArrayList<String>();
		for (String recipient : recipients) {
			toAddresses.add(recipient);
		}
		Destination destination = new Destination(toAddresses);

		Content bodyContent = new Content();
		bodyContent.setData(msgBody);
		Body body = new Body();
		body.setText(bodyContent);
		Message message = new Message(bodyContent, body);

		Content subjectContent = new Content();
		subjectContent.setData(subject);
		message.setSubject(subjectContent);

		try {
			client.sendEmail(new SendEmailRequest(from, destination, message));
			return true;
		} catch (AmazonServiceException ase) {
			logger.error(
					"Caught a AmazonserviceException, which means that there was an error response returned by "
							+ "AmazonSimpleEmailService indicating either a problem with the data in the request, or a server side issue.",
					ase);
		} catch (AmazonClientException ace) {
			logger.warn(
					"Caught a AmazonClientException, which means that there was an internal error "
							+ "was encountered inside the client while attempting to make the request or handle the response.",
					ace);
		}
		
		return false;
		
	}

}
