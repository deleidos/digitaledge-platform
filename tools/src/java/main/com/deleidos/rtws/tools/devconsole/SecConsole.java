package com.deleidos.rtws.tools.devconsole;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.AuthorizeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.CreateSecurityGroupRequest;
import com.amazonaws.services.ec2.model.DeleteSecurityGroupRequest;
import com.amazonaws.services.ec2.model.DescribeSecurityGroupsResult;
import com.amazonaws.services.ec2.model.IpPermission;
import com.amazonaws.services.ec2.model.RevokeSecurityGroupIngressRequest;
import com.amazonaws.services.ec2.model.SecurityGroup;
import com.amazonaws.services.ec2.model.UserIdGroupPair;
import com.deleidos.rtws.commons.config.PropertiesUtils;


/**
 * Development Console for AWS environment.
 */
@SuppressWarnings("serial")
public final class SecConsole extends JPanel implements ActionListener   {
    private JTable table;
    private JTextField from,to,range;
    private JButton delete;
    private JComboBox groups;
    private static int lastselected=0;
    private JButton addButton, updateButton ,last, backup, restor,deletegroup, creategroup;
    private JList protocol, selectgourps;
    private JScrollPane listScrollPane, chooseScrollPane;
    private JFileChooser fc;
    static AmazonEC2 ec2;
    static JFrame frame ;
    static List<SecurityGroup> input;
    private List<IpPermission> permissions = new ArrayList<IpPermission>();

    
    /** Constructor. */
    private SecConsole() {
        super(new BorderLayout());
        input = getData();
        Object groupname[]= new Object[input.size()];
        for (int c=0;c<input.size();c++){
			   groupname[c]=input.get(c).getGroupName();
        }
        
        permissions = input.get(lastselected).getIpPermissions();
        
      //  Object[][] data=new Object[permissions.size()][4]; 
      //  for(int index=0;index<permissions.size();index++){
       // 	data[index][0]=permissions.get(index).getIpProtocol();
       //     data[index][1]=permissions.get(index).getFromPort();
       // 	data[index][2]=permissions.get(index).getToPort();
       // 	data[index][3]=permissions.get(index).getIpRanges();
       // }
      //  table=new JTable(data,new String[]{"Protocol","From","To","Ranges"});
        selectgourps=new JList(groupname);
        selectgourps.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        protocol=new JList(new Object[]{"TCP","UDP","ICMP"});
        protocol.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        protocol.setSelectedIndex(0);
        chooseScrollPane= new JScrollPane(selectgourps);
        listScrollPane = new JScrollPane(table);
        groups= new JComboBox(groupname);
        groups.addActionListener(this);
        groups.setSelectedIndex(lastselected);
        table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        addButton=new JButton("Add");
        addButton.addActionListener(new SelectListener());
        updateButton=new JButton("Reload");
        updateButton.addActionListener(new UpdateListener());
        delete =new JButton("Remove");
        delete.addActionListener(new SelectListener());
        backup =new JButton("Backup Groups");
        //backup.addActionListener(new SelectListener());
        backup.setEnabled(false);
        creategroup=new JButton("Create Group");
        creategroup.addActionListener(new SelectListener());
        deletegroup=new JButton("Delete Group");
        deletegroup.addActionListener(new SelectListener());
        restor=new JButton("Restor Group");
        //restor.addActionListener(new SelectListener());
        restor.setEnabled(false);        
        from=new JTextField("0");
        to=new JTextField("0");
        range=new JTextField("0.0.0.0/0");
        add(listScrollPane,BorderLayout.CENTER);
        JPanel buttons=new JPanel();
        buttons.setLayout(new BoxLayout(buttons,
                BoxLayout.Y_AXIS));
        JPanel newip=new JPanel();
        newip.setLayout(new BoxLayout(newip,
                BoxLayout.X_AXIS));
        JPanel tofrom=new JPanel();
        tofrom.setLayout(new BoxLayout(tofrom,
                BoxLayout.Y_AXIS));
        newip.add(new JLabel("Add new rule:                                       "));
        newip.add(new JSeparator(1));
        newip.add(new JLabel(" Protocol:"));
        newip.add(new JSeparator(1));
        newip.add(protocol);
        newip.add(new JSeparator(1));
        newip.add(new JLabel("  Port Range: "));
        tofrom.add(new JLabel("From:"));
        tofrom.add(from);
        tofrom.add(new JLabel("To:"));
        tofrom.add(to);
        newip.add(tofrom);
        newip.add(new JLabel(" IP Range(s):"));
        newip.add(range);
        newip.add(new JLabel(" Groups:"));
        newip.add(chooseScrollPane);
        
        add(newip,BorderLayout.SOUTH);
        buttons.add(new JLabel("Domains"));
        buttons.add(groups);
        buttons.add(new JSeparator());
        buttons.add(updateButton);
        buttons.add(new JSeparator());
        buttons.add(creategroup);
        buttons.add(deletegroup);
        buttons.add(backup);
        buttons.add(restor); 
        buttons.add(new JSeparator());
        buttons.add(addButton); 
        buttons.add(delete);
        add(buttons,BorderLayout.WEST);
        fc =new JFileChooser();
        
        	
    }
    
