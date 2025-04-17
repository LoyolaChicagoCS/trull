package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import org.apache.log4j.Logger;

/**
 * A combinator for executing zero or more components in sequence.
 * The components that form part of the sequence should indicate termination. 
 * A sequence terminates when its last component terminates.
 */
public class Sequence extends AbstractCombinator {  

	private static final Logger logger = Logger.getLogger(Sequence.class);
	
  /**
   * Constructs a Sequence component.  Specific properties can be changed later.
   */
	public Sequence() { }
	
  /**
   * Constructs a Sequence component consisting of the two given components in order.
   * @param first the first component in the sequence.
   * @param second the second component in the sequence.
   */
	public Sequence(Component first, Component second) {
		this(new Component[] { first, second });
	}
	
  /**
   * Constructs a Sequence component consisting of the given list of components.
   * @param components the list of components in the sequence.
   */
	public Sequence(Component[] components) {
		for (int i = 0; i < components.length; i ++) {
			addComponent(components[i]);
		}
	}

	/**
   * An internal listener that listens to the currently active child branch
   * of this component and forwards events to the external listeners.
   */
  private PropertyChangeListener internalListener = new ForwardListener();

  /**
   * The child termination listener of this component.
   */
	private TerminationListener terminationListener;

	protected TerminationListener getChildTerminationListener() {
		if (terminationListener == null) {
			terminationListener = new SequenceTerminationListener();
		}
		return terminationListener;
	}

  /**
   * The index of the currently active component.
   */
  private int currentComponentIndex = 0;

  /**
   * An instance of Done used as the default child.
   */
  private final Done DONE = new Done();
  
  /**
   * Returns the currently active component.
   * @return the currently active component.
   */
  protected Component getCurrentComponent() {
		if (components.size() > 0) {
			return getComponent(currentComponentIndex);
		} else {
			return DONE;
		}
	}
  
  /**
   * Switches to the next component in the sequence.
   */
  protected void switchToNextComponent() {
		int size = getComponentCount();
		if (size > 1) {
			disconnectComponent(getCurrentComponent()); 
			currentComponentIndex = (currentComponentIndex + 1) % size;
			connectComponent(getCurrentComponent());
		}
	}
  
  /**
   * Switches to the first component in the sequence.
   */
  protected void switchToFirstComponent() {
		currentComponentIndex = 0;
		connectComponent(getCurrentComponent());
  }

  public synchronized void removeComponent(Component c) {
		// case 1: c == current
		// case 2: c is before current
		// case 3: c is after current
		int index = components.indexOf(c);
		if (getComponentCount() > 1) {
			if (index == currentComponentIndex) {
				switchToNextComponent();
			} else if (index < currentComponentIndex) {
				currentComponentIndex--;
			}
		}
		super.removeComponent(c);
	}

  /**
   * Hooks up the given component for communication and termination.
   * @param c the component to be hooked up.
   */
  protected void connectComponent(Component c) {
		c.addPropertyChangeListener(internalListener);
  		c.setTerminationListener(getChildTerminationListener());
  }
  
  /**
   * Unhooks the given component from communication and termination.
   * @param c the component to be unhooked.
   */
  protected void disconnectComponent(Component c) {
		c.removePropertyChangeListener(internalListener);
  		c.setTerminationListener(null);
  }
  
	public void start(PropertyChangeEvent incoming) {
		if (logger.isDebugEnabled()) logger.debug(this + " starting");
		switchToFirstComponent();
		Component current = getCurrentComponent();
		if (logger.isDebugEnabled()) logger.debug(this + " first component is " + current);
		current.start(incoming);
	}
	
	public void stop() { 
		if (logger.isDebugEnabled()) logger.debug(this + " stopping");
		Component current = getCurrentComponent();
		if (logger.isDebugEnabled()) logger.debug(this + " current component is " + current);
		current.stop();
	}
	
	public void suspend() {	
		if (logger.isDebugEnabled()) logger.debug(this + " suspending");
		Component current = getCurrentComponent();
		if (logger.isDebugEnabled()) logger.debug(this + " current component is " + current);
		current.suspend();
	}
	
	public void resume() {
		if (logger.isDebugEnabled()) logger.debug(this + " resuming");
		Component current = getCurrentComponent();
		if (logger.isDebugEnabled()) logger.debug(this + " current component is " + current);
		current.resume();
	}
		
  /**
   * Forwards events received from external sources to the
   * components of this composite.
   * @param evt the event received.
   */
  public void propertyChange(PropertyChangeEvent evt) {
		if (logger.isDebugEnabled()) logger.debug(this + " received external event " + evt);
		getCurrentComponent().propertyChange(evt);
	}
  
  /**
   * A dedicated listener that forwards the event to all external listeners.
   */
  protected class ForwardListener implements PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent evt) {
    		if (logger.isDebugEnabled()) logger.debug(this + " emitting internal event " + evt);
      getExternalListeners().firePropertyChange(evt);
    }
  }
  
  /**
   * A termination listener that, when the current component terminates,
   * starts the next component in the sequence.  
   */
	protected class SequenceTerminationListener implements TerminationListener {
		public void componentTerminated(EventObject event) {
			if (currentComponentIndex < components.size() - 1) {
				switchToNextComponent();
				getCurrentComponent().start(null);
				if (logger.isDebugEnabled()) logger.debug(this + " switched to next component " + getCurrentComponent());
			} else {
				disconnectComponent(getCurrentComponent());
				fireTermination();
			}
		}
	}
}

