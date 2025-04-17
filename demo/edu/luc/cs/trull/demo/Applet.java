package edu.luc.cs.trull.demo;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.EventObject;

import javax.swing.JApplet;
import javax.swing.JFrame;

import org.apache.log4j.Logger;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.DefaultTopLevelComponent;
import edu.luc.cs.trull.TerminationListener;
import edu.luc.cs.trull.TopLevelComponent;
import edu.luc.cs.trull.swing.FastSwingScheduler;

/**
 * An applet wrapper for Trull demos.  Subclasses specify
 * the top-level Trull expression using <code>setComponent</code>.
 * A <code>main</code> method may be provided using <code>runInFrame</code>.
 */
public class Applet extends JApplet {

    private static Logger cat = Logger.getLogger(Applet.class);

    private TopLevelComponent top;
    
    private Component component;
    
    /**
     * Used by subclasses to specify the top-level Trull
     * expression of this applet.
     */
    protected void setComponent(final Component component) {
    	  this.component = component; 
        this.top = new DefaultTopLevelComponent(component, new FastSwingScheduler());
        component.setTerminationListener(new TerminationListener() {
        	    public void componentTerminated(EventObject event) {
                cat.info(this + " received termination request from top " + component);
                System.exit(0);
            }
        });
    }
    
    /**
     * Starts the top-level Trull expression.
     */
    public void start() {
        try {
        	   top.start();
        } catch (InterruptedException e) {
            throw new RuntimeException("Applet thread interrupted");   
        }
    }

    /**
     * Suspends the top-level Trull expression.
     */
    public void stop() {
        try {
            top.stop();
        } catch (InterruptedException e) {
            throw new RuntimeException("Applet thread interrupted");   
        }
    }

    /**
     * Stops the top-level Trull expression.
     */
    public void destroy() {
    	  component.setTerminationListener(null);
    	  component = null;
        top = null;
    }

   /**
     * Runs an instance of this applet in a frame as an
     * application.  Subclasses should invoke this method
     * in their main method on a new instance of themselves.
     */
    public static void runInFrame(String title, final Applet applet) {
        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowIconified(WindowEvent evt) {
                applet.stop();
            }
            public void windowDeiconified(WindowEvent evt) {
                applet.start();
            }
        });
        frame.setContentPane(applet);
        applet.init();
        applet.start();
        frame.pack();
        frame.setVisible(true);
    }
}
