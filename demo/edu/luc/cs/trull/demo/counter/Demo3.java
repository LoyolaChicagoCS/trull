package edu.luc.cs.trull.demo.counter;

import java.awt.GridLayout;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Local;
import edu.luc.cs.trull.Rename;
import edu.luc.cs.trull.demo.Applet;
import edu.luc.cs.trull.demo.Clock;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A three-digit carry counter driven by an automatic clock
 * (<a href="http://www.cs.luc.edu/trull/latest/doc/javaws/demo3.jnlp">run this demo</a>).
 *
 * <PRE>
 * Demo3:
 *
 * local UP in
 * ||  CompositeCounter
 *         CompositeCounter
 *             CarryCounter(10)
 *             CarryCounter(5)
 *         CarryCounter(3)
 * ||  rename TICK to UP in
 *         act Clock(100)
 * </PRE>
 * <P>
 * <A HREF="../../../../demo/triveni/demo/counter/Demo3.java">view source</A>
 * <P>
 * <A HREF="doc-files/counter3.html">run applet</A>
 *
 * @see edu.luc.cs.trull.demo.counter.CarryCounter
 * @see edu.luc.cs.trull.demo.Clock
 */
public class Demo3 extends Applet implements EventLabels {

  public void init() {
    super.init();
    getContentPane().setLayout(new GridLayout(1, 0));
    VisualComponent cc1, cc2, cc3;
    setComponent(
      new Local(UP,
        new Composite(new Component[] {
          new CompositeCounter(
            new CompositeCounter(
              cc1 = new CarryCounter(10),
              cc2 = new CarryCounter(5)),
            cc3 = new CarryCounter(3)),
          new Rename(TICK, UP, new Clock(100))
        }))
    );
    getContentPane().add(cc3.getView());
    getContentPane().add(cc2.getView());
    getContentPane().add(cc1.getView());
  }

  public static void main(String[] args) {
    runInFrame("Demo3", new Demo3());
  }
}
