package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;

enum State
{
    WAITING, CENTERING, HOLDING, SHOOTING, DISABLED
}

enum ShooterSubState
{
    WARMINGUP, SHOOTING, DISABLED
}

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

    public CollectAndShoot(int collectorTalonID, int shooterTalonOneID,
            int shooterTalonTwoID, int collectSwitchChannel,
            int centerSwitchChannel, int holdPosSwitchChannel, int stickPort)
    {
        collectorTalon = new CANTalon(collectorTalonID);
        shooterTalonOne = new CANTalon(shooterTalonOneID);
        shooterTalonTwo = new CANTalon(shooterTalonTwoID);

        collectSwitch = new DigitalInput(collectSwitchChannel);
        centerSwitch = new DigitalInput(centerSwitchChannel);
        holdPosSwitch = new DigitalInput(holdPosSwitchChannel);

        stick = new Joystick(stickPort);

        state = State.WAITING;
        shooterSubState = ShooterSubState.DISABLED;

        timer.start();
    }

    public void idle()
    {
        switch(state)
        {
            case WAITING:
                if(stick.getRawButton(11))
                    state = State.DISABLED;
                collectorTalon.set(1.0);
                if(collectSwitch.get())
                    state = State.CENTERING;
                break;
            case CENTERING:
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
                shooterTalonOne.set(0.0);
                shooterTalonTwo.set(0.0);
                break;
            case HOLDING:
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
                    state = State.SHOOTING;
                    timer.reset();
                    timer.start();
                }
                break;
            case SHOOTING:
                if(stick.getRawButton(11))
                    state = State.DISABLED;
                switch(shooterSubState)
                {
                    case DISABLED:
                        shooterTalonOne.set(0.0);
                        shooterTalonTwo.set(0.0);
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
                        break;
                }
                break;
            default:
                break;

        }
    }
}
