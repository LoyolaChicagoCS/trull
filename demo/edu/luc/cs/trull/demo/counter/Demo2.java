package edu.luc.cs.trull.demo.counter;

import java.awt.GridLayout;

import edu.luc.cs.trull.AwaitOne;
import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.demo.Applet;
import edu.luc.cs.trull.demo.Clock;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A bounded counter activity driven by an automatic clock
 * (<a href="http://www.cs.luc.edu/trull/latest/doc/javaws/demo2.jnlp">run this demo</a>).
 *
 * <PRE>
 * Demo2:
 *
 *     CounterExpr("down", 10)
 * ||  act Clock(1000)
 * ||  loop
 *         await TICK -> emit UP
 * ||  loop
 *         await OUCH -> emit RESET
 * ||  loop
 *         await PRESSED -> emit DOWN
 * </PRE>
 * <P>
 * <A HREF="../../../../demo/triveni/demo/counter/Demo2.java">view source</A>
 * <P>
 * <A HREF="doc-files/counter2.html">run applet</A>
 *
 * @see edu.luc.cs.trull.demo.counter.CounterComponent
 * @see edu.luc.cs.trull.demo.Clock
 */
public class Demo2 extends Applet implements EventLabels {

  public void init() {
    super.init();
    getContentPane().setLayout(new GridLayout(1, 0));
    VisualComponent counter;
    setComponent(
      new Composite(new Component[] {
        counter = new CounterComponent(DOWN, 10, 100, 40),
        new Clock(1000),
        new Loop(new AwaitOne(TICK, new Emit(UP))),
        new Loop(new AwaitOne(OUCH, new Emit(RESET))),
        new Loop(new AwaitOne(PRESSED, new Emit(DOWN)))
      })
    );
    getContentPane().add(counter.getView());
  }

  public static void main(String[] args) {
    runInFrame("Demo2", new Demo2());
  }
}
