package com.saic.rtws.commons.net.listener.util;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public final class SocketStreamWriter {
	
	private SocketStreamWriter() {
		// Not instantiable.
	}
	
	public static void write(String message, OutputStream os){
		
		PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(os)));
		out.println(message);
		out.flush();
		
	}
	
}