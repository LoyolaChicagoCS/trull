package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.EmitComponent;

/**
 * timemode =
 *   emit TIME
 * ; emit NOW(time / 60)
 * ; loop
 *     UPDATE -> emit NOW(time / 60)
 */
public class TimeMode extends EmitComponent implements EventLabels {

	private DateModel data;
	
	public void setModel(DateModel data) {
		this.data = data;
	}

	public void start(PropertyChangeEvent incoming) {
	  super.start(incoming);
		scheduleEvent(TIME);
		scheduleEvent(NOW, new Integer(data.getTime() / DateModel.SEC_PER_MIN));
	}
	
	public void propertyChange(final PropertyChangeEvent event) {
		if (UPDATE.equals(event.getPropertyName())) {
			scheduleEvent(NOW, new Integer(data.getTime() / DateModel.SEC_PER_MIN));
		}
	}
}