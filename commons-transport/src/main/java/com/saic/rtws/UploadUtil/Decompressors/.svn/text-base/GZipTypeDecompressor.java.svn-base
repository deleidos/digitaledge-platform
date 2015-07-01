package com.saic.rtws.UploadUtil.Decompressors;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.io.FilenameUtils;

import com.saic.rtws.UploadUtil.Decompressors.FileTypeDecompressor;

public class GZipTypeDecompressor extends FileTypeDecompressor {
	
	private GzipCompressorInputStream gis;
	private boolean done=false;

	public GZipTypeDecompressor(InputStream inputFile, String fileName) throws IOException {
		super();
		gis = new GzipCompressorInputStream(inputFile);
		this.currentEntryName=formatEntryName(fileName);
		//gzip by itself only compresses a single file.
	}

	@Override
	public InputStream nextFile() throws IOException {
		if(!done){
			done=true;
			return gis;
		}else{
			return null;
		}
	}

	public void close() {
		if (gis != null) {
			try {
				gis.close();
			} catch (Exception drop) {}
		}
	}
	
	private String formatEntryName(String fileName) {
		// Since gzip only compresses a single file, the compressed file is named:
		//   fileName.ext.gz
		// Therefore, the decompressed file should only be fileName.ext so we must
		// remove the extension added by gzip
		String formattedEntryName = FilenameUtils.removeExtension(fileName);
		
		return formattedEntryName;
	}
}
