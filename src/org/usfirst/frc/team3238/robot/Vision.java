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
    Timer timerTwo;
    PIController pidX, pidY;
    NetworkTable netTab;
    double defaultX[] = { Constants.Vision.defaultX };
    double x[];
    double defaultY[] = { Constants.Vision.defaultY };
    double y[];
    double chassisY;
    double chassisTwist;
    int cyclesNotFound = 0;
    boolean enabled = true;
    boolean isAligned = false;
    boolean targetFound = false;
    String control = "resetLeft";

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
        timerTwo = new Timer();
        timerTwo.reset();
        pidX = new PIController(Constants.Vision.pValueX,
                Constants.Vision.iValueX, Constants.Vision.errorX);
        pidX.setThrottle(Constants.Vision.throttleX);
        pidY = new PIController(Constants.Vision.pValueY,
                Constants.Vision.iValueY, Constants.Vision.errorY);
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

    void runTele()
    {
        shooter.idle(collector.isCollecting());
        collector.idle();
        chassis.arcadeDriveAuto(chassisY, chassisTwist);
    }

    boolean getTowerPos()
    {
        boolean ret;
        try
        {
            x = netTab.getNumberArray("centerX", defaultX);
            y = netTab.getNumberArray("centerY", defaultY);
            ret = true;
        } catch(Exception e)
        {
            x = defaultX;
            y = defaultY;
            DriverStation.reportError("Target not found!", false);
            ret = false;
        }
        
        if(x.length == 0 || y.length == 0)
        {
            ret = false;
        }

        return ret;
    }

    public void teleVision()
    {
        try
        {
            if(!getTowerPos() && enabled)
            {
                stopChassis();
            } else if(!pidY.isAligned(Constants.Vision.setPointY, y[0]))
            {
                chassisTwist = -pidY.getMotorValue(Constants.Vision.setPointY,
                        y[0]);
                DriverStation
                        .reportError(
                                "Y PID: " + -pidY.getMotorValue(
                                        Constants.Vision.setPointY, y[0]),
                        false);
                DriverStation
                        .reportError(
                                "X PID: " + -pidX.getMotorValue(
                                        Constants.Vision.setPointX, x[0]),
                        false);
                chassisY = -pidX.getMotorValue(Constants.Vision.setPointX,
                        x[0]);
                timer.reset();
                timer.start();
            } else if(!pidX.isAligned(Constants.Vision.setPointX, x[0]))
            {
                chassisY = -pidX.getMotorValue(Constants.Vision.setPointX,
                        x[0]);
                DriverStation
                        .reportError(
                                "X PID: " + -pidX.getMotorValue(
                                        Constants.Vision.setPointX, x[0]),
                        false);
                DriverStation
                        .reportError(
                                "Y PID: " + -pidY.getMotorValue(
                                        Constants.Vision.setPointY, y[0]),
                        false);
                chassisTwist = -pidY.getMotorValue(Constants.Vision.setPointY,
                        y[0]);
                timer.reset();
                timer.start();
            } else
            {
                stopChassis();
            }

            SmartDashboard.putNumber("PID X",
                    pidX.getMotorValue(Constants.Vision.setPointX, x[0]));
            SmartDashboard.putNumber("PID Y",
                    pidY.getMotorValue(Constants.Vision.setPointY, y[0]));

            runTele();
        } catch(ArrayIndexOutOfBoundsException e)
        {
            targetFound = false;
            
            DriverStation.reportError(e.getMessage(), true);
        }
    }
    
    public void sweepX(int position)
    {
        switch(position)
        {
            case 0:
                chassis.disable();
                break;
            case 1:
                chassis.arcadeDriveAuto(0.0, -0.6);
                break;
            case 2:
                chassis.arcadeDriveAuto(0.0, -0.6);
                break;
            case 3:
                chassis.arcadeDriveAuto(0.0, 0.5);
                break;
            case 4:
                chassis.arcadeDriveAuto(0.0, -0.5);
                break;
            case 5:
                chassis.arcadeDriveAuto(0.0, 0.5);
                break;
        }
    }
    
    public void sweepX()
    {
        switch(control)
        {
            case "left":
                chassis.arcadeDriveAuto(0.0, 0.5);
                if(timer.get() >= Math.random())
                {
                    control = "resetRight";
                }
                break;
            case "resetLeft":
                timer.reset();
                timer.start();
                control = "left";
                break;
            case "resetRight":
                timer.reset();
                timer.start();
                control = "right";
                break;
            case "right":
                chassis.arcadeDriveAuto(0.0, 0.5);
                if(timer.get() >= Math.random() * -1)
                {
                    control = "resetRight";
                }
                break;
        }
    }
    
    public void sweepY()
    {
        chassis.arcadeDriveAuto(-0.4, 0.0);
    }

    public void idle(int pos)
    {
        try
        {
            if(!getTowerPos() && enabled)
            {
                stopChassis();
            } else if(!pidY.isAligned(Constants.Vision.setPointY, y[0]))
            {
                targetFound = true;
                chassisTwist = -pidY.getMotorValue(Constants.Vision.setPointY,
                        y[0]);
                DriverStation
                        .reportError(
                                "Y PID: " + -pidY.getMotorValue(
                                        Constants.Vision.setPointY, y[0]),
                        false);
                chassisY = -pidX.getMotorValue(Constants.Vision.setPointX,
                        x[0]);
                timer.reset();
                timer.start();
            } else if(!pidX.isAligned(Constants.Vision.setPointX, x[0]))
            {
                targetFound = true;
                chassisY = -pidX.getMotorValue(Constants.Vision.setPointX,
                        x[0]);
                DriverStation
                        .reportError(
                                "X PID: " + -pidX.getMotorValue(
                                        Constants.Vision.setPointX, x[0]),
                        false);
                chassisTwist = -pidY.getMotorValue(Constants.Vision.setPointY,
                        y[0]);
                timer.reset();
                timer.start();
            } else if(pidX.isAligned(Constants.Vision.setPointX, x[0])
                    && pidY.isAligned(Constants.Vision.setPointY, y[0]))
            {
                if((timer.get() > 1.0)
                        && shooter.isRPMReached(Constants.Vision.shooterSpeed))
                {
                    collector.shoot();
                    enabled = false;
                }
                stopChassis();
            } else
            {
                stopChassis();
            }

            SmartDashboard.putNumber("PID X",
                    pidX.getMotorValue(Constants.Vision.setPointX, x[0]));
            SmartDashboard.putNumber("PID Y",
                    pidY.getMotorValue(Constants.Vision.setPointY, y[0]));

            run();
            cyclesNotFound = 0;
        } catch(ArrayIndexOutOfBoundsException e)
        {
            if(targetFound)
            {
                sweepX(pos);
                DriverStation.reportError("Target Found and Lost", false);
            } else
            {
                sweepX(pos);
                DriverStation.reportError("Target not found", false);
            }
            DriverStation.reportError(e.getMessage(), true);
        }
    }
}
