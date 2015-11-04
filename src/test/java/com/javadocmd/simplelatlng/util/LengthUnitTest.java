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
package com.javadocmd.simplelatlng.util;

import static com.javadocmd.simplelatlng.util.LengthUnit.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class LengthUnitTest {

	private static final double DELTA = 0.000001;

	@Test
	public void testKilometerConversions() {
		assertEquals(10.0, KILOMETER.convertTo(KILOMETER, 10.0), DELTA);
		assertEquals(-10.0, KILOMETER.convertTo(KILOMETER, -10.0), DELTA);
		assertEquals(0, KILOMETER.convertTo(KILOMETER, 0), DELTA);
		assertTrue(Double.isInfinite(KILOMETER.convertTo(KILOMETER,
				Double.POSITIVE_INFINITY)));
	}
	
	@Test
	public void testSameUnitConversions() {
		assertEquals(5.7, MILE.convertTo(MILE, 5.7), DELTA);
		
		assertEquals(-3.758, NAUTICAL_MILE.convertTo(NAUTICAL_MILE, -3.758), DELTA);
		
		assertEquals(981723, ROD.convertTo(ROD, 981723), DELTA);
		
		assertEquals(0.000078, METER.convertTo(METER, 0.000078), DELTA);
	}
	
	@Test
	public void testSingleConversions() {
		assertEquals(1000.0, KILOMETER.convertTo(METER, 1.0), DELTA);
		
		assertEquals(0.6213712, KILOMETER.convertTo(MILE, 1.0), DELTA);
		
		assertEquals(0.5399568, KILOMETER.convertTo(NAUTICAL_MILE, 1.0), DELTA);
		
		assertEquals(198.8387815, KILOMETER.convertTo(ROD, 1.0), DELTA);
	}

	@Test
	public void testDoubleConversions() {
		assertEquals(0.8689762, MILE.convertTo(NAUTICAL_MILE, 1.0), DELTA);
		
		assertEquals(13.5788400, ROD.convertTo(METER, 2.7), DELTA);
		
		assertTrue(Double.isInfinite(MILE.convertTo(METER, Double.POSITIVE_INFINITY)));
	}

	@Test
	public void testPrimaryIsScaleOfOne() {
		assertEquals(1.0, PRIMARY.getScaleFactor(), DELTA);
	}

}
