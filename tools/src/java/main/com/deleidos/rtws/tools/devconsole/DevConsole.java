package com.deleidos.rtws.tools.devconsole;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeAvailabilityZonesResult;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.deleidos.rtws.commons.config.PropertiesUtils;


/**
 * Development Console for AWS environment.
 */
@SuppressWarnings("serial")
public final class DevConsole extends JPanel implements ActionListener   {
    private JTable table;
    private JTextField cmd;
    private JButton command;
    private JComboBox domainlist, masterbox, accountlist;
    private Object[] domains,masterdomain;
    private JButton launchButton, updateButton, load ,last, browser, webapp,stopmaster, startmaster;
    private DefaultListModel[] currprop;
    private Runtime rt = Runtime.getRuntime();
    private Map<String,DefaultListModel[]> domainmap=new HashMap<String,DefaultListModel[]>();
    private JScrollPane listScrollPane;
    private JFileChooser fc;
    static AmazonEC2 ec2[]=new AmazonEC2Client[3];
    static JFrame frame[]=new JFrame[3] ;
    private DefaultListModel masters;
    private int lastselected=0;
    int index;
    static Dimension dim;
    static Point loc;
    
    /** Constructor. */
    private DevConsole(int ind) {
        super(new BorderLayout());
        index=ind;
        masters = new DefaultListModel();
        Object[] input = getData();
        int PDN ,hoststart,hostend,domainstart,domainend,groupstart,groupend,namestart,nameend,keystart,keyend;
        String tempstring, tempdns, tempdomain, temphost;
        DefaultListModel[] tempprop;
        Instance tempinst;
        for(int index=0;index<input.length;index++){
        	tempstring=input[index].toString();
        	tempinst= (Instance) input[index];
        	PDN=tempstring.indexOf("PublicDnsName:")+15;
        	tempdns=(tempstring.substring(PDN, tempstring.indexOf(",",PDN)));
        	hoststart=tempstring.indexOf("Host, Value: ")+13;
        	hostend=tempstring.indexOf(",",hoststart);
        	if (hoststart==12) {
				temphost= "No Host";
			} else {
				temphost=tempstring.substring(hoststart,hostend);
			}
        	if(temphost.equals("master")){
        		masters.addElement(tempinst);
        	}
        	
        	if (tempdns.equals("")){
        		
        	}
        	else{
        	//System.out.println(input[index]);
        	
        	groupstart=tempstring.indexOf("Group, Value: ")+14;
        	groupend=tempstring.indexOf(",",groupstart);
        	namestart=tempstring.indexOf("Name, Value: ")+13;
        	keystart=tempstring.indexOf("KeyName: ")+9;
        	keyend=tempstring.indexOf(",",keystart);
        	domainstart=tempstring.indexOf("Domain, Value: ")+15;
        	domainend=tempstring.indexOf(",",domainstart);
        	if(domainstart==14) {
				tempdomain= "No Domain";
			} else {
				tempdomain= tempstring.substring(domainstart,domainend);
			}
        	
        	
        	if (domainmap.containsKey(tempdomain)){
        		tempprop=domainmap.remove(tempdomain);
        	}
        	else{
        		tempprop=new DefaultListModel[]{new DefaultListModel(),new DefaultListModel(),new DefaultListModel(),new DefaultListModel()};
        		
        	}
        	int placer=0;
        	while(placer<tempprop[0].size()){
        		if(tempprop[0].get(placer).toString().compareTo(temphost)>0) {
					break;
				}
        		placer++;
        	}
        	
        		
        	if (namestart==12){
        		tempprop[2].add(placer,"No Name");
        	}
        	else{
        		nameend=tempstring.indexOf(",",namestart);		
        		tempprop[2].add(placer,tempstring.substring(namestart,nameend));

        	}
        	
        	tempprop[3].add(placer,tempdns+"|"+tempstring.substring(keystart,keyend));
        	//domainlist.addElement(tempstring.substring(domainstart,domainend));
        	tempprop[0].add(placer,temphost);
        	if(groupstart==13) {
				tempprop[1].add(placer,"No Group");
			} else {
				tempprop[1].add(placer,tempstring.substring(groupstart,groupend));
			}
        	//System.out.println(tempstring.substring(groupstart,groupend));
        	//System.out.println(grouplist.get(index));
        	//System.out.println(DNS.get(index));
        	//System.out.println(tempstring);
        	//System.out.println(tempinst);
        	domainmap.put(tempdomain, tempprop);
        	}
        }
        masterdomain=new Object[masters.size()];
        int temp;
        for(int counter=0;counter<masters.size();counter++){
        	temp=masters.get(counter).toString().indexOf("Domain, Value: ")+15;
        	
        	if(temp==14) {
				masterdomain[counter]= "No Domain";
			}
			else {
				masterdomain[counter]= masters.get(counter).toString().substring(temp,masters.get(counter).toString().indexOf(",",temp));
      // System.out.println(masterdomain[counter]);
      // if("cybercef.rtws.saic.com".equals(masterdomain[counter].toString())){
    	  //  Set<String> collection=new HashSet<String>();
    	   // collection.add(((Instance)masters.get(counter)).getInstanceId());
    	 //  StartInstancesRequest start=new StartInstancesRequest();
    	 //  start.setInstanceIds(collection);
    	  // ec2.startInstances(start);
      // }
			}
        }
       domains=  domainmap.keySet().toArray();
       currprop=domainmap.get(domains[0]);
       Object[][] data=new Object[currprop[0].size()][3]; 
       for(int index=0;index<currprop[0].size();index++){
        	data[index][0]=currprop[0].get(index);
            data[index][1]=currprop[1].get(index);
        	data[index][2]=currprop[2].get(index);  
        }
        table=new JTable(data,new String[]{"Host","Group","Name"});
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listScrollPane = new JScrollPane(table);
        
        domainlist = new JComboBox(domains);
        accountlist=new JComboBox(new String[] {"A1","A2","A3"});
        launchButton=new JButton("PuTTy");
        launchButton.addActionListener(new SelectListener());
        updateButton=new JButton("Reload");
        updateButton.addActionListener(new UpdateListener());
        command =new JButton("Run Command");
        command.addActionListener(new SelectListener());
        browser =new JButton("Run JMS");
        browser.addActionListener(new SelectListener());
        startmaster=new JButton("Start Master");
        startmaster.addActionListener(new SelectListener());
        stopmaster=new JButton("Stop Master");
        stopmaster.addActionListener(new SelectListener());
        webapp=new JButton("Web App");
        webapp.addActionListener(new SelectListener());
        cmd=new JTextField("Enter commands here. Use a ; to seperate commands. OR Enter the web app you want.");
        load=new JButton("Load script");
        load.addActionListener(new LoadListener());
        last=new JButton("Last Script");
        last.addActionListener(new LoadListener());
        
        domainlist.addActionListener(this);
        domainlist.setSelectedIndex(lastselected);    
        accountlist.addActionListener(new UpdateListener());
        accountlist.setSelectedIndex(index);
        add(listScrollPane,BorderLayout.CENTER);
        JPanel buttons=new JPanel();
        buttons.setLayout(new BoxLayout(buttons,
                BoxLayout.Y_AXIS));
        
        add(cmd,BorderLayout.SOUTH);
        buttons.add(new JLabel("Domains"));
        buttons.add(accountlist);
        buttons.add(domainlist);
        buttons.add(new JSeparator());
       buttons.add(updateButton);
        buttons.add(new JSeparator());
        buttons.add(startmaster);
        buttons.add(stopmaster);
        buttons.add(last);
        buttons.add(load); 
        buttons.add(new JSeparator());
        buttons.add(launchButton); 
        buttons.add(command);
        buttons.add(browser);
        buttons.add(webapp);
        add(buttons,BorderLayout.WEST);
        fc =new JFileChooser();
        
        	
    }
    	
