package edu.luc.cs.trull.demo.wristwatch;

/**
 * Event labels for the wristwatch.
 */
public interface EventLabels {

    // incoming events

    String START      = "Start";
    String RESET      = "Reset";
    String TICK       = "Tick";
    String MODE       = "Mode";
    String LIGHT      = "Light";

    // outgoing events

    String NOW        = "Now";
    String LIGHT_ON   = "LightOn";
    String LIGHT_OFF  = "LightOff";
    String TIME       = "Time";
    String DATE       = "Date";
    String STOP       = "Stop";
    String LAP        = "Lap";
    String HOUR       = "Hour";
    String MIN        = "Min";
    String DAY        = "Day";
    String MONTH      = "Month";
    
    // internal events
    
    String UPDATE     = "Update";
}