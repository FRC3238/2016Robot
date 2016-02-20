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
    Joystick assistantJoystick, mainJoystick, launchPad;
    CANTalon leftDriveTalonA, leftDriveTalonB, rightDriveTalonA,
            rightDriveTalonB;
    CANTalon breacherTalon;
    CANTalon collectorTalon;
    CANTalon shooterTalonLeft, shooterTalonRight;
    DigitalInput ballDetectSwitch;
    Timer timer;
    SendableChooser chooser;
    public static boolean camChanging;
    public static boolean camDead;
    AutonomousValueTester avt;
    
    public void robotInit()
    {
        try
        {
            chooser.addDefault("Moat", "moat");
            chooser.addObject("Rock", "rockwall");
            chooser.addObject("Ramparts", "ramparts");
            chooser.addObject("Rough Terrain", "roughterrain");
            chooser.addObject("Low Bar", "lowbar");
            chooser.addObject("Cheval De Frise", "cheval");
            chooser.addObject("Portcullis", "portcullis");
            SmartDashboard.putData("Auto Choices", chooser);
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
            // try
            // {
            camera = new Camera(Constants.Camera.frontCamName,
                    Constants.Camera.rearCamName,
                    Constants.Camera.crosshairCenterX,
                    Constants.Camera.crosshairCenterY, mainJoystick);
            camera.init(Constants.Camera.camQuality, Constants.Camera.camSize);
            camDead = false;
            // } catch(Exception e)
            // {
            // DriverStation.reportError("Camera error: ", true);
            // camDead = true;
            // }
            chassis = new Chassis(leftDriveTalonA, leftDriveTalonB,
                    rightDriveTalonA, rightDriveTalonB);
            collector = new Collector(collectorTalon, ballDetectSwitch,
                    mainJoystick);
            shooter = new Shooter(shooterTalonLeft, shooterTalonRight,
                    mainJoystick, assistantJoystick, launchPad);
            timer = new Timer();
            avt = new AutonomousValueTester(chassis, breacherArm, shooter);
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }

    public void autonomousInit()
    {
        avt.init();
    }

    public void autonomousPeriodic()
    {
        avt.start((String) chooser.getSelected()); 
    }

    public void teleopInit()
    {
        shooter.init();
        collector.init();
    }

    public void teleopPeriodic()
    {
        SmartDashboard.putNumber("SPD", shooter.itera);
        chassisCommands();
        //camera.stream();
        breacherArm.run(assistantJoystick);
        collector.idle();
        shooter.changer();
    }

    // Drive system
    private void chassisCommands()
    {
        chassis.setMotorInversion(mainJoystick);
        chassis.arcadeDrive(mainJoystick);
    }

    public void disabledPeriodic()
    {
        //camera.stream();
        avt.stop();
    }

    public void testPeriodic()
    {

    }

}
