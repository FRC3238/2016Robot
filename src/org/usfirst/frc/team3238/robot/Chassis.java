package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

public class Chassis
{
    RobotDrive driveTrain;

    double xValue;
    double twistValue;

    Chassis(SpeedController leftMotorController,
            SpeedController rightMotorController)
    {
        driveTrain = new RobotDrive(leftMotorController, rightMotorController);
    }

    void setJoystickData(double x, double twist)
    {
        xValue = x;
        twistValue = twist;
    }

    void idle()
    {
        double mappedX;
        double mappedTwist;

        if(xValue < 0)
        {
            mappedX = -(xValue * xValue);
        } else
        {
            mappedX = xValue * xValue;
        }

        if(twistValue < 0)
        {
            if(twistValue > -0.75)
            {
                mappedTwist = 0.3333 * twistValue;
            } else
            {
                mappedTwist = -0.4444 * (twistValue * twistValue);
            }
        } else
        {
            if(twistValue < 0.75)
            {
                mappedTwist = 0.3333 * twistValue;
            } else
            {
                mappedTwist = 0.4444 * (twistValue * twistValue);
            }
        }

        driveTrain.arcadeDrive(mappedX, mappedTwist);
    }
}
