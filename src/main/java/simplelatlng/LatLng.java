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
package simplelatlng;

import simplelatlng.util.LatLngConfig;

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
public class LatLng {

	private double latitude;
	private double longitude;

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
		return latitude;
	}

	/**
	 * Get longitude for this point in degrees.
	 * 
	 * @return longitude in degrees.
	 */
	public double getLongitude() {
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
		if (degreesEqual(Math.abs(this.latitude), 90)) {
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
		this.latitude = lat;
	}

	/**
	 * Sets the longitude for this point.
	 */
	private void setLongitude(double longitude) {
		double lng = LatLngTool.normalizeLongitude(longitude);
		if (Double.isNaN(lng))
			throw new IllegalArgumentException("Invalid longitude given.");
		this.longitude = lng;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		if (!(obj instanceof LatLng))
			return false;

		LatLng latlng = (LatLng) obj;
		if (!degreesEqual(this.latitude, latlng.latitude)) {
			return false;
		}

		if (degreesEqual(this.latitude, 90)) {
			// Latitudes are both 90, longitude doesn't matter.
			return true;
		}

		return degreesEqual(this.longitude, latlng.longitude);
	}

	/**
	 * Tests whether two angles fall within the tolerance
	 * allowed in {@link simplelatlng.util.LatLngConfig}. Ignores
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
		return Math.abs(degree2 - degree1) <= LatLngConfig.DEGREE_TOLERANCE;
	}

	/**
	 * Tests whether two angles fall within the tolerance
	 * allowed in {@link simplelatlng.util.LatLngConfig}. Ignores
	 * NaN and infinite values, returning false in either case.
	 * 
	 * @param radian1 one radian angle.
	 * @param radian2 another radian angle.
	 * @return true if they should be considered equal, false otherwise.
	 */
	public static boolean radiansEqual(double radians1, double radians2) {
		if (Double.isNaN(radians1) || Double.isNaN(radians2))
			return false;
		if (Double.isInfinite(radians1) || Double.isInfinite(radians2))
			return false;
		return Math.abs(radians2 - radians1) <= LatLngConfig.RADIAN_TOLERANCE;
	}
}
