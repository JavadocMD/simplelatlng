/*
 *    Copyright 2010 Tyler Coles
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.javadocmd.simplelatlng;

import java.io.Serializable;
import java.util.Random;

import com.javadocmd.simplelatlng.util.LatLngConfig;

/**
 * <p>A representation of a single point in latitude and longitude.
 * All data is handled in degrees and will be normalized if possible 
 * to the +/- 90 latitude, +/- 180 longitude region.</p>
 * 
 * <p>Note that attempting to create a LatLng with invalid values 
 * (NaN, negative infinity, positive infinity) will throw
 * IllegalArgumentExceptions.</p>
 * 
 * @author Tyler Coles
 */
public class LatLng implements Serializable {

	private static final long serialVersionUID = 7086953744720769418L;

	/**
	 * Creates a random latitude and longitude. (Not inclusive of (-90, 0))
	 */
	public static LatLng random() {
		return random(new Random());
	}

	/**
	 * Creates a random latitude and longitude. (Not inclusive of (-90, 0))
	 * 
	 * @param r the random number generator to use, if you want to be 
	 * specific or are creating many LatLngs at once.
	 */
	public static LatLng random(Random r) {
		return new LatLng((r.nextDouble() * -180.0) + 90.0,
				(r.nextDouble() * -360.0) + 180.0);
	}

	/**
	 * Tests whether two angles fall within the tolerance
	 * allowed in {@link com.javadocmd.simplelatlng.util.LatLngConfig}. Ignores
	 * NaN and infinite values, returning false in either case.
	 * 
	 * @param degree1 one degree angle.
	 * @param degree2 another degree angle.
	 * @return true if they should be considered equal, false otherwise.
	 */
	public static boolean degreesEqual(double degree1, double degree2) {
		if (Double.isNaN(degree1) || Double.isNaN(degree2))
			return false;
		if (Double.isInfinite(degree1) || Double.isInfinite(degree2))
			return false;
		return LatLngConfig.doubleToLong(degree1) == LatLngConfig
				.doubleToLong(degree2);
	}

	private long latitude;
	private long longitude;

	/**
	 * Creates a LatLng point.
	 * 
	 * @param latitude the latitude in degrees.
	 * @param longitude the longitude in degrees.
	 */
	public LatLng(double latitude, double longitude) {
		this.setLatitudeLongitude(latitude, longitude);
	}

	/**
	 * Get latitude for this point in degrees.
	 * 
	 * @return latitude in degrees.
	 */
	public double getLatitude() {
		return LatLngConfig.longToDouble(latitude);
	}

	/**
	 * Get the internal long representation of this point's latitude
	 * in degrees. Intended for library use only.
	 * 
	 * @return the internal representation of latitude in degrees.
	 */
	public long getLatitudeInternal() {
		return latitude;
	}

	/**
	 * Get longitude for this point in degrees.
	 * 
	 * @return longitude in degrees.
	 */
	public double getLongitude() {
		return LatLngConfig.longToDouble(longitude);
	}

	/**
	 * Get the internal long representation of this point's longitude
	 * in degrees. Intended for library use only.
	 * 
	 * @return the internal representation of longitude in degrees.
	 */
	public long getLongitudeInternal() {
		return longitude;
	}

	/**
	 * Sets the latitude and longitude for this point.
	 * 
	 * @param latitude the latitude in degrees.
	 * @param longitude the longitude in degrees.
	 */
	public void setLatitudeLongitude(double latitude, double longitude) {
		this.setLatitude(latitude);
		if (Math.abs(this.latitude) == 90000000L) {
			// At the poles all longitudes intersect. Simplify for later comparison.
			this.setLongitude(0);
		} else {
			this.setLongitude(longitude);
		}
	}

	/**
	 * Sets the latitude for this point.
	 */
	private void setLatitude(double latitude) {
		double lat = LatLngTool.normalizeLatitude(latitude);
		if (Double.isNaN(lat))
			throw new IllegalArgumentException("Invalid latitude given.");
		this.latitude = LatLngConfig.doubleToLong(lat);
	}

	/**
	 * Sets the longitude for this point.
	 */
	private void setLongitude(double longitude) {
		double lng = LatLngTool.normalizeLongitude(longitude);
		if (Double.isNaN(lng))
			throw new IllegalArgumentException("Invalid longitude given.");
		this.longitude = LatLngConfig.doubleToLong(lng);
	}

	/**
	 * Returns true if this LatLng represents a polar coordinate (+/- 90 degrees latitude).
	 */
	public boolean isPolar() {
		return this.latitude == 90000000L || this.latitude == -90000000L;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof LatLng))
			return false;

		LatLng latlng = (LatLng) obj;
		if (this.latitude != latlng.latitude) {
			return false;
		}

		return this.longitude == latlng.longitude;
	}

	@Override
	public int hashCode() {
		String s = Long.toString(latitude) + "|" + Long.toString(longitude);
		return s.hashCode();
	}

	@Override
	public String toString() {
		return String.format("(%s,%s)",
				LatLngConfig.getDegreeFormat().format(LatLngConfig.longToDouble(this.latitude)),
				LatLngConfig.getDegreeFormat().format(LatLngConfig.longToDouble(this.longitude)));
	}
}
