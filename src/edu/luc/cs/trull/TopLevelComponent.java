package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;

/**
 * A top-level Trull component whose lifecycle methods
 * can be invoked without worrying about thread safety.
 */
public interface TopLevelComponent {
  
  /**
   * Schedules the component's start method for execution.
   */
	void start() throws InterruptedException;
	
  /**
   * Schedules the component's start method for execution,
   * passing the incoming event to the component's start method.
   * @param event the incoming event
   */
	void start(PropertyChangeEvent event) throws InterruptedException;
	
  /**
   * Schedules the component's stop method for execution.
   */
	void stop() throws InterruptedException;

  /**
   * Schedules a component's suspend method for execution.
   */
	void suspend() throws InterruptedException;
	
  /**
   * Schedules a component's resume method for execution.
   */
	void resume() throws InterruptedException;

  /**
   * Schedules a component's property change event listener method 
   * for execution.
   * @param event the event the component is to receive
   */
	void propertyChange(final PropertyChangeEvent event) throws InterruptedException;
	
	/**
	 * Executes a Runnable and waits for its completion.
	 * @param r the Runnable to be executed.
	 * @throws InterruptedException if the current thread is interrupted while waiting.
	 */
	void execute(Runnable r) throws InterruptedException;

	/**
	 * Waits for the component to finish processing all pending requests.  
	 * This capability is important for avoiding race conditions.
	 * @throws InterruptedException if the current thread is interrupted 
   * before or while waiting
	 */
	void await() throws InterruptedException;
}
