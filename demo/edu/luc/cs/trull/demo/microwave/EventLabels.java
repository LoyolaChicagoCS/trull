package edu.luc.cs.trull.demo.microwave;

/**
 * Event labels for the microwave demo.
 */
public interface EventLabels {

	// UI outputs

	String OPEN = "Open";
	String CLOSE = "Close";

	String POWER = "Power";
	String TIMER = "Timer";
	String RESET = "Reset";
	String START = "Start";

	String TENMIN = "TenMin";
	String ONEMIN = "OneMin";
	String TENSEC = "TenSec";
	String ONESEC = "OneSec";

	// UI inputs

	String TIME = "Time";

	String BEEP = "Beep";

	String HIGH = "High";
	String MEDIUM = "Medium";
	String LOW = "Low";
	String OFF = "Off";

	String HEATON = "HeatOn";
	String HEATOFF = "HeatOff";
	String LIGHTON = "LightOn";
	String LIGHTOFF = "LightOff";

	// internal logic

	String HEATMODE = "HeatMode";
	String TIMERMODE = "TimerMode";

	String TICK = "Tick";

	String RUN = "Run";
	String STOP = "Stop";
	String FINISH = "Finish";
}