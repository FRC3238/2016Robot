
package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

enum AutoChoices
{
    LOWBAR, PORTCULLIS, CHEVALDEFRISE, ROCKWALL, ROUGHTERRAIN, MOAT, RAMPARTS, HIGHGOAL, DISABLED
}

enum LowBarAuto
{
    LOWERING, FORWARD, REVSHOOT
}

enum PortcullisAuto
{
    LOWERING, FORWARD, REVSHOOT
}

enum ChevalAuto
{
    FORWARD, LOWERING, ONWARD, REVSHOOT
}

enum RockAuto
{
    FORWARD, BRAKING, REVSHOOT
}

enum RoughAuto
{
    FORWARD, BRAKING, REVSHOOT
}

enum MoatAuto
{
    FORWARD, BRAKING, REVSHOOT
}

enum RampAuto
{
    FORWARD, BRAKING, REVSHOOT
}

enum HighGoalAuto
{
    LOWBAR, FORWARD, SHOOT
}

public class Autonomous
{
    Chassis chassis;
    Breacher breacher;
    Shooter shooter;
    Collector collector;
    Vision autoAim;
    Timer timer;
    Timer shootTime;
    SendableChooser chooser;
    SendableChooser botPos;

    int auto = 0;
    int lowBar1 = 1;
    int portcullis1 = 2;
    int cheval1 = 3;
    LowBarAuto lowBar;
    PortcullisAuto portcullis;
    ChevalAuto cheval;
    RockAuto rock;
    RoughAuto rough;
    MoatAuto moat;
    RampAuto ramp;
    HighGoalAuto goal;

