package com.saic.rtws.commons.util.index;

import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import org.geotools.referencing.GeodeticCalculator;

import com.saic.rtws.commons.model.geo.Coordinate;

/**
 * This is a really crappy implementation of a geo-spatial index. It provides methods to search for records based on
 * geo-spatial proximity. The implementation provided here divides the lat/lon space into bins, storing records into the
 * appropriate bin based on the coordinate position. Retrieval is optimized by narrowing the search to only those bins
 * that overlap the given search criteria.
 * 
 * In practice, this is only efficient for small data sets that are distributed over large geographic areas. Because
 * searching within each bin is performed linearly, data sets that a very large or very tight tend to overload some
 * bins. Thus, searches within heavily loaded bins yield near worst case performance.
 * 
 * TODO Recommend replacing this with a disk backed R-tree implementation.
 */
public class CoordinateIndex<E> implements Index<Coordinate, E> {

	/** Constant represent half the circumference of the earth. */
	protected static final double HALF_WAY_AROUND_THE_WORLD = 20003930.458;

	/** The number of bins to be allocated for both axis. */
	private static int SIZE = 720;

	/** The bins. */
	@SuppressWarnings("unchecked")
	private Collection<Entry<E>>[][] index = new LinkedList[SIZE][SIZE];

	/**
	 * Constructor.
	 */
	public CoordinateIndex() {
		super();
	}

	/**
	 * Associates the given entry with the given coordinate in this index.
	 * TODO Consider rectifying the provided coordinate before adding to index to insure that lat/lon 
	 * are with bounds for future distance calculations.
	 */
	public void associate(Coordinate value, E entry) {
		int x = hashLongitude(value.getLongitude());
		int y = hashLatitude(value.getLatitude());
		Entry<E> item = new Entry<E>(value, entry);
		if (index[x][y] == null) {
			index[x][y] = new LinkedList<Entry<E>>(Collections.singleton(item));
		} else {
			index[x][y].add(item);
		}
	}

	/**
	 * Associates the given entry with the given coordinate in this index.
	 */
	public void associate(double lat, double lon, E entry) {
		associate(new Coordinate(lat, lon), entry);
	}

	/**
	 * Finds the entry who's coordinate is closest to the given coordinate.
	 */
	public E findNearest(Coordinate location) {
		return findNearest(location, Double.MAX_VALUE);
	}

	/**
	 * Finds the entry who's coordinate is closest to the given coordinate and within the given radius (in meters).
	 */
	public E findNearest(Coordinate location, double max) {
		if(Math.abs(location.getLatitude()) > 66.0) {
			return findNearestPolarLocation(location, max);
		} else {
			return findNearestEquatorialLocation(location, max);
		}
	}

	/**
	 * Performs a search by starting at the bin associated with the given location and proceeding
	 * outward in concentric bands. This approach work best for areas near the equator, where the
	 * longitudes and latitudes are roughly equal.
	 */
	private E findNearestEquatorialLocation(Coordinate location, double max) {
		
		Entry<E> nearestEntry = null;
		double nearestDistance = max;
		double maxRadius = computeMaximumSearchRadius(location, max);

		for (int radius = 0; radius <= maxRadius; radius++) {
			RadialSearch search = new RadialSearch(location, radius);
			while (search.next()) {
				for (Entry<E> entry : get(search.getX(), search.getY())) {
					double distance = computeOrthodromicDistance(location, entry.value);
					if (distance <= nearestDistance) {
						nearestEntry = entry;
						nearestDistance = distance;
					}
				}

			}
			if (nearestEntry != null) {
				break;
			}
		}

		return (nearestEntry != null) ? nearestEntry.entry : null;
		
	}
	
	/**
	 * Performs a search by looking at an entire polar region. This functionality replaces a "radial"
	 * search, which doesn't work near the poles. The index is a cylindrical projection; as such,
	 * drawing a circle at some radius around a pole, when projected onto a flat map, ends up looking
	 * like a rectangle that spans all the way from 180w to 180e. 
	 */
	private E findNearestPolarLocation(Coordinate location, double max) {
		
		Entry<E> nearestEntry = null;
		double nearestDistance = max;
		
		double lat = location.getLatitude();
		int north = (lat > 66.0) ? SIZE - 1 : hashLatitude(computeRelativeLatitude(location, max));
		int south = (lat < 66.0) ? 0 : hashLatitude(computeRelativeLatitude(location, -max));
		
		BoxSearch search = new BoxSearch(north, south, SIZE - 1, 0);
		while(search.next()) {
			for (Entry<E> match : get(search.getX(), search.getY())) {
				double distance = computeOrthodromicDistance(location, match.value);
				if (distance <= nearestDistance) {
					nearestEntry = match;
					nearestDistance = distance;
				}
			}
		}
		
		return (nearestEntry != null) ? nearestEntry.entry : null;
		
	}
	
