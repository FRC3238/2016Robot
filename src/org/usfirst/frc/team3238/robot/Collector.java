package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Collector
{
    CANTalon collectorTalon;
    DigitalInput ballSwitch;
    Timer timer;

    double throttleValue;
    boolean bForward;
    boolean bReverse;
    boolean bManual;
    String collectorMode;

    Collector(int collectorTalonID, int ballSwitchPort)
    {
        collectorTalon = new CANTalon(collectorTalonID);
        ballSwitch = new DigitalInput(ballSwitchPort);
        timer = new Timer();
        timer.start();
        collectorMode = "manual";
    }

    void setJoystickData(double throttle, boolean buttonForward,
            boolean buttonReverse, boolean buttonManualMode)
    {
        throttleValue = throttle;
        bForward = buttonForward;
        bReverse = buttonReverse;
        bManual = buttonManualMode;
    }

    void idle()
    {
        switch(collectorMode)
        {
            case "collecting":
                collectorTalon.set(throttleValue);
                if(bManual)
                {
                    collectorMode = "manual";
                }
                if(ballSwitch.get())
                {
                    collectorMode = "holding";
                }
                break;

            case "holding":
                collectorTalon.set(0.0);
                if(bManual)
                {
                    collectorMode = "manual";
                }
                if(bReverse)
                {
                    collectorMode = "ejecting";
                    timer.reset();
                    timer.start();
                }
                break;

            case "ejecting":
                collectorTalon.set(-throttleValue);
                if(bManual)
                {
                    collectorMode = "manual";
                }
                if(timer.get() > 4.0)
                {
                    collectorMode = "collecting";
                }
                break;

            case "manual":
                if(bForward)
                {
                    collectorTalon.set(throttleValue);
                } else if(bReverse)
                {
                    collectorTalon.set(-throttleValue);
                } else
                {
                    collectorTalon.set(0.0);
                }
                if(bManual)
                {
                    collectorMode = "collecting";
                }
        }
    }
}
