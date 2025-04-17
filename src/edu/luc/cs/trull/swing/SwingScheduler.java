package edu.luc.cs.trull.swing;

import org.apache.log4j.Logger;

import EDU.oswego.cs.dl.util.concurrent.WaitableInt;

/**
 * An implementation of the Scheduler interface based on the
 * Java Swing event dispatch thread.  It supports the await method.
 * This Scheduler implementation guarantees 
 * that the application satisfies the Swing single-thread rule 
 * without explicit scheduling of tasks in the Swing event dispatch thread.
 * @see edu.luc.cs.trull.Scheduler
 */
public class SwingScheduler extends FastSwingScheduler {

	private static final Logger logger = Logger.getLogger(SwingScheduler.class);

  /**
   * The current length of the queue of Runnables scheduled through this scheduler.
   */
	private WaitableInt queueLength = new WaitableInt(0);

	public void schedule(Runnable task) {
		queueLength.increment();
    super.schedule(new QueuedRunnable(task));
	}

  public void execute(Runnable task) throws InterruptedException {
    queueLength.increment();
    super.execute(new QueuedRunnable(task));
  }
  
	public void await() throws InterruptedException {
    if (logger.isDebugEnabled()) logger.debug(this + " waiting for queue to become empty");
		queueLength.whenEqual(0, null);
    if (logger.isDebugEnabled()) logger.debug(this + " queue is now empty");
	}

	/**
	 * A wrapper around a runnable that decrements the queue length
	 * when the runnable has finished. 
	 */
	private class QueuedRunnable implements Runnable {
		private Runnable task;
		public QueuedRunnable(Runnable task) { this.task = task; }
		public void run() {
      try {
			  task.run();
      } finally {
			  queueLength.decrement();
      }
		}
	}
}