   // class ChangeListener implements ActionListener {
        
        public void actionPerformed(final ActionEvent e) {
            this.remove(listScrollPane);
        	int pos=domainlist.getSelectedIndex();
            //System.out.println(pos);
            currprop=domainmap.get(domains[pos]);
            Object[][] data=new Object[currprop[0].size()][3]; 
             for(int index=0;index<currprop[0].size();index++){
             	data[index][0]=currprop[0].get(index);
                 data[index][1]=currprop[1].get(index);
             	data[index][2]=currprop[2].get(index);
             }
             table=new JTable(data,new String[]{"Host","Group","Name"});
             listScrollPane = new JScrollPane(table);
             this.add(listScrollPane,BorderLayout.CENTER);
             dim= frame[index].getSize();
             frame[index].pack();
             accountlist.setSelectedIndex(index);
             frame[index].setSize(dim);
        }
    //    }
        private void reload(boolean changeindex){
        	if(changeindex)
        		lastselected=domainlist.getSelectedIndex();
        	 dim= frame[index].getSize();
        	JComponent newContentPane = new DevConsole(index);
            newContentPane.setOpaque(true); 
            frame[index].setContentPane(newContentPane); 
            frame[index].pack();
            frame[index].setSize(dim);
        }
        class UpdateListener implements ActionListener {
            
