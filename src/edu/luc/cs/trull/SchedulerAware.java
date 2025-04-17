package edu.luc.cs.trull;

/**
 * Interface to be implemented by components that want to be aware
 * of the (typically global) event scheduler.
 */
public interface SchedulerAware {
  
  /**
   * Supplies the event scheduler to this component
   * @param scheduler the event scheduler
   */
  void setScheduler(Scheduler scheduler);
}
