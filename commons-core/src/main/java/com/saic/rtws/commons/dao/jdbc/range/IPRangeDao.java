package com.saic.rtws.commons.dao.jdbc.range;

import net.sf.json.JSONObject;

import com.saic.rtws.commons.dao.jdbc.range.AbstractContainerDao;
import com.saic.rtws.commons.util.index.NoOverlapRangeIndex;

/**
 * Data access object used to load the IPTOCOUNTRY table into a range index.
 * @author floydac
 * 
 */
public class IPRangeDao extends AbstractContainerDao<Long,JSONObject>{

	public static final String ipstart = "STARTIPNUM";
	public static final String ipend = "ENDIPNUM";
	public static final String country = "COUNTRY";
	public static final String region = "REGION";
	public static final String postalCode = "POSTALCODE";
	public static final String latitude = "LATITUDE";
	public static final String longitude = "LONGITUDE";
	public static final String metroCode = "METROCODE";
	public static final String areaCode = "AREACODE";
	
	public IPRangeDao(){
		super(new NoOverlapRangeIndex<Long,JSONObject>());
		super.getIndexHandler().setRecordBuilder(new IPRangeRecordBuilder());
		super.getIndexHandler().setRangeBuilder(new LongRangeBuilder(IPRangeDao.ipstart, IPRangeDao.ipend));
	}
	
	@Override
	protected String buildSql() {
		return "SELECT " + IPRangeDao.ipstart + ", " + IPRangeDao.ipend + ", " + IPRangeDao.country + ", " + IPRangeDao.region + ", " + 
			               IPRangeDao.postalCode + ", " + IPRangeDao.latitude + ", " + IPRangeDao.longitude + ", " + IPRangeDao.metroCode +", "+ IPRangeDao.areaCode + " FROM IP_GEO";
	}

}
