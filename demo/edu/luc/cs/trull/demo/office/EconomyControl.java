package edu.luc.cs.trull.demo.office;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import edu.luc.cs.trull.AwaitOne;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.EmptyComponent;
import edu.luc.cs.trull.EventFunction;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.Nil;
import edu.luc.cs.trull.Sequence;
import edu.luc.cs.trull.Watching;

/**
 * The economy control logic for an office.  Control temporarily 
 * reverts to occupant mode while the door is open.
 * <pre>
 *   loop
 *     await REQUESTTEMP(t) -> { lastTemp = t; }
 * |
 *     await ECONOMYMODE(t) / { economyTemp = t; } ->
 *     loop
 *       do
 *         loop
 *           do 
 *             emit SLEEP(economyTemp)
 *           ; nil
 *           watching DOOROPEN ->
 *             do
 *               emit AWAKE(lastTemp)
 *             ; nil
 *             watching DOORCLOSE
 *       watching OCCUPANTMODE ->
 *       do
 *         emit AWAKE(lastTemp)
 *       ; nil
 *       watching ECONOMYMODE(t) / { economyTemp = t; }
 * </pre>
 */
public class EconomyControl extends Composite implements EventLabels {

  private Temp lastTemp = new Temp(Temp.INITIAL_TARGET_TEMP);

  private Temp economyTemp = new Temp(Temp.INITIAL_ECONOMY_TEMP);

  public EconomyControl() {
    addComponent(new SetLastTemp());
    
    AwaitOne econ = new AwaitOne(ECONOMYMODE,
      new SetEconomyTemp(),
    		new Loop(
          new Watching(
            new Loop(
              new Watching(
                new Sequence(
                  new EmitSleep(),
                  Nil.getInstance()
                ),
                DOOROPEN,
                new Watching(
                  new Sequence(
                    new EmitAwake(),
                    Nil.getInstance()
                  ),
                  DOORCLOSE
                )
              )
            ),
            OCCUPANTMODE,
            new Watching(
              new Sequence(
                new EmitAwake(),
                Nil.getInstance()
              ),
              ECONOMYMODE,
              new SetEconomyTemp()
            )
          )
        )
      );
    addComponent(econ);
  }
  
  class SetLastTemp extends EmptyComponent {
  	  public void propertyChange(PropertyChangeEvent event) {
  	  	  if (REQUESTTEMP.equals(event.getPropertyName())) {
		  	  int temp = ((Integer) event.getNewValue()).intValue();
		  	  lastTemp.setTemp(temp);
  	  	  }
  	  }
  }
  
  class SetEconomyTemp implements PropertyChangeListener {
    public void propertyChange(PropertyChangeEvent event) {
	  	  int temp = ((Integer) event.getNewValue()).intValue();
	  	  economyTemp.setTemp(temp);
	  }
	}
  
  class EmitSleep extends Emit {
		public EmitSleep() {
			setLabel(SLEEP);
			setValueGenerator(new EventFunction() {
				public Object apply(PropertyChangeEvent incoming) {
					return new Integer(economyTemp.getTemp());
				}
			});
		}
  }

  class EmitAwake extends Emit {
  		public EmitAwake() {
  			setLabel(AWAKE);
  			setValueGenerator(new EventFunction() {
  				public Object apply(PropertyChangeEvent incoming) {
  					return new Integer(lastTemp.getTemp());
  				}
  			});
  		}
  }
}

