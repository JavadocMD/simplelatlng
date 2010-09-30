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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import simplelatlng.LatLng;
import simplelatlng.util.LatLngConfig;

public class RectangularWindowTest {

	// TODO: test constructors for exceptions.

	@Test
	public void testRectangularWindow1() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(45, -67.5), 30, 45);
		assertEquals(-90, w.getMinLongitude(), t);
		assertEquals(-45, w.getMaxLongitude(), t);
		assertEquals(30, w.getMinLatitude(), t);
		assertEquals(60, w.getMaxLatitude(), t);
		assertFalse(w.crosses180thMeridian());
	}

	@Test
	public void testRectangularWindow2() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(-90, 0), 30, 30);
		assertEquals(-15, w.getMinLongitude(), t);
		assertEquals(15, w.getMaxLongitude(), t);
		assertEquals(-90, w.getMinLatitude(), t);
		assertEquals(-75, w.getMaxLatitude(), t);
		assertFalse(w.crosses180thMeridian());
	}

	@Test
	public void testRectangularWindow3() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(0, 180), 40, 20);
		assertEquals(-170, w.getMinLongitude(), t);
		assertEquals(170, w.getMaxLongitude(), t);
		assertEquals(-20, w.getMinLatitude(), t);
		assertEquals(20, w.getMaxLatitude(), t);
		assertTrue(w.crosses180thMeridian());
	}

	@Test
	public void testContains() {
		RectangularWindow w1 = new RectangularWindow(new LatLng(45, -67.5), 30,
				45);
		assertTrue(w1.contains(new LatLng(45, -67.5)));
		assertTrue(w1.contains(new LatLng(30, -67.5)));
		assertTrue(w1.contains(new LatLng(60, -67.5)));
		assertTrue(w1.contains(new LatLng(45, -90)));
		assertTrue(w1.contains(new LatLng(45, -45)));
		assertTrue(w1.contains(new LatLng(30, -90)));
		assertTrue(w1.contains(new LatLng(60, -45)));
		assertTrue(w1.contains(new LatLng(60, -90)));
		assertTrue(w1.contains(new LatLng(30, -45)));
		assertTrue(w1.contains(new LatLng(30, -405)));
		assertFalse(w1.contains(new LatLng(45, -100)));
		assertFalse(w1.contains(new LatLng(45, -30)));
		assertFalse(w1.contains(new LatLng(29, -67.5)));
		assertFalse(w1.contains(new LatLng(61, -67.5)));
		assertFalse(w1.contains(new LatLng(0, 0)));
		assertFalse(w1.contains(new LatLng(30, -127.32001)));
		assertFalse(w1.contains(new LatLng(30, -487.32001)));

		RectangularWindow w2 = new RectangularWindow(new LatLng(-90, 0), 30, 30);
		assertTrue(w2.contains(new LatLng(-90, 0)));
		assertTrue(w2.contains(new LatLng(-90, 10)));
		assertTrue(w2.contains(new LatLng(-90, 180)));
		assertTrue(w2.contains(new LatLng(-90, -240)));
		assertTrue(w2.contains(new LatLng(-90, 7293.1298383)));
		assertTrue(w2.contains(new LatLng(-1000, 0)));
		assertTrue(w2.contains(new LatLng(-1000, 360)));
		assertTrue(w2.contains(new LatLng(-75, 0)));
		assertTrue(w2.contains(new LatLng(-75, 15)));
		assertTrue(w2.contains(new LatLng(-75, -15)));
		assertFalse(w2.contains(new LatLng(-74.999998, 0)));
		assertFalse(w2.contains(new LatLng(-74.999998, 14.999998)));

		RectangularWindow w3 = new RectangularWindow(new LatLng(0, 180), 40, 360);
		for (double lng = 0; lng < 720; lng += 20) {
			assertTrue(w3.contains(new LatLng(0, lng)));
			assertTrue(w3.contains(new LatLng(5, lng)));
			assertTrue(w3.contains(new LatLng(10, lng)));
			assertTrue(w3.contains(new LatLng(20, lng)));
			assertTrue(w3.contains(new LatLng(-5, lng)));
			assertTrue(w3.contains(new LatLng(-10, lng)));
			assertTrue(w3.contains(new LatLng(-15, lng)));
			assertTrue(w3.contains(new LatLng(-20, lng)));
			assertFalse(w3.contains(new LatLng(-20.1, lng)));
			assertFalse(w3.contains(new LatLng(20.1, lng)));
			assertFalse(w3.contains(new LatLng(40, lng)));
			assertFalse(w3.contains(new LatLng(-40, lng)));
			assertFalse(w3.contains(new LatLng(-90, lng)));
			assertFalse(w3.contains(new LatLng(90, lng)));
		}

	}

}
