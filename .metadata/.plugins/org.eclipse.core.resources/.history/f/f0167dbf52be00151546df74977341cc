package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.RobotDrive;
//import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.vision.USBCamera;

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
    RobotDrive robotDrive;
    // DoubleSolenoid leftSolenoid, rightSolenoid;
    CANTalon leftTalon, rightTalon;
    Joystick stick;
    USBCamera cameraOne;
    Camera camera;

    // Compressor compressor;

    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
    public void robotInit()
    {
        // compressor = new Compressor();
        // compressor.setClosedLoopControl(true);
        chooser = new SendableChooser();
        chooser.addDefault("Default Auto", defaultAuto);
        chooser.addObject("My Auto", customAuto);
        SmartDashboard.putData("Auto choices", chooser);
        // SmartDashboard.putString("Test Statement",
        // "This is a test of the SmartDashboard.");
        // leftSolenoid = new DoubleSolenoid(0,1);
        // rightSolenoid = new DoubleSolenoid(2,3);
        leftTalon = new CANTalon(1);
        rightTalon = new CANTalon(2);
        robotDrive = new RobotDrive(leftTalon, rightTalon);
        stick = new Joystick(0);
        camera = new Camera();
        camera.init();
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
        robotDrive.arcadeDrive(-stick.getY(), -stick.getTwist(), true);
        // if(stick.getRawButton(1)){
        // leftSolenoid.set(DoubleSolenoid.Value.kForward);
        // rightSolenoid.set(DoubleSolenoid.Value.kForward);
        // }
        // if(stick.getRawButton(2)){
        // leftSolenoid.set(DoubleSolenoid.Value.kReverse);
        // rightSolenoid.set(DoubleSolenoid.Value.kReverse);
        // }

        if(stick.getRawButton(3))
            camera.changeCam();
        camera.idle();
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