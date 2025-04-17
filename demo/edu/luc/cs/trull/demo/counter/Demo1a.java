package edu.luc.cs.trull.demo.counter;

import java.awt.GridLayout;

import edu.luc.cs.trull.AwaitOne;
import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.demo.Applet;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A bounded counter expression driven by a manual clock
 * (<a href="http://www.cs.luc.edu/trull/latest/doc/javaws/demo1a.jnlp">run this demo</a>).
 *
 * <PRE>
 * Demo1a:
 *
 *     CounterExpr("down", 10)
 * ||  act ButtonComponent("tick")
 * ||  loop
 *         await TICK -> emit UP
 * ||  loop
 *         await OUCH -> emit RESET
 * ||  loop
 *         await PRESSED -> emit DOWN
 * </PRE>
 * <P>
 * <A HREF="../../../../demo/triveni/demo/counter/Demo1a.java">view source</A>
 * <P>
 * <A HREF="doc-files/counter1a.html">run applet</A>
 *
 * @see edu.luc.cs.trull.demo.counter.CounterComponent
 * @see edu.luc.cs.trull.demo.counter.ButtonComponent
 */
public class Demo1a extends Applet implements EventLabels {

  public void init() {
    super.init();
    getContentPane().setLayout(new GridLayout(1, 0));
    VisualComponent counter;
    VisualComponent button;
    setComponent(
      new Composite(new Component[] {
        counter = new CounterComponent(DOWN, 10, 100, 40),
        counter = new CarryCounter(10),
        button = new ButtonComponent(TICK),
        new Loop(new AwaitOne(TICK, new Emit(UP))),
//        new Loop(new AwaitOne(OUCH, new Emit(RESET))),
//        new Loop(new AwaitOne(PRESSED, new Emit(DOWN)))
      })
    );
    getContentPane().add(counter.getView());
    getContentPane().add(button.getView());
  }

  public static void main(String[] args) {
    runInFrame("Demo1a", new Demo1a());
  }
}
