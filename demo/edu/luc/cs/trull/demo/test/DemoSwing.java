package edu.luc.cs.trull.demo.test;

import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.demo.Applet;
import edu.luc.cs.trull.demo.counter.CounterComponent;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * An example of how to use the SwingTester component.
 */
public class DemoSwing extends Applet {

  public void init() {
    super.init();
    VisualComponent counter = new CounterComponent("down", 10, 100, 40);
    SwingTester tester = new SwingTester("CounterTest");
    setComponent(new Composite(counter, tester));
    getContentPane().add(counter.getView());
  }

  public static void main(String[] args) {
    runInFrame("DemoSwing", new DemoSwing());
  }
}
