package com.saic.rtws.commons.model.geo;

public class Coordinate {

	private double latitude;
	private double longitude;
	private double altitude;
	
	public Coordinate() {
		this(0, 0, 0);
	}
	
	public Coordinate(double latitude, double longitude) {
		this(latitude, longitude, 0);
	}
	
	public Coordinate(double latitude, double longitude, double altitude) {
		setLatitude(latitude);
		setLongitude(longitude);
		setAltitude(altitude);
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		if(Math.abs(latitude) > 90.0) {
			throw new IllegalArgumentException("Latitude '" + latitude + "' is outside the bound [+/-]90.");
		} else {
			this.latitude = latitude;
		}
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		longitude = longitude % 360;
		if(Math.abs(longitude) <= 180) {
			this.longitude = longitude;
		} else {
			this.longitude = longitude - Math.signum(longitude) * 360;
		}
	}

	public double getAltitude() {
		return altitude;
	}

	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(altitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		Coordinate other = (Coordinate) obj;
		if (Double.doubleToLongBits(altitude) != Double.doubleToLongBits(other.altitude)) return false;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude)) return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude)) return false;
		return true;
	}
	
}
