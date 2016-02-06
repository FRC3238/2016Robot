package org.usfirst.frc.team3238.robot;

import java.io.FileNotFoundException;
import java.text.ParseException;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;

public class Robot extends IterativeRobot
{
    Camera camera;
    Chassis chassis;
    CollectAndShoot ballControl;
    Joystick joystickZero, joystickOne;
    CANTalon leftDriveTalonA, leftDriveTalonB, rightDriveTalonA,
            rightDriveTalonB, breacherTalon, collectorTalon, shooterTalonA,
            shooterTalonB;
    DigitalInput ballDetect;
    DigitalInput armDetectTop, armDetectBot;
    Breacher breacherArm;
    ConstantInterpreter ci;
    public double throttleRangeAdjuster;
    public static boolean camChanging;

    public void defineConstants() throws java.io.FileNotFoundException
    {

    }

    public void robotInit()
    {
        try
        {
            ci = new ConstantInterpreter("kConstants.txt");

            ballDetect = new DigitalInput(ci.retrieveInt("ballDetectPort"));

            armDetectTop = new DigitalInput(ci.retrieveInt("armDetectTopPort"));
            armDetectBot = new DigitalInput(ci.retrieveInt("armDetectBotPort"));
            leftDriveTalonA = new CANTalon(
                    ci.retrieveInt("DriveLeftTalonAPort"));
            leftDriveTalonB = new CANTalon(
                    ci.retrieveInt("DriveLeftTalonBPort"));
            rightDriveTalonA = new CANTalon(
                    ci.retrieveInt("DriveRightTalonAPort"));
            rightDriveTalonB = new CANTalon(
                    ci.retrieveInt("DriveRightTalonBPort"));

            breacherTalon = new CANTalon(ci.retrieveInt("breacherTalonPort"));

            collectorTalon = new CANTalon(ci.retrieveInt("collectorTalonPort"));

            breacherArm = new Breacher(breacherTalon, armDetectTop,
                    armDetectBot);
            chassis = new Chassis(leftDriveTalonA, leftDriveTalonB,
                    rightDriveTalonA, rightDriveTalonB);
            breacherArm = new Breacher(breacherTalon);

            shooterTalonA = new CANTalon(ci.retrieveInt("ShooterLeftTalonPort"));
            shooterTalonB = new CANTalon(
                    ci.retrieveInt("ShooterRightTalonPort"));
            camera = new Camera(ci.retrieveString("frontCameraName"),
                    ci.retrieveString("rearCameraName"),
                    ci.retrieveInt("crosshairCenterX"),
                    ci.retrieveInt("crosshairCenterY"));
            camera.init(ci.retrieveInt("cameraQuality"),
                    ci.retrieveInt("cameraSize"));
            ballControl = new CollectAndShoot(
                    ci.retrieveInt("collectorTalonPort"),
                    ci.retrieveInt("ShooterLeftTalonPort"),
                    ci.retrieveInt("ShooterRightTalonPort"), 0, 1, 2, 0);
        } catch(Exception e)
        {
            e.printStackTrace();

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
        try
        {
            throttleRangeAdjuster = ci.retrieveDouble("throttleRangeAdjuster");
            joystickZero = new Joystick(ci.retrieveInt("joystickZeroPort"));
            joystickOne = new Joystick(ci.retrieveInt("joystickOnePort"));
        } catch(Exception e)
        {
            e.printStackTrace();
        }
        camChanging = true;
    }

    public void teleopPeriodic()
    {
        double throttleZero = joystickZero.getThrottle()
                + throttleRangeAdjuster;
        // double throttleOne = joystickOne.getThrottle() +
        // throttleRangeAdjuster;
        chassisCommands();
        cameraCommands();
        // breacherCommands(throttleOne);
    }

    // Drive system
    private void chassisCommands()
    {
        chassis.proDrive(joystickZero.getX(), joystickZero.getY(),
                joystickZero.getTwist());
    }

    // Camera stuff
    private void cameraCommands()
    {
        if(joystickZero.getRawButton(ci.retrieveInt("camChangeButton"))
                && camChanging)
        {
            camChanging = false;
            camera.changeCam();
        } else if(!joystickZero.getRawButton(ci.retrieveInt("camChangeButton")))
        {
            camChanging = true;
        }
        camera.idle();
    }

    // breacher stuff
    private void breacherCommands(double throttleOne)
    {
        if(joystickZero.getRawButton(ci
                .retrieveInt("breacherTalonForwardButton")))
        {

            breacherArm.raiseArm();
        } else if(joystickZero.getRawButton(ci
                .retrieveInt("breacherTalonReverseButton")))
        {
            breacherArm.raiseArmWO(throttleOne);
        } else if(joystickZero.getRawButton(ci
                .retrieveInt("breacherTalonReverseButton")))
        {
            breacherArm.lowerArmWO(-throttleOne);
        } else
        {
            breacherArm.standby();
        }

    }

    public void disabledPeriodic()
    {
        camera.idle();
    }

    public void testPeriodic()
    {
    }

}
