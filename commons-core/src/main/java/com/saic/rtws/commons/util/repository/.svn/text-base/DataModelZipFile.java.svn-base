package com.saic.rtws.commons.util.repository;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DataModelZipFile {
	
	public static final String CANONICAL_PREFIX  = "canonical";
	public static final String XLATE_PREFIX      = "xlate";
	public static final String DATASRC_PREFIX    = "datasources";
	public static final String ENRICHMENT_PREFIX = "enriched";
	public static final String ENRICHCFG_PREFIX  = "enrichcfg";
	
	public static final String JSON_TYPE = ".json";
	public static final String TEXT_TYPE = ".txt";
	
	protected class FullZipEntry {
		ZipEntry zentry;
		byte[]   contents;
	}
	
	protected HashMap<String,FullZipEntry> entries;
	
	public DataModelZipFile(InputStream in) throws IOException {
		// Read all the zip file entries into memory
		entries = new HashMap<String,FullZipEntry>();
		ZipInputStream zipStream = new ZipInputStream(in);
		ZipEntry ze = null;
		while ((ze = zipStream.getNextEntry()) != null) {
			FullZipEntry fullEntry = new FullZipEntry();
			fullEntry.zentry = ze;
			int bytesLeft = (int)ze.getSize();
			if(bytesLeft != -1){
				fullEntry.contents = new byte[bytesLeft];
				int offset = 0;
				while (bytesLeft > 0) {
					int actual = zipStream.read(fullEntry.contents, offset, bytesLeft);
					bytesLeft -= actual;
					offset += actual;
				}
			}
			else{
				List<Byte> byteList = new ArrayList<Byte>();
				//while bytes available to read in current entry
				while(zipStream.available() == 1){
					Integer tmp = zipStream.read();
					//while not end of file
					if(tmp != -1){
						byteList.add(tmp.byteValue());
					}
				}
				fullEntry.contents = new byte[byteList.size()];
				for(int i = 0; i < byteList.size(); i++){
					fullEntry.contents[i] = byteList.get(i);
				}
			}
			String name = ze.getName();
			entries.put(name, fullEntry);
		}
		in.close();
	}
	
	private InputStream getInputStream(String filename) {
		FullZipEntry fullEntry = entries.get(filename);
		if (fullEntry == null) {
			return null;
		}
		return new ByteArrayInputStream(fullEntry.contents);
	}
	
	public InputStream getCanonicalModel() throws IOException {
		String filename = CANONICAL_PREFIX + JSON_TYPE;
		return getInputStream(filename);
	}
	
	public InputStream getTranslationMapping(String inputFormatName) throws IOException {
		String filename = XLATE_PREFIX + "_" + inputFormatName + JSON_TYPE;
		return getInputStream(filename);
	}
	
	public InputStream getDataSourceParams() throws IOException {
		String filename = DATASRC_PREFIX + JSON_TYPE;
		return getInputStream(filename);
	}
	
	@Deprecated
	public InputStream getEnrichmentModel() throws IOException {
		String filename = ENRICHMENT_PREFIX + JSON_TYPE;
		return getInputStream(filename);
	}
	
	public InputStream getEnrichmentConfig() throws IOException {
		String filename = ENRICHCFG_PREFIX + JSON_TYPE;
		return getInputStream(filename);
	}
	
	public InputStream getParserConfig(String parser, String dataSource) throws IOException {
		String filename = parser+"_"+dataSource + TEXT_TYPE;
		return getInputStream(filename);
	}
	
	
	
	
}
