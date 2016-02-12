package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;

/**
 * Enumeration to control the main switch statement
 */
enum State
{
    WAITING, CENTERING, HOLDING, SHOOTING, DISABLED
}

/**
 * Enumeration to control the shooters substate switch statement
 */
enum ShooterSubState
{
    WARMINGUP, SHOOTING, DISABLED
}

/**
 * This is the class for the combined shooter and collector.
 * 
 * @author Aaron Jenson
 */
public class CollectAndShoot
{
    State state;
    ShooterSubState shooterSubState;

    CANTalon collectorTalon;
    CANTalon shooterTalonOne;
    CANTalon shooterTalonTwo;
    DigitalInput collectSwitch;
    DigitalInput centerSwitch;
    DigitalInput holdPosSwitch;
    Joystick stick;
    Timer timer;

    public CollectAndShoot(CANTalon collectorTalon, CANTalon shooterTalonOne,
            CANTalon shooterTalonTwo, DigitalInput collectSwitch,
            DigitalInput centerSwitch, DigitalInput holdPosSwitch,
            Joystick stick)
    {
        this.collectorTalon = collectorTalon;
        this.shooterTalonOne = shooterTalonOne;
        this.shooterTalonTwo = shooterTalonTwo;

        this.collectSwitch = collectSwitch;
        this.centerSwitch = centerSwitch;
        this.holdPosSwitch = holdPosSwitch;

        this.stick = stick;

        state = State.WAITING;
        shooterSubState = ShooterSubState.DISABLED;

        timer.start();
    }
    
    public void disable() {
        shooterTalonOne.set(0.0);
        shooterTalonTwo.set(0.0);
    }

    /**
     * The main method that controls the collector and shooter. This must be
     * called in teleop periodic to function correctly.
     */
    public void idle()
    {
        switch(state)
        {
            case WAITING:
                disable();
                if(stick.getRawButton(11))
                    state = State.DISABLED;
                collectorTalon.set(1.0);
                if(collectSwitch.get())
                    state = State.CENTERING;
                break;
            case CENTERING:
                disable();
                if(stick.getRawButton(11))
                    state = State.DISABLED;
                collectorTalon.set(0.8);
                if(centerSwitch.get())
                    state = State.HOLDING;
                break;
            case DISABLED:
                if(stick.getRawButton(11))
                    state = State.WAITING;
                collectorTalon.set(0.0);
                disable();
                break;
            case HOLDING:
                disable();
                if(stick.getRawButton(11))
                    state = State.DISABLED;
                if(collectSwitch.get() && !holdPosSwitch.get()
                        && !centerSwitch.get())
                    collectorTalon.set(0.5);
                else if(centerSwitch.get() && !holdPosSwitch.get()
                        && !collectSwitch.get())
                    collectorTalon.set(-0.5);
                else if(holdPosSwitch.get())
                    collectorTalon.set(0.0);
                else if(!collectSwitch.get() && !centerSwitch.get()
                        && !holdPosSwitch.get())
                    state = State.WAITING;
                else
                    DriverStation
                            .reportError(
                                    "Your collector limit switches are seriously messed up!",
                                    false);

                if(stick.getRawButton(1))
                {
                    shooterSubState = ShooterSubState.WARMINGUP;
                    state = State.SHOOTING;
                }
                break;
            case SHOOTING:
                if(stick.getRawButton(11))
                    state = State.DISABLED;
                switch(shooterSubState)
                {
                    case DISABLED:
                        disable();
                        break;
                    case SHOOTING:
                        collectorTalon.set(0.85);
                        if(timer.get() >= 2.0)
                        {
                            timer.stop();
                            shooterSubState = ShooterSubState.DISABLED;
                            state = State.WAITING;
                        }
                        break;
                    case WARMINGUP:
                        if(!stick.getRawButton(1))
                        {
                            timer.reset();
                            timer.start();
                            shooterSubState = ShooterSubState.SHOOTING;
                        }
                        shooterTalonOne.set(0.9);
                        shooterTalonTwo.set(0.9);
                        break;
                    default:
                        disable();
                        DriverStation.reportError(
                                "Shooter is in default state!", false);
                        break;
                }
                break;
            default:
                disable();
                collectorTalon.set(0.0);
                if(stick.getRawButton(11))
                    state = State.DISABLED;
                DriverStation.reportError(
                        "Collecter/Shooter is in default state!", false);
                break;
        }
    }
}
