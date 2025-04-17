package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import junit.framework.TestCase;

/**
 *  
 */
public class TestIf extends TestCase {
	
	int result;
	
	public void setUp() throws Exception {
		super.setUp();
		result = -1;
	}
	
	public void tearDown() throws Exception {
	  super.tearDown();
	}
	
	private static final String TEST = "Test";
	private static final String BLAH = "Blah";
		
	public void testChoice() throws Exception {
	  final If top = new If();
	  top.setPredicate(new EventPredicate() {
	  		public boolean apply(PropertyChangeEvent event) {
	  			return TEST.equals(event.getPropertyName());
	  		}
	  });
	  top.addComponent(new ActionComponent(new PropertyChangeListener() {
	  	  public void propertyChange(PropertyChangeEvent event) {
	  	  	  result = 77; 
	  	  }
	  }));
	  top.addComponent(new ActionComponent(new PropertyChangeListener() {
	  	  public void propertyChange(PropertyChangeEvent event) {
   	  	  result = 88; 
	  	  }
	  }));
	  
	  new DefaultTopLevelComponent(top, new DefaultScheduler())
	  	.start(new PropertyChangeEvent(this, TEST, null, TEST));
		assertEquals(77, result);
	  new DefaultTopLevelComponent(top, new DefaultScheduler())
  		.start(new PropertyChangeEvent(this, BLAH, null, BLAH));
		assertEquals(88, result);
	}
}
