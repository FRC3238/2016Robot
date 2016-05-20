package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Counter;

enum ShooterState
{
    DISABLED, RUNNING
}

public class Shooter
{
    ShooterState state;

    CANTalon leftTalon, rightTalon;
    DigitalInput leftHE, rightHE;
    Joystick mainStick, assistStick, launchPad;
    Timer timer;
    Counter leftCounter, rightCounter;

    private double wiggleRoom, errorLeft = 50.0, errorRight = 50.0;
    private double power;
    double rpm;
    private double powerAdjustLeft;
    private double powerAdjustRight;
    private boolean adjusting;

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
            leftCounter.setSemiPeriodMode(true);
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

    private void setPowerOverride(double shooterPower)
    {
        if(launchPad.getRawButton(Constants.LaunchPad.shooterUp) || assistStick
                .getPOV() == Constants.AssistantDriver.shooterManualUp)
        {
            leftTalon.set(0.5);
            rightTalon.set(0.5);
        } else if(launchPad.getRawButton(Constants.LaunchPad.shooterDown)
                || assistStick
                        .getPOV() == Constants.AssistantDriver.shooterManualDown)
        {
            leftTalon.set(-0.6);
            rightTalon.set(-0.6);
        } else
        {
            leftTalon.set(shooterPower + powerAdjustLeft);
            rightTalon.set(shooterPower + powerAdjustRight);
        }
        SmartDashboard.putNumber("Left Motor Speed",
                shooterPower + powerAdjustLeft);
        SmartDashboard.putNumber("Right Motor Power",
                shooterPower + powerAdjustRight);
    }

    private void controlPower()
    {
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

    public void init()
    {
        state = ShooterState.DISABLED;
    }

    public void changeState(ShooterState st)
    {
        state = st;
    }
    
    public void reset()
    {
        powerAdjustRight = 0;
        powerAdjustLeft = 0;
    }

    public void idle(boolean isCollecting)
    {
        SmartDashboard.putString("ALLAH", state.toString());
        switch(state)
        {
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
            case RUNNING:
                setPowerOverride(power);
                controlPower();
                equalizeRPM(rpm);
                if(isCollecting)
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

    public double rpm(Counter count)
    {
        try
        {
            return(60.0 / count.getPeriod());
        } catch(Exception e)
        {
            return 0.0;
        }
    }

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

    public void equalizeRPM(double rp)
    {
        double leftRPM = rpm(leftCounter);
        double rightRPM = rpm(rightCounter);
        SmartDashboard.putString("DB/String 1", "Left: " + leftRPM);
        SmartDashboard.putString("DB/String 6", "Right: " + rightRPM);
        SmartDashboard.putNumber("RightError", errorRight);
        SmartDashboard.putNumber("LeftError", errorLeft);
        SmartDashboard.putString("DB/String 0", "RPM Target: " + rp);
        if(Math.abs(leftRPM - rp) > errorLeft)
        {
            if(leftRPM > rp)
            {
                powerAdjustLeft -= 0.01;
            } else
            {
                powerAdjustLeft += 0.01;
            }
            if(errorLeft < 40)
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

    public void resetErrors()
    {
        errorRight = 20;
        errorLeft = 20;
        wiggleRoom = 50;
    }

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