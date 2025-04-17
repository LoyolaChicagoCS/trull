package edu.luc.cs.trull.demo.counter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;

import edu.luc.cs.trull.EmitComponent;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A component that emits an event each time the button is pressed.
 */
public class ButtonComponent extends EmitComponent implements VisualComponent, EventLabels {

  private JButton button;

  public ButtonComponent(String label) {
    button = new JButton(label);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) { firePropertyChange(TICK); }
    });
  }
  
  public JComponent getView() { return button; } 
}
