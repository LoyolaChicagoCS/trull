package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;

/**
 * An adapter for using a simple PropertyChangeListener
 * as a Trull component.  When started, this component invokes the listener 
 * method and then terminates.
 * @see java.beans.PropertyChangeListener
 */
public class ActionComponent extends TerminatingComponent {

	private static final Logger logger = Logger.getLogger(ActionComponent.class);

  /**
   * Creates an ActionComponent component with a null listener.  
   */
	public ActionComponent() { }
	
  /**
   * Creates an ActionComponent component with the given listener as its action.
   * @param action the listener to be used as the action.  May be null.
   */
	public ActionComponent(PropertyChangeListener action) {
		setAction(action);
	}
	
  /**
   * The action of this component.
   */
	private PropertyChangeListener action;
	
  /**
   * Sets the action of this component to the given listener.
   * @param action the listener to be used as the action.  May be null.
   */
	public synchronized void setAction(PropertyChangeListener action) {
		this.action = action;
	}
	
  /** 
   * Gets the action of this component.
   * @return the listener used as the action.
   */
	public synchronized PropertyChangeListener getAction() {
		return action;
	}
	
	public void start(PropertyChangeEvent incoming) {
		if (logger.isDebugEnabled()) logger.debug(this + " starting");
		if (action != null) {
		  action.propertyChange(incoming);
		}
		scheduleTermination();
	}

	public void stop() { 
		if (logger.isDebugEnabled()) logger.debug(this + " stopping");
	}
}
