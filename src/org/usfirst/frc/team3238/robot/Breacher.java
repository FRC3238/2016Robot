package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class Breacher {
	CANTalon breacherTalon;
	DigitalInput armDetectTop, armDetectBot;

	boolean m_armDetectTop;
	boolean m_armDetectBot;
	double m_assistantDriverThrottle;
	double talonPower;
	double assistantX, assistantY, assistantThrottle;
	Breacher(CANTalon breacherTalon) {
		this.breacherTalon = breacherTalon;
	}

	Breacher(CANTalon breacherTalon, DigitalInput armDetectTop,
			DigitalInput armDetectBot) {
		this.breacherTalon = breacherTalon;
		this.armDetectTop = armDetectTop;
		this.armDetectBot = armDetectBot;

	}

	void setData(boolean armDetectTop, boolean armDetectBot,
			Joystick assistantDriver) {
		this.m_armDetectTop = armDetectTop;
		this.m_armDetectBot = armDetectBot;
		this.assistantThrottle = assistantDriver.getThrottle();
		this.assistantX = assistantDriver.getX();
		this.assistantY = assistantDriver.getY();

	}

	void run(Joystick assistantDriver) {
		talonPower = 0.0;
		ifStick(Math.abs(assistantDriver.getY()), 0.1);
		ifStick(assistantDriver, Constants.AssistantDriver.breacherDown, -1.0);
		ifStick(assistantDriver, Constants.AssistantDriver.breacherUp, 1.0);

		moveArmWO(talonPower);
	}

	void ifStick(double in, double out) {
		if (in > out) {
			talonPower = in;
		}
	}

	void ifStick(Joystick assistantDriver, int button, double assign) {

		if (assistantDriver.getRawButton(button)) {
			talonPower = assign;
		}
	}

	void raiseArm() {
		if (!m_armDetectTop) {
			talonPower = m_assistantDriverThrottle;
			execute();
		} else {
			disable();
		}
	}

	void lowerArm() {
		if (!m_armDetectBot) {
			talonPower = -m_assistantDriverThrottle;
			execute();
		} else {
			disable();
		}
	}

	void moveArmWO(double throttle) {
		talonPower = throttle;
		execute();
	}

	void disable() {
		moveArmWO(0.0);
	}

	void execute() {

		breacherTalon.set(talonPower);

	}

}
