package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

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
        m_armDetectTop = armDetectTop;
        m_armDetectBot = armDetectBot;
        m_assistantDriverThrottle = assistantDriverThrottle;

    }
    
    void idle(Joystick assistantDriver) {
    	if(assistantDriver.getRawButton(Constants.AssistantDriver.breacherUp))
        {
            raiseArmWO(1.0);
        } else if(assistantDriver
                .getRawButton(Constants.AssistantDriver.breacherDown))
        {
            lowerArmWO(1.0);
        } else if(Math.abs((assistantDriver.getY())) > 0.1)
        {
            raiseArmWO(assistantDriver.getY());
        } else
        {
            standby();
        }
    }

    void raiseArm()
    {
        if(!m_armDetectTop)
        {
            talonPower = m_assistantDriverThrottle;
            execute();
        } else {
        standby();
        }
    }
    
    void lowerArm()
    {
        if(!m_armDetectBot)
        {
            talonPower = -m_assistantDriverThrottle;
            execute();
        } else {
        standby();
        }
    }
    void lowerArmWO(double jsOneThrottle) {
        
        talonPower = -jsOneThrottle;
        execute();
    }
    void raiseArmWO(double jsOneThrottle) {
        talonPower = jsOneThrottle;
        execute();
    }
    void standby()
    {
    	breacherTalon.set(0);
    }

    void execute()
    {

        breacherTalon.set(talonPower);

    }

}
