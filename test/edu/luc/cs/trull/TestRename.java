package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

/**
 * do emit a watching b || do emit b watching a 
 */
public class TestRename extends TestCase {
	
	private static final String HELLO = "Hello"; 
	private static final String WORLD = "World"; 
	private static final String HOLA  = "Hola"; 
	private static final String MUNDO = "Mundo"; 

	private List history = new LinkedList();
	
	public void setUp() throws Exception {
		super.setUp();
	  history.clear();
	}
	
	public void tearDown() throws Exception {
	  super.tearDown();
	}
		
	public void testChoice() throws Exception {
	  Control con = new Control();
		Emit emit = new Emit();
		emit.setLabel(WORLD);
		con.setStartEvents(new String[] { HELLO });
		con.setBody(emit);
		
		final Rename top = new Rename();
		top.addComponent(con);
		top.addEventRenaming(HELLO, HOLA);
		top.addEventRenaming(WORLD, MUNDO);
		
		top.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				history.add(evt);
				System.out.println("got event " + evt.getPropertyName());
			}
		});

		TopLevelComponent tlc = 
		  new DefaultTopLevelComponent(top, new DefaultScheduler());
		tlc.start();
		tlc.propertyChange(new PropertyChangeEvent(this, HELLO, null, HELLO));
		tlc.await();
		assertEquals(0, history.size());
		tlc.propertyChange(new PropertyChangeEvent(this, HOLA, null, HOLA));
    tlc.await();
		assertEquals(1, history.size());
		assertEquals(MUNDO, ((PropertyChangeEvent) history.get(0)).getPropertyName()); 
	}
}
