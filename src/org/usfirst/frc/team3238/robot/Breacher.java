package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

public class Breacher
{
    CANTalon breacherTalon;
    DigitalInput armDetectTop, armDetectBot;

    boolean m_armDetectTop;
    boolean m_armDetectBot;
    double m_joystickZeroThrottle;
    double m_joystickOneThrottle;
    double talonPower;

    Breacher(CANTalon breacherTalon, DigitalInput armDetectTop, DigitalInput armDetectBot)
    {
        this.breacherTalon = breacherTalon;
        this.armDetectTop = armDetectTop;
        this.armDetectBot = armDetectBot;
        
    }

    void setData(boolean armDetectTop, boolean armDetectBot,
            double joystickZeroThrottle, double joystickOneThrottle)
    {
        m_armDetectTop = armDetectTop;
        m_armDetectBot = armDetectBot;
        m_joystickZeroThrottle = joystickZeroThrottle;
        m_joystickOneThrottle = joystickOneThrottle;

    }

    void raiseArm()
    {
        if(!m_armDetectTop)
        {
            talonPower = m_joystickOneThrottle;
            execute();
        } else {
        standby();
        }
    }

    void lowerArm()
    {
        if(!m_armDetectBot)
        {
            talonPower = -m_joystickOneThrottle;
            execute();
        } else {
        standby();
        }
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
