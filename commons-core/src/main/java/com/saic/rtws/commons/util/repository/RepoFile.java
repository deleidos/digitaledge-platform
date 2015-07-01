package com.saic.rtws.commons.util.repository;

public class RepoFile
{
	private String name;
	private boolean isDirectory;
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public boolean isDirectory()
	{
		return isDirectory;
	}
	public void setDirectory(boolean isDirectory)
	{
		this.isDirectory = isDirectory;
	}
}