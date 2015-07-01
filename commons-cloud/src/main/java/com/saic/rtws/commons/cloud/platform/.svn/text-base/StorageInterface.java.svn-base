package com.saic.rtws.commons.cloud.platform;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.jets3t.service.acl.Permission;
import org.jets3t.service.model.StorageOwner;

import com.saic.rtws.commons.cloud.exception.StorageException;

@XmlJavaTypeAdapter(StorageInterface.TypeAdapter.class)
public interface StorageInterface {

	/**
	 * Check on the existence of a bucket by name.
	 * 
	 * @param bucketName
	 *            name of the bucket to create
	 * @return true if the specified bucket exists, false if it does not exist
	 * @throws StorageException
	 *             the check has failed for some reason
	 */
	public boolean bucketExists(String bucketName)
			throws StorageException;

	/**
	 * Creates a new bucket with the specified bucketName. Will not fail if the
	 * bucket already exists and is owned by you. By default the ACL is
	 * read/write only with your credentials.
	 * 
	 * @param bucketName
	 *            name of the bucket to create
	 * @throws StorageException
	 *             creation has failed for some reason
	 */
	public void createBucket(String bucketName)
			throws StorageException;

	/**
	 * Deletes a bucket by name.
	 * 
	 * @param bucketName
	 *            name of the bucket to delete
	 * @return true if bucket was deleted, false if bucket was not empty
	 * @throws StorageException
	 *             the deletion of the bucket failed for some reason
	 */
	public boolean deleteBucket(String bucketName)
			throws StorageException;

	/**
	 * Check on the existence of a file in a bucket.
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param fileKey
	 *            the key, including "directory" structure, as known to
	 *            the Storage service
	 * @return true if the specified file exists inside the specified bucket,
	 *         false otherwise
	 * @throws StorageException
	 *             the check has failed for some reason
	 */
	public boolean fileExists(String bucketName, String fileKey)
			throws StorageException;

	/**
	 * Generates a unique file key based on a file key that you want to use and
	 * the bucket name where the file will be stored. The desired file key will
	 * be used if there are no existing files with the same key in the bucket.
	 * Otherwise a one-up number will be added near the end of the file key.
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param desiredFileKey
	 *            file key that you want to use
	 * @return a unique file key that does not currently exist in the bucket
	 * @throws StorageException
	 *             access to the Storage service has failed for some reason
	 */
	public String generateUniqueFileKey(String bucketName, String desiredFileKey)
			throws StorageException;

	/**
	 * Retrieves the contents of a file and places it onto the file system.
	 * 
	 * @param bucketName
	 *            name of the bucket that contains the file
	 * @param fileKey
	 *            the key, including "directory" structure, as known to
	 *            the Storage service
	 * @param destFile
	 *            the File on the local file system that will receive the
	 *            contents
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public void getFile(String bucketName, String fileKey, File destFile) 
			throws StorageException;
	
	/**
	 * Retrieves the contents of a file and return an input stream.
	 * 
	 * @param bucketName
	 *            name of the bucket that contains the file
	 * @param fileKey
	 *            the key, including "directory" structure, as known to
	 *            the Storage service
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public InputStream getFile(String bucketName, String fileKey)
			throws StorageException;

	/**
	 * Retrieves the contents of a file and places it onto the file system. This
	 * method will retry if the retrieval fails.
	 * 
	 * @param bucketName
	 *            name of the bucket that contains the file
	 * @param fileKey
	 *            the key, including "directory" structure, as known to
	 *            the Storage service
	 * @param destFile
	 *            the File on the local file system that will receive the
	 *            contents
	 * @param retryCount
	 *            number of times to retry getting the file if a failure happens
	 * @param retryDelay
	 *            number of milliseconds to wait between retry attempts
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public void getFileWithRetry(String bucketName, String fileKey,
			File destFile, int retryCount, int retryDelay)
			throws StorageException;

	/**
	 * Retrieves the contents of a file and places it onto the file system.
	 * 
	 * @param URL
	 *            URL describing where to get the file, in the form
	 *            http://<server>:<port>/<s3-virtual-path/<bucketName/<fileKey>
	 * @param destFile
	 *            the File on the local file system that will receive the
	 *            contents
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public void getFile(String URL, File destFile) 
			throws StorageException;

	/**
	 * Retrieves the contents of a file and places it onto the file system. This
	 * method will retry if the retrieval fails.
	 * 
	 * @param URL
	 *            URL describing where to get the file, in the form
	 *            http://<server>:<port>/<s3-virtual-path/<bucketName/<fileKey>
	 * @param destFile
	 *            the File on the local file system that will receive the
	 *            contents
	 * @param retryCount
	 *            number of times to retry getting the file if a failure happens
	 * @param retryDelay
	 *            number of milliseconds to wait between retry attempts
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public void getFileWithRetry(String URL, File destFile,
			int retryCount, int retryDelay)
			throws StorageException;

	/**
	 * Get a public URL that can retrieve the file.
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @param fileKey
	 *            the key, including "directory" structure, as known to
	 *            the Storage service
	 * @return a URL in the form <server base URL>/<bucket name>/<Object Key>
	 */
	public String getFileAccessURL(String bucketName, String fileKey)
			throws StorageException;

