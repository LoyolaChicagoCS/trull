package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;

/**
 * An abstract superclass for Trull combinators with a single child.  
 */
public abstract class SingleChildCombinator extends AbstractCombinator {  

	private static final Logger logger = Logger.getLogger(SingleChildCombinator.class);

  /**
   * Constructs an instance with the default child component.
   */
	public SingleChildCombinator() {
		addComponent(getDefaultChild());
	}

  /**
   * Constructs an instance with the given child component.
   * @param child the child component.
   */
	public SingleChildCombinator(Component child) {
		this();
		addComponent(child);
	}

	/**
	 * Returns the default child component.
	 */
	protected abstract Component getDefaultChild();
	
	/**
	 * Returns the internal listener that listens to the child component.
	 */
	protected abstract PropertyChangeListener getInternalListener();
	
	/**
	 * Returns the current child component.
	 */
	protected Component getChild() {
		return getComponent(0);
	}
	
	/**
	 * Forwards incoming events to the child component.   
	 */  
	public void propertyChange(PropertyChangeEvent evt) {
		if (logger.isDebugEnabled()) logger.debug(this + " received external event " + evt);
		getComponent(0).propertyChange(evt);
	}

	/**
	 * Replaces the default child of the combinator with the given child.
	 */
	public synchronized void addComponent(Component child) {
		// replace default child if present
		if (getComponentCount() > 0 && getChild() == getDefaultChild()) {
			super.removeComponent(0);
		}
		if (getComponentCount() > 0) {
			throw new IllegalArgumentException("this combinator cannot have more than one child");
		}
		super.addComponent(child);
		child.addPropertyChangeListener(getInternalListener());
		child.setTerminationListener(getChildTerminationListener());
	} 
	
	/**
	 * Replaces the current child of the combinator
	 * with the default child.
	 */
	public synchronized void removeComponent(int pos) {
		Component child = getChild();
		child.removePropertyChangeListener(getInternalListener());
		child.setTerminationListener(null);
		super.removeComponent(0);
		addComponent(getDefaultChild());
	}

	public void start(PropertyChangeEvent incoming) {
		if (logger.isDebugEnabled()) logger.debug(this + " starting child " + getChild());
		getChild().start(incoming);
	}
	
	public void stop() {
		if (logger.isDebugEnabled()) logger.debug(this + " stopping");
		getChild().stop();
	}
	
	public void suspend() {
		if (logger.isDebugEnabled()) logger.debug(this + " suspending");
    getChild().suspend();
	}
	
	public void resume() {
		if (logger.isDebugEnabled()) logger.debug(this + " resuming");
    getChild().resume();
	}
}
