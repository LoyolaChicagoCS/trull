package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.EmitComponent;

/**
 * ( emit HOUR
 * ; emit NOW(time / 60)
 * ; loop
 *  	START -> emit NOW(time = (time + 3600) % ...)
 * )
*/
class SetHour extends EmitComponent implements EventLabels, DateConstants {

	private DateModel data;
	
	public void setModel(DateModel data) {
		this.data = data;
	}

	public void start(PropertyChangeEvent incoming) { 
	  super.start(incoming);
		scheduleEvent(HOUR);
		scheduleEvent(NOW, new Integer(data.getTime() / SEC_PER_MIN));
	}
	
	public void propertyChange(final PropertyChangeEvent event) {
		if (START.equals(event.getPropertyName())) {
			data.setTime((data.getTime() + SEC_PER_HOUR) % SEC_PER_DAY);					
			scheduleEvent(NOW, new Integer(data.getTime() / SEC_PER_MIN));
		}
	}
}
