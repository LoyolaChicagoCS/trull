package edu.luc.cs.trull.demo.counter;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Local;
import edu.luc.cs.trull.Rename;
import edu.luc.cs.trull.Suspend;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * A suspend/resume wrapper around a component.
 * <p>
 * <A HREF="../../../../../demo/triveni/demo/counter/Pauseable.tvn">view Triveni source</A>
 */
public class Pauseable extends Local implements VisualComponent, EventLabels {

    private String pauseLab;

    private String continueLab;

    private Component component;
    
    private JPanel panel;

    public Pauseable(final String pauseLab, final String continueLab, final Component component) {
        this.pauseLab = pauseLab;
        this.continueLab = continueLab;
        this.component = component;

        final String PAUSE = "Pause";
        final String CONTINUE = "Continue";

        ButtonComponent pauseClock = new ButtonComponent(pauseLab);
        ButtonComponent continueClock = new ButtonComponent(continueLab);
        
        panel = new JPanel();
        panel.setLayout(new GridLayout(1, 0));
        panel.add(pauseClock.getView());
        panel.add(continueClock.getView());
        
        setLocalEvents(new String[] { PAUSE, CONTINUE });
        addComponent(new Composite(new Component[] {
            new Suspend(PAUSE, CONTINUE, component),
            new Rename(TICK, PAUSE, pauseClock),
            new Rename(TICK, CONTINUE, continueClock)
        }));
    }

    public JComponent getView() { return panel; }
}