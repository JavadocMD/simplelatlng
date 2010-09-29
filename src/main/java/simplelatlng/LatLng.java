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
	 * Tests whether two degree measurements fall within the tolerance
	 * allowed in {@link simplelatlng.util.LatLngConfig}. Ignores
	 * NaN and infinite values, returning false in either case.
	 * 
	 * @param degree1 one degree measurement.
	 * @param degree2 another degree measurement.
	 * @return true if they should be considered equal, false otherwise.
	 */
	public static boolean degreesEqual(double degree1, double degree2) {
		if (Double.isNaN(degree1) || Double.isNaN(degree2))
			return false;
		if (Double.isInfinite(degree1) || Double.isInfinite(degree2))
			return false;
		return Math.abs(degree2 - degree1) <= LatLngConfig.DEGREE_TOLERANCE;
	}
}
