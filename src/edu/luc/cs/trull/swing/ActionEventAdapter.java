package edu.luc.cs.trull.swing;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import edu.luc.cs.trull.EmitComponent;

/**
 * A converter that listens to a Swing component and converts 
 * ActionEvents events to PropertyChangeEvents.
 */
public class ActionEventAdapter extends EmitComponent implements ActionListener {

  /**
   * Converts an ActionEvent to a PropertyChangeEvent 
   * fired to the listeners to this component.
   * @param e the incoming ActionEvent.
   */
  public void actionPerformed(ActionEvent e) {
    firePropertyChange(e.getActionCommand());
  }
}
