package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;

enum ShooterState
{
    DISABLED, RUNNING
}

public class Shooter
{
    ShooterState shooterState;

    CANTalon leftTalon, rightTalon;
    DigitalInput leftHE, rightHE;
    Joystick mainStick, assistStick, launchPad;
    Timer timer;

    private double power;
    private double powerAdjust;
    private boolean adjusting;

    Shooter(CANTalon shooterLeftTalon, CANTalon shooterRightTalon,
            DigitalInput leftHESensor, DigitalInput rightHESensor,
            Joystick mainDriverJoystick, Joystick assistantDriverJoystick,
            Joystick launchPadStick)
    {
        try
        {
            leftTalon = shooterLeftTalon;
            rightTalon = shooterRightTalon;
            rightTalon.setInverted(true);
            leftHE = leftHESensor;
            rightHE = rightHESensor;
            mainStick = mainDriverJoystick;
            assistStick = assistantDriverJoystick;
            launchPad = launchPadStick;
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

        power = Constants.Shooter.presetPowerTwo;
        powerAdjust = 0.0;
        adjusting = false;
        // shooterState
    }

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
            timer = new Timer();
            timer.reset();
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }

        power = Constants.Shooter.presetPowerThree;
        powerAdjust = 0.0;
        adjusting = false;
        shooterState = ShooterState.DISABLED;
    }

    private void setPower(double shooterPower)
    {
        if(launchPad.getRawButton(Constants.LaunchPad.shooterUp))
        {
            leftTalon.set(power+powerAdjust);
            rightTalon.set(power+powerAdjust);
        } else if(launchPad.getRawButton(Constants.LaunchPad.shooterDown))
        {
            leftTalon.set(-power+powerAdjust);
            rightTalon.set(-power+powerAdjust);
        } else
        {
            leftTalon.set(shooterPower+powerAdjust);
            rightTalon.set(shooterPower+powerAdjust);
        }
    }

    private void controlPower()
    {
        switchWithParam(Constants.AssistantDriver.manualShooterPreset1, Constants.Shooter.presetPowerOne);
        switchWithParam(Constants.AssistantDriver.manualShooterPreset2, Constants.Shooter.presetPowerTwo);
        switchWithParam(Constants.AssistantDriver.manualShooterPreset3, Constants.Shooter.presetPowerThree);
        switchWithParam(Constants.AssistantDriver.manualShooterPreset4, Constants.Shooter.presetPowerFour);
        adjustWithParam(assistStick.getRawButton(Constants.AssistantDriver.manualShooterAdd)
                && !adjusting, 0.02);
        adjustWithParam(assistStick.getRawButton(Constants.AssistantDriver.manualShooterSubtract)
                && !adjusting, -0.02);
        
        adjusting = assistStick.getRawButton(Constants.AssistantDriver.manualShooterAdd) &&
        		assistStick.getRawButton(Constants.AssistantDriver.manualShooterSubtract);
        
    }

    
    private void switchWithParam(int button, double preset) {
    	if(assistStick.getRawButton(button)) {
    		powerAdjust = 0;
    		power = preset;
    	}
    }
    private void adjustWithParam(boolean isTrue, double inc) {
    	adjusting = true;
    	if(isTrue) {
    		powerAdjust+=inc;
    	}
    }
    private void resetAdjust(boolean b) {
    	adjusting = !b;
    }
    
    public void run()
    {
        switch(shooterState)
        {
            case DISABLED:
                setPower(0.0);
                controlPower();
                if(assistStick
                        .getRawButton(Constants.AssistantDriver.prepShootOn)) {
                    shooterState = ShooterState.RUNNING;
                    powerAdjust = 0;
                }
                break;
            case RUNNING:
                setPower(power);
                controlPower();
                if(assistStick
                        .getRawButton(Constants.AssistantDriver.prepShootOff)) {
                    shooterState = ShooterState.DISABLED;
                    powerAdjust = 0;
                }
                break;
            default:
                break;
        }
    }
}
