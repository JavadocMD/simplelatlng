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
import java.util.function.Predicate;
import java.util.regex.Pattern;

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
	public static final Predicate<String> isGeohash = Pattern
			.compile("^[0-9bcdefghjkmnpqrstuvwxyz]+$", Pattern.CASE_INSENSITIVE).asPredicate();

	private static final int BITS = ((PRECISION * 5) / 2) + PRECISION % 2;
	private static final char[] HASH_CHARS_ARRAY = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'b',
			'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };
	protected static final int[] CHAR_VALUE = new int[(int) 'z' + 1];
	protected static final double[] LAT_BIT_VALUES = new double[BITS];
	protected static final double[] LNG_BIT_VALUES = new double[BITS];
	private static final double MAX_LAT = 90.0;
	private static final double MAX_LNG = 180.0;

	static {
		double latValue = MAX_LAT;
		double lngValue = MAX_LNG;
		for (int i = 0; i < BITS; i++) {
			latValue = latValue / 2d;
			lngValue = lngValue / 2d;
			LAT_BIT_VALUES[i] = latValue;
			LNG_BIT_VALUES[i] = lngValue;
		}

		for (int i = 0; i < HASH_CHARS_ARRAY.length; i++) {
			char c = HASH_CHARS_ARRAY[i];
			char d = Character.toUpperCase(c);
			CHAR_VALUE[c] = i;
			CHAR_VALUE[d] = i;
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
		// truncate hashes that are longer than supported
		int charLength = Math.min(hash.length(), PRECISION);
		hash = hash.substring(0, charLength);
		if (!isGeohash.test(hash)) {
			throw new IllegalArgumentException("Geohash string contains invalid characters.");
		}

		int bitSize = charLength * 5;
		int latSize = bitSize / 2;
		int lngSize = bitSize / 2 + bitSize % 2;
		DeInterleaveResult bits = deInterleave(bitSize, hashToBits(hash));
		double latd = bitsToDouble(latSize, bits.lat, LAT_BIT_VALUES);
		double lngd = bitsToDouble(lngSize, bits.lng, LNG_BIT_VALUES);
		return new LatLng(latd, lngd);
	}

	/**
	 * Converts a hash string into a series of bits.
	 * 
	 * @param hash the geohash string.
	 * @return the bits in the geohash.
	 */
	protected static long hashToBits(String hash) {
		long bits = 0;
		for (char c : hash.toCharArray()) {
			bits = (bits << 5) | CHAR_VALUE[c];
		}
		return bits;
	}

	protected static class DeInterleaveResult {
		public final long lat;
		public final long lng;

		public DeInterleaveResult(long lat, long lng) {
			this.lat = lat;
			this.lng = lng;
		}
	}

	/**
	 * De-interleaves a series of bits.
	 * 
	 * @param bitSize the number of bits in our hash.
	 * @param bits    the bits in the geohash.
	 */
	protected static DeInterleaveResult deInterleave(int bitSize, long bits) {
		long a = 0;
		long b = 0;
		for (int i = 0; i < bitSize; i++) {
			long write = 1L << i;
			a |= (bits >> i) & write;
			b |= (bits >> (i + 1)) & write;
		}
		if (bitSize % 2 == 0) {
			return new DeInterleaveResult(a, b);
		} else {
			return new DeInterleaveResult(b, a);
		}
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
	protected static double bitsToDouble(int bitSize, long bits, double[] bitValues) {
		// assumes bitSize > 1; realistically 5 is the least we would expect
		double value = 0;
		double lastValue = value;

		for (int i = 0; i < bitSize; i++) {
			lastValue = value;
			long read = 1L << (bitSize - i - 1);
			if ((bits & read) == 0) {
				value -= bitValues[i];
			} else {
				value += bitValues[i];
			}
		}

		// Round the result.
		// The spec requires that our final result lie within the final step's min/max
		// values. Using fixed degrees precision as we do, it turns out that's not
		// always possible.
		// So we'll try rounding half-up, then up, and finally default to down. This
		// also implies this geohashing implementation is not round-trip stable for all
		// values.
		final BigDecimal unrounded = new BigDecimal(value);
		double lastBounds = bitValues[bitSize - 2];
		final BigDecimal min = new BigDecimal(lastValue - lastBounds);
		final BigDecimal max = new BigDecimal(lastValue + lastBounds);
		// Try half-up rounding...
		BigDecimal x = unrounded.setScale(6, RoundingMode.HALF_UP);
		if (x.compareTo(min) >= 0 && x.compareTo(max) <= 0) {
			return x.doubleValue();
		}
		// If that's not in the min/max range, try rounding up...
		x = unrounded.setScale(6, RoundingMode.UP);
		if (x.compareTo(min) >= 0 && x.compareTo(max) <= 0) {
			return x.doubleValue();
		}
		// Finally fall-back to rounding down.
		x = unrounded.setScale(6, RoundingMode.DOWN);
		return x.doubleValue();
	}

	/**
	 * Geohashes a latitude and longitude.
	 * 
	 * @param point the point to hash.
	 * @return the hash string to the set character precision: {@link #PRECISION}.
	 */
	public static String hash(LatLng point) {
		long lat = doubleToBits(BITS, point.getLatitude(), MAX_LAT);
		long lng = doubleToBits(BITS, point.getLongitude(), MAX_LNG);
		long bits = interleave(BITS * 2, lat, lng);
		return bitsToHash(BITS * 2, bits);
	}

	/**
	 * Encodes an interleaved set of bits to its base-32 geohash.
	 * 
	 * @param bits the set of bits to encode.
	 * @return the encoded string.
	 */
	protected static String bitsToHash(int bitSize, long bits) {
		StringBuilder hash = new StringBuilder();
		for (int i = bitSize - 5; i >= 0; i -= 5) {
			int value = (int) (bits >> i) & 0b11111;
			hash.append(HASH_CHARS_ARRAY[value]);
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
	protected static long interleave(int bitSize, long lat, long lng) {
		long a = lat;
		long b = lng;
		if (bitSize % 2 == 1) {
			a = lng;
			b = lat;
		}

		long bits = 0;
		for (int i = 0; i < bitSize; i++) {
			bits |= (a & 1L) << i;
			long tmp = b;
			b = a >> 1;
			a = tmp;
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
	protected static long doubleToBits(int bitSize, double value, double maxRange) {
		long bits = 0;

		double maxValue = maxRange;
		double minValue = -maxRange;
		double midValue;
		for (int i = bitSize - 1; i >= 0; i--) {
			midValue = (maxValue + minValue) / 2.0d;
			if (value >= midValue) {
				bits |= 1L << i;
				minValue = midValue;
			} else {
				// bits |= 0L << i; (which is a no-op)
				maxValue = midValue;
			}
		}

		return bits;
	}
}
