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
import java.math.BigDecimal;
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

	public static final BigDecimal DEGREE_90 = new BigDecimal(90,
			LatLngConfig.DEGREE_CONTEXT);
	public static final BigDecimal DEGREE_180 = new BigDecimal(180,
			LatLngConfig.DEGREE_CONTEXT);
	public static final BigDecimal DEGREE_360 = new BigDecimal(360,
			LatLngConfig.DEGREE_CONTEXT);

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

	private BigDecimal latitude;
	private BigDecimal longitude;

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
		return latitude.doubleValue();
	}

	/**
	 * Get latitude for this point in degrees
	 * as a BigDecimal.
	 * 
	 * @return latitude in degrees;
	 */
	public BigDecimal getLatitudeBig() {
		return latitude;
	}

	/**
	 * Get longitude for this point in degrees.
	 * 
	 * @return longitude in degrees.
	 */
	public double getLongitude() {
		return longitude.doubleValue();
	}

	/**
	 * Get longitude for this point in degrees
	 * as a BigDecimal.
	 * 
	 * @return longitude in degrees.
	 */
	public BigDecimal getLongitudeBig() {
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
		if (this.latitude.abs().equals(DEGREE_90)) {
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
		this.latitude = new BigDecimal(lat);
	}

	/**
	 * Sets the longitude for this point.
	 */
	private void setLongitude(double longitude) {
		double lng = LatLngTool.normalizeLongitude(longitude);
		if (Double.isNaN(lng))
			throw new IllegalArgumentException("Invalid longitude given.");
		this.longitude = new BigDecimal(lng);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof LatLng))
			return false;

		LatLng latlng = (LatLng) obj;
		return this.latitude.equals(latlng.latitude)
				&& this.longitude.equals(latlng.longitude);

	}

	@Override
	public int hashCode() {
		String s = latitude.toString() + "|" + longitude.toString();
		return s.hashCode();
	}

	@Override
	public String toString() {
		return String.format("(%s,%s)", this.latitude.toString(),
				this.longitude.toString());
	}
}
