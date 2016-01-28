package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Timer;

public class Collector
{
    CANTalon collectorTalon;
    DigitalInput ballSwitch;
    Timer timer;

    double throttleValue, deadZone = 0.1;
    boolean bForward, bReverse, bManual, rotate = true, manual = true;
    String collectorMode;

    Collector(int collectorTalonID, int ballSwitchPort)
    {
    	rotate = true;
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
        run();
    }
    void proCollector(double throttle, boolean buttonForward, boolean buttonReverse, boolean buttonManual) {
    	if(buttonManual && rotate) {
    		manual = !manual;
    		rotate = !rotate;
    	} else if(!buttonManual && !rotate){
    		rotate = !rotate;
    	}
    	if(manual) {
    		if(buttonReverse && ballSwitch.get())
            {
                collectorTalon.set(-throttleValue);
            } else if(buttonForward)
            {
                collectorTalon.set(throttleValue);
            } else
            {
                collectorTalon.set(0.0);
            }
    	} else {
    		if(buttonForward || !ballSwitch.get())
    		{
    			collectorTalon.set(throttleValue);
    		} else if(buttonReverse) {
    			collectorTalon.set(-throttleValue);
    		} else {
    			collectorTalon.set(0.0);
    		}
    	} 	
    	
    }
    void disable() {
    	collectorTalon.set(0.0);
    }
    void run()
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
                    timer.reset();
                    timer.start();
                    collectorMode = "ejecting";
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
                break;
        }
    }
}
