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

import java.util.BitSet;

import org.junit.Test;

import com.javadocmd.simplelatlng.Geohasher.BitStore;

public class GeohasherTest {

	@Test
	public void testHash1() {
		assertEquals("s00000000000", Geohasher.hash(new LatLng(0,0)));
	}

	@Test
	public void testDecode1() {
		assertEquals(new LatLng(0,0), Geohasher.decode("s00000000000"));
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
	public void testHashToBits() {
		BitStore bits = (BitStore) hashToBits("ezs42");
		assertEquals(25, bits.size());
		String expected = "0110111111110000010000010";
		assertEquals(expected, bits.toString());
	}

	@Test
	public void testDeInterleave() {
		BitSet[] bits = deInterleave(hashToBits("ezs42"));
		BitStore evens = (BitStore) bits[0];
		BitStore odds = (BitStore) bits[1];

		assertEquals("0111110000000", evens.toString());
		assertEquals("101111001001", odds.toString());
	}

	@Test
	public void testBitsToDouble() {
		BitSet[] bits = deInterleave(hashToBits("ezs42"));
		double lng = bitsToDouble(bits[0], Geohasher.LNG_BIT_VALUES);
		assertEquals(-5.603, lng, 0.001);
		
		double lat = bitsToDouble(bits[1], Geohasher.LAT_BIT_VALUES);
		assertEquals(42.605, lat, 0.001);
		
	}

	@Test
	public void testHashToBits2() {
		BitStore bits = (BitStore) hashToBits("ezs42ebpbpbm");
		assertEquals(60, bits.size());
		String expected = "011011111111000001000001001101010101010101010101010101010011";
		assertEquals(expected, bits.toString());
	}

	@Test
	public void testDeInterleave2() {
		BitSet[] bits = deInterleave(hashToBits("ezs42ebpbpbm"));
		BitStore evens = (BitStore) bits[0];
		assertEquals("011111000000010000000000000001", evens.toString());
		
		BitStore odds = (BitStore) bits[1];
		assertEquals("101111001001011111111111111101", odds.toString());
	}

	@Test
	public void testBitsToDouble2() {
		BitSet[] bits = deInterleave(hashToBits("ezs42ebpbpbm"));
		double lng = bitsToDouble(bits[0], Geohasher.LNG_BIT_VALUES);
		assertEquals(-5.603027, lng, 0.000001);
		
		double lat = bitsToDouble(bits[1], Geohasher.LAT_BIT_VALUES);
		assertEquals(42.604980, lat, 0.000001);
	}
	
	@Test
	public void testDecodeHash1() {
		 LatLng p = new LatLng(42.604980, -5.603027);
		 assertEquals(p, Geohasher.decode("ezs42"));
		 assertEquals("ezs42ebpbpbm", Geohasher.hash(p));
		 assertEquals(p, Geohasher.decode("ezs42ebpbpbm"));
	}
}