   // class ChangeListener implements ActionListener {
        
        public void actionPerformed(final ActionEvent e) {
        	this.remove(listScrollPane);
        	int pos=groups.getSelectedIndex();
            String groupnames="";
        	permissions = input.get(pos).getIpPermissions();
            Object[][] data=new Object[permissions.size()][5]; 
            for(int index=0;index<permissions.size();index++){
            	data[index][0]=permissions.get(index).getIpProtocol();
                data[index][1]=permissions.get(index).getFromPort();
            	data[index][2]=permissions.get(index).getToPort();
            	data[index][3]=permissions.get(index).getIpRanges();
            	for(int c=0;c<permissions.get(index).getUserIdGroupPairs().size();c++){
            		groupnames+=permissions.get(index).getUserIdGroupPairs().get(c).getGroupName()+ ", ";
            	}
            	data[index][4]=groupnames;
            	groupnames="";
            	//System.out.println(permissions.get(index));
            }
             table=new JTable(data,new String[]{"Protocal","From","To","Ranges", "Groups"});
             listScrollPane = new JScrollPane(table);
             this.add(listScrollPane,BorderLayout.CENTER);
             Dimension d= frame.getSize();
             frame.pack();
             frame.setSize(d);
        }
    //    }
        class UpdateListener implements ActionListener {
            
            public void actionPerformed(final ActionEvent e) {
            	reload(true);
            }}
        class LoadListener implements ActionListener {
            
            public void actionPerformed(final ActionEvent e) {
            	String path="";
            	if (e.getSource().equals(last)){
            		path="S:\\Software\\PuTTY\\script.txt";
            	}
            	else{
            	 int returnVal = fc.showOpenDialog(SecConsole.this);
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
						//protocol.setText(newcmd);
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						// 
						e1.printStackTrace();
					}
                 
            }}
    class SelectListener implements ActionListener {
        
