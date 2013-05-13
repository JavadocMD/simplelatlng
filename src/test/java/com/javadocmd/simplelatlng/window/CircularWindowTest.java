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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LatLngConfig;
import com.javadocmd.simplelatlng.util.LengthUnit;
import com.javadocmd.simplelatlng.window.LatLngWindowTest.TestObject;

public class CircularWindowTest {

	@Test
	public void testCircularWindowLatLngDouble() {
		CircularWindow w = new CircularWindow(new LatLng(10, 0), 45);
		assertEquals(10, w.getCenter().getLatitude(), LatLngConfig.DEGREE_TOLERANCE);
		assertEquals(0, w.getCenter().getLongitude(), LatLngConfig.DEGREE_TOLERANCE);
		assertEquals(45, w.getRadius(), LatLngConfig.DEGREE_TOLERANCE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCircularWindowLatLngDouble2() {
		new CircularWindow(null, 45);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCircularWindowLatLngDouble3() {
		new CircularWindow(new LatLng(10, 0), Double.NaN);
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
			assertFalse(w.contains(new LatLng(69.999999, 0)));
			assertFalse(w.contains(new LatLng(-90, 0)));
			assertFalse(w.contains(new LatLng(0, 0)));
		}
	}

	@Test
	public void filterCopySort() {
		List<TestObject> source = new ArrayList<TestObject>();
		source.add(new TestObject(new LatLng(0, 0))); // 0 *
		source.add(new TestObject(new LatLng(90, 0))); // 10007.557535177228
		source.add(new TestObject(new LatLng(45, 45))); // 6671.705023451485
		source.add(new TestObject(new LatLng(20, -15))); // 2759.2189262563847
		source.add(new TestObject(new LatLng(-5, 7))); // 955.7308173367139 *
		source.add(new TestObject(new LatLng(8, -5))); // 1048.0527061057333 *
		source.add(new TestObject(new LatLng(3, 0))); // 333.5852511725742 *
		source.add(new TestObject(new LatLng(1, 3))); // 351.6136586697643 *

		List<TestObject> destination = new ArrayList<TestObject>();

		CircularWindow window = new CircularWindow(new LatLng(0, 0), 10);
		window.filterCopySort(source, destination, LatLngWindowTest.helper);

		Iterator<TestObject> i = destination.iterator();
		assertEquals(new LatLng(0, 0), i.next().getPoint());
		assertEquals(new LatLng(3, 0), i.next().getPoint());
		assertEquals(new LatLng(1, 3), i.next().getPoint());
		assertEquals(new LatLng(-5, 7), i.next().getPoint());
		assertEquals(new LatLng(8, -5), i.next().getPoint());
		assertEquals(5, destination.size());
	}

	@Test
	public void testOverlaps() {
		CircularWindow w1 = new CircularWindow(new LatLng(0, 0), 10);
		CircularWindow w2 = new CircularWindow(new LatLng(0, 0), 10);
		assertTrue(w1.overlaps(w2));
		assertTrue(w2.overlaps(w1));
		CircularWindow w3 = new CircularWindow(new LatLng(1, 1), 5);
		assertTrue(w1.overlaps(w3));
		CircularWindow w4 = new CircularWindow(new LatLng(10, 0), 10);
		assertTrue(w1.overlaps(w4));
		CircularWindow w5 = new CircularWindow(new LatLng(45, 45), 5);
		assertFalse(w1.overlaps(w5));
	}

	@Test
	public void testGetRadius() {
		CircularWindow w = new CircularWindow(new LatLng(10, 0), 45);
		assertEquals(45, w.getRadius(), 0.000001);
	}

	@Test
	public void testWithLengthUnits() {
		CircularWindow w = new CircularWindow(new LatLng(10, 0), 100, LengthUnit.KILOMETER);
		assertEquals(100, w.getRadius(LengthUnit.KILOMETER), 0.0001);
	}
}
