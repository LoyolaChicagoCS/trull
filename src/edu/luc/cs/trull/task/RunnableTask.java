package edu.luc.cs.trull.task;

/**
 * An adapter for using a Runnable as a one-step Task. 
 */
public class RunnableTask implements Task {

  private final Runnable runnable;

  public RunnableTask(final Runnable runnable) {
    this.runnable = runnable;
  }

  public boolean hasNext() {
    return false;
  }

  public Object next() {
    runnable.run();
    return null;
  }

  public void restart() {}
}