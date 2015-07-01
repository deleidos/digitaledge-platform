package com.saic.rtws.UploadUtil.Decompressors;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.apache.commons.io.FilenameUtils;

import com.saic.rtws.UploadUtil.Decompressors.FileTypeDecompressor;

public class BZipTypeDecompressor extends FileTypeDecompressor {
	
	private BZip2CompressorInputStream bis;
	private boolean done=false;

	public BZipTypeDecompressor(InputStream inputFile, String fileName) throws IOException {
		super();
		bis = new BZip2CompressorInputStream(inputFile);
		this.currentEntryName=formatEntryName(fileName);
	}

	@Override
	public InputStream nextFile() throws IOException {
		if(!done){
			done=true;
			return bis;
		}else{
			return null;
		}
	}

	public void close() {
		if (bis != null) {
			try {
				bis.close();
			} catch (Exception drop) {}
		}
	}
	
	private String formatEntryName(String fileName) {
		// Since bzip only compresses a single file, the compressed file is named:
		//   fileName.ext.bz2
		// Therefore, the decompressed file should only be fileName.ext so we must
		// remove the extension added by bzip
		String formattedEntryName = FilenameUtils.removeExtension(fileName);
		
		return formattedEntryName;
	}
}
