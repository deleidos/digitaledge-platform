package com.saic.rtws.UploadUtil.Decompressors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.github.junrar.Archive;
import com.github.junrar.exception.RarException;
import com.github.junrar.rarfile.FileHeader;

public class RarTypeDecompressor extends FileTypeDecompressor{
	
	Archive archive;
	List<FileHeader> fileHeaders;
	int currentIndex=-1;
	InputStream lastStream;
	
	public RarTypeDecompressor(File inFile) throws IOException{
		super();
		try {
			archive = new Archive(inFile);
			fileHeaders = archive.getFileHeaders();
		} catch (RarException e) {
			throw new IOException(e);
		}
	}

	@Override
	public InputStream nextFile() throws IOException {
		if(lastStream!=null){
			try{
				lastStream.close();
			}catch(Exception drop){}
		}
		currentIndex++;
		if(currentIndex>=fileHeaders.size())
			return null;
		FileHeader fh = fileHeaders.get(currentIndex);
		while(fh.isDirectory()){
			currentIndex++;
			if(currentIndex>=fileHeaders.size())
				return null;
			fh = fileHeaders.get(currentIndex);
		}
		this.currentEntryName = fh.getFileNameString();
		try {
			return archive.getInputStream(fh);
		} catch (RarException e) {
			throw new IOException(e);
		}
	}

	@Override
	public void close() {
		try{
		archive.close();
		}catch(Exception drop){}
	}
}
