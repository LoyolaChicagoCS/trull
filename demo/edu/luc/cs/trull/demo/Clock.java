package edu.luc.cs.trull.demo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;

import javax.swing.Timer;

import edu.luc.cs.trull.EmitComponent;

/**
 * A continuous one-tick-per-second activity.
 */

public class Clock extends EmitComponent {

    public static final String TICK = "TICK";
    
    public static final int DELAY = 1000;

    protected Timer timer;

    public Clock(final String tick, int delay) {
        timer = new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                firePropertyChange(tick);
            }
        });
    }

    public Clock(String tick) { this(tick, DELAY); }

    public Clock(int delay) { this(TICK, delay); }

    public Clock() { this(TICK); }

    public void start(PropertyChangeEvent event) {
        super.start(event);
        timer.start();
    }

    public void stop() {
        super.stop();
        timer.stop();
    }

    public void suspend() {
        super.suspend();
        timer.stop();
    }

    public void resume() {
        super.resume();
        timer.start();
    }
}
