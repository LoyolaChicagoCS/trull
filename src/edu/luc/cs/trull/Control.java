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
 * A combinator that maps sets of events 
 * to a component's lifecycle methods start and stop.
 * It behaves as follows: initially, it waits and does nothing.
 * If and when any it receives one of the starting events, 
 * it starts the body component.  If the body reaches termination,
 * this component also terminates.  However, if it receives a stopping event
 * before the body terminates, it stops the body and starts the handler;
 * in this case, this component terminates when the handler terminates.
 * This combinator is a generalization of AwaitOne and Watching, 
 * which are provided for convenience as subtypes of this combinator.
 * <PRE>
 * await START ->
 *   do 
 *     body
 *   watching STOP ->
 *     handler
 * </PRE>
 * @see edu.luc.cs.trull.AwaitOne
 * @see edu.luc.cs.trull.Watching
 */
public class Control extends AbstractCombinator {
	
  private static final Logger logger = Logger.getLogger(Control.class);

	/**
	 * This constraint object indicates that the component should be added as the body. 
	 */
	public static final Object BODY = new Object();
	
	/**
	 * This constraint object indicates that the component should be added as the handler. 
	 */
	public static final Object HANDLER = new Object();

  /**
   * Constructs a Control component whose body
   * is an non-terminating empty component (Nil) and
   * whose handler is an empty component that terminates immediately (Done).
   * Specific properties can be changed later. 
   * @see Nil
   * @see Done
   */
	public Control() {
		addComponent(getDefaultBody(), BODY);
		addComponent(getDefaultHandler(), HANDLER);
	}
	
  /**
   * Constructs a Control component.
   * @param startLabels the array of event labels that cause the body to start.
   * @param startAction the action invoked when a start event is received.  May be null.
   * @param body the body component to be started after a start event is received.  If null, a Nil component is used.
   * @param stopLabels the array of event labels that cause the body to stop.
   * @param stopAction the action invoked when a stop event is received.  May be null.
   * @param handler the component to be started after a stop event is received.  If null, a Done component is used.
   */
	public Control(String[] startLabels, PropertyChangeListener startAction, 
			Component body, 
			String[] stopLabels, PropertyChangeListener stopAction, 
			Component handler) {
		addComponent(body, BODY);
		addComponent(handler, HANDLER);
		setStartEvents(startLabels);
		setStartAction(startAction);
		setStopEvents(stopLabels);
		setStopAction(stopAction);
	}

  /**
   * <code>Control(startLabels, null, body, stopLabels, null, handler)</code>
   */
	public Control(String[] startLabels, Component body, 
			String[] stopLabels, Component handler) {
		this(startLabels, null, body, stopLabels, null, handler);
	}

  /**
   * <code>Control([startLabel], startAction, body, [stopLabel], stopAction, handler)</code>
   */
	public Control(String startLabel, PropertyChangeListener startAction, 
			Component body, 
			String stopLabel, PropertyChangeListener stopAction, 
			Component handler) {
		this(new String[] { startLabel }, startAction, body,
				new String[] { stopLabel }, stopAction, handler);	
	}

  /**
   * <code>Control([startLabel], body, [stopLabel], handler)</code>
   */
	public Control(String startLabel, Component body, 
			String stopLabel, Component handler) {
		this(new String[] { startLabel }, body, new String[] { stopLabel }, handler);	
	}

  /**
   * <code>Control(startLabels, body, stopLabels, new Done())</code>
   */
  public Control(String[] startLabels, Component body, String[] stopLabels) {
        this(startLabels, body, stopLabels, new Done());
    }

  /**
   * <code>Control([startLabel], body, [stopLabel], new Done())</code>
   */
  public Control(String startLabel, Component body, String stopLabel) {
    this(new String[] { startLabel }, body, new String[] { stopLabel });   
  }

	private static final int STOPPED = 1;
	private static final int RUNNING = 2;
	private static final int TIMEOUT = 4;

	private Done DONE = new Done();
	
	/**
	 * The current state of this component.
	 */
	private int state = STOPPED;
	
	/**
	 * The internal listener that listens to the child component.
	 */
	private PropertyChangeListener bodyListener = new BodyListener();
	
	/**
	 * The internal listener that listens to the handler component.
	 */
	private PropertyChangeListener handlerListener = new HandlerListener();
	
	/**
	 * The set of triggering events for starting this component.
	 */	
	private Set startEvents = new HashSet();
	
	/**
	 * The set of triggering events for stopping this component.
	 */	
	private Set stopEvents = new HashSet();
	
	/**
	 * An action to be invoked when a start event is received.
	 */
	private PropertyChangeListener startAction;
	
	/**
	 * An action to be invoked when a stop event is received.
	 */
	private PropertyChangeListener stopAction;
	
	protected Component getDefaultBody() {
		return Nil.getInstance();
	}
	
	protected Component getDefaultHandler() {
		return DONE;
	}
	
