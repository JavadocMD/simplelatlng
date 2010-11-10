package com.javadocmd.simplelatlng;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Implements the <a href="http://en.wikipedia.org/wiki/Geohash">Geohash</a> 
 * algorithm for hashing latitude and longitude points. Note: this implementation
 * is only "stable" with 12-character hashes. Decoding "s" and re-hashing the 
 * result yields "t40000000000". Decoding and re-hashing "t40000000000" yields 
 * the same. 12 characters was chosen because this gives us precision up to
 * one-millionth of a degree, like the rest of this library.
 * 
 * @author Tyler Coles
 */
public class Geohasher {

	/**
	 * <p>Number of hash characters supported.</p> 
	 * <p>Translates to binary bits per value by the formula:<br/>
	 * BITS = ((PRECISION * 5) / 2) + PRECISION % 2.</p>
	 * <p>BITS in turn translates to numerical precision
	 * by the formula:<br/>
	 * LATITUDE_ERROR = 90.0 / (2 ^ (BITS + 1))<br/>
	 * LONGITUDE_ERROR = 180.0 / (2 ^ (BITS + 1))</p>
	 */
	public static final int PRECISION = 12;
	private static final int BITS = ((PRECISION * 5) / 2) + PRECISION % 2;
	private static final double MAX_LAT = 90.0;
	private static final double MAX_LNG = 180.0;
	private static final char[] HASH_CHARS_ARRAY = new char[]{'0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'j', 'k', 'm', 'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y',
			'z'};
	private static final Map<Character, Integer> HASH_CHARS_MAP;
	private static final double[] LAT_BIT_VALUES;
	private static final double[] LNG_BIT_VALUES;
	static {
		double latValue = MAX_LAT;
		double lngValue = MAX_LNG;
		LAT_BIT_VALUES = new double[BITS];
		LNG_BIT_VALUES = new double[BITS];
		for (int i = 0; i < BITS; i++) {
			latValue /= 2.0;
			lngValue /= 2.0;
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
	 * @param hash the geohash string of any precision, although LatLng will 
	 * still not become <em>more</em> precise than its settings.
	 * @return the decoded point.
	 */
	public static LatLng decode(String hash) {
		BitSet[] b = deInterleave(hashToBits(hash));
		double lat = bitsToDouble(b[1], LAT_BIT_VALUES);
		double lng = bitsToDouble(b[0], LNG_BIT_VALUES);
		return new LatLng(lat, lng);
	}

	/**
	 * Converts a hash string into a string of bits.
	 */
	private static BitSet hashToBits(String hash) {
		try {
			BitSet bits = new BitStore();

			char[] chars = hash.toLowerCase().toCharArray();
			int offset = (chars.length - 1) * 5;
			for (int i = 0; i < chars.length; i++) {
				int value = HASH_CHARS_MAP.get(chars[i]).intValue();
				for (int x = 0; x < 5; x++) {
					bits.set(offset + x, (value & 0x1) == 0x1);
					value >>= 1;
				}
				offset -= 5;
			}

			return bits;
		} catch (NullPointerException e) {
			throw new IllegalArgumentException(
					"Geohash string contains invalid characters.");
		}
	}

	/**
	 * De-interleaves a series of bits.
	 * 
	 * @param bits the bits to de-interleave.
	 * @return two bit sets: [0] = even bits, [1] = odd bits
	 */
	private static BitSet[] deInterleave(BitSet bits) {
		BitSet[] sets = new BitSet[]{new BitStore(), new BitStore()};

		int n = bits.size();
		for (int i = 0; i < n; i++) {
			sets[i % 2].set(i / 2, bits.get(i));
		}

		return sets;
	}

	/**
	 * Converts the set of bits representing a single value to double.
	 * The bit set passed into this function should already be de-interleaved.
	 * 
	 * @param bits the bits for this value.
	 * @param bitValues the correct set of pre-computed bit-values to use
	 * for the particular value we are decoding: latitude or longitude. 
	 * @return the value.
	 */
	private static double bitsToDouble(BitSet bits, double[] bitValues) {
		double value = 0.0;
		int n = bits.size();
		for (int i = 0; i < n; i++) {
			if (bits.get(n - i - 1)) {
				value += bitValues[i];
			} else {
				value -= bitValues[i];
			}
		}
		return value;
	}

	/**
	 * Geohashes a latitude and longitude.
	 * 
	 * @param point the point to hash.
	 * @return the hash string to the set character precision: {@link #PRECISION}.
	 */
	public static String hash(LatLng point) {
		return bitsToHash(interleave(doubleToBits(point.getLongitude(), MAX_LNG),
				doubleToBits(point.getLatitude(), MAX_LAT)));
	}

	/**
	 * Encodes an interleaved set of bits to its base-32 geohash.
	 * 
	 * @param bits the set of bits to encode.
	 * @return the encoded string.
	 */
	private static String bitsToHash(BitSet bits) {
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
	 * @param oddBits the bits to use for odd bits. (1, 3, 5,...)
	 * @return the interleaved bits.
	 */
	private static BitSet interleave(BitSet evenBits, BitSet oddBits) {
		BitSet bits = new BitStore();
		for (int i = 0; i < (evenBits.size() + oddBits.size()); i++) {
			if (i % 2 == 0) {
				bits.set(i, evenBits.get(i / 2));
			} else {
				bits.set(i, oddBits.get(i / 2));
			}
		}
		return bits;
	}

	/**
	 * Converts a double value to its bit representation in the geohash 
	 * specification.
	 * 
	 * @param value the value to encode.
	 * @param maxRange the max range for the particular value we are
	 * encoding: latitude = 90.0, longitude = 180.0.
	 * @return the bit set for this value.
	 */
	private static BitSet doubleToBits(double value, double maxRange) {
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
	 * Specialization of BitSet to <em>actually</em> keep track of
	 * the number of bits that are being usefully employed, regardless
	 * of whether or not they are 1 or 0. This requires that you call set
	 * for 0's <em>and</em> 1's. Not all features are implemented, but setting, 
	 * getting, and size work fine, which is all I need for this class.
	 * 
	 * @author Tyler Coles
	 */
	private static class BitStore extends BitSet {

		private static final long serialVersionUID = 4630759467120792604L;
		private int highestBit = -1;

		private void updateHighestBit(int bitIndex) {
			if (bitIndex > highestBit)
				highestBit = bitIndex;
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
