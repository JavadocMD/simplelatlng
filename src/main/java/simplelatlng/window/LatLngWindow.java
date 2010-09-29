package simplelatlng.window;

import simplelatlng.LatLng;

/**
 * An interface specifying a region in the latitude/longitude space.
 * Implementations will decide upon the shape of this region.
 * 
 * @author Tyler Coles
 */
public interface LatLngWindow {

	/**
	 * Returns the center point of the window.
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
