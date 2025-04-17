package edu.luc.cs.trull.task;

import java.beans.PropertyChangeEvent;

import org.apache.log4j.Logger;

import edu.luc.cs.trull.TerminatingComponent;

/**
 * A Worker for the step-by-step execution of a Task using Trull's default
 * scheduling mechanism. If this Worker does not provide sufficient
 * responsiveness, consider making the task finer-grained or using a Worker
 * implementation that carries out the task in one or more separate threads.
 * 
 * @see edu.luc.cs.trull.task.Task
 * @see edu.luc.cs.trull.task.QueuedTaskWorker
 * @see edu.luc.cs.trull.task.PooledTaskWorker
 */
public class DefaultTaskWorker extends TerminatingComponent {

  private static Logger logger = Logger.getLogger(DefaultTaskWorker.class);

  /**
   * The stopped state.
   */
  protected static final int STOPPED = 1;

  /**
   * The running state.
   */
  protected static final int RUNNING = 2;

  /**
   * The suspended state.
   */
  protected static final int SUSPENDED = 3;

  /**
   * The optional task factory used by this worker.
   */
  protected TaskFactory taskFactory;
  
  /**
   * The task executed by this worker.
   */
  protected Task task;

  /**
   * The variable holding the current state.
   */
  protected int state = STOPPED;

  /**
   * A flag indicating whether the task is being executed for the first time.
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
  public DefaultTaskWorker(final Task task) {
    if (task == null) {
      throw new IllegalArgumentException();
    }
    this.task = task;
  }

  /**
   * Constructs a worker for the given task factory.
   * 
   * @param taskFactory
   *          the task factory for this worker.
   * @throws IllegalArgumentException
   *           if the task is null.
   */
  public DefaultTaskWorker(final TaskFactory taskFactory) {
    if (taskFactory == null) {
      throw new IllegalArgumentException();
    }
    this.task = null;
    this.taskFactory = taskFactory;
  }

  /**
   * Sets the task to be performed.
   * 
   * @param task
   *          the task to be performed.
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
   * Sets the task factory for this worker.
   * 
   * @param taskFactory
   *          the task factory for this worker.
   * @throws IllegalArgumentException
   *           if the task is null.
   */
  public synchronized void setTaskFactory(TaskFactory taskFactory) {
    if (taskFactory == null) {
      throw new IllegalArgumentException();
    }
    this.task = null;
    this.taskFactory = taskFactory;
  }

  /**
   * A Runnable that executes one step of the task and reschedules itself for
   * execution as long as the worker has not been suspended.
   */
  protected final Runnable repeat = new Runnable() {
    public void run() {
      logger.debug(this + ".repeat.run()");
      if (state != RUNNING)
        return;
      if (task.hasNext()) {
        task.next();
        if (state == RUNNING) {
          logger.debug(this + ".repeat.run() rescheduling itself");
          getScheduler().schedule(repeat);
        }
      } else {
        state = STOPPED;
        logger.debug(this + ".repeat.run() scheduling termination");
        scheduleTermination();
      }
    }
  };

  /**
   * Restarts the task if necessary and schedules the repeat Runnable for
   * executing the task step-by-step.
   */
  public void start(PropertyChangeEvent incoming) {
    logger.debug(this + ".start(" + incoming + ")");
    if (state != STOPPED) return;
    state = RUNNING;
    if (taskFactory != null) {
      logger.debug(this + ".start() creating task");
      task = taskFactory.apply(incoming);
    }
    if (firstTime) {
      firstTime = false;
    } else {
      logger.debug(this + ".start() restarting task");
      task.restart();
    }
    getScheduler().schedule(repeat);
    logger.debug(this + ".start() done starting task");
  }

  public void stop() {
    logger.debug(this + ".stop()");
    state = STOPPED;
  }

  public void suspend() {
    logger.debug(this + ".suspend()");
    if (state == RUNNING) {
      state = SUSPENDED;
    }
  }

  public void resume() {
    logger.debug(this + ".resume()");
    if (state == SUSPENDED) {
      state = RUNNING;
      getScheduler().schedule(repeat);
    }
    logger.debug(this + ".start() done resuming task");
  }
}