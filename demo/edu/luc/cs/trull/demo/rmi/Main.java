package edu.luc.cs.trull.demo.rmi;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.rmi.RMISecurityManager;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;

import edu.luc.cs.trull.AwaitOne;
import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.EmitComponent;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.Rename;

/**
 * A peer-to-peer example.
 * <P>
 * <A HREF="../../../../demo/triveni/demo/remote/Remote.java">view source</A>
 */
public class Main {

    public static void main(String[] args) throws Exception {
        if (args.length != 4) {
            System.err.println("usage: java triveni.demo.remote.Peer selfHost selfName peerHost peerName");
            System.exit(1);
        }

        String selfHost = args[0];
        String selfName = args[1];
        String peerHost = args[2];
        String peerName = args[3];

        // Create and install a security manager
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        // Create a PeerImpl (the constructor does everything)
        Component component = new TestComponent(selfName, peerName);
        
        Peer self = new PeerImpl(component, selfHost, selfName, peerHost, peerName);
        self.start();
    }
}


/**
 * The event labels used in this demo.
 */
interface EventLabels {
    String PRESS    = "Press";
    String RESPONSE = "Response";
    String CHECK    = "Check";
    String DOH      = "Doh";
}

/**
 * The main logic of the demo component. 
 * Note the renaming, which ultimately hooks up our button to the peer's checkbox.
 */
class TestComponent extends Rename implements EventLabels {
    public TestComponent(String selfName, String peerName) {
    		addEventRenaming(RESPONSE, peerName);
    		addEventRenaming(CHECK, selfName);
    		setLocalEvent(PRESS);
    		addComponent(
            new Composite(
            		new UserInterface(selfName),
							new Loop(new AwaitOne(PRESS, 
                  new Composite(new Emit(RESPONSE), new Emit(DOH))))
            )
        );
    }
}

/**
 * A button (-> PRESS) and a checkbox (<- CHECK) in a frame.
 */
class UserInterface extends EmitComponent implements EventLabels {

    final JCheckBox checkbox = new JCheckBox(CHECK);

    public UserInterface(String name) {
        final JButton button = new JButton(PRESS);
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) { 
            		firePropertyChange(PRESS); 
            }
        });

        JFrame frame = new JFrame(name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new GridLayout(2, 1));
        frame.getContentPane().add(button);
        frame.getContentPane().add(checkbox);
        frame.pack();
        frame.setVisible(true);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        final String label = evt.getPropertyName();
        if (CHECK.equals(label)) {
            checkbox.setSelected(! checkbox.isSelected());
        }
    }
}
