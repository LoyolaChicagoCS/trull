package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A combinator for mapping sets of events 
 * to a component's lifecycle methods suspend and resume.
 * It behaves as follows.
 * First, the body component is executed automatically.
 * If, before the body terminates,
 * an event occurs whose label matches the suspend labels,
 * then the execution of the body is suspended.  If,
 * while the body is suspended,
 * an event occurs whose label matches the resume labels,
 * then the execution of the body is resumed where
 * it had been suspended.
 * @see edu.luc.cs.trull.Control
 * @see edu.luc.cs.trull.Watching
 */
public class Suspend extends SingleChildCombinator {  

	private static final Logger logger = Logger.getLogger(Suspend.class);

  /**
   * Constructs a Suspend component.  Specific properties can be changed later.
   */
	public Suspend() { }

  /**
   * Constsructs a Suspend component.
   * @param suspendLabels the event labels on which the body is suspended. 
   * @param suspendAction the action invoked upon suspending.  May be null.
   * @param resumeLabels the event labels on which the body is resumed.
   * @param resumeAction the action invoked upon resuming.  May be null.
   * @param body the body of the component.  If null, Nil is used.
   */
  public Suspend(String[] suspendLabels, PropertyChangeListener suspendAction, 
			String[] resumeLabels, PropertyChangeListener resumeAction,
			Component body) {
		super(body);
		setSuspendEvents(suspendLabels);
		setSuspendAction(suspendAction);
		setResumeEvents(resumeLabels);
		setResumeAction(resumeAction);
	}

  /**
   * <code>Suspend([suspendLabel], suspendAction, [resumeLabel], resumeAction, body)</code>
   */
	public Suspend(String suspendLabel, PropertyChangeListener suspendAction, 
			String resumeLabel, PropertyChangeListener resumeAction,
			Component body) {
		this(new String[] { suspendLabel }, suspendAction,
				new String[] { resumeLabel }, resumeAction,
				body);
	}

  /**
   * <code>Suspend([suspendLabel], null, [resumeLabel], null, body)</code>
   */
	public Suspend(String[] suspendLabels, String[] resumeLabels, 	Component body) {
		this(suspendLabels, null, resumeLabels, null, body);
	}

  /**
   * <code>Suspend([suspendLabel], [resumeLabel], body)</code>
   */
	public Suspend(String suspendLabel, String resumeLabel, 	Component body) {
		this(suspendLabel, null, resumeLabel, null, body);
	}

  /**
   * <code>Suspend([], [], body)</code>
   */
  public Suspend(Component body) {
    this(new String[0], null, new String[0], null, body);
  }
    
	/**
	 * The current state of this component.
	 */
	private boolean suspended = false;
	
	protected Component getDefaultChild() {
		return Nil.getInstance();
	}

	/**
	 * The internal listener that listens to the child component.
	 */
	private PropertyChangeListener internalListener;
	
	protected PropertyChangeListener getInternalListener() {
		if (internalListener == null) {
			internalListener = new LocalListener();
		}
		return internalListener;
	}

  /**
   * The child termination listener for this component.
   */
	private TerminationListener terminationListener;

	protected TerminationListener getChildTerminationListener() {
		if (terminationListener == null) {
			terminationListener = new SuspendTerminationListener();
		}
		return terminationListener;
	}

	/**
	 * The set of triggering events for suspending this component.
	 */	
	private Set suspendEvents = new HashSet();
	
	/**
	 * The set of triggering events for resuming this component.
	 */	
	private Set resumeEvents = new HashSet();
	
	/**
	 * An action to be invoked when a susupend event is received.
	 */
	private PropertyChangeListener suspendAction;
	
	/**
	 * An action to be invoked when a resume event is received.
	 */
	private PropertyChangeListener resumeAction;

  /**
   * Sets the suspending event label for this component to the given string. 
   * @param label the suspending event label.
   */
	public synchronized void setSuspendEvent(String label) {
		setSuspendEvents(Collections.singleton(label));
	}

  /**
   * Sets the suspending event labels for this component to the given collection of strings. 
   * @param evts the suspending event labels.
   */
	public synchronized void setSuspendEvents(Collection evts) {
		suspendEvents.clear();
		suspendEvents.addAll(evts);
	}

  /**
   * Sets the suspending event labels for this component to the given array of strings. 
   * @param labels the suspending event labels.
   */
	public synchronized void setSuspendEvents(String[] labels) {
		setSuspendEvents(Arrays.asList(labels));
	}

