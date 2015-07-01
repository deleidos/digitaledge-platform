package com.saic.rtws.commons.util.repository;

import java.io.Serializable;
import java.util.Comparator;

public class RepoFileComparator implements Comparator<RepoFile>, Serializable
{
	private static final long serialVersionUID = 1L;

	@Override
	public int compare(RepoFile file1, RepoFile file2)
	{
		int result = 0;

		if(file1 == null && file2 == null)
		{
			result = 0;
		}
		else if(file1 != null && file2 == null)
		{
			result = 1;
		}
		else if(file1 == null && file2 != null)
		{
			result = -1;
		}
		else
		{
			if(file1.isDirectory() && !file2.isDirectory())
			{
				result = 1;
			}
			if(!file1.isDirectory() && file2.isDirectory())
			{
				result = -1;
			}
			else
			{
				if(file1.getName() == null && file2.getName() == null)
				{
					result = 0;
				}
				else if(file1.getName() != null && file2.getName() == null)
				{
					result = 1;
				}
				else if(file1.getName() == null && file2.getName() != null)
				{
					result = -1;
				}
				else
				{
					result = file1.getName().compareTo(file2.getName());
				}
			}
		}
		
		return result;
	}
}
