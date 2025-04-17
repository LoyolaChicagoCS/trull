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
public class TestLoop extends TestCase {

	private static final String HELLO = "Hello";

  private static final String WORLD = "World";

  private List history = new LinkedList();

	private long eventCount;

	public void setUp() throws Exception {
		super.setUp();
		history.clear();
		eventCount = 0;
	}

	public void tearDown() throws Exception {
		super.tearDown();
	}

	public void testLoop() throws Exception {
		final int time = 10000;
		final Loop top = new Loop();
		Emit emit = new Emit();
		emit.setLabel(HELLO);
		top.addComponent(emit);

		top.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				eventCount++;
				//				history.add(evt);
				//				System.out.println("got event " + evt.getPropertyName());
			}
		});

		TopLevelComponent tlc = 
		  new DefaultTopLevelComponent(top, new DefaultScheduler());
		tlc.start();
		Thread.sleep(time);
		tlc.stop();
		tlc.execute(new Runnable() {
			public void run() {
				System.out.println(eventCount * 1000 / time + " eps");
			}
		});
	}

	public void testLoop2() throws Exception {
		final int num = 10000;
    final PropertyChangeEvent event = new PropertyChangeEvent(this, HELLO, null, HELLO);
		final Loop top = new Loop(new AwaitOne(HELLO, new Emit(WORLD)));

		top.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				eventCount++;
			}
		});

		TopLevelComponent tlc = 
		  new DefaultTopLevelComponent(top, new DefaultScheduler());
		tlc.start();
    for (int i = 0; i < num; i ++) {
      tlc.propertyChange(event);
    }
		tlc.execute(new Runnable() {
			public void run() {	}
		});
    tlc.execute(new Runnable() {
      public void run() { }
    });
    assertEquals(num, eventCount);
	}
}

