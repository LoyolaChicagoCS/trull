package edu.luc.cs.trull.demo.office;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.EmitComponent;

/**
 * The occupant control logic for an office.  It allows the occupant
 * to set the thermostat and the light.
 */
public class OccupantControl extends EmitComponent implements EventLabels {

  private Temp actualTemp = new Temp(Temp.INITIAL_ACTUAL_TEMP);
  
  private Temp targetTemp = new Temp(Temp.INITIAL_TARGET_TEMP);
  
	public void propertyChange(final PropertyChangeEvent event) {
		if (REQUESTTEMP.equals(event.getPropertyName())) {
			int temp = ((Integer) event.getNewValue()).intValue();
			scheduleEvent(SETTEMP, new Integer(temp));
		} else if (SWITCHON.equals(event.getPropertyName())) {
			scheduleEvent(LIGHTON);
		} else if (SWITCHOFF.equals(event.getPropertyName())) {
			scheduleEvent(LIGHTOFF);
		}
	}
}
