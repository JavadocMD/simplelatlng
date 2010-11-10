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

import static java.lang.Math.abs;
import static java.lang.Math.min;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LatLngConfig;
import com.javadocmd.simplelatlng.util.LengthUnit;


/**
 * <p>A "pseudo-rectangular" window bounded by a minimum and maximum latitude
 * and a minimum and maximum longitude. (The larger the window, the less rectangular 
 * this window actually is.) Naturally a window cannot span more than 180 
 * degrees latitude or 360 degrees longitude.</p>
 * 
 * <p>Note: the latitude span provided when creating this window is not a guarantee. 
 * If you create a latitude whose center is (90, 0) (the geographic North Pole) and 
 * whose latitude span is 10 degrees, the resulting window has a maximum latitude of 90 
 * and a minimum latitude of 85. Thus, windows are "squashed" if they hit the poles.</p>
 * 
 * @author Tyler Coles
 */
public class RectangularWindow extends LatLngWindow<RectangularWindow> {

	private long latitudeDelta;
	private long longitudeDelta;
	private long minLatitude;
	private long maxLatitude;
	private long minLongitude;
	private long maxLongitude;
	private boolean crosses180thMeridian;
	private LatLng center;

	/**
	 * Creates a pseudo-rectangular window.
	 * 
	 * @param center the center point.
	 * @param deltaLat the span of the window in latitude in degrees.
	 * @param deltaLng the span of the window in longitude in degrees.
	 */
	public RectangularWindow(LatLng center, double deltaLat, double deltaLng) {
		this.setWindow(center, deltaLat, deltaLng);
	}

	/**
	 * Creates a psuedo-rectangular window. The height will include the all latitudes
	 * within <code>height/2</code> North and South, while the width will include all
	 * longitudes within <code>width/2</code> East and West of the center point. This
	 * is an approximation that will work fine for small window away from the poles, but
	 * keep in mind that, for example in the northern hemisphere, the top of the rectangle
	 * is narrower than the bottom of the rectangle, with the middle of the rectangle's width
	 * being somewhere in between. 
	 * 
	 * @param center the center point of the window.
	 * @param width the approximate width of the window in <code>LenghtUnit</code>s. 
	 * @param height the height of the window in <code>LenghtUnit</code>s.
	 * @param unit the units for <code>width</code> and <code>height</code>.
	 */
	public RectangularWindow(LatLng center, double width, double height,
			LengthUnit unit) {
		double deltaLat = LatLngWindow.lengthToLatitudeDelta(height, unit);
		double deltaLng = LatLngWindow.lengthToLongitudeDelta(width, unit,
				deltaLat);
		this.setWindow(center, deltaLat, deltaLng);
	}

	/**
	 * Sets the bounds of this window.
	 * 
	 * @param center the center point.
	 * @param deltaLat the span of the window in latitude in degrees.
	 * @param deltaLng the span of the window in longitude in degrees.
	 */
	public void setWindow(LatLng center, double deltaLat, double deltaLng) {
		if (center == null)
			throw new IllegalArgumentException("Invalid center point.");
		if (Double.isNaN(deltaLat) || Double.isInfinite(deltaLat))
			throw new IllegalArgumentException("Invalid latitude delta.");
		if (Double.isNaN(deltaLng) || Double.isInfinite(deltaLng))
			throw new IllegalArgumentException("Invalid longitude delta.");

		double dlat = min(abs(deltaLat), 180.0);
		this.setLatWindow(center.getLatitude(), dlat);

		double dlng = min(abs(deltaLng), 360.0);
		this.setLngWindow(center.getLongitude(), dlng);

		this.center = center;
	}

	/**
	 * Fixes and sets the latitude parameters for the window.
	 */
	private void setLatWindow(double centerLat, double deltaLat) {
		double lat1 = LatLngTool.normalizeLatitude(centerLat + (deltaLat / 2.0));
		double lat2 = LatLngTool.normalizeLatitude(centerLat - (deltaLat / 2.0));
		this.maxLatitude = LatLngConfig.doubleToLong(Math.max(lat1, lat2));
		this.minLatitude = LatLngConfig.doubleToLong(Math.min(lat1, lat2));
		this.latitudeDelta = LatLngConfig.doubleToLong(deltaLat);
	}

