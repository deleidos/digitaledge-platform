package com.saic.rtws.UploadUtil.Decompressors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.RandomAccessFile;
import java.util.concurrent.CountDownLatch;

import net.sf.sevenzipjbinding.ExtractAskMode;
import net.sf.sevenzipjbinding.ExtractOperationResult;
import net.sf.sevenzipjbinding.IArchiveExtractCallback;
import net.sf.sevenzipjbinding.ISequentialOutStream;
import net.sf.sevenzipjbinding.ISevenZipInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;

public class SevenZipTypeDecompressor extends FileTypeDecompressor{
	
	ISevenZipInArchive inArchive;
	
	int numEntries;
	
	int currentEntry=-1;
	
	public SevenZipTypeDecompressor(File inFile) throws IOException {
		super();
		
		try {
            SevenZip.initSevenZipFromPlatformJAR();
            System.out.println("7-Zip-JBinding library was initialized");
            

            RandomAccessFile randomAccessFile = new RandomAccessFile(inFile, "r");

            inArchive = SevenZip.openInArchive(null, // Choose format automatically
                    new RandomAccessFileInStream(randomAccessFile));
            numEntries = inArchive.getNumberOfItems();
            
        } catch (SevenZipNativeInitializationException e) {
            e.printStackTrace();
        } catch (SevenZipException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public InputStream nextFile() throws IOException {
		currentEntry++;
		if(currentEntry<numEntries){
			try {
				
				MyExtractCallback mec = new MyExtractCallback(inArchive);
				while(inArchive.getSimpleInterface().getArchiveItem(currentEntry).isFolder()){
					currentEntry++;
					if(currentEntry>=numEntries)
						return null;
				}
				this.currentEntryName = inArchive.getSimpleInterface().getArchiveItem(currentEntry).getPath();
				
				new FileExtractor(mec,currentEntry).start();
				//see comments on SafetyWrappedInputStream
			//	return mec.in;
				return new SafetyWrappedInputStream(mec.in);
			} catch (SevenZipException e) {
				throw new IOException(e);
			}
		}else{
			return null;
		}
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}
	
	public class FileExtractor extends Thread{
		
		private MyExtractCallback mec;
		private int currentEntry;
		
		public FileExtractor(MyExtractCallback mec, int currentEntry){
			this.mec = mec;
			this.currentEntry = currentEntry;
		}

		@Override
		public void run() {
			try {
				inArchive.extract(new int[]{currentEntry}, false, mec);
			} catch (SevenZipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static class MyExtractCallback implements IArchiveExtractCallback {
        private int size = 0;
        private int index;
        
        private PipedInputStream pin;
        private PipedOutputStream pout;
        
        private InputStream in;
        private OutputStream out;

		public MyExtractCallback(ISevenZipInArchive inArchive) {
			pin = new PipedInputStream();
			try {
				pout = new PipedOutputStream(pin);
				final CountDownLatch latch = new CountDownLatch(1);
				in = new InputStream() {
					@Override
					public int read() throws IOException {
						return pin.read();
					}
					@Override
					public void close() throws IOException {
						super.close();
						latch.countDown();
					}
				};
				out = new OutputStream() {
					@Override
					public void write(int b) throws IOException {
						pout.write(b);
					}
					@Override
					public void close() throws IOException {
						while (latch.getCount() != 0) {
							try {
								latch.await();
							} catch (InterruptedException e) {
								// too bad
							}
						}
						super.close();
					}
				};
			} catch (IOException e) {
				// not sure what to do here... No errors to throw
				pin = null;
				pout = null;
				// we'll throw an exception later
			}
        }

        public ISequentialOutStream getStream(int index, 
                ExtractAskMode extractAskMode) throws SevenZipException {
        	
        	if(pin==null || pout==null){
        		throw new SevenZipException("Error creating streams to read 7z file.");
        	}
            this.index = index;
            if (extractAskMode != ExtractAskMode.EXTRACT) {
                return null;
            }
            return new ISequentialOutStream() {

                public int write(byte[] data) throws SevenZipException {
                	
                	try {
						out.write(data);
						size+=data.length;
					} catch (IOException e) {
						throw new SevenZipException(e);
					}
                    return data.length; // Return amount of proceed data
                }
            };
        }

        public void prepareOperation(ExtractAskMode extractAskMode) 
                throws SevenZipException {
        }

        public void setOperationResult(ExtractOperationResult 
                extractOperationResult) throws SevenZipException {
            if (extractOperationResult != ExtractOperationResult.OK) {
                System.err.println("Extraction error");
            } else {
                size = 0;
            }
        }

        public void setCompleted(long completeValue) throws SevenZipException {
        }

        public void setTotal(long total) throws SevenZipException {
        }

    }

}
