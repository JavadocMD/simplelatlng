package simplelatlng;

import static org.junit.Assert.*;
import static simplelatlng.LatLng.degreesEqual;

import org.junit.Test;

import simplelatlng.util.LatLngConfig;

public class LatLngTest {

	@Test
	public void testLatLng1() {
		LatLng l = new LatLng(33.109283, -123.182312);
		assertEquals(33.109283, l.getLatitude(), LatLngConfig.DEGREE_TOLERANCE);
		assertEquals(-123.182312, l.getLongitude(), LatLngConfig.DEGREE_TOLERANCE);

		l = new LatLng(Double.POSITIVE_INFINITY, -123.182312);
		assertEquals(90, l.getLatitude(), LatLngConfig.DEGREE_TOLERANCE);
		assertEquals(0, l.getLongitude(), LatLngConfig.DEGREE_TOLERANCE);

		l = new LatLng(Double.NEGATIVE_INFINITY, -123.182312);
		assertEquals(-90, l.getLatitude(), LatLngConfig.DEGREE_TOLERANCE);
		assertEquals(0, l.getLongitude(), LatLngConfig.DEGREE_TOLERANCE);

		l = new LatLng(Double.NEGATIVE_INFINITY, Double.NaN);
		assertEquals(-90, l.getLatitude(), LatLngConfig.DEGREE_TOLERANCE);
		assertEquals(0, l.getLongitude(), LatLngConfig.DEGREE_TOLERANCE);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLatLng2() {
		new LatLng(0, Double.NaN);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLatLng3() {
		new LatLng(0, Double.POSITIVE_INFINITY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLatLng4() {
		new LatLng(0, Double.NEGATIVE_INFINITY);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLatLng5() {
		new LatLng(Double.NaN, 0);
	}

	@Test
	public void testSetLatitudeLongitude() {
		LatLng l = new LatLng(0, 0);
		l.setLatitudeLongitude(33.109283, -123.182312);
		assertEquals(33.109283, l.getLatitude(), LatLngConfig.DEGREE_TOLERANCE);
		assertEquals(-123.182312, l.getLongitude(), LatLngConfig.DEGREE_TOLERANCE);
	}

	@Test
	public void testEqualsObject() {
		LatLng l1 = new LatLng(0.0, 0.0);
		assertTrue(l1.equals(l1));

		LatLng l2 = new LatLng(0.0, 0.0);
		assertTrue(l1.equals(l2));
		assertTrue(l2.equals(l1));

		LatLng l3 = new LatLng(1.0, -1.0);
		assertFalse(l1.equals(l3));
		assertFalse(l2.equals(l3));

		LatLng l4 = new LatLng(33.987123, -127.839392);
		LatLng l5 = new LatLng(33.9871239, -127.8393925);
		assertTrue(l4.equals(l5));

		LatLng l6 = new LatLng(-90, 0);
		LatLng l7 = new LatLng(-91, -127.3292);
		assertTrue(l6.equals(l7));
	}

	@Test
	public void testDegreesEqual() {
		assertTrue(degreesEqual(0.0, 0.0));
		assertTrue(degreesEqual(123.123456, 123.123456));
		assertTrue(degreesEqual(123456789.123456, 123456789.123456));
		assertTrue(degreesEqual(123456789.1234560, 123456789.1234569));
		assertTrue(degreesEqual(0.0, -0.0));
		assertTrue(degreesEqual(-123456789.123456, -123456789.123456));
		assertTrue(degreesEqual(-123456789.1234560, -123456789.1234569));

		assertFalse(degreesEqual(0, 1));
		assertFalse(degreesEqual(-123, 123));
		assertFalse(degreesEqual(0.0, 0.2));
		assertFalse(degreesEqual(0.0, 0.02));
		assertFalse(degreesEqual(0.0, 0.002));
		assertFalse(degreesEqual(0.0, 0.0002));
		assertFalse(degreesEqual(0.0, 0.00002));
		assertFalse(degreesEqual(0.0, 0.000002));
		assertTrue(degreesEqual(0.0, 0.0000002));
		assertTrue(degreesEqual(0.0, 0.000000999999));
	}

}
