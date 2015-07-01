package com.saic.rtws.commons.management;

import java.util.List;

import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetStat;
import org.hyperic.sigar.ProcStat;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Swap;
import org.hyperic.sigar.Tcp;

import com.saic.rtws.commons.model.jmx.FileSystemStatBean;

public interface OSMetricsMXBean extends ManagedBean {

	public double getUptime() throws SigarException;
	public double [] getLoadAverage() throws SigarException;
	public ProcStat getProcStat() throws SigarException;
	public CpuInfo getCpuInfo() throws SigarException;
	public CpuPerc [] getCpuPercStat() throws SigarException;
	public Mem getMemStat() throws SigarException;
	public Swap getSwapStat() throws SigarException;
	public List<FileSystemStatBean> getFileSystemStat() throws SigarException;
	public NetStat getNetStat() throws SigarException;
	public Tcp getTcpStat() throws SigarException;
	
}
