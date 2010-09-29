package simplelatlng.window;

import simplelatlng.LatLng;
import simplelatlng.LatLngTool;
import simplelatlng.util.LengthUnit;

/**
 * A circular window.
 * 
 * @author Tyler Coles
 */
public class CircularWindow implements LatLngWindow {

	private LatLng center;
	private double radius; // Stored in radians.

	/**
	 * Constructs a circular window.
	 * 
	 * @param center the center point.
	 * @param radiusInDegrees the radius of the circle given in 
	 * latitude/longitude degrees. Since we are using a spherical
	 * approximation of the Earth, one degree of latitude spans
	 * the same surface distance as one degree of longitude.
	 */
	public CircularWindow(LatLng center, double radiusInDegrees) {
		this.center = center;
		this.radius = Math.toRadians(radiusInDegrees);
	}
	
	public CircularWindow(LatLng center, double radius, LengthUnit unit) {
		
	}

	@Override
	public boolean contains(LatLng point) {
		return LatLngTool.distanceInRadians(center, point) <= radius;
	}

	@Override
	public LatLng getCenter() {
		return center;
	}

}
