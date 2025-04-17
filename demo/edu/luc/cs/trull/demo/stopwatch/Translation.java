package edu.luc.cs.trull.demo.stopwatch;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.EmitComponent;
import edu.luc.cs.trull.demo.wristwatch.EventLabels;

/**
 * The controller logic of the stopwatch.  
 * It can run as part of this stopwatch 
 * or as a component within another Trull application.  
 */
public class Translation extends EmitComponent implements EventLabels {

	private Application swa = new Application();
  
	private final int STOP_RUNTIME_STATE = 0;
  private final int RUN_RUNTIME_STATE = 1;
  private final int RUN_LAPTIME_STATE = 2;
  private final int STOP_LAPTIME_STATE = 3;

  private int theState = STOP_RUNTIME_STATE;

	public void propertyChange(PropertyChangeEvent event) {
		String eventLabel = event.getPropertyName();
		if (eventLabel.equals(TICK)) {
			if (theState == RUN_RUNTIME_STATE) {
				swa.incRuntime();
				scheduleEvent(NOW, new Integer(swa.getRuntime()));
			} else if (theState == RUN_LAPTIME_STATE) {
				swa.incRuntime();
			}
		} else if (eventLabel.equals(START)) {
			if (theState == STOP_RUNTIME_STATE) {
				theState = RUN_RUNTIME_STATE;
			} else if (theState == RUN_RUNTIME_STATE) {
				theState = STOP_RUNTIME_STATE;
			} else if (theState == RUN_LAPTIME_STATE) {
				theState = STOP_LAPTIME_STATE;
			} else if (theState == STOP_LAPTIME_STATE) {
				theState = RUN_LAPTIME_STATE;
			}
		} else if (eventLabel.equals(RESET)) {
			if (theState == RUN_RUNTIME_STATE) {
				theState = RUN_LAPTIME_STATE;
			} else if (theState == RUN_LAPTIME_STATE) {
				theState = RUN_RUNTIME_STATE;
			} else if (theState == STOP_RUNTIME_STATE) {
				swa.resetRuntime();
			} else if (theState == STOP_LAPTIME_STATE) {
				theState = STOP_RUNTIME_STATE;
			}
			scheduleEvent(NOW, new Integer(swa.getRuntime()));
		}
	}

	public void start(PropertyChangeEvent incoming) {
		super.start(incoming);
		scheduleEvent(STOP);
		scheduleEvent(NOW, new Integer(swa.getRuntime()));
	}
	
	public void stop() {
		super.stop();
	}
}