	public synchronized void addComponent(Component comp, Object constraint) {
		if (constraint == BODY) {
			setBody(comp);
		} else if (constraint == HANDLER) {
			setHandler(comp);
		} else {
			throw new IllegalArgumentException("unknown constraint");
		}
	}
	
  /**
   * This method is not supported by this class.
   * <code>addComponent(Component comp, Object constraints)</code>
   * should be used instead.
   */
	public void addComponent(Component comp) {
		throw new UnsupportedOperationException("should use constraint");
	}
	
	/**  
	 * Sets the child component.   
	 * @param c the child component.   
	 */  
	protected void setBody(Component c) {
		Component body = null; 
		if (getComponentCount() > 0) {
			body = getBody();
			body.removePropertyChangeListener(bodyListener);
			body.setTerminationListener(null);
			super.removeComponent(0);
		}
		body = c == null ? getDefaultBody() : c;
		body.addPropertyChangeListener(bodyListener);
		body.setTerminationListener(bodyTerminationListener);
		super.addComponent(body, 0);
	} 

	/**  
	 * Sets the handler component.   
	 * @param c the handler component.   
	 */  
	protected void setHandler(Component c) {
		Component handler = null;
		if (getComponentCount() > 1) {
			handler = getHandler();
			handler.removePropertyChangeListener(handlerListener);
			handler.setTerminationListener(null);
			super.removeComponent(1);
		}
		handler = c == null ? getDefaultHandler() : c;
		handler.addPropertyChangeListener(handlerListener);
		handler.setTerminationListener(handlerTerminationListener);
		super.addComponent(handler, 1);
	} 

  /**
   * Returns the body of this component.
   * @return the body of this component.
   */
	protected Component getBody() {
		return getComponent(0);
	}
	
  /**
   * Returns the handler of this component.
   * @return the handler of this component.
   */
	protected Component getHandler() {
		return getComponent(1);
	}
	
  /**
   * Sets the starting event label for this component to the given string. 
   * @param label the starting event label.
   */
	public synchronized void setStartEvent(String label) {
		setStartEvents(Collections.singleton(label));
	}

  /**
   * Sets the starting event labels for this component to the given collection of strings. 
   * @param evts the collection of starting event labels.
   */
	public synchronized void setStartEvents(Collection evts) {
		startEvents.clear();
		startEvents.addAll(evts);
	}

  /**
   * Sets the starting event labels for this component to the given array. 
   * @param labels the array of starting event labels.
   */
	public synchronized void setStartEvents(String[] labels) {
		setStartEvents(Arrays.asList(labels));
	}

  /**
   * Returns an array of starting event labels for this component. 
   * @return label the array of starting event labels.
   */
	public synchronized String[] getStartEvents() {
		return (String[]) startEvents.toArray(new String[0]);
	}
	
  /**
   * Sets the stopping event label for this component to the given string. 
   * @param label the stopping event label.
   */
	public synchronized void setStopEvent(String label) {
		setStopEvents(Collections.singleton(label));
	}

  /**
   * Sets the stopping event labels for this component to the given collection of strings. 
   * @param evts the collection of stopping event labels.
   */
	public synchronized void setStopEvents(Collection evts) {
		stopEvents.clear();
		stopEvents.addAll(evts);
	}

  /**
   * Sets the stopping event labels for this component to the given array. 
   * @param labels the array of stopping event labels.
   */
	public synchronized void setStopEvents(String[] labels) {
		setStopEvents(Arrays.asList(labels));
	}
	
  /**
   * Returns an array of stopping event labels for this component. 
   * @return label the array of stopping event labels.
   */
	public synchronized String[] getStopEvents() {
		return (String[]) stopEvents.toArray(new String[0]);
	}
	
  /**
   * Sets the action for this component to be executed when
   * a starting event has occurred.
   * @param action the start action for this component.  May be null.
   */
	public synchronized void setStartAction(PropertyChangeListener action) {
		startAction = action;
	}

  /**
   * Returns the action for this component to be executed when
   * a starting event has occurred.
   * @return the start action for this component.
   */
	public synchronized PropertyChangeListener getStartAction() {
		return startAction;
	}
	
  /**
   * Sets the action for this component to be executed when
   * a stopping event has occurred.
   * @param action the stop action for this component.  May be null.
   */
	public synchronized void setStopAction(PropertyChangeListener action) {
		stopAction = action;
	}

  /**
   * Returns the action for this component to be executed when
   * a stopping event has occurred.
   * @return the stop action for this component.
   */
	public synchronized PropertyChangeListener getStopAction() {
		return stopAction;
	}
	
	public void start(PropertyChangeEvent incoming) {
		if (logger.isDebugEnabled()) logger.debug(this + " starting");
		state = STOPPED;
		if (startEvents.isEmpty()) {
			startChild(incoming);
		}
	}
	
	public void stop() {
		if (logger.isDebugEnabled()) logger.debug(this + " stopping");
		if (state == RUNNING) {
     	getBody().stop();
	  } else if (state == TIMEOUT) {
   	  getHandler().stop();
	  }
	}
	
