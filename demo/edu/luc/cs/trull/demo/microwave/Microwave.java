package edu.luc.cs.trull.demo.microwave;

import java.beans.PropertyChangeEvent;

import edu.luc.cs.trull.AwaitOne;
import edu.luc.cs.trull.Component;
import edu.luc.cs.trull.Composite;
import edu.luc.cs.trull.Control;
import edu.luc.cs.trull.Emit;
import edu.luc.cs.trull.EmitComponent;
import edu.luc.cs.trull.EmptyComponent;
import edu.luc.cs.trull.EventFunction;
import edu.luc.cs.trull.EventPredicate;
import edu.luc.cs.trull.If;
import edu.luc.cs.trull.Local;
import edu.luc.cs.trull.Loop;
import edu.luc.cs.trull.Sequence;
import edu.luc.cs.trull.Suspend;
import edu.luc.cs.trull.Watching;
import edu.luc.cs.trull.demo.AssertViolationListener;
import edu.luc.cs.trull.demo.Clock;
import edu.luc.cs.trull.tl.Assert;
import edu.luc.cs.trull.tl.BackTo;
import edu.luc.cs.trull.tl.Expr;
import edu.luc.cs.trull.tl.Implies;
import edu.luc.cs.trull.tl.Not;
import edu.luc.cs.trull.tl.Or;

/**
 * The control logic for a microwave oven.
 * <p>
 * <A HREF="../../../../../demo/triveni/demo/microwave/MicrowaveLogic.tvn">view Triveni source</A>
 */
public class Microwave extends Composite implements EventLabels {
  public Microwave() {
    addComponent(new LightController()); 
    addComponent(new TimerController());
    // clock ticks only while running
    addComponent(new Loop(new Control(RUN, new Clock(TICK), STOP)));
    addComponent(new SetMode());
    // disable heat while in timer mode
    addComponent(new Suspend(TIMERMODE, HEATMODE, new HeatController()));
    addComponent(new Safety());
    }
}

class LightController extends Composite implements EventLabels {
	public LightController() {
    addComponent(new Loop(new AwaitOne(HEATON, new Emit(LIGHTON))));
    // if the door is open, the light stays on when the heat goes off
    addComponent(new Loop(new Suspend(OPEN, CLOSE, new AwaitOne(HEATOFF, new Emit(LIGHTOFF)))));
    addComponent(new Loop(new AwaitOne(OPEN, new Emit(LIGHTON))));
    // we assume here that the heat is never on when the door is open (see Safety at the end)  
    addComponent(new Loop(new AwaitOne(CLOSE, new Emit(LIGHTOFF))));
		addComponent(new Loop(new Control(LIGHTON, new Light(), LIGHTOFF)));
	}
}

class SetMode extends Composite implements EventLabels {
  public SetMode() {
    addComponent(new Loop(new AwaitOne(FINISH, new Emit(HEATMODE))));
    // cannot change mode or power until program has completed all the way
    addComponent(new Suspend(RUN, FINISH, new Composite(new Component[] {
      new Loop(new AwaitOne(TIMER, new Emit(TIMERMODE))),
      new Loop(new AwaitOne(TIMER, new Emit(OFF))),
      new Loop(new AwaitOne(RESET, new Emit(HEATMODE))),
      // cannot change power while in timer mode
      new Suspend(TIMERMODE, HEATMODE, new SetPower())
    })));
  }
}

class SetPower extends EmitComponent implements EventLabels {
	private String power = HIGH;
	public void propertyChange(final PropertyChangeEvent event) {
		if (POWER.equals(event.getPropertyName())) {
			if (LOW.equals(power)) {
				scheduleEvent(power = MEDIUM);
			} else if (MEDIUM.equals(power)) {
				scheduleEvent(power = HIGH);
			} else if (HIGH.equals(power)) {
				scheduleEvent(power = LOW);
			}
		}
	}
  public void resume() {
    super.resume();
    scheduleEvent(power = HIGH);
  }
}

class HeatController extends Composite implements EventLabels {
  public HeatController() {
    addComponent(new Loop(new Control(RUN, new Heater(), STOP)));
    addComponent(new Loop(new Control(RUN, new Platter(), STOP)));
    addComponent(new Loop(new AwaitOne(RUN, new Emit(HEATON))));
    addComponent(new Loop(new AwaitOne(STOP, new Emit(HEATOFF))));
    // only while running should opening the door emit a reset
    addComponent(new Loop(new Control(RUN, new AwaitOne(OPEN, new Emit(RESET)), STOP)));
  }
}

class TimerController extends Composite implements EventLabels {
    
  private int time = 0;

  private final static String DONE = "Done";

