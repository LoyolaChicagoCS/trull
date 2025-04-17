package edu.luc.cs.trull.demo.stopwatch;

import edu.luc.cs.trull.demo.wristwatch.DateConstants;

/**
 * The passive data model of the stopwatch.
 */
public class Application implements DateConstants {

  private int secVal = 0;

  public void resetRuntime(){
    secVal = 0;
  }

  public void incRuntime(){
    secVal = (secVal + SEC_PER_TICK) % SEC_PER_HOUR;
  }

  public int getRuntime() {
  	return secVal;
  }
}