package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;

public class Chassis
{
    RobotDrive driveTrain;
    
    double mainX, mainY, mainTwist;
    public String state = "disabled";
    double speedMult, turnMult;
    public double[] powerZones = {0.04, 0.2, 0.4, 1.0};
    
    CANTalon leftMotorControllerA, rightMotorControllerA,
    				leftMotorControllerB, rightMotorControllerB;
    Chassis(CANTalon leftMotorControllerA,
            CANTalon leftMotorControllerB, 
            CANTalon rightMotorControllerA,
            CANTalon rightMotorControllerB)
    {
    	this.leftMotorControllerA = leftMotorControllerA;
    	this.rightMotorControllerA = rightMotorControllerA;
    	this.leftMotorControllerB = leftMotorControllerB;
    	this.rightMotorControllerB = rightMotorControllerB;
    	invertMotors(false);
        driveTrain = new RobotDrive(leftMotorControllerA, leftMotorControllerB,
                rightMotorControllerA, rightMotorControllerB);
        speedMult = Constants.Chassis.yMultiplier;
        turnMult = Constants.Chassis.twistMultiplier;
    }
    
    
    void setJoystickData(Joystick mainDriver)
    {
        mainX = mainDriver.getX();
        mainY = mainDriver.getY();
        mainTwist = mainDriver.getTwist();
      
        setMotorInversion(mainDriver);
    }
    
    void run(Joystick mainDriver) {
    	setJoystickData(mainDriver);
    	setMotorInversion(mainDriver);
    	//arcadeDrive();
    	motorcadeDrive();
    }
    
    void setMotorInversion(Joystick mainDriver) {
        if (mainDriver.getThrottle() > 0.5) {
            invertMotors(false);
        } else {
            invertMotors(true);
        }
    }
    void motorcadeDrive() {
    	double mappedY = -(mainY*speedMult);
    	double mappedTwist = mainTwist * turnMult;
    	if(Math.abs(mainTwist) < powerZones[0]) {
    		mappedTwist = 0;
    	}
    	if(Math.abs(mainY) < powerZones[0]) {
    		mappedY = 0;
    	}
    	for(int i = 1; i < powerZones.length - 1; i++) {
    		if(mappedY != 0 && Math.abs(mappedY) > powerZones[i] && Math.abs(mappedY) < powerZones[i + 1]) {
    			mappedY = mappedY * Math.pow(Math.abs(mappedY), powerZones.length - i);
    			debugState(i);
    		}
    		if(mappedTwist != 0 && Math.abs(mainTwist) > powerZones[i] && Math.abs(mappedTwist) < powerZones[i + 1]) {
    			mappedTwist = mappedTwist * Math.pow(Math.abs(mappedTwist), powerZones.length - i);
    		}
    	}
    	setMotors(mappedY, mappedTwist);

    }
    void debugState(int i) {
    	if(i == 1) {
    		state = "cubed";
    	} else if(i==2) {
    		state = "squared";
    	} else if(i==3) {
    		state = "mono";
    	}
    }
    void arcadeDrive() {
    	double mappedY = -(mainY*speedMult);
    	double mappedTwist = mainTwist * turnMult;
    	driveTrain.arcadeDrive(mappedY, mappedTwist, true);
    }
    void arcadeDrive(Joystick mainDriver) {
        double mappedY = -(mainDriver.getTwist()*speedMult);
        double mappedTwist = mainDriver.getY() * turnMult;
        driveTrain.arcadeDrive(mappedY, mappedTwist, true);
    }
    void setMotors(double spd) {
    	this.leftMotorControllerA.set(spd);
    	this.leftMotorControllerB.set(spd);
    	this.rightMotorControllerA.set(spd);
    	this.rightMotorControllerB.set(spd);
    }
    void setMotors(double spd, double turn) {
    	this.leftMotorControllerA.set(spd+turn);
    	this.leftMotorControllerB.set(spd+turn);
    	this.rightMotorControllerA.set(spd-turn);
    	this.rightMotorControllerB.set(spd-turn);
    }
    void invertMotors(boolean inv) {
    	this.leftMotorControllerA.setInverted(inv);
        this.leftMotorControllerB.setInverted(inv);
        this.rightMotorControllerA.setInverted(inv);
        this.rightMotorControllerB.setInverted(inv);
    }
    public void disable() {
    	setMotors(0.0);
    }
}
