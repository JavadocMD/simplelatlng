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

import com.javadocmd.simplelatlng.util.LatLngConfig;
import com.javadocmd.simplelatlng.util.LengthUnit;

/**
 * <p>Primary calculations and tools.</p> 
 * 
 * <p>Note: distance calculations are done using the Haversine formula
 * which uses a spherical approximation of the Earth. Values are known
 * to differ from reality by as much as 0.3% so if complete accuracy is very 
 * important to you, you should be using a different library. Furthermore, 
 * by default this library uses the mean radius of the Earth (6371.009 km). 
 * If your calculations are localized to a particular region of the Earth, 
 * there may be values to use for this radius which will yield more accurate 
 * results. To set the radius used by this library, see {@link com.javadocmd.simplelatlng.util.LatLngConfig}.</p> 
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
	 * <p>This "distance" function is mostly for internal use. Most users will simply
	 * rely upon {@link #distance(LatLng, LatLng, LengthUnit)}</p>
	 * 
	 * <p>Yields the internal angle for an arc between two points on the surface of a sphere
	 * in radians. This angle is in the plane of the great circle connecting the two points
	 * measured from an axis through one of the points and the center of the Earth.
	 * Multiply this value by the sphere's radius to get the length of the arc.</p>
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
	 * <p>Calculate the initial bearing in degrees on a great-circle course from one point to another.</p>
	 * 
	 * <p>Remember, you can calculate the final bearing by swapping the start/end points and reversing the 
	 * resulting "initial" bearing by adding or subtracting 180 degrees.</p>
	 * 
	 * @param start the starting point.
	 * @param end the ending point.
	 * @return the initial bearing in degrees, normalized to the 0 to +360 range.
	 */
	public static double initialBearing(LatLng start, LatLng end) {
		return normalizeBearing(Math.toDegrees(initialBearingInRadians(start, end)));
	}

	/**
	 * <p>Calculate the initial bearing in radians on a great-circle course from one point to another.</p>
	 * 
	 * <p>Remember, you can calculate the final bearing by swapping the start/end points and reversing the 
	 * resulting "initial" bearing by adding or subtracting 180 degrees.</p>
	 * 
	 * @param start the starting point.
	 * @param end the ending point.
	 * @return the initial bearing in radians.
	 */
	public static double initialBearingInRadians(LatLng start, LatLng end) {
		double lat1R = Math.toRadians(start.getLatitude());
		double lat2R = Math.toRadians(end.getLatitude());
		double dLngR = Math.toRadians(end.getLongitude()
				- start.getLongitude());
		double a = Math.sin(dLngR) * Math.cos(lat2R);
		double b = Math.cos(lat1R) * Math.sin(lat2R) - Math.sin(lat1R)
				* Math.cos(lat2R) * Math.cos(dLngR);
		return Math.atan2(a, b);
	}
	
	/**
	 * Clamp latitude to +/- 90 degrees.
	 * 
	 * @param latitude in degrees.
	 * @return the normalized latitude. Returns NaN if 
	 * the input is NaN.
	 */
	public static double normalizeLatitude(double latitude) {
		if (Double.isNaN(latitude))
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
		if (Double.isNaN(longitude) || Double.isInfinite(longitude))
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
	
	/**
	 * Convert a bearing to be within the 0 to +360 degrees range.
	 * 
	 * @param bearing the bearing in degrees.
	 * @return the normalized breaing. Returns NaN if the input
	 * is NaN, positive infinity, or negative infinity.
	 */
	public static double normalizeBearing(double bearing) {
		if (Double.isNaN(bearing) || Double.isInfinite(bearing))
			return Double.NaN;
		double bearingResult = bearing % 360;
		if (bearingResult < 0)
			bearingResult += 360;
		return bearingResult;
	}

	private LatLngTool() {

	}
}
