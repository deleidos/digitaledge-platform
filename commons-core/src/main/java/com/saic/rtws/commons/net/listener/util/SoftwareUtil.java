package com.saic.rtws.commons.net.listener.util;

import java.io.File;

import com.saic.rtws.commons.config.UserDataProperties;

public final class SoftwareUtil {
	
	public static final String MASTER_HOME_DIR = "/usr/local/rtws/master";
	public static final String MASTER_BIN_DIR = "/usr/local/rtws/master/bin";
	public static final String MASTER_START_SCRIPT_FILENAME = "start_master.sh";
	public static final String MASTER_STOP_SCRIPT_FILENAME = "stop_master.sh";
	public static final String MASTER_START_SCRIPT_PATH = MASTER_BIN_DIR + File.separator + MASTER_START_SCRIPT_FILENAME;
	public static final String MASTER_STOP_SCRIPT_PATH = MASTER_BIN_DIR + File.separator + MASTER_STOP_SCRIPT_FILENAME;
	
	public static final String INGEST_HOME_DIR = "/usr/local/rtws/ingest";
	public static final String INGEST_CONF_DIR = "/usr/local/rtws/ingest/conf";
	public static final String INGEST_CONF_FILE = "/saic-rtws-ingest-conf.tar.gz";
	public static final String INGEST_BIN_DIR = "/usr/local/rtws/ingest/bin";
	public static final String INGEST_START_SCRIPT_FILENAME = "start_ingest.sh";
	public static final String INGEST_START_WITH_DEBUG_SCRIPT_FILENAME = "start_ingest_with_debug.sh";
	public static final String INGEST_STOP_SCRIPT_FILENAME = "stop_ingest.sh";
	public static final String INGEST_START_SCRIPT_PATH = INGEST_BIN_DIR + File.separator + INGEST_START_SCRIPT_FILENAME;
	public static final String INGEST_START_WITH_DEBUG_SCRIPT_PATH = INGEST_BIN_DIR + File.separator + INGEST_START_WITH_DEBUG_SCRIPT_FILENAME;
	public static final String INGEST_STOP_SCRIPT_PATH = INGEST_BIN_DIR + File.separator + INGEST_STOP_SCRIPT_FILENAME;

	public static final String TRANSPORT_HOME_DIR = "/usr/local/rtws/transport";
	public static final String TRANSPORT_BIN_DIR = "/usr/local/rtws/transport/bin";
	public static final String TRANSPORT_CONF_DIR = "/usr/local/rtws/transport/conf";
	public static final String TRANSPORT_CONF_FILE = "/saic-rtws-transport-conf.tar.gz";
	public static final String TRANSPORT_START_SCRIPT_FILENAME = "start_transport.sh";
	public static final String TRANSPORT_STOP_SCRIPT_FILENAME = "stop_transport.sh";
	public static final String TRANSPORT_START_SCRIPT_PATH = TRANSPORT_BIN_DIR + File.separator + TRANSPORT_START_SCRIPT_FILENAME;
	public static final String TRANSPORT_STOP_SCRIPT_PATH = TRANSPORT_BIN_DIR + File.separator + TRANSPORT_STOP_SCRIPT_FILENAME;
	
	public static final String JETTY_HOME_DIR = "/usr/local/jetty";
	public static final String JETTY_BIN_DIR = "/usr/local/jetty/bin";
	public static final String JETTY_WEBAPPS_DIR = "/usr/local/jetty/webapps";
	public static final String JETTY_START_SCRIPT_FILENAME = "jetty_start.sh";
	public static final String JETTY_START_REMOTE_DEBUG_SCRIPT_FILENAME = "jetty_start_remote_debug.sh";
	public static final String JETTY_STOP_SCRIPT_FILENAME = "jetty_stop.sh";
	public static final String JETTY_START_SCRIPT_PATH = JETTY_BIN_DIR + File.separator + JETTY_START_SCRIPT_FILENAME;
	public static final String JETTY_START_REMOTE_DEBUG_SCRIPT_PATH = JETTY_BIN_DIR + File.separator + JETTY_START_REMOTE_DEBUG_SCRIPT_FILENAME;
	public static final String JETTY_STOP_SCRIPT_PATH = JETTY_BIN_DIR + File.separator + JETTY_STOP_SCRIPT_FILENAME;
	
