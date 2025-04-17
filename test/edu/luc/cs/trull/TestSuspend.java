package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

/**
 * suspend on b resume on c in emit a || suspend on a resume on c in emit b
 */
public class TestSuspend extends TestCase {
	
	private static final String HELLO = "Hello"; 
	private static final String LEFT = "Left"; 
	private static final String RIGHT = "Right"; 
	private static final String RESUME = "Resume"; 

	private List history = new LinkedList();
	
	public void setUp() throws Exception {
		super.setUp();
	  history.clear();
	}
	
	public void tearDown() throws Exception {
	  super.tearDown();
	}
		
	public void testSuspend() throws Exception {
	  Suspend left = new Suspend();
		Emit emitLeft = new Emit();
		emitLeft.setLabel(RIGHT);
		left.setSuspendEvent(LEFT);
		left.setResumeEvent(RESUME);
		left.addComponent(emitLeft);
		
		Suspend right = new Suspend();
		Emit emitRight = new Emit();
		emitRight.setLabel(LEFT);
		right.setSuspendEvent(RIGHT);
		right.setResumeEvent(RESUME);
		right.addComponent(emitRight);
		
		final Composite top = new Composite();
		top.addComponent(left);
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
		Thread.sleep(2000);
		tlc.propertyChange(new PropertyChangeEvent(this, RESUME, null, RESUME));
		tlc.await();

		assertEquals(2, history.size());
	}
}

