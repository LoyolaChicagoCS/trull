package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

/**
 * do emit a watching b || do emit b watching a 
 */
public class TestChoice extends TestCase {
	
	private static final String LEFT = "Left"; 
	private static final String MIDDLE = "Middle"; 
	private static final String RIGHT = "Right"; 

	private List history = new LinkedList();
	
	public void setUp() throws Exception {
		super.setUp();
		history.clear();
	}
	
	public void tearDown() throws Exception {
	  super.tearDown();
	}
		
	public void testChoice() throws Exception {
	  Control left = new Control();
		Emit leftEmit = new Emit();
		leftEmit.setLabel(LEFT);
		left.setStopEvents(new String[] { RIGHT, MIDDLE });
		left.setBody(leftEmit);
		
		Control middle = new Control();
		Emit middleEmit = new Emit();
		middleEmit.setLabel(MIDDLE);
		middle.setStopEvents(new String[] { RIGHT, LEFT });
		middle.setBody(middleEmit);

		Control right = new Control();
		Emit rightEmit = new Emit();
		rightEmit.setLabel(RIGHT);
		right.setStopEvents(new String[] { LEFT, MIDDLE });
		right.setBody(rightEmit);
		
		final Composite top = new Composite();
		top.addComponent(left);
		top.addComponent(middle);
		top.addComponent(right);
		
		top.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				history.add(evt);
				System.out.println("got event " + evt.getPropertyName());
			}
		});

		TopLevelComponent tlc = 
		  new DefaultTopLevelComponent(top, new DefaultScheduler());

		tlc.start();
		tlc.await();
		assertEquals(1, history.size());
		Thread.sleep(2000);
    tlc.start();
		tlc.await();
		assertEquals(2, history.size());
	}
}

