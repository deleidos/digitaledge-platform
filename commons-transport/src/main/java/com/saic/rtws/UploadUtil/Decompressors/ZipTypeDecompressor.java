package com.saic.rtws.UploadUtil.Decompressors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.saic.rtws.UploadUtil.FileDecompressor;

public class ZipTypeDecompressor extends FileTypeDecompressor {

	private ZipEntry entry;
	private ZipInputStream zis;

	public ZipTypeDecompressor(InputStream inputFile)
			throws FileNotFoundException {
		super();
		zis = new ZipInputStream(inputFile);
	}

	@Override
	public InputStream nextFile() throws IOException {
		entry = zis.getNextEntry();
		while (entry != null && entry.isDirectory()) {
			entry = zis.getNextEntry();
			// ignore directory entries, we want only the files
		}
		if (entry == null)
			return null;
		this.currentEntryName = entry.getName();
		return zis;
	}

	public void close() {
		if (zis != null) {
			try {
				zis.close();
			} catch (Exception drop) {}
		}
	}

}
