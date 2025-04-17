package edu.luc.cs.trull;

/**
 * An abstraction of a centralized event processing thread 
 * such as the Swing event dispatch thread.
 */
public interface Scheduler {

	/**
	 * Schedules a Runnable for later execution.
	 * @param r the Runnable to be scheduled.
	 */
	void schedule(Runnable r);

	/**
	 * Executes a Runnable and waits for its completion.
	 * @param r the Runnable to be executed.
	 * @throws InterruptedException if the current thread is interrupted while waiting.
	 */
	void execute(Runnable r) throws InterruptedException;

	/**
	 * Waits for the event queue to become empty.  
	 * This capability is important for avoiding race conditions.
   * @throws InterruptedException if the current thread is interrupted while waiting.
	 */
	void await() throws InterruptedException;
}