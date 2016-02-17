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
    Joystick assistantDriver, mainDriver, launchPad;
    CANTalon leftDriveTalonA, leftDriveTalonB, rightDriveTalonA,
            rightDriveTalonB;
    CANTalon breacherTalon;
    CANTalon collectorTalon;
    CANTalon shooterTalonLeft, shooterTalonRight;
    DigitalInput armDetectTop, armDetectBot;
    DigitalInput ballDetectSwitch;

    public void robotInit()
    {
        try
        {
            assistantDriver = new Joystick(Constants.Robot.joystickZeroPort);
            mainDriver = new Joystick(Constants.Robot.joystickOnePort);
            launchPad = new Joystick(Constants.Robot.launchpadPort);

            armDetectTop = new DigitalInput(Constants.Breacher.armDetectTopPort);
            armDetectBot = new DigitalInput(Constants.Breacher.armDetectBotPort);
            ballDetectSwitch = new DigitalInput(
                    Constants.Collector.ballDetectChannel);

            leftDriveTalonA = new CANTalon(Constants.Chassis.leftMotorOneID);
            leftDriveTalonB = new CANTalon(Constants.Chassis.leftMotorTwoID);
            rightDriveTalonA = new CANTalon(Constants.Chassis.rightMotorOneID);
            rightDriveTalonB = new CANTalon(Constants.Chassis.rightMotorTwoID);
            rightDriveTalonA.setInverted(true);
            rightDriveTalonB.setInverted(true);
            breacherTalon = new CANTalon(Constants.Breacher.breacherTalonPort);
            collectorTalon = new CANTalon(
                    Constants.Collector.collectorTalonPort);
            shooterTalonLeft = new CANTalon(
                    Constants.Shooter.shooterLeftTalonPort);
            shooterTalonRight = new CANTalon(
                    Constants.Shooter.shooterRightTalonPort);

            breacherArm = new Breacher(breacherTalon);
            try
            {
                camera = new Camera(Constants.Camera.frontCamName,
                        Constants.Camera.rearCamName,
                        Constants.Camera.crosshairCenterX,
                        Constants.Camera.crosshairCenterY, mainDriver);
                camera.init(Constants.Camera.camQuality,
                        Constants.Camera.camSize);
            } catch(Exception e)
            {
                DriverStation.reportError("Camera error: ", true);
            }
            chassis = new Chassis(leftDriveTalonA, leftDriveTalonB,
                    rightDriveTalonA, rightDriveTalonB);
            collector = new Collector(collectorTalon, ballDetectSwitch,
                    mainDriver);
            shooter = new Shooter(shooterTalonLeft, shooterTalonRight,
                    mainDriver, assistantDriver, launchPad);
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }

    public void autonomousInit()
    {
    	
    }

    public void autonomousPeriodic()
    {

    }

    public void teleopInit()
    {

    }

    public void teleopPeriodic()
    {
    	chassis.idle(mainDriver);
        camera.stream();
        breacherArm.idle(assistantDriver);
        collector.idle();
        shooter.idle();
        SmartDashboard.putString("PowerZone", chassis.state);
    }

    public void disabledPeriodic()
    {
        camera.stream();
    }

    public void testPeriodic()
    {

    }

}
