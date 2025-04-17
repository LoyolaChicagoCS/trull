package edu.luc.cs.trull.demo.office;

import edu.luc.cs.trull.DefaultTopLevelComponent;
import edu.luc.cs.trull.TopLevelComponent;
import edu.luc.cs.trull.swing.FastSwingScheduler;

/**
 * A driver for running an office building.
 */
public class Main {
	
	public static void main(String[] args) throws Exception {
	  Building building = new Building();
	  for (int i = 0; i < args.length; i ++) {
	  		building.addOffice(args[i]);
	  }
	  TopLevelComponent wrapper = 
	  	new DefaultTopLevelComponent(building, new FastSwingScheduler());
	  wrapper.start();
	}
}
