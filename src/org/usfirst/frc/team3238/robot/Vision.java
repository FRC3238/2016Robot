package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Timer;

public class Vision
{

    Chassis chassis;
    Shooter shooter;
    Collector collector;
    Timer timer;
    NetworkTable netTab;
    double defaultY[] = { 120 };
    double y[];
    boolean isAligned = false;

    public Vision(Chassis driveTrain, Shooter shooter, Collector collector)
    {
        this.chassis = driveTrain;
        this.collector = collector;
        this.shooter = shooter;
        shooter.state = ShooterState.DISABLED;
        shooter.rpm = 3700;
        timer = new Timer();
        timer.reset();
        timer.start();
        netTab = NetworkTable.getTable("GRIP/myContoursReport");
    }

    public void init()
    {
        shooter.state = ShooterState.DISABLED;
        shooter.rpm = 3700;
        timer.reset();
        timer.start();
    }

    public void run()
    {
        SmartDashboard.putNumber("Vision timer: ", timer.get());
        
        try
        {
            SmartDashboard.putNumber("Y Vision",
                    netTab.getNumberArray("centerY", defaultY)[0]);

            y = netTab.getNumberArray("centerY", defaultY);
        } catch(Exception e)
        {
            DriverStation.reportError("Target not found!", false);
        }

        if(y.length > 1)
        {
            DriverStation.reportError("Two vision targets!", false);
        } else if(y[0] < 112)
        {
            chassis.leftMotorControllerA.set(0.3);
            chassis.leftMotorControllerB.set(0.3);
            chassis.rightMotorControllerA.set(0.3);
            chassis.rightMotorControllerB.set(0.3);
            timer.reset();
            timer.start();
            shooter.state = ShooterState.RUNNING;
            shooter.rpm = 3700;
        } else if(y[0] > 128)
        {
            chassis.rightMotorControllerA.set(-0.3);
            chassis.rightMotorControllerB.set(-0.3);
            chassis.leftMotorControllerA.set(-0.3);
            chassis.leftMotorControllerB.set(-0.3);
            timer.reset();
            timer.start();
            shooter.state = ShooterState.RUNNING;
            shooter.rpm = 3700;
        } else
        {
            chassis.rightMotorControllerA.set(0.0);
            chassis.rightMotorControllerB.set(0.0);
            chassis.leftMotorControllerA.set(0.0);
            chassis.leftMotorControllerB.set(0.0);
            shooter.state = ShooterState.RUNNING;
            if(shooter.leftRPM >= 3500 && shooter.rightRPM >= 3500
                    && timer.get() > 1.0)
            {
                collector.shoot();
            } else
            {
                collector.state = CollectorState.DISABLED;
            }
        }

//        if(y[0] < 128 && y[0] > 112 && timer.get() != 0.0)
//        {
//            timer.reset();
//            timer.start();
//        }

        shooter.state = ShooterState.RUNNING;
        shooter.rpm = 3700;

        if(isAligned)
        {
            shooter.state = ShooterState.RUNNING;
            if(shooter.leftRPM >= 3700)
            {
                if(shooter.rightRPM >= 3700)
                {
                    collector.state = CollectorState.SHOOTING;
                } else
                {
                    collector.state = CollectorState.DISABLED;
                }
            } else
            {
                collector.state = CollectorState.DISABLED;
            }
        }

        shooter.idle(collector.isCollecting());
        collector.idle();
    }
}
