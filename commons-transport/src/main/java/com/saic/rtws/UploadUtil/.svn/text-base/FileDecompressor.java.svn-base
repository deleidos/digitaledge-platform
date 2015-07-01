package com.saic.rtws.UploadUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.saic.rtws.UploadUtil.Decompressors.BZipTypeDecompressor;
import com.saic.rtws.UploadUtil.Decompressors.FileTypeDecompressor;
import com.saic.rtws.UploadUtil.Decompressors.GZipTypeDecompressor;
import com.saic.rtws.UploadUtil.Decompressors.RarTypeDecompressor;
import com.saic.rtws.UploadUtil.Decompressors.SevenZipTypeDecompressor;
import com.saic.rtws.UploadUtil.Decompressors.TarBZipTypeDecompressor;
import com.saic.rtws.UploadUtil.Decompressors.TarGZipTypeDecompressor;
import com.saic.rtws.UploadUtil.Decompressors.TarTypeDecompressor;
import com.saic.rtws.UploadUtil.Decompressors.ZipTypeDecompressor;

public class FileDecompressor {
	
	private FileTypeDecompressor decompressor;

	public FileDecompressor(File inFile) throws IOException {
		String fileName = inFile.getAbsolutePath().toLowerCase();
		
		if(fileName.endsWith(".zip")){//tested
			InputStream fileStream = new FileInputStream(inFile);
			decompressor = new ZipTypeDecompressor(fileStream);
		}else if(fileName.endsWith(".tar")){//tested
			InputStream fileStream = new FileInputStream(inFile);
			decompressor = new TarTypeDecompressor(fileStream);
		}else if(fileName.endsWith(".tar.gz") || fileName.endsWith(".tgz")){//tested
			InputStream fileStream = new FileInputStream(inFile);
			decompressor = new TarGZipTypeDecompressor(fileStream);
		}else if(fileName.endsWith(".tbz2") || fileName.endsWith(".tar.bz2")){
			InputStream fileStream = new FileInputStream(inFile);
			decompressor = new TarBZipTypeDecompressor(fileStream);
		}else if(fileName.endsWith(".gz")){//tested
			InputStream fileStream = new FileInputStream(inFile);
			decompressor = new GZipTypeDecompressor(fileStream,inFile.getAbsolutePath());
		}else if(fileName.endsWith(".7z")){//tested
			decompressor = new SevenZipTypeDecompressor(inFile);
		}else if(fileName.endsWith(".rar")){//tested
			decompressor = new RarTypeDecompressor(inFile);
		}else if(fileName.endsWith(".bz2")){
			InputStream fileStream = new FileInputStream(inFile);
			decompressor = new BZipTypeDecompressor(fileStream,inFile.getAbsolutePath());
		}
	}
	
	public String entryName(){
		return decompressor.entryName();
	}
	
	public InputStream nextEntry() throws IOException{
		return decompressor.nextFile();
	}
	
	public void close() throws IOException {
		decompressor.close();
	}
	
	public static boolean isCompressedType(File file){
		return isCompressedType(file.getAbsolutePath());
	}

	public static boolean isCompressedType(String fileName) {
		return fileName.endsWith(".zip") 
				|| fileName.endsWith(".tar")
				|| fileName.endsWith(".tar.gz") 
				|| fileName.endsWith(".tgz")
				|| fileName.endsWith(".gz") 
				|| fileName.endsWith(".7z")
				|| fileName.endsWith(".rar") 
				|| fileName.endsWith(".tbz2")
				|| fileName.endsWith(".tar.bz2")
				|| fileName.endsWith(".bz2");
	}
}
