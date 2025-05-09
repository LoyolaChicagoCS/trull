package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.EmitComponent;

/**
 *     ( emit MONTH
 *     ; emit NOW(60 * month + day)
 *     ; loop
 *         START -> emit NOW(60 * (month = ...) + day)
 *     )
*/
class SetMonth extends EmitComponent implements EventLabels, DateConstants {
	
	private DateModel data;
	
	public void setModel(DateModel data) {
		this.data = data;
	}
	
	public void start(PropertyChangeEvent incoming) { 
	  super.start(incoming);
		scheduleEvent(MONTH);
		scheduleEvent(NOW, new Integer(SEC_PER_MIN * data.getMonth() + data.getDay()));
	}

	public void propertyChange(final PropertyChangeEvent event) {
		if (START.equals(event.getPropertyName())) {
			data.setMonth(data.getMonth() % MONTHS_PER_YEAR + 1);
			data.setDay((data.getDay() - 1) % data.daysPerMonth() + 1);
			scheduleEvent(NOW, new Integer(SEC_PER_MIN * data.getMonth() + data.getDay()));
		}
	}
}
