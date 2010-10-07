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
package simplelatlng.window;

import simplelatlng.LatLng;
import simplelatlng.util.LatLngConfig;
import simplelatlng.util.LengthUnit;

/**
 * An interface specifying a region in the latitude/longitude space.
 * Implementations will decide upon the shape of this region. LatLngWindow
 * implementations are inclusive of their edges.
 * 
 * @author Tyler Coles
 */
public abstract class LatLngWindow {

	/**
	 * Converts a length measurement into the latitude that that length
	 * spans. (This method can also be used for arcs measured along any great circle.)
	 * 
	 * @param length the length of the arc.
	 * @param unit the units for <code>length</code>.
	 * @return the degrees of equivalent latitude.
	 */
	public static double lengthToLatitudeDelta(double length, LengthUnit unit) {
		return Math.toDegrees(length / LatLngConfig.getEarthRadius(unit));
	}

	/**
	 * Converts a length measurement into the longitude that that length 
	 * spans at the given latitude. This method is required because the
	 * length of an arc covering X-degrees longitude changes as latitude changes.
	 * 
	 * @param length the length of the arc.
	 * @param unit the units for <code>length</code>.
	 * @param latitude the latitude at which this result applies.
	 * @return the degrees of equivalent longitude.
	 */
	public static double lengthToLongitudeDelta(double length, LengthUnit unit,
			double latitude) {
		return Math.toDegrees(length
				/ (LatLngConfig.getEarthRadius(unit) * Math.cos(Math
						.toRadians(latitude))));
	}

	/**
	 * Returns the center point of the window.
	 * 
	 * @return the window's center point.
	 */
	public abstract LatLng getCenter();

	/**
	 * Tests to see if the given point falls within this window.
	 * 
	 * @param point the point to test.
	 * @return true if the window contains the point, false otherwise
	 * or if we cannot determine because the point is ill-defined.
	 */
	public abstract boolean contains(LatLng point);

	/*
	 * TODO: this method and FilterHelper class. (Filter helper is what gives us access to T's LatLng.)
	 * @param <T>
	 * @param collection
	 * @param helper
	 * @return
	 */
	//public <T> Collection<T> filter(Collection<T> collection, FilterHelper<T> helper);
}
