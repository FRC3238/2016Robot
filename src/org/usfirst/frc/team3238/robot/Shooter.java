package org.usfirst.frc.team3238.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Counter;

//only two enum states, intuitive
enum ShooterState
{
    DISABLED, RUNNING
}

/**
 * A class to control the management of the shooter subsystem which consists of two motors operating spinning grooved cyclinders fast
 * enough to fire a foam ball out of it
 * 
 * @author FRC Team 3238
 * 
 * @version 1.0
 */ 
public class Shooter
{
    ShooterState state;

    CANTalon leftTalon, rightTalon;
    DigitalInput leftHE, rightHE; //hall effect sensors, detect change in magnetic field (we have magnets on the shooter)
    Joystick mainStick, assistStick, launchPad;
    Timer timer;
    Counter leftCounter, rightCounter; //used for fast counting of the hall effect sensors

    private double wiggleRoom, errorLeft = 50.0, errorRight = 50.0; //wiggleroom is param for the allowed error, other params are the current error
    private double power;
    double rpm;
    private double powerAdjustLeft; //adjusting for error with left motor
    private double powerAdjustRight;
    private boolean adjusting;

    /**
     * Constructor for shooter
     * @param shooterLeftTalon the left shooter talon
     * @param shooterRightTalon the right shooter talon
     * @param mainDriverJoystick the joystick that allows shooting with collector
     * @param assistantDriverJoystick the joystick that turns on shooter motors and changes RPM 
     */ 
    Shooter(CANTalon shooterLeftTalon, CANTalon shooterRightTalon,
            Joystick mainDriverJoystick, Joystick assistantDriverJoystick,
            Joystick launchPadStick)
    {
        try
        {
            leftTalon = shooterLeftTalon;
            rightTalon = shooterRightTalon;
            rightTalon.setInverted(true);
            mainStick = mainDriverJoystick;
            assistStick = assistantDriverJoystick;
            launchPad = launchPadStick;
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }

        try
        {
            leftHE = new DigitalInput(Constants.Shooter.hallEffectLeftPort);
            rightHE = new DigitalInput(Constants.Shooter.hallEffectRightPort);
            leftCounter = new Counter(leftHE);
            rightCounter = new Counter(rightHE);
            leftCounter.setSemiPeriodMode(true); //counts the period of repeated signals (HE will return true, false, true, false constantly)
            rightCounter.setSemiPeriodMode(true);
            timer = new Timer();
            timer.reset();
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }

        power = 0.4;
        rpm = Constants.Shooter.presetPowerTwo;
        powerAdjustLeft = 0.0;
        powerAdjustRight = 0.0;
        adjusting = false;
        state = ShooterState.DISABLED;
        wiggleRoom = 50;
    }

    /**
     * Setting power within the class of the shooter motors
     * 
     * @param shooterPower the speed of the shooter motors
     */ 
    private void setPowerOverride(double shooterPower)
    {
        if(launchPad.getRawButton(Constants.LaunchPad.shooterUp) || assistStick
                .getPOV() == Constants.AssistantDriver.shooterManualUp) //if driver uses tophat up then turn on motors
        {
            leftTalon.set(0.5);
            rightTalon.set(0.5);
        } else if(launchPad.getRawButton(Constants.LaunchPad.shooterDown) //if driver uses tophat down then pull ball from shooter into collection system
                || assistStick
                        .getPOV() == Constants.AssistantDriver.shooterManualDown)
        {
            leftTalon.set(-0.6);
            rightTalon.set(-0.6);
        } else
        { //if neither than just set it as shooterpower and adjust the value based on RPM
            leftTalon.set(shooterPower + powerAdjustLeft);
            rightTalon.set(shooterPower + powerAdjustRight);
        }
        SmartDashboard.putNumber("Left Motor Speed",
                shooterPower + powerAdjustLeft);
        SmartDashboard.putNumber("Right Motor Power",
                shooterPower + powerAdjustRight);
    }

