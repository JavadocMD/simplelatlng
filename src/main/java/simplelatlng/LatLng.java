package simplelatlng;

/**
 * A representation of a single point in latitude and longitude.
 * All data is handled in degrees and will be normalized if possible 
 * to the +/- 90 latitude, +/- 180 longitude region.
 * 
 * @author Tyler Coles
 */
public class LatLng {

	private double latitude;
	private double longitude;

	public LatLng(double latitude, double longitude) {
		this.setLatitude(latitude);
		this.setLongitude(longitude);
	}

	public LatLng() {

	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = LatLngTool.normalizeLatitude(latitude);
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = LatLngTool.normalizeLongitude(longitude);
	}
}
