package edu.luc.cs.trull;

import org.apache.log4j.Logger;

import EDU.oswego.cs.dl.util.concurrent.Latch;
import EDU.oswego.cs.dl.util.concurrent.WaitableInt;

/**
 * An implementation of the Scheduler interface based on
 * Doug Lea's QueuedExecutor.  It supports the await method.
 */
public class DefaultScheduler extends FastScheduler {

	private static final Logger logger = Logger.getLogger(DefaultScheduler.class);

  /**
   * The current length of the queue of Runnables. 
   */
	private WaitableInt queueLength = new WaitableInt(0);

  /**
   * A dummy latch to be used when scheduling Runnables 
   * without waiting for their execution.
   */
  private static Latch DUMMY = new Latch();
    
	public void schedule(Runnable task) {
		queueLength.increment();
    super.schedule(new QueuedRunnable(task, DUMMY));
	}

	public void execute(Runnable task) throws InterruptedException {
    // the equivalent of SwingUtilities.invokeAndWait
    queueLength.increment();
    super.execute(task);
	}

  public void await() throws InterruptedException {
    if (logger.isDebugEnabled()) logger.debug(this + " waiting for queue to become empty");
    queueLength.whenEqual(0, null);
    if (logger.isDebugEnabled()) logger.debug(this + " queue is now empty");
  }

  protected Runnable makeWaitable(Runnable task, Latch done) {
    return new QueuedRunnable(task, done);
  }
    
  /**
   * A wrapper around a runnable that decrements the queue length
   * when the runnable has finished. 
	 */
	private class QueuedRunnable extends WaitableRunnable {
    public QueuedRunnable(Runnable task, Latch done) { 
      super(task, done); 
    }
		public void run() {
      try {
      	  super.run();
      } finally {
        queueLength.decrement();
      }
		}
	}
}