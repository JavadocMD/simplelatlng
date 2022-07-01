# Introduction

**SimpleLatLng** provides a simple, lightweight library for common latitude and longitude calculation needs in Java.

With this library, you get the following:

1. an object to represent a Latitude/Longitude point,
1. methods to calculate the approximate distance between two points,
1. classes that specify windows of varying shapes for testing whether or not a point lies within the window,
1. a [Geohash](http://en.wikipedia.org/wiki/Geohash) implementation.

In short, if you have looked at other geo-spatial libraries in Java and thought "this is way more than I need", this library might be for you!

## Usage

[See the wiki for basic usage.](https://github.com/JavadocMD/simplelatlng/wiki)

## License

**SimpleLatLng** is licensed through the commercial-friendly [Apache 2.0 License](http://www.apache.org/licenses/LICENSE-2.0)

## Download

Binaries can be downloaded directly from the [releases page](http://github.com/JavadocMD/simplelatlng/releases).

SimpleLatLng is also available from the [Maven central repositories](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.javadocmd%22%20AND%20a%3A%22simplelatlng%22):

```
<dependency>
  <groupId>com.javadocmd</groupId>
  <artifactId>simplelatlng</artifactId>
  <version>RELEASE</version>
</dependency>
```

## Disclaimer

At this point no claims are made as to the accuracy of the calculations
provided by this library. They are based upon approximations and your
results may differ from tested results. However, this project makes a best
effort to produce results that are "good enough" for most uses, and/or
tune-able for your particular circumstances. The only suggestion is that
you independently evaluate the results of this code for your use-case
before relying upon it in a production environment.

If you would like to contribute to the correctness of the code or the
battery of tests it is run through, the project founder would greatly
appreciate it.
