package edu.luc.cs.trull.demo.counter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import edu.luc.cs.trull.EmitComponent;

/**
 * An adapter that causes a button to emit a Trull event.
 */
class ButtonReactor extends EmitComponent implements EventLabels {

  ButtonReactor(JButton b, final String evt) {
    b.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) { firePropertyChange(evt); }
    });
  }
}
