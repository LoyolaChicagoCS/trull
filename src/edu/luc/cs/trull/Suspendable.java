package edu.luc.cs.trull;

/**
 * An abstraction of an activity that can be suspended and resumed.
 * Subclasses are responsible for correctly implementing
 * this behavior.  Both lifecycle methods should return almost
 * immediately. 
 * @see edu.luc.cs.trull.Component
 * @see edu.luc.cs.trull.Startable
 */
interface Suspendable {
	
  /**
   * Suspends this suspendable component.
   */
	void suspend();

  /**
   * Resumes this suspendable component.
   */
	void resume();
}
