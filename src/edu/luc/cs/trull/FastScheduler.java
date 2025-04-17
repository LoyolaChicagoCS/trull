package edu.luc.cs.trull;

import org.apache.log4j.Logger;

import EDU.oswego.cs.dl.util.concurrent.Latch;
import EDU.oswego.cs.dl.util.concurrent.LinkedQueue;
import EDU.oswego.cs.dl.util.concurrent.QueuedExecutor;

/**
 * An implementation of the Scheduler interface based on
 * Doug Lea's QueuedExecutor.  To allow for faster execution, this class
 * does not support the await method.
 */
public class FastScheduler implements Scheduler {

	private static final Logger logger = Logger.getLogger(FastScheduler.class);

	protected QueuedExecutor executor = new QueuedExecutor(new LinkedQueue());

  /**
   * Sole constructor.
   */
	public FastScheduler() {
    executor.restart();
		executor.getThread().setName("Trull-EventQueue");
		executor.getThread().setPriority(Thread.NORM_PRIORITY + 1);
	}
    
	public void schedule(Runnable task) {
    if (logger.isDebugEnabled()) logger.debug(this + " scheduling " + task);
		try {
      executor.execute(task); 
		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted during FastScheduler.schedule");
		}
	}

	public void execute(Runnable task) throws InterruptedException {
    if (logger.isDebugEnabled()) logger.debug(this + " executing " + task);
    // the equivalent of SwingUtilities.invokeAndWait
    Latch done = new Latch();
    executor.execute(makeWaitable(task, done));
    done.acquire();
	}

  /**
   * This method is not supported in the interest of faster execution.
   */
	public void await() throws InterruptedException {
    throw new UnsupportedOperationException("DefaultScheduler if you need the await method");
	}
    
  /**
   * Factory method for a Runnable to schedule.
   * @param task the original task to schedule.
   * @param done the latch indicating when the task has been executed.
   * @return the Runnable to schedule.
   */
  protected Runnable makeWaitable(Runnable task, Latch done) {
    return new WaitableRunnable(task, done);
  }

  /**
	 * A wrapper around a Runnable that releases a latch 
   * when the Runnable has finished. 
	 */
	protected class WaitableRunnable implements Runnable {
		private Runnable task;
    private Latch done;
    public WaitableRunnable(Runnable task, Latch done) {
      this.task = task;
      this.done = done;
    }
		public void run() {
      try {
			  task.run();
      } finally {
        done.release();
      }
		}
	}
}