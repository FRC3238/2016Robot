package org.usfirst.frc.team3238.robot;

/**
 * This is a generic proportional integral controller.
 * 
 * @author FRC Team 3238
 */
public class PIController
{
    double m_cummulativeError = 0; //ignore the extra 'm'
    double m_oldTime = 0;
    double m_pConstant;
    double m_iConstant;
    double m_error;
    double m_throttle;

    /**
     * PI Constructor
     * 
     * @param pConstant the 'proportionality' constant
     * @param iConstant the 'integral' constant
     * @param error the current error
     */ 
    PIController(double pConstant, double iConstant, double error)
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
        } else
        {
            timeDifference = time - m_oldTime;
        }
        double returnPower;
        if(error != 0)
        {
            returnPower = error * m_pConstant + m_cummulativeError
                    * m_iConstant * timeDifference;
        } else
        {
            returnPower = 0;
        }
        // Set up the "old" values for the next loop
        m_oldTime = time;
        return limitOutput(returnPower) * m_throttle;
    }

    boolean isAligned(double setPoint, double sensorValue)
    {
        return Math.abs(setPoint - sensorValue) < m_error
        
    }

    /**
     * Resets the cummulativeError and oldTime variables
     */
    void reinit()
    {
        m_cummulativeError = 0;
        m_oldTime = 0;
    }

    /**
     * mutator method for the proportionality and integral constants
     */ 
    void inputConstants(double pConstant, double iConstant)
    {
        m_pConstant = pConstant;
        m_iConstant = iConstant;
    }
    /**
     * mutator method for throttle which is multiplied by the motor speed to change turn power
     */ 
    void setThrottle(double throttle)
    {
        m_throttle = throttle;
    }
    /**
     * Pretty complicated and unnecessary math but in the end returns a value that is between -1.0 and 1.0 although it doesn't matter, 
     * because it's used to utilize motor power in the scope of the allowed power assignment but extending beyond this scope will
     * cause it to be limited anyway and is less useful for debugging.
     * 
     * @param motorPower the current motor power
     * @return returnPower the motor power within -1.0<=x<=1.0
     */ 
    double limitOutput(double motorPower)
    {
        double returnPower;
        if(motorPower > 1.0)
        {
            returnPower = 1.0;
        } else if(motorPower < -1.0)
        {
            returnPower = -1.0;
        } else if(motorPower < 0.5 && motorPower > 0.0)
        {
            returnPower = 0.5;
        } else if(motorPower > -0.5 && motorPower < 0.0)
        {
            returnPower = -0.5;
        }
        else
        {
            returnPower = motorPower;
        }
        return returnPower;
    }
}
