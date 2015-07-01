package com.saic.rtws.UploadUtil.Decompressors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;

import com.saic.rtws.UploadUtil.FileDecompressor;

public class TarTypeDecompressor extends FileTypeDecompressor {

	private TarArchiveEntry entry;
	private TarArchiveInputStream tis;
	
	public TarTypeDecompressor(InputStream inputFile) throws FileNotFoundException {
		super();
		tis = new TarArchiveInputStream(inputFile);
	}

	@Override
	public InputStream nextFile() throws IOException {
		TarArchiveEntry entry = tis.getNextTarEntry();
			while(entry!=null && entry.isDirectory()){
				entry = tis.getNextTarEntry();
			}
			if (entry == null)
				return null;
			this.currentEntryName = entry.getName();
			return tis;
	}

	@Override
	public void close() {
		if(tis!=null){
			try{
				tis.close();
			}catch(Exception drop){}
		}
	}

}
