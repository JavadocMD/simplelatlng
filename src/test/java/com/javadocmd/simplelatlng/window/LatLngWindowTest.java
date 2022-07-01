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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class LatLngWindowTest {

	static FilterHelper<TestObject> helper = new FilterHelper<TestObject>() {

		@Override
		public LatLng getLatLng(TestObject object) {
			return object.getPoint();
		}

	};

	private Map<Integer, TestObject> testObjects;

	@Before
	public void setUp() throws Exception {
		Map<Integer, TestObject> map = new HashMap<Integer, TestObject>();
		map.put(1, new TestObject(new LatLng(0, 0)));
		map.put(2, new TestObject(new LatLng(90, 0)));
		map.put(3, new TestObject(new LatLng(45, 45)));
		map.put(4, new TestObject(new LatLng(20, -15)));
		map.put(5, new TestObject(new LatLng(-5, 7)));
		map.put(6, new TestObject(new LatLng(7, -5)));
		map.put(7, new TestObject(new LatLng(3, 0)));
		map.put(8, new TestObject(new LatLng(0, 3)));
		this.testObjects = map;
	}

	@Test
	public void testFilterRectangularWindow1() {
		RectangularWindow window = new RectangularWindow(new LatLng(0, 0), 20, 20);
		window.filter(testObjects.values(), helper);
		assertTrue(testObjects.containsKey(1));
		assertTrue(testObjects.containsKey(5));
		assertTrue(testObjects.containsKey(6));
		assertTrue(testObjects.containsKey(7));
		assertTrue(testObjects.containsKey(8));
		assertEquals(5, testObjects.size());
	}

	@Test
	public void testFilterRectangularWindow2() {
		RectangularWindow window = new RectangularWindow(new LatLng(0, 0), 10, 10);
		window.filter(testObjects.values(), helper);
		assertTrue(testObjects.containsKey(1));
		assertTrue(testObjects.containsKey(7));
		assertTrue(testObjects.containsKey(8));
		assertEquals(3, testObjects.size());
	}

	@Test
	public void testFilterCircularWindow1() {
		CircularWindow window = new CircularWindow(new LatLng(0, 0), 10);
		window.filter(testObjects.values(), helper);
		assertTrue(testObjects.containsKey(1));
		assertTrue(testObjects.containsKey(5));
		assertTrue(testObjects.containsKey(6));
		assertTrue(testObjects.containsKey(7));
		assertTrue(testObjects.containsKey(8));
		assertEquals(5, testObjects.size());
	}

	@Test
	public void testFilterCircularWindow2() {
		CircularWindow window = new CircularWindow(new LatLng(0, 0), 8);
		window.filter(testObjects.values(), helper);
		assertTrue(testObjects.containsKey(1));
		assertTrue(testObjects.containsKey(7));
		assertTrue(testObjects.containsKey(8));
		assertEquals(3, testObjects.size());
	}

	@Test
	public void testFilterCopy() {
		List<TestObject> result = new ArrayList<TestObject>();
		RectangularWindow window = new RectangularWindow(new LatLng(0, 0), 20, 20);
		window.filterCopy(testObjects.values(), result, helper);
		Iterator<TestObject> i = result.iterator();
		TestObject first = i.next();
		assertTrue(testObjects.get(1) == first); // Shallow copy!
		assertEquals(new LatLng(0, 0), first.getPoint());
		assertEquals(new LatLng(-5, 7), i.next().getPoint());
		assertEquals(new LatLng(7, -5), i.next().getPoint());
		assertEquals(new LatLng(3, 0), i.next().getPoint());
		assertEquals(new LatLng(0, 3), i.next().getPoint());
		assertEquals(5, result.size());
	}

	@Test
	public void testLongitudeDeltaToLength() {
		assertEquals(111.195, LatLngWindow.longitudeDeltaToLength(1.0, LengthUnit.KILOMETER, 0.0), 0.001);
		assertEquals(78.626, LatLngWindow.longitudeDeltaToLength(1.0, LengthUnit.KILOMETER, 45.0), 0.001);
	}

	@Test
	public void testLengthToLongitudeDelta() {
		assertEquals(1.0, LatLngWindow.lengthToLongitudeDelta(111.195, LengthUnit.KILOMETER, 0.0), 0.001);
		assertEquals(.707, LatLngWindow.lengthToLongitudeDelta(78.626, LengthUnit.KILOMETER, 0.0), 0.001);
	}

	/**
	 * A test object to use for filter tests.
	 */
	public static class TestObject {

		private LatLng point;

		public TestObject() {

		}

		public TestObject(LatLng point) {
			this.point = point;
		}

		public void setPoint(LatLng point) {
			this.point = point;
		}

		public LatLng getPoint() {
			return point;
		}
	}
}
