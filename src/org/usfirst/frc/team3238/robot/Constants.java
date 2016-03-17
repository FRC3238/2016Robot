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

        public static final int manualShooterPreset1 = 4;
        public static final int manualShooterPreset2 = 8;
        public static final int manualShooterPreset3 = 3;
        public static final int manualShooterPreset4 = 10;

        public static final int manualShooterSubtract = 11;
        public static final int manualShooterAdd = 12;

        public static final int shooterManualUp = 0;
        public static final int shooterManualDown = 180;
    }

    public class LaunchPad
    {
        public static final int cameraSwitch = 8;
        public static final int driveReverseSwitch = 9;
        public static final int parkingBrakeSwitch = 14;
        public static final int shooterUp = 3;
        public static final int shooterDown = 4;

        public static final int twistPower = 0;
    }

    public class Auto
    {
        // TODO set values and times

        // General
        public static final double brakeSpeed = 0.0;
        public static final double brakeTime = 0.0;
        public static final double armLowerPower = -0.3;
        public static final double shootRevTime = 13.0;

        // Cheval De Frise
        public static final double chevalTime = 2.1;
        public static final double chevalArmTime = 2.5;
        public static final double chevalBreachTime = 1.5;
        public static final double chevalArmPower = -0.8;
        public static final double chevalArmRaisePower = 1.0;
        public static final double chevalSpeed = 0.25;
        public static final double chevalBreachSpeed = 0.5;

        // High Goal
        public static final double goalForwardPowerY = 0.4;
        public static final double goalForwardPowerTwist = 0.1;
        public static final double goalForwardTime = 1.5;

        // Low Bar
        public static final double lowBarArmTime = 1.4;
        public static final double lowBarArmPower = -0.8;
        public static final double lowBarBreachTime = 2.7;
        public static final double lowBarPower = 0.4;

        // Portcullis
        public static final double portcullisBreachTime = 2.7;
        public static final double portcullisArmTime = 1.4;
        public static final double portcullisArmPower = -0.8;
        public static final double portcullisSpeed = 0.4;

        // Rock Wall
        public static final double rockBreachTime = 1.5;
        public static final double rockSpeed = 0.9;

        // Rough Terrain
        public static final double roughBreachTime = 2.0;
        public static final double roughSpeed = 0.7;

        // Moat
        public static final double moatBreachTime = 1.5;
        public static final double moatSpeed = 0.8;

        // Ramparts
        public static final double rampBreachTime = 1.2;
        public static final double rampSpeed = 0.8;
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

        // to be determined
        public static final int encoderValueTop = 1;
        public static final int encoderValueMidTop = 2;
        public static final int encoderValueMidBot = 3;
        public static final int encoderValueBot = 4;
    }

    public class Camera
    {
        public static final String frontCamName = "cam1";
        public static final String rearCamName = "cam2";

        public static final int camQuality = 10;
        public static final int camSize = 1;

        public static final int crosshairCenterXTower = 160;
        public static final int crosshairCenterYTower = 240;
        public static final int crosshairCenterXShooter = 370;
        public static final int crosshairCenterYShooter = 200;
        public static final int crosshairLength = 20;
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
        public static final double centerPower = 0.4;
        public static final double liftPower = 0.15;

        public static final int ballDetectPort = 2;
        public static final int collectorTalonID = 6;
    }

    public class Shooter
    {
        public static final int shooterLeftTalonID = 7;
        public static final int shooterRightTalonID = 8;

        public static final int hallEffectLeftPort = 0;
        public static final int hallEffectRightPort = 1;

        public static final double pValueLeft = 1;
        public static final double iValueLeft = 0;
        public static final double pValueRight = 1;
        public static final double iValueRight = 0;
        public static final double error = 50;

        public static final double presetPowerOne = 3450;
        public static final double presetPowerTwo = 3450;
        public static final double presetPowerThree = 4225;
        public static final double presetPowerFour = 3450;
    }

    public class Vision
    {
        public static final double towerYLowerLimit = 112;
        public static final double towerYUpperLimit = 128;
        public static final double shooterSpeed = 3700;

        public static final double pValueX = 16;
        public static final double iValueX = 0;
        public static final double throttleX = 0.5;
        public static final double pValueY = 16;
        public static final double iValueY = 0;
        public static final double throttleY = 0.5;
        public static final double setPointX = 120;
        public static final double setPointY = 160;
        public static final double error = 1;
    }

    public class Joysticks
    {
        public static final int joystickZeroPort = 0;
        public static final int joystickOnePort = 1;
        public static final int launchpadPort = 2;
    }
}
