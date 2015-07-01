package com.saic.rtws.commons.dao.jdbc.spatial;

import java.util.Collection;

import javax.sql.DataSource;

import com.saic.rtws.commons.dao.SpatialDao;
import com.saic.rtws.commons.dao.jdbc.DataAccessUtil;
import com.saic.rtws.commons.model.geo.Coordinate;
import com.saic.rtws.commons.util.index.CoordinateIndex;

/**
 * Base class for data access objects that provide geo-spatial searching by caching data set in a spatial index and
 * delegating search operation to that index.
 */
public abstract class AbstractSpatialDao<T> implements SpatialDao<T> {

	/** The data source through which to load the data set. */
	private DataSource dataSource;

	/** The result handler that will be used to build the spatial index. */
	private SpatialIndexResultHandler<T> indexHandler = new SpatialIndexResultHandler<T>();

	/** The spatial index. */
	private CoordinateIndex<T> index;

	/**
	 * Constructor.
	 */
	public AbstractSpatialDao() {
		super();
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
	 * The result handler that will be used to build the spatial index.
	 */
	public SpatialIndexResultHandler<T> getIndexHandler() {
		return indexHandler;
	}

	/**
	 * The result handler that will be used to build the spatial index.
	 */
	public void setIndexHandler(SpatialIndexResultHandler<T> indexHandler) {
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
	 * Finds the entry who's coordinate is closest to the given coordinate.
	 */
	public T findNearest(Coordinate location) {
		return index.findNearest(location);
	}

	/**
	 * Finds the entry who's coordinate is closest to the given coordinate and within the given radius (in meters).
	 */
	public T findNearest(Coordinate location, double max) {
		return index.findNearest(location, max);
	}

	/**
	 * Finds all entries who's coordinates are within the given distance of the given coordinate.
	 */
	public Collection<T> findNear(Coordinate location, double distance) {
		return index.findNear(location, distance);
	}

	/**
	 * Finds all entries who's coordinates are within the given bounding box.
	 */
	public Collection<T> findWithin(double north, double south, double east, double west) {
		return index.findWithin(north, south, east, west);
	}

	/**
	 * Builds the sql statement needed to load the index.
	 */
	protected abstract String buildSql();

}
