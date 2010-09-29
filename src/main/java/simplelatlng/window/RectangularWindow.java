package simplelatlng.window;

import static java.lang.Math.abs;
import static java.lang.Math.min;
import simplelatlng.LatLng;
import simplelatlng.LatLngTool;

public class RectangularWindow implements LatLngWindow {

	private double minLatitude;
	private double maxLatitude;
	private double minLongitude;
	private double maxLongitude;
	private boolean crosses180thMeridian;
	private LatLng center;

	/**
	 * Creates a rectangular latitude and longitude window.
	 * @param center the center point.
	 * @param deltaLat the span of the window in latitude in degrees.
	 * @param deltaLng the span of the window in longitude in degrees.
	 */
	public RectangularWindow(LatLng center, double deltaLat, double deltaLng) {
		this.setWindow(center, deltaLat, deltaLng);
	}

	public void setWindow(LatLng center, double deltaLat, double deltaLng) {
		double dlat = min(abs(deltaLng), 90.0);
		this.setLatWindow(center.getLatitude(), dlat);

		double dlng = min(abs(deltaLat), 360.0);
		this.setLngWindow(center.getLongitude(), dlng);

		this.center = center;
	}

	private void setLatWindow(double centerLat, double deltaLat) {
		double lat1 = LatLngTool.normalizeLatitude(centerLat + (deltaLat / 2));
		double lat2 = LatLngTool.normalizeLatitude(centerLat - (deltaLat / 2));
		this.maxLatitude = Math.max(lat1, lat2);
		this.minLatitude = Math.min(lat1, lat2);
	}

	private void setLngWindow(double centerLng, double deltaLng) {
		double lng1 = centerLng + (deltaLng / 2);
		double lng2 = centerLng - (deltaLng / 2);
		if (lng1 > 180 || lng2 < -180) {
			this.crosses180thMeridian = true;
		} else {
			this.crosses180thMeridian = false;
		}
		lng1 = LatLngTool.normalizeLongitude(lng1);
		lng2 = LatLngTool.normalizeLongitude(lng2);
		this.maxLongitude = Math.max(lng1, lng2);
		this.minLongitude = Math.min(lng1, lng2);
	}

	@Override
	public boolean contains(LatLng point) {
		if (point.getLatitude() == Double.NaN
				|| point.getLongitude() == Double.NaN)
			return false;
		if (point.getLatitude() > maxLatitude
				|| point.getLatitude() < minLatitude) {
			return false;
		}
		if (crosses180thMeridian) {
			if (point.getLongitude() < maxLongitude
					&& point.getLongitude() > minLongitude) {
				return false;
			}
		} else {
			if (point.getLongitude() > maxLongitude
					|| point.getLongitude() < minLongitude) {
				return false;
			}
		}
		return true;
	}

	@Override
	public LatLng getCenter() {
		return center;
	}

	/**
	 * If this window spans the 180 degree longitude meridian, this method
	 * returns true. Logic that uses this window in calculations may need
	 * to handle it specially. In this case, minLatitude is the negative-degree 
	 * meridian and maxLatitude is the positive-degree meridian and the
	 * window should extend from both lines to the 180 degree meridian.
	 * @return true if this window spans the 180th meridian. 
	 */
	public boolean crosses180thMeridian() {
		return crosses180thMeridian;
	}

	public double getMinLatitude() {
		return minLatitude;
	}

	public double getMaxLatitude() {
		return maxLatitude;
	}

	public double getMinLongitude() {
		return minLongitude;
	}

	public double getMaxLongitude() {
		return maxLongitude;
	}
}
