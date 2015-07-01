package com.saic.rtws.commons.net.listener.executor;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.saic.rtws.commons.config.UserDataProperties;
import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.client.target.PurgeLuceneIndexCommandClient;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.process.JettyProcess;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;
import com.saic.rtws.commons.net.listener.util.ProcessGroupUtil;

/**
 * Purges the lucene index directory from the given lucene datasink nodes.
 * 
 * If the command was received on the master node, the executor will parse
 * the json request for all target lucene datasink nodes and broadcast out 
 * the event.
 * 
 * If the command was received on a lucene data node, the executor will
 * stop jetty, remove the lucene directory, and start jetty.
 * 
 * If the command was receieved on all other nodes, the executor will
 * throw a ServerException.
 */
public class PurgeLuceneIndexCommandExecutor extends CommandExecutor {
	
	private static final Lock l = new ReentrantLock();
	private static final String LUCENE_DIR = "/mnt/rdafs/lucene";
	private static final BroadcastStatusMap status = new BroadcastStatusMap();
	
	static {
		status.registerCommand(Command.PURGE_LUCENE_INDEX);
	}
	
	private final Logger logger = LogManager.getLogger(this.getClass());

	public static PurgeLuceneIndexCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new PurgeLuceneIndexCommandExecutor(command, request, os);
	}
	
	private PurgeLuceneIndexCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}
	
	@Override
	public ExecuteResult execute() throws ServerException {
		
		if (canBroadcast()) {
			return broadcast(PurgeLuceneIndexCommandClient.class.getName(), status);
		} else if (isDatasinkLuceneNode()) {
			return purge();
		} else {
			throw new ServerException("This is not a datasink lucene node.");
		}
		
	}
	
	private boolean isDatasinkLuceneNode() {
		
		String value = UserDataProperties.getInstance().getString(UserDataProperties.RTWS_PROCESS_GROUP);
		if (value != null && value.equals(ProcessGroupUtil.DATASINK_LUCENE)){
			return true;
		}
		
		return false;
		
	}
	
	private ExecuteResult purge() throws ServerException {
		
		if (l.tryLock()) {
			try {
				JettyProcess jp = JettyProcess.newInstance(false);
		
				logger.debug(String.format("Stopping jetty for command %s.", this.command));
				if (! jp.stop()) {
					throw new ServerException("Failed to stop jetty while attempting to purge lucene index.");
				}
		
				try {
					logger.debug(String.format("Deleting directory %s for command %s.", LUCENE_DIR, this.command));
					FileUtils.deleteDirectory(new File(LUCENE_DIR));
				} catch (IOException e) {
					throw new ServerException(String.format("Fail to delete lucene dir %s.", LUCENE_DIR));
				}
		
				logger.debug(String.format("Starting jetty for command %s.", this.command));
				if (! jp.start()) {
					throw new ServerException("Failed to start jetty after purging lucene index.");
				}
				
				return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
			} finally {
				l.unlock();
			}
		}
		
		throw new ServerException("Failed to acquire lock on the lucene index resource.");
		
	}
	
}