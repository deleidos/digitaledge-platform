package com.deleidos.rtws.systemcfg.serialize;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Unmarshaller;

import com.deleidos.rtws.commons.cloud.platform.aws.AwsConnectionFactory;
import com.deleidos.rtws.commons.cloud.platform.aws.AwsServiceInterface;
import com.deleidos.rtws.master.core.AggregatingStatisticsCollector;
import com.deleidos.rtws.master.core.ConsensusScalingMonitor;
import com.deleidos.rtws.master.core.DiscoveryStatisticsCollector;
import com.deleidos.rtws.master.core.DiskUtilizationScalingMonitor;
import com.deleidos.rtws.master.core.ThroughputScalingMonitor;
import com.deleidos.rtws.master.core.TimeInQueueScalingMonitor;
import com.deleidos.rtws.master.core.TimeInQueueScalingMonitor2;
import com.deleidos.rtws.master.core.beans.ClusterDefinition;
import com.deleidos.rtws.master.core.beans.ProcessGroup;
import com.deleidos.rtws.master.core.beans.ScalingMonitor;
import com.deleidos.rtws.master.core.net.shutdown.CommandShutdown;


public class DefinitionSerializerImpl implements DefinitionSerializer {

	Class<?>[] DEFAULT_CLASSES = {ClusterDefinition.class, ProcessGroup.class, AwsServiceInterface.class, AwsConnectionFactory.class,
			CommandShutdown.class, ScalingMonitor.class, ConsensusScalingMonitor.class, DiskUtilizationScalingMonitor.class, 
			ThroughputScalingMonitor.class, TimeInQueueScalingMonitor.class, TimeInQueueScalingMonitor2.class, AggregatingStatisticsCollector.class,
			DiscoveryStatisticsCollector.class};
	Class<?>[] configClasses = DEFAULT_CLASSES;
	JAXBContext context = null;
	Marshaller marshaller;
	Unmarshaller unMarshaller;
	
	public void initialize() {
		try {
			// Configure an XML marshaller for the classes referenced in the configuration file.
			context = JAXBContext.newInstance(configClasses);
			unMarshaller = context.createUnmarshaller();
			marshaller = context.createMarshaller();
			marshaller.setProperty("jaxb.formatted.output", true);
		} catch(JAXBException jaxbe) {
			System.out.println("Error creating marshaller " + jaxbe.getErrorCode() + " : " +jaxbe.getMessage());
		}
	}
	public <T extends Object> void createDefinitionFile(T  definition, File file) throws MarshalException {
		try {
			marshaller.marshal(definition, System.out);
		} catch(JAXBException jaxbe) {
			throw new MarshalException("Error marshalling class " + jaxbe.getErrorCode() + " : " +jaxbe.getMessage());
		}
	}
	
	public <T extends Object> T createObject(File inputFile, Class<T> type) throws MarshalException {
		T definition = null;
		
		try {
			definition = type.cast(unMarshaller.unmarshal(inputFile));
		} catch(JAXBException jaxbe) {
			throw new MarshalException("Error unmarshalling class " + jaxbe.getErrorCode() + " : " +jaxbe.getMessage());
		} catch(Exception ex) {
			throw new MarshalException("Unknown error: " + ex.getMessage());
		}
		return definition;
	}
	
	public void dispose() {
		// Not Implemented
	}

}