            public void actionPerformed(final ActionEvent e) {
            	if(e.getSource().equals(updateButton))
            		reload(true);
            	else{
            		int pos = accountlist.getSelectedIndex();
            		accountlist.setSelectedIndex(index);
            		if(pos!=index){
            			dim=frame[index].getSize();
            			loc=frame[index].getLocation();
            			for(int c=0;c<3;c++){
            				if(pos==c){
            					frame[c].setSize(dim);
            					frame[c].setLocation(loc);            					
            					frame[c].setVisible(true);
            				}
            				else{
            					frame[c].setVisible(false);
            				}
            			}
            		}
            	}
            }}
        class LoadListener implements ActionListener {
            
            public void actionPerformed(final ActionEvent e) {
            	String path="";
            	if (e.getSource().equals(last)){
            		path="S:\\Software\\PuTTY\\script.txt";
            	}
            	else{
            	 int returnVal = fc.showOpenDialog(DevConsole.this);
                 if (returnVal != JFileChooser.APPROVE_OPTION) {
                	 return;
                 }
                 path=fc.getSelectedFile().getPath();
            	}
                     try {
						FileReader file = new FileReader(path);
						BufferedReader in= new BufferedReader(file);
						String read=in.readLine();
						String newcmd="";
						while(read!=null){
							newcmd+=read+";";
							read=in.readLine();
						}
						cmd.setText(newcmd);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						// 
						e1.printStackTrace();
					}
                 
            }}
    class SelectListener implements ActionListener {
        
        public void actionPerformed(final ActionEvent e) {
        	int[] selected=table.getSelectedRows();
        	String tempdns,temphost;
        	if (e.getSource().equals(command)){
        		  try{	  
        			  BufferedWriter out = new BufferedWriter(new FileWriter("S:\\Software\\PuTTY\\script.txt"));
        			  out.write(cmd.getText());        			  
        			  out.close();
        			  }catch (Exception filee){
        			  System.err.println("Error: " + filee.getMessage());
        			  }
        	for(int index=0;index<selected.length;index++){
        		tempdns=currprop[3].get(selected[index]).toString();
        	 try { 
				rt.exec("\"S:\\Software\\PuTTY\\putty.exe\" -i \"S:\\AWS\\KeyPairs\\"+tempdns.substring(tempdns.indexOf("|")+1)+".ppk\" ubuntu@"+tempdns.substring(0,tempdns.indexOf("|"))+" -m \"S:\\Software\\PuTTY\\script.txt\"");
        	 } catch (IOException e1) {
				System.out.println("Failed to exicute putty");
				e1.printStackTrace();
			}
        	}
        }
        
    	else if (e.getSource().equals(launchButton)){
        	for(int index=0;index<selected.length;index++){
    		tempdns=currprop[3].get(selected[index]).toString();
       	 try { 
				rt.exec("\"S:\\Software\\PuTTY\\putty.exe\" -i \"S:\\AWS\\KeyPairs\\"+tempdns.substring(tempdns.indexOf("|")+1)+".ppk\" ubuntu@"+tempdns.substring(0,tempdns.indexOf("|")));
       	 } catch (IOException e1) {
				System.out.println("Failed to exicute putty");
				e1.printStackTrace();
			}
       	 
       	}
       }
    	else if(e.getSource().equals(webapp)){
    		for(int index=0;index<selected.length;index++){
        		tempdns=currprop[3].get(selected[index]).toString();
        	try {
     			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://"+tempdns.substring(0,tempdns.indexOf("|"))+"/"+cmd.getText());
     		} catch (IOException net) {
     			// TODO Auto-generated catch block
     			net.printStackTrace();
     		
        }}
    	}
    	else if (e.getSource().equals(stopmaster)){
    		for(int index=0;index<selected.length;index++){
    			temphost=currprop[0].get(selected[index]).toString();
    			tempdns=currprop[3].get(selected[index]).toString();
    		if (temphost.equals("master")) {
    			try { 
    				rt.exec("\"S:\\Software\\PuTTY\\putty.exe\" -i \"S:\\AWS\\KeyPairs\\"+tempdns.substring(tempdns.indexOf("|")+1)+".ppk\" ubuntu@"+tempdns.substring(0,tempdns.indexOf("|"))+" -m \"S:\\Software\\PuTTY\\stopmaster.txt\"");
    			} catch (IOException e1) {
    				System.out.println("Failed to exicute putty");
    				e1.printStackTrace();
 			}
			}
    		else
    			JOptionPane.showMessageDialog(null, "Not a master");
    		}
    	}
    	else if (e.getSource().equals(startmaster)){
    		masterbox=new JComboBox(masterdomain);
    		if(JOptionPane.showConfirmDialog(null,masterbox,"Which master do you want to start",2)==0){
    		  Set<String> collection=new HashSet<String>();
    	    	    collection.add(((Instance)masters.get(masterbox.getSelectedIndex())).getInstanceId());
    	    	   StartInstancesRequest start=new StartInstancesRequest();
    	    	   start.setInstanceIds(collection);
    	    	  ec2[index].startInstances(start);
    		}
    			
    	}
    	else{	
    		for(int index=0;index<selected.length;index++){
        		tempdns=currprop[3].get(selected[index]).toString();
        	try {
     			Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler http://"+tempdns.substring(0,tempdns.indexOf("|"))+":8161");
     		} catch (IOException net) {
     			// TODO Auto-generated catch block
     			net.printStackTrace();
     		}}
        }}
       
    }

