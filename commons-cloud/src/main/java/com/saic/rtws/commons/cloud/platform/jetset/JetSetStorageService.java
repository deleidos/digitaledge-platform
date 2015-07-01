package com.saic.rtws.commons.cloud.platform.jetset;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.StorageObjectsChunk;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.CanonicalGrantee;
import org.jets3t.service.acl.EmailAddressGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.model.StorageOwner;

import com.saic.rtws.commons.cloud.exception.StorageException;
import com.saic.rtws.commons.cloud.platform.StorageInterface;

public class JetSetStorageService implements StorageInterface {

	private static final Logger log = Logger.getLogger(JetSetStorageService.class);

	/** Local Jets3t client created by the factory */
	private S3Service s3client;

	/** The connection factory being used to access the Amazon services API. */
	private JetSetConnectionFactory factory;

	public JetSetStorageService() {
		super();
	}

	/**
	 * Get a singleton of the S3Service client.
	 */
	public S3Service getClient()
			throws StorageException {
		if (s3client == null) {
			try {
				s3client = factory.getJets3tService();
			}

			catch (S3ServiceException s3e) {
				log.fatal("JetS3t factory call failed! ", s3e);
				throw new StorageException("JetS3t factory failed!", s3e);
			}
		}
		return s3client;
	}

	public JetSetConnectionFactory getConnectionFactory() {
		return factory;
	}

	public void setConnectionFactory(JetSetConnectionFactory value) {
		factory = value;
	}

	@Override
	public boolean bucketExists(String bucketName)
			throws StorageException {
		try {
			S3Bucket bucket = getClient().getBucket(bucketName);
			return bucket != null;
		}

		catch (ServiceException s3e) {
			throw new StorageException("Jets3t failure in bucketExists()", s3e);
		}
	}

	@Override
	public void createBucket(String bucketName)
			throws StorageException {
		try {
			S3Bucket bucket = getClient().getBucket(bucketName);
			if (bucket == null) {
				bucket = new S3Bucket(bucketName);
				bucket.setAcl(AccessControlList.REST_CANNED_PRIVATE);
				bucket = getClient().createBucket(bucket);
			}
		}

		catch (S3ServiceException s3e) {
			throw new StorageException("Jets3t failure in createBucket()", s3e);
		}
	}

	@Override
	public boolean deleteBucket(String bucketName)
			throws StorageException {
		try {
			S3Object[] allObjects = getClient().listObjects(bucketName);
			if (allObjects != null && allObjects.length > 0) {
				// bucket is not empty
				return false;
			}
			getClient().deleteBucket(bucketName);
			return true;
		}

		catch (ServiceException s3e) {
			throw new StorageException("Jets3t failure in deleteBucket()", s3e);
		}
	}

