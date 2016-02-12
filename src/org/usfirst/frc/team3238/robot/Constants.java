package org.usfirst.frc.team3238.robot;

public class Constants
{
    public class Breacher
    {
        public static final int breacherTalonPort = 5;
        public static final int armDetectTopPort = 1;
        public static final int armDetectBotPort = 2;
        
        public static final int breacherUpButton = 4;
        public static final int breacherDownButton = 6;
    }

    public class Camera
    {
        public static final String frontCamName = "cam1";
        public static final String rearCamName = "cam2";

        public static final int camQuality = 30;
        public static final int camSize = 0;

        public static final int crosshairCenterX = 320;
        public static final int crosshairCenterY = 240;
        
        public static final int camChangeButton = 2;
        public static final int camKillSwitch = 8;
    }

    public class Chassis
    {
        public static final int leftMotorOneID = 1;
        public static final int leftMotorTwoID = 2;
        public static final int rightMotorOneID = 3;
        public static final int rightMotorTwoID = 4;
    }

    public class CollectAndShoot
    {
        public static final int ballDetectChannel = 0;
        public static final int centerDetectChannel = 3;
        public static final int holdPosDetectChannel = 4;
        public static final int shooterLeftTalonPort = 7;
        public static final int shooterRightTalonPort = 8;
        public static final int collectorTalonPort = 6;

        public static final int shooterButton = 1;
        public static final int killSwitch = 11;
    }

    public class Robot
    {
        public static final int joystickZeroPort = 0;
        public static final int joystickOnePort = 1;
        public static final int launchpadPort = 2;
        
        public static final double throttleRangeAdjuster = 0.5;
    }
}
