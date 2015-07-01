package com.saic.rtws.commons.cloud.sweeper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.cli.CommandLine;
import org.jets3t.service.S3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DeleteVolumeRequest;
import com.amazonaws.services.ec2.model.Volume;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;

import com.saic.rtws.commons.cloud.platform.ServiceInterface;
import com.saic.rtws.commons.cloud.platform.StorageInterface;
import com.saic.rtws.commons.cloud.platform.aws.AwsConnectionFactory;
import com.saic.rtws.commons.cloud.platform.jetset.JetSetConnectionFactory;

/**
 * This tool is used to clean up unused EBS volumes in amazon. Any EBS volume
 * that is not a) currently attached to a running instance b) currently listed
 * in a processes.xml file of a stopped instance will be deleted.
 * 
 */
public class EBSVolumeSweeper {

	List<String> processRootBuckets;

	private S3Service s3Client;
	private AmazonEC2Client ec2Client;
	private List<String> unprocessedFiles;

	private Set<String> keptVolumes;
	private Set<String> whiteList;

	private XPathFactory xfactory;
	private XPath xpath;
	private XPathExpression xProcessExpr;
	private XPathExpression xVolumeExpr;
	private String xPathProcessString = "//item/volumes/id/text()";
	private String xPathVolumeString = "//volumes/id/text()";

	String[] whiteListStrs = { "vol-09eaf664", // 5 GiB ?
			"vol-0beaf666", // 5 GiB ?
			"vol-03eaf66e", // 1 TiB ?
			"vol-20896f4c", // 50 GiB possible raid ?
			"vol-22896f4e", // 50 GiB possible raid ?
			"vol-3c896f50", // 50 GiB possible raid ?
			"vol-38896f54", // 50 GiB possible raid ?
			"vol-764ba51a", // 1 GiB ?
			"vol-0288616e", // 1 GiB ?
			"vol-4bd87e27", // 50 GiB possible raid ?
			"vol-45d87e29", // 50 GiB possible raid ?
			"vol-43d87e2f", // 50 GiB possible raid ?
			"vol-5fd87e33", // 50 GiB possible raid ?
			"vol-f312829f", // 5 GiB ?
			"vol-aa2eb0c3", // Oracle
			"vol-e283e68b", // Oracle
			"vol-a25ac1cb", // MongoDB benchmark
			"vol-a054cf79", // MongoDB benchmark
			"vol-1454cf7d", // MongoDB benchmark
			"vol-ec54cf85", // MongoDB benchmark
			"vol-d654cfbf", // MongoDB benchmark
			"vol-c67b11af", // MongoDB benchmark
			"vol-8b53eae0", // CyberCEF Data
			"vol-70057519", // Dev1
			"vol-40177529", // Dev1
			"vol-50bd553a", // Dev-FAA
			"vol-7d992a10" // SGWatch
	};

	private boolean euca = true;

	/**
	 * Construct the ebs volume tool and do some initialization.
	 * 
	 * @throws XPathExpressionException
	 * 
	 */
	public EBSVolumeSweeper() {
	}

	private void fillWhiteList() {
		for (String s : whiteListStrs)
			whiteList.add(s);
	}

	public synchronized void addBucket(String bucket) {
		if (processRootBuckets == null) {
			processRootBuckets = new ArrayList<String>();
		}
		processRootBuckets.add(bucket);
	}

