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

import org.junit.Test;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LatLngConfig;
import com.javadocmd.simplelatlng.util.LengthUnit;

public class RectangularWindowTest {

	@Test
	public void testRectangularWindow1() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(45, -67.5), 30, 45);
		assertEquals(-90, w.getLeftLongitude(), t);
		assertEquals(-45, w.getRightLongitude(), t);
		assertEquals(30, w.getMinLatitude(), t);
		assertEquals(60, w.getMaxLatitude(), t);
		assertFalse(w.crosses180thMeridian());
	}

	@Test
	public void testRectangularWindow2() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(-90, 0), 30, 30);
		assertEquals(-15, w.getLeftLongitude(), t);
		assertEquals(15, w.getRightLongitude(), t);
		assertEquals(-90, w.getMinLatitude(), t);
		assertEquals(-75, w.getMaxLatitude(), t);
		assertFalse(w.crosses180thMeridian());
	}

	@Test
	public void testRectangularWindow3() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(0, 180), 40, 20);
		assertEquals(-170, w.getRightLongitude(), t);
		assertEquals(170, w.getLeftLongitude(), t);
		assertEquals(-20, w.getMinLatitude(), t);
		assertEquals(20, w.getMaxLatitude(), t);
		assertTrue(w.crosses180thMeridian());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRectangularWindow4() {
		new RectangularWindow(null, 10, 10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRectangularWindow5() {
		new RectangularWindow(new LatLng(0, 0), Double.POSITIVE_INFINITY, 10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRectangularWindow6() {
		new RectangularWindow(new LatLng(0, 180), 10, Double.NEGATIVE_INFINITY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRectangularWindow7() {
		new RectangularWindow(new LatLng(0, 0), 10, Double.NaN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRectangularWindow8() {
		new RectangularWindow(new LatLng(0, 0), Double.NaN, 10);
	}

	@Test
	public void testRectangularWindow9() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(23.079731,
				-25.136718), 141.826753, 309.726562);
		assertEquals(-179.999999, w.getLeftLongitude(), t);
		assertEquals(129.726563, w.getRightLongitude(), t);
		assertEquals(-47.833645, w.getMinLatitude(), t);
		assertEquals(90, w.getMaxLatitude(), t);
		assertFalse(w.crosses180thMeridian());
	}

	@Test
	public void testRectangularWindow10() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(23.079731,
				-30.136718), 141.826753, 309.726562);
		assertEquals(175.000001, w.getLeftLongitude(), t);
		assertEquals(124.726563, w.getRightLongitude(), t);
		assertEquals(-47.833645, w.getMinLatitude(), t);
		assertEquals(90, w.getMaxLatitude(), t);
		assertTrue(w.crosses180thMeridian());
	}

	@Test
	public void testRectangularWindow11() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		double width = 31683.495744;
		double height = 15770.437674;
		RectangularWindow w = new RectangularWindow(new LatLng(23.079731,
				-30.136718), width, height, LengthUnit.KILOMETER);
		assertEquals(175.000001, w.getLeftLongitude(), t);
		assertEquals(124.726563, w.getRightLongitude(), t);
		assertEquals(-47.833645, w.getMinLatitude(), t);
		assertEquals(90, w.getMaxLatitude(), t);
		assertTrue(w.crosses180thMeridian());
	}

	@Test
	public void testRectangularWindow12() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		double width = 19687.211770;
		double height = 9799.295782;
		RectangularWindow w = new RectangularWindow(new LatLng(23.079731,
				-30.136718), width, height, LengthUnit.MILE);
		assertEquals(175.000001, w.getLeftLongitude(), t);
		assertEquals(124.726563, w.getRightLongitude(), t);
		assertEquals(-47.833645, w.getMinLatitude(), t);
		assertEquals(90, w.getMaxLatitude(), t);
		assertTrue(w.crosses180thMeridian());
	}
	
	@Test
	public void testRectangularWindow13() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(60, -45), new LatLng(30, -90));
		assertEquals(new LatLng(45, -67.5), w.getCenter());
		assertEquals(-90, w.getLeftLongitude(), t);
		assertEquals(-45, w.getRightLongitude(), t);
		assertEquals(30, w.getMinLatitude(), t);
		assertEquals(60, w.getMaxLatitude(), t);
		assertFalse(w.crosses180thMeridian());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testRectangularWindow14() {
		new RectangularWindow(new LatLng(-75, 15), new LatLng(-90, -15));
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testRectangularWindow15() {
		new RectangularWindow(new LatLng(0, 0), new LatLng(45, -30));
	}

	@Test
	public void testRectangularWindow16() {
		double t = LatLngConfig.DEGREE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(20, -170), new LatLng(-20, 170));
		assertEquals(new LatLng(0, 180), w.getCenter());
		assertEquals(-170, w.getRightLongitude(), t);
		assertEquals(170, w.getLeftLongitude(), t);
		assertEquals(-20, w.getMinLatitude(), t);
		assertEquals(20, w.getMaxLatitude(), t);
		assertTrue(w.crosses180thMeridian());
	}

	@Test
	public void testContains1() {
		RectangularWindow window = new RectangularWindow(new LatLng(45, -67.5),
				30, 45);
		assertTrue(window.contains(new LatLng(45, -67.5)));
		assertTrue(window.contains(new LatLng(30, -67.5)));
		assertTrue(window.contains(new LatLng(60, -67.5)));
		assertTrue(window.contains(new LatLng(45, -90)));
		assertTrue(window.contains(new LatLng(45, -45)));
		assertTrue(window.contains(new LatLng(30, -90)));
		assertTrue(window.contains(new LatLng(60, -45)));
		assertTrue(window.contains(new LatLng(60, -90)));
		assertTrue(window.contains(new LatLng(30, -45)));
		assertTrue(window.contains(new LatLng(30, -405)));
		assertFalse(window.contains(new LatLng(45, -100)));
		assertFalse(window.contains(new LatLng(45, -30)));
		assertFalse(window.contains(new LatLng(29, -67.5)));
		assertFalse(window.contains(new LatLng(61, -67.5)));
		assertFalse(window.contains(new LatLng(0, 0)));
		assertFalse(window.contains(new LatLng(30, -127.32001)));
		assertFalse(window.contains(new LatLng(30, -487.32001)));
	}

	@Test
	public void testContains2() {
		RectangularWindow window = new RectangularWindow(new LatLng(-90, 0), 30,
				30);
		assertTrue(window.contains(new LatLng(-90, 0)));
		assertTrue(window.contains(new LatLng(-90, 10)));
		assertTrue(window.contains(new LatLng(-90, 180)));
		assertTrue(window.contains(new LatLng(-90, -240)));
		assertTrue(window.contains(new LatLng(-90, 7293.1298383)));
		assertTrue(window.contains(new LatLng(-1000, 0)));
		assertTrue(window.contains(new LatLng(-1000, 360)));
		assertTrue(window.contains(new LatLng(-75, 0)));
		assertTrue(window.contains(new LatLng(-75, 15)));
		assertTrue(window.contains(new LatLng(-75, -15)));
		assertFalse(window.contains(new LatLng(-75, 15.000001)));
		assertTrue(window.contains(new LatLng(-75, 14.999999)));
		assertFalse(window.contains(new LatLng(-75, -15.000001)));
		assertTrue(window.contains(new LatLng(-75, -14.999999)));
		assertFalse(window.contains(new LatLng(-74.999999, 0)));
		assertFalse(window.contains(new LatLng(-74.999999, 14.999999)));
	}

	@Test
	public void testContains3() {
		RectangularWindow window = new RectangularWindow(new LatLng(0, 180), 40,
				360);
		for (double lng = 0; lng < 720; lng += 20) {
			assertTrue(window.contains(new LatLng(0, lng)));
			assertTrue(window.contains(new LatLng(5, lng)));
			assertTrue(window.contains(new LatLng(10, lng)));
			assertTrue(window.contains(new LatLng(20, lng)));
			assertTrue(window.contains(new LatLng(-5, lng)));
			assertTrue(window.contains(new LatLng(-10, lng)));
			assertTrue(window.contains(new LatLng(-15, lng)));
			assertTrue(window.contains(new LatLng(-20, lng)));
			assertFalse(window.contains(new LatLng(-20.1, lng)));
			assertFalse(window.contains(new LatLng(20.1, lng)));
			assertFalse(window.contains(new LatLng(40, lng)));
			assertFalse(window.contains(new LatLng(-40, lng)));
			assertFalse(window.contains(new LatLng(-90, lng)));
			assertFalse(window.contains(new LatLng(90, lng)));
		}
	}

	@Test
	public void testContains4() {
		RectangularWindow window = new RectangularWindow(new LatLng(0, 180), 40,
				40);
		assertTrue(window.contains(new LatLng(0, 180)));
		assertTrue(window.contains(new LatLng(0, 200)));
		assertTrue(window.contains(new LatLng(0, -160)));
		assertTrue(window.contains(new LatLng(0, 160)));
		assertFalse(window.contains(new LatLng(0, -159)));
		assertFalse(window.contains(new LatLng(0, 159)));
	}

	@Test
	public void testOverlaps1() {
		RectangularWindow w1 = new RectangularWindow(new LatLng(0, 0), 10, 10);
		RectangularWindow w2 = new RectangularWindow(new LatLng(0, 0), 10, 10);
		assertTrue(w1.overlaps(w2));
		assertTrue(w2.overlaps(w1));
		RectangularWindow w3 = new RectangularWindow(new LatLng(5, 5), 10, 10);
		RectangularWindow w4 = new RectangularWindow(new LatLng(10, 10), 10, 10);
		assertTrue(w1.overlaps(w3));
		assertTrue(w1.overlaps(w4));
		RectangularWindow w5 = new RectangularWindow(new LatLng(11, 11), 10, 10);
		assertFalse(w1.overlaps(w5));
		RectangularWindow w6 = new RectangularWindow(new LatLng(-5, -5), 10, 10);
		assertTrue(w1.overlaps(w6));
		assertFalse(w4.overlaps(w6));
		assertFalse(w6.overlaps(w4));
		RectangularWindow w7 = new RectangularWindow(new LatLng(-5, 5), 10, 10);
		assertTrue(w1.overlaps(w7));
		assertFalse(w4.overlaps(w7));
		assertFalse(w7.overlaps(w4));
	}

	@Test
	public void testOverlaps2() {
		RectangularWindow w0 = new RectangularWindow(new LatLng(0, 0), 2, 2);
		RectangularWindow a = new RectangularWindow(new LatLng(3, 1), 2, 2);
		RectangularWindow b = new RectangularWindow(new LatLng(1, 3), 2, 2);
		RectangularWindow c = new RectangularWindow(new LatLng(-1, 3), 2, 2);
		RectangularWindow d = new RectangularWindow(new LatLng(-3, 1), 2, 2);
		RectangularWindow e = new RectangularWindow(new LatLng(-3, -1), 2, 2);
		RectangularWindow f = new RectangularWindow(new LatLng(-1, -3), 2, 2);
		RectangularWindow g = new RectangularWindow(new LatLng(1, -3), 2, 2);
		RectangularWindow h = new RectangularWindow(new LatLng(3, -1), 2, 2);

		assertFalse(w0.overlaps(a));
		assertFalse(w0.overlaps(b));
		assertFalse(w0.overlaps(c));
		assertFalse(w0.overlaps(d));
		assertFalse(w0.overlaps(e));
		assertFalse(w0.overlaps(f));
		assertFalse(w0.overlaps(g));
		assertFalse(w0.overlaps(h));

		assertFalse(a.overlaps(w0));
		assertFalse(b.overlaps(w0));
		assertFalse(c.overlaps(w0));
		assertFalse(d.overlaps(w0));
		assertFalse(e.overlaps(w0));
		assertFalse(f.overlaps(w0));
		assertFalse(g.overlaps(w0));
		assertFalse(h.overlaps(w0));
	}

	@Test
	public void testOverlaps3() {
		RectangularWindow w0 = new RectangularWindow(new LatLng(0, 180), 2, 2);
		RectangularWindow a = new RectangularWindow(new LatLng(3, 181), 2, 2);
		RectangularWindow b = new RectangularWindow(new LatLng(1, 183), 2, 2);
		RectangularWindow c = new RectangularWindow(new LatLng(-1, 183), 2, 2);
		RectangularWindow d = new RectangularWindow(new LatLng(-3, 181), 2, 2);
		RectangularWindow e = new RectangularWindow(new LatLng(-3, 179), 2, 2);
		RectangularWindow f = new RectangularWindow(new LatLng(-1, 177), 2, 2);
		RectangularWindow g = new RectangularWindow(new LatLng(1, 177), 2, 2);
		RectangularWindow h = new RectangularWindow(new LatLng(3, 179), 2, 2);

		assertFalse(w0.overlaps(a));
		assertFalse(w0.overlaps(b));
		assertFalse(w0.overlaps(c));
		assertFalse(w0.overlaps(d));
		assertFalse(w0.overlaps(e));
		assertFalse(w0.overlaps(f));
		assertFalse(w0.overlaps(g));
		assertFalse(w0.overlaps(h));

		assertFalse(a.overlaps(w0));
		assertFalse(b.overlaps(w0));
		assertFalse(c.overlaps(w0));
		assertFalse(d.overlaps(w0));
		assertFalse(e.overlaps(w0));
		assertFalse(f.overlaps(w0));
		assertFalse(g.overlaps(w0));
		assertFalse(h.overlaps(w0));
	}

	@Test
	public void testOverlaps4() {
		RectangularWindow w1 = new RectangularWindow(new LatLng(0, 180), 2, 2);
		RectangularWindow w2 = new RectangularWindow(new LatLng(0, 180), 2, 1);
		RectangularWindow w3 = new RectangularWindow(new LatLng(0, 180), 2, 4);

		assertTrue(w1.overlaps(w2));
		assertTrue(w2.overlaps(w1));

		assertTrue(w1.overlaps(w3));
		assertTrue(w3.overlaps(w1));

		assertTrue(w3.overlaps(w2));
		assertTrue(w2.overlaps(w3));

		RectangularWindow w4 = new RectangularWindow(new LatLng(0, 178), 2, 2);
		assertTrue(w1.overlaps(w4));
		assertFalse(w2.overlaps(w4));
		assertTrue(w3.overlaps(w4));

		assertTrue(w4.overlaps(w1));
		assertFalse(w4.overlaps(w2));
		assertTrue(w4.overlaps(w3));
	}

	@Test
	public void testGetHeight() {
		RectangularWindow w = new RectangularWindow(new LatLng(0, 0), 111.195,
				111.195, LengthUnit.KILOMETER);
		assertEquals(111.195, w.getHeight(LengthUnit.KILOMETER), 0.001);
	}

	@Test
	public void testGetWidth() {
		RectangularWindow w = new RectangularWindow(new LatLng(0, 0), 111.195,
				LengthUnit.KILOMETER);
		assertEquals(111.195, w.getWidth(LengthUnit.KILOMETER), 0.001);
	}

	@Test
	public void testGetLatitudeDelta() {
		RectangularWindow w = new RectangularWindow(new LatLng(45, -67.5), 30, 45);
		assertEquals(30, w.getLatitudeDelta(), LatLngConfig.DEGREE_TOLERANCE);
	}

	@Test
	public void testGetLongitudeDelta() {
		RectangularWindow w = new RectangularWindow(new LatLng(45, -67.5), 30, 45);
		assertEquals(45, w.getLongitudeDelta(), LatLngConfig.DEGREE_TOLERANCE);
	}
}
