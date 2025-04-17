package edu.luc.cs.trull;

import junit.framework.TestCase;

public class TestDone extends TestCase {
	
	public void setUp() throws Exception {
		super.setUp();
	}

	public void tearDown() throws Exception {
	  super.tearDown();
	}
	
	public void testDone() throws Exception {
		final Done done = new Done();
		new DefaultTopLevelComponent(done, new DefaultScheduler()).start();
	}

	public void testDoneComposite() throws Exception {
		final Composite top = new Composite();
		top.addComponent(new Done());
		top.addComponent(new Done());
		top.addComponent(new Done());
		top.addComponent(new Done());
		top.addComponent(new Done());
		new DefaultTopLevelComponent(top, new DefaultScheduler()).start();
	}

	public void testDoneSequence() throws Exception {
		final Sequence top = new Sequence();
		top.addComponent(new Done());
		top.addComponent(new Done());
		top.addComponent(new Done());
		top.addComponent(new Done());
		top.addComponent(new Done());
		new DefaultTopLevelComponent(top, new DefaultScheduler()).start();
	}
}
