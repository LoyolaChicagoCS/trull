package edu.luc.cs.trull.demo.counter;

import java.awt.GridLayout;

import javax.swing.JPanel;

import edu.luc.cs.trull.*;
import edu.luc.cs.trull.demo.*;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A pausable two-digit carry counter driven by an automatic clock
 * (<a href="http://www.cs.luc.edu/trull/latest/doc/javaws/demo4.jnlp">run this demo</a>).
 *
 * <PRE>
 * Demo4:
 *
 * local UP in
 * ||  Pausable PAUSE CONTINUE
 *         CompositeCounter
 *             CarryCounter(5)
 *             CarryCounter(5)
 * ||  rename TICK to UP in
 *         act Clock(200)
 * </PRE>
 * <P>
 * <A HREF="../../../../demo/triveni/demo/counter/Demo4.java">view source</A>
 * <P>
 * <A HREF="doc-files/counter4.html">run applet</A>
 *
 * @see edu.luc.cs.trull.demo.counter.CarryCounter
 * @see edu.luc.cs.trull.demo.Clock
 */

public class Demo4 extends Applet implements EventLabels {

  public void init() {
    super.init();
    getContentPane().setLayout(new GridLayout(1, 0));
    JPanel counters = new JPanel();
    counters.setLayout(new GridLayout(1, 0));
    VisualComponent c1, c2, c3;
    setComponent(
      new Composite(new Component[] {
        c1 = new Pauseable("Pause", "Continue",
          new CompositeCounter(
            c2 = new CarryCounter(5),
            c3 = new CarryCounter(5))),
        new Rename(TICK, UP, new Clock(200))
      })
    );
    counters.add(c3.getView());
    counters.add(c2.getView());
    getContentPane().add(counters);
    getContentPane().add(c1.getView());
  }

  public static void main(String[] args) {
    runInFrame("Demo4", new Demo4());
  }
}
