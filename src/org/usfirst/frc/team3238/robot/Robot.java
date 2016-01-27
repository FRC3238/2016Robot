package org.usfirst.frc.team3238.robot;

import java.io.FileNotFoundException;
import java.text.ParseException;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;

/*
 * @author Vladimir Lenin
 * @author Karl Marx
 * @author Friedrich Engels
 */
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
    public int camChangeButton, breacherTalonForwardButton,
            breacherTalonReverseButton, collectorForwardButton,
            collectorReverseButton, collectorManualButton,
            shootToggleButton;
    public double throttleRangeAdjuster;
    public static boolean camChangeBoolean0;
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
        try {
        camChangeBoolean0 = true;
        throttleRangeAdjuster = ci.retrieveDouble("throttleRangeAdjuster");
        camChangeButton = ci.retrieveInt("camChangeButton");
        breacherTalonForwardButton = ci.retrieveInt("breacherTalonForwardButton");
        breacherTalonReverseButton = ci.retrieveInt("breacherTalonReverseButton");
        shootToggleButton = ci.retrieveInt("shootToggleButton");
        collectorForwardButton = ci.retrieveInt("collectorForwardButton");
        collectorReverseButton = ci.retrieveInt("collectorReverseButton");
        collectorManualButton = ci.retrieveInt("collectorManualButton");
        
        final int joystickZeroPort = ci.retrieveInt("joystickZeroPort");
        final int joystickOnePort = ci.retrieveInt("joystickOnePort");
        final int leftDriveTalonPort = ci.retrieveInt("leftDriveTalonPort");
        final int rightDriveTalonPort = ci.retrieveInt("rightDriveTalonPort");
        final int breacherTalonPort = ci.retrieveInt("breacherTalonPort");
        final int collectorKillSwitchButton = ci.retrieveInt("collectorKillSwitchButton");
        final int collectorTalonPort = ci.retrieveInt("collectorTalonPort");
        final int ballLimitSwitchPort = ci.retrieveInt("ballLimitSwitchPoint");
        final int ballDetectChannel = ci.retrieveInt("ballDetectChannel");
        final int armDetectTopChannel = ci.retrieveInt("armDetectTopChannel");
        final int armDetectBotChannel = ci.retrieveInt("armDetectBotChannel");

        leftDriveTalon = new CANTalon(leftDriveTalonPort);
        rightDriveTalon = new CANTalon(rightDriveTalonPort);

        breacherTalon = new CANTalon(breacherTalonPort);
        
        collectorTalon = new CANTalon(collectorTalonPort);

        breacherArm = new Breacher(breacherTalon, armDetectTop, armDetectBot);
        chassis = new Chassis(leftDriveTalon, rightDriveTalon);

        collector = new Collector(collectorTalonPort, ballLimitSwitchPort);

        camera = new Camera();
        camera.init();

        joystickZero = new Joystick(joystickZeroPort);
        joystickOne = new Joystick(joystickOnePort);
        }catch(Exception e) {
        	e.printStackTrace();
        	System.exit(0);
        }
    }
    
	    //void autonomousInit()
	    //{
	    //}
    public void autonomousPeriodic()
    {
    }
    public void teleopInit()
    {
    }

    public void teleopPeriodic()
    {
        double throttleZero = joystickZero.getThrottle() + throttleRangeAdjuster;
        double throttleOne = joystickOne.getThrottle() + throttleRangeAdjuster;
        chassisCommands();
        cameraCommands();
        collectOrShootDivisor(throttleZero);
        breacherCommands(throttleOne);
    }
    
    private void collectOrShootDivisor(double throttleZero) {
        if(!joystickZero.getRawButton(shootToggleButton)) {
        	shooter.disable();
        	collectorCommands(throttleZero);
        } else {
        	collector.disable();
        	shooter.enable(throttleZero);
        }
    }
    
    private void chassisCommands() {
        //chassis.setJoystickData(joystickZero.getX(), joystickZero.getTwist());
        //chassis.scrubDrive();
    	chassis.ezDrive(joystickZero.getX(), joystickZero.getY());
    }
    
    
    private void cameraCommands() {
        if(joystickZero.getRawButton(camChangeButton) && camChangeBoolean0) {
            camChangeBoolean0 = false;
            camera.changeCam();
        } else if(!joystickZero.getRawButton(camChangeButton)){
            camChangeBoolean0 = true;
        }
        camera.stream();
    }
    
    private void breacherCommands(double throttleOne)
    {
        if(joystickZero.getRawButton(breacherTalonForwardButton))
        {
            breacherArm.raiseArm();
        } else if(joystickZero.getRawButton(breacherTalonReverseButton))
        {
            breacherArm.lowerArm();
        } else
        {
            breacherArm.standby();
        }
    }
    private void collectorCommands(double throttleZero) {
    	collector.setJoystickData(throttleZero,
                joystickZero.getRawButton(collectorForwardButton),
                joystickZero.getRawButton(collectorReverseButton),
                joystickZero.getRawButton(collectorManualButton));
    }

    public void disabledPeriodic()
    {
        camera.stream();
    }
    public void testPeriodic()
    {
    }

}
