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
    SpeedController leftMotorController, rightMotorController;
    Chassis(SpeedController leftMotorController,
            SpeedController rightMotorController)
    {
    	this.leftMotorController = leftMotorController;
    	this.rightMotorController = rightMotorController;
    	this.leftMotorController.setInverted(true);
        driveTrain = new RobotDrive(leftMotorController, rightMotorController);
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
    	leftMotorController.set(y);
    	rightMotorController.set(y);
    	} else {
    	leftMotorController.set(y*x);
    	rightMotorController.set(y*-x);
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
    			leftMotorController.set(y+twist);
    			rightMotorController.set(y-twist); 	
    	} else if(tY <= squaredZone && tY > cubedZone) { //squared
    		driveStatement = "squared";
    			leftMotorController.set(tY*y+twist);
    			rightMotorController.set(tY*y-twist);
    	} else if(tY <= cubedZone && tY > deadZone) { //cubed
    		driveStatement = "cubed";		
    			leftMotorController.set(tY*tY*y+twist);
    			rightMotorController.set(tY*tY*y-twist);
    	} else { //RIP
    		driveStatement = "dead";
    		leftMotorController.set(twist);
    		rightMotorController.set(-twist);
    	}

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
    void scrubDrive() 
    {
    	config(0);
    	mappedX = Math.abs(xValue) * xValue;  
    	
    	if(Math.abs(twistValue) < squaredZone) {
    		mappedTwist = 0.3333 * twistValue;
    	} else {
    		mappedTwist = 0.4444 * (Math.abs(twistValue) * twistValue);
    	}        

        driveTrain.arcadeDrive(mappedX, mappedTwist);
    }
    void disable() {
    	leftMotorController.set(0);
    	rightMotorController.set(0);
    }
}
