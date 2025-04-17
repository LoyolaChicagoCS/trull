package edu.luc.cs.trull.demo.counter;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.luc.cs.trull.EmitComponent;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A bounded counter implemented as a JavaBean
 * with Trull support only for event emission.
 * The current value of the counter is visualized as the position of a button
 * in a rectangle whose height is the maximum value times the height of the
 * button. The counter emits OUCH when the maximum value is reached and
 * PRESSED when the button is pressed.
 * <p>
 * <A HREF="../../../../../demo/triveni/demo/counter/Counter.tvn">view Triveni source</A>
 */
public class Counter extends EmitComponent implements VisualComponent, EventLabels {

    private String buttonName;

    private int numSteps;

    private  int width;

    private int height;
    
    private JPanel panel = new JPanel();

    private JButton b;

    private int position;

    public Counter(final String buttonName, final int numSteps, final int width, final int height) {
      this.buttonName = buttonName;
      this.numSteps = numSteps;
      this.width = width;
      this.height = height;
      this.numSteps = numSteps;
      this.height = height;
      this.width = width;
      b = new JButton(buttonName);
      b.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent evt) { firePropertyChange(PRESSED); }
      });
      panel.setPreferredSize(new Dimension(width, height * numSteps));
      panel.setLayout(null);
      panel.add(b);
      b.setSize(width, height);
      resetButton();
    }

    void placeButton() {
       b.setLocation(0, height * (numSteps - position));
       panel.repaint();
    }

    void resetButton() {
        position = 1;
        placeButton();
    }

    void raiseButton() {
        if (position < numSteps) {
             position++;
             placeButton();
        } else if (position == numSteps) {
            scheduleEvent(OUCH);
        }
    }

    void lowerButton() {
        if (position > 1) {
            position--;
            placeButton();
        }
    }

    public void propertyChange(PropertyChangeEvent event){
        String label = event.getPropertyName();
        if (UP.equals(label)) {
            raiseButton();
        } else if (DOWN.equals(label)) {
            lowerButton();
        } else if (RESET.equals(label)) {
            resetButton();
        }
    }
    
    public JComponent getView() {
      return panel;   
    }
}
