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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;

import com.javadocmd.simplelatlng.util.LengthUnit;
import com.javadocmd.simplelatlng.window.CircularWindow;
import com.javadocmd.simplelatlng.window.RectangularWindow;

/**
 * Basic benchmarking test. Repeats the critical functions a bunch of times and
 * reports how long that took. Also reports on approximate memory usage.
 */
public class SpeedProfile {

	private static final int NUMBER_OF_POINTS = 100000;

	private static final NumberFormat integer = NumberFormat.getInstance();
	private static final NumberFormat decimal = new DecimalFormat("0.00");

	public static void main(String[] args) {
		SpeedProfile p = new SpeedProfile();

		System.out.println();

		p.profileEquals();
		p.profileDistance();
		p.profileRectangularWindow();
		p.profileCircularWindow();
		p.profileGeohasher();
		p.profileHashCode();
	}

	private LatLng[] points;

	public SpeedProfile() {
		long memStart = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		points = new LatLng[NUMBER_OF_POINTS];
		for (int i = 0; i < points.length; i++) {
			points[i] = LatLng.random();
		}
		long memEnd = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

		double mem = (double) (memEnd - memStart);

		System.out.printf("All test run against %s points.\n", integer.format(NUMBER_OF_POINTS));
		System.out.printf("Storage approximately %s MB.\n", decimal.format(mem / 1048576.0));
		System.out.printf("Averages %s bytes per LatLng.\n", decimal.format(mem / (double) NUMBER_OF_POINTS));
	}

	private void profileEquals() {
		LatLng testPoint = LatLng.random();

		Date start = new Date();
		for (int i = 0; i < points.length; i++) {
			points[i].equals(testPoint);
		}
		Date end = new Date();

		System.out.printf("Tested equality in %s ms.\n", integer.format(end.getTime() - start.getTime()));
	}

	private void profileGeohasher() {
		Date start = new Date();
		for (int i = 0; i < points.length; i++) {
			Geohasher.hash(points[i]);
		}
		Date end = new Date();

		System.out.printf("Geohashed (one way) in %s ms.\n", integer.format(end.getTime() - start.getTime()));

		start = new Date();
		for (int i = 0; i < points.length; i++) {
			String s = Geohasher.hash(points[i]);
			Geohasher.decode(s);
		}
		end = new Date();

		System.out.printf("Geohashed and decoded in %s ms.\n", integer.format(end.getTime() - start.getTime()));
	}

	private void profileHashCode() {
		// Repeat 5 times in hopes of detecting a caching benefit if any exists.
		Date start = new Date();
		for (int j = 0; j < 5; j++) {
			for (int i = 0; i < points.length; i++) {
				points[i].hashCode();
			}
		}
		Date end = new Date();

		System.out.printf("hashCode() 5x in %s ms.\n", integer.format(end.getTime() - start.getTime()));
	}

	private void profileDistance() {
		LatLng testPoint = LatLng.random();

		Date start = new Date();
		for (int i = 0; i < points.length; i++) {
			LatLngTool.distance(testPoint, points[i], LengthUnit.KILOMETER);
		}
		Date end = new Date();

		System.out.printf("Calculated kilometer distances in %s ms.\n", integer.format(end.getTime() - start.getTime()));

		start = new Date();
		for (int i = 0; i < points.length; i++) {
			LatLngTool.distance(testPoint, points[i], LengthUnit.MILE);
		}
		end = new Date();

		System.out.printf("Calculated mile distances in %s ms.\n", integer.format(end.getTime() - start.getTime()));
	}

	private void profileRectangularWindow() {
		RectangularWindow window = new RectangularWindow(new LatLng(0, 0), 10, 10);

		Date start = new Date();
		for (int i = 0; i < points.length; i++) {
			window.contains(points[i]);
		}
		Date end = new Date();

		System.out.printf("RectangularWindow tested contains in %s ms.\n", integer.format(end.getTime() - start.getTime()));
	}

	private void profileCircularWindow() {
		CircularWindow window = new CircularWindow(new LatLng(0, 0), 10);

		Date start = new Date();
		for (int i = 0; i < points.length; i++) {
			window.contains(points[i]);
		}
		Date end = new Date();

		System.out.printf("CircularWindow tested contains in %s ms.\n", integer.format(end.getTime() - start.getTime()));
	}
}
