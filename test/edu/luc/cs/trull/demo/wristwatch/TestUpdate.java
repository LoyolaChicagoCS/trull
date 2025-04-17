package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;

import junit.framework.TestCase;
import edu.luc.cs.trull.DefaultScheduler;
import edu.luc.cs.trull.DefaultTopLevelComponent;
import edu.luc.cs.trull.TopLevelComponent;

/**
 * A test case for testing the wristwatch's update component.
 */
public class TestUpdate extends TestCase implements EventLabels {

	Update update;
	DateModel data;
	TopLevelComponent component;

	protected void setUp() throws Exception {
		super.setUp();
		data = new DateModel();
		update = new Update();
		update.setModel(data);
		component = new DefaultTopLevelComponent(update, new DefaultScheduler());
	}
	
	protected void tearDown() throws Exception {
		data = null;
		update = null;
		component = null;
    super.tearDown();
	}
	
	public void testUpdate() throws Exception {
		assertEquals(0, data.getTime());
		int reps = 1000;
		for (int i = 0; i < reps; i ++) {
			component.propertyChange(new PropertyChangeEvent(this, TICK, null, TICK));
		}
		component.await();
		assertEquals(reps, data.getTime());
	}
}
