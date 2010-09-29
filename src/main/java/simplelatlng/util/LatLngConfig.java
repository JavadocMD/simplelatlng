package simplelatlng.util;


/**
 * Configuration parameters for latitude and longitude calculations.
 * 
 * @author Tyler Coles
 */
public class LatLngConfig {

	private static double[] EARTH_RADIUS;

	static {
		setEarthRadius(6371.009, LengthUnit.KILOMETER);
	}

	public static double getEarthRadius(LengthUnit unit) {
		return EARTH_RADIUS[unit.ordinal()];
	}

	synchronized public static void setEarthRadius(double radius, LengthUnit unit) {
		EARTH_RADIUS = new double[LengthUnit.values().length];
		for (LengthUnit toUnit : LengthUnit.values()) {
			EARTH_RADIUS[toUnit.ordinal()] = unit.convertTo(toUnit, radius);
		}
	}
}
