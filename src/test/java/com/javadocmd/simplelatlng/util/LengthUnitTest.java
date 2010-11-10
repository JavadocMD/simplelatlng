package com.javadocmd.simplelatlng.util;

import static com.javadocmd.simplelatlng.util.LengthUnit.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class LengthUnitTest {

	@Test
	public void testConvertTo() {
		assertEquals(10.0, KILOMETER.convertTo(KILOMETER, 10.0), 0.000001);
		assertEquals(-10.0, KILOMETER.convertTo(KILOMETER, -10.0), 0.000001);
		assertEquals(0, KILOMETER.convertTo(KILOMETER, 0), 0.000001);
		assertTrue(Double.isInfinite(KILOMETER.convertTo(KILOMETER,
				Double.POSITIVE_INFINITY)));

		assertEquals(0.6213712, KILOMETER.convertTo(MILE, 1.0), 0.000001);
		assertEquals(0.5399568, KILOMETER.convertTo(NAUTICAL_MILE, 1.0), 0.000001);
		assertEquals(0.0050292, KILOMETER.convertTo(ROD, 1.0), 0.000001);

		assertEquals(0.8689762, MILE.convertTo(NAUTICAL_MILE, 1.0), 0.000001);
	}

	@Test
	public void testGetScaleFactor() {
		assertEquals(1.0, PRIMARY.getScaleFactor(), 0.000001);
	}

}
