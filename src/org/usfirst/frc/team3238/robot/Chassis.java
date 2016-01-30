package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

public class Chassis
{
    RobotDrive driveTrain;

    double xValue;
    double twistValue;
    double mappedX;
    double mappedTwist;
    public static double deadZone = 0.15, monoZone, squaredZone, cubedZone,
    		twistMultiplier = 0.5;
    SpeedController leftMotorControllerA, rightMotorControllerA,
    				leftMotorControllerB, rightMotorControllerB;
    Chassis(SpeedController leftMotorControllerA,
            SpeedController rightMotorControllerA, 
            SpeedController leftMotorControllerB,
            SpeedController rightMotorControllerB)
    {
    	this.leftMotorControllerA = leftMotorControllerA;
    	this.rightMotorControllerA = rightMotorControllerA;
    	this.leftMotorControllerB = leftMotorControllerB;
    	this.rightMotorControllerB = rightMotorControllerB;
    	this.leftMotorControllerA.setInverted(true);
    	this.leftMotorControllerB.setInverted(true);
        
        squaredZone = 0.75;
    }
    void setSquaredZone(double sq) {
    	squaredZone = sq;
    }
    void setJoystickData(double x, double twist)
    {
        xValue = x;
        twistValue = twist;
    }
    void ezDrive(double x, double y) { //still better than scrubdrive!
    	config(0);
    	if(Math.abs(x) < deadZone) {
    	setInvertVariable(y, 0);
    	} else {
    	disable();
    	}
    }
    void proDrive(double x, double y, double twist) {
    	config(1); //Drivers choose whether they want x axis after testing
    	double tY = Math.abs(y);
    	String driveStatement;
    	if(twist <= deadZone) { 
    		twist = 0;
    	} else {
    		twist = twist*twistMultiplier;
    	}
    	if(tY <= monoZone && tY > squaredZone) { // in monoZone
    		driveStatement = "mono";
    		setInvertVariable(y, twist);		
    	} else if(tY <= squaredZone && tY > cubedZone) { //squared
    		driveStatement = "squared";
    		setInvertVariable(tY*y, twist);
    	} else if(tY <= cubedZone && tY > deadZone) { //cubed
    		driveStatement = "cubed";		
    		setInvertVariable(tY*tY*y, twist);
    	} else { //RIP
    		driveStatement = "dead";
    		setInvertVariable(0, twist);
    	}

    }
    void setInvertVariable(double prime, double vert) {
    	leftMotorControllerA.set(prime + vert);
    	leftMotorControllerB.set(prime + vert);
    	rightMotorControllerA.set(prime - vert);
    	rightMotorControllerB.set(prime - vert);
    }
    void setZones(double mZ, double sZ, double cZ, double dZ) {
    	
    	deadZone = dZ;
    	monoZone = mZ;
    	squaredZone = sZ;
    	cubedZone = cZ;
    	
    }
    //Configures zones for drive multipliers
    void config(int config) {
    	switch(config) {
    	case 0: //scrubDrive config
    		setZones(monoZone, 0.75, cubedZone, 0.15);
    	break;
    	case 1: //proDrive config
    		setZones(1.0, 0.66, 0.36, 0.12);
    	default: 
    		break;
    	}
    }
    
    void disable() {
    	leftMotorControllerA.set(0);
    	leftMotorControllerB.set(0);
    	rightMotorControllerA.set(0);
    	rightMotorControllerB.set(0);
    }
}

