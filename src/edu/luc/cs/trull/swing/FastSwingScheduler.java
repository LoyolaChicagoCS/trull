package edu.luc.cs.trull.swing;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;

import edu.luc.cs.trull.Scheduler;

/**
 * An implementation of the Scheduler interface based on the
 * Java Swing event dispatch thread.  To allow for faster execution, it does not 
 * support the await method.
 * This Scheduler implementation guarantees 
 * that the application satisfies the Swing single-thread rule 
 * without explicit scheduling of tasks in the Swing event dispatch thread.
 * @see edu.luc.cs.trull.Scheduler
 * @see edu.luc.cs.trull.swing.SwingScheduler
 */
public class FastSwingScheduler implements Scheduler {

	private static final Logger logger = Logger.getLogger(FastSwingScheduler.class);

	public void schedule(Runnable task) {
		if (logger.isDebugEnabled()) logger.debug(this + " scheduling " + task);
		SwingUtilities.invokeLater(task);
	}

	public void execute(Runnable task) throws InterruptedException {
		if (logger.isDebugEnabled())	 logger.debug(this + " executing " + task);
		try {
			SwingUtilities.invokeAndWait(task);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}

  /**
   * This method is not supported in the interest of faster execution.
   */
	public void await() throws InterruptedException {
    throw new UnsupportedOperationException("Use SwingScheduler if you need this method");
	}
}