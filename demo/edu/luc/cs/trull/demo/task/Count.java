package edu.luc.cs.trull.demo.task;

import java.util.NoSuchElementException;

import edu.luc.cs.trull.task.Task;

/**
 * A simple task that wastes some CPU cycles in each step.
 */
public class Count implements Task {

  final int max;
  final int increment;
  int i = 0;

  public Count(int max) {
    this(max, 1);
  }

  public Count(int max, int increment) {
    this.max = max;
    this.increment = increment;
    // restart();
    // causes NullPointerException when trying to access
    // uninitialized reference from (inner) subclass to object
    // in surrounding scope in overridden version of restart()
    // (one have the same problem without the presence of inner classes!)
  }

  public Object next() {
//    System.err.println(this + ": " + System.currentTimeMillis());
    if (! hasNext()) {
      throw new NoSuchElementException();
    }
    Object result = null;
    for (int k = 0; k < increment; k ++) {
      i++; // do the work
      result = new Integer(i);
    }
//    System.err.println(this + ": " + System.currentTimeMillis());
    return result;
  }

  public boolean hasNext() {
    return i < max;
  }

  public void restart() {
    i = 0;
  }
}