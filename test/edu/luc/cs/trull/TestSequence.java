package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

/**
 * loop
 *   emit a
 */
public class TestSequence extends TestCase {

	private static final String HELLO = "Hello"; 
	private static final String WORLD = "World"; 

	private List history = new LinkedList();

	public void setUp() throws Exception {
		super.setUp();
	  history.clear();
	}
	
	public void tearDown() throws Exception { 
	  super.tearDown();
	}
		
	public void testSequenceEmpty() throws Exception {
		final Sequence top = new Sequence();
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
		assertEquals(0, history.size());
	}

	public void testSequenceNonempty() throws Exception {
		Sequence seq = new Sequence();
		Emit emit = new Emit();
		emit.setLabel(HELLO);
		seq.addComponent(emit);
		Emit emit2 = new Emit();
		emit2.setLabel(WORLD);
		seq.addComponent(emit2);
		
		final Control top = new Control();
		top.setBody(seq);

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
		assertEquals(2, history.size());
		assertEquals(HELLO, ((PropertyChangeEvent) history.get(0)).getPropertyName()); 
		assertEquals(WORLD, ((PropertyChangeEvent) history.get(1)).getPropertyName()); 
	}
}


