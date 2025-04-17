package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A combinator for event renaming and event hiding.
 * It provides an implementation of consistent renaming of event labels 
 * within a given component from any event labels seen externally.
 * @see edu.luc.cs.trull.Local
 */
public class Rename extends SingleChildCombinator {  

	private static final Logger logger = Logger.getLogger(Rename.class);

  /**
   * Constructs a Rename component.  Specific properties can be changed later.
   */
	public Rename() { }

  /**
   * Constructs a Rename component.
   * @param internalLabels the array of original event labels before the renaming.
   * @param externalLabels the array of resulting event labels after the renaming.
   * @param body the body component.
   */
	public Rename(String[] internalLabels, String[] externalLabels, Component body) {
		super(body);
		for (int i = 0; i < internalLabels.length; i ++) {
			addEventRenaming(internalLabels[i], externalLabels[i]);
		}
	}
	
  /**
   * <code>Rename([internalLabel], [externalLabel], body)</code>  
   */
  public Rename(String internalLabel, String externalLabel, Component body) {
    this(new String[] { internalLabel }, new String[] { externalLabel }, body);
  }
    
  /**
   * <code>Rename([], [], body)</code>  
   */
  public Rename(Component body) {
    this(new String[0], new String[0], body);
  }

	protected Component getDefaultChild() {
		return Nil.getInstance();
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
	 * The set of hidden events local to this component.
	 */	
	private Set localEvents = new HashSet();
	
	/**
	 * A hashmap for inside events that needs to be renamed 
	 * when leaving the component.
	 */
	private Map renameEventsInside = new HashMap();
	
	/**
	 * A hashmap for outside eventsthat needs to be renamed
	 * when entering the component.
	 */
	private Map renameEventsOutside = new HashMap();
	
  /**
   * Sets the event label to be hidden.
   * @param label the event label to be hidden.
   */
	public synchronized void setLocalEvent(String label) {
		setLocalEvents(Collections.singleton(label));
	}

  /**
   * Sets the event labels to be hidden.
   * @param evts the collection of event labels to be hidden.
   */
	public synchronized void setLocalEvents(Collection evts) {
		localEvents.clear();
		localEvents.addAll(evts);
	}

  /**
   * Sets the event labels to be hidden.
   * @param labels the array of event labels to be hidden.
   */
	public synchronized void setLocalEvents(String[] labels) {
		setLocalEvents(Arrays.asList(labels));
	}
	
  /**
   * Returns the event labels that have been hidden.
   * @return the array of event labels that have been hidden.
   */
	public synchronized String[] getLocalEvents() {
		return (String[]) localEvents.toArray(new String[0]);
	}

  /**
   * Adds an event renaming to this component.  An incoming externalEvent 
   * is seen by the component as an internalEvent; an outgoing internalEvent
   * is seen by the outside world as an externalEvent. 
   * @param internalEvent the original event seen inside the component.
   * @param externalEvent the corresponding event seen outside the component.
   */
	public synchronized void addEventRenaming(String internalEvent, String externalEvent) {
		if (renameEventsInside.containsKey(internalEvent) ||
				renameEventsInside.containsKey(externalEvent)) {
			throw new IllegalArgumentException("inconsistent renaming");
		}
		renameEventsInside.put(internalEvent, externalEvent);
		renameEventsOutside.put(externalEvent, internalEvent);
	}
	
  /**
   * Removes the event renaming for the given internal event label.
   * @param internalEvent the internal event label.
   */
	public synchronized void removeEventRenaming(String internalEvent) {
		if (renameEventsInside.containsKey(internalEvent)) {
			renameEventsOutside.remove(renameEventsInside.get(internalEvent));
			renameEventsInside.remove(internalEvent);
		} else {
			renameEventsInside.remove(renameEventsOutside.get(internalEvent));
			renameEventsOutside.remove(internalEvent);
		}
	}

  /**
   * Returns the array of event renamings currently in effect.
   * @return the array of event renamings.
   */
	public synchronized Map.Entry[] getEventRenamings() {
		return (Map.Entry[]) renameEventsInside.entrySet().toArray(new Map.Entry[0]); 
	}
	
	/**
	 * Processes incoming events by forwarding them to the child
	 * component.
	 * @param evt the event received.  
	 */  
	public void propertyChange(PropertyChangeEvent evt) {
		String label = evt.getPropertyName();
		if (localEvents.contains(label)) {
			if (logger.isDebugEnabled()) logger.debug(this + " blocking hidden external event " + evt);
		} else if (renameEventsInside.containsKey(label)) {
			if (logger.isDebugEnabled()) logger.debug(this + " ignoring renamed external event " + evt);
		} else {
			if (logger.isDebugEnabled()) logger.debug(this + " received external event " + evt);
			PropertyChangeEvent newEvent = evt;
			if (renameEventsOutside.containsKey(label)) {
				String newLabel = (String) renameEventsOutside.get(label);
				if (logger.isDebugEnabled()) logger.debug(this + " renaming " + label + " to " + newLabel);
				newEvent = new PropertyChangeEvent(evt.getSource(), newLabel, evt.getOldValue(), evt.getNewValue()); 
			}
			getChild().propertyChange(newEvent);
		}
	}
	
	/**   
	 * A dedicated listener that forwards the event to all external listeners.
	 */  
	protected class LocalListener implements PropertyChangeListener {    
		public void propertyChange(PropertyChangeEvent evt) {    
			String label = evt.getPropertyName();
			if (localEvents.contains(label)) {
				if (logger.isDebugEnabled()) logger.debug(this + " blocking hidden internal event " + evt);
			} else {
				if (logger.isDebugEnabled()) logger.debug(this + " emitting internal event " + evt);
				PropertyChangeEvent newEvent = evt;
				if (renameEventsInside.containsKey(label)) {
					String newLabel = (String) renameEventsInside.get(label);
					if (logger.isDebugEnabled()) logger.debug(this + " renaming " + label + " to " + newLabel);
					newEvent = new PropertyChangeEvent(evt.getSource(), newLabel, evt.getOldValue(), evt.getNewValue()); 
				}
				getExternalListeners().firePropertyChange(newEvent);  
			}
		}
	}
}
