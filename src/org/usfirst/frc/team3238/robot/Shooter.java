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

    private void setPowerOverride(double shooterPower)
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
        SmartDashboard.putNumber("Adjust", powerAdjust);
        SmartDashboard.putNumber("Power", power);
        SmartDashboard.putBoolean("Adjusting", adjusting);
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset1)) {
            powerAdjust = 0;
            power = Constants.Shooter.presetPowerOne;
        }
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset2)) {
            powerAdjust = 0;
            power = Constants.Shooter.presetPowerTwo;
        }
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset3)) {
            powerAdjust = 0;
            power = Constants.Shooter.presetPowerThree;
        }
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset4)) {
            power = Constants.Shooter.presetPowerFour;
            powerAdjust = 0;
        }
        if(assistStick.getRawButton(Constants.AssistantDriver.manualShooterAdd)
                && !adjusting)
        {
            
            adjusting = true;
            powerAdjust = powerAdjust + 0.02;
            
        } else if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterSubtract) && !adjusting)
        {

            adjusting = true;
            powerAdjust = powerAdjust - 0.02;
            
        } else if(!assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterAdd)
                && !assistStick.getRawButton(
                        Constants.AssistantDriver.manualShooterSubtract))
        {
            adjusting = false;
        }
        
    }

    public void idle()
    {
        switch(shooterState)
        {
            case DISABLED:
                setPowerOverride(0.0);
                controlPower();
                if(assistStick
                        .getRawButton(Constants.AssistantDriver.prepShootOn)) {
                    shooterState = ShooterState.RUNNING;
                    powerAdjust = 0;
                }
                break;
            case RUNNING:
                setPowerOverride(power);
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
