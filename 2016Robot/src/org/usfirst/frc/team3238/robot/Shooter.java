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
    public double itera = 0.8;
    private double wiggleRoom, errorLeft = 50.0, errorRight = 50.0;
    private double power;
    private double rpm;
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
        
        // try
        // {
        leftHE = new DigitalInput(Constants.Shooter.hallEffectLeftPort);
        rightHE = new DigitalInput(Constants.Shooter.hallEffectRightPort);
        leftCounter = new Counter(leftHE);
        rightCounter = new Counter(rightHE);
        leftCounter.setSemiPeriodMode(true);
        rightCounter.setSemiPeriodMode(true);
        timer = new Timer();
        timer.reset();
        // } catch(Exception e)
        // {
        // DriverStation.reportError(e.getMessage(), true);
        // }

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
        if(launchPad.getRawButton(Constants.LaunchPad.shooterUp))
        {
            leftTalon.set(power + powerAdjustLeft);
            rightTalon.set(power + powerAdjustRight);
        } else if(launchPad.getRawButton(Constants.LaunchPad.shooterDown))
        {
            leftTalon.set(-power + powerAdjustLeft);
            rightTalon.set(-power + powerAdjustRight);
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
        SmartDashboard.putNumber("Adjust", powerAdjustRight);
        SmartDashboard.putNumber("Power", power);
        SmartDashboard.putBoolean("Adjusting", adjusting);
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset1))
        {
            powerAdjustLeft = 0;
            powerAdjustRight = 0;
            rpm = Constants.Shooter.presetPowerOne;
            wiggleRoom = 50;
        }
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset2))
        {
            powerAdjustLeft = 0;
            powerAdjustRight = 0;
            rpm = Constants.Shooter.presetPowerTwo;
            resetErrors();
        }
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset3))
        {
            powerAdjustLeft = 0;
            powerAdjustRight = 0;
            rpm = Constants.Shooter.presetPowerThree;
            resetErrors();
        }
        if(assistStick
                .getRawButton(Constants.AssistantDriver.manualShooterPreset4))
        {
            rpm = Constants.Shooter.presetPowerFour;
            powerAdjustLeft = 0;
            powerAdjustRight = 0;
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
        timer.start();
    }
    public void changer() {
        SmartDashboard.putNumber("el Time", timer.get());
        SmartDashboard.putBoolean("MSA", assistStick.getRawButton(Constants.AssistantDriver.manualShooterAdd));
        if(assistStick.getRawButton(Constants.AssistantDriver.manualShooterAdd) && timer.get() > 0.2) {
            itera += 0.1;
           
            timer.reset();
        }
        if(assistStick.getRawButton(Constants.AssistantDriver.manualShooterSubtract)&&timer.get() > 0.2) {
            itera-=0.1;
            timer.reset();
        }
    }
    public void idle()
    {
        switch(state)
        {
            case DISABLED:
                setPowerOverride(0.0);
                controlPower();
                resetErrors();
                
                if(assistStick.getRawButton(Constants.AssistantDriver.manualShooterAdd) && timer.get() > 0.2) {
                    itera += 0.1;
                   
                    timer.reset();
                }
                if(assistStick.getRawButton(Constants.AssistantDriver.manualShooterSubtract)&&timer.get() > 0.2) {
                    itera-=0.1;
                    timer.reset();
                }
                
                // equalizeShooterSpeeds();
                if(assistStick
                        .getRawButton(Constants.AssistantDriver.prepShootOn))
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

    public void equalizeRPM(double rp)
    {
        double leftRPM = rpm(leftCounter);
        double rightRPM = rpm(rightCounter);
        SmartDashboard.putNumber("leftRPM", leftRPM);
        SmartDashboard.putNumber("RightRPM", rightRPM);
        SmartDashboard.putNumber("RightError", errorRight);
        SmartDashboard.putNumber("LeftError", errorLeft);
        SmartDashboard.putNumber("RPM Target", rp);
        if(Math.abs(leftRPM - rp) > errorLeft)
        {
            if(leftRPM > rp)
            {
                powerAdjustLeft -= 0.01;
            } else
            {
                powerAdjustLeft += 0.01;
            }
            if(errorLeft < 100)
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
            if(errorRight < 100)
            {
                errorRight += 2;
            }
        }
    }

    public void resetErrors()
    {
        errorRight = 50;
        errorLeft = 50;
        wiggleRoom = 50;
    }

    public void equalizeShooterSpeeds()
    {
        double leftRpm = rpm(leftCounter);
        double rightRpm = rpm(rightCounter);
        // SmartDashboard.putNumber("leftRPM", leftRpm);
        // SmartDashboard.putNumber("RightRPM", rightRpm);
        // SmartDashboard.putNumber("Wiggle Room", wiggleRoom);
        // SmartDashboard.putNumber("Right", power+powerAdjustRight);
        // SmartDashboard.putNumber("left", power+powerAdjustLeft);
        if(Math.abs(leftRpm - rightRpm) > wiggleRoom)
        {
            if(leftRpm > rightRpm)
            {
                powerAdjustRight += 0.01;
                // powerAdjustLeft -= 0.01;
            } else
            {
                powerAdjustRight -= 0.01;
                // powerAdjustLeft += 0.01;
            }
            wiggleRoom += 5;
        }
    }
}