	@Override
	public boolean fileExists(String bucketName, String fileKey)
			throws StorageException {
		try {
			// handle Eucalyptus Walrus limitation of not matching '/' in the prefix of listObjects()
			String prefix = new String(fileKey);
			if (prefix.indexOf('/') >= 0) {
				prefix = prefix.substring(0, prefix.indexOf('/'));
			}

			// Grab the list of STORAGE OBJECTS in the bucketName that start with the fileKey
			S3Object[] storageObjects = getClient().listObjects(bucketName, prefix, null);
			if (storageObjects != null) {

				// See if object name exactly matches the fileKey
				for(S3Object storageObject : storageObjects) {
					String objectKey = "";
					try {
						objectKey = URLDecoder.decode(storageObject.getKey(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						objectKey = storageObject.getKey();
					}

					if (objectKey.equals(fileKey)) {
						// an exact match is found
						return true;
					}
				}
			}
		}

		catch (S3ServiceException s3e) {
			log.error("Storage Error Message: " + s3e.getMessage() + " Code: " + s3e.getErrorCode(), s3e);
			throw new StorageException("Jets3t failure in fileExists()", s3e);
		}
		
		// File was not found.
		return false;
	}

	@Override
	public String generateUniqueFileKey(String bucketName, String desiredFileKey)
			throws StorageException {

		// Remove extension and get all files starting with the desiredFileKey prefix
		int dot = desiredFileKey.lastIndexOf('.');
		String prefix = (dot == -1 ? desiredFileKey : desiredFileKey.substring(0, dot));

		// handle Eucalyptus Walrus limitation of not matching '/' in the prefix of listObjects()
		if (prefix.indexOf('/') >= 0) {
			prefix = prefix.substring(0, prefix.indexOf('/'));
		}

		List<String> allMatches = new ArrayList<String>();
		try {
			S3Object[] storageObjects = getClient().listObjects(bucketName, prefix, null);
			if (storageObjects != null && storageObjects.length > 0) {

				// There may be existing duplicate fileKeys
				for(S3Object storageObject : storageObjects) {
					try {
						allMatches.add(URLDecoder.decode(storageObject.getKey(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						allMatches.add(storageObject.getKey());
					}
				}

			}
		}

		catch (S3ServiceException s3e) {
			throw new StorageException("Jets3t failure in generateUniqueFileKey()", s3e);
		}

		return nextOneUpFileKey(desiredFileKey, allMatches);
	}

	@Override
	public void getFile(String fileURL, File destFile)
			throws StorageException {
		try {
			String[] bucketAndKey = bucketAndKeyFromURL(fileURL);
			getFile(bucketAndKey[0], bucketAndKey[1], destFile);
		}

		catch (MalformedURLException ue) {
			throw new StorageException("Cannot get file at invalid URL [" + fileURL + "]", ue);
		}
	}

	@Override
	public void getFileWithRetry(String fileURL, File destFile, int retryCount, int retryDelay)
			throws StorageException {
		try {
			String[] bucketAndKey = bucketAndKeyFromURL(fileURL);
			getFileWithRetry(bucketAndKey[0], bucketAndKey[1], destFile, retryCount, retryDelay);
		}

		catch (MalformedURLException ue) {
			throw new StorageException("Cannot get file at invalid URL [" + fileURL + "]", ue);
		}
	}

	@Override
	public void getFile(String bucketName, String fileKey, File destFile)
			throws StorageException {
		S3Object s3Object = null;
		try {
			s3Object = getClient().getObject(bucketName, fileKey);
			
			// Use the data in the object to write the file.
			try {
				writeToFile(s3Object.getDataInputStream(), destFile);
			}

			catch(IOException ioe) {
				throw new StorageException("Failed to write to filesystem: [" + destFile.getAbsolutePath() + "]", ioe);
			}
			catch (ServiceException s3e) {
				throw new StorageException("Jets3t failure in getDataInputStream() from bucket [" + bucketName + "] fileKey [" + fileKey + "]", s3e);
			}
		}

		catch (S3ServiceException s3e) {
			throw new StorageException("Jets3t failure in getFile() getObject()", s3e);
		}

		finally {
			if (s3Object != null) {
				try {
					s3Object.closeDataInputStream();
				} catch(IOException ignore) {}
			}
		}

	}
	
	@Override
	public InputStream getFile(String bucketName, String fileKey) 
			throws StorageException {
		InputStream is = null;
		
		try {
			S3Object s3Object = null;
			s3Object = getClient().getObject(bucketName, fileKey);
			is = s3Object.getDataInputStream();
		}
		catch (ServiceException s3e) {
			throw new StorageException("Jets3t failure in getDataInputStream() from bucket [" + bucketName + "] fileKey [" + fileKey + "]", s3e);
		}
		
		return is;
	}

	@Override
	public void getFileWithRetry(String bucketName, String fileKey, File destFile, int retryCount, int retryDelay)
			throws StorageException {
		for(int retry = 1; retry <= retryCount; retry++) {
			try {
				getFile(bucketName, fileKey, destFile);
				// File has been downloaded, return.
				break;
			}

			catch (Exception e) {
				if (retry >= retryCount) {
					throw new StorageException("Failed to retrieve file: [" + fileKey + "] from storage environment, retry exhausted.", e);
				}
				try {
					Thread.sleep(retryDelay);
				}
				catch(InterruptedException ie) {
					// Restore the interrupted status, re-interrupt main thread so that it can be passed up the hierarchy.
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	@Override
	public String getFileAccessURL(String bucketName, String fileKey)
			throws StorageException {
		// This URL will be good for 1 day from now
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		try {
			return getClient().createSignedGetUrl(bucketName, fileKey, cal.getTime());
		}

		catch (S3ServiceException s3e) {
			throw new StorageException("Jets3t failure in getFileAccessURL()", s3e);
		}
	}

	@Override
	public List<String> listBuckets() throws StorageException {
		List<String> result = new ArrayList<String>();

		try {
			S3Bucket[] allBuckets = getClient().listAllBuckets();
			if (allBuckets != null) {
				for (S3Bucket bucket : allBuckets) {
					result.add(bucket.getName());
				}
			}
		}

		catch (S3ServiceException s3e) {
			throw new StorageException("Jets3t failure in listBuckets()", s3e);
		}

		return result;
	}

	@Override
	public List<String> listFiles(String bucketName) throws StorageException {
		List<String> result = new ArrayList<String>();

		try {
			S3Object[] allObjects = getClient().listObjects(bucketName);
			if (allObjects != null) {
				for (S3Object obj : allObjects) {
					try {
						result.add(URLDecoder.decode(obj.getKey(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						result.add(obj.getKey());
					}
				}
			}
		}

		catch (S3ServiceException s3e) {
			throw new StorageException("Jets3t failure in listFiles()", s3e);
		}

		return result;
	}

	@Override
	public void removeFile(String fileURL)
			throws StorageException {
		try {
			String[] bucketAndKey = bucketAndKeyFromURL(fileURL);
			removeFile(bucketAndKey[0], bucketAndKey[1]);
		}

		catch (MalformedURLException ue) {
			throw new StorageException("Cannot remove storage file at invalid URL [" + fileURL + "]", ue);
		}
	}

	@Override
	public void removeFile(String bucketName, String fileKey)
			throws StorageException {
		try {
			getClient().deleteObject(bucketName, fileKey);
		}

		catch (ServiceException s3e) {
			throw new StorageException("Jets3t failure in removeFile()", s3e);
		}
	}

	@Override
	public void storeFile(String bucketName, String fileKey, File sourceFile)
			throws StorageException {
		storeFile(bucketName, fileKey, sourceFile, null, null, false);
	}

	@Override
	public void storeFileWithRetry(String bucketName, String fileKey, File sourceFile, int retryCount, int retryDelay)
			throws StorageException {
		storeFileWithRetry(bucketName, fileKey, sourceFile, null, null, false, retryCount, retryDelay);
	}

	@Override
	public void storeFile(String bucketName, String fileKey, File sourceFile,
			String contentType, String encoding, boolean serverEncryption)
			throws StorageException {
		if (sourceFile == null || !sourceFile.exists()) {
			throw new StorageException("Source file [" +
					sourceFile == null ? "null" : sourceFile.getAbsolutePath()
					+ "] cannot be found");
		}

		S3Object s3Object = null;
		S3Object object = null;
		try {
			s3Object = new S3Object(sourceFile);
			s3Object.setKey(fileKey);
			s3Object.setAcl(AccessControlList.REST_CANNED_PRIVATE);
			if (StringUtils.isNotBlank(contentType)) {
				s3Object.setContentType(contentType);
			}
			if (StringUtils.isNotBlank(encoding)) {
				s3Object.setContentEncoding(encoding);
			}
			
			object = getClient().putObject(bucketName, s3Object);
		}
		catch (NoSuchAlgorithmException nse) {
			throw new StorageException("MD5 hash algorithm is not supported!", nse);
		}
		catch (IOException ioe) {
			throw new StorageException("Failure accessing file [" + sourceFile.getAbsolutePath() + "]", ioe);
		}
		catch (S3ServiceException s3e) {
			log.error(s3e.toString(), s3e);
			throw new StorageException("Jets3t failure in storeFile()", s3e);
		}
		finally {
			if (s3Object != null) {
				try {
					s3Object.closeDataInputStream();
					s3Object = null;
				} catch(IOException ignore) {}
			}
			if (object != null) {
				try {
					object.closeDataInputStream();
					object = null;
				} catch(IOException ignore) {}
			}
		}
	}

	@Override
	public void storeFileWithRetry(String bucketName, String fileKey,
			File sourceFile, String contentType, String encoding,
			boolean serverEncryption, int retryCount, int retryDelay)
			throws StorageException {
		for(int retry = 1; retry <= retryCount; retry++) {
			try {
				storeFile(bucketName, fileKey, sourceFile, null, null, false);
				// File has been uploaded, return.
				break;
			}

			catch (Exception e) {
				if (retry >= retryCount) {
					throw new StorageException("Failed to store file: [" + fileKey + "] to storage environment, retry exhausted.", e);
				}
				try {
					Thread.sleep(retryDelay);
				}
				catch(InterruptedException ie) {
					// Restore the interrupted status, re-interrupt main thread so that it can be passed up the hierarchy.
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	/*
	 * Returns the desired fileKey if it's not in the list of existing files,
	 * but if it is then we append a one-up number before the file extension
	 * 
	 * Separated from generateUniqueFileKey() to make this testable without making a remote jets3t call
	 */
	protected String nextOneUpFileKey(String desiredFileKey, List<String> fileKeys) {
		// By default you can use the desiredFileKey unless there are collisions
		String result = desiredFileKey;
		boolean desiredFileKeyWasFound = false;

		// split desired fileKey into prefix and extension
		int dot = desiredFileKey.lastIndexOf('.');
		String desiredPrefix = (dot == -1 ? desiredFileKey : desiredFileKey.substring(0, dot));
		String desiredDotPlusExt = (dot == -1 ? "" : desiredFileKey.substring(dot));

		int maxSuffix = 0;
		for (String fileKey : fileKeys) {
			// check for desiredFileKey exact match
			if (fileKey.equals(desiredFileKey)) {
				desiredFileKeyWasFound = true;
			}

			// split this fileKey into prefix and extension
			int fnDot = fileKey.lastIndexOf('.');
			String fnPrefix = (fnDot == -1 ? fileKey : fileKey.substring(0, fnDot));
			String fnDotPlusExt = (fnDot == -1 ? "" : fileKey.substring(fnDot));

			// Did we find a fnPrefix that already ends in a one-up number after a dash?
			if (desiredDotPlusExt.equals(fnDotPlusExt) &&
					fnPrefix.matches(desiredPrefix + "-\\d+$")) {
				try {
					Integer last = new Integer(fnPrefix.substring(fnPrefix.lastIndexOf('-') + 1));
					if (last.intValue() > maxSuffix) {
						maxSuffix = last.intValue();
					}
				}
				catch (NumberFormatException ignore) {}
			}
		}

		if (desiredFileKeyWasFound) {
			StringBuffer resultBuffer = new StringBuffer(desiredPrefix);
			resultBuffer.append("-");
			resultBuffer.append(String.valueOf(maxSuffix + 1));
			resultBuffer.append(desiredDotPlusExt);
			result = resultBuffer.toString();
		}

		return result;
	}

	protected String[] bucketAndKeyFromURL(String url)
			throws MalformedURLException {
		URL urlFormat = new URL(url);
		String path = urlFormat.getPath();

		String s3EndpointVirtualPath = factory.getStorageVirtualPath();
		if (s3EndpointVirtualPath != null && path.startsWith(s3EndpointVirtualPath)) {
			path = path.substring(s3EndpointVirtualPath.length());
		}
		if (path.charAt(0) == '/') {
			path = path.substring(1);
		}

		int firstSlash = path.indexOf('/');
		String[] results = new String[2];
		results[0] = path.substring(0, firstSlash);
		results[1] = path.substring(firstSlash + 1);

		return results;
	}

	protected void writeToFile(InputStream stream, File destFile)
			throws IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(destFile);
			IOUtils.copy(stream, out);
		} finally {
			IOUtils.closeQuietly(out);
		}
	}

	
	public void changeACLOnBucket(String bucketName, String grantee, Permission permission, boolean recursive) throws StorageException {
		try{
			//get bucket
			S3Bucket bucket = getClient().getBucket(bucketName);
		
			//change acl permissions
			AccessControlList acl = getClient().getBucketAcl(bucketName);
			
			//if acls found then append new grantee
			if(acl != null){
				if(grantee.contains("@") == true){
					acl.grantPermission(new EmailAddressGrantee(grantee), permission);
				}
				else{
					acl.grantPermission(new CanonicalGrantee(grantee), permission);
				}
			}
			//if no acls found for bucket reset
			else{
				acl = new AccessControlList();
				if(grantee.contains("@") == true){
					acl.grantPermission(new EmailAddressGrantee(grantee), permission);
					acl.grantPermission(new CanonicalGrantee(bucket.getOwner().getId()), Permission.PERMISSION_FULL_CONTROL);
				}
				else{
					acl.grantPermission(new CanonicalGrantee(grantee), permission);
					acl.grantPermission(new CanonicalGrantee(bucket.getOwner().getId()), Permission.PERMISSION_FULL_CONTROL);
				}
				acl.setOwner(bucket.getOwner());
			}
		
			//update bucket acl
			bucket.setAcl(acl);
			getClient().putBucketAcl(bucket);
			
			if (recursive)
				changeFilesACL(bucketName, grantee, permission, bucket.getOwner());
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
		
	}
	
	public void changeACLOnBucket(String bucketName, String grantee, Permission permission) throws StorageException{
		changeACLOnBucket(bucketName, grantee, permission, true);
	}
	
	public void changeFilesACL(String bucketName, String prefix, String grantee, Permission permission) throws StorageException{
		try{
			StorageObjectsChunk objectChunks = getClient().listObjectsChunked(bucketName, prefix, null, 1000, null, true);
			for(StorageObject object : objectChunks.getObjects()){
				AccessControlList acl = getClient().getObjectAcl(bucketName, object.getKey());
				acl = changeFileACL(acl, grantee, permission, null);
				getClient().putObjectAcl(bucketName, object.getKey(), acl);
			}
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}
	
	public void changeFilesACL(String bucketName, String prefix, List<String> grantees, Permission permission)
			throws StorageException {
		try{
			StorageObjectsChunk objectChunks = getClient().listObjectsChunked(bucketName, prefix, null, 1000, null, true);
			for(StorageObject object : objectChunks.getObjects()){
				AccessControlList acl = getClient().getObjectAcl(bucketName, object.getKey());
				for (String grantee : grantees) {
					acl = changeFileACL(acl, grantee, permission, null);
				}
				getClient().putObjectAcl(bucketName, object.getKey(), acl);
			}
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}

	protected AccessControlList changeFileACL(AccessControlList acl, String grantee, Permission permission, StorageOwner owner){
		if(acl != null){
			if(grantee.contains("@") == true){
				acl.grantPermission(new EmailAddressGrantee(grantee), permission);
			}
			else{
				acl.grantPermission(new CanonicalGrantee(grantee), permission);
			}
		}
		else{
			acl = new AccessControlList();
			if(grantee.contains("@") == true){
				acl.grantPermission(new EmailAddressGrantee(grantee), permission);
				acl.grantPermission(new CanonicalGrantee(owner.getId()), Permission.PERMISSION_FULL_CONTROL);
			}
			else{
				acl.grantPermission(new CanonicalGrantee(grantee), permission);
				acl.grantPermission(new CanonicalGrantee(owner.getId()), Permission.PERMISSION_FULL_CONTROL);
			}
			
			acl.setOwner(owner);
		}
		
		return acl;
	}
	
	public void changeFilesACL(String bucketName, String grantee, Permission permission, StorageOwner owner) throws StorageException{
		try{
			List<String> files = listFiles(bucketName);
			
			for(String file : files){
				AccessControlList acl = getClient().getObjectAcl(bucketName, file);
				
				acl = changeFileACL(acl, grantee, permission, owner);
				
				getClient().putObjectAcl(bucketName, file, acl);
			}
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}
	
	public void putFiles(String sourceDir, String destinationBucketName, String prefix) throws StorageException{
		try {
			if (prefix == null){
				prefix = ""; // Empty String
			} else {
				if (!(prefix.endsWith("/"))) { 
					prefix = prefix + "/";
				}
			}
			
			File f = new File(sourceDir);
			for(File tmp : f.listFiles()){
				if (tmp.isDirectory()) {
					putFiles(tmp.getAbsolutePath(), destinationBucketName, prefix + tmp.getName() + "/");
				} else if (tmp.isFile()) {
					storeFile(destinationBucketName, String.format("%s%s", prefix, tmp.getName()), tmp);
				} else {
					log.info(String.format("%s is not a directory, not copying files to destination bucket %s", 
							sourceDir, destinationBucketName));
				}	
			}
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}
	
	public void getFiles(String bucketName, String destinationDir, String prefix) throws StorageException{
		try{
			if(prefix == null){
				List<String> files = listFiles(bucketName);
				for(String file : files){
					String url = getFileAccessURL(bucketName, file);
					getFile(url, new File(String.format("%s/%s", destinationDir, file)));
				}
			}
			else{
				StorageObjectsChunk objectChunks = getClient().listObjectsChunked(bucketName, prefix, null, 1000, null, true);
				for(StorageObject object : objectChunks.getObjects()){
					//String file = String.format("%s%s", prefix, object.getKey());
					String url = getFileAccessURL(bucketName, object.getKey());
					String [] path = object.getKey().split("/");
					
					//not the best solution, but gets everything working again
					//listobjectschunked can return a prefix that is a directory, the getFile will throw an exception,
					//the rest of the files are not downloaded, for now wrap try catch and log error.
					try{
						getFile(url, new File(String.format("%s/%s", destinationDir, path[path.length -  1])));
					}catch(Exception e){
						log.error(e.toString(), e);
					}
				}
			}
			
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}
	
	public void copyFiles(String sourceBucketName, String destinationBucketName, boolean replaceMetadata, String sourcePrefix, String destPrefix) throws StorageException{
		try{
			List<String> files = new ArrayList<String>();
			List<String> destFiles = null; 
			if(sourcePrefix == null){
				if(destPrefix == null){
					files = listFiles(sourceBucketName);
				}
				else{
					destFiles = new ArrayList<String>();
					for(String file : files){
						files.add(file);
						String [] path = file.split("/");
						destFiles.add(String.format("%s%s", destPrefix, path[path.length-1]));
					}
				}
			}
			else{
				if(destPrefix == null){
					StorageObjectsChunk objectChunks = getClient().listObjectsChunked(sourceBucketName, sourcePrefix, null, 1000, null, true);
					for(StorageObject object :objectChunks.getObjects()){
						files.add(object.getKey());
					}	
				}
				else{
					destFiles = new ArrayList<String>();
					StorageObjectsChunk objectChunks = getClient().listObjectsChunked(sourceBucketName, sourcePrefix, null, 1000, null, true);
					for(StorageObject object :objectChunks.getObjects()){
						files.add(object.getKey());
						String [] path = object.getKey().split("/");
						destFiles.add(String.format("%s%s", destPrefix, path[path.length-1]));
					}	
				}
			}
			if(destFiles == null){
				for(String file : files){
					getClient().copyObject(sourceBucketName, file, destinationBucketName, new StorageObject(file), replaceMetadata);
				}
			}
			else{
				Iterator<String> srcItr = files.iterator();
				Iterator<String> destItr = destFiles.iterator();
				while(srcItr.hasNext() == true && destItr.hasNext() == true){
					getClient().copyObject(sourceBucketName, srcItr.next(), destinationBucketName, new StorageObject(destItr.next()), replaceMetadata);
				}
			}
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}
	
	public void deleteFiles(String bucketName, String prefix) throws StorageException{
		try{
			if(prefix == null){
				List<String> fileKeys = listFiles(bucketName);  //get files in bucket
				//delete all files in bucket
				
				for(String file : fileKeys){
					getClient().deleteObject(bucketName, file);
				}
			}
			else{
				//list all files that contain the prefix
				S3Object [] objects = getClient().listObjects(bucketName, prefix, null);
				
				for(S3Object object : objects){
					getClient().deleteObject(bucketName, object.getKey());
				}
			}
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}
}
