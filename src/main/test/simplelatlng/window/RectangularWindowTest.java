package simplelatlng.window;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import org.junit.Test;

import simplelatlng.LatLng;
import simplelatlng.TestConfig;

public class RectangularWindowTest {

	@Test
	public void testRectangularWindow1() {
		double t = TestConfig.DOUBLE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(45, -67.5), 45, 30);
		assertEquals(-90, w.getMinLongitude(), t);
		assertEquals(-45, w.getMaxLongitude(), t);
		assertEquals(30, w.getMinLatitude(), t);
		assertEquals(60, w.getMaxLatitude(), t);
		assertFalse(w.crosses180thMeridian());
	}

	@Test
	public void testRectangularWindow2() {
		double t = TestConfig.DOUBLE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(-90, 0), 30, 30);
		assertEquals(-15, w.getMinLongitude(), t);
		assertEquals(15, w.getMaxLongitude(), t);
		assertEquals(-90, w.getMinLatitude(), t);
		assertEquals(-75, w.getMaxLatitude(), t);
		assertFalse(w.crosses180thMeridian());
	}

	@Test
	public void testRectangularWindow3() {
		double t = TestConfig.DOUBLE_TOLERANCE;
		RectangularWindow w = new RectangularWindow(new LatLng(0, 180), 40, 20);
		assertEquals(-160, w.getMinLongitude(), t);
		assertEquals(160, w.getMaxLongitude(), t);
		assertEquals(-10, w.getMinLatitude(), t);
		assertEquals(10, w.getMaxLatitude(), t);
		assertTrue(w.crosses180thMeridian());
	}

	@Test
	public void testContains() {
		RectangularWindow w1 = new RectangularWindow(new LatLng(45, -67.5), 45,
				30);
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

		RectangularWindow w3 = new RectangularWindow(new LatLng(0, 180), 40, 20);
		assertTrue(w3.contains(new LatLng(0, 180)));
	}
	
	public static void main(String[] args) {
		RectangularWindow rw = new RectangularWindow(new LatLng(0,0), 10, 10);
		LatLng point = new LatLng(0,0);
		Date d1 = new Date();
		for (int i = 0; i < 1000000; i++) {
			rw.contains(point);
		}
		Date d2 = new Date();
		
		System.out.println(d2.getTime() - d1.getTime());
		
		CircularWindow cw = new CircularWindow(new LatLng(0,0), 10);
		d1 = new Date();
		for (int i = 0; i < 1000000; i++) {
			cw.contains(point);
		}
		d2 = new Date();
		
		System.out.println(d2.getTime() - d1.getTime());
	}
}
