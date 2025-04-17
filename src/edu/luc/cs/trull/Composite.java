package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/*
 * TODO RESEARCH Add ability to restrict the types of events
 * for which connections are actually established.  This
 * is important for efficiency.
 */

/**
 * The composition of zero or more concurrent components
 * that communicate through PropertyChangeEvents. 
 */
public class Composite extends AbstractCombinator {  
	
	private static final Logger logger = Logger.getLogger(Composite.class);

  /**
   * Constructs an empty composite that terminates immediately.
   * Other components can be added later.
   */
	public Composite() {
		this.addComponent(new Done());
	}

  /**
   * Constructs a composite that consists of the components given in the array. 
   * @param components the array of components to be included in this composite.
   */
	public Composite(Component[] components) {
		this();
		for (int i = 0; i < components.length; i ++) {
			this.addComponent(components[i]);
		}
	}
	
  /**
   * Constructs a composite that consists of the given components.
   * @param first the first component in this composite.
   * @param second the second component in this composite.
   */ 
	public Composite(Component first, Component second) {
		this(new Component[] { first, second });
	}
	
	/**
	 * A list of the components within this composite.  The components   
	 * in this list receive events originating outside or within the composite.  
	 * Components of the composite do not receive back the event that they   
	 * send out.
	 */  
	private PropertyChangeMulticaster internalListeners = new PropertyChangeMulticaster(this);
	
	/** 
	 * A map from the components within this composite to their associated   
	 * internal forward listeners.  Each component has a dedicated listener   
	 * that forwards an outgoing event to all external listeners and all internal   
	 * components other than the component sending the event.   
	 */  
	private Map forwardListeners = new HashMap();  
	
	/**
	 * A set of those children that implement the controllable interface.
	 */
	private MethodForwardingSupport controllables = new MethodForwardingSupport(components);
	
	/**
	 * A set of all children. 
	 */
	private Set stillRunning = new HashSet();

	/**
	 * A set that keeps track of those children that haven't terminated.
	 */
	private Set allChildren = new HashSet();
	
	public synchronized void addComponent(Component c) {
		super.addComponent(c);
		// create a dedicated listener for the component    
		PropertyChangeListener l = new ForwardListener(c);
		forwardListeners.put(c, l);  
		internalListeners.addPropertyChangeListener(c);  
		c.addPropertyChangeListener(l);
		c.setTerminationListener(getChildTerminationListener());
  	  allChildren.add(c);
	} 

	public synchronized void removeComponent(Component c) {
		super.removeComponent(c);
		PropertyChangeListener l = (PropertyChangeListener) forwardListeners.remove(c); 
		internalListeners.removePropertyChangeListener(c); 
		c.removePropertyChangeListener(l);
		c.setTerminationListener(null);
	  allChildren.remove(c);
	} 
	
	public void start(PropertyChangeEvent incoming) {
		if (logger.isDebugEnabled()) logger.debug(this + " starting");
		stillRunning.clear();
		stillRunning.addAll(allChildren);
		controllables.start(incoming);
	}
	
	public void stop() { 
		if (logger.isDebugEnabled()) logger.debug(this + " stopping");
		controllables.stop();
	}
	
	public void suspend() {	
		if (logger.isDebugEnabled()) logger.debug(this + " suspending");
		controllables.suspend();
	}
	
	public void resume() {
		if (logger.isDebugEnabled()) logger.debug(this + " resuming");
		controllables.resume();
	}
	
	/**   
	 * Forwards events received from external sources to the   
	 * components of this composite.   
	 * @param evt the event received.  
	 */  
	public void propertyChange(PropertyChangeEvent evt) {    
		if (logger.isDebugEnabled()) logger.debug(this + " received external event " + evt);
		internalListeners.firePropertyChange(evt);  
	}
		
	/**   
	 * A dedicated listener   
	 * that forwards the event to all external listeners and all internal  
	 * components other than the component sending the event.   
	 */  
	protected class ForwardListener implements PropertyChangeListener {    
		private PropertyChangeListener source;   
		public ForwardListener(PropertyChangeListener source) {     
			this.source = source; 
		}    
		public void propertyChange(PropertyChangeEvent evt) {    
			if (logger.isDebugEnabled()) logger.debug(this + " emitting internal event " + evt);
			PropertyChangeListener[] listeners = internalListeners.getPropertyChangeListeners(); 
			for (int i = 0; i < listeners.length; i ++) {    
				if (listeners[i] != source) {     
					listeners[i].propertyChange(evt);     
				}    
			}    
			getExternalListeners().firePropertyChange(evt);  
		} 
	}
	
  /**
   * The child termination listener of this component.
   */
	private TerminationListener terminationListener;

	protected TerminationListener getChildTerminationListener() {
		if (terminationListener == null) {
			terminationListener = new CompositeTerminationListener();
		}
		return terminationListener;
	}
	
  /**
   * This component terminates as soon as all its children have terminated.
   */
	protected class CompositeTerminationListener implements TerminationListener {
		public void componentTerminated(EventObject event) {
			if (logger.isDebugEnabled()) logger.debug(this + " received term from child " + event.getSource());
			stillRunning.remove(event.getSource());
			if (stillRunning.isEmpty()) {
				fireTermination();
			}
		}
	}
}
