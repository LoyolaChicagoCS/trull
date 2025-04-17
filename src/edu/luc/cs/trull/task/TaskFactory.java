package edu.luc.cs.trull.task;

import java.beans.PropertyChangeEvent;

/**
 * A function from events to tasks.
 */
public interface TaskFactory {
  /**
   * Computes a boolean from the given event.
   * @param incoming the event.
   * @return the computed boolean.
   */
  Task apply(PropertyChangeEvent incoming);
}
