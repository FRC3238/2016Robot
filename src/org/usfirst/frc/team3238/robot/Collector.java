package org.usfirst.frc.team3238.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Enum list to track and perform routines based on current state of action
 */ 
enum CollectorState
{
    DISABLED, COLLECTING, CENTERING, LOWERING, HOLDING, EJECTING, SHOOTING, RAISING, MANUAL, AUTOSHOOT
}

/**
 * A class to control our ball-feed system that consists of one motor turning a dowel with a grooved rubber surface to collect
 * boulders
 * 
 * @author FRC Team 3238
 * 
 * @version 1.0
 */ 
public class Collector
{
    CollectorState state; //Defining an enum state

    CANTalon talon;
    DigitalInput ballDetect;
    Shooter shooter;
    Joystick stick, assistStick, manualControl;
    Timer timer;

    private boolean loweringToSwitch;
    private boolean manual;

    /**
     * Constructor for collector object
     * @param collectorTalon the motor controller for the collector
     * @param ballDetect the limit switch that is triggered when a boulder passes over it
     * @param shooter the shooting system on the robot
     * @param stick the joystick that changes the key states; autocollect, autoeject, shoot
     * @param assistantDriver the joystick which uses top hat functions to manually move the shooter, determines
     *      whether to switch to manual or not
     * @param manualControl same exact functionality as assistantDriver except a different joystick, uses button instead of tophat
     */ 
    Collector(CANTalon collectorTalon, DigitalInput ballDetect,
            Shooter shooter, Joystick stick, Joystick assistantDriver,
            Joystick manualControl)
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

        state = CollectorState.DISABLED; //default state
        loweringToSwitch = false; //a boolean that will later be used to perfect a PIish ball centering routine
        manual = false; //collector in automatic mode by default
    }

    /**
     * Sets the collector power from within the class
     * @param power the speed of the talon
     */ 
    private void setPowerOverride(double power)
    {
        if(stick.getPOV() == Constants.MainDriver.manualCollectIn) //If in manual mode and tophat is up then collect
            talon.set(-Constants.Collector.defaultPower);
        else if(stick.getPOV() == Constants.MainDriver.manualCollectOut)//else if manual mode and tophat is down then eject
            talon.set(Constants.Collector.defaultPower);
        else
            talon.set(power);//if auto mode then just set the original power
    }

    /**
     * Just to reset the collectorstate
     */ 
    public void init()
    {
        state = CollectorState.DISABLED;
    }

    /**
     * An extremely long function that switches the state of the collector for specialized routines
     */ 
    public void idle()
    {
        switch(state)
        {
            case AUTOSHOOT:
                timer.reset();
                timer.start();
                state = CollectorState.SHOOTING;
                break;
            case CENTERING:
                shooter.leftTalon.set(0.0);
                shooter.rightTalon.set(0.0);
                setPowerOverride(Constants.Collector.centerPower);
                if(stick.getRawButton(Constants.MainDriver.autoCollect))
                {
                    state = CollectorState.LOWERING;
                }
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
                if(stick.getRawButton(Constants.MainDriver.autoCollect)) //If driver presses collect button then turn on collector function
                {
                    state = CollectorState.COLLECTING;
                }
                if(stick.getRawButton(Constants.MainDriver.autoEject)) //if driver presses eject button turn on eject function
                {
                    state = CollectorState.EJECTING;
                }
                if(stick.getRawButton(Constants.MainDriver.shoot)) //Switch to shooting statement if trigger is pressed
                {
                    timer.reset();//reset for use in the shooting function
                    timer.start();
                    state = CollectorState.SHOOTING;
                }
                if(ballDetect.get() //If the shooter is trying to pull a ball into the collection system manually then shut off power and switch to manual collector
                        && manualControl
                                .getRawButton(Constants.LaunchPad.shooterDown)
                        || ballDetect.get()
                        && assistStick.getPOV() == Constants.AssistantDriver.shooterManualDown)
                {
                    state = CollectorState.MANUAL;
                }
                SmartDashboard.putString("state", "disabled");
                break;
            case EJECTING: //sets power to negative to pull the ball out 
                setPowerOverride(-1.0);
                if(stick.getRawButton(Constants.MainDriver.autoCollect)) 
                    state = CollectorState.COLLECTING;
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                    state = CollectorState.DISABLED;
                SmartDashboard.putString("state", "ejecting");
                break;
            case HOLDING: //Currently has the ball in position to fire and is waiting for command
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
            case LOWERING: //Ball has passed limit switch going into the bot and is going down slightly until it passes the switch again
                shooter.leftTalon.set(0.0);
                shooter.rightTalon.set(0.0);
                setPowerOverride(-Constants.Collector.centerPower);
                if(!ballDetect.get())
                    loweringToSwitch = true;
                if(stick.getRawButton(Constants.MainDriver.autoCollect))
                {
                    state = CollectorState.COLLECTING;
                }
                if(ballDetect.get() && loweringToSwitch) //if it passes the limit switch again go to it's final centering position
                    state = CollectorState.RAISING;
                SmartDashboard.putString("state", "lowering");
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                {
                    state = CollectorState.DISABLED;
                }
                break;
            case MANUAL:
                if(!ballDetect.get() //If shooter is pulling down then make it run through the lowering routine to center the new ball (see above)
                        && manualControl
                                .getRawButton(Constants.LaunchPad.shooterDown)
                        || !ballDetect.get()
                        && assistStick.getPOV() == Constants.AssistantDriver.shooterManualDown)
                {
                    manual = true;
                    state = CollectorState.LOWERING;
                } else if(ballDetect.get() //same as above except limit switch has been pressed
                        && manualControl
                                .getRawButton(Constants.LaunchPad.shooterDown)
                        || ballDetect.get()
                        && assistStick.getPOV() == Constants.AssistantDriver.shooterManualDown)
                {
                    state = CollectorState.MANUAL;
                } else
                {
                    state = CollectorState.DISABLED;
                }
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                    state = CollectorState.DISABLED;
                break;
            case RAISING: //Raises the ball after first getting it from the field, passing the limit switch, then dropping it past the switch again
                shooter.leftTalon.set(0.0);
                shooter.rightTalon.set(0.0);
                setPowerOverride(Constants.Collector.liftPower);
                if(stick.getRawButton(Constants.MainDriver.autoCollect)) //if driver is trying to recollect then retry the routine (ball probably stuck)
                {
                    state = CollectorState.LOWERING;
                }
                if(!ballDetect.get() && !manual) //if manual is false and there is no ball triggering the switch then disable the collector)
                {
                    state = CollectorState.DISABLED;
                } else if(!ballDetect.get() && manual) //if no ball hits the limit switch and manual mode is on then switch it off and start collecting
                {
                    manual = false;
                    state = CollectorState.COLLECTING;
                }
                if(stick.getRawButton(Constants.MainDriver.motorOff))
                {
                    state = CollectorState.DISABLED;
                }
                break;
            case SHOOTING: //moves the ball up to fire through the spinning shooter motors
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

    public void shoot() //easier object access
    {
        state = CollectorState.AUTOSHOOT;
    }

    public boolean isCollecting() //returns if the collector is collecting, used to determine if the shooter should keep spinning
    {
        if(state == CollectorState.COLLECTING)
        {
            return true;
        }
        return false;
    }
}
