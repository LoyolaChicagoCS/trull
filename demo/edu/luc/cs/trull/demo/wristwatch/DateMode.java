package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.EmitComponent;

/**
 * datemode =
 *   emit DATE
 * ; emit NOW(60 * month + day)
 * ; loop
 *     UPDATE -> emit NOW(60 * month + day)
 */
public class DateMode extends EmitComponent implements EventLabels {

	private DateModel data;

	public void setModel(DateModel data) {
		this.data = data;
	}

	public void start(PropertyChangeEvent incoming) {
		scheduleEvent(DATE);
   	scheduleEvent(NOW, new Integer(DateModel.SEC_PER_MIN * data.getMonth() + data.getDay()));
	}

	public void stop() { }

	public void propertyChange(final PropertyChangeEvent event) {
		if (UPDATE.equals(event.getPropertyName())) {
	   	scheduleEvent(NOW, new Integer(DateModel.SEC_PER_MIN * data.getMonth() + data.getDay()));
		}
	}
}