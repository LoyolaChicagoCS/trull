package edu.luc.cs.trull.demo.office;

import edu.luc.cs.trull.Composite;

/**
 * An office building.
 * <pre>
 * controlPanelUI | office1 | ... | officeN
 * </pre>
 */
public class Building extends Composite {
	
	public Building() {
	  addComponent(new ControlPanelUI());
	}
	
	public void addOffice(String name) {
		addComponent(new Office(name));
	}
}
