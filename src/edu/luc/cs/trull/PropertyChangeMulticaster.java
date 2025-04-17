package edu.luc.cs.trull;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeListenerProxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * A multicaster for PropertyChangeEvents based on Doug Lea's efficient
 * copy-on-write implementation but with the missing getPropertyChangeListeners
 * methods added.
 */

public class PropertyChangeMulticaster 
extends EDU.oswego.cs.dl.util.concurrent.PropertyChangeMulticaster {

	public PropertyChangeMulticaster(Object source) {
		super(source);
	}
	
	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {

		if (listener == null) throw new NullPointerException();

		PropertyChangeMulticaster child = null;

		synchronized (this) {
			if (children == null)
				children = new HashMap();
			else
				child = (PropertyChangeMulticaster) children.get(propertyName);

			if (child == null) {
				child = new PropertyChangeMulticaster(source);
				children.put(propertyName, child);
			}
		}

		child.addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListenerIfAbsent(String propertyName,
			PropertyChangeListener listener) {

		if (listener == null) throw new NullPointerException();

		PropertyChangeMulticaster child = null;

		synchronized (this) {
			if (children == null)
				children = new HashMap();
			else
				child = (PropertyChangeMulticaster) children.get(propertyName);

			if (child == null) {
				child = new PropertyChangeMulticaster(source);
				children.put(propertyName, child);
			}
		}

		child.addPropertyChangeListenerIfAbsent(listener);
	}

  /**
   * Returns an array of all the listeners that were added to this 
   * PropertyChangeSupport object with addPropertyChangeListener. 
   * @return all of the PropertyChangeListeners added 
   * or an empty array if no listeners have been added.
   */
  public PropertyChangeListener[] getPropertyChangeListeners() {
		List returnList = null; 
			
		synchronized (this) {
			// Add all the PropertyChangeListeners
			returnList = Arrays.asList(listeners);

			// Add all the PropertyChangeListenerProxys
			if (children != null) {
				Iterator iterator = children.keySet().iterator();
				while (iterator.hasNext()) {
					String key = (String) iterator.next();
					PropertyChangeMulticaster child = 
						(PropertyChangeMulticaster) children.get(key);
					PropertyChangeListener[] childListeners = 
						child.getPropertyChangeListeners();
					for (int index = childListeners.length - 1; index >= 0; index--) {
						returnList.add(new PropertyChangeListenerProxy(key, childListeners[index]));
					}
				}
			}
		}
		
		return (PropertyChangeListener[]) 
			returnList.toArray(new PropertyChangeListener[0]);
	}

  /**
   * Returns an array of all the listeners that have been associated 
   * with the named property.
   * @return all of the PropertyChangeListeners associated with the named property 
   * or an empty array if no listeners have been added.
   */
	public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
		List returnList = new ArrayList();

		synchronized (this) {
			if (children != null) {
				PropertyChangeMulticaster child = 
					(PropertyChangeMulticaster) children.get(propertyName);
				if (child != null) {
					returnList.addAll(Arrays.asList(child.getPropertyChangeListeners()));
				}
			}
		}
		
		return (PropertyChangeListener[]) 
			returnList.toArray(new PropertyChangeListener[0]);
	}
}
