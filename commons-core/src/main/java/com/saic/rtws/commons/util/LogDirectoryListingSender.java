package com.saic.rtws.commons.util;

import java.io.File;
import java.io.OutputStream;
import java.net.Socket;

public interface LogDirectoryListingSender extends Runnable {

	public void setLogDirectory(File directoryOfLogs, Socket socket);
	public void setLogDirectory(File directoryOfLogs, OutputStream os);

}
