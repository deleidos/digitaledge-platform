package com.saic.rtws.commons.config.filesystem;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.AbstractFileFilter;

/**
 * Checks files against a list of aboslute path wildcards.
 * 
 * @author rayju
 */
public class AbsPathWildcardFileFilter extends AbstractFileFilter {
	private static final IOCase DEFAULT_CASE_SENSITIVITY = IOCase.SENSITIVE;
	
	private List<String> wildcardPatterns;
	private IOCase caseSensitivity;

	public AbsPathWildcardFileFilter(List<String> wildcardPatterns) {
        this(wildcardPatterns, null);
    }

    public AbsPathWildcardFileFilter(List<String> wildcardPatterns, IOCase caseSensitivity) {
        super();
        if(wildcardPatterns == null)
        {
        	throw new IllegalArgumentException("Wildcard Patterns cannot be empty.");
        }
        this.wildcardPatterns = wildcardPatterns;
        this.caseSensitivity = (caseSensitivity != null ? caseSensitivity : DEFAULT_CASE_SENSITIVITY);
    }

    @Override
    public boolean accept(File dir, String name) {
        for (String wildcardPattern : wildcardPatterns) {
            if (FilenameUtils.wildcardMatch(dir.getAbsolutePath() + File.separator + name, wildcardPattern, caseSensitivity)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean accept(File file) {
        for (String wildcardPattern : wildcardPatterns) {
            if (FilenameUtils.wildcardMatch(file.getAbsolutePath(), wildcardPattern, caseSensitivity)) {
                return true;
            }
        }
        return false;
    }
}
