package com.saic.rtws.commons.dao;

import java.util.Collection;

import com.saic.rtws.commons.model.geo.Coordinate;
import com.saic.rtws.commons.util.Initializable;

public interface SpatialDao<T> extends Initializable {
	
	public T findNearest(Coordinate location);
	public T findNearest(Coordinate location, double max);
	public Collection<T> findNear(Coordinate location, double distance);
	public Collection<T> findWithin(double north, double south, double east, double west);
	
}
