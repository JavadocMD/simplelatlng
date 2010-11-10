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
package com.javadocmd.simplelatlng.util;

/**
 * A utility class for handling units and unit conversions 
 * within this library.
 * 
 * @author Tyler Coles
 */
public enum LengthUnit {
	/**
	 * Miles, using the scale factor 0.6213712 miles per kilometer.
	 */
	MILE(0.6213712),
	/**
	 * Nautical miles, using the scale factor 0.5399568 nautical miles per kilometer.
	 */
	NAUTICAL_MILE(0.5399568),
	/**
	 * Rods, using the scale factor 0.0050292 rods to the kilometer.
	 * Because your car gets forty rods to the hogshead and that's 
	 * they way you likes it.
	 */
	ROD(0.0050292),
	/**
	 * Kilometers, the primary unit.
	 */
	KILOMETER(1.0);

	/**
	 * The primary length unit. All scale factors are relative
	 * to this unit. Any conversion not involving the primary
	 * unit will first be converted to this unit, then to 
	 * the desired unit.
	 */
	public static final LengthUnit PRIMARY = KILOMETER;

	private double scaleFactor;

	LengthUnit(double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	/**
	 * Convert a value of this unit type to the units specified
	 * in the parameters.
	 *  
	 * @param toUnit the unit to convert to.
	 * @param value the value to convert.
	 * @return the converted value.
	 */
	public double convertTo(LengthUnit toUnit, double value) {
		double _value = value;
		if (this == PRIMARY) {
			if (toUnit == PRIMARY)
				return value; // Avoid multiplying by 1.0 needlessly.
		} else {
			_value = value / this.scaleFactor; // Convert to primary unit.
		}
		return _value * toUnit.scaleFactor; // Convert to destination unit.
	}

	/**
	 * Retrieve the scale factor between this unit and the primary 
	 * length unit.
	 * 
	 * @return the scale factor.
	 */
	public double getScaleFactor() {
		return scaleFactor;
	}
}