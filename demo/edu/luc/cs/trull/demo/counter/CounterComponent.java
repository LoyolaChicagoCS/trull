package edu.luc.cs.trull.demo.counter;

import java.awt.Dimension;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.luc.cs.trull.ActionComponent;
import edu.luc.cs.trull.AwaitOne;
import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.EventPredicate;
import edu.luc.cs.trull.If;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.swing.VisualComponent;

/**
* A bounded counter implemented fully in Trull as a complex component.  
* The current value of the counter is visualized as the position of a button
* in a rectangle whose height is the maximum value times the
* height of the button.
* <p>
* <A HREF="../../../../../demo/triveni/demo/counter/CounterExpr.tvn">view Triveni source</A>
*/
public class CounterComponent extends Composite implements VisualComponent, EventLabels {

    private String buttonName;

    private int numSteps;

    private int width;

    private int height;
    
    private JButton b;
    
    private JPanel panel;
    
    private int position;
    
    public CounterComponent(final String buttonName, final int numSteps, final int width, final int height) {
      this.buttonName = buttonName;
      this.numSteps = numSteps;
      this.width = width;
      this.height = height;
      b = new JButton(buttonName);
      panel = new JPanel();
      panel.setPreferredSize(new Dimension(width, height * numSteps));
      panel.setLayout(null);
      panel.add(b);
      b.setSize(width, height);
      resetButton();

      Component probe;
      
      addComponent(new ButtonReactor(b, PRESSED));
      addComponent(
          new Loop(
              new AwaitOne(UP, new If(new EventPredicate() {
                  public boolean apply(PropertyChangeEvent event) {
                      return position == numSteps;
                  }},
                  new Emit(OUCH),
                  new ActionComponent(new PropertyChangeListener(){
                      public void propertyChange(PropertyChangeEvent event){
                          raiseButton();
                  }})/*Init*/
              )/*If*/)/*Await*/
          )/*Loop*/
      );
      addComponent(
          new Loop(
              new AwaitOne(DOWN, new ActionComponent(new PropertyChangeListener(){
                  public void propertyChange(PropertyChangeEvent event){
                      lowerButton();
              }})/*Init*/)/*Await*/
          )/*Loop*/ 
      );
      addComponent(
          new Loop(
              new AwaitOne(RESET, new ActionComponent(new PropertyChangeListener(){
                  public void propertyChange(PropertyChangeEvent event){
                      resetButton();
              }})/*Init*/)/*Await*/
          )/*Loop*/
      );
    }
    
    public JComponent getView() { return panel; }
    
    void placeButton() {
        b.setLocation(0, height * (numSteps - position));
        panel.repaint();
    }
    
    void resetButton() {
        position = 1;
        placeButton();
    }
    
    void raiseButton() {
        if (position < numSteps) {
             position++;
             placeButton();
        }
    }
    
    void lowerButton() {
        if (position > 1) {
            position--;
            placeButton();
        }
    }
}