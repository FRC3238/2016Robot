package org.usfirst.frc.team3238.robot;

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
            rightDriveTalonB, breacherTalon, collectorTalon, shooterTalonLeft,
            shooterTalonRight;
    DigitalInput armDetectTop, armDetectBot;
    Breacher breacherArm;
    public double throttleRangeAdjuster;
    public static boolean camChanging;
    public static boolean camDead;

    public void defineConstants() throws java.io.FileNotFoundException
    {

    }

    public void robotInit()
    {
        try
        {
            armDetectTop = new DigitalInput(Constants.Breacher.armDetectTopPort);
            armDetectBot = new DigitalInput(Constants.Breacher.armDetectBotPort);

            leftDriveTalonA = new CANTalon(Constants.Chassis.leftMotorOneID);
            leftDriveTalonB = new CANTalon(Constants.Chassis.leftMotorTwoID);
            rightDriveTalonA = new CANTalon(Constants.Chassis.rightMotorOneID);
            rightDriveTalonB = new CANTalon(Constants.Chassis.rightMotorTwoID);

            breacherTalon = new CANTalon(Constants.Breacher.breacherTalonPort);

            breacherArm = new Breacher(breacherTalon, armDetectTop,
                    armDetectBot);
            chassis = new Chassis(leftDriveTalonA, leftDriveTalonB,
                    rightDriveTalonA, rightDriveTalonB);

            breacherArm = new Breacher(breacherTalon);

            camera = new Camera(Constants.Camera.frontCamName,
                    Constants.Camera.rearCamName,
                    Constants.Camera.crosshairCenterX,
                    Constants.Camera.crosshairCenterY);
            camera.init(Constants.Camera.camQuality, Constants.Camera.camSize);
            camDead = false;

            ballControl = new CollectAndShoot(
                    Constants.CollectAndShoot.collectorTalonPort,
                    Constants.CollectAndShoot.shooterLeftTalonPort,
                    Constants.CollectAndShoot.shooterRightTalonPort,
                    Constants.CollectAndShoot.ballDetectChannel,
                    Constants.CollectAndShoot.centerDetectChannel,
                    Constants.CollectAndShoot.holdPosDetectChannel,
                    Constants.Robot.joystickZeroPort);

            throttleRangeAdjuster = Constants.Robot.throttleRangeAdjuster;

            joystickZero = new Joystick(Constants.Robot.joystickZeroPort);
            joystickOne = new Joystick(Constants.Robot.joystickOnePort);
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
        breacherCommands(throttleZero);
        ballControl.idle();
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
        if(joystickZero.getRawButton(Constants.Camera.camChangeButton)
                && camChanging)
        {
            camChanging = false;
            camera.changeCam();
        } else if(!joystickZero.getRawButton(Constants.Camera.camChangeButton))
        {
            camChanging = true;
        }

        if(camDead)
            camera.idle();

        if(joystickZero.getRawButton(Constants.Camera.camKillSwitch) && camDead)
            camDead = false;
        else if(joystickZero.getRawButton(Constants.Camera.camKillSwitch)
                && !camDead)
            camDead = true;
    }

    // breacher stuff
    private void breacherCommands(double throttleOne)
    {
        if(joystickZero.getRawButton(Constants.Breacher.breacherUpButton))
        {
            breacherArm.raiseArm();
        } else if(joystickZero
                .getRawButton(Constants.Breacher.breacherDownButton))
        {
            breacherArm.raiseArmWO(throttleOne);
        } else if(joystickZero
                .getRawButton(Constants.Breacher.breacherDownButton))
        {
            breacherArm.lowerArmWO(throttleOne);
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
