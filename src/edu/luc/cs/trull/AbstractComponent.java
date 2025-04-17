package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.apache.log4j.Logger;

/**
 * An abstract superclass for components.  In particular, this class
 * provides support for adding and removing listeners and
 * firing events to the listeners.
 */
public abstract class AbstractComponent extends TerminatingComponent {
    
  private static final Logger logger = Logger.getLogger(AbstractComponent.class);

  /**
   * Sole constructor.
   */
  public AbstractComponent() { }
  
	/**
	 * The external listeners of this component.
	 */
  private PropertyChangeMulticaster listeners = new PropertyChangeMulticaster(this);

	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}
	
	public PropertyChangeListener[] getPropertyChangeListeners() {
		return listeners.getPropertyChangeListeners();
	}
	
	/**
	 * Returns the external listeners as a single multicaster.
   * @return the multicaster object.  
	 */
	protected PropertyChangeMulticaster getExternalListeners() {
		return listeners;
	}
  
	/**
	 * Fires a PropertyChangeEvent whose property name is the given label.
   * @param label the property name of the event to be fired. 
	 */
	protected void firePropertyChange(final String label) {
		firePropertyChange(label, null);
	}

	/**
	 * Fires a PropertyChangeEvent whose property name is the given label
	 * and whose new value is the given object. 
   * @param label the property name of the event to be fired.
   * @param value the new property value of the event to be fired.
	 */
	protected void firePropertyChange(final String label, final Object value) {
		firePropertyChange(
				new PropertyChangeEvent(this, label, null, value == null ? label : value));
	}
	
	/**
	 * Fires the given PropertyChangeEvent.
   * @param event the event to be fired.
	 */
	protected void firePropertyChange(PropertyChangeEvent event) {
    if (logger.isInfoEnabled()) logger.info(this + " emitting " + Util.toString(event));
		listeners.firePropertyChange(event);
	}
}