	/**
	 * Get a list of all buckets.
	 * 
	 * @return a list of bucket names
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public List<String> listBuckets()
			throws StorageException;

	/**
	 * Get a list of all files stored in a single bucket.
	 * 
	 * @param bucketName
	 *            name of the bucket
	 * @return a list of file names
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public List<String> listFiles(String bucketName)
			throws StorageException;

	/**
	 * Removes a file from the Storage service.
	 * 
	 * @param fileURL
	 *            URL used to access the file in the Storage service
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public void removeFile(String fileURL)
			throws StorageException;

	/**
	 * Removes a file from a bucket in the Storage service.
	 * 
	 * @param bucketName
	 *            name of the bucket that contains the file
	 * @param fileKey
	 *            the key, including "directory" structure, as known to
	 *            the Storage service
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public void removeFile(String bucketName, String fileKey)
			throws StorageException;

	/**
	 * Store a file in a bucket using the Storage Service. Content type is
	 * derived from the sourceFile and default encoding and serverEncryption
	 * settings are used.
	 * 
	 * @param bucketName
	 *            name of the bucket to place the file into
	 * @param fileKey
	 *            the key, including "directory" structure, as known to
	 *            the Storage service
	 * @param sourceFile
	 *            the File to store
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public void storeFile(String bucketName, String fileKey, File sourceFile)
			throws StorageException;

	/**
	 * Store a file in a bucket using the Storage Service. Content type is
	 * derived from the sourceFile and default encoding and serverEncryption
	 * settings are used.
	 * 
	 * @param bucketName
	 *            name of the bucket to place the file into
	 * @param fileKey
	 *            the key, including "directory" structure, as known to
	 *            the Storage service
	 * @param sourceFile
	 *            the File to store
	 * @param retryCount
	 *            number of times to retry storing the file if a failure happens
	 * @param retryDelay
	 *            number of milliseconds to wait between retry attempts
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public void storeFileWithRetry(String bucketName, String fileKey, File sourceFile,
			int retryCount, int retryDelay)
			throws StorageException;

	/**
	 * Store a file in a bucket using the Storage Service.
	 * 
	 * @param bucketName
	 *            name of the bucket to place the file into
	 * @param fileKey
	 *            the key, including "directory" structure, as known to
	 *            the Storage service
	 * @param sourceFile
	 *            the File to store
	 * @param contentType
	 *            MIME type of data in the file
	 * @param encoding
	 *            encoding of data in the file
	 * @param serverEncryption
	 *            sadly, this is ignored at this time
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public void storeFile(String bucketName, String fileKey, File sourceFile,
			String contentType, String encoding, boolean serverEncryption)
			throws StorageException;

	/**
	 * Store a file in a bucket using the Storage Service.
	 * 
	 * @param bucketName
	 *            name of the bucket to place the file into
	 * @param fileKey
	 *            the key, including "directory" structure, as known to
	 *            the Storage service
	 * @param sourceFile
	 *            the File to store
	 * @param contentType
	 *            MIME type of data in the file
	 * @param encoding
	 *            encoding of data in the file
	 * @param serverEncryption
	 *            sadly, this is ignored at this time
	 * @param retryCount
	 *            number of times to retry storing the file if a failure happens
	 * @param retryDelay
	 *            number of milliseconds to wait between retry attempts
	 * @throws StorageException
	 *             if a failure has occurred for some reason
	 */
	public void storeFileWithRetry(String bucketName, String fileKey, File sourceFile,
			String contentType, String encoding, boolean serverEncryption,
			int retryCount, int retryDelay)
			throws StorageException;

