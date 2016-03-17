package org.usfirst.frc.team3238.robot;

/**
 * This is a generic proportional integral controller.
 * 
 * @author Team 3238
 */
public class PIVelocityController
{
    double m_cummulativeError = 0;
    double m_oldTime = 0;
    double m_oldMotorVal = 0;
    double m_pConstant;
    double m_iConstant;
    double m_error;
    double m_throttle;

    PIVelocityController(double pConstant, double iConstant, double error)
    {
        m_pConstant = pConstant;
        m_iConstant = iConstant;
        m_error = error;
        m_throttle = 1.0;
    }

    /**
     * Takes the current setpoint and sensorValue and calculates the necessary
     * response using the PI loop
     *
     * @param setpoint
     *            The desired sensorValue
     * @param sensorValue
     *            The current value of the sensor
     * @return m_returnPower The motor power calculated by the PI loop to get
     *         the sensorValue closer to the setpoint
     */
    double getMotorValue(double setpoint, double sensorValue)
    {
        double error = setpoint - sensorValue;
        m_cummulativeError += error;
        double time = System.currentTimeMillis();
        // If this is the first loop, set timeDifference to 0
        double timeDifference;
        if(m_oldTime == 0)
        {
            timeDifference = 0;
        } 
        else
        {
            timeDifference = time - m_oldTime;
        }
        double returnPower;
        if(error != 0)
        {
            returnPower = m_oldMotorVal + (error * m_pConstant + m_cummulativeError
                    * m_iConstant * timeDifference);
        }
        else
        {
            returnPower = 0;
        }
        // Set up the "old" values for the next loop
        m_oldTime = time;
        m_oldMotorVal = returnPower;
        return limitOutput(returnPower)*m_throttle;
    }
    
    boolean isTargetReached(double setPoint, double sensorValue)
    {
        if(Math.abs(setPoint - sensorValue) < m_error){
            return true;
        } else
        {
            return false;
        }
    }

    /**
     * Resets the cummulativeError and oldTime variables
     */
    void reinit()
    {
        m_cummulativeError = 0;
        m_oldTime = 0;
        m_oldMotorVal = 0;
    }
    
    void inputConstants(double pConstant, double iConstant)
    {
        m_pConstant = pConstant;
        m_iConstant = iConstant;
    }
    
    void setThrottle(double throttle)
    {
        m_throttle = throttle;
    }
    
    double limitOutput(double motorPower)
    {
        double returnPower;
        if(motorPower > 1.0)
        {
            returnPower = 1.0;
        }
        else if(motorPower < -1.0)
        {
            returnPower = -1.0;
        }
        else
        {
            returnPower = motorPower;
        }
        return returnPower;
    }
}