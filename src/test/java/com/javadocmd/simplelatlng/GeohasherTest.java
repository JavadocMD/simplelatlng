/* Copyright 2010 Tyler Coles
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. */
package com.javadocmd.simplelatlng;

import static com.javadocmd.simplelatlng.Geohasher.bitsToDouble;
import static com.javadocmd.simplelatlng.Geohasher.deInterleave;
import static com.javadocmd.simplelatlng.Geohasher.hashToBits;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.javadocmd.simplelatlng.Geohasher.DeInterleaveResult;

public class GeohasherTest {

	@Test
	public void testHash1() {
		assertEquals("s00000000000", Geohasher.hash(new LatLng(0, 0)));
	}

	@Test
	public void testDecode1() {
		assertEquals(new LatLng(0, 0), Geohasher.decode("s00000000000"));
	}

	@Test
	public void testHash2() {
		LatLng p = new LatLng(44.869797, 6.599944);
		assertEquals("spuxq0mctb6u", Geohasher.hash(p));
	}

	@Test
	public void testDecode2() {
		LatLng p = new LatLng(44.869797, 6.599944);
		assertEquals(p, Geohasher.decode("spuxq0mctb6u"));
	}

	@Test
	public void testHash3() {
		LatLng p = new LatLng(44.86979, 6.59994);
		assertEquals("spuxq0mcmxpg", Geohasher.hash(p));
	}

	@Test
	public void testDecode3() {
		LatLng p = new LatLng(44.86979, 6.59994);
		assertEquals(p, Geohasher.decode("spuxq0mcmxpg"));
	}

	@Test
	public void testHash4() {
		LatLng p = new LatLng(44.86978, 6.59994);
		assertEquals("spuxq0mcmtpz", Geohasher.hash(p));
	}

	@Test
	public void testDecode4() {
		LatLng p = new LatLng(44.86978, 6.59994);
		assertEquals(p, Geohasher.decode("spuxq0mcmtpz"));
	}

	@Test
	public void testHash5() {
		LatLng p = new LatLng(44, 6);
		assertEquals("spu629cnd4kr", Geohasher.hash(p));
	}

	@Test
	public void testDecode5() {
		LatLng p = new LatLng(44, 6);
		assertEquals(p, Geohasher.decode("spu629cnd4kr"));
	}

	@Test
	public void testHash6() {
		LatLng p = new LatLng(1, 1);
		assertEquals("s00twy01mtw0", Geohasher.hash(p));
	}

	@Test
	public void testDecode6() {
		LatLng p = new LatLng(1, 1);
		assertEquals(p, Geohasher.decode("s00twy01mtw0"));
	}

	@Test
	public void testDecode7() {
		// Test long inputs.
		LatLng p = new LatLng(1, 1);
		assertEquals(p, Geohasher.decode("s00twy01mtw0bbbbbbbbbbbbbbbb"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDecode8() {
		// Test empty input.
		Geohasher.decode("");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDecode9() {
		// Test null input.
		Geohasher.decode(null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testDecode10() {
		// Test invalid characters input.
		Geohasher.decode("this input is invalid");
	}

	@Test
	public void testHashToBits() {
		long bits = hashToBits("ezs42");
		assertEquals(0b0110111111110000010000010L, bits);
	}

	public void testDeIterleave() {
		DeInterleaveResult bits0 = deInterleave(10, 0b1010101010);
		assertEquals(0b00000, bits0.lat);
		assertEquals(0b11111, bits0.lng);
		// e -> 01101 | lng: 011, lat: 10
		DeInterleaveResult bits1 = deInterleave(5, hashToBits("e"));
		assertEquals(0b10, bits1.lat);
		assertEquals(0b011, bits1.lng);
		// ez -> 01101 11111 | lng: 01111, lat: 10111
		DeInterleaveResult bits2 = deInterleave(10, hashToBits("ez"));
		assertEquals(0b10111, bits2.lat);
		assertEquals(0b01111, bits2.lng);
	}

	@Test
	public void testBitsToDouble0() {
		final double[] V = Geohasher.LAT_BIT_VALUES;
		final double D = 0.001;
		// assertEquals(45d, bitsToDouble(1, 0b1L, V), D);
		assertEquals(22.5d, bitsToDouble(2, 0b10L, V), D);
		assertEquals(33.75d, bitsToDouble(3, 0b101L, V), D);
		assertEquals(39.375d, bitsToDouble(4, 0b1011L, V), D);
		assertEquals(42.188d, bitsToDouble(5, 0b10111L, V), D);
		assertEquals(43.594d, bitsToDouble(6, 0b101111L, V), D);
		assertEquals(42.891d, bitsToDouble(7, 0b1011110L, V), D);
		assertEquals(42.539d, bitsToDouble(8, 0b10111100L, V), D);
		assertEquals(42.715d, bitsToDouble(9, 0b101111001L, V), D);
		assertEquals(42.627d, bitsToDouble(10, 0b1011110010L, V), D);
		assertEquals(42.583d, bitsToDouble(11, 0b10111100100L, V), D);
		assertEquals(42.605d, bitsToDouble(12, 0b101111001001L, V), D);
	}

	@Test
	public void testHashToBits1() {
		long bits = hashToBits("ezs42");
		assertEquals(0b0110111111110000010000010L, bits);
		assertEquals(14672002L, bits);
	}

	@Test
	public void testDeInterleave1() {
		DeInterleaveResult bits = deInterleave(25, 0b0110111111110000010000010L);
		assertEquals(0b101111001001L, bits.lat);
		assertEquals(0b0111110000000L, bits.lng);
	}

	@Test
	public void testBitsToDouble1() {
		int n = "ezs42".length() * 5;
		int latSize = n / 2;
		int lngSize = n / 2 + n % 2;
		double lat = bitsToDouble(latSize, 0b101111001001L, Geohasher.LAT_BIT_VALUES);
		assertEquals(42.605, lat, 0.001);
		double lng = bitsToDouble(lngSize, 0b0111110000000L, Geohasher.LNG_BIT_VALUES);
		assertEquals(-5.603, lng, 0.001);
	}

	@Test
	public void testHashToBits2() {
		long bits = hashToBits("ezs42ebpbpbm");
		long expected = 0b011011111111000001000001001101010101010101010101010101010011L;
		assertEquals(expected, bits);
	}

	@Test
	public void testDeInterleave2() {
		DeInterleaveResult bits = deInterleave(60, hashToBits("ezs42ebpbpbm"));
		assertEquals(0b101111001001011111111111111101L, bits.lat);
		assertEquals(0b011111000000010000000000000001L, bits.lng);
	}

	@Test
	public void testBitsToDouble2() {
		DeInterleaveResult bits = deInterleave(60, hashToBits("ezs42ebpbpbm"));
		double lat = bitsToDouble(30, bits.lat, Geohasher.LAT_BIT_VALUES);
		assertEquals(42.604980, lat, 0.000001);
		double lng = bitsToDouble(30, bits.lng, Geohasher.LNG_BIT_VALUES);
		assertEquals(-5.603027, lng, 0.000001);
	}

	@Test
	public void testDecodeHash1() {
		LatLng p = new LatLng(42.604980, -5.603027);
		assertEquals(p, Geohasher.decode("ezs42"));
		assertEquals("ezs42ebpbpbm", Geohasher.hash(p));
		assertEquals(p, Geohasher.decode("ezs42ebpbpbm"));
	}
}