	/**
	 * Finds all entries who's coordinates are within the given distance of the given coordinate.
	 */
	public Collection<E> findNear(Coordinate location, double distance) {

		int westIndex = hashLongitude(computeRelativeLongitude(location, -distance));
		int eastIndex = hashLongitude(computeRelativeLongitude(location, distance));
		int southIndex = hashLatitude(computeRelativeLatitude(location, -distance));
		int northIndex = hashLatitude(computeRelativeLatitude(location, distance));

		LinkedList<E> buffer = new LinkedList<E>();
		BoxSearch search = new BoxSearch(northIndex, southIndex, eastIndex, westIndex);
		while(search.next()) {
			for (Entry<E> match : get(search.getX(), search.getY())) {
				Coordinate value = match.value;
				if (computeOrthodromicDistance(location, value) <= distance) {
					buffer.add(match.entry);
				}
			}
		}
		
		return buffer;

	}

	/**
	 * Finds all entries who's coordinates are within the given bounding box.
	 */
	public Collection<E> findWithin(double north, double south, double east, double west) {

		int westIndex = hashLongitude(west);
		int eastIndex = hashLongitude(east);
		int southIndex = hashLatitude(south);
		int northIndex = hashLatitude(north);

		LinkedList<E> buffer = new LinkedList<E>();
		BoxSearch search = new BoxSearch(northIndex, southIndex, eastIndex, westIndex);
		while(search.next()) {
			for (Entry<E> match : get(search.getX(), search.getY())) {
				Coordinate value = match.value;
				if (!between(value.getLongitude(), east, west)) {
					// Not on the side of the planet that measures the shorter distance between east/west. 
				} else if (value.getLatitude() < south) {
					// South of bounding box; ignore.
				} else if (value.getLatitude() > north) {
					// North of bounding box; ignore.
				} else {
					buffer.add(match.entry);
				}
			}
		}
		
		return buffer;

	}

	/**
	 * Determines whether the given longitude is within the given east/west bounds.  
	 */
	private static boolean between(double longitude, double east, double west) {
		// If the boundaries are reversed, then they represent a bounding box
		// that overlaps the international date line. The calculation for inside
		// verse outside is essentially backwards.
		if(east - west < 0) {
			return longitude >= west || longitude <= east;
		} else {
			return longitude >= west && longitude <= east; 
		}
	}
	
	/**
	 * Retrieves the entries from the given bin, wrapping "x" indexes where needed.
	 */
	private Collection<Entry<E>> get(int x, int y) {
		// Adjust the horizontal index to wrap around if it is out of bounds.
		x = (x < 0) ? ((SIZE + (x % SIZE)) % SIZE) : x % SIZE;
		if (y < 0 || y >= SIZE) {
			return Collections.emptySet();
		} else if (index[x][y] == null) {
			return Collections.emptySet();
		} else {
			return index[x][y];
		}
	}

	/**
	 * Computes the bin index for the given latitude value.
	 */
	protected static int hashLatitude(double value) {
		if(value == 90.0) {
			return SIZE - 1;
		} else {
			return (int) ((value + 90.0) * SIZE / 180.0);
		}
	}

	/**
	 * Computes the bin index for the given longitude value.
	 */
	protected static int hashLongitude(double value) {
		if(value == 180.0) {
			return SIZE - 1;
		} else {
			return (int)((value + 180.0) * (SIZE / 360.0));
		}
	}

	/**
	 * Computes the longitude value that is the given distance east/west of the given coordinate.
	 */
	protected static double computeRelativeLongitude(Coordinate location, double distance) {
		
		GeodeticCalculator calc = new GeodeticCalculator();
		calc.setStartingGeographicPoint(location.getLongitude(), location.getLatitude());
		calc.setDirection(Math.signum(distance) * 90, Math.min(Math.abs(distance), HALF_WAY_AROUND_THE_WORLD));
		
		return calc.getDestinationGeographicPoint().getX();
	}

	/**
	 * Computes the latitude value that is the given distance north/south of the given coordinate.
	 */
	protected static double computeRelativeLatitude(Coordinate location, double distance) {
		
		GeodeticCalculator calc = new GeodeticCalculator();
		calc.setStartingGeographicPoint(location.getLongitude(), location.getLatitude());
		calc.setDirection((distance < 0) ? 180 : 0, Math.min(Math.abs(distance), HALF_WAY_AROUND_THE_WORLD));
		
		// If the resulting point is in the opposite hemisphere, then shift has wrapped around
		// one of the poles; truncate the relative shift be simply returning the pole itself.
		Point2D result = calc.getDestinationGeographicPoint();
		if(Integer.signum((int)result.getX()) != Integer.signum((int)location.getLongitude())) {
			return 90.0 * Math.signum(distance);
		} else {
			return result.getY();
		}
		
	}