  /**
   * Starts the body of this component.
   * @param incoming the triggering event.
   */
	protected void startChild(PropertyChangeEvent incoming) { 
		if (logger.isDebugEnabled()) logger.debug(this + " starting child");
		state = RUNNING;
		if (startAction != null) {
			startAction.propertyChange(incoming);
		}
		getBody().start(incoming);
	}
	
  /**
   * Stops the body of this component and starts its handler.
   * @param incoming the triggering event.
   */
	protected void stopChild(PropertyChangeEvent incoming) { 
		if (logger.isDebugEnabled()) logger.debug(this + " stopping child");
		getBody().stop();
		state = TIMEOUT;
		if (stopAction != null) {
			stopAction.propertyChange(incoming);
		}
		if (logger.isDebugEnabled()) logger.debug(this + " starting handler");
		getHandler().start(incoming);
	}
	
	public void suspend() { 
		if (logger.isDebugEnabled()) logger.debug(this + " suspending");
		if (state == RUNNING) {
	    getBody().suspend();
		} else if (state == TIMEOUT) {
     	getHandler().suspend();
		}
	}
	
	public void resume() {
		if (logger.isDebugEnabled()) logger.debug(this + " resuming");
		if (state == RUNNING) {
			getBody().resume();
   	} else if (state == TIMEOUT) {
   		getHandler().resume();
   	}
	}
	
	/**
	 * The internal listener that is notified if the child component terminates.
	 */
	private final TerminationListener bodyTerminationListener = new TerminationListener() {
		public void componentTerminated(EventObject event) {
			if (state != RUNNING) {
				if (logger.isDebugEnabled()) logger.debug(Control.this + " not running, ignoring termination from " + event.getSource());
				return;
			} 
			if (logger.isDebugEnabled()) logger.debug(Control.this + " terminating after termination from " + event.getSource());
			fireTermination();
		}
	};
	
	/**
	 * The internal listener that is notified if the child component terminates.
	 */
	private final TerminationListener handlerTerminationListener = new TerminationListener() {
		public void componentTerminated(EventObject event) {
			if (state != TIMEOUT) {
				if (logger.isDebugEnabled()) logger.debug(Control.this + " not running, ignoring termination from " + event.getSource());
				return;
			} 
			if (logger.isDebugEnabled()) logger.debug(Control.this + " terminating after termination from " + event.getSource());
			fireTermination();
		}
	};
	
	protected TerminationListener getChildTerminationListener() {
		// this combinator does not use the termination support provided
		// by AbstractCombinator
		return null;
	}
	
	/**
	 * Forwards incoming events to the currently active child component.   
	 * @param evt the event received.  
	 */  
	public void propertyChange(PropertyChangeEvent evt) {
		String label = evt.getPropertyName();
		switch (state) {
			case STOPPED:
				if (startEvents.contains(label)) {
					startChild(evt);
				}				
				break;
			case RUNNING:
				if (stopEvents.contains(label)) {
					stopChild(evt);
				} else {
					if (logger.isDebugEnabled()) logger.debug(this + " received external event " + evt);
					getBody().propertyChange(evt);
				}
				break;
			case TIMEOUT:
				if (logger.isDebugEnabled()) logger.debug(this + " received external event " + evt);
				getHandler().propertyChange(evt);
				break;
		}
	}
	
	/**   
	 * A dedicated listener that forwards the event to all external listeners.
	 */  
	protected class BodyListener implements PropertyChangeListener {    
		public void propertyChange(PropertyChangeEvent evt) {
			String label = evt.getPropertyName();
			switch (state) {
				case STOPPED:
					// a child cannot start itself
					if (logger.isDebugEnabled()) logger.debug(Control.this + " not started yet, ignoring internal event " + evt);
					break;
				case RUNNING:
					if (stopEvents.contains(label)) {
						stopChild(evt);
					} else {
						if (logger.isDebugEnabled()) logger.debug(Control.this + " emitting internal event " + evt);
						getExternalListeners().firePropertyChange(evt);  
					}
					break;
				case TIMEOUT:
					if (logger.isDebugEnabled()) logger.debug(Control.this + " timed out, ignoring internal event " + evt);
					break;
			}
		}
	}

	/**   
	 * A dedicated listener that forwards the event to all external listeners.
	 */  
	protected class HandlerListener implements PropertyChangeListener {    
		public void propertyChange(PropertyChangeEvent evt) {
			String label = evt.getPropertyName();
			switch (state) {
				case STOPPED:
				case RUNNING:
					if (logger.isDebugEnabled()) logger.debug(Control.this + " not timed out yet, ignoring internal event " + evt);
					break;
				case TIMEOUT:
					if (logger.isDebugEnabled()) logger.debug(Control.this + " emitting internal event " + evt);
					getExternalListeners().firePropertyChange(evt);  
					break;
			}
		}
	}
}
