package edu.luc.cs.trull.demo.stopwatch;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.Timer;

import edu.luc.cs.trull.demo.wristwatch.EventLabels;
import edu.luc.cs.trull.swing.FastSwingScheduler;

/**
 * The driver for this application.
 */
public class Main extends JApplet implements EventLabels {

	Timer timer = null;

  public void init() {
	  final Translation translation = new Translation();
	  translation.setScheduler(new FastSwingScheduler());
  	ActionListener listener = new ActionListener() {
	    public void actionPerformed(ActionEvent event) {
	    	String eventLabel = event.getActionCommand();
	    	if (eventLabel == null) { eventLabel = TICK; }
	    	System.out.println(eventLabel);
	      PropertyChangeEvent e = new PropertyChangeEvent(this, eventLabel, null, eventLabel);
	      translation.propertyChange(e);
	    }
	  };
    timer = new Timer(1000, listener);
    Presentation presentation = new Presentation();
    presentation.addActionListener(listener);
    translation.addPropertyChangeListener(presentation);
    this.getContentPane().add(presentation);
  }

  public void start() {
  	timer.start();
  }

  public void stop() {
  	timer.stop();
  }

  public static void main(String args[]) {
	  // create a frame around the applet
    JFrame frame = new JFrame("Stop Watch Demo");
    Main theInterface = new Main();

    // invoke the applet's lifecycle methods manually
    theInterface.init();
    frame.setContentPane(theInterface);
    // bring the close button to life
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    theInterface.start();

    frame.pack();
    frame.setVisible(true);
  } // end main.
}