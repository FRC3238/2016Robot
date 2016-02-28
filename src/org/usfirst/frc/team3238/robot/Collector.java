package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

enum CollectorState
{
    DISABLED, COLLECTING, CENTERING, LOWERING, HOLDING, EJECTING, SHOOTING, RAISING, MANUAL
}

public class Collector
{
    CollectorState state;

    CANTalon talon;
    DigitalInput ballDetect;
    Shooter shooter;
    Joystick stick, assistStick, manualControl;
    Timer timer;

    private boolean loweringToSwitch;
    private boolean manual;

    Collector(CANTalon collectorTalon, DigitalInput ballDetect, Shooter shooter,
            Joystick stick, Joystick assistantDriver, Joystick manualControl)
    {
        try
        {
            this.talon = collectorTalon;
            this.ballDetect = ballDetect;
            this.shooter = shooter;
            this.stick = stick;
            this.assistStick = assistantDriver;
            this.manualControl = manualControl;
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
        manual = false;
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

    public void init()
    {
        state = CollectorState.DISABLED;
    }

    public void idle()
    {
        switch(state)
        {
            case CENTERING:
                shooter.leftTalon.set(0.0);
                shooter.rightTalon.set(0.0);
                setPowerOverride(Constants.Collector.centerPower);
                if(timer.get() >= 0.15)
                {
                    timer.reset();
                    timer.start();
                    loweringToSwitch = false;
                    state = CollectorState.LOWERING;
                }
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                {
                    state = CollectorState.DISABLED;
                }
                SmartDashboard.putString("state", "centering");
                break;
            case COLLECTING:
                shooter.leftTalon.set(0.0);
                shooter.rightTalon.set(0.0);
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
                manual = false;
                if(stick.getRawButton(Constants.MainDriver.autoCollect))
                {
                    state = CollectorState.COLLECTING;
                }
                if(stick.getRawButton(Constants.MainDriver.autoEject))
                {
                    state = CollectorState.EJECTING;
                }
                if(stick.getRawButton(Constants.MainDriver.shoot))
                {
                    timer.reset();
                    timer.start();
                    state = CollectorState.SHOOTING;
                }
                if(ballDetect.get()
                        && manualControl
                                .getRawButton(Constants.LaunchPad.shooterDown)
                        || ballDetect.get() && assistStick
                                .getPOV() == Constants.AssistantDriver.shooterManualDown)
                {
                    state = CollectorState.MANUAL;
                }
                // if(ballDetect.get() && assistStick
                // .getPOV() == Constants.AssistantDriver.shooterManualDown)
                // {
                // state = CollectorState.MANUAL;
                // }
                SmartDashboard.putString("state", "disabled");
                break;
            case EJECTING:
                setPowerOverride(-1.0);
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
                shooter.leftTalon.set(0.0);
                shooter.rightTalon.set(0.0);
                setPowerOverride(-Constants.Collector.centerPower);
                if(!ballDetect.get())
                    loweringToSwitch = true;
                if(ballDetect.get() && loweringToSwitch)
                    state = CollectorState.RAISING;
                // if(timer.get() >= 0.15)
                // {
                // timer.stop();
                // timer.reset();
                // state = CollectorState.HOLDING;
                // }
                SmartDashboard.putString("state", "lowering");
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                {
                    state = CollectorState.DISABLED;
                }
                break;
            case MANUAL:
                if(!ballDetect.get()
                        && manualControl
                                .getRawButton(Constants.LaunchPad.shooterDown)
                        || !ballDetect.get() && assistStick
                                .getPOV() == Constants.AssistantDriver.shooterManualDown)
                {
                    manual = true;
                    state = CollectorState.LOWERING;
                } else if(ballDetect.get()
                        && manualControl
                                .getRawButton(Constants.LaunchPad.shooterDown)
                        || ballDetect.get() && assistStick
                                .getPOV() == Constants.AssistantDriver.shooterManualDown)
                {
                    state = CollectorState.MANUAL;
                } else
                {
                    state = CollectorState.DISABLED;
                }
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                    state = CollectorState.DISABLED;
                break;
            case RAISING:
                shooter.leftTalon.set(0.0);
                shooter.rightTalon.set(0.0);
                setPowerOverride(Constants.Collector.liftPower);
                if(!ballDetect.get() && !manual)
                {
                    state = CollectorState.DISABLED;
                } else if(!ballDetect.get() && manual)
                {
                    manual = false;
                    state = CollectorState.COLLECTING;
                }
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                {
                    state = CollectorState.DISABLED;
                }
                break;
            case SHOOTING:
                setPowerOverride(Constants.Collector.defaultPower);
                if(timer.get() >= 1.0)
                {
                    timer.stop();
                    timer.reset();
                    state = CollectorState.DISABLED;
                }
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                {
                    state = CollectorState.DISABLED;
                }
                SmartDashboard.putString("state", "shooting");
                break;
            default:
                break;
        }

    }

    public void StartShooting()
    {
        state = CollectorState.SHOOTING;
    }
    
    public boolean isCollecting()
    {
        if(state == CollectorState.COLLECTING)
        {
            return true;
        }
        return false;
    }
}