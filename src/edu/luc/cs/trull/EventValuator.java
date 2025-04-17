package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;

/**
 * A function from events to int.
 */
public interface EventValuator {

  /**
   * Computes an int from the given event.
   * @param incoming the event.
   * @return the computed int.
   */
  int apply(PropertyChangeEvent incoming);
}