	static class TypeAdapter extends XmlAdapter<Object, StorageInterface> {
		public StorageInterface unmarshal(Object element) { return (StorageInterface)element; }
		public Object marshal(StorageInterface bean) { return bean; }
	}
	
	/**
	 * Recursively change the acl value on the bucket and files within it.
	 * 
	 * @param bucketName the bucket name
	 * @param grantee the grantee value
	 * @param permission {@link Permission} to set for grantee
	 * @throws StorageException {@link StorageException}
	 */
	public void changeACLOnBucket(String bucketName, String grantee, Permission permission) throws StorageException;
	
	/**
	 * Change the acl value on the bucket.
	 * 
	 * @param bucketName the bucket name
	 * @param grantee the grantee value
	 * @param permission {@link Permission} to set for grantee
	 * @param recursive Flag to specify whether or not files within should be modified
	 * @throws StorageException {@link StorageException}
	 */
	public void changeACLOnBucket(String bucketName, String grantee, Permission permission, boolean recursive) throws StorageException;
	
	/**
	 * Upload files from a local directory to a S3 bucket/prefix.
	 * 
	 * @param sourceDir the directory to upload the files from
	 * @param destinationBucketName the destination bucket name
	 * @param prefix the prefix value to append to the file name
	 * @throws StorageException {@link StorageException}
	 */
	public void putFiles(String sourceDir, String destinationBucketName, String prefix) throws StorageException;
	
	/**
	 * Download files from a S3 bucket/prefix to a directory on the local file system.
	 * 
	 * @param bucketName the bucket name
	 * @param destinationDir the name of the directory
	 * @param prefix the prefix value to append to the file name
	 * @throws StorageException {@link StorageException}
	 */
	public void getFiles(String bucketName, String destinationDir, String prefix) throws StorageException;
	
	/**
	 * Copy files between S3 buckets/prefixs.
	 * 
	 * @param sourceBucketName the source bucket name
	 * @param destinationBucketName the destination bucket name
	 * @param replaceMetadata boolean value to keep or replace the metadata from the source file
	 * @param sourcePrefix the prefix value to append to the source file name
	 * @param destPrefix the prefix value to append to the destination file name
	 * @throws StorageException
	 */
	public void copyFiles(String sourceBucketName, String destinationBucketName, boolean replaceMetadata, String sourcePrefix, String destPrefix) throws StorageException;
	
	/**
	 * Delete files from a S3 bucket/prefix.
	 * 
	 * @param bucketName the bucket name
	 * @param prefix the prefix value to append to the file name
	 * @throws StorageException {@link StorageException}
	 */
	public void deleteFiles(String bucketName, String prefix) throws StorageException;
	
	
	/**
	 * Recursively change the acl for all files within the bucket.
	 * 
	 * @param bucketName the bucket name
	 * @param grantee the grantee value
	 * @param permission {@link Permission} to set for grantee
	 * @param owner {@link StorageOwner} owner of the bucket
	 * @throws StorageException {@link StorageException}
	 */
	public void changeFilesACL(String bucketName, String grantee, Permission permission, StorageOwner owner) throws StorageException;
	
	/**
	 * Recursively change the acl for all files under a bucket prefix. Prefix example  s3://test.bucket/prefix/filename.
	 * 
	 * @param bucketName the bucket name
	 * @param prefix prefix of file name
	 * @param grantee the grantee value
	 * @param permission {@link Permission} to set for grantee
	 * @throws StorageException {@link StorageException}
	 */
	public void changeFilesACL(String bucketName, String prefix, String grantee, Permission permission) throws StorageException;
	
	/**
	 * Recursively change the acl for all files under a bucket prefix. Prefix example  s3://test.bucket/prefix/filename.
	 * 
	 * @param bucketName the bucket name
	 * @param prefix prefix of file name
	 * @param grantees a list of grantees, either email or canonical id for each
	 * @param permission {@link Permission} to set for grantee
	 * @throws StorageException {@link StorageException}
	 */
	public void changeFilesACL(String bucketName, String prefix, List<String> grantees, Permission permission) throws StorageException;

}
