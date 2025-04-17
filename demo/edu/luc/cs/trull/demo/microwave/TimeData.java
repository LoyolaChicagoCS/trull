package edu.luc.cs.trull.demo.microwave;

/**
 * An event representing a time in seconds.
 */

public class TimeData {

    int val;

    public TimeData(int val) { this.val = val; }

    public int getValue() { return val; }

    public String toString() { return "TimeData[val=" + val + "]"; }
}
