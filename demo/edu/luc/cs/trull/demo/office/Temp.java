package edu.luc.cs.trull.demo.office;

/**
 * A representation of a temperature.
 */
public class Temp {

  public final static int INITIAL_ACTUAL_TEMP = 20;
  public final static int INITIAL_TARGET_TEMP = 20;
  public final static int INITIAL_ECONOMY_TEMP = 16;
  public final static int MAX_TEMP = 40;
  public final static int MIN_TEMP = 0;

  private int temp;

  public Temp(int t) {
      temp = t;
  }

  public Temp() {
      this(0);
  }

  public synchronized void setTemp(int t) {
      temp = t;
  }

  public synchronized int getTemp() {
      return temp;
  }
}
