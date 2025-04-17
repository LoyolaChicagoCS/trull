package edu.luc.cs.trull.demo.office;

/**
 * Event labels for the office demo.
 * <p>
 * In general, it is good practice to define named constants 
 * for all nonlocal event labels.
 * </p>
 */

public interface EventLabels {
    
    public final String TEMP         = "Temp";
    public final String SETTEMP      = "SetTemp";
    public final String REQUESTTEMP  = "RequestTemp";
    public final String ECONOMYMODE  = "EconomyMode";
    public final String OCCUPANTMODE = "OccupantMode";
    public final String SLEEP        = "Sleep";
    public final String AWAKE        = "Awake";

    public final String HEATON    = "HeatOn";
    public final String HEATOFF   = "HeatOff";

    public final String SWITCHON  = "SwitchOn";
    public final String SWITCHOFF = "SwitchOff";

    public final String LIGHTON   = "LightOn";
    public final String LIGHTOFF  = "LightOff";

    public final String DOOROPEN  = "DoorOpen";
    public final String DOORCLOSE = "DoorClose";
}
