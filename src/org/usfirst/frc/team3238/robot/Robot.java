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
public class Robot extends IterativeRobot {
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
			collectorReverseButton, collectorManualButton, shootToggleButton;
	public double throttleRangeAdjuster;
	public static boolean camChangeBoolean0;

	public void defineConstants() throws java.io.FileNotFoundException {
	}

	public void robotInit() {
		try {
			ci = new ConstantInterpreter("kConstants.txt");

			armDetectTop = new DigitalInput(ci.retrieveInt("armDetectTopChannel"));
			armDetectBot = new DigitalInput(ci.retrieveInt("armDetectBotChannel"));
			leftDriveTalon = new CANTalon(ci.retrieveInt("leftDriveTalonPort"));
			rightDriveTalon = new CANTalon(ci.retrieveInt("rightDriveTalonPort"));

			breacherTalon = new CANTalon(ci.retrieveInt("breacherTalonPort"));

			collectorTalon = new CANTalon(ci.retrieveInt("collectorTalonPort"));

			breacherArm = new Breacher(breacherTalon, armDetectTop,armDetectBot);
			chassis = new Chassis(leftDriveTalon, rightDriveTalon);

			collector = new Collector(ci.retrieveInt("collectorTalonPort"),ci.retrieveInt("ballLimitSwitchPoint"));

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	public void autonomousInit() {
	}

	public void autonomousPeriodic() {
	}

	public void teleopInit() {
		try {
			throttleRangeAdjuster = ci.retrieveDouble("throttleRangeAdjuster");
			camChangeButton = ci.retrieveInt("camChangeButton");
			breacherTalonForwardButton = ci.retrieveInt("breacherTalonForwardButton");
			breacherTalonReverseButton = ci.retrieveInt("breacherTalonReverseButton");
			shootToggleButton = ci.retrieveInt("shootToggleButton");
			collectorForwardButton = ci.retrieveInt("collectorForwardButton");
			collectorReverseButton = ci.retrieveInt("collectorReverseButton");
			collectorManualButton = ci.retrieveInt("collectorManualButton");
			joystickZero = new Joystick(ci.retrieveInt("joystickZeroPort"));
			joystickOne = new Joystick(ci.retrieveInt("joystickOnePort"));
			camera = new Camera();
		} catch (Exception e) {
			e.printStackTrace();
		}
		camera.init();
		camChangeBoolean0 = true;
	}

	public void teleopPeriodic() {
		double throttleZero = joystickZero.getThrottle()
				+ throttleRangeAdjuster;
		// double throttleOne = joystickOne.getThrottle() +
		// throttleRangeAdjuster;
		chassisCommands();
		cameraCommands();
		// collectOrShootDivisor(throttleZero);
		collectorCommands(throttleZero);
		// breacherCommands(throttleOne);
	}

	// Toggles b/w collector and shooter
	private void collectOrShootDivisor(double throttleZero) {
		if (!joystickZero.getRawButton(shootToggleButton)) {
			shooter.disable();
			collectorCommands(throttleZero);
		} else {
			collector.disable();
			shooter.enable(throttleZero);
		}
	}

	// Drive system
	private void chassisCommands() {
		chassis.proDrive(joystickZero.getX(), joystickZero.getY(),
				joystickZero.getTwist());
	}

	// Camera stuff
	private void cameraCommands() {
		if (joystickZero.getRawButton(camChangeButton) && camChangeBoolean0) {
			camChangeBoolean0 = false;
			camera.changeCam();
		} else if (!joystickZero.getRawButton(camChangeButton)) {
			camChangeBoolean0 = true;
		}
		camera.stream();
	}

	// breacher stuff
	private void breacherCommands(double throttleOne) {
		if (joystickZero.getRawButton(breacherTalonForwardButton)) {
			breacherArm.raiseArm();
		} else if (joystickZero.getRawButton(breacherTalonReverseButton)) {
			breacherArm.lowerArm();
		} else {
			breacherArm.standby();
		}
	}

	// solely collector stuff
	private void collectorCommands(double throttleZero) {
		collector.proCollector(throttleZero, joystickZero.getRawButton(collectorForwardButton), 
				joystickZero.getRawButton(collectorReverseButton), joystickZero.getRawButton(collectorManualButton));
	}

	public void disabledPeriodic() {
		camera.stream();
	}

	public void testPeriodic() {
	}

}
