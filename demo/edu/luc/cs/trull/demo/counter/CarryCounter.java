package edu.luc.cs.trull.demo.counter;

import javax.swing.JComponent;

import edu.luc.cs.trull.AwaitOne;
import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.Rename;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A carry counter suitable for building a multi-digit counter.
 * There are two cases, a single-digit carry counter that reports
 * overflow when it reaches its maximum value, and a carry counter
 * build from two existing carry counters.
 * <p>
 * To chain two carry counters, the overflow of the less significant digits (c1)
 * increments the more significant digits (c2).
 * <p>
 * <A HREF="../../../../../demo/triveni/demo/counter/CarryCounter.tvn">view Triveni source</A>
 */
public class CarryCounter extends Rename implements VisualComponent, EventLabels {

    private int max;
    
    private VisualComponent counter;

    public CarryCounter(final int max) {
        this.max = max;
        addEventRenaming(OUCH, OVERFLOW);
        setLocalEvents(new String[] { DOWN, RESET, PRESSED });
        counter = new CounterComponent(DOWN, max, 100, 40); 
        Composite body = new Composite(new Component[] {
            counter, 
            new Loop(new AwaitOne(OUCH, new Emit(RESET))),
            new Loop(new AwaitOne(PRESSED, new Emit(DOWN)))
        });
        addComponent(body);
    }

    public JComponent getView() { return counter.getView(); }
}