	public static final String SEARCHAPI_WEBAPP_PATH = JETTY_WEBAPPS_DIR + File.separator + "searchapi";
	public static final String SEARCHAPI_WEBAPP_WEBINF_PATH = SEARCHAPI_WEBAPP_PATH + File.separator + "WEB-INF";
	public static final String SEARCHAPI_WEBAPP_CLASSES_PATH = SEARCHAPI_WEBAPP_WEBINF_PATH + File.separator + "classes";
	
	public static final String ACTIVEMQ_HOME_DIR = "/usr/local/apache-activemq";
	public static final String ACTIVEMQ_BIN_DIR = "/usr/local/apache-activemq/bin";
	public static final String ACTIVEMQ_SCRIPT_FILENAME = "activemq";
	public static final String ACTIVEMQ_JAR_FILENAME = "run.jar";
	public static final String ACTIVEMQ_SCRIPT_PATH = ACTIVEMQ_BIN_DIR + File.separator + ACTIVEMQ_SCRIPT_FILENAME;
	public static final String ACTIVEMQ_JAR_PATH = ACTIVEMQ_BIN_DIR + File.separator + ACTIVEMQ_JAR_FILENAME;
	public static final String ACTIVEMQ_CONTROL_DIR = "/usr/local/rtws/commons-core/bin/boot";
	public static final String ACTIVEMQ_START_SCRIPT_FILENAME = "start_activemq.sh";
	public static final String ACTIVEMQ_STOP_SCRIPT_FILENAME = "activemq_stop.sh";
	public static final String ACTIVEMQ_START_SCRIPT_PATH = ACTIVEMQ_CONTROL_DIR + File.separator + ACTIVEMQ_START_SCRIPT_FILENAME;
	public static final String ACTIVEMQ_STOP_SCRIPT_PATH = ACTIVEMQ_CONTROL_DIR + File.separator + ACTIVEMQ_STOP_SCRIPT_FILENAME;
	
	private SoftwareUtil() {
		// Not instantiable.
	}
	
	public static boolean isMasterInstalled() {
		
		File startScript = new File(MASTER_START_SCRIPT_PATH);
		File stopScript = new File(MASTER_STOP_SCRIPT_PATH);
		
		if (startScript.exists() && stopScript.exists()) {
			return true;
		}
		
		return false;
		
	}
	
	public static boolean isTransportInstalled() {
		
		File startScript = new File(TRANSPORT_START_SCRIPT_PATH);
		File stopScript = new File(TRANSPORT_STOP_SCRIPT_PATH);
		
		if (startScript.exists() && stopScript.exists()) {
			return true;
		}
		
		return false;
		
	}
	
	public static boolean isIngestInstalled() {
		
		File startScript = new File(INGEST_START_SCRIPT_PATH);
		File stopScript = new File(INGEST_STOP_SCRIPT_PATH);
		
		if (startScript.exists() && stopScript.exists() && isIngest()) {
			return true;
		}
		
		return false;
		
	}
	
	public static boolean isIngest() {
		
		String ingestConfig = UserDataProperties.getInstance().getString("RTWS_INGEST_CONFIG");
		return (ingestConfig != null && ! ingestConfig.isEmpty());
		
	}
	
	public static boolean isJettyInstalled() {
		
		File startScript = new File(JETTY_START_SCRIPT_PATH);
		File startDebugScript = new File(JETTY_START_REMOTE_DEBUG_SCRIPT_PATH);
		File stopScript = new File(JETTY_STOP_SCRIPT_PATH);
		
		if (startScript.exists() && startDebugScript.exists() && stopScript.exists()) {
			return true;
		}
		
		return false;
		
	}
	
	public static boolean isInternalShardSearchApi() {
		
		String ingestConfigFile = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_INGEST_CONFIG);
		
		if (ingestConfigFile == null) {
			return false;
		}
		
		String ingestConfFilePath = SEARCHAPI_WEBAPP_CLASSES_PATH +  File.separator + ingestConfigFile;
		
		if (SoftwareUtil.isJettyInstalled() && 
			new File(ingestConfFilePath).exists()) {
			return true;
		}
			
		return false;
		
	}
	
	public static boolean isActiveMQInstalled() {
		
		File activeMQJar = new File(ACTIVEMQ_JAR_PATH);
		File activeMQScript = new File(ACTIVEMQ_SCRIPT_PATH);
		File startScript = new File(ACTIVEMQ_START_SCRIPT_PATH);
		File stopScript = new File(ACTIVEMQ_STOP_SCRIPT_PATH);
		
		if(activeMQJar.exists() && activeMQScript.exists() && startScript.exists() && stopScript.exists()) {
			return true;
		}
		
		return false;
		
	}
}