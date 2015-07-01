package com.saic.rtws.UploadUtil.Decompressors;

import java.io.IOException;
import java.io.InputStream;

/**
 * SafetyWrappedInputStream is a fix for a library bug in the 7z library used by the decompressor.
 * Instead of correctly returning -1 on an end-of-stream, the library seems to throw a "Write end dead"
 * IOException.  By catching and handling the error, the library deficiency is invisible to the end user. 
 *
 */
public class SafetyWrappedInputStream extends InputStream{
	
	private InputStream is;
	
	public SafetyWrappedInputStream(InputStream is){
		this.is = is;
	}

	@Override
	public int read() throws IOException {
		try{
			return is.read();
		}catch(IOException e){
			if(e.getMessage().equalsIgnoreCase("Write end dead")){
				return -1;
			}
			throw e;
		}
	}
	
	@Override
	public int read(byte[] b) throws IOException{
		try{
			return is.read(b);
		}catch(IOException e){
			if(e.getMessage().equalsIgnoreCase("Write end dead")){
				return -1;
			}
			throw e;
		}
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		try{
			return is.read(b,off,len);
		}catch(IOException e){
			if(e.getMessage().equalsIgnoreCase("Write end dead")){
				return -1;
			}
			throw e;
		}
	}

	@Override
	public long skip(long n) throws IOException {
		return is.skip(n);
	}

	@Override
	public int available() throws IOException {
		return is.available();
	}

	@Override
	public void close() throws IOException {
		is.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		is.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		is.reset();
	}

	@Override
	public boolean markSupported() {
		return is.markSupported();
	}
	

}
