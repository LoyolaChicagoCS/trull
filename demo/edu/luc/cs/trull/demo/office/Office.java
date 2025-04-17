package edu.luc.cs.trull.demo.office;

import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Rename;

/**
 * An office in an office building.
 * <pre>
 *   local 
 *     SWITCHON, SWITCHOFF, LIGHTON, LIGHTOFF, 
 *     REQUESTTEMP, SETTEMP, DOOROPEN, DOORCLOSE
 *   in
 *     temperatureStableOffice | officeControl
 * </pre>
 */
public class Office extends Rename implements EventLabels {

	private TemperatureStableOffice tso;
	
  public Office(String name) {
  	  setLocalEvents(new String[] { 
  	    SWITCHON, SWITCHOFF, LIGHTON, LIGHTOFF, REQUESTTEMP, SETTEMP, DOOROPEN, DOORCLOSE
  	  });
  	  Composite top = new Composite();
    top.addComponent(new TemperatureStableOffice(name));
    top.addComponent(new OfficeControl());
    addComponent(top);
  }
}

