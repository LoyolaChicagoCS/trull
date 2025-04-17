package edu.luc.cs.trull.task;

/*
 * TODO RESEARCH these Tasks support only linear dependency chains; 
 * explore general DAGs and workers that concurrently
 * work on suitable steps in the DAG.  (George)
 * Call this SequentialTask and introduce ParallelTask
 * Investigate relationship with reactive vats.
 */ 

/**
 * An abstraction of a piece of work that can be broken up into
 * discrete steps.  A Task is usually performed by a Worker.  
 * The granularity of the steps is up to the implementor
 * of the Task and determines the granularity of control over the Worker
 * executing the Task.
 * 
 * @see edu.luc.cs.trull.task.AbstractTaskWorker
 */
public interface Task {
  
  /**
   * Returns <code>true</code> if the task is still in progress.
   * @return <code>true</code> if the task is still in progress.
   */
  boolean hasNext();

  /**
   * Performs the next discrete step of this task.
   * @return the intermediate result of this step.
   */
  Object next();

  /**
   * Reinitializes this task so that it can be performed again.
   */
  void restart();
}