    public Autonomous(Chassis chassis, Breacher breacher, Shooter shooter,
            Collector collector, Vision autoAim)
    {
        try
        {
            this.chassis = chassis;
            this.breacher = breacher;
            this.shooter = shooter;
            this.collector = collector;
            this.autoAim = autoAim;
            timer = new Timer();
            shootTime = new Timer();
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }

    public void init()
    {

        auto = (int) SmartDashboard.getNumber("DB/Slider 0");

        lowBar = LowBarAuto.LOWERING;
        portcullis = PortcullisAuto.LOWERING;
        cheval = ChevalAuto.FORWARD;
        rock = RockAuto.FORWARD;
        rough = RoughAuto.FORWARD;
        moat = MoatAuto.FORWARD;
        ramp = RampAuto.FORWARD;
        goal = HighGoalAuto.LOWBAR;
        shooter.rpm = Constants.Shooter.presetPowerOne;
        timer.reset();
        timer.start();
        shootTime.reset();
        shootTime.start();
    }

    public void autoRun()
    {

        SmartDashboard.putNumber("Auto", auto);
        switch(auto)
        {

            case 2:
                switch(cheval)
                {
                    case FORWARD:
                        cheval = (ChevalAuto) forward(cheval,
                                ChevalAuto.LOWERING, Constants.Auto.chevalTime,
                                Constants.Auto.chevalSpeed);
                        break;
                    case LOWERING:
                        cheval = (ChevalAuto) lowerArm(cheval,
                                ChevalAuto.ONWARD,
                                Constants.Auto.chevalArmPower,
                                Constants.Auto.chevalArmTime);
                        forward(0.05);
                        break;
                    case ONWARD:
                        breacher.autoRaise(Constants.Auto.chevalArmRaisePower);
                        // stopCBS(false, true, false);
                        SmartDashboard.putNumber("The Breacher Power",
                                breacher.getTalonSpeed());
                        cheval = (ChevalAuto) forward(cheval,
                                ChevalAuto.REVSHOOT,
                                Constants.Auto.chevalBreachTime,
                                Constants.Auto.chevalBreachSpeed);
                        SmartDashboard.putNumber("The Breacher Power 2",
                                breacher.getTalonSpeed());
                        break;
                    case REVSHOOT:
                        stopCBS(false, true, false);
                        revNoLower();
                        break;
                    default:
                        DriverStation.reportError(
                                "Auto default state! Auto is dysfunctional!",
                                true);
                        break;
                }
                break;
            case 0:
                disableAll();
                break;
            case 7:
                switch(lowBar)
                {
                    case FORWARD:
                        stopCBS(false, true, false);
                        lowBar = (LowBarAuto) forward(lowBar,
                                LowBarAuto.REVSHOOT,
                                Constants.Auto.lowBarBreachTime,
                                Constants.Auto.lowBarPower);
                        break;
                    case LOWERING:
                        lowBar = (LowBarAuto) lowerArm(lowBar,
                                LowBarAuto.FORWARD,
                                Constants.Auto.lowBarArmPower,
                                Constants.Auto.lowBarArmTime);
                        break;
                    case REVSHOOT:
                        revNoLower();
                        break;
                    default:
                        DriverStation.reportError(
                                "Auto default state! Auto is disfunctional!",
                                true);
                        break;
                }
                break;
            case 1:
                switch(portcullis)
                {
                    case FORWARD:
                        breacher.moveArm(0.0);
                        portcullis = (PortcullisAuto) forward(portcullis,
                                PortcullisAuto.REVSHOOT,
                                Constants.Auto.portcullisBreachTime,
                                Constants.Auto.portcullisSpeed);
                        break;
                    case LOWERING:
                        portcullis = (PortcullisAuto) lowerArm(portcullis,
                                PortcullisAuto.FORWARD,
                                Constants.Auto.portcullisArmPower,
                                Constants.Auto.portcullisArmTime);
                        break;
                    case REVSHOOT:
                        revNoLower();
                        break;
                    default:
                        DriverStation.reportError(
                                "Auto default state! Auto is disfunctional!",
                                true);
                        break;
                }
                break;
            case 5:
                switch(rock)
                {
                    case FORWARD:
                        rock = (RockAuto) forward(rock, RockAuto.BRAKING,
                                Constants.Auto.rockBreachTime,
                                Constants.Auto.rockSpeed);
                        break;
                    case BRAKING:
                        rock = (RockAuto) forward(rock, RockAuto.REVSHOOT,
                                Constants.Auto.brakeTime,
                                Constants.Auto.brakeSpeed);
                        break;
                    case REVSHOOT:
                        rev();
                        break;
                    default:
                        DriverStation.reportError(
                                "Auto default state! Auto is disfunctional!",
                                true);
                        break;
                }
                break;
            case 6:
                switch(rough)
                {
                    case FORWARD:
                        rough = (RoughAuto) forward(rough, RoughAuto.BRAKING,
                                Constants.Auto.roughBreachTime,
                                Constants.Auto.roughSpeed);
                        break;
                    case BRAKING:
                        rough = (RoughAuto) forward(rough, RoughAuto.REVSHOOT,
                                Constants.Auto.brakeTime,
                                Constants.Auto.brakeSpeed);
                        break;
                    case REVSHOOT:
                        revNoLower();
                        break;
                    default:
                        DriverStation.reportError(
                                "Auto default state! Auto is disfunctional!",
                                true);
                        break;
                }
                break;
            case 3:
                switch(moat)
                {
                    case BRAKING:
                        moat = (MoatAuto) forward(moat, MoatAuto.REVSHOOT,
                                Constants.Auto.brakeTime,
                                Constants.Auto.brakeSpeed);
                        break;
                    case FORWARD:
                        moat = (MoatAuto) forward(moat, MoatAuto.REVSHOOT,
                                Constants.Auto.moatBreachTime,
                                Constants.Auto.moatSpeed);

                        break;
                    case REVSHOOT:
                        revNoLower();
                        break;
                    default:
                        break;
                }
                break;
            case 4:
                switch(ramp)
                {
                    case BRAKING:
                        ramp = (RampAuto) forward(ramp, RampAuto.REVSHOOT,
                                Constants.Auto.brakeTime,
                                Constants.Auto.brakeSpeed);
                        break;
                    case FORWARD:
                        ramp = (RampAuto) forward(ramp, RampAuto.REVSHOOT,
                                Constants.Auto.rampBreachTime,
                                Constants.Auto.rampSpeed);
                        break;
                    case REVSHOOT:
                        rev();
                        break;
                    default:
                        break;
                }
                break;
            case 8:
                switch(goal)
                {

                    case FORWARD:
                        goal = (HighGoalAuto) forwardTank(goal,
                                HighGoalAuto.SHOOT,
                                Constants.Auto.goalForwardTime,
                                Constants.Auto.goalForwardPowerY,
                                Constants.Auto.goalForwardPowerTwist);
                        break;
                    case LOWBAR:
                        switch(lowBar)
                        {
                            case FORWARD:
                                stopCBS(false, true, false);
                                goal = (HighGoalAuto) forward(goal,
                                        HighGoalAuto.FORWARD,
                                        Constants.Auto.lowBarBreachTime,
                                        Constants.Auto.lowBarPower);
                                break;
                            case LOWERING:
                                lowBar = (LowBarAuto) lowerArm(lowBar,
                                        LowBarAuto.FORWARD,
                                        Constants.Auto.lowBarArmPower,
                                        Constants.Auto.lowBarArmTime);
                                break;
                            case REVSHOOT:
                                revNoLower();
                                break;
                            default:
                                DriverStation
                                        .reportError(
                                                "Auto default state! Auto is disfunctional!",
                                                true);
                                break;
                        }

                        if(lowBar == LowBarAuto.REVSHOOT)
                        {
                            goal = HighGoalAuto.FORWARD;
                        }
                        break;
                    case SHOOT:
                        autoAim.idle();
                        break;
                    default:
                        break;
                }
                break;
            default:
                DriverStation.reportError(
                        "Auto default state! Auto is disfunctional!", true);
                break;
        }
    }

    private void disableAll()
    {
        stopShooting();
        chassis.disable();
        breacher.moveArm(0.0);
    }

    private void rev()
    {
        chassis.disable();
        SmartDashboard.putNumber("SHT", shootTime.get());
        SmartDashboard.putNumber("SHTA", Constants.Auto.shootRevTime);
        collector.state = CollectorState.DISABLED;
        shooter.idle(false);
        if(shootTime.get() > Constants.Auto.shootRevTime)
        {
            breacher.moveArm(Constants.Auto.armLowerPower);
            shooter.state = ShooterState.RUNNING;
        }
    }

    private void revNoLower()
    {
        chassis.disable();
        SmartDashboard.putNumber("SHT", shootTime.get());
        SmartDashboard.putNumber("SHTA", Constants.Auto.shootRevTime);
        collector.state = CollectorState.DISABLED;
        shooter.idle(false);
        if(shootTime.get() > Constants.Auto.shootRevTime)
        {
            shooter.state = ShooterState.RUNNING;
        }
    }

    private void startTimer(Object st, Object nst)
    {
        st = nst;
    }

    private Object lowerArm(Object stateControl, Object nextState,
            double power, double time)
    {
        chassis.disable();
        stopShooting();
        breacher.moveArm(power);
        SmartDashboard.putNumber("TIME", timer.get());
        SmartDashboard.putNumber("TIMER", time);
        SmartDashboard.putString("TIME1", nextState.toString());
        if(timer.get() >= time)
        {
            startTimer();
            return nextState;
        }
        return stateControl;
    }

    private void forward(double power)
    {
        chassis.setPower(power);
    }

    private void stopCBS(boolean chassi, boolean ar, boolean shoote)
    {
        if(chassi)
        {
            chassis.setPower(0);
        }
        if(ar)
        {
            breacher.moveArm(0);
        }
        if(shoote)
        {
            stopShooting();
        }

    }

    private Object forward(Object stateControl, Object nextState, double time,
            double power)
    {
        chassis.setPower(power);
        stopShooting();

        if(timer.get() >= time)
        {
            startTimer();
            return nextState;
        }
        return stateControl;
    }

    private Object forwardTank(Object stateControl, Object nextState,
            double time, double powerY, double powerTwist)
    {
        chassis.arcadeDriveAuto(powerY, powerTwist);
        stopShooting();

        if(timer.get() >= time)
        {
            startTimer();
            return nextState;
        }
        return stateControl;
    }

    private void startTimer()
    {
        timer.reset();
        timer.start();
    }

    private void stopShooting()
    {
        shooter.state = ShooterState.DISABLED;
        collector.state = CollectorState.DISABLED;
    }
}
