package edu.luc.cs.trull.demo.test;

import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.demo.Applet;
import edu.luc.cs.trull.demo.counter.CounterComponent;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * An example of how to use the ConsoleTester component.
 */
public class DemoConsole extends Applet {

  public void init() {
    super.init();
    VisualComponent counter = new CounterComponent("down", 10, 100, 40);
    ConsoleTester tester = new ConsoleTester();
    setComponent(new Composite(counter, tester));
    getContentPane().add(counter.getView());
  }

  public static void main(String[] args) {
    runInFrame("DemoConsole", new DemoConsole());
  }
}
