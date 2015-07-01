/*
 * Classification: UNCLASSIFIED
 *
 * Source File: Constant.java
 *
 * Contractor: SAIC
 *             1334 Ashton Road
 *             Hanover, MD 21076
 *             (410) 850-5000 (Voice)
 *             (410) 850-5027 (Fax)
 *
 * Copyright (c) 2002. All Rights Reserved.
 * This software is the confidential and proprietary information of
 * SAIC.  You shall not disclose such Confidential Information and shall
 * use it only in accordance with the terms of the license agreement you
 * entered into with SAIC.
 *
 * Modification History:
 *  Date      Developer         Ref #     Description
 *  2002AUG07 S. Lindberg       SAIC 0000 Initial Configuration
 *  2002SEP10 S. Lindberg       SAIC 0000 Correct for coding standards
 *  2002SEP12 S. Lindberg       SAIC 0000 Add min & max Earth Elevation &
 *                                        bottom ocean elevation.
 *  2002SEP24 K. Vig            SAIC 0000 Added the square root of 2
 *  2003APR21 S. Lindberg       CAPS 1058 Update EARTH_RADIUS to match MGRS
 *  2003JUL13 JT                CAPS 2091 Added DEG_EPSILON
 *  2003AUG06 S. Lindberg       CAPS 2496 Add PIx3_2 (270 degrees)
 *  2004SEP02 KV                CAPS 4083 Cleanup excess classes
 */

package com.saic.rtws.commons.util;

/**
 * This class is made up of constants used in various mathematical conversions.
 * All class members are accessible via public static final keywords to ensure
 * that their usage in the software generates the highest performing
 * calculations possible based on compiler optimizations.  These constants also
 * ensure a common set of values are used throughout the application.
 *
 * @see java.lang.StrictMath StrictMath
 */
public final class Constant {
	
	/** Constructor. */
	protected Constant() { }
	
	/** 1.5 .*/
	private static final double MAGIC_NUMBER_ONE_POINT_FIVE = 1.5;
	/** 1.943846 .*/
	private static final double MAGIC_NUMBER_ONE_TO_NMPH = 1.943846;
	/** 2.0 .*/
	private static final double MAGIC_NUMBER_TWO = 2.0;
	/** 4.0 .*/
	private static final double MAGIC_NUMBER_FOUR = 4.0;
	/** 8.0 .*/
	private static final double MAGIC_NUMBER_EIGHT = 8.0;
	/** 60.0 .*/
	private static final double MAGIC_NUMBER_SIXTY = 60.0;
	/** 180.0 .*/
	private static final double MAGIC_NUMBER_ONE_HUNDRED_EIGHTY = 180.0;
	/** 10800.0 .*/
	private static final double MAGIC_NUMBER_TEN_THOUSAND_EIGHT_HUNDRED = 10800.0;
	/** 20001600.0 .*/
	private static final double MAGIC_NUMBER_TWENTY_MILLION_SIXTEEN_HUNDRED = 20001600.0;
	
    /** Conversion factor from Hours to Minutes (minutes/hour). */
    public static final double MINUTES_PER_HOUR = 60.0;

    /** Conversion factor from Days to Hours (hours/day). */
    public static final double HOURS_PER_DAY = 24.0;

    /** Conversion factor from Hours to Seconds (seconds/hour). */
    public static final double SECONDS_PER_HOUR = 3600.0;

    /** Conversion factor from Days to Seconds (seconds/day). */
    public static final double SECONDS_PER_DAY = 86400.0;

    /** Closest double to {@link java.lang.StrictMath#PI PI} times 8. */
    public static final double PIx8 = (StrictMath.PI * MAGIC_NUMBER_EIGHT);

    /** Closest double to {@link java.lang.StrictMath#PI PI} times 4. */
    public static final double PIx4 = (StrictMath.PI * MAGIC_NUMBER_FOUR);

    /** Closest double to {@link java.lang.StrictMath#PI PI} times 2. */
    public static final double PIx2 = (StrictMath.PI * MAGIC_NUMBER_TWO);

    /** Closest double to {@link java.lang.StrictMath#PI PI} times 1.5. */
    public static final double PIx3_2 = (StrictMath.PI * MAGIC_NUMBER_ONE_POINT_FIVE);

    /** Closest double to {@link java.lang.StrictMath#PI PI}. */
    public static final double PI = StrictMath.PI;    //  3.141592653589793

    /** Closest double to negative {@link java.lang.StrictMath#PI PI}. */
    public static final double NEGATIVE_PI = -(StrictMath.PI); // -3.141592653589793

