package org.usfirst.frc.team3238.robot;

public class Constants
{
    public class MainDriver
    {
        public static final int shoot = 1;
        public static final int autoCollect = 2;
        public static final int autoEject = 5;
        public static final int motorOff = 3;
        public static final int frontChangeCamButton = 12;
        public static final int backChangeCamButton = 11;

        public static final int manualCollectIn = 0;
        public static final int manualCollectOut = 180;
    }

    public class AssistantDriver
    {
        public static final int prepShootOn = 1;
        public static final int prepShootOff = 2;

        public static final int breacherUpButton = 6;
        public static final int breacherMiddleUpButton = 5;
        public static final int breacherMiddleDownButton = 3;
        public static final int breacherDownButton = 4;

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
        public static final int shooterUp = 2;
        public static final int shooterDown = 3;
        
        public static final int twistPower = 0;
    }

    public class Breacher
    {
        public static final int breacherTalonID = 5;
        public static final int armDetectTopPort = 3;
        public static final int armEncoderPortA = 6;
        public static final int armEncoderPortB = 7;

        public static final double speedDuringPreset = 1.0;
        public static final double armPower = .5;
        public static final double deadzone = .1;
        
        //to be determined
        public static final int encoderValueTop = 1;
        public static final int encoderValueMidTop = 2;
        public static final int encoderValueMidBot = 3;
        public static final int encoderValueBot = 4;
    }

    public class Camera
    {
        public static final String frontCamName = "cam0";
        public static final String rearCamName = "cam1";

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
        public static final double yMultiplier = 0.95;
        public static final double twistMultiplier = 0.88;
    }

    public class Collector
    {
        public static final double defaultPower = 0.75;
        public static final double centerPower = 0.62;
        public static final double liftPower = 0.35;

        public static final int ballDetectPort = 2;
        public static final int collectorTalonID = 6;
    }

    public class Shooter
    {
        public static final int shooterLeftTalonID = 7;
        public static final int shooterRightTalonID = 8;
        
        public static final int hallEffectLeftPort = 0;
        public static final int hallEffectRightPort = 1;

        public static final double presetPowerOne = 2500;
        public static final double presetPowerTwo = 3450;
        public static final double presetPowerThree = 4000;
        public static final double presetPowerFour = 5000;
        public static final double suggestedRPM = 3450.0;
    }

    public class Joysticks
    {
        public static final int joystickZeroPort = 0;
        public static final int joystickOnePort = 1;
        public static final int launchpadPort = 2;
    }
}