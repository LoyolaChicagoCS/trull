package edu.luc.cs.trull.task;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;

import EDU.oswego.cs.dl.util.concurrent.Executor;
import EDU.oswego.cs.dl.util.concurrent.WaitableInt;
import edu.luc.cs.trull.TerminatingComponent;


/**
 * A Worker for the step-by-step execution of a Task using an Executor.
 * @see edu.luc.cs.trull.task.Task
 * @see edu.luc.cs.trull.task.TaskFactory
 */
abstract class AbstractTaskWorker extends TerminatingComponent {

  private static Logger logger = Logger.getLogger(AbstractTaskWorker.class);

  // do not change ordering: STOPPED < RUNNING < SUSPENDED
  
  /**
   * The stopped state.
   */
  protected static final int STOPPED   = 1;

  /**
   * The running state.
   */
  protected static final int RUNNING   = 2;

  /**
   * The suspended state.
   */
  protected static final int SUSPENDED = 3;

  /**
   * The optional task factory used by this worker.
   */
  protected TaskFactory taskFactory;
  
  /**
   * The task currently being executed by this worker.
   */
  protected Task task;

  /**
   * The Runnable for starting work on the task.
   */
  protected Runnable start;

  /**
   * The variable holding the current state.
   */
  protected final WaitableInt state = new WaitableInt(STOPPED);

  /**
   * A variable indicating whether the task is being executed for the first time.
   */
  protected boolean firstTime = true;

  /**
   * Constructs a worker for the given task.
   * 
   * @param task
   *          the task to be performed.
   * @throws IllegalArgumentException
   *           if the task is null.
   */
  public AbstractTaskWorker(final Task task) {
    if (task == null) {
      throw new IllegalArgumentException();
    }
    this.task = task;
  }

  /**
   * Constructs a worker for the given task.
   * @param task the task to be performed.
   * @throws IllegalArgumentException if the task is null.
   */
  public AbstractTaskWorker(final TaskFactory taskFactory) {
    if (taskFactory == null) {
      throw new IllegalArgumentException();
    }
    this.task = null;
    this.taskFactory = taskFactory;
  }

  /**
   * The executor used by this worker to schedule portions of the work.
   * @return the executor used by this worker.
   */
  protected abstract Executor getExecutor();

  /**
   * Sets the Runnable for starting the work.
   * @param start
   */
  protected synchronized void setStart(Runnable start) {
    this.start = start;
  }
  
  /**
   * Sets the task factory for this worker.
   * 
   * @param taskFactory
   *          the task factory for this worker.
   * @throws IllegalArgumentException
   *           if the task is null.
   */
  public synchronized void setTask(Task task) {
    if (task == null) {
      throw new IllegalArgumentException();
    }
    this.task = task;
  }

  /**
   * Sets the task to be performed.
   * @param task the task to be performed.
   * @throws IllegalArgumentException if the task is null.
   */
  public synchronized void setTaskFactory(TaskFactory taskFactory) {
    if (taskFactory == null) {
      throw new IllegalArgumentException();
    }
    this.task = null;
    this.taskFactory = taskFactory; 
  }

  public void start(PropertyChangeEvent incoming) {
    logger.debug(this + ".start(" + incoming + ")");
    try {
      if (! state.commit(STOPPED, RUNNING)) return;
      if (taskFactory != null) {
        task = taskFactory.apply(incoming);
        logger.debug(this + ".start() created task");
      }
      getExecutor().execute(start);
      logger.debug(this + ".executor.execute() invoked");
    } catch (InterruptedException e) {
      logger.debug(this + ".executor.execute() interrupted");
    }
  }

  public void stop() {
    logger.debug(this + ".stop()");
    state.set(STOPPED);
  }

  public void suspend() {
    logger.debug(this + ".suspend()");
    state.commit(RUNNING, SUSPENDED);
  }

  public void resume() {
    logger.debug(this + ".resume()");
    state.commit(SUSPENDED, RUNNING);
  }
}