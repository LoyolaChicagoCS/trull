package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A combinator that cycles round-robin among several components
 * on any event from a given set of triggering events.  It is largely
 * equivalent to this nested Watching construct, but it does not
 * cycle to the next component when the current component terminates.
 * <PRE>
 * cycle c e1 ... en =
 *   loop
 *     do e1 watching c ->
 *       do e2 watching c ->
 *         ...
 *           do en watching c
 * </PRE>
 * @see edu.luc.cs.trull.Loop
 * @see edu.luc.cs.trull.Watching
 */
public class Cycle extends AbstractCombinator {

	private static final Logger logger = Logger.getLogger(Cycle.class);
	
  /**
   * Constructs an empty Cycle component.  Specific properties can be changed later.
   */
	public Cycle() { }
	
  /**
   * Constructs a Cycle component.
   * @param labels the array of String event labels triggering a switch. 
   * @param action the action invoked when a switch is triggered.  May be null.
   * @param branches the components among which this component cycles.
   */
	public Cycle(String[] labels, PropertyChangeListener action, Component[] branches) {
		setCycleEvents(labels);
		setCycleAction(action);
		for (int i = 0; i < branches.length; i ++) {
			addComponent(branches[i]);
		}
	}
	
  /**
   * <code>Cycle([label], action, branches)</code> 
   */
	public Cycle(String label, PropertyChangeListener action, Component[] branches) {
		this(new String[] { label }, action, branches);
	}

  /**
   * <code>Cycle(labels, null, branches)</code> 
   */
	public Cycle(String[] labels, Component[] branches) {
		this(labels, null, branches);
	}

  /**
   * <code>Cycle(label, null, branches)</code> 
   */
    public Cycle(String label, Component[] branches) {
        this(label, null, branches);
    }

	/**
   * An internal listener that listens to the currently active child branch
   * of this component and forwards events to the external listeners.
   */
  private PropertyChangeListener internalListener = new LocalListener();

  /**
   * The set of triggering events for cycling among components.
   */
	private Set cycleEvents = new HashSet();

  /**
   * Theh index of the currently active component.
   */
  private int currentComponentIndex = 0;

  /**
   * The action to be invoked when switching to the next component.
   */
  private PropertyChangeListener cycleAction;
  
  /** 
   * Sets the action to be invoked when switching to the next component.
   * @param action the action to be invoked when switching to the next component.  
   * May be null.
   */
  public synchronized void setCycleAction(PropertyChangeListener action) {
  	 	this.cycleAction = action;
  }
  
  /** 
   * Returns the action to be invoked when switching to the next component.
   * @return action the action to be invoked when switching to the next component.
   */
	public synchronized PropertyChangeListener getCycleAction() {
		return cycleAction;
	}

  /**
   * Sets the triggering event label for this component to the given string. 
   * @param label the triggering event label.
   */
	public synchronized void setCycleEvent(String label) {
		setCycleEvents(Collections.singleton(label));
	}

  /**
   * Sets the triggering event labels for this component to the given collection. 
   * @param evts the collection of triggering event labels.
   */
	public synchronized void setCycleEvents(Collection evts) {
		cycleEvents.clear();
		cycleEvents.addAll(evts);
	}

  /**
   * Sets the triggering event labels for this component to the given array. 
   * @param labels the array of triggering event labels.
   */
	public synchronized void setCycleEvents(String[] labels) {
		setCycleEvents(Arrays.asList(labels));
	}
  
  /**
   * Returns an array of triggering event labels for this component. 
   * @return label the array of triggering event labels.
   */
	public synchronized String[] getCycleEvents() {
		return (String[]) cycleEvents.toArray(new String[0]);
	}
	
  /**
   * Returns the currently active child component of this Cycle component.
   * @return the currently active child component.
   */
  protected Component getCurrentComponent() {
		if (getComponentCount() > 0) {
			return getComponent(currentComponentIndex);
		} else {
			return Nil.getInstance();
		}
	}
  
  /**
   * Switches (cycles) to the next child component.
   */
  protected void switchToNextComponent() {
		int size = getComponentCount();
		if (size > 1) {
			getCurrentComponent().removePropertyChangeListener(internalListener);
			currentComponentIndex = (currentComponentIndex + 1) % size;
			getCurrentComponent().addPropertyChangeListener(internalListener);
		}
	}
  
  /**
   * Switches (cycles) to the first child component.
   */
  protected void switchToFirstComponent() {
	  if (getComponentCount() > 1) {
			getCurrentComponent().removePropertyChangeListener(internalListener);
			currentComponentIndex = 0;
			getCurrentComponent().addPropertyChangeListener(internalListener);
  	  }
  }

  /**
   * Removes a component from the cycle.
   * @param c the component to be removed.
   */
  public synchronized void removeComponent(Component c) {
		// case 1: c == current
		// case 2: c is before current
		// case 3: c is after current
		int index = components.indexOf(c);
		int size = getComponentCount();
		if (size > 1) {
			if (index == currentComponentIndex) {
				switchToNextComponent();
			} else if (index < currentComponentIndex) {
				currentComponentIndex--;
			}
		} else if (size == 1) {
			getCurrentComponent().removePropertyChangeListener(internalListener);
		}
		super.removeComponent(c);
	}
  
	public void start(PropertyChangeEvent incoming) {
		if (logger.isDebugEnabled()) logger.debug(this + " starting");
		switchToFirstComponent();
		startCurrent(incoming);
	}
	
	public void stop() { 
		if (logger.isDebugEnabled()) logger.debug(this + " stopping");
		getCurrentComponent().stop();
	}
	
  /**
   * Starts the currently active component.
   * @param incoming the triggering event.
   */
	protected void startCurrent(PropertyChangeEvent incoming) {
		if (cycleAction != null) {
			cycleAction.propertyChange(incoming);
		}
		getCurrentComponent().start(incoming);
	}
	
	public void suspend() {	
		if (logger.isDebugEnabled()) logger.debug(this + " suspending");
		getCurrentComponent().suspend();
	}
	
	public void resume() {
		if (logger.isDebugEnabled()) logger.debug(this + " resuming");
		getCurrentComponent().resume();
	}

  /**
   * Forwards events received from external sources to the
   * currently active child component.
   * @param evt the event received.
   */
  public void propertyChange(PropertyChangeEvent evt) {
		if (cycleEvents.contains(evt.getPropertyName())) {
			stop();
			switchToNextComponent();
			startCurrent(evt);
			if (logger.isDebugEnabled()) logger.debug(this + " switching to component " + getCurrentComponent());
		} else {
			if (logger.isDebugEnabled()) logger.debug(this + " received external event " + evt);
			getCurrentComponent().propertyChange(evt);
		}
	}

  /**
   * Returns null because this component ignores termination of its children.
   * @return null.
   */
	protected TerminationListener getChildTerminationListener() {
    // TODO consider switching to the next component
		return null;
	}
  
  /**
   * A dedicated listener that forwards the event from 
   * the currently active child component to all external listeners.
   */
  protected class LocalListener implements PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent evt) {
    	  if (cycleEvents.contains(evt.getPropertyName())) {
    	  	  stop();
      	  	switchToNextComponent();
      	  	startCurrent(evt);
  	   		if (logger.isDebugEnabled()) logger.debug(this + " switching to component " + getCurrentComponent());
  		  } else {
    		  if (logger.isDebugEnabled()) logger.debug(this + " emitting child event " + evt);
          getExternalListeners().firePropertyChange(evt);
  		  }
    }
  }
}
