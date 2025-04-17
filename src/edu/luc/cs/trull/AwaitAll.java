package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A conjunctive await.
 * A conjunctive await waits until all specified events have occurred,
 * executes an action if present,
 * and continues as the given child component if present.  
 * It terminates when the child component terminates. 
 * If no events are specified, the combinator waits for any event.
 * @see edu.luc.cs.trull.AwaitOne
 */
public class AwaitAll extends SingleChildCombinator {  

	private static final Logger logger = Logger.getLogger(AwaitAll.class);

  /**
   * Constructs an empty AwaitAll component that is triggered by any first
   * incoming event.  Specific properties can be changed later.
   */
	public AwaitAll() { }
	
  /**
   * Constructs an AwaitAll component that waits for all the given labels,
   * executes the given action, and then starts the body.
   * @param labels an array of event labels to wait for.
   * @param action the action to be executed when all specified events have occurred.  May be null.
   * @param body the component to be started after all specified events have occurred.  If null, an empty component is used.
   */
	public AwaitAll(String[] labels, PropertyChangeListener action, Component body) {
		super(body);
		setEvents(labels);
		setAction(action);
	}
	
  /**
   * <code>AwaitAll(labels, null, body)</code>.
   */
	public AwaitAll(String[] labels, Component body) {
		this(labels, null, body);
	}

  /**
   * <code>AwaitAll(labels, action, new Done())</code>
   */
	public AwaitAll(String[] labels, PropertyChangeListener action) {
		setEvents(labels);
		setAction(action);
	}

  /**
   * An instance of Done to be used in various places.
   */
	private Done DONE;

	protected Component getDefaultChild() {
		if (DONE == null) {
			DONE = new Done();
		}
		return DONE;
	}

  /**
   * The listener to the child component.
   */
	private PropertyChangeListener internalListener;
	
	protected PropertyChangeListener getInternalListener() {
		if (internalListener == null) {
			internalListener = new LocalListener();
		}
		return internalListener;
	}

  /**
   * The child termination listener of this component.
   */
	private TerminationListener terminationListener;

	protected TerminationListener getChildTerminationListener() {
		if (terminationListener == null) {
			terminationListener = new DefaultTerminationListener();
		}
		return terminationListener;
	}

	/**
	 * The set of triggering events for starting this component.
	 */	
	private Set startEvents = new HashSet();
	
	/**
	 * The set of triggering events still missing.
	 */	
	private Set missingEvents = new HashSet();
	
	/**
	 * The action to be invoked as soon as all events have come in.
	 */
	private PropertyChangeListener action;

	/**
	 * This flag indicates whether the child has been started. 
	 */
	private boolean hasStartedBody = false;
	
  /**
   * Sets the triggering events for this component.
   * @param evts a collection of String event labels.
   */
	public synchronized void setEvents(Collection evts) {
		startEvents.clear();
		startEvents.addAll(evts);
	}

  /**
   * Sets the triggering events for this component.
   * @param labels an array of String event labels.
   */
	public synchronized void setEvents(String[] labels) {
		setEvents(Arrays.asList(labels));
	}
	
  /**
   * Returns the triggering events for this component.
   * @return an array containing the String event labels. 
   */
	public synchronized String[] getEvents() {
		return (String[]) startEvents.toArray(new String[0]);
	}
	
  /**
   * Sets the action for this component to be executed when
   * all triggering events have occurred.
   * @param action the action for this component.  May be null.
   */
	public synchronized void setAction(PropertyChangeListener action) {
		this.action = action;
	}

  /**
   * Returns the action for this component to be executed when
   * all triggering events have occurred.
   * @return the action for this component.
   */
	public synchronized PropertyChangeListener getAction() {
		return action;
	}
	
	public void start(PropertyChangeEvent incoming) {
		if (logger.isDebugEnabled()) logger.debug(this + " starting");
		hasStartedBody = false;
		missingEvents.clear();
		missingEvents.addAll(startEvents);
	}
	
	protected void startBody(PropertyChangeEvent incoming) { 
		if (logger.isDebugEnabled()) logger.debug(this + " starting child");
		if (action != null) {
			action.propertyChange(incoming);
		}
		hasStartedBody = true;
		super.start(incoming);
	}
	
	public void stop() {
		if (hasStartedBody) {
			super.stop();
		}
	}
	
	public void suspend() {
		if (hasStartedBody) {
			super.suspend();
		}
	}
	
	public void resume() {
		if (hasStartedBody) {
			super.resume();
		}
	}
	
	/**
	 * Processes incoming events.  If the component has
   * already been triggered, events are forwarded to the child
	 * component.   
	 * @param evt the event being received.  
	 */  
	public void propertyChange(PropertyChangeEvent evt) {
		String label = evt.getPropertyName();
		if (hasStartedBody) {
			if (logger.isDebugEnabled()) logger.debug(this + " received external event " + evt);
			getComponent(0).propertyChange(evt);
		} else if (startEvents.isEmpty() || missingEvents.contains(label)) {
			if (logger.isDebugEnabled()) logger.debug(this + " received missing event " + evt);
			missingEvents.remove(label);
			if (missingEvents.isEmpty()) {
				startBody(evt);
			}
		} else {
			if (logger.isDebugEnabled()) logger.debug(this + " ignoring external event " + evt);
		}
	}
	
	/**   
	 * A dedicated listener that forwards outgoing events to all external listeners.
	 */  
	protected class LocalListener implements PropertyChangeListener {    
		public void propertyChange(PropertyChangeEvent evt) {
			if (hasStartedBody) {
				if (logger.isDebugEnabled()) logger.debug(this + " emitting internal event " + evt);
				getExternalListeners().firePropertyChange(evt);
			} else {
				if (logger.isDebugEnabled()) logger.debug(this + " ignoring internal event " + evt);
			}
		}
	}
}
