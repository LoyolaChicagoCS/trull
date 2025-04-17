package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.EventObject;

import org.apache.log4j.Logger;

/**
 * A combinator that provides looping.  Each time the body component
 * terminates, the loop repeats and the body component is started anew.
 * The body of the loop is sequentially composed with itself infinitely many times.
 * Therefore, a loop component never terminates.  The only way to get
 * out of a loop is by stopping it explicitly.  This can be achieved 
 * by embedding it within a preemption combinator such as Control, Cycle, or Watching.
 * <PRE>
 * loop
 *   body
 * </PRE>
 * @see edu.luc.cs.trull.Sequence
 * @see edu.luc.cs.trull.Control
 * @see edu.luc.cs.trull.Cycle
 * @see edu.luc.cs.trull.Watching
 */
public class Loop extends SingleChildCombinator {  

	private static final Logger logger = Logger.getLogger(Loop.class);

  /**
   * Constructs a Loop component with an empty but non-terminating (Nil)
   * body.  Specific properties can be changed later.
   */
	public Loop() {
		super();
	}
	
  /**
   * Constructs a Loop component with the given body.
   */
	public Loop(Component body) {
		super(body);
	}

	protected Component getDefaultChild() {
		return Nil.getInstance();
	}

  /**
   * The listener to the body of this component.
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
			terminationListener = new LoopTerminationListener();
		}
		return terminationListener;
	}

	/**   
	 * A dedicated listener that forwards the event to all external listeners.
	 */  
	protected class LocalListener implements PropertyChangeListener {    
		public void propertyChange(PropertyChangeEvent evt) {
			if (logger.isDebugEnabled()) logger.debug(this + " emitting internal event " + evt);
			getExternalListeners().firePropertyChange(evt);  
		}
	}

  /**
   * This termination listener restarts the body component anew 
   * after every time it terminates.
   */
	protected class LoopTerminationListener implements TerminationListener {
		public void componentTerminated(EventObject event) {
			if (logger.isDebugEnabled()) logger.debug(this + " repeating after termination from " + event.getSource());
			start(null);
		}
	}
}
