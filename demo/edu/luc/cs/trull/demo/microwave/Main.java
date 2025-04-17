package edu.luc.cs.trull.demo.microwave;

import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.demo.Applet;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A driver for the Microwave demo. From the control panel, 
 * you may set the running time by successively
 * pressing the time buttons. You may also change the power level or
 * set the unit to timer mode. Pressing the stop button after starting
 * the unit will temporarily stop the unit, and pressing the start
 * button will restart it. Pressing the stop button twice in a row
 * resets the unit. You may also open or close the door. If you open
 * the door and restart the unit with the door open, an orange popup
 * indicates that a safety condition was violated.  
 * <P>
 * <A HREF="../../../../demo/triveni/demo/microwave/">view source directory</A>
 * <P>
 * <A HREF="doc-files/microwave.html">run applet</A>
 */
public class Main extends Applet {

    public void init() {
        super.init();        
        VisualComponent controlPanel;
        setComponent(new Composite(
            controlPanel = new ControlPanel(),
            new Microwave()
        ));
        getContentPane().add(controlPanel.getView());
    }

    public static void main(String[] args) throws java.io.IOException {
        runInFrame("Microwave", new Main());
    }
}
