package edu.luc.cs.trull.demo.counter;

import java.awt.GridLayout;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Rename;
import edu.luc.cs.trull.demo.Applet;
import edu.luc.cs.trull.demo.Clock;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A haltable two-digit carry counter driven by an automatic clock
 * (<a href="http://www.cs.luc.edu/trull/latest/doc/javaws/demo6.jnlp">run this demo</a>).
 *
 * <PRE>
 * Demo6:
 *
 * local UP in
 * ||  Haltable HALT
 *         CompositeCounter
 *             CarryCounter(5)
 *             CarryCounter(5)
 * ||  rename TICK to UP in
 *         act Clock(200)
 * </PRE>
 * <P>
 * <A HREF="../../../../demo/triveni/demo/counter/Demo6.java">view source</A>
 * <P>
 * <A HREF="doc-files/counter6.html">run applet</A>
 *
 * @see edu.luc.cs.trull.demo.counter.CarryCounter
 * @see edu.luc.cs.trull.demo.Clock
 */
public class Demo6 extends Applet implements EventLabels {

  public void init() {
    super.init();
    getContentPane().setLayout(new GridLayout(1, 0));
    VisualComponent c1, c2, h;
    setComponent(
        h = new Haltable("Halt",
            new Composite(new Component[] {
                new CompositeCounter(
                    c1 = new CarryCounter(5),
                    c2 = new CarryCounter(5)),
                new Rename(TICK, UP, new Clock(200))
      })
    ));
    getContentPane().add(c2.getView());
    getContentPane().add(c1.getView());
    getContentPane().add(h.getView());
  }

  public static void main(String[] args) {
    runInFrame("Demo6", new Demo6());
  }
}
