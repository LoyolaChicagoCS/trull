package edu.luc.cs.trull;

import java.beans.PropertyChangeListener;

/**
 * A combinator for event-based preemption.
 * First, the body component is started unconditionally.  If
 * an event with any of the given labels occurs before the body
 * terminates, then the action <code>stopAction</code> is invoked and
 * the handler branch is started, 
 * corresponding by position to the event label if applicable.
 * Otherwise, if the body terminates normally, then the component
 * terminates without starting the handler.
 * This combinator is a special case of Control.
 * <PRE>
 *   do 
 *     body
 *   watching STOP ->
 *     handler
 * </PRE>
 * @see edu.luc.cs.trull.Control
 * @see edu.luc.cs.trull.AwaitOne
 * @see edu.luc.cs.trull.Suspend
 */
public class Watching extends Control {
	
  /**
   * Constructs a Watching component.  Specific properties can be changed later.
   */
	public Watching() { }

  /**
   * Constructs a Watching component.
   * @param body the body component of the Watching component.
   * @param stopLabels the String event labels on which the body is preempted.  If null, Nil is used.
   * @param stopAction the action to be invoked upon preemption.  May be null.
   * @param handler the component to invoke after preemption.  If null, Done is used.
   */
	public Watching(Component body, 
			String[] stopLabels, PropertyChangeListener stopAction, 
			Component handler) {
		super(new String[0], null, body, stopLabels, stopAction, handler);
	}
	
  /**
   * <code>Watching(body, stopLabels, stopAction, new Done())</code>
   */
  public Watching(Component body, 
        String[] stopLabels, PropertyChangeListener stopAction) {
    super(new String[0], null, body, stopLabels, stopAction, new Done());
  }
    
  /**
   * <code>Watching(body, [stopLabel], stopAction)</code>
   */
  public Watching(Component body, 
      String stopLabel, PropertyChangeListener stopAction) {
    super(new String[0], null, body, new String[] { stopLabel }, stopAction, new Done());
  }
  
  /**
   * <code>Watching(body, [stopLabel], stopAction, handler)</code>
   */
	public Watching(Component body, 
			String stopLabel, PropertyChangeListener stopAction, 
			Component handler) {
		this(body, new String[] { stopLabel }, stopAction, handler);
	}

  /**
   * <code>Watching(body, stopLabels, null, handler)</code>
   */
	public Watching(Component body, String[] stopLabels, Component handler) {
		this(body, stopLabels, null, handler);
	}
	
  /**
   * <code>Watching(body, [stopLabel], handler)</code>
   */
	public Watching(Component body, String stopLabel, Component handler) {
		this(body, new String[] { stopLabel }, handler);
	}

  /**
   * <code>Watching(body, stopLabels, new Done())</code>
   */
	public Watching(Component body, String[] stopLabels) {
		this(body, stopLabels, new Done());
	}
	
  /**
   * <code>Watching(body, [stopLabel])</code>
   */
	public Watching(Component body, String stopLabel) {
		this(body, new String[] { stopLabel });
	}

  /**
   * Constructs a Watching component whose handler is a Switch with the
   * given branches.  When a triggering event occurs, the branch
   * is chosen whose position corresponds to the position of the 
   * triggering event in the array of event labels.
   * @param stopLabels an array of triggering labels.
   * @param branches an array of components to branch to.  
   * There must be as many components as labels.
   */
	public Watching(Component body, String[] stopLabels, Component[] branches) {
		this(body, stopLabels, new Switch(new BranchValuator(stopLabels), branches));
		if (stopLabels.length != branches.length) {
			throw new IllegalArgumentException("not the same number of labels and branches");
		}
	}
}
