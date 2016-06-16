package com.deleidos.rtws.tools.devconsole;

import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClient;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class SecMonitor {
	static AmazonEC2Client ec2;
	private static String accessKey = "redacted";
	private static String secretKey = "redacted";
	private static void init()   {
	        ec2 = new AmazonEC2Client(new BasicAWSCredentials(accessKey, secretKey));
    }

    /**
     * Get all the instances from AWS.
     * @return Array of instance objects.
     */
	private static String getDeleted(List<IpPermission> oip ,List<IpPermission> nip){
		String returner="";
		boolean found=false;
		for(int c=0;c<oip.size();c++){
			found=false;
			for(int i=0;i<nip.size()&&!found;i++){
				if(oip.get(c).toString().equals(nip.get(i).toString())){
					found=true;
				}
			}
			if(!found)
			returner+=oip.get(c)+"\n";
		}
		
		return returner;
	}
	private static String getAdded(List<IpPermission> oip ,List<IpPermission> nip){
		String returner="";
		boolean found=false;
		for(int c=0;c<nip.size();c++){
			found=false;
			for(int i=0;i<oip.size()&&!found;i++){
				if(nip.get(c).toString().equals(oip.get(i).toString())){
					found=true;
				}
			}
			if(!found)
			returner+=nip.get(c)+"\n";
		}
		
		return returner;
	}
    protected static List<SecurityGroup> getData()   {

		//CreateSecurityGroupRequest r1 = new CreateSecurityGroupRequest("SecConsole", "This is Elan's test security group");
		//ec2.createSecurityGroup(r1);
		DescribeSecurityGroupsResult describeSecurityGroupsResult =  ec2.describeSecurityGroups();
		List<SecurityGroup> securityGroups =  describeSecurityGroupsResult.getSecurityGroups();
		return securityGroups;
        
    }
   /* private static List<Groups> decode(Object[] objects){
    	List<Groups> groups =new LinkedList<Groups>();
    	Groups gr=new Groups(objects[0].toString().substring(1));
    	for(int c=1; c<objects.length;c++){
    		if(objects[c].toString().charAt(0)==':'){
    			groups.add(gr);
    			gr=new Groups(objects[c].toString().substring(1));
    		}
    		else if(objects[c].toString().charAt(0)=='P'){
    			gr.protocal=objects[c].toString().substring(1);
    		}
    		else if(objects[c].toString().charAt(0)=='F'){
    			gr.from=objects[c].toString().substring(1);
    		}
    		else if(objects[c].toString().charAt(0)=='T'){
    			gr.to=objects[c].toString().substring(1);
    		}
    		else if(objects[c].toString().charAt(0)=='G'){
    			gr.groups.add(objects[c].toString().substring(1));
    		}
    		else if(objects[c].toString().charAt(0)=='R'){
    			gr.ranges.add(objects[c].toString().substring(1));
    		}
    	}
    	groups.add(gr);
    	
    	return groups;
    }*/
    
   public static String changed(List<SecurityGroup> older ,List<SecurityGroup> newer){
	   
	   String changes="";
	   boolean found=false;
	   for(int c=0;c<older.size();c++){
		   for(int i=0;i<newer.size()&&!found;i++){
			   if(older.get(c).getGroupName().equals(newer.get(i).getGroupName())){
				   if(older.get(c).toString().equals(newer.get(i).toString())){
					   found=true;
				   	}
				   else{
					  //changes+="Changes in security group "+ older.get(c).getGroupName()+".\n";
					   List<IpPermission> oip =older.get(c).getIpPermissions();
					   List<IpPermission> nip =newer.get(i).getIpPermissions();
					   if(oip.toString().equals(nip.toString().toString())){
						  //if the group has been remade but the permissions are the same.	   
					   }
					   else{
						   String deleted=getDeleted(oip,nip);
						   String added=getAdded(oip,nip);
						   if(!deleted.isEmpty()){
							   changes+="\nChanges in security group "+ older.get(c).getGroupName()+".  IP rule(s) deleted\n"+deleted;
							   
							   
						   	}
						   if(!added.isEmpty()){
							   changes+="\nChanges in security group "+ older.get(c).getGroupName()+".  IP rule(s) added\n"+added;   
						   }
					   	}
				   }
				   found=true;
				   }
			  
		   }
	   
	   if(!found){
		   changes+="\nSecurity group "+ older.get(c).getGroupName()+" has been deleted.\n";
		   
	   }
		   found=false;
   	}
	   for(int c=0;c<newer.size();c++){
		   for(int i=0;i<older.size()&&!found;i++){
			   if(newer.get(c).getGroupName().equals(older.get(i).getGroupName())){
				   found=true;
			   }
		   }
		   if(!found){
			   changes+="\nSecurity group "+ newer.get(c).getGroupName()+" has been added.\n";
		   }
		   found=false;
	   }
	   
	   return changes;
   }
	private static void sendSESEmail(String from, String[] recipients, String subject, String msgBody) {
    	
		AmazonSimpleEmailServiceClient client = new AmazonSimpleEmailServiceClient(new BasicAWSCredentials(accessKey, secretKey));
		List<String> toAddresses = new ArrayList<String>();
		for (String recipient : recipients)
			toAddresses.add(recipient);
		Destination destination = new Destination(toAddresses);
		
		Content bodyContent = new Content();
		bodyContent.setData(msgBody);
		Body body = new Body();
		body.setText(bodyContent);
		com.amazonaws.services.simpleemail.model.Message message = new com.amazonaws.services.simpleemail.model.Message(bodyContent, body);
		
		Content subjectContent = new Content();
		subjectContent.setData(subject);
		message.setSubject(subjectContent);
		
		try {
			/*SendEmailResult  result =*/ client.sendEmail(new SendEmailRequest(from, destination, message));
		} catch (AmazonServiceException ase) {
			ase.printStackTrace();
			System.out.println("Caught a AmazonClientException, which means that there was an error response returned by " 
					+ "AmazonSimpleEmailService indicating either a problem with the data in the request, or a server side issue.");
		} catch (AmazonClientException ace) {
			ace.printStackTrace();
			System.out.println("Caught a AmazonClientException, which means that there was an internal error "
	                + "was encountered inside the client while attempting to make the request or handle the response.");
		} 
	}

   public static void main(String[] args) {
		init();
		final List<SecurityGroup> original =getData();
			
		
		while(true)
		{
			try {
				Thread.sleep(24*60*60*1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			init();
			List<SecurityGroup> newer=getData();
			String data=(changed(original,newer));
			if (data.length()>2){
				sendSESEmail("dev@rtsaic.com",new String[] {"jaffeee@saic.com"}, "Changes in security groups", data);
			
			}
		
		}
		
	}

}
