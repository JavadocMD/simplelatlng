package simplelatlng.window;

import simplelatlng.LatLng;

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