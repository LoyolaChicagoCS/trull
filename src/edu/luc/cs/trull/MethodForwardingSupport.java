package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;

/**
 * Method forwarding support for implementing Composite.  
 * The provided methods are distributed (forwarded) to each 
 * component in the list.
 */
class MethodForwardingSupport implements Startable, Suspendable {

  /**
   * Constructs a method forwarding support instance for the given list of components.
   * @param components the list of components.
   */
	public MethodForwardingSupport(List components) {
		this.components = components;
	}
	
  /**
   * The list of components to which to forward the methods.
   */
	private List components;
	
	public void start(PropertyChangeEvent incoming) {
		Iterator i = components.iterator();
		while (i.hasNext()) {
			((Component) i.next()).start(incoming);
		}
	}
	
	public void stop() { 
		Iterator i = components.iterator();
		while (i.hasNext()) {
			((Component) i.next()).stop();
		}
	}
	
	public void suspend() {	
		Iterator i = components.iterator();
		while (i.hasNext()) {
			((Component) i.next()).suspend();
		}
	}
	
	public void resume() {
		Iterator i = components.iterator();
		while (i.hasNext()) {
			((Component) i.next()).resume();
		}
	}
}