    /** Closest double to {@link java.lang.StrictMath#PI PI} divided by 2. */
    public static final double PI_2 = (StrictMath.PI / MAGIC_NUMBER_FOUR);

    /** Closest double to negative {@link java.lang.StrictMath#PI PI} divided
     *  by 2. */
    public static final double NEGATIVE_PI_2 = -(StrictMath.PI / MAGIC_NUMBER_TWO);

    /** Closest double to {@link java.lang.StrictMath#PI PI} divided by 4. */
    public static final double PI_4 = (StrictMath.PI / MAGIC_NUMBER_FOUR);

    /** Closest double to negative {@link java.lang.StrictMath#PI PI} divided by
     *  4. */
    public static final double NEGATIVE_PI_4 = -(StrictMath.PI / MAGIC_NUMBER_FOUR);

    /** Closest double to {@link java.lang.StrictMath#PI PI} divided by 8. */
    public static final double PI_8 = (StrictMath.PI / MAGIC_NUMBER_EIGHT);

    /** Closest double to negative {@link java.lang.StrictMath#PI PI} divided by
     *  8. */
    public static final double NEGATIVE_PI_8 = -(StrictMath.PI / MAGIC_NUMBER_EIGHT);

    /** Closest double to the square root of 2. */
    public static final double SQRT_2 = StrictMath.sqrt(2.0);

    /** Conversion factor from Radians to Degrees (degrees/radian). */
    public static final double DEG_PER_RAD = MAGIC_NUMBER_ONE_HUNDRED_EIGHTY / Constant.PI;

    /** Conversion factor from Degrees to Radians (radians/degree). */
    public static final double RAD_PER_DEG = Constant.PI / MAGIC_NUMBER_ONE_HUNDRED_EIGHTY;

    /** Minimum non-sumberged land elevation on the Earth (meters). */
    public static final double MIN_EARTH_ELEVATION = -1000.0;

    /** Maximum land elevation on the Earth (meters). */
    public static final double MAX_EARTH_ELEVATION = 8850.0; //Mt. Everest, Asia

    /** Minimum submerged land elevation (meters). */
    public static final double BOTTOM_OF_OCEAN = -10924.0; //Pacific Ocean

    /** Mean radius of the earth (meters). */
    public static final double EARTH_RADIUS = 6378137.0;

    /** Mean radius of the earth multiplied by 2 (meters). */
    public static final double EARTH_RADIUSx2 = Constant.EARTH_RADIUS * MAGIC_NUMBER_TWO;

    /** Conversion factor from Nautical Miles to Meters (meters/nautical mile). */
    public static final double METERS_PER_NAUTMILE = 1852.0;

    /** Conversion factor from Meters to Nautical Miles (nautical miles/meter). */
    public static final double NAUTMILES_PER_METER = 1 / METERS_PER_NAUTMILE; //0.00054;

    /** Conversion factor from Meters to Radians (radians/meter). */
    public static final double RADIANS_PER_METER = Constant.PI / MAGIC_NUMBER_TWENTY_MILLION_SIXTEEN_HUNDRED;

    /** Conversion factor from Radians to Meters (meters/radian). */
    public static final double METERS_PER_RADIAN = MAGIC_NUMBER_TWENTY_MILLION_SIXTEEN_HUNDRED / Constant.PI;

    /** Conversion factor from Kilometers to Radians (radians/kilometer). */
    public static final double RADIANS_PER_KILOMETER =
            Constant.RADIANS_PER_METER * Constant.METERS_PER_KILOMETER;

    /** Conversion factor from Nautical Miles to Degrees (degrees/nautical
     *  mile). */
    public static final double DEGREES_PER_NAUTMILE = 1.0 / MAGIC_NUMBER_SIXTY;

    /** Conversion factor from Nautical Miles to Radians (radians/nautical
     *  mile). */
    public static final double RADIANS_PER_NAUTMILE = Constant.PI / MAGIC_NUMBER_TEN_THOUSAND_EIGHT_HUNDRED;

    /** Conversion factor from Nautical Miles to Kilometers
     *  (kilometers/nautical mile). */
    public static final double KILOMETERS_PER_NAUTMILE = 1.852;

    /** Conversion factor from Kilometers to Nautical Miles
     *  (nautical miles/kilometer). */
    public static final double NAUTMILES_PER_KILOMETER = 0.53996;

    /** Conversion factor from Feet to Meters (meters/foot). */
    public static final double METERS_PER_FOOT = 0.3048;

    /** Conversion factor from data mile to feet. */
    public static final double FEET_PER_DATA_MILE = 6000.0;
    
