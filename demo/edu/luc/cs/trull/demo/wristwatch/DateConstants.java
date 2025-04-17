package edu.luc.cs.trull.demo.wristwatch;

/**
 * Constants used in the calculation of times and dates.
 */
public interface DateConstants {
	int SEC_PER_TICK    = 1;
	int SEC_PER_MIN     = 60;
	int MIN_PER_HOUR    = 60;
	int HOURS_PER_DAY   = 24;
	int SEC_PER_HOUR    = MIN_PER_HOUR * SEC_PER_MIN;
	int SEC_PER_DAY     = HOURS_PER_DAY * SEC_PER_HOUR;
	int MONTHS_PER_YEAR = 12;
}
