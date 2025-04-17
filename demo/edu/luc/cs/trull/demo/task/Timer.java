package edu.luc.cs.trull.demo.task;

import java.util.NoSuchElementException;

import edu.luc.cs.trull.task.Task;

/**
 * A timer implemented as a Trull task.
 */
public class Timer implements Task {

  private final static long DEFAULT_UNIT = 100;

  private final long totalTime;
  private final long unitTime;
  private long timeLeft;

  public Timer(long totalTime, long unitTime) {
    this.totalTime = totalTime;
    this.unitTime = unitTime;
    timeLeft = totalTime;
  }

  public Timer(long totalTime) {
    this(totalTime, DEFAULT_UNIT);
  }

  public void restart() {
    timeLeft = totalTime;
  }

  public boolean hasNext() {
    return timeLeft > 0;
  }

  public Object next() {
    if (! hasNext()) {
      throw new NoSuchElementException();
    }
    try {
      long sleepTime = Math.min(timeLeft, unitTime);
      Thread.sleep(sleepTime);
      timeLeft -= sleepTime;
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
    return new Integer((int) (totalTime - timeLeft));
  }
}
