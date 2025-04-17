package edu.luc.cs.trull;

import java.util.EventListener;
import java.util.EventObject;

/**
 * A listener for termination of a Trull component.
 */
public interface TerminationListener extends EventListener {

	/**
	 * Indicates termination by the source of the event to this listener.
	 */
	void componentTerminated(EventObject event);
}
