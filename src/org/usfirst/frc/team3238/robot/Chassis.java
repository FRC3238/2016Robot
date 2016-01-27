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
    double nullZone, monoZone, squaredZone, cubedZone, deadZone = 0.15;
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
    void ezDrive(double x, double y) {
    	if(Math.abs(x) < deadZone) {
    	leftMotorController.set(y);
    	rightMotorController.set(y);
    	} else {
    	leftMotorController.set(y*x);
    	rightMotorController.set(y*-x);
    	}
    }
    void proDrive() {
    	
    }
    void scrubDrive()
    {
    	mappedX = Math.abs(xValue) * xValue;  
    	
    	if(Math.abs(twistValue) < squaredZone) {
    		mappedTwist = 0.3333 * twistValue;
    	} else {
    		mappedTwist = 0.4444 * (Math.abs(twistValue) * twistValue);
    	}        

        driveTrain.arcadeDrive(mappedX, mappedTwist);
    }
}
