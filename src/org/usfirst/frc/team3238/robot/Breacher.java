package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;

public class Breacher
{
    CANTalon breacherTalon;
    DigitalInput armDetectTop, armDetectBot;

    boolean m_armDetectTop;
    boolean m_armDetectBot;
    double m_assistantDriverThrottle;
    double talonPower;
    Breacher(CANTalon breacherTalon) {
        this.breacherTalon = breacherTalon;
    }
    Breacher(CANTalon breacherTalon, DigitalInput armDetectTop, DigitalInput armDetectBot)
    {
        this.breacherTalon = breacherTalon;
        this.armDetectTop = armDetectTop;
        this.armDetectBot = armDetectBot;
        
    }

    void setData(boolean armDetectTop, boolean armDetectBot, double assistantDriverThrottle)
    {
        this.m_armDetectTop = armDetectTop;
        this.m_armDetectBot = armDetectBot;
        this.m_assistantDriverThrottle = assistantDriverThrottle;

    }
    
    void idle(Joystick assistantDriver) {
    	double assignment = 0.0;
    	if(assistantDriver.getRawButton(Constants.AssistantDriver.breacherUp))
        {
            assignment = 1.0;
        } else if(assistantDriver
                .getRawButton(Constants.AssistantDriver.breacherDown))
        {
            assignment = -1.0;
        } else if(Math.abs((assistantDriver.getY())) > 0.1)
        {
           assignment=assistantDriver.getY();
        } else
        {
            assignment = 0.0;
        }
    	moveArmWO(assignment);
    }

    void raiseArm()
    {
        if(!m_armDetectTop)
        {
            talonPower = m_assistantDriverThrottle;
            execute();
        } else {
        disable();
        }
    }
    
    void lowerArm()
    {
        if(!m_armDetectBot)
        {
            talonPower = -m_assistantDriverThrottle;
            execute();
        } else {
        disable();
        }
    }
    void moveArmWO(double throttle) {
    	talonPower = throttle;
    	execute();
    }
    
    void disable()
    {
    	moveArmWO(0.0);
    }

    void execute()
    {

        breacherTalon.set(talonPower);

    }

}
