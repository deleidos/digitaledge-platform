package com.saic.rtws.commons.util.file;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

/**
 * File object that represents a file and a hidden copy of it, providing a transactional
 * way to edit the file without overwriting the original until a commit point it reached.
 * Also provides optional file locking to synchronize file access between JVMs.
 */
public class TransactedFile extends File {

	private static final long serialVersionUID = -5669573344116639489L;
	
	/** The original file. */
	private File realFile;
	
	/** The temporary file. */
	private File tempFile;
	
	/** A file used to managed lock. */
	private File lockFile;
	
	/** A file used as a rollback a failed commit. */
	private File saveFile;
	
	/** A channel object used to establish lock. */
	private transient FileChannel channel;
	
	/** The actual file lock. */
	private transient FileLock lock;
	
	/**
	 * Constructor.
	 */
	public TransactedFile(File file) {
		super(checkFile(file).getAbsolutePath());
		realFile = this;
		tempFile = tempFile(realFile);
		saveFile = saveFile(realFile);
		lockFile = lockFile(realFile);
	}
	
	/**
	 * Gets the temporary file that should be used to write changes.
	 */
	public File getTempFile() {
		return tempFile;
	}
	
	/**
	 * Initializes the contents of the temporary file; if copying is specified, the
	 * original file is copied, otherwise the temporary file will initially not exist.
	 */
	public void init(boolean copy) throws IOException {
		
		RandomAccessFile real = null;
		RandomAccessFile temp = null;
		
		// If the original file doesn't initially exist, or a copy is not needed,
		// the shadow file should also not initially exist; delete just in case. 
		if(!copy || !realFile.exists()) {
			tempFile.delete();
			
		// If the original file exists, copy it.
		} else try {
			
			// Open the original file.
			real = new RandomAccessFile(realFile, "r");
			FileChannel realChannel = real.getChannel();

			// Open shadow file.
			temp = new RandomAccessFile(tempFile, "rw");
			FileChannel tempChannel = temp.getChannel();
			
			// Copy the bytes from the original file to the shadow file. 
			ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 256);
			while(realChannel.read(buffer) != -1) {
				buffer.flip();
				tempChannel.write(buffer);
				buffer.compact();
			}
			
			// Just in case the buffer isn't empty yet, keep writing until it is.
			buffer.flip();
			while(buffer.hasRemaining()) {
				tempChannel.write(buffer);
			}
			
		// If the copy failed, revert back to the original.
		} catch(IOException e) {
			revert();
			throw e;
			
		// Clean up resource.
		} finally {
			try {real.close();} catch(Exception ignore) { }
			try {temp.close();} catch(Exception ignore) { }
			
		}
		
	}
	
	/**
	 * Replace the original file with the temporary file.
	 */
	public void commit() throws IOException {
		
		// Make sure there isn't an old save file around. 
		saveFile.delete();
		
		// Keep the original file in case the replace fails.
		if(realFile.exists() && !realFile.renameTo(saveFile)) {
			throw new IOException("Unable to overwrite file '" + realFile + "'.");
		}
		
		// If the replace succeeds, remove the save file.
		if(tempFile.renameTo(realFile)) {
			saveFile.delete();
			
		// If the replace files, put the original back.
		} else {
			saveFile.renameTo(realFile);
			throw new IOException("Unable to overwrite file '" + realFile + "'.");
		}
		
	}
	
	public boolean isLocked(){
		if(lock != null){
			return lock.isValid();
		}
		else{
			return false;
		}
	}
	
	/**
	 * Discard the temporary file, and leave the original file as is.
	 */
	public void revert() {
		tempFile.delete();
	}
	
	/**
	 * Establish a file lock for this file.
	 */
	public void lock() throws IOException {
		if(channel != null || lock != null) {
			throw new IllegalStateException("File is already locked '" + getName() + "'.");
		} else try {
			channel = new RandomAccessFile(lockFile, "rw").getChannel();
			lock = channel.lock();
		} catch(IOException e) {
			try {lock.release();} catch(Exception ignore) { } finally {lock = null;}
			try {channel.close();} catch(Exception ignore) { } finally {channel = null;}
			throw e;
		}
	}
	
	/**
	 * Release the file lock for this file.
	 */
	public void release() {
		if(channel == null || lock == null) {
			throw new IllegalStateException("File is not locked '" + getName() + "'.");
		} else try {
			lock.release();
		} catch(IOException e) {
			try {
				channel.close();
			} catch(Exception ignore) { 
				// Ignore.
			}
		} finally {
			lockFile.delete();
			channel = null;
			lock = null;
		}
	}
	
	/**
	 * Generate the name for the temporary copy of the given file.
	 */
	private static File tempFile(File file) {
		return new File(file.getParent(), "." + file.getName());
	}
	
	/**
	 * Generate the name for the backup copy of the given file.
	 */
	private static File saveFile(File file) {
		return new File(file.getParent(), "." + file.getName() + ".tmp");
	}
	
	/**
	 * Generate the name for the lock of the given file.
	 */
	private static File lockFile(File file) {
		return new File(file.getParent(), "." + file.getName() + ".lck");
	}
	
	/**
	 * Validate that transactional temporary files can be generated for the given file.
	 */
	private static File checkFile(File file) {
		if(file == null) {
			throw new NullPointerException("Cannot create a temp tile for a null object.");
		} else if(file.getName().startsWith(".")) {
			throw new IllegalArgumentException("Cannot handle transactions for a hidden file.");
		} else if(file.getName().endsWith(".lck")) {
			throw new IllegalArgumentException("Cannot handle transactions for a lock file.");
		} else if(file.isDirectory()) {
			throw new IllegalArgumentException("Cannot handle transactions for a directory.");
		} else if(!file.getParentFile().canWrite()) {
			throw new IllegalArgumentException("Cannot handle transactions for a directory without write permissions.");
		} else if(file.exists()) {
			if(!file.canRead()) {
				throw new IllegalArgumentException("Cannot handle transaction for a file without read permissions.");
			} else if(!file.canWrite()) {
				throw new IllegalArgumentException("Cannot handle transaction for a file without write permissions.");
			}
		}
		return file.getAbsoluteFile();
	}
	
}
