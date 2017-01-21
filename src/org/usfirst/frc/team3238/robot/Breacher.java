package org.usfirst.frc.team3238.robot;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * An object to control the breacher arm on our robot
 * 
 * @author FRC Team 3238
 * 
 * @version 1.0
 */ 
public class Breacher
{
    CANTalon breacherTalon;
    DigitalInput armDetectTop;
    Encoder armEncoder;
    
    double deadzone;

    int userInput;
    int motorEncoderPosition;

    double talonPower;
    double assistantX, assistantY, assistantThrottle;

    //Constructor without digitalinput
    Breacher(CANTalon breacherTalon)
    {
        this.breacherTalon = breacherTalon;
        armDetectTop = new DigitalInput(Constants.Breacher.armDetectTopPort);
        armEncoder = new Encoder(Constants.Breacher.armEncoderPortA,
                Constants.Breacher.armEncoderPortB);
    }
    //Constructor with digitalinput, armdetectbot is not on the robot and is unused
    Breacher(CANTalon breacherTalon, DigitalInput armDetectTop,
            DigitalInput armDetectBot)
    {
        this.breacherTalon = breacherTalon;
        this.armDetectTop = armDetectTop;
    }
    /**
     * Sets joystick data, originally intended to override the 'is this limitswitch pressed' check but was ultimately unnecessary 
     *
     * @param armDetectTop currently no functionality
     * @param armDetectBot currently no functionality
     * @param assistantDriver : the secondary joystick, sets internal values from the active joystick values
     */ 
    void setData(boolean armDetectTop, boolean armDetectBot,
            Joystick assistantDriver)
    {
        this.assistantThrottle = assistantDriver.getThrottle();
        this.assistantX = assistantDriver.getX();
        this.assistantY = assistantDriver.getY();
    }
    /**
     * Runs the breacher arm and goes through 'ifStick' method in order to check if it should move
     * 
     * @param assistantDriver : the secondary joystick, moves the arm based on the ifStick return
     */ 
    void run(Joystick assistantDriver)
    {
        talonPower = 0.0; //talonPower is a global variable for the speed of the breacher
        ifStick(assistantDriver.getY(), Constants.Breacher.deadzone);
        moveArm(talonPower); //talonPower is redefined in ifStick
    }
    /**
     * Checks if the breacher should move and if so assigns the value to 'talonPower'
     * 
     * @param in : the desired value from the alternate joystick's y axis
     * @param out : the 'deadzone', the joystick is not always zero'd out so this is a safeguard so that the breacher won't gradually move
     */
    void ifStick(double in, double out)
    {
        SmartDashboard.putBoolean("armDetectTop", armDetectTop.get()); //the value of the limit switch at the extent of the breacher's range
        if(Math.abs(in) > out)
        {
            if(!armDetectTop.get() && in < 0) //if the limit switch is pressed the breacher should not move because it could damage the motor
            {
                talonPower = in;
            }
            if(armDetectTop.get())
            {
                talonPower = in;
            }
        }
    }
    /**
     * Goes through the run function except with a set value, not taking it from the joystick, and removing the deadzone. Mainly
     * for autonomous purposes hence name 'auto'Raise
     * 
     * @param power the power of the breacher motor
     */ 
    void autoRaise(double power)
    {
        ifStick(power, 0.0);
        moveArm(talonPower);
    }
    /**
     * Goes through the autoRaise function except ignores safeguards, meaning it could be damaged by extending beyond what is allowed
     * by the limit switch
     * 
     * @param talonPower the power of the breacher motor
    */
    void moveArm(double talonPower)
    {
        SmartDashboard.putNumber("Variable_NAME", talonPower);
        SmartDashboard.putBoolean("ArmDetectTop", armDetectTop.get());
        breacherTalon.set(talonPower);
    }

    /**
     * Returns the current breacher speed
     * 
     * @return double between -1.0 and 1.0 of the power assigned to the breacher
     */ 
    public double getTalonSpeed()
    {
        return breacherTalon.get();
    }
}
