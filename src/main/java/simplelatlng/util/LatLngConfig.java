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
package simplelatlng.util;

/**
 * Configuration parameters for latitude and longitude calculations.
 * 
 * @author Tyler Coles
 */
public class LatLngConfig {

	/**
	 * The tolerance (in degrees) by which two angles can differ and still be considered the same.
	 * A tolerance of 1e-6 yields a precision of nearly 1 centimeter, which is far more accurate
	 * than any of the distance calculations can claim to be.
	 */
	public static final double DEGREE_TOLERANCE = 0.000001;
	/**
	 * The tolerance (in radians) by which two angles can differ and still be considered the same.
	 * Equivalent to DEGREE_TOLERANCE.
	 */
	public static final double RADIAN_TOLERANCE = Math
			.toRadians(DEGREE_TOLERANCE);

	/**
	 * The Earth's mean radius in kilometers. Used as the default radius 
	 * for calculations.
	 */
	public static final double EARTH_MEAN_RADIUS_KILOMETERS = 6371.009;

	/**
	 * Earth's radius stored in all of the support unit types.
	 * This is pre-calculated to eliminate unit conversions
	 * when doing many distance calculations.
	 */
	private static double[] EARTH_RADIUS;

	static {
		// Initialize earth radius using the mean radius.
		setEarthRadius(EARTH_MEAN_RADIUS_KILOMETERS, LengthUnit.KILOMETER);
	}

	/**
	 * Retrieve the Earth's spherical approximation radius in the desired unit.
	 * 
	 * @param unit the desired unit for the result.
	 * @return the Earth's radius in the desired unit.
	 */
	public static double getEarthRadius(LengthUnit unit) {
		return EARTH_RADIUS[unit.ordinal()];
	}

	/**
	 * Sets the Earth's radius for the purposes of all future 
	 * calculations in this library. If there is a radius that 
	 * is more accurate for the locations you most care about,
	 * you can configure that here.
	 * 
	 * @param radius the Earth's spherical approximation radius.
	 * @param unit the unit the radius is given in.
	 */
	synchronized public static void setEarthRadius(double radius, LengthUnit unit) {
		EARTH_RADIUS = new double[LengthUnit.values().length];
		for (LengthUnit toUnit : LengthUnit.values()) {
			EARTH_RADIUS[toUnit.ordinal()] = unit.convertTo(toUnit, radius);
		}
	}

	private LatLngConfig() {
	}
}
