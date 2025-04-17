package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;

/**
 * An EventValuator suitable for branching on the label of an 
 * incoming event.  This valuator returns the index of the 
 * label of the incoming event in the specified array of event
 * labels, or -1 if not in the array.
 */
public class BranchValuator implements EventValuator {

	protected Map eventMap = new HashMap();
	
  /**
   * Constructs a BranchValuator that returns the index of the 
   * label of the incoming event in the specified array 
   * @param labels the array of event labels to be considered.
   */
	public BranchValuator(String[] labels) {
		for (int i = 0; i < labels.length; i ++) {
			eventMap.put(labels[i], new Integer(i));
		}
	}
	
	public int apply(PropertyChangeEvent incoming) {
		Integer value = (Integer) eventMap.get(incoming.getPropertyName());
		return value != null ? value.intValue() : -1;
	}
}