    /**
     * Changes and checks current RPM settings.
     */ 
    private void controlPower()
    {
        //same as all the following checks, if a certain button is pressed then set the rpm to a constant and reset allowed error
        if(assistStick.getRawButton(Constants.AssistantDriver.prepShootOn)) 
        {
            rpm = Constants.Shooter.presetPowerTwo;
            wiggleRoom = 50;
        }
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset1))
        {
            rpm = Constants.Shooter.presetPowerOne;
            wiggleRoom = 50;
        }
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset2))
        {
            rpm = Constants.Shooter.presetPowerTwo;
            resetErrors();
        }
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset3))
        {
            rpm = Constants.Shooter.presetPowerThree;
            resetErrors();
        }
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset4))
        {
            rpm = Constants.Shooter.presetPowerFour;
            resetErrors();
        }
        powerAdjust();

    }
    /**
     * Adjusts the power if the driver wants to add or subtract shooter power manually
     */ 
    private void powerAdjust()
    {
        if(assistStick.getRawButton(Constants.AssistantDriver.manualShooterAdd)
                && !adjusting)
        {

            adjusting = true;
            powerAdjustLeft = powerAdjustLeft + 0.02;
            powerAdjustRight = powerAdjustRight + 0.02;
            resetErrors();
        } else if(assistStick.getRawButton(
                Constants.AssistantDriver.manualShooterSubtract) && !adjusting)
        {

            adjusting = true;
            powerAdjustLeft = powerAdjustLeft - 0.02;
            powerAdjustRight = powerAdjustRight - 0.02;
            resetErrors();

        } else if(!assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterAdd)
                && !assistStick.getRawButton(
                        Constants.AssistantDriver.manualShooterSubtract))
        {
            adjusting = false;
        }
    }
    /**
     * easy way to disable the shooterstate from another class
     */ 
    public void init()
    {
        state = ShooterState.DISABLED;
    }
    /**
     * mutator method that provides easy way to disable or start a shooter
     * 
     * @param st the state that the shooter will set to
     */ 
    public void changeState(ShooterState st)
    {
        state = st;
    }
    /**
     * resets the 'power adjust' AKA the manually added change in motor speeds
     */
    public void reset()
    {
        powerAdjustRight = 0;
        powerAdjustLeft = 0;
    }
    /**
     * the iterative function for the shooter, a long enum case statement mandating how it should operate
     * 
     * @param isCollecting if the collector is collecting is true
     */ 
    public void idle(boolean isCollecting)
    {
        SmartDashboard.putString("ALLAH", state.toString()); //u Akbar
        switch(state)
        {
            //don't turn on the shooter until the driver wants to
            case DISABLED:
                setPowerOverride(0.0);
                controlPower();
                resetErrors();
                if(assistStick
                        .getRawButton(Constants.AssistantDriver.prepShootOn)
                        || assistStick.getRawButton(
                                Constants.AssistantDriver.manualShooterPreset3))
                {
                    state = ShooterState.RUNNING;
                    powerAdjustLeft = 0;
                    powerAdjustRight = 0;
                }
                break;
                //aligns with the RPM and allowed errors to make the shooter accurate
            case RUNNING:
                setPowerOverride(power);
                controlPower();
                equalizeRPM(rpm);
                if(isCollecting) //don't shoot if the collector is collecting because it would just fire the ball immediately
                {
                    state = ShooterState.DISABLED;
                    powerAdjustLeft = 0;
                    powerAdjustRight = 0;
                }
                if(assistStick
                        .getRawButton(Constants.AssistantDriver.prepShootOff))
                {
                    state = ShooterState.DISABLED;
                    powerAdjustLeft = 0;
                    powerAdjustRight = 0;
                }

                break;
            default:
                break;
        }
    }
    /**
     * Calculates the current shooter rpm
     * 
     * @param count the counter of detections by the hall effect sensors
     */ 
    public double rpm(Counter count)
    {
        try
        {
            return(60.0 / count.getPeriod()); //extrapolates recent data from hall effect counter to determine RPM
        } catch(Exception e)
        {
            return 0.0;
        }
    }
    /**
     * Returns if the desired RPM is within allowed error
     * 
     * @param desiredRPM the desired RPM
     * 
     * @return if the RPM is within the margin of error
     */ 
    public boolean isRPMReached(double desiredRPM)
    {
        if((Math.abs(rpm(leftCounter) - desiredRPM) <= Constants.Shooter.error)
                && (Math.abs(rpm(rightCounter)
                        - desiredRPM) <= Constants.Shooter.error))
        {
            return true;
        } else
        {
            return false;
        }
    }
    /**
     * changes the shooter speeds based on the difference between current and desired rpm and error allowance
     * 
     * @param rp the desired rpm
     */ 
    public void equalizeRPM(double rp)
    {
        double leftRPM = rpm(leftCounter);
        double rightRPM = rpm(rightCounter);
        SmartDashboard.putString("DB/String 1", "Left: " + leftRPM);
        SmartDashboard.putString("DB/String 6", "Right: " + rightRPM);
        SmartDashboard.putNumber("RightError", errorRight);
        SmartDashboard.putNumber("LeftError", errorLeft);
        SmartDashboard.putString("DB/String 0", "RPM Target: " + rp);
        if(Math.abs(leftRPM - rp) > errorLeft) //is current error within allowed
        {
            if(leftRPM > rp)
            {
                powerAdjustLeft -= 0.01;
            } else
            {
                powerAdjustLeft += 0.01;
            }
            if(errorLeft < 40) //allow more error for the left motor at a max of 40
            {
                errorLeft += 2;
            }
        }
        if(Math.abs(rightRPM - rp) > errorRight) 
        {
            if(rightRPM > rp)
            {
                powerAdjustRight -= 0.01;
            } else
            {
                powerAdjustRight += 0.01;
            }
            if(errorRight < 40) 
            {
                errorRight += 2;
            }
        }
    }

    /**
     * Resets allowed errors
     */ 
    public void resetErrors()
    {
        errorRight = 20;
        errorLeft = 20;
        wiggleRoom = 50;
    }
    /**
     * Not used, would theoretically equalize shooter speeds but is unnecessary due to machine error
     */ 
    public void equalizeShooterSpeeds()
    {
        double leftRpm = rpm(leftCounter);
        double rightRpm = rpm(rightCounter);
        if(Math.abs(leftRpm - rightRpm) > wiggleRoom)
        {
            if(leftRpm > rightRpm)
            {
                powerAdjustRight += 0.01;
            } else
            {
                powerAdjustRight -= 0.01;
            }
            wiggleRoom += 5;
        }
    }
}
