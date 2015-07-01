package com.saic.rtws.commons.util.repository;

/**
 * Depending on the configured repository type, it constructs
 * the appropriate system repository.
 */
public class SystemRepositoryFactory {
	
	/**
	 * Given a string, creates the appropriate system repository.
	 */
	public static AbstractSystemRepository createSystemRepository(String type) {
		
		return createSystemRepository(determineRepositoryType(type));
		
	}
	
	/**
	 * Given the repository type, creates the appropriate service timer task.
	 */
	public static AbstractSystemRepository createSystemRepository(RepositoryType type) {
		
		if (type != null && type == RepositoryType.CONFIG_DIR) {
			return new ConfigDirSystemRepository();
		}
		else if (type == RepositoryType.JCR) {
			return new JcrSystemRepository();
		}
		else {
			return null;
		}
		
	}
	
	/**
	 * Given a string, determine its repository type.
	 */
	private static RepositoryType determineRepositoryType(String type) {
		
		if (type != null && RepositoryType.CONFIG_DIR.name().equalsIgnoreCase(type)) {
			return RepositoryType.CONFIG_DIR;
		}
		else if (type != null && RepositoryType.JCR.name().equalsIgnoreCase(type)) {
			return RepositoryType.JCR;
		}
		else {
			return null;
		}
		
	}
	
}