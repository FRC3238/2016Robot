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
    Autonomous auto;
    Camera camera;
    Chassis chassis;
    CollectAndShoot ballControl;
    Joystick joystickZero, joystickOne;
    CANTalon leftDriveTalonA, leftDriveTalonB, rightDriveTalonA,
            rightDriveTalonB;
    CANTalon breacherTalon;
    CANTalon collectorTalon;
    CANTalon shooterTalonLeft, shooterTalonRight;
    DigitalInput armDetectTop, armDetectBot, centerDetect, holdDetect;
    DigitalInput ballDetectSwitch;
    Timer timer;
    public double throttleRangeAdjuster;
    public static boolean camChanging, allah;
    public static boolean camDead;

    public void robotInit()
    {
        joystickZero = new Joystick(Constants.Robot.joystickZeroPort);
        joystickOne = new Joystick(Constants.Robot.joystickOnePort);

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
        centerDetect = new DigitalInput(Constants.CollectAndShoot.centerDetectChannel);
        holdDetect = new DigitalInput(Constants.CollectAndShoot.holdPosDetectChannel);
        breacherArm = new Breacher(breacherTalon);
        try
        {
            camera = new Camera(Constants.Camera.frontCamName,
                    Constants.Camera.rearCamName,
                    Constants.Camera.crosshairCenterX,
                    Constants.Camera.crosshairCenterY);
            camera.init(Constants.Camera.camQuality, Constants.Camera.camSize);
            camDead = false;
        } catch(Exception e)
        {
            DriverStation.reportError("Camera error: ", true);
            camDead = true;
        }
        chassis = new Chassis(leftDriveTalonA, leftDriveTalonB,
                rightDriveTalonA, rightDriveTalonB);
        ballControl = new CollectAndShoot(collectorTalon, shooterTalonLeft,
                shooterTalonRight, ballDetectSwitch, centerDetect, holdDetect, joystickOne);
        timer = new Timer();

        throttleRangeAdjuster = Constants.Robot.throttleRangeAdjuster;
    }

    public void autonomousInit()
    {
    	allah = true;
    }

    public void autonomousPeriodic()
    {
    	if(allah)
    	    auto.bruteAutonomous();
    	allah = false;
    }

    public void teleopInit()
    {
        camChanging = true;
    }

    public void teleopPeriodic()
    {
        chassisCommands();
        cameraCommands();
        breacherCommands();
        ballControlCommands();
    }

    private void ballControlCommands()
    {
        ballControl.idle();
       
    }

    // Drive system
    private void chassisCommands()
    {
        chassis.setMotorInversion(joystickZero);
        chassis.arcadeDrive(joystickOne);
    }

    // Camera stuff
    private void cameraCommands()
    {
        if(!camDead)
        {
            if(joystickZero.getRawButton(Constants.Camera.camChangeButton)
                    && camChanging)
            {
                camChanging = false;
                camera.changeCam();
            } else if(!joystickZero
                    .getRawButton(Constants.Camera.camChangeButton))
            {
                camChanging = true;
            }
        }
        if(camDead)
            camera.idle();

        if(joystickZero.getRawButton(Constants.Camera.camKillSwitch)
                && camDead)
            camDead = false;
        else if(joystickZero.getRawButton(Constants.Camera.camKillSwitch)
                && !camDead)
            camDead = true;
    }

    // breacher stuff
    private void breacherCommands()
    {
        if(joystickZero.getRawButton(Constants.Breacher.breacherUpButton))
        {
            breacherArm.raiseArmWO(1.0);
        } else if(joystickZero
                .getRawButton(Constants.Breacher.breacherDownButton))
        {
            breacherArm.lowerArmWO(1.0);
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
