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

import java.util.Collection;
import java.util.Iterator;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LatLngConfig;
import com.javadocmd.simplelatlng.util.LengthUnit;


/**
 * An interface specifying a region in the latitude/longitude space.
 * Implementations will decide upon the shape of this region. LatLngWindow
 * implementations are inclusive of their edges.
 * 
 * @author Tyler Coles
 */
public abstract class LatLngWindow<T extends LatLngWindow<T>> {

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
	 * Converts a latitude degree arc to the length of that arc.
	 * This is the opposite operation to {@link #lengthToLatitudeDelta(double, LengthUnit)}
	 * 
	 * @param deltaLat the latitude arc degrees.
	 * @param unit the desired length units.
	 * @return the arc length.
	 */
	public static double latitudeDeltaToLength(double deltaLat, LengthUnit unit) {
		return LatLngConfig.getEarthRadius(unit) * Math.toRadians(deltaLat);
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
	 * Converts a longitude degree arc at a specific latitude to the length
	 * of the arc. This is the opposite operation to {@link #lengthToLongitudeDelta(double, LengthUnit, double)}
	 * 
	 * @param deltaLng the longitude arc degrees.
	 * @param unit the desired length units.
	 * @param latitude the latitude at which this arc lies.
	 * @return the arc length.
	 */
	public static double longitudeDeltaToLength(double deltaLng,
			LengthUnit unit, double latitude) {
		return LatLngConfig.getEarthRadius(unit) * Math.toRadians(deltaLng)
				* Math.cos(Math.toRadians(latitude));
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

	/**
	 * Test if this window overlaps the given window.
	 * 
	 * @param window the window to test against this one.
	 * @return true if the windows overlap.
	 */
	public abstract boolean overlaps(T window);

	/**
	 * Goes through the given collection removing items whose LatLng
	 * point does not fall within this window. The LatLng point used
	 * in this collection traversal is accessed via the FilterHelper
	 * instance passed in.
	 * 
	 * @param <E> the type of elements in the collection.
	 * @param collection the collection of elements.
	 * @param helper the instance of FilterHelper that gives this 
	 * method access to E's LatLng value that we will test against
	 * this window.
	 */
	public <E> void filter(Collection<E> collection, FilterHelper<E> helper) {
		for (Iterator<E> i = collection.iterator(); i.hasNext();) {
			E object = i.next();
			if (!this.contains(helper.getLatLng(object))) {
				i.remove();
			}
		}
	}

	/**
	 * <p>Goes through the source collection copying items whose LatLng
	 * point falls within this window to the destination collection.
	 * The LatLng point used in this collection traversal is accessed via 
	 * the FilterHelper instance passed in.</p>
	 * <p>This should save you from copying an entire collection and then 
	 * filtering it when you want to leave the first collection intact.
	 * This is a shallow copy, meaning <strong>changes made to objects within 
	 * the copied collection will be reflected in the original</strong>, 
	 * so beware.</p>
	 * 
	 * @param <E> the type of elements in the collection.
	 * @param source the source collection of elements.
	 * @param destination the destination collection; after this method 
	 * runs, destination contains all items that fit within this window.
	 * @param helper the instance of FilterHelper that gives this 
	 * method access to E's LatLng value that we will test against
	 * this window.
	 */
	public <E> void filterCopy(Collection<E> source, Collection<E> destination,
			FilterHelper<E> helper) {
		for (Iterator<E> i = source.iterator(); i.hasNext();) {
			E object = i.next();
			if (this.contains(helper.getLatLng(object))) {
				destination.add(object);
			}
		}
	}
}
