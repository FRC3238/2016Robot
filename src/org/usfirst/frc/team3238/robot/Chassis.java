package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;

public class Chassis
{
    RobotDrive driveTrain;

    public int reverseDrive;
    boolean motorInversion;
    boolean noDoublePress;
    boolean gate2;
    double xValueZero;
    double yValueZero;
    double twistValueZero;
    double xValueOne;
    double yValueOne;
    double twistValueOne;
    double mappedX;
    double mappedTwist;
    double multiplier;
    boolean reversing;
    /*public static double[] zoneArray;
    public int deadZone = 0, monoZone = 3, squaredZone = 2, cubedZone = 1;*/
    public double	twistMultiplier = 0.5, deadZone = 0.15;
    Joystick joystickOne;
    CANTalon leftMotorControllerA, rightMotorControllerA,
    				leftMotorControllerB, rightMotorControllerB;
    Chassis(CANTalon leftMotorControllerA,
            CANTalon leftMotorControllerB, 
            CANTalon rightMotorControllerA,
            CANTalon rightMotorControllerB)
    {
    	//zoneArray = new double[4];
        reverseDrive = 1;
        motorInversion = false;
        noDoublePress = false;
        gate2 = true;
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
        reversing = false;
    }
    
    
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
        if (joystickOne.getThrottle()>.5) {
            motorInversion = false;
            reverseDrive = 1;
        } else {
            motorInversion = true;
            reverseDrive = -1;
        }
        System.out.println(reverseDrive);
        this.leftMotorControllerA.setInverted(motorInversion);
        this.leftMotorControllerB.setInverted(motorInversion);
        this.rightMotorControllerA.setInverted(motorInversion);
        this.rightMotorControllerB.setInverted(motorInversion);
    }
    void arcadeDrive(Joystick joystickZero) {
        double tw = joystickZero.getTwist();
        /*if(joystickZero.getTwist() <= 0.04) {
            tw = 0;
        }*/
//        if(multiplier == 0.85 && joystickOne.getRawButton(1) && !reversing){
//            multiplier = -.90;
//            reversing = true;
//        } 
//        else if(multiplier == -0.85 && joystickOne.getRawButton(1) && !reversing){
//            multiplier = .90;
//            reversing = true;
//        }
//        else{
//            reversing = false;
//        }
        driveTrain.arcadeDrive(joystickOne.getY() * multiplier, -(tw*0.78*reverseDrive), true);
    }
    
    void suckyAuto() {
        leftMotorControllerA.set(.5);
        leftMotorControllerB.set(.5);
        rightMotorControllerA.set(.5);
        rightMotorControllerB.set(.5);
    }
    
    void suckyAutoTooPointOh() {
        
    }
    
    void tankDrive(Joystick joystickZero, Joystick joystickOne) {
        if (joystickZero.getThrottle()<.5) {
            driveTrain.tankDrive(joystickZero, joystickOne);
        } else {
            driveTrain.tankDrive(joystickOne, joystickZero);
        }
    }
    
    void normal() {
        this.leftMotorControllerA.setInverted(false);
        this.leftMotorControllerB.setInverted(false);
        this.rightMotorControllerA.setInverted(false);
        this.rightMotorControllerB.setInverted(false);
        motorInversion = false;
    }

    void gDrive(Joystick jsZero) {
        double twist = jsZero.getTwist();
        double ex = jsZero.getX();
        double ey = jsZero.getY();
        if(twist <= deadZone) {
            twist = 0;
        }
    }
    
//    void ezDrive(double x, double y) { //still better than scrubdrive! Too bad it doesn't work
//    	config(0);
//    	if(Math.abs(x) < deadZone) {
//    	setInvertVariable(y, 0);
//    	} else {
//    	disable();
//    	}
//    }
//    void proDrive(double x, double y, double twist) {
//    	config(1); //Drivers choose whether they want x axis after testing
//    	double tY = Math.abs(y);
//    	String driveStatement;
//    	if(twist <= deadZone) { 
//    		twist = 0;
//    	} else {
//    		twist = twist*twistMultiplier;
//    	}
//    	
//    	for(int i = 2; i >= 0; i--) {
//    		if(tY <= zoneArray[i+1] && tY > zoneArray[i]) {
//    			setInvertVariable(Math.pow(tY, 3-i)*y/tY, twist);
//    		} else {
//    			disable();
//    		}
//    	}	
//
//    }
//    void setInvertVariable(double prime, double vert) {
//    	leftMotorControllerA.set(prime + vert);
//    	leftMotorControllerB.set(prime + vert);
//    	rightMotorControllerA.set(prime + vert);
//    	rightMotorControllerB.set(prime + vert);
//    } 
//    void setZones(double mZ, double sZ, double cZ, double dZ) {
//    	
//    	zoneArray[deadZone] = dZ;
//    	zoneArray[monoZone] = mZ;
//    	zoneArray[squaredZone] = sZ;
//    	zoneArray[cubedZone] = cZ;
//    	
//    }
//    //Configures zones for drive multipliers
//    void config(int config) {
//    	switch(config) {
//    	case 0: //scrubDrive config
//    		setZones(monoZone, 0.75, cubedZone, 0.15);
//    	break;
//    	case 1: //proDrive config
//    		setZones(1.0, 0.66, 0.36, 0.12);
//    	default: 
//    		break;
//    	}
//    }
    
    void disable() {
    	leftMotorControllerA.set(0);
    	leftMotorControllerB.set(0);
    	rightMotorControllerA.set(0);
    	rightMotorControllerB.set(0);
    }
}
