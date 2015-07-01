package com.saic.rtws.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

public class FileUtil {

	private static Logger logger = Logger.getLogger(FileUtil.class);
	
	public static void replaceFileContent(File f, String regex, String replacement) throws IOException {
		String content = fileToString(f);
		content = content.replaceAll(regex, replacement);
		stringToFile(content, f);
	}
	
	public static void replaceFileContent(File f, Map<String, String> regexReplaceMap) throws IOException {
		String content = fileToString(f);
		for (Map.Entry<String, String> entry : regexReplaceMap.entrySet()) {
		    String regex = entry.getKey();
		    String replacement = entry.getValue();
		    content = content.replaceAll(regex, replacement);
		}
		stringToFile(content, f);
	}

	public static String fileToString(File f) throws IOException {
		FileInputStream fis = null;
		String content = "";
		try {
			fis = new FileInputStream(f);
			content = org.apache.commons.io.IOUtils.toString(fis);
			return content;
		} finally {
			org.apache.commons.io.IOUtils.closeQuietly(fis);
		}
	}
	
	// Erases existing file contents!
	public static void stringToFile(String content, File f) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(f, false);
			org.apache.commons.io.IOUtils.write(content, fos);
		} finally {
			org.apache.commons.io.IOUtils.closeQuietly(fos);
		}
	}
	
	// @TODO handle paths in the tar.gz and multiple files
	public static File extractFileFromTarGzip(File tarGzip, String fileNameToExtract, File outputDirectory)
		throws IOException {
		
		// Make sure the outputDirectory is actually a directory
		if(outputDirectory.isFile()) {
			outputDirectory = outputDirectory.getParentFile();
		}
		
		TarArchiveInputStream ais = null;
		BufferedOutputStream bos = null;
		try {
			// create the input stream to the original tar.gz file
			ais = new TarArchiveInputStream(
					new GZIPInputStream(new BufferedInputStream(
							new FileInputStream(tarGzip))));
	
			// create the output stream for the extracted file
			File extractedFile = new File(outputDirectory, fileNameToExtract);
			bos = new BufferedOutputStream(
					new FileOutputStream(extractedFile));
			
			// find the file by name
			ArchiveEntry nextEntry;
			while ((nextEntry = ais.getNextEntry()) != null) {
				if (nextEntry.getName().equals(fileNameToExtract)) {
					IOUtils.copy(ais, bos);
				}
			}
			
			// need to close the streams before potentially deleting the file below
			org.apache.commons.io.IOUtils.closeQuietly(ais);
			ais = null;
			org.apache.commons.io.IOUtils.closeQuietly(bos);
			bos = null;
			
			// check that we actually got something 
			if (extractedFile.length() == 0) {
				extractedFile.delete();
				return null;
			} else {
				return extractedFile;
			}
		} finally {
			// if an exception happened and the streams were not closed, close them here
			if (ais != null) {
				org.apache.commons.io.IOUtils.closeQuietly(ais);
			}
			if (bos != null) {
				org.apache.commons.io.IOUtils.closeQuietly(bos);
			}
		}
	}
	
	// @TODO handle paths in the tar.gz and multiple files
	public static File extractFileFromTarGzip(File tarGzip, String fileNameToExtract)
			throws IOException {
		return extractFileFromTarGzip(tarGzip, fileNameToExtract, tarGzip.getParentFile());
	}

	// @TODO handle paths in the tar.gz and multiple files
	public static void replaceFileInTarGZip(File tarGzip, File fileToReplace)
			throws IOException {
		
		TarArchiveInputStream ais = null;
		TarArchiveOutputStream aos = null;
		FileInputStream fis = null;
		try {
			// create the input stream to the original tar.gz file
			ais = new TarArchiveInputStream(
					new GZIPInputStream(new BufferedInputStream(
							new FileInputStream(tarGzip))));
	
			// create the output stream to the temp.tar.gz file
			File tempFile = new File(tarGzip.getParent(), "tmp"
					+ tarGzip.getName());
			tempFile.createNewFile();
			aos = new TarArchiveOutputStream(
					new GZIPOutputStream(new BufferedOutputStream(
							new FileOutputStream(tempFile))));
	
			// copy the files from the original tar.gz into the tmp.tar.gz
			// excluding the file to be replaced
			ArchiveEntry nextEntry;
			while ((nextEntry = ais.getNextEntry()) != null) {
				if (!nextEntry.getName().equals(fileToReplace.getName())) {
					aos.putArchiveEntry(nextEntry);
					IOUtils.copy(ais, aos);
					aos.closeArchiveEntry();
				}
			}
	
			// create the new entry for the file we are replacing and add it to the tmp.tar.gz
			TarArchiveEntry entry = new TarArchiveEntry(fileToReplace.getName());
			entry.setSize(fileToReplace.length());
			aos.putArchiveEntry(entry);
			fis = new FileInputStream(fileToReplace);
			IOUtils.copy(fis, aos);
			org.apache.commons.io.IOUtils.closeQuietly(fis);
			fis = null;
			
			// close the tmp.tar.gz archive, these need to close before renaming the file below
			aos.closeArchiveEntry();
			aos.finish();
			org.apache.commons.io.IOUtils.closeQuietly(ais);
			ais = null;
			org.apache.commons.io.IOUtils.closeQuietly(aos);
			aos = null;
			
			// replace the original tar.gz with the tmp.tar.gz
			boolean success = tarGzip.delete();
			if (!success) {
				throw new IOException("Failed to delete original tar.gz file:" + tarGzip.getName());
			}
			success = tempFile.renameTo(tarGzip);
			if (!success) {
				throw new IOException("Failed to rename the new tar.gz file:" + tempFile.getName());
			} 
		} finally {
			// if an exception happened and the streams were not closed, close them here
			if (fis != null) {
				org.apache.commons.io.IOUtils.closeQuietly(fis);
			}
			if (ais != null) {
				org.apache.commons.io.IOUtils.closeQuietly(ais);
			}
			if (aos != null) {
				org.apache.commons.io.IOUtils.closeQuietly(aos);
			}
		}
	}
	
	// creates a new empty file if it doesn't exist (including non-existent parent dirs)
	// if the file does exist, it blows it away and creates a new empty one
	public static File initializeFile(String parent, String child) throws IOException {
		File f = new File(parent, child);
		if (f.exists()) {
			f.delete();
		}
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}
		f.createNewFile();
		return f;
	}

	public static boolean isGZipped(File f) {
		int magic = 0;
		try {
			RandomAccessFile raf = new RandomAccessFile(f, "r");
			magic = raf.read() & 0xff | ((raf.read() << 8) & 0xff00);
			raf.close();
		} catch (Throwable e) {
			logger.error("Error checking gzip status of file", e);
		}
		return magic == GZIPInputStream.GZIP_MAGIC;
	}
	
	// only supports checking for gzip files for now
	public static InputStream getInputStream(File file) throws IOException {
		if (isGZipped(file)) {
			FileInputStream fStream = new FileInputStream(file);
			return new GZIPInputStream(fStream);
		} else {
			return new FileInputStream(file);
		}
	}

	/**
	 * Extracts the contents of the Tar GZip file to the directory in which it is located
	 */
	public static boolean extractTarGZip(File tarGZip) {
		return extractTarGZip(tarGZip, new File(tarGZip.getParent()));
	}
	
	/**
	 * Extracts the contents of the Tar GZip file to a specified directory
	 */	
	public static boolean extractTarGZip(File tarGZip, File outputDirectory) {
		if(isGZipped(tarGZip)) {
			TarArchiveInputStream ais = null;
			
			try {
			
				// Open the Tar GZip file
				ais = new TarArchiveInputStream(
						new GZIPInputStream(new BufferedInputStream(
								new FileInputStream(tarGZip))));
				
				// Ensure the directory to extract to exists
				if(!outputDirectory.exists()) {
					outputDirectory.mkdirs();
				}
				
				// Extract all the contents of the Tar GZip file
				TarArchiveEntry nextEntry;
				while ((nextEntry = ais.getNextTarEntry()) != null) {
					// Create a file for the extracted file
					File extractedFile = new File(outputDirectory, nextEntry.getName());
					
					if(nextEntry.isDirectory()) {
						if(!extractedFile.exists()) {
							extractedFile.mkdirs();
						}
					}
					else {
						// Copy the contents of the Tar GZip entry
						BufferedOutputStream bos = null;
						try {
							bos = new BufferedOutputStream(
								new FileOutputStream(extractedFile));
						
							IOUtils.copy(ais, bos);
						} catch (Exception ex) {
							throw ex;
						} finally {
							if(bos != null) try { bos.close(); } catch(Exception ignore) {} 
						}
					}
				}

				return true;
			} catch(Exception ex) {
				logger.error("Unable to extract Tar GZip file [" + tarGZip.getAbsolutePath() + 
									"] to directory [" + outputDirectory.getAbsolutePath() + "]", ex);
			} finally {
				if(ais != null) try { ais.close(); } catch(Exception ignore) {}
			}
		}
		
		return false;
	}
	
	/**
	 * Compresses the contents of a directory to a Tar GZip file. If the source is not a
	 * directory, then only the specified file will be compressed to the output file.
	 */
	public static boolean compressDirToTarGZip(File sourceDirectory, File outputFile) {
		
		TarArchiveOutputStream aos = null;
		try {
			// Ensure the outputFile exists
			outputFile.createNewFile();
			
			aos = new TarArchiveOutputStream(
					new GZIPOutputStream(new BufferedOutputStream(
							new FileOutputStream(outputFile))));
			
			// Make sure to record the path of the sourceDirectory to properly form the sub directories
			// in the Tar GZip file
			String sourceDirectoryPath;
			if(sourceDirectory.isDirectory()) {
				// The source directory is the directory specified
				sourceDirectoryPath = sourceDirectory.getAbsolutePath();
			}
			else {
				// A single file is all we are compressing, the source directory of the
				// file is the directory in which it is located
				sourceDirectoryPath = sourceDirectory.getParent();
			}
			
			compressDirToTarGZip(sourceDirectoryPath, sourceDirectory, aos, outputFile.getAbsolutePath());
			
			return true;
		} catch (Exception ex) {
			logger.error("Unable to compress directory [" + sourceDirectory.getAbsolutePath() +
									"] to Tar GZip file [" + outputFile.getAbsolutePath() + "]", ex);
		} finally {
			if(aos != null) try { aos.close(); } catch(Exception ignore) {}
		}
		
		return false;
	}
	
	private static void compressDirToTarGZip(String sourceDirectoryPath, File file, TarArchiveOutputStream aos, String outputFilePath) throws IOException {
		if(file.isDirectory()) {
			// Work through all the files in the directory
			String[] fileNames = file.list();
			for(String fileName : fileNames) {
				compressDirToTarGZip(sourceDirectoryPath, new File(file, fileName), aos, outputFilePath);
			}
		}
		else {
			// Make sure not to compress the output file
			if(file.getAbsolutePath().equals(outputFilePath)) {
				return;
			}
			
			// The name of the archive entry must be the name of the current file excluding the source directory
			// which is being compressed.  For example, if we are compressing /tmp/lib/commons-core.jar and the 
			// directory being compressed is the tmp directory, then the entry name in the compressed file must
			// be lib/commons-core.jar.
			String archiveEntryName = file.getAbsolutePath().substring(
											file.getAbsolutePath().lastIndexOf(sourceDirectoryPath) + sourceDirectoryPath.length() + 1,
											file.getAbsolutePath().length());
			
			// Create an archive entry to write to
			TarArchiveEntry entry = new TarArchiveEntry(archiveEntryName);
			entry.setSize(file.length());
			aos.putArchiveEntry(entry);
			
			// Copy the contents of the file to the entry
			FileInputStream fis = new FileInputStream(file);
			IOUtils.copy(fis, aos);
			org.apache.commons.io.IOUtils.closeQuietly(fis);
			fis = null;
			
			aos.closeArchiveEntry();
		}
	}
	
	public static void copyDirectory(File sourceDirectory, File destDirectory) {
	
		try {
			FileUtils.copyDirectory(sourceDirectory, destDirectory);
		} catch (IOException e) {
			logger.error("Failed to copy directory [" + sourceDirectory.getAbsolutePath() +
							"] to [" + destDirectory.getAbsolutePath() + "]", e);
		}
		
	}
	
	public static void copyFile(File sourceFile, File destFile) {
	
		try {
			FileUtils.copyFile(sourceFile, destFile);
		} catch (IOException e) {
			logger.error("Failed to copy file [" + sourceFile.getAbsolutePath() +
							"] to [" + destFile.getAbsolutePath() + "]", e);
		}
		
	}
	
	public static void moveFile(File sourceFile, File destFile) {
	
		try {
			FileUtils.moveFile(sourceFile, destFile);
		} catch (IOException e) {
			logger.error("Failed to move file [" + sourceFile.getAbsolutePath() +
							"] to [" + destFile.getAbsolutePath() + "]", e);
		}
		
	}
	
	// Convenience Wrapper class
	public static boolean deleteQuietly(File file) {
		return FileUtils.deleteQuietly(file);
	}
	
	public static String buildPath(String basePath, String... paths) {
		// If the basePath ends with a path separator, remove it since it will be added below
		if(basePath.endsWith(File.separator)) {
			basePath = basePath.substring(0, basePath.length()-1);
		}
		StringBuilder sb = new StringBuilder(basePath);
		
		for(String path : paths) {
			sb.append(File.separator).append(path);
		}
		
		return sb.toString();
	}
}
