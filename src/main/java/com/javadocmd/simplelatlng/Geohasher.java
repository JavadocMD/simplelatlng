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
package com.javadocmd.simplelatlng;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements the <a href="http://en.wikipedia.org/wiki/Geohash">Geohash</a>
 * algorithm for hashing latitude and longitude points. Note: this
 * implementation is only "stable" with 12-character hashes. Decoding "s" and
 * re-hashing the result yields "t40000000000". Decoding and re-hashing
 * "t40000000000" yields the same. 12 characters was chosen because this gives
 * us precision up to one-millionth of a degree, like the rest of this library.
 */
public class Geohasher {

	/**
	 * <p>
	 * Number of hash characters supported.
	 * </p>
	 * <p>
	 * Translates to binary bits per value by the formula:
	 * </p>
	 * 
	 * <pre>
	 * BITS = ((PRECISION * 5) / 2) + PRECISION % 2.
	 * </pre>
	 * <p>
	 * BITS in turn translates to numerical precision by the formula:
	 * </p>
	 * 
	 * <pre>
	 * LATITUDE_ERROR = 90.0 / (2 ^ (BITS + 1))
	 * LONGITUDE_ERROR = 180.0 / (2 ^ (BITS + 1))
	 * </pre>
	 */
	public static final int PRECISION = 12;
	private static final int BITS = ((PRECISION * 5) / 2) + PRECISION % 2;
	private static final double MAX_LAT = 90.0;
	private static final double MAX_LNG = 180.0;
	private static final char[] HASH_CHARS_ARRAY = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	private static final Map<Character, Integer> HASH_CHARS_MAP;
	protected static final BigDecimal[] LAT_BIT_VALUES;
	protected static final BigDecimal[] LNG_BIT_VALUES;
	static {
		BigDecimal latValue = new BigDecimal(MAX_LAT);
		BigDecimal lngValue = new BigDecimal(MAX_LNG);
		LAT_BIT_VALUES = new BigDecimal[BITS];
		LNG_BIT_VALUES = new BigDecimal[BITS];

		BigDecimal TWO = new BigDecimal("2");
		for (int i = 0; i < BITS; i++) {
			latValue = latValue.divide(TWO);
			lngValue = lngValue.divide(TWO);
			LAT_BIT_VALUES[i] = latValue;
			LNG_BIT_VALUES[i] = lngValue;
		}

		HASH_CHARS_MAP = new HashMap<Character, Integer>(HASH_CHARS_ARRAY.length);
		for (int i = 0; i < HASH_CHARS_ARRAY.length; i++) {
			HASH_CHARS_MAP.put(HASH_CHARS_ARRAY[i], i);
		}
	}

	/**
	 * Decodes a geohash string to its LatLng equivalent.
	 * 
	 * @param hash the geohash string of any precision, although LatLng will still
	 *             not become <em>more</em> precise than its settings.
	 * @return the decoded point.
	 * @throws IllegalArgumentException on null or empty strings, or strings which
	 *                                  contain invalid geohash characters:
	 *                                  [0-9bcdefghjkmnpqrstuvwxyz]
	 */
	public static LatLng decode(String hash) {
		if (hash == null || hash.isEmpty()) {
			throw new IllegalArgumentException("Geohash string cannot be empty or null.");
		}
		int n = Math.min(hash.length(), PRECISION); // truncate hashes that are longer than supported
		BitSet[] b = deInterleave(hashToBits(hash.substring(0, n).toLowerCase()));
		double lat = bitsToDouble(b[1], LAT_BIT_VALUES);
		double lng = bitsToDouble(b[0], LNG_BIT_VALUES);
		return new LatLng(lat, lng);
	}

	/**
	 * Converts a hash string into a series of bits.
	 * 
	 * @param hash the geohash string.
	 * @return the bits in the geohash.
	 */
	protected static BitSet hashToBits(String hash) {
		try {
			BitSet bits = new BitStore();

			int offset = (hash.length() - 1) * 5;
			for (int i = 0; i < hash.length(); i++) {
				int value = HASH_CHARS_MAP.get(hash.charAt(i)).intValue();
				for (int x = 0; x < 5; x++) {
					bits.set(offset + x, (value & 0x1) == 0x1);
					value >>= 1;
				}
				offset -= 5;
			}

			return bits;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException("Geohash string contains invalid characters.");
		}
	}

	/**
	 * De-interleaves a series of bits.
	 * 
	 * @param bits the bits to de-interleave.
	 * @return two bit sets: [0] = even bits, [1] = odd bits
	 */
	protected static BitSet[] deInterleave(BitSet bits) {
		BitSet[] sets = new BitSet[] { new BitStore(), new BitStore() };

		int n = bits.size();
		for (int i = 0; i < n; i++) {
			sets[i % 2].set((n - i - 1) / 2, bits.get(n - i - 1));
		}

		return sets;
	}

