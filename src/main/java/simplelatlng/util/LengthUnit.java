package simplelatlng.util;

public enum LengthUnit {
	MILE(0.6213712), KILOMETER(1.0);

	public static final LengthUnit PRIMARY = KILOMETER;

	private double scaleFactor;

	LengthUnit(double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public double convertTo(LengthUnit toUnit, double value) {
		double _value = value;
		if (this == KILOMETER) {
			if (toUnit == KILOMETER)
				return value;
		} else {
			_value = value / this.scaleFactor; // Convert to kilometers
		}
		return _value * toUnit.scaleFactor; // Convert to destination unit
	}

	public double getScaleFactor() {
		return scaleFactor;
	}
}