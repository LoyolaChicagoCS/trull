package edu.luc.cs.trull;

import java.beans.PropertyChangeListener;

/**
 * A disjunctive await.
 * A disjunctive await is an event-driven selection
 * mechanism that waits until an event corresponding
 * to one of the listed labels has occurred, executes 
 * the corresponding action if present,
 * and continues as the associated component if present.
 * A disjunctive await terminates when the chosen component
 * terminates.
 * This combinator is a special case of the Control combinator.
 * @see edu.luc.cs.trull.Control
 */
public class AwaitOne extends Control {
	
  /**
   * Constructs an empty AwaitOne component.
   * Specific properties can be set later.
   */
	public AwaitOne() { }
	
  /**
   * Constructs an AwaitOne component with the given array of triggering labels,
   * action to be invoked when one of the triggering events occurs, and component
   * to be started. 
   * @param startLabels an array of triggering labels.
   * @param startAction the action to be invoked when a triggering event occurs.  May be null.
   * @param body the component to be started after a triggering event occurs.  If null, an empty component is used.
   */
	public AwaitOne(String[] startLabels, PropertyChangeListener startAction, 
			Component body) {
		super(startLabels, startAction, body, new String[0], null, new Done());
	}

  /**
   * <code>AwaitOne([startLabel], startAction, body)</code> 
   */
	public AwaitOne(String startLabel, PropertyChangeListener startAction, 
			Component body) {
		this(new String[] { startLabel }, startAction, body);
	}

  /**
   * <code>AwaitOne(startLabels, null, body)</code> 
   */
	public AwaitOne(String[] startLabels, Component body) {
		this(startLabels, null, body);
	}

  /**
   * <code>AwaitOne([startLabel], body)</code> 
   */
	public AwaitOne(String startLabel, Component body) {
		this(startLabel, null, body);
	}
	
  /**
   * <code>AwaitOne(startLabels, startAction, new Done())</code> 
   */
	public AwaitOne(String[] startLabels, PropertyChangeListener startAction) {
		this(startLabels, startAction, new Done());
	}

  /**
   * <code>AwaitOne([startLabel], startAction)</code> 
   */
	public AwaitOne(String startLabel, PropertyChangeListener startAction) {
		this(startLabel, startAction, new Done());
	}
	
  /**
   * Constructs an AwaitOne component whose body is a Switch with the
   * given branches.  When a triggering event occurs, the branch
   * is chosen whose position corresponds to the position of the 
   * triggering event in the array of event labels.
   * @param startLabels an array of triggering labels.
   * @param branches an array of components to branch to.  
   * There must be as many components as labels.
   */
	public AwaitOne(String[] startLabels, Component[] branches) {
		this(startLabels, new Switch(new BranchValuator(startLabels), branches));
		if (startLabels.length != branches.length) {
			throw new IllegalArgumentException("not the same number of labels and branches");
		}
	}
}