	/**
	 * Converts the set of bits representing a single value to double. The bit set
	 * passed into this function should already be de-interleaved.
	 * 
	 * @param bits      the bits for this value.
	 * @param bitValues the correct set of pre-computed bit-values to use for the
	 *                  particular value we are decoding: latitude or longitude.
	 * @return the value.
	 */
	protected static double bitsToDouble(BitSet bits, BigDecimal[] bitValues) {
		BigDecimal value = BigDecimal.ZERO;
		BigDecimal lastValue = value;
		int n = bits.size();
		for (int i = 0; i < n; i++) {
			lastValue = value;
			if (bits.get(i)) {
				value = value.add(bitValues[n - i - 1]);
			} else {
				value = value.subtract(bitValues[n - i - 1]);
			}
		}

		BigDecimal lastDelta2x = bitValues[n - 1].multiply(new BigDecimal(2));
		BigDecimal roundingMin = lastValue.subtract(lastDelta2x);
		BigDecimal roundingMax = lastValue.add(lastDelta2x);

		BigDecimal rounded = value.setScale(6, RoundingMode.HALF_UP);
		if (rounded.compareTo(roundingMin) < 0 || rounded.compareTo(roundingMax) > 0) {
			rounded = value.setScale(6, RoundingMode.HALF_DOWN);
		}
		return rounded.doubleValue();
	}

	/**
	 * Geohashes a latitude and longitude.
	 * 
	 * @param point the point to hash.
	 * @return the hash string to the set character precision: {@link #PRECISION}.
	 */
	public static String hash(LatLng point) {
		BitSet lat = doubleToBits(point.getLatitude(), MAX_LAT);
		BitSet lng = doubleToBits(point.getLongitude(), MAX_LNG);
		return bitsToHash(interleave(lng, lat));
	}

	/**
	 * Encodes an interleaved set of bits to its base-32 geohash.
	 * 
	 * @param bits the set of bits to encode.
	 * @return the encoded string.
	 */
	protected static String bitsToHash(BitSet bits) {
		StringBuilder hash = new StringBuilder();
		for (int i = 0; i < bits.size(); i += 5) {
			int value = 0;
			for (int j = 0; j < 5; j++) {
				if (bits.get(i + j)) {
					value |= (0x1 << j);
				}
			}
			hash.insert(0, HASH_CHARS_ARRAY[value]);
		}
		return hash.toString();
	}

	/**
	 * Interleaves two sets of bits.
	 * 
	 * @param evenBits the bits to use for even bits. (0, 2, 4,...)
	 * @param oddBits  the bits to use for odd bits. (1, 3, 5,...)
	 * @return the interleaved bits.
	 */
	protected static BitSet interleave(BitSet evenBits, BitSet oddBits) {
		int n = evenBits.size() + oddBits.size();
		BitSet bits = new BitStore();
		for (int i = 0; i < n; i++) {
			if (i % 2 == 0) {
				bits.set(n - i - 1, evenBits.get((n - i - 1) / 2));
			} else {
				bits.set(n - i - 1, oddBits.get((n - i - 1) / 2));
			}
		}
		return bits;
	}

	/**
	 * Converts a double value to its bit representation in the geohash
	 * specification.
	 * 
	 * @param value    the value to encode.
	 * @param maxRange the max range for the particular value we are encoding:
	 *                 latitude = 90.0, longitude = 180.0.
	 * @return the bit set for this value.
	 */
	protected static BitSet doubleToBits(double value, double maxRange) {
		BitSet bits = new BitStore();

		double maxValue = maxRange;
		double minValue = -maxRange;
		double midValue;
		for (int i = 0; i < BITS; i++) {
			midValue = (maxValue + minValue) / 2.0;
			if (value >= midValue) {
				bits.set(BITS - i - 1);
				minValue = midValue;
			} else {
				bits.set(BITS - i - 1, false);
				maxValue = midValue;
			}
		}

		return bits;
	}

	/**
	 * Specialization of BitSet to <em>actually</em> keep track of the number of
	 * bits that are being usefully employed, regardless of whether or not they are
	 * 1 or 0. This requires that you call set for 0's <em>and</em> 1's. Not all
	 * features are implemented, but setting, getting, and size work fine, which is
	 * all I need for this class.
	 */
	protected static class BitStore extends BitSet {

		private static final long serialVersionUID = 4630759467120792604L;
		private int highestBit = -1;

		private void updateHighestBit(int bitIndex) {
			if (bitIndex > highestBit)
				highestBit = bitIndex;
		}

		public String toString() {
			String s = "";
			for (int i = 0; i < size(); i++)
				s = (get(i) ? "1" : "0") + s;
			return s;
		}

		@Override
		public void set(int bitIndex) {
			super.set(bitIndex);
			updateHighestBit(bitIndex);
		}

		@Override
		public void set(int bitIndex, boolean value) {
			super.set(bitIndex, value);
			updateHighestBit(bitIndex);
		}

		@Override
		public void set(int fromIndex, int toIndex) {
			super.set(fromIndex, toIndex);
			updateHighestBit(toIndex);
		}

		@Override
		public void set(int fromIndex, int toIndex, boolean value) {
			super.set(fromIndex, toIndex, value);
			updateHighestBit(toIndex);
		}

		@Override
		public void flip(int bitIndex) {
			super.flip(bitIndex);
			updateHighestBit(bitIndex);
		}

		@Override
		public void flip(int fromIndex, int toIndex) {
			super.flip(fromIndex, toIndex);
			updateHighestBit(toIndex);
		}

		@Override
		public int size() {
			return highestBit + 1;
		}
	}
}
