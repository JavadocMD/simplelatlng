package simplelatlng;

import simplelatlng.util.LatLngConfig;
import simplelatlng.util.LengthUnit;

/**
 * Primary calculations and tools.
 * 
 * @author Tyler Coles
 */
public class LatLngTool {

	/**
	 * Distance between two points.
	 * 
	 * @param point1 the first point.
	 * @param point2 the second point.
	 * @param unit the unit of measure in which to receive the result.
	 * @return the distance in the chosen unit of measure.
	 */
	public static double distance(LatLng point1, LatLng point2, LengthUnit unit) {
		return LatLngTool.distanceInRadians(point1, point2)
				* LatLngConfig.getEarthRadius(unit);
	}

	/**
	 * The internal angle for an arc between two points on the surface of a sphere
	 * in radians. Multiply this value by the sphere's radius to get the length of 
	 * the arc.
	 * 
	 * @return the internal angle for the arc connecting the two points in radians.
	 */
	public static double distanceInRadians(LatLng point1, LatLng point2) {
		double lat1R = Math.toRadians(point1.getLatitude());
		double lat2R = Math.toRadians(point2.getLatitude());
		double dLatR = Math.abs(lat2R - lat1R);
		double dLngR = Math.abs(Math.toRadians(point2.getLongitude()
				- point1.getLongitude()));
		double a = Math.sin(dLatR / 2) * Math.sin(dLatR / 2) + Math.cos(lat1R)
				* Math.cos(lat2R) * Math.sin(dLngR / 2) * Math.sin(dLngR / 2);
		return 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	}

	/**
	 * Clamp latitude to +/- 90 degrees.
	 * 
	 * @param latitude in degrees.
	 * @return the normalized latitude. Returns NaN if 
	 * the input is NaN.
	 */
	public static double normalizeLatitude(double latitude) {
		if (latitude == Double.NaN)
			return Double.NaN;
		if (latitude > 0) {
			return Math.min(latitude, 90.0);
		} else {
			return Math.max(latitude, -90.0);
		}
	}

	/**
	 * Convert longitude to be within the +/- 180 degrees range.
	 * 
	 * @param longitude in degrees.
	 * @return the normalized longitude. Returns NaN if the input
	 * is NaN, positive infinity, or negative infinity.
	 */
	public static double normalizeLongitude(double longitude) {
		if (longitude == Double.NaN || longitude == Double.POSITIVE_INFINITY
				|| longitude == Double.NEGATIVE_INFINITY)
			return Double.NaN;
		double longitudeResult = longitude % 360;
		if (longitudeResult > 180) {
			double diff = longitudeResult - 180;
			longitudeResult = -180 + diff;
		} else if (longitudeResult < -180) {
			double diff = longitudeResult + 180;
			longitudeResult = 180 + diff;
		}
		return longitudeResult;
	}

}
