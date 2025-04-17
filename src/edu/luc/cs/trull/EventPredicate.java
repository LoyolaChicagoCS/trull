package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;

/**
 * A function from events to boolean.
 */
public interface EventPredicate {
    
  /**
   * Computes a boolean from the given event.
   * @param incoming the event.
   * @return the computed boolean.
   */
	boolean apply(PropertyChangeEvent incoming);
}
