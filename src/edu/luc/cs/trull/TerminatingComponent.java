package edu.luc.cs.trull;

import java.util.EventObject;

import org.apache.log4j.Logger;

/**
 * An abstract superclass for components that signal termination to 
 * their parents in the component tree.  
 * To indicate termination, subclasses should use 
 * scheduleTermination from within a lifecycle method or event listener method.
 * To propagate termination upward directly, subclasses should use
 * fireTermination from within their child termination listener.
 */
public class TerminatingComponent extends EmptyComponent implements SchedulerAware {  

	private static final Logger logger = Logger.getLogger(TerminatingComponent.class);

	/**
	 * The scheduler provided to this component.
	 */
	private Scheduler scheduler;
	
	public void setScheduler(final Scheduler scheduler) {
	  this.scheduler = scheduler;
	}
	
	protected Scheduler getScheduler() {
	  return scheduler;
	}
	
	/**
	 * The event object indicating that this component wants to terminate.
	 */
	private final EventObject TERM = new EventObject(this);

	/**
	 * Returns the unique termination event for this component.
   * @return the termination event.
	 */
	protected EventObject getTermEvent() {
		return TERM;
	}
	
	/**
	 * The parent of this component. 
	 */
	private TerminationListener parent;
	
	/**
	 * Returns the parent of this component.
   * @return the parent of this component.
	 */
	protected synchronized TerminationListener getParent() {
		return parent;
	}
	
	/**
	 * Sets the TerminationListener of this component. 
	 * Usually the component's parent is the termination listener.
   * @param parent the termination listener. 
	 */
	public synchronized void setTerminationListener(TerminationListener parent) {
		this.parent = parent;
	}
	
	/**
	 * This emitter simply indicates termination.
	 */
  private final Runnable terminationEmitter = new Runnable() {
		public void run() {
			fireTermination();
		}
	};

	/**
	 * Informs the termination listener that this component wants to terminate.
   * Subclasses use this method to propagate termination events upward
   * to parent components, usually in response to receiving a termination
   * event from a child component.  To preserve non-overlapping processing of
   * events, this method must not be invoked from within an event listener
   * method.
	 */
	protected void fireTermination() {
		if (logger.isDebugEnabled()) logger.debug(this + " terminating");
		if (parent != null) {
			parent.componentTerminated(TERM);
		}
	}
	
	/**
   * Schedules a request to the termination listener that this 
   * component wants to terminate.  This method is used by subclasses.
   * This method must be used when a component decides to terminate in
   * response to an incoming event. 
	 */
	public void scheduleTermination() {
		scheduler.schedule(terminationEmitter);
	}
}
