package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

public class Shooter {
	CANTalon leftShooterTalon, rightShooterTalon;
	boolean m_ballReady;
	
	Shooter(CANTalon leftShooterTalonA, CANTalon rightShooterTalonA, DigitalInput ballLoad) {
		leftShooterTalon = leftShooterTalonA;
		rightShooterTalon = rightShooterTalonA;
		leftShooterTalon.reverseOutput(true);
		rightShooterTalon.reverseOutput(false);
	}
	void disable() {
		leftShooterTalon.set(0.0);
		rightShooterTalon.set(0.0);
	}
	void enable(double throttle) {
		double deadzone = 0.1;
		if(throttle > deadzone) {			
			leftShooterTalon.set(throttle);
			rightShooterTalon.set(throttle);
		} else {
			disable();
		}
	}
}
