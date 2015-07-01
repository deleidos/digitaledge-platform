package com.saic.rtws.commons.net.listener.process;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.net.listener.client.target.UpdateNatIPTableRulesClient;

public class IPTablesProcess {

	private static final Logger logger = LogManager.getLogger(IPTablesProcess.class);
	
	public enum IPTablesStatus {Success, Failure}
	
	private JSONObject command;
	
	public static IPTablesProcess newInstance(String command){
		return new IPTablesProcess(command);
	}
	
	public IPTablesProcess(String command){
		this.command = JSONObject.fromObject(command);
	}
	
	public IPTablesStatus addRule(){
		IPTablesStatus status = IPTablesStatus.Success;
		Process process = null;
		String ipTableCommand = null;
		try{
			
			String serverIp = command.getString(UpdateNatIPTableRulesClient.SERVER_IP);
			String port = command.getString(UpdateNatIPTableRulesClient.PORT);
			String cidrSubnet = command.getString(UpdateNatIPTableRulesClient.SUBNET_CIDR);
			
			if(StringUtils.isBlank(serverIp) == true || StringUtils.isBlank(port) == true){
				return IPTablesStatus.Failure;
			}
			
			//update prerouting
			ipTableCommand = String.format("/sbin/iptables -A PREROUTING -s ! %s -t nat -i eth0 -p tcp --dport %s -j DNAT --to %s", cidrSubnet, port, serverIp);
			process = new ProcessBuilder("/bin/bash", "-c", ipTableCommand).start();
		
			logger.info(ipTableCommand);
			
			synchronized (process) {
				int retVal =process.waitFor(); // Let the script finish running before we get the exit value.
				
				if(retVal != 0){
					status = IPTablesStatus.Failure;
				}
			}
			
			//update forwarding
			ipTableCommand = String.format("/sbin/iptables -A FORWARD -s ! %s -p tcp -d %s --dport %s -j ACCEPT", cidrSubnet, serverIp, port);
			process = new ProcessBuilder("/bin/bash", "-c", ipTableCommand).start();
		
			logger.info(ipTableCommand);
			
			synchronized (process) {
				int retVal = process.waitFor(); // Let the script finish running before we get the exit value.
				
				if(retVal != 0){
					status = IPTablesStatus.Failure;
				}
			}
		}
		catch(Exception e){
			logger.error(e.toString(), e);
			status = IPTablesStatus.Failure;
		}
		
		return status;
	}
}
