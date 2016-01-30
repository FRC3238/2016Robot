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
    Collector collector;
    Joystick joystickZero, joystickOne;
    CANTalon leftDriveTalon, rightDriveTalon, breacherTalon, collectorTalon;
    DigitalInput ballDetect;
    DigitalInput armDetectTop, armDetectBot;
    Breacher breacherArm;
    ConstantInterpreter ci;
    Shooter shooter;
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
            final int cameraQuality = ci.retrieveInt("cameraQuality");
            final int cameraSize = ci.retrieveInt("cameraSize");
            final int cameraCrosshairCenterX = ci
                    .retrieveInt("cameraCrosshairCenterX");
            final int cameraCrosshairCenterY = ci
                    .retrieveInt("cameraCrosshairCenterY");
            final int armDetectTopChannel = ci
                    .retrieveInt("armDetectTopChannel");
            final int armDetectBotChannel = ci
                    .retrieveInt("armDetectBotChannel");
            final int leftDriveTalonPort = ci.retrieveInt("leftDriveTalonPort");
            final int rightDriveTalonPort = ci
                    .retrieveInt("rightDriveTalonPort");
            final int breacherTalonPort = ci.retrieveInt("breacherTalonPort");
            final int collectorTalonPort = ci.retrieveInt("collectorTalonPort");
            final int ballDetectPort = ci.retrieveInt("ballDetectPort");
            final String frontCameraName = ci.retrieveString("frontCameraName");
            final String rearCameraName = ci.retrieveString("rearCameraName");

            armDetectTop = new DigitalInput(armDetectTopChannel);
            armDetectBot = new DigitalInput(armDetectBotChannel);
            leftDriveTalon = new CANTalon(leftDriveTalonPort);
            rightDriveTalon = new CANTalon(rightDriveTalonPort);

            breacherTalon = new CANTalon(breacherTalonPort);

            collectorTalon = new CANTalon(collectorTalonPort);

            breacherArm = new Breacher(breacherTalon, armDetectTop,
                    armDetectBot);
            chassis = new Chassis(leftDriveTalon, rightDriveTalon);

            collector = new Collector(ci.retrieveInt("collectorTalonPort"),
                    ballDetectPort);

            camera = new Camera(frontCameraName, rearCameraName,
                    cameraCrosshairCenterX, cameraCrosshairCenterY);
            camera.init(cameraQuality, cameraSize);
        } catch(Exception e)
        {
            e.printStackTrace();
            System.exit(0);
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
        // collectOrShootDivisor(throttleZero);
        collectorCommands(throttleZero,
                ci.retrieveInt("collectorForwardButton"),
                ci.retrieveInt("collectorReverseButton"),
                ci.retrieveInt("collectorManualButton"));
        // breacherCommands(throttleOne);
    }

    // Toggles b/w collector and shooter
    private void collectOrShootDivisor(double throttleZero)
    {
        if(!joystickZero.getRawButton(ci.retrieveInt("shootToggleButton")))
        {
            shooter.disable();
            collectorCommands(throttleZero,
                    ci.retrieveInt("collectorForwardButton"),
                    ci.retrieveInt("collectorReverseButton"),
                    ci.retrieveInt("collectorManualButton"));
        } else
        {
            collector.disable();
            shooter.enable(throttleZero);
        }
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
            breacherArm.lowerArm();
        } else
        {
            breacherArm.standby();
        }
    }

    // solely collector stuff
    private void collectorCommands(double throttleZero,
            int collectorForwardButton, int collectorReverseButton,
            int collectorManualButton)
    {
        collector.proCollector(throttleZero,
                joystickZero.getRawButton(collectorForwardButton),
                joystickZero.getRawButton(collectorReverseButton),
                joystickZero.getRawButton(collectorManualButton));
    }

    public void disabledPeriodic()
    {
        camera.idle();
    }

    public void testPeriodic()
    {
    }

}
