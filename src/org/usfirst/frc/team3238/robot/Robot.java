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

/**
 * The main WPI provided class, iterative and holding the configuration of subsystems and attempting to integrate them effectively.
 * 
 * @author FRC Team 3238
 * 
 * @version 1.0
 */ 
public class Robot extends IterativeRobot
{ //Lots of configuration, possible to use static classes instead for most of these.
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
    NetworkTable netTab; //a way of transmitting values
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
            netTab.putBoolean("run", true); //turns vision processing on, although does not work in autonomous
            try
            {
                camera = new Camera(assistantJoystick);
                camera.init(Constants.Camera.camQuality,
                        Constants.Camera.camSize);
                camDead = false; //camera available
            } catch(Exception e)
            {
                DriverStation.reportError("Camera error: ", true);
                camDead = true; //if error then don't turn on the camera ever
            }
            chassis = new Chassis(leftDriveTalonA, leftDriveTalonB,
                    rightDriveTalonA, rightDriveTalonB);
            shooter = new Shooter(shooterTalonLeft, shooterTalonRight,
                    mainJoystick, assistantJoystick, launchPad);
            collector = new Collector(collectorTalon, ballDetectSwitch,
                    shooter, mainJoystick, assistantJoystick, launchPad);
            vision = new Vision(chassis, shooter, collector); //vision processing
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
        netTab.putBoolean("run", true); //vision processing
    }

    public void autonomousPeriodic()
    {
        // auto.switchingAuto();
        auto.autoRun(); //the iterative case statement in the autonomous class
        netTab.putBoolean("run", true); //vision processing
    }

    public void teleopInit()
    {
        collector.init(); //allow driver to use the collector, set to automatic
        shooter.reset();
        SmartDashboard.putNumber("DB/Slider 0", 0); //reset the DB/Slider that is preset before matches to determine autonomous routine
        netTab.putBoolean("run", false); //turn off vision processing
    }

    public void teleopPeriodic()
    {
        if(mainJoystick.getRawButton(11)) //tries to align with the goal using vision processing, doesn't work
        {
            vision.teleVision(); 
            camera.stream();
            breacherArm.run(assistantJoystick);
            collector.idle();
            shooter.idle(collector.isCollecting());
        } else 
        { //simplified commands for each subsytem
            chassisCommands(); //controls drive system
            camera.stream(); //streams cam feed
            breacherArm.run(assistantJoystick); //moves breacher with values from assistantjoystick
            collector.idle(); //move like collector.run()
            shooter.idle(collector.isCollecting()); /*more like shooter.run() using if the collector is collecting as parameter 
            to determine if it should shut off so it doesn't fire immdiately after collection*/
            netTab.putBoolean("run", false); //turn off vision processing
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
        camera.stream(); //still stream camera
        auto.init(); //reset auto
        chassis.setPower(0.0); //disable everything else
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
