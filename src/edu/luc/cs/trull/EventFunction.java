package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;

/**
 * A function from events to objects.
 */
public interface EventFunction {
    
  /** 
   * Computes an object from the given event.
   * @param incoming the event.
   * @return the computed object.
   */
	Object apply(PropertyChangeEvent incoming);
}
