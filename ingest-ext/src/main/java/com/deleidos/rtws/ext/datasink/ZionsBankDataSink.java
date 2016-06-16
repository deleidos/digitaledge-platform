package com.deleidos.rtws.ext.datasink;


import org.apache.log4j.Logger;

import net.sf.json.JSONException;
import net.sf.json.JSONObject;


import com.deleidos.rtws.commons.cloud.platform.aws.AwsConnectionFactory;
import com.deleidos.rtws.commons.exception.InitializationException;
import com.deleidos.rtws.core.framework.UserConfigured;
import com.deleidos.rtws.core.framework.processor.AbstractDataSink;


public class ZionsBankDataSink extends AbstractDataSink {

	private static Logger log = Logger.getLogger(ZionsBankDataSink.class);
	private static final String[] recipients = {"morrisonjb@leidos.com","morrison.james.b@gmail.com"};
	private static final String FROM = "dev@rtsaic.com";
	private static final String SUBJECT = "Possible Fraud Discovered!";
	private String emailMsgString = "A possible check cashing fraud was detected for bank ID %s; account %s.\n" + 
	"Two or more checks were cashed from this account on %s at the following branch IDs: %s %s";
	
	
	@Override
	public void flush() {
		return;

	}
	
	@Override
	@UserConfigured(value="-1", description="TBD")
	public void setMaxTimeBetweenFlush(int value) {
		super.setMaxTimeBetweenFlush(-1);
	}

	@Override
	@UserConfigured(value="-1", description="TBD")
	public void setMaxBytesBetweenFlush(int value) {
		super.setMaxBytesBetweenFlush(-1);
	}

	@Override
	@UserConfigured(value="-1", description="")
	public void setMaxRecordsBetweenFlush(int value) {
		super.setMaxRecordsBetweenFlush(-1);
	}
	
	@Override
	public void setAwsConnectionFactory(AwsConnectionFactory awsConnectionFactory) {
		super.setAwsConnectionFactory(awsConnectionFactory);
	}
	
	/**
	 * Perform initialization work for the data sink.
	 */
	@Override
	public void initialize() throws InitializationException {
		return;

	}
	
	/**
	 * Perform disposal work for the data sink.
	 */
	@Override
	public void dispose() {
		return;

	}

	/**
	 * Compares previous record with the current record to determine if a fraud alert needs to be issued. A fraud alert
	 * is issued if two checks are cashed on the same calendar day, same account, same bank number but different branches. 
	 */
	@Override
	protected void processInternal(JSONObject record, FlushCounter counter) {
/*
		JSONObject lastTransaction = null;
		
		try {
			lastTransaction = record.getJSONObject("LastTransaction");
		} catch (JSONException ex) {
			log.error("Error getting the last transaction JSON object.", ex);
			lastTransaction=null;
		}
		// Transaction Type must be 1750 (Check cashing) otherwise we don't care about the record
		if (lastTransaction != null && !lastTransaction.isNullObject() && record.getString("TransactionType").equals("1750")) {
		
			if (!record.getString( "BranchID" ).equals(lastTransaction.getString("BranchID")))  {
				// Need to send an alert that this is a possible fraud case.
				// Get the data for the email msg
				String bankID = record.getString("Bank");
				String branchCurrent = record.getString( "BranchID" );
				String branchLast = lastTransaction.getString("BranchID");
				String account = record.getString("Account");
				String date = record.getString("DateStamp");
				log.info("Found a possible fraud case on account: " + account + " !");
				
				Object[] args = new Object[] {bankID, account, date, branchCurrent, branchLast};
				
				String emailMsg = String.format(emailMsgString, args);
								
				try {
					// This was sending SES email
					new EmailUtility(getAwsConnectionFactory()).sendEmailAlert(0l, null, null, 0, null, null);
					
				} catch (Exception ex) {
					System.out.println("Caught an Exception trying to send email" );
					ex.printStackTrace();
				}
			}
 		}
		counter.increment(1, record.size());
*/
	}
}
