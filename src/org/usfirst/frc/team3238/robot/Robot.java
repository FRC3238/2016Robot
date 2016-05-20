package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class Robot extends IterativeRobot
{
    Breacher breacherArm;
    Camera camera;
    Chassis chassis;
    Collector collector;
    Shooter shooter;
    Vision vision;
    Autonomous auto;
    Joystick assistantJoystick, mainJoystick, launchPad;
    CANTalon leftDriveTalonA, leftDriveTalonB, rightDriveTalonA,
            rightDriveTalonB;
    CANTalon breacherTalon;
    CANTalon collectorTalon;
    CANTalon shooterTalonLeft, shooterTalonRight;
    DigitalInput ballDetectSwitch;
    Timer timer, tim;
    NetworkTable netTab;
    public static boolean camChanging;
    public static boolean camDead;

    public void robotInit()
    {
        try
        {
            assistantJoystick = new Joystick(
                    Constants.Joysticks.joystickZeroPort);
            mainJoystick = new Joystick(Constants.Joysticks.joystickOnePort);
            launchPad = new Joystick(Constants.Joysticks.launchpadPort);

            ballDetectSwitch = new DigitalInput(
                    Constants.Collector.ballDetectPort);

            leftDriveTalonA = new CANTalon(Constants.Chassis.leftMotorOneID);
            leftDriveTalonB = new CANTalon(Constants.Chassis.leftMotorTwoID);
            rightDriveTalonA = new CANTalon(Constants.Chassis.rightMotorOneID);
            rightDriveTalonB = new CANTalon(Constants.Chassis.rightMotorTwoID);
            rightDriveTalonA.setInverted(true);
            rightDriveTalonB.setInverted(true);
            breacherTalon = new CANTalon(Constants.Breacher.breacherTalonID);
            collectorTalon = new CANTalon(Constants.Collector.collectorTalonID);
            shooterTalonLeft = new CANTalon(
                    Constants.Shooter.shooterLeftTalonID);
            shooterTalonRight = new CANTalon(
                    Constants.Shooter.shooterRightTalonID);

            breacherArm = new Breacher(breacherTalon);
            netTab = NetworkTable.getTable("GRIP");
            netTab.putBoolean("run", true);
            try
            {
                camera = new Camera(assistantJoystick);
                camera.init(Constants.Camera.camQuality,
                        Constants.Camera.camSize);
                camDead = false;
            } catch(Exception e)
            {
                DriverStation.reportError("Camera error: ", true);
                camDead = true;
            }
            chassis = new Chassis(leftDriveTalonA, leftDriveTalonB,
                    rightDriveTalonA, rightDriveTalonB);
            shooter = new Shooter(shooterTalonLeft, shooterTalonRight,
                    mainJoystick, assistantJoystick, launchPad);
            collector = new Collector(collectorTalon, ballDetectSwitch,
                    shooter, mainJoystick, assistantJoystick, launchPad);
            vision = new Vision(chassis, shooter, collector);
            auto = new Autonomous(chassis, breacherArm, shooter, collector,
                    vision);
            timer = new Timer();
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }

    public void autonomousInit()
    {
        auto.init();
        shooter.reset();
        netTab.putBoolean("run", true);
    }

    public void autonomousPeriodic()
    {
        // auto.switchingAuto();
        auto.autoRun();
        netTab.putBoolean("run", true);
    }

    public void teleopInit()
    {
        collector.init();
        shooter.reset();
        SmartDashboard.putNumber("DB/Slider 0", 0);
        netTab.putBoolean("run", false);
    }

    public void teleopPeriodic()
    {
        if(mainJoystick.getRawButton(11))
        {
            vision.teleVision();
            camera.stream();
            breacherArm.run(assistantJoystick);
            collector.idle();
            shooter.idle(collector.isCollecting());
        } else 
        {
            chassisCommands();
            camera.stream();
            breacherArm.run(assistantJoystick);
            collector.idle();
            shooter.idle(collector.isCollecting());
            netTab.putBoolean("run", false);
        }
        SmartDashboard.putBoolean("DB/Button 0", vision.getTowerPos());
    }

    // Drive system
    private void chassisCommands()
    {
        chassis.setMotorInversion(mainJoystick);
        chassis.arcadeDrive(mainJoystick, launchPad);
    }

    public void disabledPeriodic()
    {
        camera.stream();
        auto.init();
        chassis.setPower(0.0);
        breacherArm.moveArm(0.0);
        vision.stop();
        shooter.reset();
        netTab.putBoolean("run", false);
    }

    public void testPeriodic()
    {
        vision.teleVision();
        netTab.putBoolean("run", false);
    }

}