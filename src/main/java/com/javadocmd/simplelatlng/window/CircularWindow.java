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
package com.javadocmd.simplelatlng.window;

import java.math.BigDecimal;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LatLngConfig;
import com.javadocmd.simplelatlng.util.LengthUnit;

/**
 * <p>A circular window. Has the benefit of performing well around poles
 * and regardless of the size of the window. <code>contains()</code> checks
 * are slower with a CircularWindow than with a RectangularWindow, however.</p>
 * 
 * @author Tyler Coles
 */
public class CircularWindow extends LatLngWindow<CircularWindow> {

	private LatLng center;
	private BigDecimal radius;

	/**
	 * Constructs a circular window.
	 * 
	 * @param center the center point.
	 * @param radiusInDegrees the radius of the circle given in 
	 * degrees of angle required to create the circle. This angle is
	 * measured from an axis that joins the center of the Earth
	 * to our circle's center point on the surface.
	 */
	public CircularWindow(LatLng center, double radiusInDegrees) {
		this.setCenter(center);
		this.setRadius(radiusInDegrees);
	}

	/**
	 * Constructs a circular window that will contain all points
	 * within the specified radius.
	 * 
	 * @param center the center point.
	 * @param radius the radius of the circle given in length units.
	 * @param unit the unit to use for the radius.
	 */
	public CircularWindow(LatLng center, double radius, LengthUnit unit) {
		this.setCenter(center);
		this.setRadius(LatLngWindow.lengthToLatitudeDelta(radius, unit));
	}

	@Override
	public boolean contains(LatLng point) {
		return new BigDecimal(Math.toDegrees(LatLngTool.distanceInRadians(center,
				point)), LatLngConfig.DEGREE_CONTEXT).compareTo(radius) <= 0;
	}

	@Override
	public boolean overlaps(CircularWindow window) {
		BigDecimal angle = new BigDecimal(Math.toDegrees(LatLngTool
				.distanceInRadians(this.center, window.getCenter())),
				LatLngConfig.DEGREE_CONTEXT);
		BigDecimal combinedRadius = this.radius.add(window.radius);
		return angle.compareTo(combinedRadius) <= 0;
	}

	@Override
	public LatLng getCenter() {
		return center;
	}

	/**
	 * Sets the center of this window.
	 * 
	 * @param center the center point; may not be null.
	 */
	public void setCenter(LatLng center) {
		if (center == null)
			throw new IllegalArgumentException("Window's center may not be null.");
		this.center = center;
	}

	/**
	 * Gets the radius of this window in degrees.
	 * 
	 * @return the radius in degrees.
	 */
	public double getRadius() {
		return radius.doubleValue();
	}

	/**
	 * Gets the radius of this window in a length unit.
	 * 
	 * @param unit the unit in which to receive the result.
	 * @return the radius in the desired length unit.
	 */
	public double getRadius(LengthUnit unit) {
		return LatLngWindow.latitudeDeltaToLength(radius.doubleValue(), unit);
	}

	/**
	 * Sets the radius of this window.
	 * 
	 * @param radius radius in degrees.
	 */
	public void setRadius(double radius) {
		if (Double.isNaN(radius))
			throw new IllegalArgumentException("Invalid radius given.");
		this.radius = new BigDecimal(Math.min(Math.abs(radius), 360.0),
				LatLngConfig.DEGREE_CONTEXT);
	}

	@Override
	public String toString() {
		return String.format("center: %s; radius: %s degrees", center.toString(),
				radius.toString());
	}
}
