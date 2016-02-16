package org.usfirst.frc.team3238.robot;

public class Constants
{
    public class MainDriver
    {
        public static final int shoot = 1;
        public static final int autoCollect = 2;
        public static final int autoEject = 3;
        public static final int motorOff = 4;
        public static final int frontChangeCamButton = 12;
        public static final int backChangeCamButton = 11;

        public static final int manualCollectIn = 0;
        public static final int manualCollectOut = 4;
    }

    public class AssistantDriver
    {
        public static final int prepShootOn = 1;
        public static final int prepShootOff = 2;

        public static final int breacherUp = 6;
        public static final int breacherMiddleUp = 5;
        public static final int breacherMiddleDown = 3;
        public static final int breacherDown = 4;

        public static final int manualShooterPreset1 = 7;
        public static final int manualShooterPreset2 = 8;
        public static final int manualShooterPreset3 = 9;
        public static final int manualShooterPreset4 = 10;

        public static final int manualShooterSubtract = 11;
        public static final int manualShooterAdd = 12;
    }

    public class LaunchPad
    {
        public static final int cameraSwitch = 8;
        public static final int driveReverseSwitch = 9;
        public static final int parkingBrakeSwitch = 14;
        public static final int shooterUp = 15;
        public static final int shooterDown = 16;
    }

    public class Breacher
    {
        public static final int breacherTalonPort = 5;
        public static final int armDetectTopPort = 1;
        public static final int armDetectBotPort = 3;
    }

    public class Camera
    {
        public static final String frontCamName = "cam1";
        public static final String rearCamName = "cam2";

        public static final int camQuality = 10;
        public static final int camSize = 1;

        public static final int crosshairCenterX = 320;
        public static final int crosshairCenterY = 240;
    }

    public class Chassis
    {
        public static final int leftMotorOneID = 1;
        public static final int leftMotorTwoID = 2;
        public static final int rightMotorOneID = 3;
        public static final int rightMotorTwoID = 4;
    }

    public class Collector
    {
        public static final double defaultPower = 1.0;
        public static final double centerPower = 0.8;

        public static final int ballDetectChannel = 2;
        public static final int collectorTalonPort = 6;
    }

    public class Shooter
    {
        public static final int shooterLeftTalonPort = 7;
        public static final int shooterRightTalonPort = 8;

        public static final double presetPowerOne = 0.6;
        public static final double presetPowerTwo = 0.69;
        public static final double presetPowerThree = 0.8;
        public static final double presetPowerFour = 0.9;
    }

    public class Robot
    {
        public static final int joystickZeroPort = 0;
        public static final int joystickOnePort = 1;
        public static final int launchpadPort = 2;
    }
}
