package edu.luc.cs.trull.demo.microwave;

import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import edu.luc.cs.trull.swing.ActionEventAdapter;
import edu.luc.cs.trull.swing.VisualComponent;

/**
 * The control panel consists of buttons for setting the time, for starting
 * and stopping the device, for setting timer-only mode, and for changing the
 * power setting.  It emits events corresponding to these buttons.  The
 * control panel also displays the time left and the power setting.  It is
 * essentially stateless, that is, it contains no control logic, except
 * for maintaining the status of the door (open or closed).
 */
public class ControlPanel extends ActionEventAdapter implements VisualComponent, EventLabels {

    JPanel container = new JPanel();
    
    TimeDisplay time = new TimeDisplay();
    JLabel level = new JLabel(HIGH, JLabel.CENTER);

    JCheckBox heatStatus  = new JCheckBox("Heat on", false);
    JCheckBox lightStatus = new JCheckBox("Light on", false);

    public ControlPanel() {

        // To handle an incoming event, change the display according to the label
        // of the event.

        final JButton tenMin = new JButton("10 min");
        final JButton oneMin = new JButton("1 min");
        final JButton tenSec = new JButton("10 sec");
        final JButton oneSec = new JButton("1 sec");
        final JButton power = new JButton("Power");
        final JButton timer = new JButton("Timer");
        final JButton start = new JButton("Start");
        final JButton reset = new JButton("Stop/Reset");

        tenMin.setMnemonic(KeyEvent.VK_N);
        oneMin.setMnemonic(KeyEvent.VK_M);
        tenSec.setMnemonic(KeyEvent.VK_0);
        oneSec.setMnemonic(KeyEvent.VK_1);
        power.setMnemonic(KeyEvent.VK_P);
        timer.setMnemonic(KeyEvent.VK_T);
        start.setMnemonic(KeyEvent.VK_S);
        reset.setMnemonic(KeyEvent.VK_R);

        final JRadioButton openDoor  = new JRadioButton("Open door", false);
        final JRadioButton closeDoor = new JRadioButton("Close door", true);
        final ButtonGroup doorGroup = new ButtonGroup();

        doorGroup.add(openDoor);
        doorGroup.add(closeDoor);

        openDoor.setMnemonic(KeyEvent.VK_O);
        closeDoor.setMnemonic(KeyEvent.VK_C);

        // These are indicators only.

        heatStatus.setEnabled(false);
        lightStatus.setEnabled(false);

        container.setLayout(new GridLayout(2, 7));

        container.add(heatStatus);
        container.add(openDoor);
        container.add(tenMin);
        container.add(oneMin);
        container.add(tenSec);
        container.add(oneSec);
        container.add(time);

        container.add(lightStatus);
        container.add(closeDoor);
        container.add(power);
        container.add(timer);
        container.add(start);
        container.add(reset);
        container.add(level);

        // Event listener for the door.  It also maintains the status of the
        // door.

        ItemListener doorListener = new ItemListener() {
            boolean open = doorGroup.getSelection() == openDoor;
            public void itemStateChanged(ItemEvent e) {
                if (open == (e.getSource() == closeDoor)) {
                    open = ! open;
                    scheduleEvent(open ? OPEN : CLOSE);
                }
            }
        };

        openDoor.addItemListener(doorListener);
        closeDoor.addItemListener(doorListener);

        // Event listeners for buttons.  Each button sends out the
        // corresponding event.

        tenMin.setActionCommand(TENMIN);
        oneMin.setActionCommand(ONEMIN);
        tenSec.setActionCommand(TENSEC);
        oneSec.setActionCommand(ONESEC);
        power.setActionCommand(POWER);
        timer.setActionCommand(TIMER);
        reset.setActionCommand(RESET);
        start.setActionCommand(START);
        
        tenMin.addActionListener(this);
        oneMin.addActionListener(this);
        tenSec.addActionListener(this);
        oneSec.addActionListener(this);
        power.addActionListener(this);
        timer.addActionListener(this);
        reset.addActionListener(this);
        start.addActionListener(this);
    }

    public void propertyChange(final PropertyChangeEvent evt) {
        final String label = evt.getPropertyName();
        if (TIME.equals(label)) {
            time.setValue(((TimeData) evt.getNewValue()).getValue());
        } else if (HIGH.equals(label)) {
            level.setText(HIGH);
        } else if (MEDIUM.equals(label)) {
            level.setText(MEDIUM);
        } else if (LOW.equals(label)) {
            level.setText(LOW);
        } else if (OFF.equals(label)) {
            level.setText(TIMER);
        } else if (LIGHTON.equals(label)) {
            lightStatus.setSelected(true);
        } else if (LIGHTOFF.equals(label)) {
            lightStatus.setSelected(false);
        } else if (HEATON.equals(label)) {
            heatStatus.setSelected(true);
        } else if (HEATOFF.equals(label)) {
            heatStatus.setSelected(false);
        } else if (BEEP.equals(label)) {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public JComponent getView() { return container; }
}


/**
 * A label corresponding to a single digit.
 */

class DigitLabel extends JLabel {

    int val = 0;

    public DigitLabel() {
        super("0", CENTER);
    }

    public void setValue(int val) {
        this.val = val % 10;
        setText(Integer.toString(val));
    }

    public void increment() {
        setValue((val + 1) % 10);
    }

    public void reset() {
        setValue(0);
    }
}


/**
 * A panel for displaying a time of the form mm:ss.
 */

class TimeDisplay extends JPanel {

    public DigitLabel tenMin = new DigitLabel();
    public DigitLabel oneMin = new DigitLabel();
    public DigitLabel tenSec = new DigitLabel();
    public DigitLabel oneSec = new DigitLabel();

    public TimeDisplay() {
        super(new GridLayout(1, 5));
        add(tenMin);
        add(oneMin);
        add(new JLabel(":", JLabel.CENTER));
        add(tenSec);
        add(oneSec);
    }

    public void reset() {
        tenMin.reset();
        oneMin.reset();
        tenSec.reset();
        oneSec.reset();
    }

    public void setValue(int val) {
        tenMin.setValue(val / 600 % 10);
        oneMin.setValue(val / 60 % 10);
        tenSec.setValue(val % 60 / 10);
        oneSec.setValue(val % 10);
    }
}
