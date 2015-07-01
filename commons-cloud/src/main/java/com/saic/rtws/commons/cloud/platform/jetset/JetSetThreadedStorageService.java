package com.saic.rtws.commons.cloud.platform.jetset;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.model.StorageObject;
import org.jets3t.service.multi.DownloadPackage;
import org.jets3t.service.multi.SimpleThreadedStorageService;

import com.saic.rtws.commons.cloud.exception.StorageException;

/**
 * Extends the JetSetStorageService and wraps a SimpleThreadedStorageService to 
 * provide multi-threaded processing to methods that require multiple storage objects.
 * 
 */
public class JetSetThreadedStorageService extends JetSetStorageService {

	private Logger log = Logger.getLogger(JetSetThreadedStorageService.class);
	
	protected SimpleThreadedStorageService threadedService;
//	private int maxThreads;
	
	public JetSetThreadedStorageService() {
		super();
	}
	
	private SimpleThreadedStorageService getThreadedService()
			throws StorageException {
		if (threadedService == null) {
			threadedService = new SimpleThreadedStorageService(getClient());
//			maxThreads = getClient().getJetS3tProperties().getIntProperty("threaded-service.max-thread-count", 
//					ThreadedMultiFileWriter.DEFAULT_NUM_WRITE_THREADS);
		}
		return threadedService;
	}
	
