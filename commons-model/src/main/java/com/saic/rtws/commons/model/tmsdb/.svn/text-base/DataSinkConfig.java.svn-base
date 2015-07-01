package com.saic.rtws.commons.model.tmsdb;

/**
 * TMS DB DataSinkConfig DTO object.
 * 
 * @author LUCECU
 *
 */
public class DataSinkConfig {

	/**
	 * Fully qualified name.
	 */
	private String fqn;
	/**
	 * Can data sink auto scale.
	 */
	private boolean canAutoScale;
	/**
	 * Ingest configuration file name.
	 */
	private String ingestConfigFilename;
	/**
	 * Pipeline xml filename.
	 */
	private String pipelineXmlFilename;
	/**
	 * Pipeline XML template.
	 */
	private String pipelineXmlTemplate;
	/**
	 * Process group dependencies.
	 */
	private String processGroupDependencies;
	
	/**
	 * Default constructor.
	 */
	public DataSinkConfig(){
		
	}
	
	/**
	 * Set the fully qualified name.
	 * 
	 * @param fqn String value
	 */
	public void setFqn(String fqn){
		this.fqn = fqn;
	}
	
	/**
	 * Get the fully qualified name.
	 * 
	 * @return String value
	 */
	public String getFqn(){
		return fqn;
	}
	
	/**
	 * Set can auto scale.
	 * 
	 * @param canAutoScale boolean value
	 */
	public void setCanAutoScale(char autoScale){
		if(Character.toLowerCase(autoScale) == 'y'){
			this.canAutoScale = true;
		}
		else{
			this.canAutoScale = false;
		}
	}
	
	/**
	 * Get can auto scale.
	 * 
	 * @return boolean value
	 */
	public boolean getCanAutoScale(){
		return canAutoScale;
	}
	
	/**
	 * Set ingest config filename.
	 * 
	 * @param ingestConfigFilename String value
	 */
	public void setIngestConfigFilename(String ingestConfigFilename){
		this.ingestConfigFilename = ingestConfigFilename;
	}
	
	/**
	 * Get ingest config filename.
	 * 
	 * @return String value
	 * 
	 * @deprecated this database column no longer exists
	 */
	public String getIngestConfigFilename(){
		return ingestConfigFilename;
	}
	
	/**
	 * Set pipeline xml filename.
	 * 
	 * @param pipelineXmlFilename String value
	 */
	public void setPipelineXmlFilename(String pipelineXmlFilename){
		this.pipelineXmlFilename = pipelineXmlFilename;
	}
	
	/**
	 * Get pipeline xml filename.
	 * 
	 * @return String value
	 * 
	 * @deprecated this database column no longer exists
	 */
	public String getPipelineXmlFilename(){
		return pipelineXmlFilename;
	}
	
	/**
	 * Set pipeline xml template.
	 * 
	 * @param pipelineXmlTemplate String value
	 */
	public void setPipelineXmlTemplate(String pipelineXmlTemplate){
		this.pipelineXmlTemplate = pipelineXmlTemplate;
	}
	
	/**
	 * Get pipeline xml template.
	 * 
	 * @return String value
	 * 
	 * @deprecated this database column no longer exists
	 */
	public String getPipelineXmlTemplate(){
		return pipelineXmlTemplate;
	}
	
	/**
	 * Set the process group dependencies.
	 * 
	 * @param processGroupDependencies String value
	 */
	public void setProcessGroupDependencies(String processGroupDependencies){
		this.processGroupDependencies = processGroupDependencies;
	}
	
	/**
	 * Get the process group dependencies.
	 * 
	 * @return String value
	 */
	public String getProcessGroupDependencies(){
		return processGroupDependencies;
	}
}
