package edu.luc.cs.trull.demo.office;

import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Local;

/**
 * A temperature-stabilized office.
 * <pre>
 * local HEATON, HEATOFF in
 *   officeUI | thermostat
 * </pre>
 */
public class TemperatureStableOffice extends Local implements EventLabels {

	public TemperatureStableOffice(String name) {
		Composite composite = new Composite();
		composite.addComponent(new Thermostat());
		composite.addComponent(new OfficeUI(name));
		addComponent(composite);
		setLocalEvents(new String[] { HEATON, HEATOFF });
	}
}
