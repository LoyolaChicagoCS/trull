package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import org.apache.log4j.Logger;

/**
 * A data-driven selection mechanism that immediately evaluates its valuator 
 * (criterium) and chooses the corresponding branch.
 * The resulting component terminates when the chosen branch terminates.  
 */
public class Switch extends AbstractCombinator {

	private static final Logger logger = Logger.getLogger(Switch.class);

  /**
   * Constructs a Switch component.  Specific properties can be changed later. 
   */
	public Switch() {
		this.addComponent(new Done());
	}
	
  /**
   * Constructs a Switch component.
   * @param valuator the criterium used to choose a branch.
   * @param components the array of branch components.
   */
	public Switch(EventValuator valuator, Component[] components) {
		this();
		setValuator(valuator);
		for (int i = 0; i < components.length; i ++) {
			this.addComponent(components[i]);
		}
	}

	/**
   * An internal listener that listens to the currently active child branch
   * of this component and forwards events to the external listeners.
   */
  private PropertyChangeListener internalListener = new LocalListener();

  /**
   * The valuator (criterium) for this switch.
   */
	private EventValuator valuator;
  
  /**
   * The index of the chosen branch.
   */
  private int currentComponentIndex = 0;
  
  /**
   * Sets the criterium of this Switch to the given valuator. 
   * @param valuator the valuator to be used as the criterium. 
   */
	public synchronized void setValuator(EventValuator valuator) {
		this.valuator = valuator;
	}

  /** 
   * Returns the criterium used by this Switch.
   * @return the valuator used as the criterium.
   */
	public synchronized EventValuator getValuator() {
		return valuator;
	}
	
  /**
   * The chosen branch comopnent.
   * @return the chosen branch comopnent.
   */
  protected Component getCurrentComponent() {
		return getComponent(currentComponentIndex);
	}
  
  /**
   * Switches to the branch component with the given index.
   * @param choice the index of the branch.
   */
  protected void switchToComponent(int choice) {
		if (logger.isDebugEnabled()) logger.debug(this + " choice is " + choice);
  	  if (choice < 0 || choice >= getComponentCount() - 1) {
  	  	  choice = 0; // default branch
  	  } else {
  	  		choice = choice + 1;
  	  }
		currentComponentIndex = choice;
		Component current = getCurrentComponent(); 
		current.addPropertyChangeListener(internalListener);
		current.setTerminationListener(getChildTerminationListener());
  }
  
  /**
   * Unhooks the given component from communication and termination.
   * @param c the component to be unhooked.
   */
  protected void disconnectComponent(Component c) {
    c.removePropertyChangeListener(internalListener);
    c.setTerminationListener(null);
  }
  
	public synchronized void removeComponent(Component c) {
		super.removeComponent(c);
    disconnectComponent(c);
	} 

	public void start(PropertyChangeEvent incoming) {
		if (logger.isDebugEnabled()) logger.debug(this + " starting");
		int choice = valuator.apply(incoming);
		switchToComponent(choice);
		getCurrentComponent().start(incoming);
	}
	
	public void stop() { 
		if (logger.isDebugEnabled()) logger.debug(this + " stopping");
		getCurrentComponent().stop();
    disconnectComponent(getCurrentComponent());
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
   * components of this combinator.
   * @param evt the event received.
   */
  public void propertyChange(PropertyChangeEvent evt) {
		if (logger.isDebugEnabled()) logger.debug(this + " received external event " + evt);
		getCurrentComponent().propertyChange(evt);
	}

  /**
   * The child termination listener for this component.
   */
  private TerminationListener terminationListener;
  
	protected TerminationListener getChildTerminationListener() {
		if (terminationListener == null) {
			terminationListener = new SwitchTerminationListener();
		}
		return terminationListener;
	}
  
  /**
   * A component that terminates as soon as the chosen branch component terminates.
   */
  protected class SwitchTerminationListener implements TerminationListener {
    public void componentTerminated(EventObject event) {
      fireTermination();
      disconnectComponent(getCurrentComponent());  
    }
  }
  
  /**
   * A dedicated listener that forwards the event to all external listeners.
   */
  protected class LocalListener implements PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent evt) {
 		  if (logger.isDebugEnabled()) logger.debug(this + " emitting child event " + evt);
      getExternalListeners().firePropertyChange(evt);
    }
  }
}
