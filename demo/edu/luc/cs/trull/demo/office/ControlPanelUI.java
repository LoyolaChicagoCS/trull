package edu.luc.cs.trull.demo.office;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import edu.luc.cs.trull.EmitComponent;

/**
 * The central control panel for an office building.  It allows
 * setting the ambient (outside) temperature and the economy 
 * temperature that applies when the entire building is set to
 * economy mode.  It also allows switching the building between
 * occupant mode and economy mode.
 */
public class ControlPanelUI extends EmitComponent implements EventLabels {

  public ControlPanelUI() {
	
	JPanel panel = new JPanel();
	panel.setPreferredSize(new Dimension(192, 223));
    panel.setLayout(new GridLayout(6, 1, 10, 10));

    final JSlider currentTemp = 
      new JSlider(JSlider.HORIZONTAL, Temp.MIN_TEMP, Temp.MAX_TEMP, Temp.INITIAL_ACTUAL_TEMP);
    currentTemp.setMajorTickSpacing(10);
    currentTemp.setPaintLabels(true);

    currentTemp.addChangeListener(new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
          // getValueIsAdjusting does not work in JDK 1.2.2
          // if (! ((JSlider) e.getSource()).getValueIsAdjusting()) {
     			int temp = currentTemp.getValue();
   				firePropertyChange(TEMP, new Integer(temp));
          // }
        }
    });

    final JSlider economyTemp = new JSlider(JSlider.HORIZONTAL, Temp.MIN_TEMP, Temp.MAX_TEMP, Temp.INITIAL_ECONOMY_TEMP);
    economyTemp.setMajorTickSpacing(10);
    economyTemp.setPaintLabels(true);

    final JCheckBox turnOnEconomy = new JCheckBox("economy on", false);
    final JCheckBox turnOffEconomy = new JCheckBox("economy off", true);
    final ButtonGroup economyGroup = new ButtonGroup();

    economyGroup.add(turnOnEconomy);
    economyGroup.add(turnOffEconomy);

    ItemListener economyListener = new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        if (turnOnEconomy.isSelected()) {
          	int temp = economyTemp.getValue();
    				firePropertyChange(ECONOMYMODE, new Integer(temp));
        } else {
        		firePropertyChange(OCCUPANTMODE);
        }
      }
    };
    turnOnEconomy.addItemListener(economyListener);

    panel.add(new JLabel("ambient temp:"));
    panel.add(currentTemp);
    panel.add(new JLabel("economy temp:"));
    panel.add(economyTemp);
    panel.add(turnOnEconomy);
    panel.add(turnOffEconomy);

    JFrame frame = new JFrame("Control Panel");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setContentPane(panel);
    frame.pack();
    frame.show();
  }
}