package com.saic.rtws.commons.cloud.platform.aws;

import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeVolumesRequest;
import com.amazonaws.services.ec2.model.Volume;
import com.amazonaws.services.ec2.model.VolumeAttachment;
import com.amazonaws.services.ec2.model.VolumeAttachmentState;

public class AwsVolumeStatusUtil {

	private Logger logger = Logger.getLogger(getClass());

	public Map<String, Boolean> isAttached(AmazonEC2 client, List<String> volumeIds) {
		return isAttached(client, volumeIds, true);
	}
	
	public Map<String, Boolean> isDetached(AmazonEC2 client, List<String> volumeIds) {
		return isDetached(client, volumeIds, true);
	}
	
	public Map<String, Boolean> isDetached(AmazonEC2 client, List<String> volumeIds, boolean checkIndefinitely) {
		
		Map<String, Boolean> detached = new HashMap<String, Boolean>();
		int attempts = 0, maxAttempts = 300;
		
		int loggingThreshold = Math.abs(maxAttempts / 5);
		int thresholdCounter = loggingThreshold;
		
		/*
		 * Keep trying to get status until all volume status has been obtained
		 */
		while (detached.size() != volumeIds.size()) {
			++attempts;
			++thresholdCounter;

			try {
				if (thresholdCounter >= loggingThreshold) {
					logger.info(String.format("Checking volume detachment status:  Detached: (%s) Not-Detached: (%s)", 
							detached.size(), volumeIds.size()));
					thresholdCounter = -1;
				}

				DescribeVolumesRequest request = new DescribeVolumesRequest(volumeIds);

				List<Volume> volumeStatus = client.describeVolumes(request).getVolumes();

				for (Volume ec2Volume : volumeStatus) {
					List<VolumeAttachment> attachments = ec2Volume.getAttachments();
					if (attachments == null || attachments.size() == 0) {
						logger.debug(String.format("Volume Id:  %s, State:  %s, Attachment Info:  null",
								ec2Volume.getVolumeId(), ec2Volume.getState()));
						detached.put(ec2Volume.getVolumeId(), true);
					} else {
						for (VolumeAttachment ec2VolumeAttachmentInfo : attachments) {
							logger.debug(String.format("Volume Id:  %s, State:  %s, Attachment Info:  %s, Attachment State:  %s, VolumeDetachState: %s",
									ec2Volume.getVolumeId(), ec2Volume.getState(), ec2VolumeAttachmentInfo, ec2VolumeAttachmentInfo.getState(),
									VolumeAttachmentState.Detached.toString()));
	
							if (ec2VolumeAttachmentInfo.getState().equals(VolumeAttachmentState.Detached.toString())) {
								detached.put(ec2Volume.getVolumeId(), true);
							}
						}
					}
				}
			} catch (IllegalFormatException e) {
				logger.error(e);
			} catch (AmazonServiceException e) {
				logger.error(e);
			} catch (AmazonClientException e) {
				logger.error(e);
			}

			try {
				if (detached.size() != volumeIds.size())
					Thread.sleep(1000);

				/*
				 * Give up after N attempts
				 */
				if (! checkIndefinitely && attempts >= maxAttempts)
					return detached;

			} catch (InterruptedException e) {
				logger.error(e);
			}
		}

		/*
		 * Give a final count
		 */
		try {
			logger.info(String.format("Checking volume attachment status:  Detached: (%s) Not-Detached: (%s)",
				detached.size(), volumeIds.size()));
		} catch (IllegalFormatException e) {
			logger.error(e);
		}

		return detached;
		
	}

