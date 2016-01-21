package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.DigitalInput;

//import edu.wpi.first.wpilibj.Compressor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot
{
    final String defaultAuto = "Default";
    final String customAuto = "My Auto";
    String autoSelected;
    SendableChooser chooser;

    USBCamera cameraOne;
    Camera camera;
    
    Joystick joystickZero, joystickOne;
    
    RobotDrive robotDrive;
    
    Talon leftDriveTalon,    rightDriveTalon,
    		 leftBreacherTalon, rightBreacherTalon,
    	   	 collectorTalon;
    
    DigitalInput ballDetect;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        // SmartDashboard.putString("Test Statement",
        // "This is a test of the SmartDashboard.");
    	final int joystickZeroPort = 0,      joystickOnePort = 1;
    	final int leftDriveTalonPort = 1,    rightDriveTalonPort = 2,
    			  leftBreacherTalonPort = 3, rightBreacherTalonPort = 4,
    			  collectorTalonPort = 5;
    	final int ballDetectChannel = 0;
    	
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        

        camera = new Camera();
        camera.init();
        
        joystickZero = new Joystick(joystickZeroPort);
        joystickOne = new Joystick(joystickOnePort);
        
        leftDriveTalon = new Talon(1);
        rightDriveTalon = new Talon(2);
        leftBreacherTalon = new Talon(leftBreacherTalonPort);
        rightBreacherTalon = new Talon(rightBreacherTalonPort);
        collectorTalon = new Talon(collectorTalonPort);
        
    	//leftBreacherTalon.reverseSensor(true);
    	
    	ballDetect = new DigitalInput(ballDetectChannel);
    	
        robotDrive = new RobotDrive(leftDriveTalon, rightDriveTalon);
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
        autoSelected = (String) chooser.getSelected();
        // autoSelected = SmartDashboard.getString("Auto Selector",
        // defaultAuto);
        System.out.println("Auto selected: " + autoSelected);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic()
    {
        switch(autoSelected)
        {
            case customAuto:
                // Put custom auto code here
                break;
            case defaultAuto:
            default:
                // Put default auto code here
                break;
        }
    }

    /**
     * This function is called once at the start of operator control
     */
    public void teleopInit()
    {
        // SmartDashboard.putString("Test Statement", "Teleop is initialized.");
    }

    /**
     * This function is called periodically during operator control
     */
    public void teleopPeriodic()
    {
        double throttleZero = joystickZero.getThrottle() + .5;
        double throttleOne = joystickOne.getThrottle() + .5;
        
        robotDrive.arcadeDrive(-joystickZero.getY(), -joystickZero.getTwist(), true);
        
        if(joystickZero.getRawButton(2)) 
            camera.changeCam();
        camera.idle();
        
        //buttons that trigger the collector subsystem
        if (joystickZero.getRawButton(3)) {
        	//collects a ball continuously until limit switch is hit or kill switch through joystick
        	while (!ballDetect.get() || !joystickZero.getRawButton(11)) {
            	collectorTalon.set(throttleZero);
        	}
        } else if (joystickZero.getRawButton(5)) {
        	//spits out the ball
        	collectorTalon.set(-throttleZero);
        } else {
        	//standby
        	collectorTalon.set(0);
        }
        
        //buttons that trigger the breacher subsystem
        if (joystickZero.getRawButton(4)) {
        	//pulls the arm upwards
        	leftBreacherTalon.set(-throttleOne);
        	rightBreacherTalon.set(throttleOne);
        } else if (joystickZero.getRawButton(6)) {
        	//puts the arm down
        	leftBreacherTalon.set(throttleOne);
        	rightBreacherTalon.set(-throttleOne);
        } else {
        	//standby
        	leftBreacherTalon.set(0);
        	rightBreacherTalon.set(0);
        }
    }

    public void disabledPeriodic()
    {
        // camera.idle();
    }

    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic()
    {

    }

}