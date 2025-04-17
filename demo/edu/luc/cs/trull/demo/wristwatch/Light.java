package edu.luc.cs.trull.demo.wristwatch;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.EmitComponent;
    
/**
 * light =
 *   loop
 *     await LIGHT -> 
 *       { ticks = DELAY; }
 *       emit LIGHT_ON
 *     ; do
 *         loop
 *           await TICK ->
 *             if -- ticks < 0 then
 *               emit LIGHT_OFF
 *       watching LIGHT_OFF
 */
public class Light extends EmitComponent implements EventLabels {

    public final static int DELAY = 3;

    private int ticks = 0;
		
    public void propertyChange(final PropertyChangeEvent event) {
			String label = event.getPropertyName();
			if (ticks == 0 && LIGHT.equals(label)) {
				ticks = DELAY;
				scheduleEvent(LIGHT_ON);
			} else if (ticks > 0 && TICK.equals(label)) {
				ticks --;
				if (ticks == 0) {
					scheduleEvent(LIGHT_OFF);
				}
			}
		}
}
