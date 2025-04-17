package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;

/**
 * A data-driven selection mechanism that evaluates its predicate and
 * immediately chooses and starts the corresponding branch.  
 * The resulting component
 * terminates when the chosen branch terminates. 
 */
public class If extends Switch {

  /**
   * The predicate used by this combinator. 
   */
	private EventPredicate predicate;
	
  /**
   * Constructs an empty If component.  Specific properties can be changed later.
   */
	public If() {	}
	
  /**
   * Constructs an If component.
   * @param predicate the event predicate used in choosing a branch.
   * @param yes the branch chosen when the predicate evalutes to true.
   * @param no the branch chosen when the predicate evalutes to false.
   */
	public If(EventPredicate predicate, Component yes, Component no) {
		super(predicateToValuator(predicate), new Component[] { yes, no });
		this.predicate = predicate;
	}

  /**
   * Constructs an If component with only a yes branch.
   * @param predicate the event predicate used in choosing a branch.
   * @param yes the branch chosen when the predicate evalutes to true.
   */
	public If(EventPredicate predicate, Component yes) {
		this(predicate, yes, new Done() );
	}

  /**
   * Sets the event predicate used by this If component.
   * @param predicate the event predicate.
   */
	public synchronized void setPredicate(EventPredicate predicate) {
		super.setValuator(predicateToValuator(predicate));
		this.predicate = predicate;
	}
	
  /**
   * Returns the event predicate used by this If component.
   * @return the event predicate.
   */
	public synchronized EventPredicate getPredicate() {
		return predicate;
	}

  /**
   * Converts an event predicate to an event valuator.  
   * This approach is used since the If combinator is implemented as a Switch. 
   * @param predicate the event predicate.
   * @return the corresponding event valuator.
   */
	protected static EventValuator predicateToValuator(final EventPredicate predicate) {
		return new EventValuator() {
			public int apply(PropertyChangeEvent event) {
				return predicate.apply(event) ? 0 : 1;
			}
		};
	}
}

