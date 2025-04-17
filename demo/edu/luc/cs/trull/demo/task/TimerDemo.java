package edu.luc.cs.trull.demo.task;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Control;
import edu.luc.cs.trull.DefaultTopLevelComponent;
import edu.luc.cs.trull.EmitComponent;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.Suspend;
import edu.luc.cs.trull.swing.ButtonComponent;
import edu.luc.cs.trull.swing.FastSwingScheduler;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * This example demonstrates how a Swing timer can be placed
 * under the control of a suitable Trull component.
 */
public class TimerDemo extends EmitComponent implements EventLabels {

  public static final String DEFAULT_LABEL = "Tick";

  private int min;
  private int max;
  private final String label;

  private final ActionListener listener = new ActionListener() {
    public void actionPerformed(ActionEvent e) {
      scheduleEvent(label, null, true);
      System.out.println("tick");
    }
  };

  private final Timer timer = new Timer(0, null);

  public TimerDemo(int min, int max, String label) {
    this.min = min;
    this.max = max;
    this.label = label;
    timer.addActionListener(listener);
    timer.setRepeats(false);
  }

  public TimerDemo(int min, int max) {
    this(min, max, DEFAULT_LABEL);
  }

  public void setRandomDelay() {
    int delay = min + (int) ((max - min) * Math.random());
    timer.setInitialDelay(delay);
  }

  public void start(PropertyChangeEvent incoming) {
  		super.start(incoming);
  		System.out.println("start");
  	  setRandomDelay();
    timer.restart();
  }

  public void stop() {
  	  super.stop();
  		System.out.println("stop");
    timer.stop();
  }

  public void resume() {
  	  super.resume();
  		System.out.println("resume");
    timer.start();
  }

  public void suspend() {
  		super.suspend();
  		System.out.println("suspend");
    timer.stop();
  }

  public static void main(String[] args) throws Exception {
    VisualComponent buttonPanel = new ButtonComponent(new String[] { START, STOP, SUSPEND, RESUME });

    final Composite test = new Composite(new Component[] { 
      buttonPanel, 
      new Loop(
        new Control(START, 
          new Suspend(SUSPEND, RESUME, 
            new TimerDemo(1000, 5000)
          ), 
          STOP 
        )
      )
    });

    // put the control panel inside a frame
    final JFrame frame = new JFrame("Trull Random Timer Demo");
    frame.setContentPane(buttonPanel.getView());
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
    
    // start the Trull component
    new DefaultTopLevelComponent(test, new FastSwingScheduler()).start();
  }
}