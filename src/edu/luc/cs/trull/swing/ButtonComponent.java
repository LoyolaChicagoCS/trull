package edu.luc.cs.trull.swing;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * A visual component that provides a user interface consisting of
 * a button panel.  There is one button for each String event label specified
 * in the constructor argument.
 */
public class ButtonComponent extends ActionEventAdapter implements VisualComponent {

  public final static int HORIZONTAL = 0;
  
  public final static int VERTICAL = 1;

  public final static int DEFAULT = HORIZONTAL;

  /**
   * The panel that contains the buttons.
   */
  protected final JPanel buttonPanel = new JPanel();

  /**
   * Constructs an input panel with one button for each label in the array.
   * @param labels the labels for the buttons.
   */
  public ButtonComponent(final String[] labels) {
    setButtonLayout(DEFAULT);
    for (int i = 0; i < labels.length; i ++) {
      JButton button = new JButton(labels[i]);
      buttonPanel.add(button);
      button.addActionListener(this);
    }
  }
  
  public void setButtonLayout(int layout) {
    switch (layout) {
    case HORIZONTAL:
      buttonPanel.setLayout(new GridLayout(0, 1));
    case VERTICAL:
      buttonPanel.setLayout(new GridLayout(1, 0));
    }
  }

  public JComponent getView() { return buttonPanel; }
}
