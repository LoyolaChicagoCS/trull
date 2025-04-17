package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.EmitComponent;

/**
 * update =
 *   loop
 *     TICK -> { time ++; } emit UPDATE
 */
public class Update extends EmitComponent implements EventLabels {

	private DateModel data;
	
	public void setModel(DateModel data) {
		this.data = data;
	}
	
  public void propertyChange(final PropertyChangeEvent event) {
		if (TICK.equals(event.getPropertyName())) {
			data.incrementSecond();
			scheduleEvent(UPDATE);
		}
	}
}