	/**
	 * Fixes and sets the longitude parameters for the window.
	 */
	private void setLngWindow(double centerLng, double deltaLng) {
		double lng1 = centerLng + (deltaLng / 2.0);
		double lng2 = centerLng - (deltaLng / 2.0);
		if (lng1 > 180 || lng2 < -180) {
			this.crosses180thMeridian = true;
		} else {
			this.crosses180thMeridian = false;
		}
		lng1 = LatLngTool.normalizeLongitude(lng1);
		lng2 = LatLngTool.normalizeLongitude(lng2);
		this.maxLongitude = LatLngConfig.doubleToLong(Math.max(lng1, lng2));
		this.minLongitude = LatLngConfig.doubleToLong(Math.min(lng1, lng2));
		this.longitudeDelta = LatLngConfig.doubleToLong(deltaLng);
	}

	@Override
	public boolean contains(LatLng point) {

		if (point.getLatitudeInternal() > maxLatitude
				|| point.getLatitudeInternal() < minLatitude) {
			return false;
		}

		if (crosses180thMeridian) {
			if (point.getLongitudeInternal() < maxLongitude
					&& point.getLongitudeInternal() > minLongitude) {
				return false;
			}
		} else {
			if (point.getLongitudeInternal() > maxLongitude
					|| point.getLongitudeInternal() < minLongitude) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean overlaps(RectangularWindow window) {

		if (window.getMaxLatitude() < minLatitude
				|| window.getMinLatitude() > maxLatitude) {
			return false;
		}
		if (crosses180thMeridian) {
			if (window.maxLongitude > minLongitude
					|| window.minLongitude < maxLongitude) {
				return false;
			}
		} else {
			if (window.maxLongitude < minLongitude
					|| window.minLongitude > maxLongitude) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the height of the window.
	 * 
	 * @param unit the length units to return.
	 * @return the height of the window in the desired units.
	 */
	public double getHeight(LengthUnit unit) {
		return LatLngWindow.latitudeDeltaToLength(LatLngConfig
				.longToDouble(latitudeDelta), unit);
	}

	/**
	 * Returns the width at the mid-line of the window.
	 * 
	 * @param unit the length units to return.
	 * @return the width of the window's mid-line in the desired units
	 */
	public double getWidth(LengthUnit unit) {
		return LatLngWindow.longitudeDeltaToLength(LatLngConfig
				.longToDouble(longitudeDelta), unit, center.getLatitude());
	}

	@Override
	public LatLng getCenter() {
		return center;
	}

	/**
	 * If this window spans the 180 degree longitude meridian, this method
	 * returns true. Logic that uses this window in calculations may need
	 * to handle it specially. In this case, minLatitude is the negative-degree 
	 * meridian and maxLatitude is the positive-degree meridian and the
	 * window should extend from both lines to the 180 degree meridian.
	 * So instead of testing whether a point lies between the min/max-longitude,
	 * you would have to test if a point lay outside the min/max-longitude.
	 * 
	 * @return true if this window spans the 180th meridian. 
	 */
	public boolean crosses180thMeridian() {
		return crosses180thMeridian;
	}

	public double getLatitudeDelta() {
		return LatLngConfig.longToDouble(latitudeDelta);
	}

	public double getLongitudeDelta() {
		return LatLngConfig.longToDouble(longitudeDelta);
	}

	public double getMinLatitude() {
		return LatLngConfig.longToDouble(minLatitude);
	}

	public double getMaxLatitude() {
		return LatLngConfig.longToDouble(maxLatitude);
	}

	public double getMinLongitude() {
		return LatLngConfig.longToDouble(minLongitude);
	}

	public double getMaxLongitude() {
		return LatLngConfig.longToDouble(maxLongitude);
	}
}
