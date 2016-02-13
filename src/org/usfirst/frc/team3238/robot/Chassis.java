package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;

public class Chassis
{
    RobotDrive driveTrain;


    boolean motorInversion;

    double xValueZero, yValueZero, twistValueZero, xValueOne, yValueOne, twistValueOne, mappedX, mappedTwist, multiplier;

    
    public double	twistMultiplier = 0.5, deadZone = 0.15;
    Joystick joystickOne;
    CANTalon leftMotorControllerA, rightMotorControllerA,
    				leftMotorControllerB, rightMotorControllerB;
    Chassis(CANTalon leftMotorControllerA,
            CANTalon leftMotorControllerB, 
            CANTalon rightMotorControllerA,
            CANTalon rightMotorControllerB)
    {
        motorInversion = false;

    	this.leftMotorControllerA = leftMotorControllerA;
    	this.rightMotorControllerA = rightMotorControllerA;
    	this.leftMotorControllerB = leftMotorControllerB;
    	this.rightMotorControllerB = rightMotorControllerB;
    	this.leftMotorControllerA.setInverted(false);
    	this.leftMotorControllerB.setInverted(false);
    	this.rightMotorControllerA.setInverted(false);
    	this.rightMotorControllerB.setInverted(false);
        driveTrain = new RobotDrive(leftMotorControllerA, leftMotorControllerB,
                rightMotorControllerA, rightMotorControllerB);
        joystickOne = new Joystick(1);
        multiplier = 0.90;
    }
    
    void finalDrive() {}
    
    void setJoystickData(Joystick joystickZero, Joystick joystickOne)
    {
        xValueZero = joystickZero.getX();
        yValueZero = joystickZero.getY();
        twistValueZero = joystickZero.getTwist();
        
        xValueOne = joystickOne.getX();
        yValueOne = joystickOne.getY();
        twistValueOne = joystickOne.getTwist();

        setMotorInversion(joystickZero);
    }
    
    void setMotorInversion(Joystick joystickZero) {
    	int reverseDrive;
        if (joystickZero.getThrottle() > 0.5) {
            motorInversion = false;
            controllerInversion();
        } else {
            motorInversion = true;
            controllerInversion();
        }
    }
    
    void controllerInversion() {
        this.leftMotorControllerA.setInverted(motorInversion);
        this.leftMotorControllerB.setInverted(motorInversion);
        this.rightMotorControllerA.setInverted(motorInversion);
        this.rightMotorControllerB.setInverted(motorInversion);
    }
    
    void arcadeDrive(Joystick joystickZero) {
        double tw = joystickZero.getTwist();
        
        driveTrain.arcadeDrive(joystickOne.getY() * multiplier, -(tw*0.78), false);
    }
    
    
    void tankDrive(Joystick joystickZero, Joystick joystickOne) {
        if (joystickZero.getThrottle() < 0.5) {
            driveTrain.tankDrive(joystickZero, joystickOne);
        } else {
            driveTrain.tankDrive(joystickOne, joystickZero);
        }
    }

    void disable() {
    	leftMotorControllerA.set(0);
    	leftMotorControllerB.set(0);
    	rightMotorControllerA.set(0);
    	rightMotorControllerB.set(0);
    }
}
