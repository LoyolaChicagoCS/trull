package edu.luc.cs.trull;

import org.apache.log4j.Logger;

/**
 * A combinator for hiding events within a component.
 * It implements the concept of consistently renaming each specified event 
 * to a unique new name known only within the component.  Localized events
 * become private to the component, that is, 
 * distinct from events with the same label but outside of the component.
 * This combinator is a special case of Rename. 
 */
public class Local extends Rename {  

	private static final Logger logger = Logger.getLogger(Local.class);

  /**
   * Constructs a Local component.  Specific properties can be changed later.
   */
	public Local() { }
	
  /**
   * Constructs a Local component with the given local event labels and body.
   * @param internalLabels the event labels to be made local to this component.
   * @param body the body of this component.
   */
	public Local(String[] internalLabels, Component body) {
		this(body);
		setLocalEvents(internalLabels);
	}

  /**
   * <code>Local([internalLabel], body)</code>
   */
  public Local(String internalLabel, Component body) {
    this(new String[] { internalLabel }, body);
  }
    
  /**
   * <code>Local([], body)</code>
   */
  public Local(Component body) {
    super(body);
  }
}
