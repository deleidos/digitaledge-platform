package com.saic.rtws.commons.config.filesystem;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.AndFileFilter;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NotFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang.StringUtils;

/**
 * <p>
 * Finds all files not matching any specified exclusion patterns ordered by
 * the 0-n order of the orderedInclusionPatterns member var and gives them an id of
 * the rootConfigDir relative path (Unix style separators, /, are used in the id).
 * </p>
 * <br />
 * <b>Patterns</b>
 * <p>
 * 	Inclusion and Exclusion patterns are relative to the root directory (No leading slash).  Also, they
 *  can be tailored to match files or directories as needed.
 * </p>
 * <b>Internal Pattern Augmentation</b>
 * <p>
 * 	The excludeHiddenResources flag will be applied to inclusion and exclusion patterns automatically.
 *  This flag is true by default.
 * </p>
 * <b>Pattern Syntax</b>
 * <p>
 * Uses standard file-system style regex syntax with * and ? matching any or 1
 * character respectively. Note that these wildcards are more like regexs than
 * the ant direcotry regex support.  So, * can match any character including a
 * filesytem path separator.  Thus, there is no need to use '**' to recurse multiple
 * directories.
 * [IMPORTANT NOTE:]  All files, including system files (read .svn/ files) can be matched. 
 * </p>
 * <br />
 * <b>Load Order</b>
 * <p>
 * With inclusion patterns utilizing directory wildcards, the order of the returned
 * ConfigEntries will be partially determined by the rootConfigDirProcessingMode
 * which defaults to BREADTH_FIRST.  Within a single directory, the file or directory
 * listed order is not guarenteed. Thus, if you need specific file loading within a
 * directory, you should specify that resource via an explicit pattern before any
 * generic patterns.
 * </p>
 * 
 * @author rayju
 */
public class DirRelativePatternBasedLoadPrecedence implements ConfigLoadPrecedence {
	private static final DirectoryProcessingMode DEFAULT_ROOT_DIR_PROCESSING_MODE = DirectoryProcessingMode.BREADTH_FIRST;
	private static final boolean DEFAULT_EXCLUDE_HIDDEN_RESOURCES = true;
	private File rootConfigDir;
	private DirectoryProcessingMode rootConfigDirProcessingMode;
	private Boolean excludeHiddenResources;
	private List<String> orderedInclusionPatterns;
	private List<String> exclusionPatterns;
	
	public DirRelativePatternBasedLoadPrecedence(File rootConfigDir, List<String> orderedInclusionPatterns)
	{
		this(rootConfigDir, orderedInclusionPatterns, null);
	}
	
	public DirRelativePatternBasedLoadPrecedence(File rootConfigDir, List<String> orderedInclusionPatterns, List<String> exclusionPatterns)
	{
		this(rootConfigDir, orderedInclusionPatterns, exclusionPatterns, null);
	}
	
	public DirRelativePatternBasedLoadPrecedence(File rootConfigDir,
			List<String> orderedInclusionPatterns,
			List<String> exclusionPatterns,
			DirectoryProcessingMode rootConfigDirProcessingMode)
	{
		this(rootConfigDir, orderedInclusionPatterns, exclusionPatterns, rootConfigDirProcessingMode, null);
	}
	
	public DirRelativePatternBasedLoadPrecedence(File rootConfigDir,
			List<String> orderedInclusionPatterns,
			List<String> exclusionPatterns,
			DirectoryProcessingMode rootConfigDirProcessingMode,
			Boolean excludeHiddenResources)
	{
		this.rootConfigDir = rootConfigDir;
		this.orderedInclusionPatterns = orderedInclusionPatterns;
		this.exclusionPatterns = exclusionPatterns;
		this.rootConfigDirProcessingMode = rootConfigDirProcessingMode;
		this.excludeHiddenResources = excludeHiddenResources;
	}
	