	/**
	 * Computes the distance (in meters) between the given coordinates.
	 */
	protected static double computeOrthodromicDistance(Coordinate a, Coordinate b) {
		GeodeticCalculator calc = new GeodeticCalculator();
		calc.setStartingGeographicPoint(a.getLongitude(), a.getLatitude());
		calc.setDestinationGeographicPoint(b.getLongitude(), b.getLatitude());
		return calc.getOrthodromicDistance();
	}

	/**
	 * Computes the maximum number of bins "away" from the given coordinate one would need to search in order to cover
	 * the given distance(in meters)
	 */
	protected static int computeMaximumSearchRadius(Coordinate location, double max) {
		if (max > HALF_WAY_AROUND_THE_WORLD) {
			return SIZE / 2;
		} else {
			int north = hashLatitude(computeRelativeLatitude(location, max)) - hashLatitude(location.getLatitude());
			int south = hashLatitude(location.getLatitude()) - hashLatitude(computeRelativeLatitude(location, -max));
			int east = hashLongitude(computeRelativeLongitude(location, max)) - hashLongitude(location.getLongitude());
			int west = hashLongitude(location.getLongitude()) - hashLongitude(computeRelativeLongitude(location, -max));
			return Math.max(Math.max(north, south), Math.max(east, west));
		}
	}

	/**
	 * Inner class used to pair a coordinate with it's indexed entry.
	 */
	private static class Entry<T> {
		private Coordinate value;
		private T entry;
		public Entry(Coordinate value, T entry) {
			this.value = value;
			this.entry = entry;
		}
	}

	/**
	 * Inner class used to iterate over bins in a rectangular pattern.
	 */
	private static class BoxSearch {
		
		private int north;
		private int east;
		private int west;
		
		private int x;
		private int y;
		
		/**
		 * Constructor.
		 */
		public BoxSearch(int north, int south, int east, int west) {
			this.north = north;
			this.east = east;
			this.west = west;
			x = east;
			y = south - 1;
		}
		
		/**
		 * Move to the next bin in the pattern.
		 */
		public boolean next() {
			if(x == east) {
				x = west;
				y++;
			} else {
				x = (x + 1) % SIZE;
			}
			return y <= north;
		}
		
		/**
		 * Get the "x" axis bin index of the current position.
		 */
		public int getX() {
			return x;
		}

		/**
		 * Get the "y" axis bin index of the current position.
		 */
		public int getY() {
			return y;
		}

	}
	
	/**
	 * Inner class used to iterate over bins in circular pattern (actually a square). The search pattern includes only
	 * the bins at the given radius, not all those within it. Repeating the search with consecutively larger radius'
	 * allows you to prioritize closer matcher and stop searching early (i.e. a nearest mathc search).
	 */
	private static class RadialSearch {

		private final int x;
		private final int y;

		private int radius;
		private int verticalOffset;
		private int horizontalOffset;

		private int verticalIncrement = 0;
		private int horizontalIncrement = 0;

		private int count = -1;

		/**
		 * Constructor.
		 * 
		 * @param location
		 *            The location at the center of the search pattern.
		 * @param radius
		 *            The radius at which to search.
		 */
		public RadialSearch(Coordinate location, int radius) {
			x = hashLongitude(location.getLongitude());
			y = hashLatitude(location.getLatitude());
			this.radius = radius;
			this.verticalOffset = -radius;
			this.horizontalOffset = -radius;

		}

		/**
		 * Move to the next bin in the pattern.
		 */
		public boolean next() {
			verticalOffset += verticalIncrement;
			horizontalOffset += horizontalIncrement;
			count++;
			// A zero radius search is only one square, no need to increment.
			if (radius == 0) {
				verticalIncrement = 0;
				horizontalIncrement = 0;
			// Upper left corner; stop going north, start going east.
			} else if (verticalOffset == radius && horizontalOffset == -radius) {
				verticalIncrement = 0;
				horizontalIncrement = 1;
			// Upper right corner; stop going east, start going south.
			} else if (verticalOffset == radius && horizontalOffset == radius) {
				verticalIncrement = -1;
				horizontalIncrement = 0;
			// Lower right corner; stop going south, start going west.
			} else if (verticalOffset == -radius && horizontalOffset == radius) {
				verticalIncrement = 0;
				horizontalIncrement = -1;
			// Lower left corner; stop going west, start going north.
			} else if (verticalOffset == -radius && horizontalOffset == -radius) {
				verticalIncrement = 1;
				horizontalIncrement = 0;
			}
			return count < ((radius == 0) ? 1 : radius * 8);
		}

		/**
		 * Get the "x" axis bin index of the current position.
		 */
		public int getX() {
			return x + horizontalOffset;
		}

		/**
		 * Get the "y" axis bin index of the current position.
		 */
		public int getY() {
			return y + verticalOffset;
		}

	}

}
