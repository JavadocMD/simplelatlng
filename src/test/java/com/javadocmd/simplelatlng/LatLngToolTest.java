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

import static com.javadocmd.simplelatlng.LatLngTool.distance;
import static com.javadocmd.simplelatlng.LatLngTool.distanceInRadians;
import static com.javadocmd.simplelatlng.LatLngTool.initialBearing;
import static com.javadocmd.simplelatlng.LatLngTool.normalizeBearing;
import static com.javadocmd.simplelatlng.LatLngTool.normalizeLatitude;
import static com.javadocmd.simplelatlng.LatLngTool.normalizeLongitude;
import static com.javadocmd.simplelatlng.LatLngTool.travel;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LatLngConfig;
import com.javadocmd.simplelatlng.util.LengthUnit;


public class LatLngToolTest {

	@Test
	public void testNormalizeLatitude() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		assertEquals(0, normalizeLatitude(0), t);
		assertEquals(-0.0, normalizeLatitude(-0.0), t);
		assertEquals(5.3, normalizeLatitude(5.3), t);
		assertEquals(-5.3, normalizeLatitude(-5.3), t);
		assertEquals(35.7838, normalizeLatitude(35.7838), t);
		assertEquals(-35.7838, normalizeLatitude(-35.7838), t);
		assertEquals(90, normalizeLatitude(90), t);
		assertEquals(-90, normalizeLatitude(-90), t);
		assertEquals(90, normalizeLatitude(105), t);
		assertEquals(-90, normalizeLatitude(-105), t);
		assertEquals(90, normalizeLatitude(5738), t);
		assertEquals(-90, normalizeLatitude(-5738), t);
		assertEquals(90, normalizeLatitude(Double.POSITIVE_INFINITY), t);
		assertEquals(-90, normalizeLatitude(Double.NEGATIVE_INFINITY), t);
		assertEquals(Double.NaN, normalizeLatitude(Double.NaN), t);
	}

	@Test
	public void testNormalizeLongitude() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		assertEquals(0, normalizeLongitude(0), t);
		assertEquals(-0.0, normalizeLongitude(-0.0), t);
		assertEquals(5.3, normalizeLongitude(5.3), t);
		assertEquals(-5.3, normalizeLongitude(-5.3), t);
		assertEquals(35.7838, normalizeLongitude(35.7838), t);
		assertEquals(-35.7838, normalizeLongitude(-35.7838), t);
		assertEquals(90, normalizeLongitude(90), t);
		assertEquals(-90, normalizeLongitude(-90), t);
		assertEquals(91.384, normalizeLongitude(91.384), t);
		assertEquals(-91.384, normalizeLongitude(-91.384), t);
		assertEquals(171.384, normalizeLongitude(171.384), t);
		assertEquals(-171.384, normalizeLongitude(-171.384), t);
		assertEquals(180, normalizeLongitude(180), t);
		assertEquals(-180, normalizeLongitude(-180), t);
		assertEquals(-170, normalizeLongitude(190), t);
		assertEquals(170, normalizeLongitude(-190), t);
		assertEquals(-146.516091, normalizeLongitude(213.483909), t);
		assertEquals(146.516091, normalizeLongitude(-213.483909), t);
		assertEquals(-170, normalizeLongitude(1990), t);
		assertEquals(170, normalizeLongitude(-1990), t);
		assertEquals(Double.NaN, normalizeLongitude(Double.POSITIVE_INFINITY), t);
		assertEquals(Double.NaN, normalizeLongitude(Double.NEGATIVE_INFINITY), t);
		assertEquals(Double.NaN, normalizeLongitude(Double.NaN), t);
	}
	
	@Test
	public void testNormalizeBearing() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		assertEquals(0, normalizeBearing(0), t);
		assertEquals(-0.0, normalizeBearing(-0.0), t);
		assertEquals(5.3, normalizeBearing(5.3), t);
		assertEquals(354.7, normalizeBearing(-5.3), t);
		assertEquals(35.7838, normalizeBearing(35.7838), t);
		assertEquals(324.2162, normalizeBearing(-35.7838), t);
		assertEquals(90, normalizeBearing(90), t);
		assertEquals(270, normalizeBearing(-90), t);
		assertEquals(91.384, normalizeBearing(91.384), t);
		assertEquals(268.616, normalizeBearing(-91.384), t);
		assertEquals(171.384, normalizeBearing(171.384), t);
		assertEquals(188.616, normalizeBearing(-171.384), t);
		assertEquals(180, normalizeBearing(180), t);
		assertEquals(180, normalizeBearing(-180), t);
		assertEquals(190, normalizeBearing(190), t);
		assertEquals(170, normalizeBearing(-190), t);
		assertEquals(213.483909, normalizeBearing(213.483909), t);
		assertEquals(146.516091, normalizeBearing(-213.483909), t);
		assertEquals(190, normalizeBearing(1990), t);
		assertEquals(170, normalizeBearing(-1990), t);
		assertEquals(Double.NaN, normalizeBearing(Double.POSITIVE_INFINITY), t);
		assertEquals(Double.NaN, normalizeBearing(Double.NEGATIVE_INFINITY), t);
		assertEquals(Double.NaN, normalizeBearing(Double.NaN), t);
	}

	@Test
	public void testDistance() {
		double t = 0.01; // Distance tolerance is less picky.
		assertEquals(0, distance(new LatLng(0, 0), new LatLng(0, 0),
				LengthUnit.MILE), t);
		assertEquals(0, distance(new LatLng(36.79283, -127.36629), new LatLng(
				36.79283, -127.36629), LengthUnit.MILE), t);

		assertEquals(111.19, distance(new LatLng(0, 0), new LatLng(0, 1),
				LengthUnit.KILOMETER), t);
		assertEquals(111.19, distance(new LatLng(0, 0), new LatLng(1, 0),
				LengthUnit.KILOMETER), t);
		assertEquals(111.19, distance(new LatLng(0, 0), new LatLng(0, -1),
				LengthUnit.KILOMETER), t);
		assertEquals(111.19, distance(new LatLng(0, 0), new LatLng(-1, 0),
				LengthUnit.KILOMETER), t);
		assertEquals(111.19, distance(new LatLng(-1, 0), new LatLng(0, 0),
				LengthUnit.KILOMETER), t);

		assertEquals(5133.65, distance(new LatLng(-67.5, 45), new LatLng(0, 0),
				LengthUnit.MILE), t);
		assertEquals(8261.81, distance(new LatLng(-67.5, 45), new LatLng(0, 0),
				LengthUnit.KILOMETER), t);
	}

	@Test
	public void testDistanceInRadians() {
		distRadTest(1, new LatLng(0, 0), new LatLng(0, 1));
		distRadTest(1, new LatLng(0, 0), new LatLng(1, 0));
		distRadTest(45, new LatLng(0, 0), new LatLng(0, 45));
		distRadTest(45, new LatLng(0, 0), new LatLng(45, 0));
	}

	private static void distRadTest(double expected, LatLng point1, LatLng point2) {
		assertEquals(expected, Math.toDegrees(distanceInRadians(point1, point2)), .000001);
	}
	
	@Test
	public void testInitialBearing() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		assertEquals(0.0, initialBearing(new LatLng(0, 0), new LatLng(0, 0)), t);
		assertEquals(90.0, initialBearing(new LatLng(0, 0), new LatLng(0, 1)), t);
		assertEquals(44.995636, initialBearing(new LatLng(0, 0), new LatLng(1, 1)), t);
		assertEquals(315.004363, initialBearing(new LatLng(0, 0), new LatLng(1, -1)), t);
		assertEquals(68.256958, initialBearing(new LatLng(33.45, -112.067), new LatLng(35.1108, -106.61)), t);
	}
	
	@Test
	public void testTravel() {
		// Distance and bearing numbers are very picky if we're trying to hit an exact LatLng.
		assertEquals(new LatLng(0,0), travel(new LatLng(0,0), 0, 0, LengthUnit.KILOMETER));
		assertEquals(new LatLng(0,0), travel(new LatLng(0,0), 180, 0, LengthUnit.KILOMETER));
		assertEquals(new LatLng(1,0), travel(new LatLng(0,0), 0, 111.19508372419142, LengthUnit.KILOMETER));
		assertEquals(new LatLng(0,1), travel(new LatLng(0,0), 90, 111.19508372419142, LengthUnit.KILOMETER));
		assertEquals(new LatLng(0,0), travel(new LatLng(-67.5, 45), 312.7342096008998, 5133.651152139029, LengthUnit.MILE));
	}
}
