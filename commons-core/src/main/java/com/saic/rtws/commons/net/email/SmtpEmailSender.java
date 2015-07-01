package com.saic.rtws.commons.net.email;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

public class SmtpEmailSender implements EmailSenderInterface {

	Logger logger = Logger.getLogger(SmtpEmailSender.class);

	private Properties properties;
	private boolean auth, tls = false;	
	
	/**
	 * Configure the SmtpEmailSender
	 * 
	 * @param props
	 *            Properties to use, else System.getProperties() will be used
	 * @param emailHost
	 *            hostname of the mail server
	 * @param port
	 *            port number on the mail server for the SMTP protocol
	 * @param fromUser
	 *            default user name to use when connecting to the mail server
	 * @param fromPasswd
	 *            password to use when connecting to the mail server
	 * @param auth
	 *            is authentication required
	 * @param tls
	 *            is tls required (will not use specified port above)
	 */
	public void configure(Properties props, String emailHost, int port, String fromUser, String fromPasswd, boolean auth, boolean tls) {
		if (props == null) {
			properties = System.getProperties();
		}
		else {
			properties = props;
		}
		
		setAuth(auth);
		setTls(tls);
		
		if(auth){
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.user", fromUser);
			properties.put("mail.password", fromPasswd);
		}
		if(tls)
			properties.put("mail.smtp.starttls.enable", "true");
		
			
		properties.put("mail.smtp.port", String.valueOf(port));

		properties.put("mail.transport.protocol", "smtp");
		properties.put("mail.smtp.debug", "true");
		properties.put("mail.smtp.host", emailHost);

	}

	
	
	/**
	 * Send email using SMTP
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
	public boolean sendEmail(String from, String[] recipients, String subject, String msgBody) {
		Session session = Session.getInstance(properties, null);

		try {
			// create a message
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));

			InternetAddress[] emailRecipients = new InternetAddress[recipients.length];
			for (int i = 0; i < recipients.length; i++) {
				emailRecipients[i] = new InternetAddress(recipients[i]);
			}

			msg.setRecipients(Message.RecipientType.TO, emailRecipients);

			msg.setSubject(subject);
			msg.setSentDate(new Date());
			msg.setContent(msgBody, "text/plain");

			// send message
			String emailHost = properties.getProperty("mail.smtp.host");
			Transport transport = session.getTransport("smtp");

			if(isAuth()){
				String fromPasswd = properties.getProperty("mail.password");
				if(isTls())
					transport.connect(emailHost, from, fromPasswd);
				else{
					int port = Integer.parseInt(properties.getProperty("mail.smtp.port"));
					transport.connect(emailHost, port, from, fromPasswd);
				}
					
			}
			else {
				transport.connect(emailHost, null, null);
			}
			
			transport.sendMessage(msg, msg.getAllRecipients());

			transport.close();

			return true;
		} catch (MessagingException mex) {
			System.out.println("Failed to send SMTP message " + mex);
			logger.warn("Failed to send SMTP message", mex);
		}
		
		return false;

	}


	public boolean isAuth() {
		return auth;
	}


	public void setAuth(boolean auth) {
		this.auth = auth;
	}


	public boolean isTls() {
		return tls;
	}


	public void setTls(boolean tls) {
		this.tls = tls;
	}

}