	public void sweep(CommandLine line) throws Exception {

		String accessKey = line.getOptionValue(Sweeper.ACCESS_KEY_KEY);
		String secretKey = line.getOptionValue(Sweeper.SECRET_KEY_KEY);
		boolean remove = line.hasOption(Sweeper.REMOVE_OPT_KEY);
		String serviceEndpointHost = line.getOptionValue(Sweeper.SERVICE_ENDPOINT_HOST_KEY);
		String serviceEndpointPort = line.getOptionValue(Sweeper.SERVICE_ENDPOINT_PORT_KEY);
		String serviceEndpointStoragePath = line.getOptionValue(Sweeper.SERVICE_ENDPOINT_STORAGE_PATH_KEY);
		String serviceEndpointServicePath = line.getOptionValue(Sweeper.SERVICE_ENDPOINT_SERVICE_PATH_KEY);
		
		//AwsConnectionFactory awsConnectionFactory = new AwsConnectionFactory(accessKey, secretKey);
		//awsConnectionFactory.setServiceEndpoint(serviceEndpointHost);

		unprocessedFiles = new ArrayList<String>();
		keptVolumes = new HashSet<String>();
		whiteList = new HashSet<String>();
		fillWhiteList();
		xfactory = XPathFactory.newInstance();
		xpath = xfactory.newXPath();
		xProcessExpr = xpath.compile(xPathProcessString);
		xVolumeExpr = xpath.compile(xPathVolumeString);

		JetSetConnectionFactory jsFactory = new JetSetConnectionFactory();
		jsFactory.setCredentials(accessKey, secretKey);
		jsFactory.setStorageEndpoint(serviceEndpointHost);
		jsFactory.setStoragePortNumber(serviceEndpointPort);
		jsFactory.setStorageVirtualPath(serviceEndpointStoragePath);
		
		s3Client = jsFactory.getJets3tService();
		//s3Client = awsConnectionFactory.getAmazonS3Client(accessKey, secretKey);
		ec2Client = new AmazonEC2Client(new BasicAWSCredentials(accessKey, secretKey));
		String otherSvc = "/services/Eucalyptus";
		ec2Client.setEndpoint("http://"+serviceEndpointHost+":"+serviceEndpointPort+serviceEndpointServicePath);
		S3Bucket[] buckets = s3Client.listAllBuckets();
		//List<Bucket> buckets = s3Client.listBuckets();
		for (S3Bucket newBucket : buckets) {
			addBucket(newBucket.getName());

		}

		for (String bucket : processRootBuckets) {
			List<String> processFiles = new ArrayList<String>();

			// find volumes that are in process.xml files
			//s3Client.listObj
			S3Object[] objects = s3Client.listObjects(bucket, "process","");
			//ObjectListing objects = s3Client.listObjects(bucket, "process");
			///List<S3ObjectSummary> objs = objects.getObjectSummaries();

			for (S3Object s : objects) {
				if (s.getKey().endsWith(".xml")) {
					System.out.println(bucket + ":" + s.getKey());
					processFiles.add(s.getKey());
				}
			}

			System.out.println("got " + processFiles.size() + " Process Files from bucket " + bucket);
			for (String procFil : processFiles) {
				handleProcessFile(bucket, procFil);
			}
		}
		System.out.println("Found " + keptVolumes.size() + " volumes in process files.  They will be kept.");

		// find attached volumes

		List<Volume> vols = ec2Client.describeVolumes().getVolumes();
		int removeSize = 0;
		int keepSize = 0;
		int numKept = 0;
		List<Volume> toRemove = new ArrayList<Volume>();
		System.out.println("Found volumes");
		for (Volume v : vols) {
			if (v.getState().equalsIgnoreCase("available") && !keptVolumes.contains(v.getVolumeId())
					&& !whiteList.contains(v.getVolumeId())) {
				toRemove.add(v);
				removeSize += v.getSize();
				System.out.println("Deleting: " + v);
			} else {
				numKept++;
				keepSize += v.getSize();
				System.out.println("Keeping: " + v);
			}
		}

		System.out.println("Decided to keep " + numKept + " volumes, because they are in use.");
		System.out.println("Decided to remove " + toRemove.size() + " volumes.");
		System.out.println("Total size of volumes keeping is " + keepSize + " GB");
		System.out.println("Total size of volumes to remove is " + removeSize + " GB");
		System.out.println("UNPROCESSED FILES: ");
		for (int i = 0; i < unprocessedFiles.size(); i++)
			System.out.println("\t" + unprocessedFiles.get(i));

		ArrayList<PurgedVolume> purges = new ArrayList<PurgedVolume>();
		for (Volume v : toRemove) {
			purges.add(new PurgedVolume(v.getVolumeId(), v.getCreateTime(), v.getSize()));
		}
		Collections.sort(purges);
		for (PurgedVolume s : purges) {
			String delInfo = s.getCreateTime() + " Size [" + s.getSize() + "] Remove this volume: "
					+ s.getVolumeId();
			Sweeper.delStream.println(delInfo);
			//System.out.println(delInfo);
		}

		if (remove) {
			DeleteVolumeRequest delVol = new DeleteVolumeRequest();
			for (Volume v : toRemove) {
				delVol.setVolumeId(v.getVolumeId());
				ec2Client.deleteVolume(delVol);
			}
		}

	}

	private void handleProcessFile(String bucket, String fileLocation) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true); // never forget this!
		DocumentBuilder builder = factory.newDocumentBuilder();
		S3Object obj;
		InputStream is = null;
		Document d;

		try {

			obj = s3Client.getObject(bucket, fileLocation);
			
			d = builder.parse(obj.getDataInputStream());

			Object res = xProcessExpr.evaluate(d, XPathConstants.NODESET);
			NodeList nodes = (NodeList) res;
			for (int i = 0; i < nodes.getLength(); i++) {
				String volName = nodes.item(i).getNodeValue();
				keptVolumes.add(volName);
				System.out.println("Volume " + volName + " exists in file [" + fileLocation + "], and will be kept.");
			}
			
			res = xVolumeExpr.evaluate(d, XPathConstants.NODESET);
			nodes = (NodeList) res;
			for (int i = 0; i < nodes.getLength(); i++) {
				String volName = nodes.item(i).getNodeValue();
				keptVolumes.add(volName);
				System.out.println("Volume " + volName + " exists in file [" + fileLocation + "], and will be kept.");
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.out.println("ACCESS DENIED TO FILE: " + fileLocation);
			System.out.println("FILE WILL NOT BE PROCESSED.");
			unprocessedFiles.add(fileLocation);
		}
	}

	private class PurgedVolume implements Comparable<Object> {
		private String volumeId;
		private java.util.Date createTime;
		private Integer size;

		public PurgedVolume(String vid, java.util.Date created, Integer size) {
			this.volumeId = vid;
			this.createTime = created;
			this.size = size;
		}

		public String getVolumeId() {
			return volumeId;
		}

		public java.util.Date getCreateTime() {
			return createTime;
		}

		public Integer getSize() {
			return size;
		}

		@Override
		public int compareTo(Object o) {
			return createTime.compareTo(((PurgedVolume) o).getCreateTime());
		}
	}

}
