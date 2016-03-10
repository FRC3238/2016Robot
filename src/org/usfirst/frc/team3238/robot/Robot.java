package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.CANTalon;

public class Robot extends IterativeRobot
{
    Breacher breacherArm;
    Camera camera;
    Chassis chassis;
    Collector collector;
    Shooter shooter;
    Autonomous auto;
    Joystick assistantJoystick, mainJoystick, launchPad;
    CANTalon leftDriveTalonA, leftDriveTalonB, rightDriveTalonA,
            rightDriveTalonB;
    CANTalon breacherTalon;
    CANTalon collectorTalon;
    CANTalon shooterTalonLeft, shooterTalonRight;
    DigitalInput ballDetectSwitch;
    Timer timer, tim;
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
            collector = new Collector(collectorTalon, ballDetectSwitch, shooter,
                    mainJoystick, assistantJoystick, launchPad);
            auto = new Autonomous(chassis, breacherArm, shooter, collector);
            timer = new Timer();
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage() + "Robot init error", true);
        }
    }

    public void autonomousInit()
    {
        auto.init();

    }

    public void autonomousPeriodic()
    {
        // auto.switchingAuto();
        auto.autoRun();
    }

    public void teleopInit()
    {
        collector.init();
        SmartDashboard.putNumber("DB/Slider 0", 0);
    }

    public void teleopPeriodic()
    {
        chassisCommands();
        camera.stream();
        breacherArm.run(assistantJoystick);
        collector.idle();
        shooter.idle(collector.isCollecting());
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
    }

    public void testPeriodic()
    {

    }

}