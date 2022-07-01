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

import java.util.Comparator;

/**
 * This class wraps another class in order to attach a distance value to it. A
 * list of SortWrappers may then be easily sorted.
 * 
 * @param <E> the type that SortWrapper is wrapping.
 */
public class SortWrapper<E> {

	private E value;
	private double distance;

	public SortWrapper(E value, double distance) {
		this.value = value;
		this.distance = distance;
	}

	public E getValue() {
		return value;
	}

	public double getDistance() {
		return distance;
	}

	/**
	 * Compares SortWrappers by their associated distance value.
	 */
	public static class DistanceComparator<E> implements Comparator<SortWrapper<E>> {

		@Override
		public int compare(SortWrapper<E> o1, SortWrapper<E> o2) {
			return Double.compare(o1.getDistance(), o2.getDistance());
		}
	}
}
