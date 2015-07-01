package com.saic.rtws.commons.dao.jdbc.range;

import com.saic.rtws.commons.dao.jdbc.range.ContainerDao;
import java.util.Collection;

import javax.sql.DataSource;

import com.saic.rtws.commons.dao.jdbc.DataAccessUtil;
import com.saic.rtws.commons.dao.jdbc.range.RangeIndexResultHandler;
import com.saic.rtws.commons.util.index.Index;
import com.saic.rtws.commons.util.index.Range;
import com.saic.rtws.commons.util.index.RangeIndex;

/**
 * Base class for data access objects that provide range searching by caching data set in a range index and
 * delegating search operation to that index.
 * @author floydac
 * 
 */
public abstract class AbstractContainerDao<T extends Comparable<T>,E> implements ContainerDao<T,E> {

	/** The data source through which to load the data set. */
	private DataSource dataSource;

	/** The result handler that will be used to build the range index. */
	private RangeIndexResultHandler<T,E> indexHandler;

	/** The range index. */
	private RangeIndex<T,E> index;
	
	/**
	 * Constructor.
	 */
	public AbstractContainerDao() {
		super();
		indexHandler = new RangeIndexResultHandler<T,E>();
	}
	
	public AbstractContainerDao(RangeIndex<T,E> index){
		super();
		indexHandler = new RangeIndexResultHandler<T,E>(index);
	}

	/**
	 * The data source through which to load the data set.
	 */
	public DataSource getDataSource() {
		return dataSource;
	}

	/**
	 * The data source through which to load the data set.
	 */
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	/**
	 * The result handler that will be used to build the range index.
	 */
	public RangeIndexResultHandler<T,E> getIndexHandler() {
		return indexHandler;
	}

	/**
	 * The result handler that will be used to build the range index.
	 */
	public void setIndexHandler(RangeIndexResultHandler<T,E> indexHandler) {
		this.indexHandler = indexHandler;
	}
	

	/**
	 * Builds the index.
	 */
	public void initialize() {
		index = DataAccessUtil.session(dataSource).executeQuery(buildSql(), null, indexHandler);
	}

	/**
	 * Cleans up cached resources.
	 */
	public void dispose() {
		index = null;
	}

	/**
	 * Finds the containers that contain the given value
	 */
	public Collection<E> findContainers(T value) {
		return index.find(value);
	}


	/**
	 * Builds the sql statement needed to load the index.
	 */
	protected abstract String buildSql();

}
