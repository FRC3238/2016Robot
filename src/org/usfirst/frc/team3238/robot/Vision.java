package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

public class Vision
{
    Chassis chassis;
    Shooter shooter;
    Collector collector;
    Timer timer;
    PIController pidX, pidY;
    NetworkTable netTab;
    double defaultX[] = { Constants.Vision.defaultX };
    double x[];
    double defaultY[] = { Constants.Vision.defaultY };
    double y[];
    double chassisY;
    double chassisTwist;
    boolean enabled = true;
    boolean isAligned = false;
    boolean targetFound = false;

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
            x = defaultX;
            y = defaultY;
            DriverStation.reportError("Target not found!", false);
            return false;
        }

        if(x.length > 1 && y.length > 1)
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
        try
        {
            if(!getTowerPos() && enabled)
            {
                stopChassis();
            } else if(!pidX.isAligned(Constants.Vision.setPointX, x[0])
                    && !targetFound)
            {
                chassisTwist = pidX.getMotorValue(Constants.Vision.setPointX,
                        x[0]);
                DriverStation
                        .reportError(
                                "X PID: " + -pidX.getMotorValue(
                                        Constants.Vision.setPointX, x[0]),
                        false);
                chassisY = 0.0;
                timer.reset();
                timer.start();
            } else if(!pidY.isAligned(Constants.Vision.setPointY, y[0])
                    && !targetFound)
            {
                chassisY = -pidY.getMotorValue(Constants.Vision.setPointY,
                        y[0]);
                DriverStation
                        .reportError(
                                "Y PID: " + -pidY.getMotorValue(
                                        Constants.Vision.setPointY, y[0]),
                        false);
                chassisTwist = 0.0;
                timer.reset();
                timer.start();
            } else if(pidX.isAligned(Constants.Vision.setPointX, x[0])
                    && pidY.isAligned(Constants.Vision.setPointY, y[0]))
            {
                targetFound = true;
                stopChassis();
            } else if(targetFound)
            {
                if((timer.get() > 0.5)
                        && shooter.isRPMReached(Constants.Vision.shooterSpeed))
                {
                    collector.shoot();
                    enabled = false;
                }
            } else
            {
                stopChassis();
            }

            SmartDashboard.putNumber("PID X",
                    pidX.getMotorValue(Constants.Vision.setPointX, x[0]));
            SmartDashboard.putNumber("PID Y",
                    pidY.getMotorValue(Constants.Vision.setPointY, y[0]));

            run();
        } catch(ArrayIndexOutOfBoundsException e)
        {
            targetFound = false;
            DriverStation.reportError(e.getMessage(), true);
        }
    }
}
