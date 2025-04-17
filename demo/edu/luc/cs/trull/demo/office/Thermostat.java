package edu.luc.cs.trull.demo.office;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.EmitComponent;

/**
 * A simple thermostat.  Whenever the ambient temperature is
 * below the desired temperature, the heat is turned on.  When the
 * ambient temperature is at least the desired temperature,
 * the heat is turned off.
 */
public class Thermostat extends EmitComponent implements EventLabels {

  private Temp actualTemp = new Temp(Temp.INITIAL_ACTUAL_TEMP);
  
  private Temp targetTemp = new Temp(Temp.INITIAL_TARGET_TEMP);
  
	public void propertyChange(final PropertyChangeEvent event) {
		if (TEMP.equals(event.getPropertyName())) {
			int temp = ((Integer) event.getNewValue()).intValue();
			actualTemp.setTemp(temp);
		} else if (SETTEMP.equals(event.getPropertyName())) {
			int temp = ((Integer) event.getNewValue()).intValue();
			targetTemp.setTemp(temp);
		} else {
			return;
		}
		if (actualTemp.getTemp() < targetTemp.getTemp()) {
			scheduleEvent(HEATON);
		} else {
			scheduleEvent(HEATOFF);
		}
	}
}
