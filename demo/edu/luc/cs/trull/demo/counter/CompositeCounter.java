package edu.luc.cs.trull.demo.counter;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Local;
import edu.luc.cs.trull.Rename;

/**
 * A composite counter suitable for building a multi-digit counter from
 * two existing carry counters.  The overflow of the less significant
 * digits (c1) increments the more significant digits (c2).
 * <p>
 * <A HREF="../../../../../demo/triveni/demo/counter/CompositeCounter.tvn">view Triveni source</A>
 */
public class CompositeCounter extends Local implements EventLabels {

    private Component c1;

    private Component c2;
    
    final String CARRY = "Carry";

    public CompositeCounter(final Component c1, final Component c2) {
        this.c1 = c1;
        this.c2 = c2;
        setLocalEvent(CARRY);
        addComponent(new Composite(
            new Rename(OVERFLOW, CARRY, c1),
            new Rename(UP, CARRY, c2)
        ));
    }
}