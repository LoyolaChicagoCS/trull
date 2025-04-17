package edu.luc.cs.trull.demo.helloworld;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import edu.luc.cs.trull.AwaitOne;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.DefaultTopLevelComponent;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.swing.ButtonComponent;
import edu.luc.cs.trull.swing.SwingScheduler;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * The obligatory HelloWorld demo.
 */
public class HelloWorld implements EventLabels {
  public static void main(String[] args) throws Exception {
    // presentation component
    VisualComponent input = new ButtonComponent(new String[] { HELLO, HALLO, HOLA });

    // behavior
    Composite top = new Composite();
    top.addComponent(new Loop(new AwaitOne(HELLO, new Emit(WORLD))));
    top.addComponent(new Loop(new AwaitOne(HALLO, new Emit(WELT))));
    top.addComponent(new Loop(new AwaitOne(HOLA, new Emit(MUNDO))));
    top.addComponent(input);
    
    new DefaultTopLevelComponent(top, new SwingScheduler()).start();
    
    // rest of presentation
    JFrame frame = new JFrame("Hello World Demo");
    frame.getContentPane().add(input.getView(), BorderLayout.NORTH);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.show();
  }
}
