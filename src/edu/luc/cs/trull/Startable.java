package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;

/**
 * An abstraction of an activity that can be started and stopped.  
 * Subclasses are responsible for correctly implementing
 * this behavior.  Both lifecycle methods should return almost
 * immediately and, to preserve non-overlapping event
 * processing, should not directly fire any events. 
 * The start method may schedule events for indirect firing.
 * @see edu.luc.cs.trull.Component
 * @see edu.luc.cs.trull.Suspendable
 */
interface Startable {

	/**
   * Starts this startable component.
	 * @param incoming the event that triggered the invocation of this method.  May be null.
	 */
	void start(PropertyChangeEvent incoming);

  /**
   * Stops this startable component.
   */
	void stop();
}
