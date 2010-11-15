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

import com.javadocmd.simplelatlng.LatLng;

/**
 * An abstract class to help LatLngWindows with the task of 
 * filtering collections based on whether or not a point
 * is contained within the window. Rather than only filter
 * collections of LatLng, this helper enables you to filter
 * any class that has a LatLng value or for which a LatLng
 * value can be computed.<br/><br/>
 * An example of how you would instantiate a helper for 
 * filtering on a collection of type MyObject:<br/>
 * <pre>FilterHelper&lt;MyObject&gt; helper = new FilterHelper&lt;MyObject&gt;() {
 *    &#64;Override
 *    public LatLng getLatLng(MyObject object) {
 *      return object.getLatLng();
 *    }
 * }</pre>
 * 
 * @author Tyler Coles
 *
 * @param <S> the type which this FilterHelper knows how to filter.
 */
public abstract class FilterHelper<S> {

	/**
	 * Returns <code>object</code>'s <code>LatLng</code>
	 * value to be filtered.
	 */
	public abstract LatLng getLatLng(S object);
}