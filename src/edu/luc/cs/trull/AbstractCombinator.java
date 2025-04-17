package edu.luc.cs.trull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * An abstract superclass for combinators with the ability
 * to manage a list of Trull components.  Combinators are interior nodes 
 * in a component tree.
 */
public abstract class AbstractCombinator extends AbstractComponent implements Combinator {  

	private static final Logger logger = Logger.getLogger(AbstractCombinator.class);

  /**
   * Sole constructor.
   */
  public AbstractCombinator() { }
    
	/**
	 * The list of child components of this combinator.
	 */
  protected List components = new ArrayList();
  
	/**
	 * Sets the child components of this combinator.
	 */ 
  public synchronized void setComponents(List components) {
    this.components.clear();
    Iterator i = components.iterator();
    while (i.hasNext()) {
      addComponent((Component) i.next());
    }
  }
  	
  	/**
   * Adds a component to this combinator.
   * @param comp the component to be added.
   * @param constraints additional information on how the component should be added.
  	 * @throws IllegalArgumentException if the specified component is null.
  	 * @throws UnsupportedOperationException if this method is not overridden by the subclass.
  	 */
	public synchronized void addComponent(Component comp, Object constraints) {
		if (comp == null) {
			throw new IllegalArgumentException("cannot add null component");
		}
		throw new UnsupportedOperationException("not supported");
	}
	
	/**
   * Adds a component to this combinator.
   * @param comp the component to be added.
   * @throws IllegalArgumentException if the specified component is null.
	 */
	public synchronized void addComponent(Component comp) {
		if (comp == null) {
			throw new IllegalArgumentException("cannot add null component");
		}
		components.add(comp);
	}
	
  /**
   * Adds a component to this combinator at the specified position.
   * @param comp the component to be added.
   * @param pos the position where the component should be added.
   * @throws IllegalArgumentException if the specified component is null.
   */
	protected synchronized void addComponent(Component comp, int pos) {
    if (comp == null) {
        throw new IllegalArgumentException("cannot add null component");
    }
		components.add(pos, comp);
	}

  /**
   * Returns the component at the specified position in this combinator.
   * @return the component at the specified position.
   */
	public synchronized Component getComponent(int i) {
		return (Component) components.get(i);
	}

  /**
   * Returns an array of all components in this combinator.
   * @return an array of all components.
   */
	public synchronized List getComponents() {
		return Collections.unmodifiableList(components);
	}
	
  /**
   * Returns the number of components currently in this combinator.
   * @return the number of components. 
   */
	public synchronized int getComponentCount() {
		return components.size();
	}

  /**
   * Removes all components in this combinator.
   */
	public synchronized void removeAllComponents() {
		// use the subclass's version of removeComponent!!!
		Iterator i = components.iterator();
		while (i.hasNext()) {
			removeComponent((Component) i.next());
		}
	}

  /**
   * Removes the specified component from this combinator.
   * @param comp the component to be removed.
   */
	public synchronized void removeComponent(Component comp) {
		int pos = components.indexOf(comp);
		if (pos >= 0) {
			removeComponent(pos);
		}
	}

  /**
   * Removes the component at the specified position from this combinator.
   * @param pos the position of the component to be removed.
   */
	protected synchronized void removeComponent(int pos) {
		components.remove(pos);
	}

	/**
	 * Propagates the provided scheduler to the children of this component.
	 */
	public synchronized void setScheduler(final Scheduler scheduler) {
	  super.setScheduler(scheduler);
	  Iterator i = components.iterator();
		while (i.hasNext()) {
		  Component c = (Component) i.next();
		  if (c instanceof SchedulerAware) {
		    ((SchedulerAware) c).setScheduler(scheduler); 
		  }
		}
	}
	
	/**
	 * Returns the internal listener that is notified if 
	 * the child component terminates.
   * @return the termination listener.
	 */
	protected abstract TerminationListener getChildTerminationListener();

	/**
	 * A default termination listener.
   * The default termination behavior is propagation from the child to the 
	 * parent: when the child terminates, the parent also terminates.
	 */
	protected class DefaultTerminationListener implements TerminationListener {
		public void componentTerminated(EventObject event) {
			fireTermination();
		}
	}
}
