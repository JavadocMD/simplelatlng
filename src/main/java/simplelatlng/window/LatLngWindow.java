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

/**
 * An interface specifying a region in the latitude/longitude space.
 * Implementations will decide upon the shape of this region. LatLngWindow
 * implementations are inclusive of their edges.
 * 
 * @author Tyler Coles
 */
public interface LatLngWindow {

	/**
	 * Returns the center point of the window.
	 * 
	 * @return the window's center point.
	 */
	public LatLng getCenter();

	/**
	 * Tests to see if the given point falls within this window.
	 * 
	 * @param point the point to test.
	 * @return true if the window contains the point, false otherwise
	 * or if we cannot determine because the point is ill-defined.
	 */
	public boolean contains(LatLng point);
}
