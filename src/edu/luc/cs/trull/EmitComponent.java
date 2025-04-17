package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * A superclass or delegation support class 
 * for components that require preemptable and 
 * suspendable event emission.  
 * To schedule events for emission, 
 * subclasses should use the <code>scheduleEvent</code> methods from within
 * lifecycle or event handling methods.
 * If this component is stopped before the event has been fired,
 * the event will not be fired at all.  
 * If the component is suspended before the event has been fired, 
 * the event will not be fired until the component is resumed.
 * @see edu.luc.cs.trull.Startable
 * @see edu.luc.cs.trull.Suspendable
 */
public class EmitComponent extends AbstractComponent {
	
	private static final Logger logger = Logger.getLogger(EmitComponent.class);

	private static final int RUNNING   = 1;
	private static final int SUSPENDED = 2;
	private static final int FINISHED  = 4;

	/**
	 * The current state of this component.
	 */
	private int state = RUNNING;
	
  /**
   * The list of emitters of events that had already been scheduled 
   * when the component was suspended.
   */
	private List suspendedEmitters = new ArrayList();

	private static Map emitterCache = null; //new HashMap(); 
	
	/**
   * Returns and caches a Runnable for event emission.
   * @return the event emitter.
	 */
	protected Runnable eventEmitterCached(final String label, final Object value, final boolean terminate) {
		if (value != null) {
			return eventEmitterUncached(label, value, terminate);
		} else {
			Runnable cached = (Runnable) emitterCache.get(label);
			if (cached == null) {
				cached = eventEmitterUncached(label, label, terminate);
				emitterCache.put(label, cached);
			}
			return cached;
		}
	}
	
  /**
   * Returns a Runnable for event emission.
   * This emitter behaves as follows: if no longer alive, the event is discarded.
   * If suspended, the emitter adds itself to the suspended queue.  Upon resuming,
   * the component reschedules the events on the suspend queue.
   * @return the event emitter.
   */
	protected final Runnable eventEmitter(final String label, final Object value, final boolean terminate) {
		return eventEmitterUncached(label, value == null ? label : value, terminate);
//		return eventEmitterCached(label, value, terminate);
	}
	
  /**
   * Returns a new uncached Runnable for event emission.
   * @return the event emitter.
   */
	protected Runnable eventEmitterUncached(final String label, final Object value, final boolean terminate) {
		return new Runnable() {
	  	  PropertyChangeEvent event = 
	  	  		new PropertyChangeEvent(EmitComponent.this, label, null, value);
			public void run() {
				if (state == RUNNING) {
					if (logger.isDebugEnabled()) logger.debug(this + " emitting event " + event);
					firePropertyChange(event);
					if (terminate) {
						fireTermination();
					}
				} else if (state == SUSPENDED) {
					addToSuspended(this);
				} // else discard the event
			}
		};
	}
	
  /**
   * Schedules an event with the given properties for emission.  
   * Used by subclasses when events must not be fired directly to preserve
   * sequential, non-overlapping processing of events; this usually happens
   * when a component emits an event in response to an incoming event.
   * @param label the label (property name) of the event.
   * @param value the data value (new property value) of the event.
   * @param terminate whether the component should also request termination
   * immediately after scheduling the event for emission.
   */
	protected void scheduleEvent(String label, Object value, boolean terminate) {
		getScheduler().schedule(eventEmitter(label, value, terminate));
	}
	
  /**
   * Schedules an event with the given properties for emission.  
   * Used by subclasses when events must not be fired directly to preserve
   * sequential, non-overlapping processing of events; this usually happens
   * when a component emits an event in response to an incoming event.
   * @param label the label (property name) of the event.
   * @param value the data value (new property value) of the event.
   */
	public void scheduleEvent(String label, Object value) {
		scheduleEvent(label, value, false);
	}
	
  /**
   * Schedules an event with the given properties for emission.  
   * Used by subclasses when events must not be fired directly to preserve
   * sequential, non-overlapping processing of events; this usually happens
   * when a component emits an event in response to an incoming event.
   * @param label the label (property name) of the event.
   */
	public void scheduleEvent(String label) {
		scheduleEvent(label, null);
	}
	
	public void start(PropertyChangeEvent incoming) {
    // All lifecycle methods must be invoked from within the event dispatch thread; 
    // otherwise the state might not be in sync! 
		if (logger.isDebugEnabled()) logger.debug(this + " starting");
		state = RUNNING;
	}

	public void stop() { 
		if (logger.isDebugEnabled()) logger.debug(this + " stopping");
		state = FINISHED;
	}
	
	public void suspend() {
		if (logger.isDebugEnabled()) logger.debug(this + " suspending");
		state = SUSPENDED;
	}
	
	public void resume() {
		if (logger.isDebugEnabled()) logger.debug(this + " resuming");
		state = RUNNING;
		rescheduleSuspended();
	}
	
  /**
   * Adds the given Runnable to the list of suspended Runnables. 
   * @param r the Runnable to be added to the suspended list.
   */
	private void addToSuspended(Runnable r) {
		if (logger.isDebugEnabled()) logger.debug(this + " adding event emitter to suspend queue");
		  suspendedEmitters.add(r);
	}
	
  /**
   * Reschedules the suspended Runnables for execution. 
   */
	private void rescheduleSuspended() {
		Iterator i = suspendedEmitters.iterator();
		while (i.hasNext()) {
			if (logger.isDebugEnabled()) logger.debug(this + " rescheduling event emitter from suspend queue");
			getScheduler().schedule((Runnable) i.next());
		}
	}
}