    /** Conversion factor from Meters to Feet (feet/meter). */
    public static final double FEET_PER_METER = 3.28084;

    /** Conversion factor from Nautical Miles to Feet (feet/nautical mile). */
    public static final double FEET_PER_NAUTMILE = 6076.11549;

    /** Conversion factor from Feet to Nautical Miles (nautical miles/foot). */
    public static final double NAUTMILES_PER_FOOT = 0.00016;

    /** Conversion factor from Feet to Kilometers (kilometers/foot). */
    public static final double KILOMETERS_PER_FOOT = 0.0003;

    /** Conversion factor from Kilometers to Feet (feet/kilometer). */
    public static final double FEET_PER_KILOMETER = 3280.8399;

    /** Conversion factor from Kilometers to Meters (meters/kilometer). */
    public static final double METERS_PER_KILOMETER = 1000.0;

    /** Conversion factor from Meters to Kilometers (kilometers/meter). */
    public static final double KILOMETERS_PER_METER = 0.001;

    /** Conversion factor from Miles to Feet. */
    public static final double FEET_PER_MILE = 5280.0;
    
    /** Speed of light in a vacuum (meters per second). */
    public static final double LIGHTSPEED = 299792458;

    /** Conversion factor from Meters per Second to Knots
     *  knots/meter per second). */
    public static final double KNOTS_PER_MPS = 1.94385;

    /** Conversion factor from Knots to Meters per Second
     *  (meters per second/knot). */
    public static final double MPS_PER_KNOT = 1 / KNOTS_PER_MPS;
    //public static final double MPS_PER_KNOT = 0.51444;

    /** Conversion factor from Meters per Second to Kilometers per Hour
     *  (kilometer per hour/meter per second). */
    public static final double KPH_PER_MPS = 3.6;

    /** Conversion factor from Kilometers per Hour to Meters per Second
     *  (meters per second/kilometer per hour). */
    //public static final double MPS_PER_KPH = 0.27778;
    public static final double MPS_PER_KPH = 1 / KPH_PER_MPS;

    /** Conversion factor from Knots to Kilometers per Hour
     *  (kilometer per hour/knot). */
    public static final double KPH_PER_KNOT = 1.852;

    /** Conversion factor from Kilometers per Hour to Knots
     *  (knots/kilometer per hour). */
    public static final double KNOTS_PER_KPH = 0.53996;
    
    /** Conversion factor from Miles per Hour to Kilometers per Hour. */
    public static final double KPH_PER_MPH = 1.609344;
    
    /** Conversion factor from Date Miles per Hour to Kilometers per Hour. */
    public static final double KPH_PER_DMPH = 1.8288;

    /** Conversion factor from Feet per Second to Meters per Second (meters per
     *  second / foot per second). */
    public static final double MPS_PER_FPS = 0.3048;

    /** Conversion factor from Data Miles per Hour to Meters per Second. */
    public static final double FPS_PER_DATA_MPH = FEET_PER_DATA_MILE / SECONDS_PER_HOUR;
    
    /** Conversion factor from MPS to DMPH. */
    public static final double DATA_MPH_PER_FPS = 1 / FPS_PER_DATA_MPH;
    
    /** Conversion factor from Nautical Miles per Hour to Meters per Second
     * (meters per second / nautical miles per hour). */
    public static final double MPS_PER_NMPH = 1.0 / MAGIC_NUMBER_ONE_TO_NMPH;

    /** Conversion factor from Minutes to Seconds (seconds/minute). */
    public static final double SECONDS_PER_MINUTE = 60.0;

    /** Constant definition of Success. */
    public static final boolean SUCCESS = true;

    /** Constant definition of Failure. */
    public static final boolean FAILURE = false;

    /** Closest double to the mathematical constant.
     *  {@link java.lang.StrictMath#E E}*/
    public static final double E = StrictMath.E; // 2.718281828459045

    /** Two doubles differing by less than EPSILON are considered equivalent.*/
    public static final double EPSILON = 0.0000001;

    /** Two angles differing by less than DEG_EPSILON are considered
        equivalent.*/
    public static final double DEG_EPSILON = 0.0000573; // (EPSILON*10)*180/PI
                                     // Don't need as much precision as above
    /** 2 degrees in radians. */
    public static final double TWO_DEG_IN_RAD = MAGIC_NUMBER_TWO * Constant.RAD_PER_DEG;

    /** Integer used for specifying right side.*/
    public static final int RIGHT_SIDE = 1;

    /** Integer used for specifying left side.*/
    public static final int LEFT_SIDE = -1;
}