	public Map<String, Boolean> isAttached(AmazonEC2 client,
			List<String> volumeIds, boolean checkIndefinitely) {
		Map<String, Boolean> attached = new HashMap<String, Boolean>();
		int attempts = 0, maxAttempts = 900;

		if (Boolean.getBoolean("RTWS_TEST_MODE")) {
			maxAttempts = 5;
			logger.warn("RTWS_TEST_MODE enabled: Dropping the maxAttemps to "
					+ maxAttempts + " for test mode.");
		}

		int loggingThreshold = Math.abs(maxAttempts / 5);
		int thresholdCounter = loggingThreshold;

		/*
		 * Keep trying to get status until all volume status has been obtained
		 */
		while (attached.size() != volumeIds.size()) {
			++attempts;
			++thresholdCounter;

			try {
				if (thresholdCounter >= loggingThreshold) {
					logger.info(String
							.format("Checking volume attachment status:  Attached: (%s) Un-Attached: (%s)",
									attached.size(), volumeIds.size() - attached.size()));
					thresholdCounter = -1;
				}

				DescribeVolumesRequest request = new DescribeVolumesRequest(
						volumeIds);

				List<Volume> volumeStatus = client.describeVolumes(request)
						.getVolumes();

				for (Volume ec2Volume : volumeStatus) {

					for (VolumeAttachment ec2VolumeAttachmentInfo : ec2Volume
							.getAttachments()) {

						logger.debug(String
								.format("Volume Id:  %s, State:  %s, Attachment Info:  %s, Attachment State:  %s, VolumeAttachState: %s",
										ec2Volume.getVolumeId(), ec2Volume
												.getState(),
										ec2VolumeAttachmentInfo,
										ec2VolumeAttachmentInfo.getState(),
										VolumeAttachmentState.Attached
												.toString()));

						if (ec2VolumeAttachmentInfo.getState().equals(
								VolumeAttachmentState.Attached.toString()))
							attached.put(ec2Volume.getVolumeId(), true);

					}
				}
			} catch (IllegalFormatException e) {
				logger.error(e);
			} catch (AmazonServiceException e) {
				logger.error(e);
			} catch (AmazonClientException e) {
				logger.error(e);
			}

			try {
				if (attached.size() != volumeIds.size())
					Thread.sleep(8000);

				/*
				 * Give up after N attempts
				 */
				if (!checkIndefinitely && attempts >= maxAttempts)
					return attached;

			} catch (InterruptedException e) {
				logger.error(e);
			}
		}

		/*
		 * Give a final count
		 */
		try {
			logger.info(String
					.format("Checking volume attachment status:  Attached: (%s) Un-Attached: (%s)",
							attached.size(), volumeIds.size() - attached.size()));
		} catch (IllegalFormatException e) {
			logger.error(e);
		}

		return attached;

	}
	
	public Map<String, Boolean> isAvailable(AmazonEC2 client,
			List<String> volumeIds, boolean checkIndefinitely) {
		Map<String, Boolean> available = new HashMap<String, Boolean>();
		int attempts = 0, maxAttempts = 900;

		if (Boolean.getBoolean("RTWS_TEST_MODE")) {
			maxAttempts = 5;
			logger.warn("RTWS_TEST_MODE enabled: Dropping the maxAttemps to "
					+ maxAttempts + " for test mode.");
		}

		int loggingThreshold = Math.abs(maxAttempts / 5);
		int thresholdCounter = loggingThreshold;

		/*
		 * Keep trying to get status until all volume status has been obtained
		 */
		while (available.size() != volumeIds.size()) {
			++attempts;
			++thresholdCounter;

			try {
				if (thresholdCounter >= loggingThreshold) {
					logger.info(String
							.format("Checking volume availability status:  Available: (%s) Un-Available: (%s)",
									available.size(), volumeIds.size() - available.size()));
					thresholdCounter = -1;
				}

				DescribeVolumesRequest request = new DescribeVolumesRequest(
						volumeIds);

				List<Volume> volumeStatus = client.describeVolumes(request)
						.getVolumes();

				for (Volume ec2Volume : volumeStatus) {

					logger.debug(String
							.format("Volume Id:  %s, State:  %s",
									ec2Volume.getVolumeId(), ec2Volume
											.getState()));

					if (ec2Volume.getState().equals("available"))
						available.put(ec2Volume.getVolumeId(), true);
				}
			} catch (IllegalFormatException e) {
				logger.error(e);
			} catch (AmazonServiceException e) {
				logger.error(e);
			} catch (AmazonClientException e) {
				logger.error(e);
			}

			try {
				if (available.size() != volumeIds.size())
					Thread.sleep(8000);

				/*
				 * Give up after N attempts
				 */
				if (!checkIndefinitely && attempts >= maxAttempts)
					return available;

			} catch (InterruptedException e) {
				logger.error(e);
			}
		}

		/*
		 * Give a final count
		 */
		try {
			logger.info(String
					.format("Checking volume availability status:  Available: (%s) Un-Available: (%s)",
							available.size(), volumeIds.size() - available.size()));
		} catch (IllegalFormatException e) {
			logger.error(e);
		}

		return available;
	}

}
