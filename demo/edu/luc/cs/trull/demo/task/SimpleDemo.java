package edu.luc.cs.trull.demo.task;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.UIManager;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Control;
import edu.luc.cs.trull.DefaultTopLevelComponent;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.Suspend;
import edu.luc.cs.trull.swing.ButtonComponent;
import edu.luc.cs.trull.swing.FastSwingScheduler;
import edu.luc.cs.trull.task.DefaultTaskWorker;
import edu.luc.cs.trull.task.Task;
import edu.luc.cs.trull.task.TaskFactory;

/**
 * This example demonstrates how a task can be controlled and monitored 
 * using a simple user interface and a Trull component as the control logic.  
 */
public class SimpleDemo implements EventLabels {

  static final int MAX = 5000000;

  static final int INC = MAX / 100;

  public static void main(String[] args) throws Exception {

    // work around Apple progress bar implementation bug
    UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());

    ButtonComponent buttonComponent = new ButtonComponent(
      new String[] { START, SUSPEND, RESUME, STOP }
    );
    final JProgressBar progressBar = new JProgressBar();
    JPanel controlPanel = new JPanel();
    controlPanel.setLayout(new BorderLayout());
    controlPanel.add(buttonComponent.getView(), BorderLayout.CENTER);
    controlPanel.add(progressBar, BorderLayout.SOUTH);
    progressBar.setStringPainted(true);
    progressBar.setMaximum(MAX);
    
    // TODO consider making this stuff more compositional
    // (including support for event-based progress reporting)
    
    // component wrapping around some task
    Component taskRunner = new DefaultTaskWorker(
//    Component taskRunner = new QueuedTaskWorker(
//    Component taskRunner = new PooledTaskWorker(
      new TaskFactory() {
        public Task apply(PropertyChangeEvent event) {
          return new ProgressTask(new Count(MAX, INC), progressBar, MAX);
        }
      }
    );

    // hook it all together
    Composite test = new Composite(new Component[] { 
      buttonComponent, 
      new Loop(
        new Control(START, 
          new Suspend(SUSPEND, RESUME, 
            new Loop(
              taskRunner
            )
          ), 
          STOP 
        )
      )
    });

    // put the control panel inside a frame
    JFrame frame = new JFrame("Trull Simple Task Demo");
    frame.setContentPane(controlPanel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    // start the Trull component
    new DefaultTopLevelComponent(test, new FastSwingScheduler()).start();
  }
}