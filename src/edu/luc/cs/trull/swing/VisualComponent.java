package edu.luc.cs.trull.swing;

import javax.swing.JComponent;
import edu.luc.cs.trull.Component;

/**
 * A visual component is a Trull component with an associated view
 * in the form of a Swing component. 
 */
public interface VisualComponent extends Component {
    
  /**
   * Returns the Swing component associated with this Trull component.
   * @return the associated Swing component.
   */
  JComponent getView();
}
