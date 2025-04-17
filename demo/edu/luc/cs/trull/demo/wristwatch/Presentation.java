package edu.luc.cs.trull.demo.wristwatch;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.luc.cs.trull.PropertyChangeMulticaster;

/**
 * Presentation component for the wristwatch demo.
 */
public class Presentation extends JPanel 
implements EventLabels, DateConstants, PropertyChangeListener {

  /**
   * Converts an outgoing ActionEvent to a PropertyChangeEvent, 
   * using the action command as the property name.
   */
  protected final ActionListener converter = new ActionListener() {
    public void actionPerformed(ActionEvent event) {
      String label = event.getActionCommand();
      System.out.println(label);
      listeners.firePropertyChange(new PropertyChangeEvent(this, label, null, label));
    }
  };

  protected final JButton start = new JButton("Start/Stop");
  protected final JButton reset = new JButton("Reset/Lap");
  protected final JButton light = new JButton(LIGHT);
  protected final JButton mode = new JButton(MODE);
  protected final JLabel display = new JLabel("8 8 : 8 8", JLabel.CENTER);
  protected final JLabel modeIndicator = new JLabel(MODE, JLabel.CENTER);

  protected PropertyChangeMulticaster listeners = 
    new PropertyChangeMulticaster(this);
  
  public void addPropertyChangeListener(PropertyChangeListener l) {
    listeners.addPropertyChangeListener(l);
  }
	
  public void removePropertyChangeListener(PropertyChangeListener l) {
    listeners.removePropertyChangeListener(l);
  }
  
  /**
   * Handles incoming events.
   */
  public void propertyChange(PropertyChangeEvent event) {
	Object label = event.getPropertyName();
	if (NOW.equals(label)) {
	  int val = ((Integer) event.getNewValue()).intValue();
	  display.setText(
		(val / DateModel.SEC_PER_MIN / 10) + " " + (val / DateModel.SEC_PER_MIN % 10) + " : " +
		(val % DateModel.SEC_PER_MIN / 10) + " " + (val % 10)
	  );
	} else if (LIGHT_ON.equals(label)) {
	  display.setBackground(Color.yellow);
	  modeIndicator.setBackground(Color.yellow);
	} else if (LIGHT_OFF.equals(label)) {
	  display.setBackground(Color.lightGray);
	  modeIndicator.setBackground(Color.lightGray);
	} else if (TIME.equals(label)) {
	  modeIndicator.setText("Time");
	} else if (DATE.equals(label)) {
	  modeIndicator.setText("Date");
	} else if (STOP.equals(label)
	  | LAP.equals(label)
	  | HOUR.equals(label)
	  | MIN.equals(label)
	  | DAY.equals(label)
	  | MONTH.equals(label)) {
	  modeIndicator.setText((String) label);
	}
  }
	
  public Presentation() {
    // configure components
    start.setActionCommand(START);
    reset.setActionCommand(RESET);
    display.setOpaque(true);
    display.setBackground(Color.lightGray);
    modeIndicator.setBackground(Color.lightGray);

    // hook the components up to the converter
    start.addActionListener(converter);
    reset.addActionListener(converter);
    light.addActionListener(converter);
    mode.addActionListener(converter);

    // build the presentation
    setLayout(new GridLayout(2, 3, 10, 10));
    add(start);
    add(reset);
    add(mode);
    add(light);
    add(display);
    add(modeIndicator);
  }
}
