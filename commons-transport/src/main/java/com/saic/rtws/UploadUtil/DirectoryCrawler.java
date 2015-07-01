package com.saic.rtws.UploadUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

public class DirectoryCrawler {
	
	private File dirToCrawl;
	private boolean recursive;
	private List<File> allFiles;

	private int currentIndex;
	
	public DirectoryCrawler(File directoryToCrawl, boolean recursive){
		dirToCrawl = directoryToCrawl;
		this.recursive = recursive;
		allFiles = new ArrayList<File>();
		if(directoryToCrawl.isDirectory()){
			searchFiles(dirToCrawl);
		}else{
			allFiles.add(directoryToCrawl);
		}
		currentIndex=-1;
	}
	
	public int fileCount(){
		return allFiles.size();
	}
	
	private void searchFiles(File dir){
		for(File f : dir.listFiles()){
			if(f.isDirectory() && recursive){
				searchFiles(f);
			}else{
				allFiles.add(f);
			}
		}
	}
	
	public File nextFile(){
		currentIndex++;
		if(currentIndex<allFiles.size()){
			return allFiles.get(currentIndex);
		}
		return null;
	}
	
}
