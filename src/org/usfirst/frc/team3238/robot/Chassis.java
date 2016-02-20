package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.CANTalon;

public class Chassis
{
    RobotDrive driveTrain;
    
    double mainX, mainY, mainTwist;
    
    double speedMult, turnMult;
    
    int flip;
    
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
    void idle(Joystick mainDriver) {
        setJoystickData(mainDriver);
        setMotorInversion(mainDriver);
        arcadeDrive();
    }
    void setMotorInversion(Joystick mainDriver) {
        if (mainDriver.getThrottle() > 0.0) {
            flip = 1;
        } else {
            flip = -1;
        }
    }
    void arcadeDrive() {
        double mappedTwist = (mainTwist*turnMult);
        double mappedY = mainY * speedMult;
        driveTrain.arcadeDrive(mappedY, mappedTwist, true);
    }
    void arcadeDrive(Joystick mainDriver, Joystick dial) {
        double twistMult = (dial.getRawAxis(0) + 1.5) / 1.7;
        SmartDashboard.putNumber("twist", dial.getRawAxis(0));
        SmartDashboard.putNumber("twistMult", twistMult);
        double mappedTwist = -mainDriver.getTwist()*twistMult;
        double mappedY = -mainDriver.getY() * turnMult;
        driveTrain.arcadeDrive(mappedY * flip, mappedTwist, true);
    }
    void arcadeDriveAuto(double y, double twist) {
        driveTrain.arcadeDrive(y, twist);
    }
    void invertMotors(boolean inv) {
        this.leftMotorControllerA.setInverted(inv);
        this.leftMotorControllerB.setInverted(inv);
        this.rightMotorControllerA.setInverted(inv);
        this.rightMotorControllerB.setInverted(inv);
    }
    public void disable() {
        this.leftMotorControllerA.set(0);
        this.leftMotorControllerB.set(0);
        this.rightMotorControllerA.set(0);
        this.rightMotorControllerB.set(0);
    }
}