package edu.luc.cs.trull.demo.task;

import java.beans.PropertyChangeEvent;

import javax.swing.JComponent;
import javax.swing.JProgressBar;

import edu.luc.cs.trull.EmptyComponent;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * 
 */
public class ProgressComponent extends EmptyComponent implements EventLabels, VisualComponent {

  protected JProgressBar progressBar = new JProgressBar();
  
  public ProgressComponent() {
    progressBar.setValue(0);
    progressBar.setStringPainted(true);
  }
  
  public void propertyChange(PropertyChangeEvent e) {
    if (PROGRESS.equals(e.getPropertyName())) {
      progressBar.setValue(((Integer) e.getNewValue()).intValue());
    }
  }
  
  public void setMaximum(int max) {
    progressBar.setMaximum(max);
  }
  
  public JComponent getView() {
    return progressBar;
  }
}
