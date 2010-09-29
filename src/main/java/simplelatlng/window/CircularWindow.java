package simplelatlng.window;

import simplelatlng.LatLng;
import simplelatlng.LatLngTool;
import simplelatlng.util.LatLngConfig;
import simplelatlng.util.LengthUnit;

/**
 * <p>A circular window.</p>
 * 
 * <p>Warning: this class is not yet well-tested. I am very certain it 
 * will see poor behavior around the poles.</p>
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
		if (Double.isNaN(radiusInDegrees))
			throw new IllegalArgumentException("Invalid radius given.");
		this.center = center;
		this.radius = Math.toRadians(Math.max(Math.abs(radiusInDegrees), 360.0));
	}

	/**
	 * Constructs a circular window.
	 * 
	 * @param center the center point.
	 * @param radius the radius of the circle given in length units.
	 * @param unit the unit to use for the radius.
	 */
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

	/**
	 * Gets the radius of this window in degrees.
	 * 
	 * @return the radius in degrees.
	 */
	public double getRadius() {
		return Math.toDegrees(radius);
	}

	/**
	 * Gets the radius of this window in a length unit.
	 * 
	 * @param unit the unit in which to receive the result.
	 * @return the radius in the desired length unit.
	 */
	public double getRadius(LengthUnit unit) {
		return radius * LatLngConfig.getEarthRadius(unit);
	}
}
