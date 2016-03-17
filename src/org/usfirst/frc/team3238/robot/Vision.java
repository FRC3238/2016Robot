package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;

public class Vision
{
    Chassis chassis;
    Shooter shooter;
    Collector collector;
    Timer timer;
    PIController pidX, pidY;
    NetworkTable netTab;
    double defaultX[] = { 120 };
    double x[];
    double defaultY[] = { 160 };
    double y[];
    double chassisY;
    double chassisTwist;
    boolean enabled = true;
    boolean isAligned = false;

    public Vision(Chassis driveTrain, Shooter shooter, Collector collector)
    {
        this.chassis = driveTrain;
        this.collector = collector;
        this.shooter = shooter;
        shooter.state = ShooterState.DISABLED;
        shooter.rpm = Constants.Vision.shooterSpeed;
        timer = new Timer();
        timer.reset();
        timer.start();
        pidX = new PIController(Constants.Vision.pValueX,
                Constants.Vision.iValueX, Constants.Vision.error);
        pidX.setThrottle(Constants.Vision.throttleX);
        pidY = new PIController(Constants.Vision.pValueY,
                Constants.Vision.iValueY, Constants.Vision.error);
        pidY.setThrottle(Constants.Vision.throttleY);
        netTab = NetworkTable.getTable("GRIP/Contours");
        chassisY = 0.0;
        chassisTwist = 0.0;
        stop();
    }

    void stop()
    {
        shooter.init();
        collector.init();
        stopChassis();
        timer.reset();
        pidX.reinit();
        pidY.reinit();
    }

    void stopChassis()
    {
        chassisY = 0.0;
        chassisTwist = 0.0;
    }

    void run()
    {
        shooter.idle(collector.isCollecting());
        shooter.changeState(ShooterState.RUNNING);
        shooter.rpm = Constants.Vision.shooterSpeed;
        collector.idle();
        chassis.arcadeDriveAuto(chassisY, chassisTwist);
    }

    boolean getTowerPos()
    {
        try
        {
            x = netTab.getNumberArray("centerX", defaultX);
            y = netTab.getNumberArray("centerY", defaultY);
        } catch(Exception e)
        {
            DriverStation.reportError("Target not found!", false);
        }

        if(x.length > 1)
        {
            DriverStation.reportError("Two vision targets!", false);
            return false;
        } else
        {
            return true;
        }
    }

    public void idle()
    {
        if(getTowerPos() && !pidX.isAligned(Constants.Vision.setPointX, x[0]))
        {
            chassisTwist = pidX.getMotorValue(Constants.Vision.setPointX, x[0]);
            chassisY = 0.0;
        } else if(getTowerPos()
                && !pidY.isAligned(Constants.Vision.setPointY, y[0]))
        {
            chassisY = pidY.getMotorValue(Constants.Vision.setPointY, y[0]);
            chassisTwist = pidX.getMotorValue(Constants.Vision.setPointX, x[0]);
        } else if(pidX.isAligned(Constants.Vision.setPointX, x[0])
                && pidY.isAligned(Constants.Vision.setPointY, y[0])
                && shooter.isRPMReached(Constants.Vision.shooterSpeed))
        {
            collector.shoot();
            stopChassis();
        } else
        {
            stopChassis();
        }

        run();
    }
}
