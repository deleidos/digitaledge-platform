package com.saic.rtws.commons.net.shutdown;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class ShutdownUtil {

	public static void writeToSocketStream(String message, OutputStream os){
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
		out.println(message);
		out.flush();
	}
	
	public static void shutdownOutput(OutputStream os){
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
		out.println(Shutdown.TERMINATION);
		out.flush();
	}
}
