package com.saic.rtws.commons.net.listener.util;

import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.IngestProcess;
import com.saic.rtws.commons.net.listener.process.JettyProcess;

public final class RestartAnyIngestProcessUtil {

	private RestartAnyIngestProcessUtil() {
		// Not instantiable.
	}
	
	public static boolean restart() throws ServerException {
		
		boolean result = true;

		if (SoftwareUtil.isIngestInstalled()) {
			result = result && IngestProcess.newInstance(false).restart();
		}
		
		if (SoftwareUtil.isInternalShardSearchApi()) {
			result = result && JettyProcess.newInstance(false).restart();
		}
		
		return result;
		
	}
	
}
