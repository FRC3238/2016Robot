package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.SpeedController;

public class Chassis
{
    RobotDrive driveTrain;

    double xValue;
    double twistValue;
    double mappedX;
    double mappedTwist;
    public static double[] zoneArray;
    public int deadZone = 0, monoZone = 3, squaredZone = 2, cubedZone = 1;
    public double twistMultiplier = 0.5;
    SpeedController leftMotorControllerA, rightMotorControllerA,
            leftMotorControllerB, rightMotorControllerB;

    Chassis(SpeedController leftMotorControllerA,
            SpeedController rightMotorControllerA,
            SpeedController leftMotorControllerB,
            SpeedController rightMotorControllerB)
    {
        zoneArray = new double[4];
        this.leftMotorControllerA = leftMotorControllerA;
        this.rightMotorControllerA = rightMotorControllerA;
        this.leftMotorControllerB = leftMotorControllerB;
        this.rightMotorControllerB = rightMotorControllerB;
        this.leftMotorControllerA.setInverted(true);
        this.leftMotorControllerB.setInverted(true);

    }

    /*
     * void setSquaredZone(double sq) { squaredZone = sq; }
     */
    void setJoystickData(double x, double twist)
    {
        xValue = x;
        twistValue = twist;
    }

    void ezDrive(double x, double y)
    { // still better than scrubdrive!
        config(0);
        if(Math.abs(x) < deadZone)
        {
            setInvertVariable(y, 0);
        } else
        {
            disable();
        }
    }

    void proDrive(double x, double y, double twist)
    {
        config(1); // Drivers choose whether they want x axis after testing
        double tY = Math.abs(y);
        String driveStatement;
        if(twist <= deadZone)
        {
            twist = 0;
        } else
        {
            twist = twist * twistMultiplier;
        }

        for(int i = 2; i >= 0; i--)
        {
            if(tY <= zoneArray[i + 1] && tY > zoneArray[i])
            {
                setInvertVariable(Math.pow(tY, 3 - i) * y / tY, twist);
            } else
            {
                disable();
            }
        }

    }

    void setInvertVariable(double prime, double vert)
    {
        leftMotorControllerA.set(prime + vert);
        leftMotorControllerB.set(prime + vert);
        rightMotorControllerA.set(prime - vert);
        rightMotorControllerB.set(prime - vert);
    }

    void setZones(double mZ, double sZ, double cZ, double dZ)
    {

        zoneArray[deadZone] = dZ;
        zoneArray[monoZone] = mZ;
        zoneArray[squaredZone] = sZ;
        zoneArray[cubedZone] = cZ;

    }

    // Configures zones for drive multipliers
    void config(int config)
    {
        switch(config)
        {
            case 0: // scrubDrive config
                setZones(monoZone, 0.75, cubedZone, 0.15);
                break;
            case 1: // proDrive config
                setZones(1.0, 0.66, 0.36, 0.12);
            default:
                break;
        }
    }

    void disable()
    {
        leftMotorControllerA.set(0);
        leftMotorControllerB.set(0);
        rightMotorControllerA.set(0);
        rightMotorControllerB.set(0);
    }
}
