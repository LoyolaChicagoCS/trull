package edu.luc.cs.trull.demo.counter;

import javax.swing.JComponent;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Local;
import edu.luc.cs.trull.Rename;
import edu.luc.cs.trull.Watching;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A preemption wrapper around a component.
 * <p>
 * <A HREF="../../../../../demo/triveni/demo/counter/Haltable.tvn">view Triveni source</A>
 */
public class Haltable extends Local implements VisualComponent, EventLabels {

    private String haltLabel;

    private VisualComponent button;

    public Haltable(final String haltLabel, final Component component) {
        this.haltLabel = haltLabel;
        final String HALT = "Halt";
        setLocalEvent(HALT);
        button = new ButtonComponent(haltLabel);
        addComponent(
           new Watching(new Composite(
              component, 
              new Rename(TICK, HALT, button)
           ),                    
           HALT 
        ));
    }

    public JComponent getView() { return button.getView(); }
}