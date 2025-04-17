package edu.luc.cs.trull.demo.task;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Control;
import edu.luc.cs.trull.DefaultTopLevelComponent;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.Suspend;
import edu.luc.cs.trull.swing.ButtonComponent;
import edu.luc.cs.trull.swing.FastSwingScheduler;
import edu.luc.cs.trull.task.PooledTaskWorker;

/**
 * An example involving a more complex component with multiple
 * tasks at the leaves.
 */
public class ComplexDemo implements EventLabels {

  private final static Logger logger = Logger.getLogger(ComplexDemo.class);

  static final int MAX   = 5000000;
  static final int INC   = MAX / 100;
  static final int TASKS = 9; // 39

  public static void main(String[] args) throws Exception {

    // work around Apple progress bar implementation bug
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

    final ButtonComponent buttonComponent = new ButtonComponent(
      new String[] { START, SUSPEND, RESUME, STOP }
    );
    JProgressBar progressBar = new JProgressBar();
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BorderLayout());
    controlPanel.add(buttonComponent.getView(), BorderLayout.CENTER);

    // add many progress bars to the control panel
    JProgressBar[] progressBars = new JProgressBar[TASKS];
    for (int i = 0; i < TASKS; i ++) {
      progressBars[i] = new JProgressBar();
    }

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(0, 1));
    for (int i = 0; i < TASKS; i ++) {
      panel.add(progressBars[i]);
    }
    controlPanel.add(panel, BorderLayout.SOUTH);

    // components wrapping around some task
    Component[] tasks = new Component[TASKS];
    for (int i = 0; i < TASKS; i ++) {
//      tasks[i] = new DefaultTaskWorker(
//      tasks[i] = new QueuedTaskWorker(
    		tasks[i] = new PooledTaskWorker(
        new ProgressTask(new Count(MAX, INC), progressBars[i], MAX));
    }

    // hook it all together
///*
    final Component[] branches = new Component[TASKS / 3];
    for (int i = 0; i < TASKS / 3; i ++) {
      branches[i] = new Loop(
        new Composite(new Component[] {
          tasks[3 * i + 0],
          tasks[3 * i + 1],
          tasks[3 * i + 2]
        })
      );
    }
//*/

    final Composite test = new Composite(new Component[] { 
      buttonComponent, 
      new Loop(
        new Control(START, 
          new Suspend(SUSPEND, RESUME, new Composite(branches)), 
          STOP 
        )
      ) 
    });
    
    // put the control panel inside a frame
    final JFrame frame = new JFrame("Trull Task Demo");
    frame.setContentPane(controlPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    // start the Trull expression
    new DefaultTopLevelComponent(test, new FastSwingScheduler()).start();
  }
}