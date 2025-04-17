package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.util.Arrays;

/**
 * A class that provides utility methods used by the Trull framework.
 */
public class Util {
    
    /**
     * Returns a more informative String representation of a PropertyChangeEvent.
     * @param event the event.
     * @return the String representation.
     */
    public static String toString(PropertyChangeEvent event) {
      StringBuffer result = new StringBuffer();
      result.append(event.getClass().getName());
      result.append("[propertyName=");
      result.append(event.getPropertyName());
      result.append(",oldValue=");
      Object oldValue = event.getOldValue();
      if (oldValue == null) {
        oldValue = "null"; 
      }
      result.append(oldValue.getClass().isArray() ? Util.toString((Object[]) oldValue) : oldValue);
      result.append(",newValue=");
      Object newValue = event.getNewValue();
      if (newValue == null) {
        newValue = "null"; 
      }
      result.append(newValue.getClass().isArray() ? Util.toString((Object[]) newValue) : newValue);
      result.append("]");
      return result.toString(); 
    }

    /**
     * Returns a more informative String representation of an array.
     * @param array the array.
     * @return the String representation.
     */
    public static String toString(Object[] array) {
    	  return Arrays.asList(array).toString();
    }
}