  /**
   * Returns the array of suspending event labels for this component. 
   * @return the suspending event labels.
   */
	public synchronized String[] getSuspendEvents() {
		return (String[]) suspendEvents.toArray(new String[0]);
	}

  /**
   * Sets the resuming event label for this component to the given string. 
   * @param label the resuming event label.
   */
    public synchronized void setResumeEvent(String label) {
        setResumeEvents(Collections.singleton(label));
    }
    
  /**
   * Sets the resuming event labels for this component to the given collection of strings. 
   * @param evts the resuming event labels.
   */
	public synchronized void setResumeEvents(Collection evts) {
		resumeEvents.clear();
		resumeEvents.addAll(evts);
	}

  /**
   * Sets the resuming event labels for this component to the given array of strings. 
   * @param labels the resuming event labels.
   */
	public synchronized void setResumeEvents(String[] labels) {
		setResumeEvents(Arrays.asList(labels));
	}

  /**
   * Returns the array of resuming event labels for this component. 
   * @return the resuming event labels.
   */
	public synchronized String[] getResumeEvents() {
		return (String[]) resumeEvents.toArray(new String[0]);
	}

  /**
   * Sets the action invoked upon receiving a suspending event.
   * @param action the suspend action.
   */
	public synchronized void setSuspendAction(PropertyChangeListener action) {
		suspendAction = action;
	}
	
  /**
   * Returns the action invoked upon receiving a suspending event.
   * @return the suspend action.
   */
	public synchronized PropertyChangeListener getSuspendAction() {
		return suspendAction;
	}

  /**
   * Sets the action invoked upon receiving a resuming event.
   * @param action the resume action.
   */
	public synchronized void setResumeAction(PropertyChangeListener action) {
		resumeAction = action;
	}
	
  /**
   * Returns the action invoked upon receiving a resuming event.
   * @return the resume action.
   */
	public synchronized PropertyChangeListener getResumeAction() {
		return resumeAction;
	}

	public void start(PropertyChangeEvent incoming) {
		super.start(incoming);
		suspended = false;
	}

  /**
   * Suspends the child component.
   * @param incoming the suspending event.
   */
	protected void suspendChild(PropertyChangeEvent incoming) { 
		if (logger.isDebugEnabled()) logger.debug(this + " suspending child");
		suspended = true;
		if (suspendAction != null) {
			suspendAction.propertyChange(incoming);
		}
		getChild().suspend();
	}
	
  /**
   * Resumes the child component.
   * @param incoming the resuming event.
   */
	protected void resumeChild(PropertyChangeEvent incoming) { 
		if (logger.isDebugEnabled()) logger.debug(this + " resuming child");
		suspended = false;
		if (resumeAction != null) {
			resumeAction.propertyChange(incoming);
		}
		getChild().resume();
	}
	
	/**
	 * Forwards incoming events to the child component.   
	 * @param evt the event received.  
	 */  
	public void propertyChange(PropertyChangeEvent evt) {
		String label = evt.getPropertyName();
		if (! suspended) {
			if (suspendEvents.contains(label)) {
				suspendChild(evt);
			}	else {
				if (logger.isDebugEnabled()) logger.debug(this + " emitting internal event " + evt);
				getChild().propertyChange(evt);
			}
		} else if (resumeEvents.contains(label)) {
			resumeChild(evt);
		}
	}
	
	/**   
	 * A dedicated listener that forwards the event to all external listeners.
	 */  
	protected class LocalListener implements PropertyChangeListener {    
		public void propertyChange(PropertyChangeEvent evt) {
			String label = evt.getPropertyName();
			// a child can suspend but not resume itself
			if (! suspended) {
				if (suspendEvents.contains(label)) {
					suspendChild(evt);
				}	else {
					if (logger.isDebugEnabled()) logger.debug(this + " emitting internal event " + evt);
					getExternalListeners().firePropertyChange(evt);  
				}
			} else {
				if (logger.isDebugEnabled()) logger.debug(this + " suspended, ignoring internal event " + evt);
			}
		}
	}

  /**
   * This component terminates when its child component terminates. 
   */
	protected class SuspendTerminationListener implements TerminationListener {
		public void componentTerminated(EventObject event) {
			if (suspended) {
				if (logger.isDebugEnabled()) logger.debug(this + " suspended, ignoring termination from " + event.getSource());
				return;
			}
			if (logger.isDebugEnabled()) logger.debug(this + " terminating after termination from " + event.getSource());
			fireTermination();
		}
	}
}
