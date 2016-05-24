package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Timer;

/**
 * A class containing vision processing algorithms and operating parameters
 * disclaimer: VP is not working on our robot due to camera returns
 * 
 * @author FRC Team 3238
 * 
 * @version 1.0
 */ 
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
    boolean enabled = true; //try using vision processing?
    boolean isAligned = false;
    boolean targetFound = false;
    String control = "resetLeft";

    /**
     * Constructor for vision object
     * 
     * @param driveTrain the drive motors on the robot
     * 
     * @param shooter the shooter subsystem
     * 
     * @param collector the collector subsystem
     */ 
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
        netTab = NetworkTable.getTable("GRIP/Contours"); //gets vision processing w/ grip output
        chassisY = 0.0;
        chassisTwist = 0.0;
        stop();
    }
    /**
     * Resets dynamic values and enables subsystems. (Not sure why it's called stop, more like reset)
     */ 
    void stop()
    {
        shooter.init();
        collector.init();
        stopChassis();
        timer.reset();
        pidX.reinit();
        pidY.reinit();
    }

    /**
     * Makes the movespeed (chassisY) and turnspeed (chassisTwist) equal to 0, thus stopping chassis/drivesystem/wheel movement
     */ 
    void stopChassis()
    {
        chassisY = 0.0;
        chassisTwist = 0.0;
    }
    /**
     * Starts shooter, doesn't start if collector is collecting
     * sets the RPM to an optimized speed for a static distance
     * turns on collector mode
     * moves chassis with chassisY and chassisTwist
     */ 
    void run()
    {
        shooter.idle(collector.isCollecting());
        shooter.changeState(ShooterState.RUNNING);
        shooter.rpm = Constants.Vision.shooterSpeed;
        collector.idle();
        chassis.arcadeDriveAuto(chassisY, chassisTwist);
    }
    /**
     * used in teleop- doesn't change RPM because assume driver has the right RPM for the distance and doesn't use collector because
     * there is already a ball, tries to align with goal
     */ 
    void runTele()
    {
        shooter.idle(collector.isCollecting());
        collector.idle();
        chassis.arcadeDriveAuto(chassisY, chassisTwist);
    }
    /**
     * gets and sets the X and Y value of the 'contour' (detected shape)
     * 
     * @return ret true if X and Y are set to a dynamic value and the tower is detected.
     */ 
    boolean getTowerPos()
    {
        boolean ret;
        try
        {
            x = netTab.getNumberArray("centerX", defaultX); 
            y = netTab.getNumberArray("centerY", defaultY);
            ret = true;
        } catch(Exception e) //if defaultX and defaultY have no indexes then set X and Y to default values
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
    /**
     * Aligns with the tower in tele-op
     */ 
    public void teleVision()
    {
        try
        {
            if(!getTowerPos() && enabled) //if trying to tower align but tower is not detected, stop he chassis
            {
                stopChassis();
            } else if(!pidY.isAligned(Constants.Vision.setPointY, y[0])) //If enabled but y is not accurate, do some PI stuff to align 
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
            } else if(!pidX.isAligned(Constants.Vision.setPointX, x[0])) //same as above, just with X
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
    /**
     * Does some basic turning alignment to help for less error when the PI loop starts, based on the defense crossed in autonomous
     * 
     * @param position the position of the defense crossed in autonomous
     */ 
    public void sweepX(int position)
    {
        switch(position)
        {
            case 0:
                chassis.disable(); //chassis didn't cross anything
                break;
            case 1:
                chassis.arcadeDriveAuto(0.0, -0.6); //turn a bit to align better
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
    /**
     * Does some random chassis movement action to try to get a rough alignment
     */ 
    public void sweepX()
    {
        switch(control)
        {
            case "left":
                chassis.arcadeDriveAuto(0.0, 0.5);
                if(timer.get() >= Math.random()) //2lazy4PI, just use math.random
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
                if(timer.get() >= Math.random() * -1) //why is it -1?
                {
                    control = "resetRight";
                }
                break;
        }
    }
    
    /**
     * Drives backwards if bot is too close
     */ 
    public void sweepY()
    {
        chassis.arcadeDriveAuto(-0.4, 0.0);
    }

    /**
     * Moves and aligns bot to shoot, same functionality as teleVision() except designed for autonomous so more control with auto
     * 
     * @param pos the position of he defense crossed in autonomous
     */ 
    public void idle(int pos)
    {
        try
        {
            if(!getTowerPos() && enabled) //tower not detected but enabled, stop chassis
            {
                stopChassis();
            } else if(!pidY.isAligned(Constants.Vision.setPointY, y[0])) //if tower detected but not Y aligned use PI to align fwd/bwd
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
            } else if(!pidX.isAligned(Constants.Vision.setPointX, x[0])) //same as above, with x
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
                    && pidY.isAligned(Constants.Vision.setPointY, y[0])) //If both are aligned then start up the shooter
            {
                if((timer.get() > 1.0)
                        && shooter.isRPMReached(Constants.Vision.shooterSpeed)) //if the RPM is fast enough to shoot ball into the goal, and it's been 1second, shoot
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
