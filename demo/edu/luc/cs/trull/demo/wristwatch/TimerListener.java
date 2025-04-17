package edu.luc.cs.trull.demo.wristwatch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * A listener that converts any ActionEvent to a PropertyChangeEvent
 * with the given property name.
 */
public class TimerListener implements EventLabels, ActionListener {

  private PropertyChangeListener target;
  
  private String label;
  
  public void setEvent(String label) {
    this.label = label;
  }
  
  public void setTarget(PropertyChangeListener target) {
    this.target = target;
  }
  
  public void actionPerformed(ActionEvent event) {
    target.propertyChange(new PropertyChangeEvent(this, label, null, label));
  }

}
