package com.saic.rtws.UploadUtil.Decompressors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import com.saic.rtws.UploadUtil.FileDecompressor;

public abstract class FileTypeDecompressor {
	
	protected String currentEntryName;
	
	public abstract InputStream nextFile() throws IOException;
	
	public String entryName(){
			return currentEntryName;
	}
	
	public abstract void close();
}