  public TimerController() {
    addComponent(new Suspend(RUN, FINISH, new SetTime()));
    addComponent(new Loop(new AwaitOne(START, new CountDown())));
  }
   
  class CountDown extends Composite {
    public CountDown() {
      addComponent(
        new If(
          new EventPredicate() {
          public boolean apply(final PropertyChangeEvent event) {
            return time > 0;
          }
        },
        new Local(DONE, 
            new Watching(new Composite(new Component[] {
                new Emit(RUN),
                new Suspend(RESET, START, 
                    new Loop(
                        new AwaitOne(TICK, new If(new EventPredicate() {
                            public boolean apply(final PropertyChangeEvent event) {
                                return -- time > 0;
                            }},
                            new Emit(TIME, new EventFunction(){
                                public Object apply(final PropertyChangeEvent event){
                                    return new TimeData(time);
                            }}),
                            new Sequence(new Component[] {
                                new Emit(BEEP), 
                                new Emit(DONE)
                            })
                        ))
                    )
                ), 
                new Loop(
                    new AwaitOne(RESET, new Sequence(new Component[] {
                        new Emit(STOP), 
                        new Watching(
                            new AwaitOne(RESET, new Emit(DONE)),
                            START, 
                            new Emit(RUN)
                        )
                    }))
                )
            }),
            DONE, 
            new Composite(new Component[] {
                new Emit(STOP), 
                new Emit(FINISH), 
                new Emit(TIME, new EventFunction() {
                    public Object apply(final PropertyChangeEvent event) {
                        return new TimeData(time = 0);
                }})
            }))
        )
      ));
    }
  }

  class SetTime extends EmitComponent {
    public void propertyChange(final PropertyChangeEvent event) {
      String label = event.getPropertyName();
      if (RESET.equals(label)) {
        time = 0;
      } else if (TENMIN.equals(label)) {
        time = (time + 600) % 3600;
      } else if (ONEMIN.equals(label)) {
        time = time / 600 * 600 + (time + 60) % 600;          
      } else if (TENSEC.equals(label)) {
        time = time / 60 * 60 + (time + 10) % 60;          
      } else if (ONESEC.equals(label)) {
        time = time / 10 * 10 + (time + 1) % 10;              
      } else {
        return;   
      }
      scheduleEvent(TIME, new TimeData(time));
    }
  }
}

class Safety extends Composite implements EventLabels {
  public Safety() {
    // no mode change while running
    Expr modeSafety = new Implies(
        new Or(TIMERMODE, HEATMODE),
        new BackTo(new Not(RUN), FINISH)
    );
    Assert modeSafetyComponent = new Assert(modeSafety);
    modeSafetyComponent.addItemListener(
        new AssertViolationListener("ModeSafety", modeSafety));
    addComponent(modeSafetyComponent);
    // no power level change while running
    Expr levelSafety = new Implies(
        new Or(HIGH, new Or(MEDIUM, LOW)),
        new BackTo(new Not(RUN), FINISH)
    );
    Assert levelSafetyComponent = new Assert(levelSafety);
    modeSafetyComponent.addItemListener(
        new AssertViolationListener("LevelSafety", levelSafety));
    addComponent(levelSafetyComponent);
    // heat doesn't come on while running
    // violate this by opening the door and then starting the microwave
    Expr doorSafety = new Implies(HEATON, new BackTo(new Not(OPEN), CLOSE));
    Assert doorSafetyComponent = new Assert(doorSafety);
    doorSafetyComponent.addItemListener(
        new AssertViolationListener("DoorSafety", doorSafety));
    addComponent(doorSafetyComponent);
  }
}

class Heater extends EmptyComponent implements EventLabels {

	private String power = HIGH;

	public void start(PropertyChangeEvent incoming) {
		System.out.println("heat running on " + power);
	}
	public void stop() {
		System.out.println("heat stopped");
	}
	public void propertyChange(final PropertyChangeEvent event) {
		String label = event.getPropertyName();
		if (HIGH.equals(label)) {
			power = HIGH;
		} else if (MEDIUM.equals(label)) {
			power = MEDIUM;
		} else if (LOW.equals(label)) {
			power = LOW;
		} else {
			return;
		}
		System.out.println("heat level " + power);
	}
}

class Platter extends EmptyComponent {
	public void start(PropertyChangeEvent incoming) {
		System.out.println("platter running");
	}
	public void stop() {
		System.out.println("platter stopped");
	}
}

class Light extends EmptyComponent implements EventLabels {
	public void start(PropertyChangeEvent incoming) {
		System.out.println("light on");
	}
	public void stop() {
		System.out.println("light off");
	}
}