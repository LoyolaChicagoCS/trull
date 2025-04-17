package edu.luc.cs.trull;

import java.beans.PropertyChangeEvent;

/**
 * A top-level component wrapper implementation that combines 
 * a scheduler with an ordinary Trull component.  
 * The component's lifecycle and event listener methods are executed 
 * on the provided scheduler.
 * @see edu.luc.cs.trull.Scheduler
 */
public class DefaultTopLevelComponent implements TopLevelComponent {

	private Component component;
	
	private Scheduler scheduler;
	
	/**
	 * Constructs an empty top-level component wrapper.  The component
	 * and the scheduler can be set later.
	 */
	public DefaultTopLevelComponent() { }
	
	/**
	 * Constructs a top-level component wrapper from the given 
	 * component and scheduler.
	 * @param component the original component
	 * @param scheduler the scheduler used for executing the component's lifecycle methods
	 */
	public DefaultTopLevelComponent(Component component, Scheduler scheduler) {
		setComponent(component);
		setScheduler(scheduler);
	}
	
	/**
	 * Sets the underlying Trull component of this top-level component wrapper.
	 * @param component the underlying Trull component
	 */
	public synchronized void setComponent(Component component) {
		this.component = component;
		setScheduler(scheduler);
	}
	
	/**
	 * Sets the scheduler to be used by this top-level component wrapper.
	 * @param scheduler the scheduler
	 */
	public synchronized void setScheduler(Scheduler scheduler) {
		this.scheduler = scheduler;
		if (scheduler != null && component != null && component instanceof SchedulerAware) {
			((SchedulerAware) component).setScheduler(scheduler);
		}
	}
	
  public void start() throws InterruptedException {
		start(null);
	}

	public void start(final PropertyChangeEvent event) throws InterruptedException {
		scheduler.execute(new Runnable() {
			public void run() {
				component.start(event);
			}
		});
	}

	public void stop() throws InterruptedException {
		scheduler.execute(new Runnable() {
			public void run() {
				component.stop();
			}
		});
	}

  public void suspend() throws InterruptedException {
		scheduler.execute(new Runnable() {
			public void run() {
				component.suspend();
			}
		});
	}

	public void resume() throws InterruptedException {
		scheduler.execute(new Runnable() {
			public void run() {
				component.resume();
			}
		});
	}

	public void propertyChange(final PropertyChangeEvent event) throws InterruptedException {
		scheduler.execute(new Runnable() {
			public void run() {
				component.propertyChange(event);
			}
		});
	}
	
	public void execute(final Runnable r) throws InterruptedException {
		scheduler.execute(r);
	}

	public void await() throws InterruptedException {
		scheduler.await();
	}
}