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
    CollectAndShoot ballControl;
    Joystick joystickZero, joystickOne;
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
                shooterTalonRight, ballDetectSwitch, joystickOne);
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
        ballControl.timerHE.reset();
        ballControl.timerHE.start();
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
        // ballControl.countRpm();
        ballControl.getRpm();
        SmartDashboard.putBoolean("HEONE", ballControl.hallEffectOne.get());
        SmartDashboard.putBoolean("HETWO", ballControl.hallEffectTwo.get());
        // ballControl.syncRPM(3.5);
        SmartDashboard.putNumber("RPM Left", ballControl.rpm[0]);
        // SmartDashboard.putNumber("RPM Left", ballControl.rpm);
        // SmartDashboard.putNumber("RPM Right", ballControl.rpm);
        // SmartDashboard.putNumber("RPM1", ballControl.rpmCount[1]);
        // SmartDashboard.putNumber("RPM0", ballControl.rpmCount[0]);
        // SmartDashboard.putNumber("Time", ballControl.timerHE.get());
        // SmartDashboard.putNumber("TimeV", 1.0/ballControl.timerHE.get());
        // SmartDashboard.putNumber("RPMShort", 1.0/ballControl.timerHE.get() *
        // ballControl.rpmCount[0]);
        // SmartDashboard.putNumber("RPMShort0", 1.0/ballControl.timerHE.get() *
        // ballControl.rpmCount[1]);
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
            if(joystickZero.getRawButton(Constants.Joystick.camChangeButton)
                    && camChanging)
            {
                camChanging = false;
                camera.changeCam();
            } else if(!joystickZero
                    .getRawButton(Constants.Joystick.camChangeButton))
            {
                camChanging = true;
            }
        }
        if(camDead)
            camera.idle();

        if(joystickZero.getRawButton(Constants.Joystick.camKillSwitch)
                && camDead)
            camDead = false;
        else if(joystickZero.getRawButton(Constants.Joystick.camKillSwitch)
                && !camDead)
            camDead = true;
    }

    // breacher stuff
    private void breacherCommands()
    {
        if(joystickZero.getRawButton(Constants.Joystick.breacherUpButton))
        {
            breacherArm.raiseArmWO(1.0);
        } else if(joystickZero
                .getRawButton(Constants.Joystick.breacherDownButton))
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
        SmartDashboard.putBoolean("HEONE", ballControl.hallEffectOne.get());
        SmartDashboard.putBoolean("HETWO", ballControl.hallEffectTwo.get());

    }

    public void testPeriodic()
    {
    }

}
