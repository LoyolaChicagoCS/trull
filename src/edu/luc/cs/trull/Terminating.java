package edu.luc.cs.trull;

/**
 * A component that supports termination by notifying a TerminationListener.  
 * The termination listener is usually the component's parent in the component tree. 
 */
interface Terminating {
	
  /**
   * Sets this component's termination listener to the given component.
   * @param parent the component to be used as the termination listener.
   */
	void setTerminationListener(TerminationListener parent);
	
  /**
   * Returns the termination listener of this component.
   * @return the termination listener.
   */
	TerminationListener getTerminationListener();
}
