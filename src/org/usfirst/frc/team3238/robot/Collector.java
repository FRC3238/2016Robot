package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

enum CollectorState
{
    DISABLED, COLLECTING, CENTERING, LOWERING, HOLDING, EJECTING, SHOOTING
}

public class Collector
{
    CollectorState state;

    CANTalon talon;
    DigitalInput ballDetect;
    Joystick stick;
    Timer timer;

    private boolean loweringToSwitch;

    Collector(CANTalon collectorTalon, DigitalInput ballDetect, Joystick stick)
    {
        try
        {
            this.talon = collectorTalon;
            this.ballDetect = ballDetect;
            this.stick = stick;
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }

        try
        {
            timer = new Timer();
            timer.reset();
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }

        state = CollectorState.DISABLED;
        loweringToSwitch = false;
    }

    private void setPowerOverride(double power)
    {
        if(stick.getPOV() == Constants.MainDriver.manualCollectIn)
            talon.set(-Constants.Collector.defaultPower);
        else if(stick.getPOV() == Constants.MainDriver.manualCollectOut)
            talon.set(Constants.Collector.defaultPower);
        else
            talon.set(power);
    }

    public void idle()
    {
        switch(state)
        {
            case CENTERING:
                setPowerOverride(Constants.Collector.centerPower);
                if(timer.get() >= 0.05)
                {
                    timer.reset();
                    timer.start();
                    loweringToSwitch = false;
                    state = CollectorState.LOWERING;
                }
                SmartDashboard.putString("state", "centering");
                break;
            case COLLECTING:
                setPowerOverride(Constants.Collector.defaultPower);
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                {
                    state = CollectorState.DISABLED;
                }
                if(!ballDetect.get())
                {
                    timer.reset();
                    timer.start();
                    state = CollectorState.CENTERING;
                }
                if(stick.getRawButton(Constants.MainDriver.autoEject))
                {
                    state = CollectorState.EJECTING;
                }
                SmartDashboard.putString("state", "collecting");
                break;
            case DISABLED:
                setPowerOverride(0.0);
                if(stick.getRawButton(Constants.MainDriver.autoCollect))
                {
                    state = CollectorState.COLLECTING;
                }
                if(stick.getRawButton(Constants.MainDriver.autoEject))
                {
                    state = CollectorState.EJECTING;
                }
                SmartDashboard.putString("state", "disabled");
                break;
            case EJECTING:
                setPowerOverride(-Constants.Collector.defaultPower);
                if(stick.getRawButton(Constants.MainDriver.autoCollect))
                    state = CollectorState.COLLECTING;
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                    state = CollectorState.DISABLED;
                SmartDashboard.putString("state", "ejecting");
                break;
            case HOLDING:
                setPowerOverride(0.0);
                if(stick.getRawButton(Constants.MainDriver.shoot))
                {
                    timer.reset();
                    timer.start();
                    state = CollectorState.SHOOTING;
                }
                if(stick.getRawButton(Constants.MainDriver.autoEject))
                    state = CollectorState.EJECTING;
                SmartDashboard.putString("state", "holding");
                break;
            case LOWERING:
                setPowerOverride(-Constants.Collector.centerPower);
                if(!ballDetect.get())
                    loweringToSwitch = true;
                if(ballDetect.get() && loweringToSwitch)
                    state = CollectorState.HOLDING;
                if(timer.get() >= 0.15)
                {
                    timer.stop();
                    timer.reset();
                    state = CollectorState.HOLDING;
                }
                SmartDashboard.putString("state", "lowering");
                break;
            case SHOOTING:
                setPowerOverride(Constants.Collector.defaultPower);
                if(timer.get() >= 1.0)
                {
                    timer.stop();
                    timer.reset();
                    state = CollectorState.DISABLED;
                }
                SmartDashboard.putString("state", "shooting");
                break;
            default:
                break;
        }

    }
}