    /**
     * Initialization routine.
     */
    private static void init()   {
        AWSCredentials credentials=null;
        
        // Load in the AWSCredentials
		try {
			credentials = new PropertiesCredentials(PropertiesUtils.loadResource("src/config/AwsCredentials.properties"));
		} catch (IOException e) {
			System.out.println("Failed to load credentials");
			e.printStackTrace();
		}
		// Initialize the object with the credentials
        ec2[0] = new AmazonEC2Client(credentials);
        
        try {
			credentials = new PropertiesCredentials(PropertiesUtils.loadResource("src/config/AwsCredentials.properties"));
		} catch (IOException e) {
			System.out.println("Failed to load credentials");
			e.printStackTrace();
		}
		// Initialize the object with the credentials
        ec2[1] = new AmazonEC2Client(credentials);
        
        try {
			credentials = new PropertiesCredentials(PropertiesUtils.loadResource("src/config/AwsCredentials.properties"));
		} catch (IOException e) {
			System.out.println("Failed to load credentials");
			e.printStackTrace();
		}
		// Initialize the object with the credentials
        ec2[2] = new AmazonEC2Client(credentials);
    }

    /**
     * Get all the instances from AWS.
     * @return Array of instance objects.
     */
    protected  Object[] getData()   {

        System.out.println("===========================================");
        System.out.println("Welcome to the AWS Java SDK!");
        System.out.println("===========================================");
        Object[] test;
        
        /*
         * Amazon EC2
         *
         * The AWS EC2 client allows you to create, delete, and administer
         * instances programmatically.
         *
         * In this sample, we use an EC2 client to get a list of all the
         * availability zones, and all instances sorted by reservation id.
         */
        try {
            DescribeAvailabilityZonesResult availabilityZonesResult = ec2[index].describeAvailabilityZones();
            System.out.println("You have access to " + availabilityZonesResult.getAvailabilityZones().size() +
                    " Availability Zones.");

            DescribeInstancesResult describeInstancesRequest = ec2[index].describeInstances();
            List<Reservation> reservations = describeInstancesRequest.getReservations();
            Set<Instance> instances = new HashSet<Instance>();

            for (Reservation reservation : reservations) {
                instances.addAll(reservation.getInstances());
            }
           
            System.out.println("You have " + instances.size() + " Amazon EC2 instance(s) running.");
            test=  instances.toArray();
            return test;
            
        } catch (AmazonServiceException ase) {
                System.out.println("Caught Exception: " + ase.getMessage());
                System.out.println("Reponse Status Code: " + ase.getStatusCode());
                System.out.println("Error Code: " + ase.getErrorCode());
                System.out.println("Request ID: " + ase.getRequestId());
        }
        return new Object[0];

        
    }
    
    /**
     * Set the AmazonEC2Client client.
     * @param client AmazonEC2Client
     */
   
    

    private static void createAndShowGUI() {
    	for(int index=0;index<3;index++){
        frame[index]= new JFrame("Dev Console");
        frame[index].setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent newContentPane = new DevConsole(index);
        newContentPane.setOpaque(true); 
        frame[index].setContentPane(newContentPane); 
        frame[index].setLocation(100, 100);
        frame[index].pack();
    	}
        frame[0].setVisible(true);
        
    }

    public static void main(final String[] args) {
    	init();
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {

        	public void run() {
                createAndShowGUI();
               
            }
        });
    }
}
