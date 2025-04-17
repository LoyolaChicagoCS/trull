package edu.luc.cs.trull.demo.office;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Control;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.EventFunction;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.Rename;
import edu.luc.cs.trull.Suspend;
import edu.luc.cs.trull.demo.AssertViolationListener;
import edu.luc.cs.trull.tl.And;
import edu.luc.cs.trull.tl.Assert;
import edu.luc.cs.trull.tl.BackTo;
import edu.luc.cs.trull.tl.Expr;
import edu.luc.cs.trull.tl.Implies;
import edu.luc.cs.trull.tl.Not;
import edu.luc.cs.trull.tl.Or;
import edu.luc.cs.trull.tl.Since;

/**
 * An office control logic that switches between occupant control
 * and economy control.
 */
public class OfficeControl extends Rename implements EventLabels {

  public OfficeControl() {
  	  setLocalEvents(new String[] { SLEEP, AWAKE });
  	  Composite top = new Composite();
    addComponent(top);
  	
    /*
     * Whenever a SWITCHOFF occurs, if there was a SWITCHON, 
     * there must have been a LIGHTON in between; 
     * and whenever a SWITCHON occurs, if there was a SWITCHOFF, 
     * there must have been a LIGHTOFF in between. 
     */
    Expr switchSafety = new And(
      new Implies(SWITCHOFF, new Not(new Since(new Not(LIGHTON), SWITCHON))),  
      new Implies(SWITCHON, new Not(new Since(new Not(LIGHTOFF), SWITCHOFF)))
    );
    Assert switchSafetyComponent = new Assert(switchSafety);
    switchSafetyComponent.addItemListener(
        new AssertViolationListener("SwitchSafety", switchSafety));

    /*
     * Whenever a LIGHTON occurs, there has not been a SLEEP since the 
     * last AWAKE or beginning of execution or a SWITCHOFF since
     * the last SWITCHON;
     * and whenever a LIGHTOFF occurs, there has not been an AWAKE since
     * the last SLEEP or a SWITCHON since the last SWITCHOFF.
     */
    Expr lightSafety = new And(
        new Implies(LIGHTON, new And(
            new BackTo(new Not(SLEEP), AWAKE),  
            new Since(new Not(SWITCHOFF), SWITCHON)
        )),
        new Implies(LIGHTOFF, new Or(
            new Since(new Not(AWAKE), SLEEP),
            new Since(new Not(SWITCHON), SWITCHOFF)
        ))
     );
    Assert lightSafetyComponent = new Assert(lightSafety);
    lightSafetyComponent.addItemListener(
       new AssertViolationListener("LightSafety", lightSafety));
    top.addComponent(lightSafetyComponent);

    top.addComponent(new EconomyControl());
    top.addComponent(
        new Suspend(SLEEP, AWAKE, 
            new Composite(switchSafetyComponent, new OccupantControl())));
    top.addComponent(
        new Loop(
          new Control(SLEEP, 
            new Composite(  
              new Emit(LIGHTOFF),
              new EmitSetTemp()
            ),
            AWAKE
          )
        )
		);
    top.addComponent(new Loop(new Control(AWAKE, new EmitSetTemp(), SLEEP)));
  }

  /**
   * A component that emits a SETTEMP event with the requested temperature. 
   */
  static class EmitSetTemp extends Emit {
  		public EmitSetTemp() {
  			setLabel(SETTEMP);
  			setValueGenerator(new EventFunction() {
  	 		  public Object apply(PropertyChangeEvent incoming) {
  		   	  return (Integer) incoming.getNewValue();
  	 		  }
  		  });
  		}
  }
}
