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

public class CircularWindowTest {

	@Test
	public void testCircularWindowLatLngDouble() {
		CircularWindow w = new CircularWindow(new LatLng(10, 0), 45);
		assertEquals(10, w.getCenter().getLatitude(),
				LatLngConfig.DEGREE_TOLERANCE);
		assertEquals(0, w.getCenter().getLongitude(),
				LatLngConfig.DEGREE_TOLERANCE);
		assertEquals(45, w.getRadius(), LatLngConfig.DEGREE_TOLERANCE);
	}

	@Test
	public void testCircularWindowLatLngDoubleLengthUnit() {
		// TODO:
	}

	@Test
	public void testContains() {
		{
			CircularWindow w = new CircularWindow(new LatLng(10, 0), 45);
			assertTrue(w.contains(new LatLng(10, 0)));
			assertTrue(w.contains(new LatLng(11, 1)));
			assertTrue(w.contains(new LatLng(9, -1)));
			assertFalse(w.contains(new LatLng(55, 45)));
		}

		{
			CircularWindow w = new CircularWindow(new LatLng(90, 0), 20);
			assertTrue(w.contains(new LatLng(90, 0)));
			assertTrue(w.contains(new LatLng(89, 0)));
			assertTrue(w.contains(new LatLng(89, 45)));
			assertTrue(w.contains(new LatLng(89, -45)));
			assertTrue(w.contains(new LatLng(89, 90)));
			assertTrue(w.contains(new LatLng(89, -90)));
			assertTrue(w.contains(new LatLng(89, 180)));
			assertTrue(w.contains(new LatLng(70, 0)));
			assertTrue(w.contains(new LatLng(70, 90)));
			assertTrue(w.contains(new LatLng(70, -90)));
			assertTrue(w.contains(new LatLng(70, 180)));
			assertFalse(w.contains(new LatLng(69.999998, 0)));
			assertFalse(w.contains(new LatLng(-90, 0)));
			assertFalse(w.contains(new LatLng(0, 0)));
		}
	}

	@Test
	public void testGetRadiusLengthUnit() {
		// TODO:
	}

}