        public void actionPerformed(final ActionEvent e) {
    
        	if (e.getSource().equals(delete)){
        		List<IpPermission> tempper = new ArrayList<IpPermission>();
        		IpPermission temp=new IpPermission();
        		int select[]=table.getSelectedRows();
        		for(int c=0;c<select.length;c++){
        			temp=(input.get(groups.getSelectedIndex()).getIpPermissions().get(select[c]));
        			for(int i=0;i<temp.getUserIdGroupPairs().size();i++){
        			temp.getUserIdGroupPairs().get(i).setGroupId(null);
        			}
        			tempper.add(temp);
        		}
        		RevokeSecurityGroupIngressRequest r1 = new RevokeSecurityGroupIngressRequest();
 			   	r1.setGroupName(groups.getSelectedItem().toString());
 			   	r1.setIpPermissions(tempper);
				ec2.revokeSecurityGroupIngress(r1);
				reload(true);
        	}
        
        	else if (e.getSource().equals(addButton)){
        		AuthorizeSecurityGroupIngressRequest r2 = new AuthorizeSecurityGroupIngressRequest();
        		r2.setGroupName(groups.getSelectedItem().toString());
        		IpPermission permission = new IpPermission();
        		permission.setIpProtocol(protocol.getSelectedValue().toString().toLowerCase());
        		permission.setFromPort(Integer.decode(from.getText()));
        		permission.setToPort(Integer.decode(to.getText()));
        		List<String> ipRanges = new ArrayList<String>();
        		String[] iprange=range.getText().replace(" ", "").split(",");
        		for(int c=0;c<iprange.length;c++)
        			ipRanges.add(iprange[c]);
        		List<UserIdGroupPair> uid=new ArrayList<UserIdGroupPair>();
        		int[] selected=selectgourps.getSelectedIndices();
        		for(int c=0;c<selected.length;c++){
        			UserIdGroupPair temp=new UserIdGroupPair();
        			temp.setGroupName(input.get(selected[c]).getGroupName()); 			
        			uid.add(temp);
        		}
        		if (!uid.isEmpty())
        			permission.setUserIdGroupPairs(uid);
        		if(range.getText().length()>2&&!ipRanges.isEmpty())
        			permission.setIpRanges(ipRanges);			
        		List<IpPermission> permissionlist = new ArrayList<IpPermission>();
        		permissionlist.add(permission);
        		r2.setIpPermissions(permissionlist);
        		try{
        			ec2.authorizeSecurityGroupIngress(r2);
        		}catch(AmazonClientException e1){
        			JOptionPane.showMessageDialog(null, "Failed to add: "+e1.getMessage());
        		}
        		reload(true);
        	}
 	   	else if(e.getSource().equals(restor)){
 	   		
    	}
    	else if (e.getSource().equals(deletegroup)){
    		if(JOptionPane.showConfirmDialog(null,"Are you sure you want to delete this group?")==0){
    			DeleteSecurityGroupRequest remover=new DeleteSecurityGroupRequest();
    			remover.setGroupName(groups.getSelectedItem().toString());
    			ec2.deleteSecurityGroup(remover);
    			reload(true);
    		}
    	}
    	else if (e.getSource().equals(creategroup)){
    		JTextField name=new JTextField("Name");
    		JTextField discription=new JTextField("Description");
    		JPanel create=new JPanel();
            create.setLayout(new BoxLayout(create,
                    BoxLayout.Y_AXIS));
            create.add(name);
            create.add(discription);
    		if(JOptionPane.showConfirmDialog(null,create,"New Security Group:",2)==0&&name.getText().trim().length()>1){
    			CreateSecurityGroupRequest r1 = new CreateSecurityGroupRequest(name.getText().trim(),discription.getText().trim());
    			try{
    				ec2.createSecurityGroup(r1);
    			}catch(AmazonClientException ex){
    				JOptionPane.showMessageDialog(null, "Group already exists");
    			};
    			reload(true);  
    			int c=0;
    			for(;!name.getText().trim().equals(input.get(c).getGroupName());c++){};
    			lastselected=c;
    			reload(false);
    		}
    	}
    	else{	
    		
    	}
        }
       
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
        ec2 = new AmazonEC2Client(credentials);
    }

    /**
     * Get all the instances from AWS.
     * @return Array of instance objects.
     */
    protected static List<SecurityGroup> getData()   {

		//CreateSecurityGroupRequest r1 = new CreateSecurityGroupRequest("SecConsole", "This is Elan's test security group");
		//ec2.createSecurityGroup(r1);
		DescribeSecurityGroupsResult describeSecurityGroupsResult =  ec2.describeSecurityGroups();
		List<SecurityGroup> securityGroups =  describeSecurityGroupsResult.getSecurityGroups();
		return securityGroups;
        
    }
    
    /**
     * Set the AmazonEC2Client client.
     * @param client AmazonEC2Client
     */
    
    private void reload(boolean changeindex){
    	if(changeindex)
    		lastselected=groups.getSelectedIndex();
    	Dimension d= frame.getSize();
    	JComponent newContentPane = new SecConsole();
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane); 
        frame.pack();
        frame.setSize(d);
    }

    private static void createAndShowGUI() {
        frame= new JFrame("Sec Console");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JComponent newContentPane = new SecConsole();
        newContentPane.setOpaque(true); 
        frame.setContentPane(newContentPane); 
        frame.setLocation(100, 100);
        frame.pack();
        frame.setVisible(true);
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
