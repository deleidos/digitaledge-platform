-- Update Scaling config for Dimension Data Sink
UPDATE APPLICATION.DATASINK_CONFIG SET CAN_AUTOSCALE='Y' WHERE FQN='com.deleidos.rtws.ext.datasink.DimensionDataSink';
UPDATE APPLICATION.DATASINK_CONFIG SET SCALE_UP_FACTOR=0.75 WHERE FQN='com.deleidos.rtws.ext.datasink.DimensionDataSink';

-- Commit
commit;