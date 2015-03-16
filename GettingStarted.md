# Getting Started #
The SimpleLatLng library is designed to make it as simple as possible for you to do basic latitude and longitude handling. We have purposefully left out some bells and whistles to keep things streamlined so that you can include this in your project and get on with the work you'd rather be doing.

The library is written and compiled to Java 6. After you have included the library jar in your project or added it as a dependency in your Maven POM, you can create a point in latitude and longitude as follows:

```
LatLng point = new LatLng(33.123123, -127.123123);
```

The important thing to note is that all latitudes and longitudes are handled in degrees using the convention that positive latitudes are in the northern hemisphere and positive longitudes are east of the Prime Meridian.

This library generally considers degree measurements to the millionth (0.000001) degree. So 33.0000010 is equal to 33.0000015, but not equal to 33.0000020. This yields a resolution of around 1 centimeter which is far more accurate than the distance calculations require.

When a `LatLng` point is constructed, its latitude and longitude will be normalized to fall within the ±90 degrees latitude, ±180 degree longitude range. Special case: if your `LatLng` is constructed at a pole (±90 degrees latitude), its longitude will be set to 0 because all longitudes intersect at the poles.

## Distance between two points ##

To calculate the distance between two points, use the static methods in `LatLngTool`.

```
LatLng point1 = new LatLng(33.123123, -127.123123);
LatLng point2 = new LatLng(33.321321, -127.321321);
double distanceInMiles = LatLngTool.distance(point1, point2, LengthUnit.MILE);
```

All supported units of length are contained by the enum `LengthUnit`. We will add supported units if there is call for them.