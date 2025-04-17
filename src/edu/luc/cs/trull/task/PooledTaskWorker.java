package edu.luc.cs.trull.task;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;
import org.apache.log4j.Logger;

// TODO investigate bug showing up as unresponsiveness to suspend/stop
/**
 * A Worker that uses a multi-threaded PooledExecutor to perform its Task.
 * @see edu.luc.cs.trull.task.Task
 */
public class PooledTaskWorker extends AbstractTaskWorker {

  private static Logger logger = Logger.getLogger(PooledTaskWorker.class);

  private final static Executor executor = new PooledExecutor();

  protected final Executor getExecutor() { return executor; }

  public PooledTaskWorker(final Task task) {
    super(task);
    setStart();
  }
  
  public PooledTaskWorker(final TaskFactory taskFactory) {
    super(taskFactory);
    setStart();
  }
  
  private void setStart() {
    setStart(new Runnable() {
      public void run() {
        logger.debug(this +".run()");
        try {
          if (state.get() == STOPPED) return;
          if (firstTime) {
            firstTime = false;
          } else {
            logger.debug(this +".run() restarting task");
            task.restart();
          }
          synchronized (state.getLock()) {
            state.whenLess(SUSPENDED, null);
            if (state.get() == STOPPED) return;
          }
          while (task.hasNext()) {
            task.next();
            synchronized (state.getLock()) {
              state.whenLess(SUSPENDED, null);
              if (state.get() == STOPPED) return;
            }
          }
          state.set(STOPPED);
          scheduleTermination();
        } catch (InterruptedException e) {
          logger.debug(this +".run() interrupted, returning");
          Thread.currentThread().interrupt();
        } finally {
          logger.debug(this +".run() returning");
        }
      }
    });
  }
}