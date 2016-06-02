package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

enum LowBarGoal
{
    LOWBAR, FORWARD, SHOOT
}

enum ChevalGoal
{
    CHEVAL, FORWARD, SHOOT
}

enum PortGoal
{
    PORTCULLIS, FORWARD, SHOOT
}

enum RockGoal
{
    ROCKWALL, FORWARD, SHOOT
}

enum RoughGoal
{
    ROUGH, FORWARD, SHOOT
}

enum RampGoal
{
    RAMPARTS, FORWARD, SHOOT
}

enum MoatGoal
{
    MOAT, FORWARD, SHOOT
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

    int auto = 0;
    int pos = 0;

    LowBarAuto lowBar;
    PortcullisAuto portcullis;
    ChevalAuto cheval;
    RockAuto rock;
    RoughAuto rough;
    MoatAuto moat;
    RampAuto ramp;
    LowBarGoal goal;
    ChevalGoal chevalGoal;
    PortGoal portGoal;
    RockGoal rockGoal;
    RoughGoal roughGoal;
    MoatGoal moatGoal;
    RampGoal rampGoal;

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
        pos = (int) SmartDashboard.getNumber("DB/Slider 1");

        lowBar = LowBarAuto.LOWERING;
        portcullis = PortcullisAuto.LOWERING;
        cheval = ChevalAuto.FORWARD;
        rock = RockAuto.FORWARD;
        rough = RoughAuto.FORWARD;
        moat = MoatAuto.FORWARD;
        ramp = RampAuto.FORWARD;
        goal = LowBarGoal.LOWBAR;
        chevalGoal = ChevalGoal.CHEVAL;
        portGoal = PortGoal.PORTCULLIS;
        rockGoal = RockGoal.ROCKWALL;
        roughGoal = RoughGoal.ROUGH;
        moatGoal = MoatGoal.MOAT;
        rampGoal = RampGoal.RAMPARTS;
        shooter.rpm = Constants.Shooter.presetPowerFour;
        shooter.changeState(ShooterState.DISABLED);
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
                switch(portGoal)
                {
                    case FORWARD:
                        switch(pos)
                        {
                            case 0:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 1:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 2:
                                stopCBS(false, true, false);
                                portGoal = (PortGoal) forwardTank(
                                        portGoal,
                                        PortGoal.SHOOT,
                                        Constants.Auto.goalPortTwoForwardTime,
                                        Constants.Auto.goalPortTwoForwardPowerY,
                                        Constants.Auto.goalPortTwoForwardPowerTwist);
                                break;
                            case 3:
                                stopCBS(false, true, false);
                                portGoal = (PortGoal) forwardTank(
                                        portGoal,
                                        PortGoal.SHOOT,
                                        Constants.Auto.goalPortThreeForwardTime,
                                        Constants.Auto.goalPortThreeForwardPowerY,
                                        Constants.Auto.goalPortThreeForwardPowerTwist);
                                break;
                            case 4:
                                stopCBS(false, true, false);
                                portGoal = (PortGoal) forwardTank(
                                        portGoal,
                                        PortGoal.SHOOT,
                                        Constants.Auto.goalPortFourForwardTime,
                                        Constants.Auto.goalPortFourForwardPowerY,
                                        Constants.Auto.goalPortFourForwardPowerTwist);
                                break;
                            case 5:
                                stopCBS(false, true, false);
                                portGoal = (PortGoal) forwardTank(
                                        portGoal,
                                        PortGoal.SHOOT,
                                        Constants.Auto.goalPortFiveForwardTime,
                                        Constants.Auto.goalPortFiveForwardPowerY,
                                        Constants.Auto.goalPortFiveForwardPowerTwist);
                                break;
                        }

                        break;
                    case PORTCULLIS:
                        switch(portcullis)
                        {
                            case FORWARD:
                                breacher.moveArm(0.0);
                                portcullis = (PortcullisAuto) forward(
                                        portcullis, PortcullisAuto.REVSHOOT,
                                        Constants.Auto.portcullisBreachTime,
                                        Constants.Auto.portcullisSpeed);
                                break;
                            case LOWERING:
                                portcullis = (PortcullisAuto) lowerArm(
                                        portcullis, PortcullisAuto.FORWARD,
                                        Constants.Auto.portcullisArmPower,
                                        Constants.Auto.portcullisArmTime);
                                break;
                            case REVSHOOT:
                                portGoal = PortGoal.FORWARD;
                                break;
                            default:
                                DriverStation
                                        .reportError(
                                                "Auto default state! Auto is disfunctional!",
                                                true);
                                break;
                        }
                        break;
                    case SHOOT:
                        if(timer.get() > 1.0)
                        {
                            autoAim.idle(pos);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 5:
                switch(rockGoal)
                {
                    case FORWARD:
                        switch(pos)
                        {
                            case 0:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 1:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 2:
                                stopCBS(false, true, false);
                                rockGoal = (RockGoal) forwardTank(
                                        rockGoal,
                                        RockGoal.SHOOT,
                                        Constants.Auto.goalRockTwoForwardTime,
                                        Constants.Auto.goalRockTwoForwardPowerY,
                                        Constants.Auto.goalRockTwoForwardPowerTwist);
                                break;
                            case 3:
                                stopCBS(false, true, false);
                                rockGoal = (RockGoal) forwardTank(
                                        rockGoal,
                                        RockGoal.SHOOT,
                                        Constants.Auto.goalRockThreeForwardTime,
                                        Constants.Auto.goalRockThreeForwardPowerY,
                                        Constants.Auto.goalRockThreeForwardPowerTwist);
                                break;
                            case 4:
                                stopCBS(false, true, false);
                                rockGoal = (RockGoal) forwardTank(
                                        rockGoal,
                                        RockGoal.SHOOT,
                                        Constants.Auto.goalRockFourForwardTime,
                                        Constants.Auto.goalRockFourForwardPowerY,
                                        Constants.Auto.goalRockFourForwardPowerTwist);
                                break;
                            case 5:
                                stopCBS(false, true, false);
                                rockGoal = (RockGoal) forwardTank(
                                        rockGoal,
                                        RockGoal.SHOOT,
                                        Constants.Auto.goalRockFiveForwardTime,
                                        Constants.Auto.goalRockFiveForwardPowerY,
                                        Constants.Auto.goalRockFiveForwardPowerTwist);
                                break;
                        }
                        break;
                    case ROCKWALL:
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
                                rockGoal = RockGoal.FORWARD;
                                break;
                            default:
                                DriverStation.reportError(
                                        "Auto default state! Auto is disfunctional!",
                                        true);
                                break;
                        }
                        break;
                    case SHOOT:
                        if(timer.get() > 1.0)
                        {
                            autoAim.idle(pos);
                        }
                        break;
                    default:
                        break;
                    
                }
                break;
            case 6:
                switch(roughGoal)
                {
                    case FORWARD:
                        switch(pos)
                        {
                            case 0:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 1:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 2:
                                stopCBS(false, true, false);
                                roughGoal = (RoughGoal) forwardTank(
                                        roughGoal,
                                        RoughGoal.SHOOT,
                                        Constants.Auto.goalRoughTwoForwardTime,
                                        Constants.Auto.goalRoughTwoForwardPowerY,
                                        Constants.Auto.goalRoughTwoForwardPowerTwist);
                                break;
                            case 3:
                                stopCBS(false, true, false);
                                roughGoal = (RoughGoal) forwardTank(
                                        roughGoal,
                                        RoughGoal.SHOOT,
                                        Constants.Auto.goalRoughThreeForwardTime,
                                        Constants.Auto.goalRoughThreeForwardPowerY,
                                        Constants.Auto.goalRoughThreeForwardPowerTwist);
                                break;
                            case 4:
                                stopCBS(false, true, false);
                                roughGoal = (RoughGoal) forwardTank(
                                        roughGoal,
                                        RoughGoal.SHOOT,
                                        Constants.Auto.goalRoughFourForwardTime,
                                        Constants.Auto.goalRoughFourForwardPowerY,
                                        Constants.Auto.goalRoughFourForwardPowerTwist);
                                break;
                            case 5:
                                stopCBS(false, true, false);
                                roughGoal = (RoughGoal) forwardTank(
                                        roughGoal,
                                        RoughGoal.SHOOT,
                                        Constants.Auto.goalRoughFiveForwardTime,
                                        Constants.Auto.goalRoughFiveForwardPowerY,
                                        Constants.Auto.goalRoughFiveForwardPowerTwist);
                                break;
                        }
                        break;
                    case ROUGH:
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
                                roughGoal = RoughGoal.FORWARD;
                                break;
                            default:
                                DriverStation.reportError(
                                        "Auto default state! Auto is disfunctional!",
                                        true);
                                break;
                        }
                        break;
                    case SHOOT:
                        if(timer.get() > 1.0)
                        {
                            autoAim.idle(pos);
                        }
                        break;
                    default:
                        break;
                    
                }
                break;
            case 3:
                switch(moatGoal)
                {
                    case FORWARD:
                        switch(pos)
                        {
                            case 0:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 1:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 2:
                                stopCBS(false, true, false);
                                moatGoal = (MoatGoal) forwardTank(
                                        moatGoal,
                                        MoatGoal.SHOOT,
                                        Constants.Auto.goalMoatTwoForwardTime,
                                        Constants.Auto.goalMoatTwoForwardPowerY,
                                        Constants.Auto.goalMoatTwoForwardPowerTwist);
                                break;
                            case 3:
                                stopCBS(false, true, false);
                                moatGoal = (MoatGoal) forwardTank(
                                        moatGoal,
                                        MoatGoal.SHOOT,
                                        Constants.Auto.goalMoatThreeForwardTime,
                                        Constants.Auto.goalMoatThreeForwardPowerY,
                                        Constants.Auto.goalMoatThreeForwardPowerTwist);
                                break;
                            case 4:
                                stopCBS(false, true, false);
                                moatGoal = (MoatGoal) forwardTank(
                                        moatGoal,
                                        MoatGoal.SHOOT,
                                        Constants.Auto.goalMoatFourForwardTime,
                                        Constants.Auto.goalMoatFourForwardPowerY,
                                        Constants.Auto.goalMoatFourForwardPowerTwist);
                                break;
                            case 5:
                                stopCBS(false, true, false);
                                moatGoal = (MoatGoal) forwardTank(
                                        moatGoal,
                                        MoatGoal.SHOOT,
                                        Constants.Auto.goalMoatFiveForwardTime,
                                        Constants.Auto.goalMoatFiveForwardPowerY,
                                        Constants.Auto.goalMoatFiveForwardPowerTwist);
                                break;
                        }
                        break;
                    case MOAT:
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
                                moatGoal = MoatGoal.FORWARD;
                                break;
                            default:
                                break;
                        }
                        break;
                    case SHOOT:
                        if(timer.get() > 1.0)
                        {
                            autoAim.idle(pos);
                        }
                        break;
                    default:
                        break;
                    
                }
                break;
            case 4:
                switch(rampGoal)
                {
                    case FORWARD:
                        switch(pos)
                        {
                            case 0:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 1:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 2:
                                stopCBS(false, true, false);
                                rampGoal = (RampGoal) forwardTank(
                                        rampGoal,
                                        RampGoal.SHOOT,
                                        Constants.Auto.goalRampTwoForwardTime,
                                        Constants.Auto.goalRampTwoForwardPowerY,
                                        Constants.Auto.goalRampTwoForwardPowerTwist);
                                break;
                            case 3:
                                stopCBS(false, true, false);
                                rampGoal = (RampGoal) forwardTank(
                                        rampGoal,
                                        RampGoal.SHOOT,
                                        Constants.Auto.goalRampThreeForwardTime,
                                        Constants.Auto.goalRampThreeForwardPowerY,
                                        Constants.Auto.goalRampThreeForwardPowerTwist);
                                break;
                            case 4:
                                stopCBS(false, true, false);
                                rampGoal = (RampGoal) forwardTank(
                                        rampGoal,
                                        RampGoal.SHOOT,
                                        Constants.Auto.goalRampFourForwardTime,
                                        Constants.Auto.goalRampFourForwardPowerY,
                                        Constants.Auto.goalRampFourForwardPowerTwist);
                                break;
                            case 5:
                                stopCBS(false, true, false);
                                rampGoal = (RampGoal) forwardTank(
                                        rampGoal,
                                        RampGoal.SHOOT,
                                        Constants.Auto.goalRampFiveForwardTime,
                                        Constants.Auto.goalRampFiveForwardPowerY,
                                        Constants.Auto.goalRampFiveForwardPowerTwist);
                                break;
                        }
                        break;
                    case RAMPARTS:
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
                                rampGoal = RampGoal.FORWARD;
                                break;
                            default:
                                break;
                        }
                        break;
                    case SHOOT:
                        if(timer.get() > 1.0)
                        {
                            autoAim.idle(pos);
                        }
                        break;
                    default:
                        break;
                    
                }
                break;
            case 8:
                switch(goal)
                {

                    case FORWARD:
                        goal = (LowBarGoal) forwardTank(goal, LowBarGoal.SHOOT,
                                Constants.Auto.goalForwardTime,
                                Constants.Auto.goalForwardPowerY,
                                Constants.Auto.goalForwardPowerTwist);
                        break;
                    case LOWBAR:
                        switch(lowBar)
                        {
                            case FORWARD:
                                stopCBS(false, true, false);
                                goal = (LowBarGoal) forward(goal,
                                        LowBarGoal.FORWARD,
                                        Constants.Auto.goalBarForwardTime,
                                        Constants.Auto.goalBarForwardSpeed);
                                break;
                            case LOWERING:
                                lowBar = (LowBarAuto) lowerArm(lowBar,
                                        LowBarAuto.FORWARD,
                                        Constants.Auto.lowBarArmPower,
                                        Constants.Auto.lowBarArmTime);
                                break;
                            case REVSHOOT:
                                goal = LowBarGoal.FORWARD;
                                break;
                            default:
                                DriverStation
                                        .reportError(
                                                "Auto default state! Auto is disfunctional!",
                                                true);
                                break;
                        }
                        break;
                    case SHOOT:
                        if(timer.get() > 1.0)
                        {
                            autoAim.idle(1);
                        }
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch(chevalGoal)
                {

                    case FORWARD:
                        switch(pos)
                        {
                            case 0:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 1:
                                stopCBS(false, true, false);
                                revNoLower();
                                break;
                            case 2:
                                stopCBS(false, true, false);
                                chevalGoal = (ChevalGoal) forwardTank(
                                        chevalGoal,
                                        ChevalGoal.SHOOT,
                                        Constants.Auto.goalCDFTwoForwardTime,
                                        Constants.Auto.goalCDFTwoForwardPowerY,
                                        Constants.Auto.goalCDFTwoForwardPowerTwist);
                                break;
                            case 3:
                                stopCBS(false, true, false);
                                chevalGoal = (ChevalGoal) forwardTank(
                                        chevalGoal,
                                        ChevalGoal.SHOOT,
                                        Constants.Auto.goalCDFThreeForwardTime,
                                        Constants.Auto.goalCDFThreeForwardPowerY,
                                        Constants.Auto.goalCDFThreeForwardPowerTwist);
                                break;
                            case 4:
                                stopCBS(false, true, false);
                                chevalGoal = (ChevalGoal) forwardTank(
                                        chevalGoal,
                                        ChevalGoal.SHOOT,
                                        Constants.Auto.goalCDFFourForwardTime,
                                        Constants.Auto.goalCDFFourForwardPowerY,
                                        Constants.Auto.goalCDFFourForwardPowerTwist);
                                break;
                            case 5:
                                stopCBS(false, true, false);
                                chevalGoal = (ChevalGoal) forwardTank(
                                        chevalGoal,
                                        ChevalGoal.SHOOT,
                                        Constants.Auto.goalCDFFiveForwardTime,
                                        Constants.Auto.goalCDFFiveForwardPowerY,
                                        Constants.Auto.goalCDFFiveForwardPowerTwist);
                                break;
                        }
                        break;
                    case CHEVAL:
                        switch(cheval)
                        {
                            case FORWARD:
                                cheval = (ChevalAuto) forward(cheval,
                                        ChevalAuto.LOWERING,
                                        Constants.Auto.chevalTime,
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
                                SmartDashboard.putNumber(
                                        "The Breacher Power 2",
                                        breacher.getTalonSpeed());
                                break;
                            case REVSHOOT:
                                chevalGoal = ChevalGoal.FORWARD;
                                break;
                            default:
                                DriverStation
                                        .reportError(
                                                "Auto default state! Auto is dysfunctional!",
                                                true);
                                break;
                        }
                        break;
                    case SHOOT:
                        if(timer.get() > 1.0)
                        {
                            autoAim.idle(pos);
                        }
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
