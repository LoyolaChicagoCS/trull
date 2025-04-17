package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * An empty Trull component implementation.  It can be used as is
 * or as a starting point for more interesting components.
 */
public class EmptyComponent implements Component {

	public void propertyChange(PropertyChangeEvent evt) { }

	public void addPropertyChangeListener(PropertyChangeListener l) { }

	public void removePropertyChangeListener(PropertyChangeListener l) { }

	public PropertyChangeListener[] getPropertyChangeListeners() {
		return new PropertyChangeListener[0];
	}
	
	public void setTerminationListener(TerminationListener l) { }
	
	public TerminationListener getTerminationListener() { return null; }

	public void start(PropertyChangeEvent incoming) { }
	
	public void stop() { }
	
	public void suspend() { }
	
	public void resume() { }
}
