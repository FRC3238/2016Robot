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
    Joystick joystickZero, joystickOne, launchPad;
    CANTalon leftDriveTalonA, leftDriveTalonB, rightDriveTalonA,
            rightDriveTalonB;
    CANTalon breacherTalon;
    CANTalon collectorTalon;
    CANTalon shooterTalonLeft, shooterTalonRight;
    DigitalInput armDetectTop, armDetectBot;
    DigitalInput ballDetectSwitch;
    Timer timer;
    public double throttleRangeAdjuster;
    public static boolean camChanging;
    public static boolean camDead;

    public void robotInit()
    {
        joystickZero = new Joystick(Constants.Robot.joystickZeroPort);
        joystickOne = new Joystick(Constants.Robot.joystickOnePort);
        launchPad = new Joystick(Constants.Robot.launchpadPort);

        armDetectTop = new DigitalInput(Constants.Breacher.armDetectTopPort);
        armDetectBot = new DigitalInput(Constants.Breacher.armDetectBotPort);
        ballDetectSwitch = new DigitalInput(
                Constants.CollectAndShoot.ballDetectChannel);

        leftDriveTalonA = new CANTalon(Constants.Chassis.leftMotorOneID);
        leftDriveTalonB = new CANTalon(Constants.Chassis.leftMotorTwoID);
        rightDriveTalonA = new CANTalon(Constants.Chassis.rightMotorOneID);
        rightDriveTalonB = new CANTalon(Constants.Chassis.rightMotorTwoID);
        rightDriveTalonA.setInverted(true);
        rightDriveTalonB.setInverted(true);
        breacherTalon = new CANTalon(Constants.Breacher.breacherTalonPort);
        collectorTalon = new CANTalon(
                Constants.CollectAndShoot.collectorTalonPort);
        shooterTalonLeft = new CANTalon(
                Constants.CollectAndShoot.shooterLeftTalonPort);
        shooterTalonRight = new CANTalon(
                Constants.CollectAndShoot.shooterRightTalonPort);

        breacherArm = new Breacher(breacherTalon);
        try
        {
            camera = new Camera(Constants.Camera.frontCamName,
                    Constants.Camera.rearCamName,
                    Constants.Camera.crosshairCenterX,
                    Constants.Camera.crosshairCenterY, joystickOne);
            camera.init(Constants.Camera.camQuality, Constants.Camera.camSize);
            camDead = false;
        } catch(Exception e)
        {
            DriverStation.reportError("Camera error: ", true);
            camDead = true;
        }
        chassis = new Chassis(leftDriveTalonA, leftDriveTalonB,
                rightDriveTalonA, rightDriveTalonB);
        collector = new Collector(collectorTalon, ballDetectSwitch,
                joystickOne);
        shooter = new Shooter(shooterTalonLeft, shooterTalonRight,
                joystickOne, joystickZero, launchPad);
        timer = new Timer();

        throttleRangeAdjuster = Constants.Robot.throttleRangeAdjuster;
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
        chassisCommands();
        //cameraCommands();
        camera.idle();
        breacherCommands();
        collector.idle();
        shooter.idle();
    }

    // Drive system
    private void chassisCommands()
    {
        chassis.setMotorInversion(joystickZero);
        chassis.arcadeDrive(joystickOne);
    }

    // breacher stuff
    private void breacherCommands()
    {
        if(joystickZero.getRawButton(Constants.AssistantDriver.breacherUp))
        {
            breacherArm.raiseArmWO(1.0);
        } else if(joystickZero
                .getRawButton(Constants.AssistantDriver.breacherDown))
        {
            breacherArm.lowerArmWO(1.0);
        } else if(Math.abs((joystickZero.getY())) > 0.1){
            breacherArm.raiseArmWO(joystickZero.getY());
        }
        else
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
