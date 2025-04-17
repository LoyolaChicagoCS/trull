package edu.luc.cs.trull.demo.office;

import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.luc.cs.trull.EmitComponent;

/**
 * The occupant control panel for an office.  It provides a
 * target temperature setting, a light switch,
 * and the ability to open or close the door.  It also displays
 * the actual ambient temperature, whether the heat is on, and whether
 * the light is on.
 */
public class OfficeUI extends EmitComponent implements EventLabels {

	private static int count = 0;

	private JCheckBox heatStatus;
	private JCheckBox lightStatus;
	private JCheckBox economyStatus;
	private JSlider tempStatus;
	private JFrame frame;

	public OfficeUI(String name) {
		count ++;
		
		heatStatus = new JCheckBox("heat on", false);
		heatStatus.setEnabled(false);
		lightStatus = new JCheckBox("light on", false);
		lightStatus.setEnabled(false);
		economyStatus = new JCheckBox("economy on", false);
		economyStatus.setEnabled(false);
		
		tempStatus = new JSlider(JSlider.HORIZONTAL, Temp.MIN_TEMP, Temp.MAX_TEMP, Temp.INITIAL_ACTUAL_TEMP);
		tempStatus.setMajorTickSpacing(10);
		tempStatus.setPaintLabels(true);
		tempStatus.setEnabled(false);
		
		final JSlider requestedTemp = new JSlider(JSlider.HORIZONTAL, Temp.MIN_TEMP, Temp.MAX_TEMP, Temp.INITIAL_TARGET_TEMP);
		requestedTemp.setMajorTickSpacing(10);
		requestedTemp.setPaintLabels(true);

		requestedTemp.addChangeListener(new ChangeListener() {
   		public void stateChanged(ChangeEvent e) {
		    // getValueIsAdjusting does not work in JDK 1.2.2
     		// if (! ((JSlider) e.getSource()).getValueIsAdjusting()) {
   			int temp = requestedTemp.getValue();
				firePropertyChange(REQUESTTEMP, new Integer(temp));
     		// }
		  }
		});
		
		final JCheckBox openDoor = new JCheckBox("door open", false);
		final JCheckBox closeDoor = new JCheckBox("door closed", true);
		final ButtonGroup doorGroup = new ButtonGroup();
		
		doorGroup.add(openDoor);
		doorGroup.add(closeDoor);
		
		ItemListener doorListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean state = openDoor.isSelected();
				firePropertyChange(state ? DOOROPEN : DOORCLOSE);
			}
		};
		openDoor.addItemListener(doorListener);
		
		final JCheckBox turnOnSwitch = new JCheckBox("switch on", false);
		final JCheckBox turnOffSwitch = new JCheckBox("switch off", true);
		final ButtonGroup switchGroup = new ButtonGroup();
		
		switchGroup.add(turnOnSwitch);
		switchGroup.add(turnOffSwitch);
		
		ItemListener switchListener = new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				boolean state = turnOnSwitch.isSelected();
				firePropertyChange(state ? SWITCHON : SWITCHOFF);
			}
		};
		turnOnSwitch.addItemListener(switchListener);

		frame = new JFrame(name);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		Container contentPane = frame.getContentPane();
		
		contentPane.setLayout(new GridLayout(6, 2, 10, 10));
		contentPane.add(new JLabel("current temp:"));
		contentPane.add(tempStatus);
		contentPane.add(new JLabel("target temp:"));
		contentPane.add(requestedTemp);
		contentPane.add(turnOnSwitch);
		contentPane.add(turnOffSwitch);
		contentPane.add(openDoor);
		contentPane.add(closeDoor);
		contentPane.add(heatStatus);
		contentPane.add(lightStatus);
		contentPane.add(economyStatus);
		
		frame.setSize(200, 250);
		
		// figure out frame placement
		
		int frameWidth = frame.getSize().width;
		int frameHeight = frame.getSize().height;
		int framesPerRow = frame.getToolkit().getScreenSize().width / frameWidth;
		int framesPerColumn = frame.getToolkit().getScreenSize().height / frameHeight;
		int framesPerScreen = framesPerRow * (frame.getToolkit().getScreenSize().height / frameHeight);
		
		// do not occupy the top left corner
		// it is reserved for the global controls
		
		if (count % framesPerScreen == 0) { count ++; }
		int offset = 20 * (count / framesPerScreen);
		
		frame.setLocation(offset + frameWidth * (count % framesPerRow), offset + frameHeight * (count / framesPerRow % framesPerColumn));
		frame.show();
	}
	
	public void propertyChange(PropertyChangeEvent event) {
		String label = event.getPropertyName();
		if (HEATON.equals(label)) {
			heatStatus.setSelected(true);
		} else if (HEATOFF.equals(label)) {
			heatStatus.setSelected(false);
		} else if (LIGHTON.equals(label)) {
			lightStatus.setSelected(true);
		} else if (LIGHTOFF.equals(label)) {
			lightStatus.setSelected(false);
		} else if (ECONOMYMODE.equals(label)) {
			economyStatus.setSelected(true);
		} else if (OCCUPANTMODE.equals(label)) {
			economyStatus.setSelected(false);
		} else if (TEMP.equals(label)) {
			int temp = ((Integer) event.getNewValue()).intValue();
			tempStatus.setValue(temp);
		}
	}
}
