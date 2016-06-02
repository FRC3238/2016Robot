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
        public static final double chevalTime = 1.8;
        public static final double chevalArmTime = 2.4;
        public static final double chevalBreachTime = 1.3;
        public static final double chevalArmPower = -0.8;
        public static final double chevalArmRaisePower = 0.7;
        public static final double chevalSpeed = 0.25;
        public static final double chevalBreachSpeed = 0.65;

        // High Goal Low Bar
        public static final double goalForwardPowerY = 0.4;
        public static final double goalForwardPowerTwist = -0.3;
        public static final double goalForwardTime = 1.8;
        public static final double goalBarForwardTime = 2.2;
        public static final double goalBarForwardSpeed = 0.6;

        // TODO Set values
        // High Goal Portcullis
        public static final double goalPortTwoForwardPowerY = 0.5;
        public static final double goalPortTwoForwardPowerTwist = -0.0;
        public static final double goalPortTwoForwardTime = 0.9;
        public static final double goalPortThreeForwardPowerY = 0.4;
        public static final double goalPortThreeForwardPowerTwist = -0.0;
        public static final double goalPortThreeForwardTime = 1.0;
        public static final double goalPortFourForwardPowerY = 0.4;
        public static final double goalPortFourForwardPowerTwist = 0.0;
        public static final double goalPortFourForwardTime = 1.0;
        public static final double goalPortFiveForwardPowerY = 0.6;
        public static final double goalPortFiveForwardPowerTwist = 0.0;
        public static final double goalPortFiveForwardTime = 0.8;

        // High Goal Cheval
        public static final double goalCDFTwoForwardPowerY = 0.45;
        public static final double goalCDFTwoForwardPowerTwist = -0.0;
        public static final double goalCDFTwoForwardTime = 0.9;
        public static final double goalCDFThreeForwardPowerY = 0.4;
        public static final double goalCDFThreeForwardPowerTwist = -0.3;
        public static final double goalCDFThreeForwardTime = 0.5;
        public static final double goalCDFFourForwardPowerY = 0.4;
        public static final double goalCDFFourForwardPowerTwist = 0.15;
        public static final double goalCDFFourForwardTime = 0.5;
        public static final double goalCDFFiveForwardPowerY = 0.4;
        public static final double goalCDFFiveForwardPowerTwist = -0.0;
        public static final double goalCDFFiveForwardTime = 0.9;

        // TODO Set values
        // High Goal Rock Wall
        public static final double goalRockTwoForwardPowerY = 0.45;//
        public static final double goalRockTwoForwardPowerTwist = -0.0;//
        public static final double goalRockTwoForwardTime = 0.9;//
        public static final double goalRockThreeForwardPowerY = 0.4;//
        public static final double goalRockThreeForwardPowerTwist = -0.45;//
        public static final double goalRockThreeForwardTime = 2.25;//
        public static final double goalRockFourForwardPowerY = 0.4;//
        public static final double goalRockFourForwardPowerTwist = 0.15;//
        public static final double goalRockFourForwardTime = 2.5;//
        public static final double goalRockFiveForwardPowerY = 0.4;//
        public static final double goalRockFiveForwardPowerTwist = -0.0;//
        public static final double goalRockFiveForwardTime = 0.9;//

        // TODO Set values
        // High Goal Rough Terrain
        public static final double goalRoughTwoForwardPowerY = 0.45;//
        public static final double goalRoughTwoForwardPowerTwist = -0.0;//
        public static final double goalRoughTwoForwardTime = 0.9;//
        public static final double goalRoughThreeForwardPowerY = 0.4;//
        public static final double goalRoughThreeForwardPowerTwist = -0.45;//
        public static final double goalRoughThreeForwardTime = 2.25;//
        public static final double goalRoughFourForwardPowerY = 0.4;//
        public static final double goalRoughFourForwardPowerTwist = 0.15;//
        public static final double goalRoughFourForwardTime = 2.5;//
        public static final double goalRoughFiveForwardPowerY = 0.4;//
        public static final double goalRoughFiveForwardPowerTwist = -0.0;//
        public static final double goalRoughFiveForwardTime = 0.9;//

        // TODO Set values
        // High Goal Moat
        public static final double goalMoatTwoForwardPowerY = 0.45;//
        public static final double goalMoatTwoForwardPowerTwist = -0.0;//
        public static final double goalMoatTwoForwardTime = 0.9;//
        public static final double goalMoatThreeForwardPowerY = 0.4;//
        public static final double goalMoatThreeForwardPowerTwist = -0.45;//
        public static final double goalMoatThreeForwardTime = 2.25;//
        public static final double goalMoatFourForwardPowerY = 0.4;//
        public static final double goalMoatFourForwardPowerTwist = 0.15;//
        public static final double goalMoatFourForwardTime = 2.5;//
        public static final double goalMoatFiveForwardPowerY = 0.4;//
        public static final double goalMoatFiveForwardPowerTwist = -0.0;//
        public static final double goalMoatFiveForwardTime = 0.9;//

        // TODO Set values
        // High Goal Ramparts
        public static final double goalRampTwoForwardPowerY = 0.45;//
        public static final double goalRampTwoForwardPowerTwist = -0.0;//
        public static final double goalRampTwoForwardTime = 0.9;//
        public static final double goalRampThreeForwardPowerY = 0.4;//
        public static final double goalRampThreeForwardPowerTwist = -0.45;//
        public static final double goalRampThreeForwardTime = 2.25;//
        public static final double goalRampFourForwardPowerY = 0.4;//
        public static final double goalRampFourForwardPowerTwist = 0.15;//
        public static final double goalRampFourForwardTime = 2.5;//
        public static final double goalRampFiveForwardPowerY = 0.4;//
        public static final double goalRampFiveForwardPowerTwist = 0.0;//
        public static final double goalRampFiveForwardTime = 0.9;//

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
    }

    public class Camera
    {
        // TODO Change camera names
        public static final String frontCamName = "cam1";
        public static final String rearCamName = "cam2";

        public static final int camQuality = 10;
        public static final int camSize = 1;

        // TODO Change tower center point values
        public static final int crosshairCenterXTower = 200;
        public static final int crosshairCenterYTower = 205;
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
        public static final double twistMultiplier = 0.9;
        public static final double leftTwistMult = 0.9;
        public static final double rightTwistMult = 0.9;
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

        public static final double pValueLeft = 0.5;
        public static final double iValueLeft = 0;
        public static final double pValueRight = 0.5;
        public static final double iValueRight = 0;
        public static final double error = 100;

        public static final double presetPowerOne = 3375;
        public static final double presetPowerTwo = 3375;
        public static final double presetPowerThree = 4500;
        public static final double presetPowerFour = 3900;
    }

    public class Vision
    {
        // TODO Possibly add I values and change kangaroo filtering to using
        // rgb, not hsl
        public static final double shooterSpeed = 3450;
        public static final double pValueX = 0.003;
        public static final double iValueX = 0.00000;
        public static final double throttleX = 0.5;
        public static final double pValueY = 0.01;
        public static final double iValueY = 0.000;
        public static final double throttleY = 0.5;
        public static final double setPointX = 60;
        public static final double setPointY = 150;
        public static final double defaultX = 60;
        public static final double defaultY = 150;
        public static final double errorY = 8;
        public static final double errorX = 13;
    }

    public class Joysticks
    {
        public static final int joystickZeroPort = 0;
        public static final int joystickOnePort = 1;
        public static final int launchpadPort = 2;
    }
}
