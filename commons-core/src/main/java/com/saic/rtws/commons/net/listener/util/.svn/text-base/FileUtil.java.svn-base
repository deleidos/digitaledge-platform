package com.saic.rtws.commons.net.listener.util;

import java.io.File;

public final class FileUtil {

	private FileUtil() {
		// Not instantiable.
	}
	
	public static boolean exists(String ... filepaths) {
		
		int counter = 0;
		for (String filepath : filepaths) {
			File file = new File(filepath);
			if (file.exists()) {
				counter++;
			}
		}
		
		if (filepaths.length == counter){
			return true;
		}
		
		return false;
		
	}
	
}