package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

    Breacher(CANTalon breacherTalon)
    {
        this.breacherTalon = breacherTalon;
        armDetectTop = new DigitalInput(Constants.Breacher.armDetectTopPort);
        armEncoder = new Encoder(Constants.Breacher.armEncoderPortA,
                Constants.Breacher.armEncoderPortB);
    }

    Breacher(CANTalon breacherTalon, DigitalInput armDetectTop,
            DigitalInput armDetectBot)
    {
        this.breacherTalon = breacherTalon;
        this.armDetectTop = armDetectTop;
    }

    void setData(boolean armDetectTop, boolean armDetectBot,
            Joystick assistantDriver)
    {
        this.assistantThrottle = assistantDriver.getThrottle();
        this.assistantX = assistantDriver.getX();
        this.assistantY = assistantDriver.getY();
    }

    void run(Joystick assistantDriver)
    {
        talonPower = 0.0;
        ifStick(assistantDriver, Constants.AssistantDriver.breacherDownButton);
        ifStick(assistantDriver.getY(), Constants.Breacher.deadzone);
        moveArm(talonPower);
    }

    void ifStick(double in, double out)
    {
        SmartDashboard.putBoolean("armDetectTop", armDetectTop.get());
        if(Math.abs(in) > out)
        {
            if(!armDetectTop.get() && in < 0) {
                talonPower = in;
            }
            if(armDetectTop.get()) {
                talonPower = in;
            }
        }
    }
    
    void ifStick(Joystick assistantDriver, int button)
    {
        if(assistantDriver.getRawButton(button))
        {
            userInput = button;
            getPosition();
            setArmPosition(motorEncoderPosition);
        }
    }
    
    void moveArm(double talonPower)
    {
        breacherTalon.set(talonPower);
    }
    
    void getPosition()
    {
        switch(userInput)
        {
            case 6:
                motorEncoderPosition = Constants.Breacher.encoderValueMidTop;
                break;
            case 5:
                motorEncoderPosition = Constants.Breacher.encoderValueMidTop;
                break;
            case 3:
                motorEncoderPosition = Constants.Breacher.encoderValueMidBot;
                break;
            case 4:
                motorEncoderPosition = Constants.Breacher.encoderValueBot;
                break;
            default:
                break;
        }
    }
    
    void setArmPosition(int position)
    {
        if(armEncoder.get() < position)
        {
            moveArm(talonPower);
        } else if(armEncoder.get() > position)
        {
            moveArm(-talonPower);
        } else
        {
        }
        resetEncoder();
    }

    private void resetEncoder()
    {
        if(armDetectTop.get())
        {
            armEncoder.reset();
        }
    }
}
