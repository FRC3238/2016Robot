package org.usfirst.frc.team3238.robot;

import java.io.FileNotFoundException;

import edu.wpi.first.wpilibj.IterativeRobot;
//import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
//import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot
{
    /*
     * final String defaultAuto = "Default"; final String customAuto =
     * "My Auto"; String autoSelected; SendableChooser chooser;
     */
    Camera camera;
    Chassis chassis;
    Collector collector;
    Joystick joystickZero, joystickOne;
    CANTalon leftDriveTalon, rightDriveTalon, leftBreacherTalon,
            rightBreacherTalon, collectorTalon;
    DigitalInput ballDetect;
    DigitalInput armDetectTop, armDetectBot;
    Breacher breacherArm;
    ConstantInterpreter ci;
    public int camChangeButton, breacherTalonForwardButton,
            breacherTalonReverseButton, collectorForwardButton,
            collectorReverseButton, collectorManualButton;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void defineConstants() throws java.io.FileNotFoundException
    {

    }

    public void robotInit()
    {
        try
        {
            ci = new ConstantInterpreter("kConstants.txt");
        } catch(FileNotFoundException e)
        {
            e.printStackTrace();
        }
        camChangeButton = ci.retrieveInt("camChangeButton");
        breacherTalonForwardButton = ci
                .retrieveInt("breacherTalonForwardButton");
        breacherTalonReverseButton = ci
                .retrieveInt("breacherTalonReverseButton");
        collectorForwardButton = ci.retrieveInt("collectorForwardButton");
        collectorReverseButton = ci.retrieveInt("collectorReverseButton");
        collectorManualButton = ci.retrieveInt("collectorManualButton");
        final int joystickZeroPort = ci.retrieveInt("joystickZeroPort");
        final int joystickOnePort = ci.retrieveInt("joystickOnePort");
        final int leftDriveTalonPort = ci.retrieveInt("leftDriveTalonPort");
        final int rightDriveTalonPort = ci.retrieveInt("rightDriveTalonPort");
        final int leftBreacherTalonPort = ci
                .retrieveInt("leftBreacherTalonPort");
        final int rightBreacherTalonPort = ci
                .retrieveInt("rightBreacherTalonPort");
        final int collectorTalonPort = ci.retrieveInt("collectorTalonPort");
        final int ballLimitSwitchPort = ci.retrieveInt("ballLimitSwitchPoint");
        final int ballDetectChannel = ci.retrieveInt("ballDetectChannel");
        final int armDetectTopChannel = ci.retrieveInt("armDetectTopChannel");
        final int armDetectBotChannel = ci.retrieveInt("armDetectBotChannel");

        leftDriveTalon = new CANTalon(leftDriveTalonPort);
        rightDriveTalon = new CANTalon(rightDriveTalonPort);
        leftBreacherTalon = new CANTalon(leftBreacherTalonPort);
        rightBreacherTalon = new CANTalon(rightBreacherTalonPort);
        collectorTalon = new CANTalon(collectorTalonPort);

        /*
         * chooser = new SendableChooser(); chooser.addDefault("Default Auto",
         * defaultAuto); chooser.addObject("My Auto", customAuto);
         * SmartDashboard.putData("Auto choices", chooser);
         */

        breacherArm = new Breacher(leftBreacherTalon, rightBreacherTalon,
                armDetectTop, armDetectBot);
        chassis = new Chassis(leftDriveTalon, rightDriveTalon);

        collector = new Collector(collectorTalonPort, ballLimitSwitchPort);

        camera = new Camera();
        camera.init();

        joystickZero = new Joystick(joystickZeroPort);
        joystickOne = new Joystick(joystickOnePort);

        leftBreacherTalon.reverseSensor(true);
    }

    /**
     * This autonomous (along with the chooser code above) shows how to select
     * between different autonomous modes using the dashboard. The sendable
     * chooser code works with the Java SmartDashboard. If you prefer the
     * LabVIEW Dashboard, remove all of the chooser code and uncomment the
     * getString line to get the auto name from the text box below the Gyro
     *
     * You can add additional auto modes by adding additional comparisons to the
     * switch structure below with additional strings. If using the
     * SendableChooser make sure to add them to the chooser code above as well.
     */
    public void autonomousInit()
    {
        // autoSelected = (String) chooser.getSelected();
        // autoSelected = SmartDashboard.getString("Auto Selector",
        // defaultAuto);
        // System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        /*
         * switch(autoSelected) { case customAuto: // Put custom auto code here
         * break; case defaultAuto: default: // Put default auto code here
         * break; }
         */
    }

    /**
     * This function is called once at the start of operator control
     */
    public void teleopInit()
    {

    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        double throttleZero = joystickZero.getThrottle() + .5;
        double throttleOne = joystickOne.getThrottle() + .5;

        chassis.setJoystickData(joystickZero.getX(), joystickZero.getTwist());
        chassis.idle();

        collector.setJoystickData(joystickZero.getThrottle(),
                joystickZero.getRawButton(collectorForwardButton),
                joystickZero.getRawButton(collectorReverseButton),
                joystickZero.getRawButton(collectorManualButton));
        collector.idle();

        if(joystickZero.getRawButton(camChangeButton))
            camera.changeCam();
        camera.idle();

        // if(joystickZero.getRawButton(3))
        // {
        // collectorTalon.set(throttleZero);
        // } else if(joystickZero.getRawButton(5))
        // {
        // collectorTalon.set(-throttleZero);
        // } else
        // {
        // collectorTalon.set(0);
        // }
        collectorCommands(throttleZero);
        breacherCommands(throttleOne);
    }

    private void breacherCommands(double throttleOne)
    {
        if(joystickZero.getRawButton(breacherTalonForwardButton))
        {
            // pulls the arm upwards
            breacherArm.raiseArm();
        } else if(joystickZero.getRawButton(breacherTalonReverseButton))
        {
            // puts the arm down
            // leftBreacherTalon.set(-throttleOne);
            breacherArm.lowerArm();
        } else
        {
            // standby
            breacherArm.standby();
        }
    }

    private void collectorCommands(double throttleZero)
    {
        if(joystickZero.getRawButton(collectorForwardButton))
        {
            // collects a ball continuously until limit switch is hit or kill
            // switch through joystick
            while(!ballDetect.get() || !joystickZero.getRawButton(11))
            {
                collectorTalon.set(throttleZero);
            }
        } else if(joystickZero.getRawButton(collectorReverseButton))
        {
            // spits out the ball
            collectorTalon.set(-throttleZero);
        } else
        {
            // standby
            collectorTalon.set(0);
        }
    }

    public void disabledPeriodic()
    {
        camera.idle();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {

    }

}