### v1.3.0 ###
  * Bug fix ([Issue 7](http://code.google.com/p/simplelatlng/issues/detail?id=7)): fixed `Geohasher.hash(LatLng)` rounding and stability issues.
  * Bug fix ([Issue 8](http://code.google.com/p/simplelatlng/issues/detail?id=8)): coordinate formatting is now thread-safe and should look nicer when running in a locale other than the US. The formatting of decimals is configurable through `LatLngConfig`.

### v1.2.0 ###
  * Added `LatLngTool` method to calculate end point given a starting point, initial bearing, and a distance to travel.
  * Added `CircularWindow` method to filter and sort a set of points at the same time without having to calculate distances twice.

### v1.1.0 ###
  * Added `LatLngTool` method to calculate initial bearings on a great-circle course between two points.
  * Added a constructor to `RectangularWindow` so that you can provide the northeast and southwest corner of the window.

### v1.0.1 ###
  * `LatLng` is now `Serializable`.
  * Minor refactoring and a typo fix.

### v1.0.0 ###
  * Bug fixes in `RectangularWindow` construction and `overlaps()` methods.
  * Removed `RectangularWindow`'s `getMinLongitude()` and `getMaxLongitude()` methods. These have been replaced by `getLeftLongitude()` and `getRightLongitude()` because the internal representation has changed. In a circular numerical range, the concepts of minimum and maximum values are too ambiguous and were causing difficulties. However, in reference to a center point, left and right bounds _do_ make sense. Since we do not consider latitude to be a circular range, its usage of min and max is unchanged.
  * Added `RectangularWindow` convenience constructor which creates a square window from one length measurement.
  * Added `METER` to available `LengthUnit`s
  * Additional unit tests for complete test coverage of all non-trivial code.

### v0.1.0 ###
  * Base package changed from `simplelatlng` to `com.javadocmd.simplelatlng` for better adherence to Maven standards.
  * No significant feature additions or bug fixes since v0.0.2.

### v0.0.2 ###
  * Added features to Window, such as easy list filtering.
  * Improved unit testing.
  * Various bug fixes.

### v0.0.1 ###
  * Initial release.