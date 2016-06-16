package com.deleidos.rtws.ext.processor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONObject;

import com.deleidos.rtws.core.framework.Description;
import com.deleidos.rtws.core.framework.EnrichmentDefinition;
import com.deleidos.rtws.core.framework.EnrichmentProperty;
import com.deleidos.rtws.core.framework.processor.AbstractEnrichmentProcessor;
import com.deleidos.rtws.core.framework.processor.EnrichmentAction;
import com.deleidos.rtws.core.framework.processor.ParameterList;


@EnrichmentDefinition(
		type="SuspiciousActivity",
		description="Find if an account has an above-expected percent of activity with a certain teller.",
				properties = {
				@EnrichmentProperty(
						name="parameters",
						description="Comma delimited field(s) used as natural keys to query the dimension table.",
						type="java.lang.String"
				)
		}
	)
	@Description("Find if an account has an above-expected percent of activity with a certain teller.")
public class SuspiciousActivityEnrichment extends AbstractEnrichmentProcessor {

	
	private final int numTellers = 300;
	
	private final double Threshold = .8;
	
	private final int numMessagesThreshold = 5;
	
	private static Map<Integer,PercentContainer> suspicionMap;
	
	
	@Override
	public String getType() {
		return "SuspiciousActivity";
	}
	
	@Override
	public Object buildEnrichedElement(EnrichmentAction action,
			ParameterList parameters) {
		// TODO Auto-generated method stub
		JSONObject obj = action.getRootObject();
		boolean suspicionValue=false;
		Integer thisAcct = obj.getJSONObject("Account").getInt("Account");
		PercentContainer perc = suspicionMap.get(thisAcct);
		if(perc==null){
			perc = new PercentContainer();
			suspicionMap.put(thisAcct, perc);
		}
		Integer teller = obj.getJSONObject("Teller").getInt("Teller");
		perc.transaction(teller);
		if(perc.getPercent(teller)>Threshold && perc.getNumTransactions()>=numMessagesThreshold)
			suspicionValue=true;
		
		return suspicionValue;
	}
	
	@Override
	public void initialize() {
		super.initialize();
		suspicionMap = new ConcurrentHashMap<Integer,PercentContainer>();
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	private class PercentContainer{
		private int numTransactions;
		private int[] TransactionList;
		
		public PercentContainer(){
			numTransactions=0;
			TransactionList = new int[numTellers];
			for(int i=0;i<numTellers;i++)
				TransactionList[i]=0;
		}
		
		public void transaction(int tellerNum){
			numTransactions++;
			TransactionList[tellerNum]++;
		}
		
		public double getPercent(int tellerNum){
			int numTransactionsForTeller = TransactionList[tellerNum];
			return ((double)numTransactionsForTeller)/((double)numTransactions);
			
		}
		
		public int getNumTransactions(){
			return numTransactions;
		}
		
	}

}
