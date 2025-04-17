package edu.luc.cs.trull.demo.counter;

import java.awt.GridLayout;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Local;
import edu.luc.cs.trull.Rename;
import edu.luc.cs.trull.Watching;
import edu.luc.cs.trull.demo.Applet;
import edu.luc.cs.trull.demo.Clock;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A three-digit carry counter driven by an automatic clock that stops upon overflow
 * (<a href="http://www.cs.luc.edu/trull/latest/doc/javaws/demo5.jnlp">run this demo</a>).
 *
 * <PRE>
 * Demo5:
 *
 * local UP in
 * ||  CompositeCounter
 *         CompositeCounter
 *             CarryCounter(10)
 *             CarryCounter(5)
 *         CarryCounter(3)
 * ||  do
 *         rename TICK to UP in
 *             act Clock(100)
 *     watching OVERFLOW
 * </PRE>
 * <P>
 * <A HREF="../../../../demo/triveni/demo/counter/Demo5.java">view source</A>
 * <P>
 * <A HREF="doc-files/counter5.html">run applet</A>
 *
 * @see edu.luc.cs.trull.demo.counter.CarryCounter
 * @see edu.luc.cs.trull.demo.Clock
 */

public class Demo5 extends Applet implements EventLabels {

  public void init() {
    super.init();
    getContentPane().setLayout(new GridLayout(1, 0));
    VisualComponent v1, v2, v3;
    setComponent(
      new Local(UP,
        new Composite(new Component[] {
          new CompositeCounter(
            new CompositeCounter(
              v1 = new CarryCounter(10),
              v2 = new CarryCounter(5)),
            v3 = new CarryCounter(3)),
          new Watching(
            new Rename(TICK, UP, new Clock(100)),
          OVERFLOW)
        }))
    );
    getContentPane().add(v1.getView());
    getContentPane().add(v2.getView());
    getContentPane().add(v3.getView());
  }

  public static void main(String[] args) {
    runInFrame("Demo5", new Demo5());
  }
}
