package com.saic.rtws.commons.net.listener.executor;

import java.io.OutputStream;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.quartz.SchedulerException;

import com.saic.rtws.commons.net.listener.Command;
import com.saic.rtws.commons.net.listener.common.ResponseBuilder;
import com.saic.rtws.commons.net.listener.exception.ScheduleException;
import com.saic.rtws.commons.net.listener.exception.ServerException;
import com.saic.rtws.commons.net.listener.model.ScheduleClient;
import com.saic.rtws.commons.net.listener.model.ScheduleJob;
import com.saic.rtws.commons.net.listener.status.ExecuteResult;

public class ScheduledTaskCommandExecutor extends CommandExecutor {

	private Logger logger = LogManager.getLogger(this.getClass());

	public static ScheduledTaskCommandExecutor newInstance(String command, String request, OutputStream os) {
		return new ScheduledTaskCommandExecutor(command, request, os);
	}

	private ScheduledTaskCommandExecutor(String command, String request, OutputStream os) {
		super(command, request, os);
	}

	@Override
	public ExecuteResult execute() throws ServerException {
		// command is either Command.SCHEDULE_TASK or
		// Command.DELETE_SCHEDULED_TASK. Request is the task to schedule/delete
		// in JSON
		try {
			ScheduleJob job = new ScheduleJob(request);
			if (command.equalsIgnoreCase(Command.MASTER_SCHEDULE_TASK)) {
				ScheduleClient.INSTANCE.scheduleMaster(job);
			} else if (command.equalsIgnoreCase(Command.MASTER_DELETE_SCHEDULED_TASK)) {
				ScheduleClient.INSTANCE.unScheduleMaster(job);
			} else if (command.equals(Command.SCHEDULE_TASK)) {
				ScheduleClient.INSTANCE.schedule(job);
			} else if (command.equalsIgnoreCase(Command.DELETE_SCHEDULED_TASK)) {
				ScheduleClient.INSTANCE.unSchedule(job);
			}
		} catch (ScheduleException e) {
			logger.error("Error creating Job from command.", e);
			return buildTerminateResult(ResponseBuilder.buildErrorResponse("Error occurred - problem creating JOB object from command."));
		} catch (SchedulerException e) {
			logger.error("Error interfacing with scheduling client.", e);
			return buildTerminateResult(ResponseBuilder.buildErrorResponse("Error occurred - problem interfacing with scheduling client."));
		}
		
		return buildTerminateResult(ResponseBuilder.buildSuccessResponse());
	}

}