	@Override
	public void getFiles(String bucketName, String destinationDir, String prefix) throws StorageException {
		try{
			S3Object[] s3Objects = listS3Objects(bucketName, prefix, true);
			if (s3Objects != null) {
				DownloadPackage[] downloadPackages = new DownloadPackage[s3Objects.length];
				for (int i = 0; i < s3Objects.length; i++) {
					downloadPackages[i] = new DownloadPackage(s3Objects[i], getDestinationFile(s3Objects[i], prefix, destinationDir));
				}
				getThreadedService().downloadObjects(bucketName, downloadPackages);
			}
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}
	
	@Override
	public void putFiles(String sourceDir, String destinationBucketName, String prefix) throws StorageException {
		try {
			prefix = normalizePrefix(prefix);
			Collection<File> files = FileUtils.listFiles(new File(sourceDir), null, true);
			if (files.size() > 0) {
				StorageObject[] objects = buildStorageObjectsFromFiles(files, sourceDir, prefix, null, null);
				getThreadedService().putObjects(destinationBucketName, objects);
			}
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}
	
	@Override
	public void deleteFiles(String bucketName, String prefix) throws StorageException{
		try{
			S3Object[] s3Objects = listS3Objects(bucketName, prefix, false);
			if (s3Objects != null) {
				getThreadedService().deleteObjects(bucketName, s3Objects);
			}
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}
	
	@Override
	public void copyFiles(String sourceBucketName, String destinationBucketName, boolean replaceMetadata, 
			String sourcePrefix, String destPrefix) throws StorageException{
		try{
			destPrefix = normalizePrefix(destPrefix);
			S3Object[] s3Objects = listS3Objects(sourceBucketName, sourcePrefix, true);
			if (s3Objects != null) {
				String[] sourceObjectKeys = new String[s3Objects.length];
				StorageObject[] destinationObjects = new StorageObject[s3Objects.length];
				int i = 0;
				for (StorageObject object: s3Objects) {
					sourceObjectKeys[i] = object.getKey();
					destinationObjects[i] = new StorageObject(getDestinationKey(object, destPrefix));
					i++;
				}
				getThreadedService().copyObjects(sourceBucketName, destinationBucketName, sourceObjectKeys, destinationObjects, false);
			}
			
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}
	
	@Override
	public void changeFilesACL(String bucketName, String prefix, String grantee, Permission permission) throws StorageException{
		List<String> grantees = new ArrayList<String>(1);
		grantees.add(grantee);
		changeFilesACL(bucketName, prefix, grantees, permission);
	}
	
	@Override
	public void changeFilesACL(String bucketName, String prefix, List<String> grantees, Permission permission)
			throws StorageException {
		try{
			S3Object[] s3Objects = listS3Objects(bucketName, prefix, true);
			StorageObject[] objects = getThreadedService().getObjectACLs(bucketName, s3Objects);
			for (StorageObject object: objects) {
				AccessControlList acl = object.getAcl();
				for (String grantee : grantees) {
					changeFileACL(acl, grantee, permission, null);
				}
			}
			getThreadedService().putACLs(bucketName, objects);
		}
		catch(Exception e){
			log.error(e.toString(), e);
			throw new StorageException(e.toString(), e);
		}
	}

	private S3Object[] listS3Objects(String bucketName, String prefix, boolean excludeDirs) throws StorageException, S3ServiceException {
		List<S3Object> objectList = new ArrayList<S3Object>();
		S3Object[] objectArray = null;
		
		if(prefix == null){
			objectArray = getClient().listObjects(bucketName);
		} else {
			objectArray = getClient().listObjects(bucketName, prefix, null);
		}
		
		if (!excludeDirs) {
			return objectArray;
		} else {
			if (objectArray != null) {
				for (S3Object object: objectArray) {
					if (!object.isDirectoryPlaceholder()) {
						objectList.add(object);
					}
				}
			}
			if (objectList.size() > 0) {
				return objectList.toArray(new S3Object[objectList.size()]);
			} else {
				return null;
			}
		}
	}
	
	private String normalizePrefix(String prefix) {
		if (prefix == null){
			prefix = ""; // Empty String
		} else {
			if (!(prefix.endsWith("/"))) { 
				prefix = prefix + "/";
			}
		}
		return prefix;
	}
	
	private String getKey(File f, String sourceDir, String prefix) {
		
		String parentPath = f.getParentFile().getAbsolutePath();
		if (parentPath.length() > sourceDir.length()) {
			String addPath = parentPath.substring(sourceDir.length());
			addPath = addPath.replace("\\", "/") + "/";
			if (addPath.startsWith("/")) {
				addPath = addPath.substring(1);
			}
			prefix = prefix + addPath;
		}
		return String.format("%s%s", prefix, f.getName());
	}
	
	private StorageObject[] buildStorageObjectsFromFiles(Collection<File> files, String sourceDir, String prefix, 
			String contentType, String encoding) throws IOException, NoSuchAlgorithmException {
		StorageObject[] objects = new StorageObject[files.size()];
		int i = 0;
		String sourceDirPath = (new File(sourceDir)).getAbsolutePath();
		for (File f: files) {
			S3Object s3Object = new S3Object(f);
			s3Object.setKey(getKey(f, sourceDirPath, prefix));
			s3Object.setAcl(AccessControlList.REST_CANNED_PRIVATE);
			if (StringUtils.isNotBlank(contentType)) {
				s3Object.setContentType(contentType);
			}
			if (StringUtils.isNotBlank(encoding)) {
				s3Object.setContentEncoding(encoding);
			}
			objects[i++] = s3Object;
		}
		return objects;
	}
	
//	private void writeFiles(StorageObject[] objects, String destinationDir) throws ServiceException {
//		if (objects != null) {
//			ThreadedMultiFileWriter fileWriter = new ThreadedMultiFileWriter(maxThreads);
//			for (StorageObject object: objects) {
//				fileWriter.writeFile(object.getDataInputStream(), getDestinationFile(object, destinationDir));
//			}
//			fileWriter.waitUntilCompletion();
//		}
//	}
	
	private File getDestinationFile(StorageObject object, String prefix, String destinationDir) {
		String [] path = object.getKey().split("/");
		int prefixLength = prefix.split("/").length;
		for (int i = prefixLength; i < path.length - 1; i++) {
			destinationDir+= "/" + path[i];
		}
		return new File(String.format("%s/%s", destinationDir, path[path.length-1]));
	}
	
	private String getDestinationKey(StorageObject object, String destPrefix) {
		String [] path = object.getKey().split("/");
		int prefixLength = destPrefix.split("/").length;
		for (int i = prefixLength; i < path.length - 1; i++) {
			destPrefix+= path[i] + "/";
		}
		String test = String.format("%s%s", destPrefix, path[path.length-1]);
		return test;
	}

}
