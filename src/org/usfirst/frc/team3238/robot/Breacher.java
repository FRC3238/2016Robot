package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;

public class Breacher
{
    CANTalon m_breacherTalon;
    DigitalInput armDetectTop, armDetectBot;

    boolean m_armDetectTop;
    boolean m_armDetectBot;
    double m_joystickZeroThrottle;
    double m_joystickOneThrottle;
    double leftTalonPower;
    double rightTalonPower;

    Breacher(CANTalon breacherTalon, DigitalInput armDetectTop, DigitalInput armDetectBot)
    {
        m_breacherTalon = breacherTalon;
        leftBreacherTalon.reverseOutput(true);
        rightBreacherTalon.reverseOutput(false);
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
            leftTalonPower = m_joystickOneThrottle;
            rightTalonPower = m_joystickOneThrottle;
            execute();
        } else {
        standby();
        }
    }

    void lowerArm()
    {
        if(!m_armDetectBot)
        {
            leftTalonPower = -m_joystickOneThrottle;
            rightTalonPower = -m_joystickOneThrottle;
            execute();
        } else {
        standby();
        }
    }

    void standby()
    {
        leftTalonPower = 0;
        rightTalonPower = 0;
        leftBreacherTalon.set(leftTalonPower);
        rightBreacherTalon.set(rightTalonPower);
    }

    void execute()
    {

        leftBreacherTalon.set(leftTalonPower);
        rightBreacherTalon.set(rightTalonPower);

    }

}