	@Override
	public List<ConfigEntry> getConfigEntries() throws ConfigurationException
	{
		List<ConfigEntry> result = new ArrayList<ConfigEntry>();
		
		String rootDirPath = rootConfigDir.getAbsolutePath();
		
		IOFileFilter includedResourcesFilter = null;
		if(exclusionPatterns == null || exclusionPatterns.size() == 0)
		{
			includedResourcesFilter = TrueFileFilter.INSTANCE;
		}
		else
		{
			List<String> fqExclusionPatterns = new ArrayList<String>();
			for(String currExclusionPattern : exclusionPatterns)
			{
				if(StringUtils.isBlank(currExclusionPattern))
				{
					throw new ConfigurationException("Empty Exclusion Pattern specified.");
				}
				else if(currExclusionPattern.startsWith("/"))
				{
					throw new ConfigurationException("Inclusion Pattern should be relative (i.e. not start with '/').");
				}
				fqExclusionPatterns.add(rootDirPath + File.separator + FilenameUtils.separatorsToSystem(currExclusionPattern));
			}
			includedResourcesFilter = new NotFileFilter(new AbsPathWildcardFileFilter(fqExclusionPatterns));
		}
		
		boolean tmpExcludeHiddenResources = (excludeHiddenResources != null ? excludeHiddenResources.booleanValue() : DEFAULT_EXCLUDE_HIDDEN_RESOURCES);
		// Hidden files included by default ... only need to augment the filter if hidden resources should be excluded
		if(tmpExcludeHiddenResources)
		{
			includedResourcesFilter = new AndFileFilter(HiddenFileFilter.VISIBLE, includedResourcesFilter);
		}
		
		List<File> includedFiles = listFiles(rootConfigDir,
				(rootConfigDirProcessingMode != null ? rootConfigDirProcessingMode : DEFAULT_ROOT_DIR_PROCESSING_MODE),
				includedResourcesFilter,
				includedResourcesFilter);
				
		if(includedFiles != null && includedFiles.size() > 0)
		{
			List<File> unmatchedFiles = new ArrayList<File>(includedFiles);
			
			for(String currInclusionPattern : orderedInclusionPatterns)
			{
				if(StringUtils.isBlank(currInclusionPattern))
				{
					throw new ConfigurationException("Empty Inclusion Pattern specified.");
				}
				else if(currInclusionPattern.startsWith("/"))
				{
					throw new ConfigurationException("Inclusion Pattern should be relative (i.e. not start with '/').");
				}
				
				List<Integer> matchedFileIndices = new ArrayList<Integer>();
				File currFile = null;
				for(int index=0; index < unmatchedFiles.size(); index++)
				{
					currFile = unmatchedFiles.get(index);
					
					String currFileAbsPath = currFile.getAbsolutePath();
					String absInclusionPattern = rootDirPath + File.separator + FilenameUtils.separatorsToSystem(currInclusionPattern);
					if(FilenameUtils.wildcardMatch(currFileAbsPath, absInclusionPattern))
					{
						result.add(new ConfigEntry(getRootRelPath(currFile), currFile));
						matchedFileIndices.add(0, new Integer(index));
					}
				}
				
				if(matchedFileIndices.size() > 0)
				{
					for(Integer matchedFileIndex : matchedFileIndices)
					{
						unmatchedFiles.remove(matchedFileIndex.intValue());
					}
				}
				
				if(unmatchedFiles.size() == 0)
				{
					break;
				}
			}
		}
		
		return result;
	}
	
	private String getRootRelPath(File file)
	{
		String rootDirPath = rootConfigDir.getPath() + File.separator;
		String configFilePath = file.getPath();
		int configFileRelPathStartIndex = configFilePath.indexOf(rootDirPath) + rootDirPath.length();
		
		String result = FilenameUtils.separatorsToUnix(configFilePath.substring(configFileRelPathStartIndex));
		return result;
	}
	
	private List<File> listFiles(File directory, DirectoryProcessingMode processingMode, IOFileFilter fileFilter, IOFileFilter dirFilter) throws ConfigurationException
	{
		List<File> result = new ArrayList<File>();
		
		if(fileFilter == null)
		{
			throw new ConfigurationException("configuration FileFilter cannot be null");
		}
		
		if(directory != null && directory.isDirectory() && directory.canRead())
		{
			File[] dirs = null;
			switch(processingMode)
			{
				case BREADTH_FIRST:
					result.addAll(Arrays.asList(directory.listFiles((FileFilter)new AndFileFilter(FileFileFilter.FILE, fileFilter))));
					
					if(dirFilter != null)
					{
						dirs = directory.listFiles((FileFilter)new AndFileFilter(DirectoryFileFilter.DIRECTORY, dirFilter));
						for(File dir : dirs)
						{
							result.addAll(listFiles(dir, processingMode, fileFilter, dirFilter));
						}
					}
					break;
				case DEPTH_FIRST:
					if(dirFilter != null)
					{
						dirs = directory.listFiles((FileFilter)new AndFileFilter(DirectoryFileFilter.DIRECTORY, dirFilter));
						for(File dir : dirs)
						{
							result.addAll(listFiles(dir, processingMode, fileFilter, dirFilter));
						}
					}
					
					result.addAll(Arrays.asList(directory.listFiles((FileFilter)new AndFileFilter(FileFileFilter.FILE, fileFilter))));
					break;
				default:
					throw new ConfigurationException("Unknown DirectoryProcessingMode '" + processingMode.toString() + "'");
			}
		}
		
		return result;
	}
}
