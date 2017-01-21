package org.usfirst.frc.team3238.robot;

import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import org.opencv.core.Point;

/**
 * The main WPI provided class, iterative and holding the configuration of subsystems and attempting to integrate them effectively.
 * 
 * @author FRC Team 3238
 * 
 * @version 1.0
 */ 
public class Robot extends IterativeRobot
{ //Lots of configuration, possible to use static classes instead for most of these.
    Breacher breacherArm;

    Chassis chassis;
    Collector collector;
    Shooter shooter;
    //Vision vision;
   // Autonomous auto;
    Joystick assistantJoystick, mainJoystick, launchPad;
    CANTalon leftDriveTalonA, leftDriveTalonB, rightDriveTalonA,
            rightDriveTalonB;
    CANTalon breacherTalon;
    CANTalon collectorTalon;
    CANTalon shooterTalonLeft, shooterTalonRight;
    DigitalInput ballDetectSwitch;
    Timer timer, tim;
//    NetworkTable netTab; //a way of transmitting values
    private static double kP = 1, kI = 0.12, kD = 0.0, kF = 0.0, kToleranceDegrees = 2.0f;
//    PIDController turnController;
    double rotateToAngleRate;
    rotateToAngle rTA = new rotateToAngle();
//    AHRS ahrs;
    public void report() {
        align();
        // SmartDashboard.putNumber("O_ID Target Angle: " , getRequiredAngle());
        SmartDashboard.putNumber("O_ID Rectangle Center X: " , x);
//        DriverStation.reportError("AHRS: " + ahrs.getAngle() + " Offset: " + angleOffset + " Subtracted: " + (ahrs.getAngle() + angleOffset), false);
       // DriverStation.reportError("Degrees: " +)
        SmartDashboard.putNumber("O_ID Rect Center X: " , ImageProcessor.getRectCenter().x);
//        SmartDashboard.putNumber("O_ID NavX Angle: ", ahrs.getAngle());

        SmartDashboard.putNumber("O_ID Rotation Rate: " , rotateToAngleRate);
    }
    static double power = 0.55;
    public void robotInit()
    {
        try {
//            ahrs = new AHRS(SPI.Port.kMXP);
        } catch(RuntimeException e) {
            DriverStation.reportError("Error instantiating navX-MXP:  " + e.getMessage(), true);

        }
//        rTA.init();

//        turnController = new PIDController(kP, kI, kD, ahrs, this);
//        turnController.setInputRange(-180.0f,  180.0f);
//        turnController.setOutputRange(-power, power);
//        turnController.setAbsoluteTolerance(kToleranceDegrees);
//        turnController.setContinuous(true);

        try
        {
            ImageProcessor.init();
//
//            //  DriverStation.reportWarning("Thread Started", false);
            (new Thread(new ImageProcessor())).start();
            assistantJoystick = new Joystick(
                    Constants.Joysticks.joystickZeroPort);
            mainJoystick = new Joystick(Constants.Joysticks.joystickOnePort);
            launchPad = new Joystick(Constants.Joysticks.launchpadPort);

            ballDetectSwitch = new DigitalInput(
                    Constants.Collector.ballDetectPort);

            leftDriveTalonA = new CANTalon(Constants.Chassis.leftMotorOneID);
            leftDriveTalonB = new CANTalon(Constants.Chassis.leftMotorTwoID);
            rightDriveTalonA = new CANTalon(Constants.Chassis.rightMotorOneID);
            rightDriveTalonB = new CANTalon(Constants.Chassis.rightMotorTwoID);
            rightDriveTalonA.setInverted(true);
            rightDriveTalonB.setInverted(true);
            breacherTalon = new CANTalon(Constants.Breacher.breacherTalonID);
            collectorTalon = new CANTalon(Constants.Collector.collectorTalonID);
            shooterTalonLeft = new CANTalon(
                    Constants.Shooter.shooterLeftTalonID);
            shooterTalonRight = new CANTalon(
                    Constants.Shooter.shooterRightTalonID);

            breacherArm = new Breacher(breacherTalon);
//            netTab = NetworkTable.getTable("GRIP");
//            netTab.putBoolean("run", true); //turns vision processing on, although does not work in autonomous

            chassis = new Chassis(leftDriveTalonA, leftDriveTalonB,
                    rightDriveTalonA, rightDriveTalonB);
            shooter = new Shooter(shooterTalonLeft, shooterTalonRight,
                    mainJoystick, assistantJoystick, launchPad);
            collector = new Collector(collectorTalon, ballDetectSwitch,
                    shooter, mainJoystick, assistantJoystick, launchPad);


            timer = new Timer();
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }

        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
        DriverStation.reportError("THE CODE HAS UPLOADED!!!!!!!!!!!", false);
    }

    public void autonomousInit()
    {
       // auto.init();
        checks = 0;
        shooter.reset();
//        netTab.putBoolean("run", true); //vision processing
        t.reset();
        t.start();
        gotit = false;
        once = true;
        totalError = 0;
        //ImageProcessor.camera.setExposureManual(0);
//        rTA.ahrs.reset();
    }

    double camWidthMult = 0.4, camMult = 0.4*0.102*0.119*1.73;
    public void align() {
        double width = ImageProcessor.combined.width, xDiff = ImageProcessor.combined.x-320;
        double inches = width * camWidthMult;
        double degrees = camMult * xDiff;
        double x = inches * Math.cos(Math.toRadians(degrees));
        double y = inches * Math.sin(Math.toRadians(degrees) - 10.75);
        double angle = Math.atan((y / x));

        double distance = Math.sqrt(Math.pow(x, 2)+ Math.pow(y,2));
        SmartDashboard.putNumber("N_ID: Angle" , angle);
        SmartDashboard.putNumber("N_ID: Distance", distance);
        SmartDashboard.putNumber("N_ID: Degrees", degrees);
        SmartDashboard.putNumber("N_ID: Inches", inches);


    }
     double nTuner = getIdentifier("tuner", 0.5);
    public void angleTurner() {
        double x = ImageProcessor.getRectCenter().x;


    }
    double newAngle = 0, last_pose = 0;
    boolean once = true;
    public void dashReport(String name, double value) {
        SmartDashboard.putNumber(name, value);
    }
    public void turnToAngle() {

        if(ImageProcessor.hasNewFrame())
            last_pose = rTA.getAngle();

        if(ImageProcessor.hasProcessed()) {
            double angleDisplacement = (319.5-ImageProcessor.combined.x)*0.11;
            double currentAngle = rTA.getAngle();
            newAngle = (currentAngle - angleDisplacement) - (currentAngle-last_pose);
            t.reset();

            if (newAngle > 180) newAngle = -180 + (newAngle - 180);
            if(newAngle < -180) newAngle = 180+(newAngle + 180);

            dashReport("current Angle", currentAngle);
            dashReport("angle displacement", angleDisplacement);
            dashReport("last pose", last_pose);
            SmartDashboard.putNumber("NewAngle", newAngle);
        }
        rTA.goToAngle(newAngle);

        double n = rTA.getSpeed();


        chassis.arcadeDriveAuto(0, n);
        DriverStation.reportError("??" + n,false);

        rTA.kP = getIdentifier("kayP", 0.06);
        rTA.turnController.setPID(rTA.kP, rTA.kI, rTA.kD);
    }
    public void autonomousPeriodic()
    {
        SmartDashboard.putNumber("NavX Angle Value", rTA.getAngle());

        SmartDashboard.putNumber("Center X Value", ImageProcessor.combined.x);
        delayedAlignment();

//        turnToAngle();
    }
    public void commentStorage() {
        //        delayedAlignment();
        Preferences r = Preferences.getInstance();
//        camWidthMult = r.getDouble("camWidthMult", 1.0);
//        camMult = r.getDouble("camMult", 1.0);
//        report();
//        align();
////        DriverStation.reportWarning(""+ImageProcessor.camera.getLastFrameTime(),false);
//       /*if(Math.abs(ImageProcessor.getRectCenter().x-320) > 7) {
//           if (ImageProcessor.hasNewFrame()) {
//
//               x = ImageProcessor.getRectCenter().x;
//
//               speed = (x - 320) * (maxSpeed / (320));
//           } else {
//
//               powAmt += overshootConstant;
//               chassis.arcadeDriveAuto(0, speed/powAmt);
//           }
//       } else
//           chassis.arcadeDriveAuto(0,0);
//
//       DriverStation.reportWarning(""+ImageProcessor.combined.area(),false);*/
//       //if(mainJoystick.getRawButton(10))
//        t.start();
    }
    int close_size = 1000, checks = 0;
    double speed = 0.01, maxSpeed = 0.8, minSpeed = 0.365;
    double powAmt = 0.3, overshootConstant = 0.1, dDenom = 160.0;
    double totalError = 0;
    boolean gotit = false;
    Timer t = new Timer();
    public double delayDenominator() {
        return 1+dDenom*t.get();
    }
    public double getIdentifier(String identity, double backup) {
        Preferences p = Preferences.getInstance();

        return p.getDouble(identity, backup);
    }
    double checkThresh;
    public  void delayedAlignment() {
        powAmt = getIdentifier("powAmt", powAmt);
        dDenom = getIdentifier("dDenom", dDenom);
        maxSpeed = getIdentifier("maxSpeed", maxSpeed);
        checkThresh = getIdentifier("cTh", 150);
//        report();
        if(Math.abs(ImageProcessor.getRectCenter().x-319.5) > 5 && checks < checkThresh) {
            if (ImageProcessor.hasNewFrame()) {
                x = ImageProcessor.getRectCenter().x;
                DriverStation.reportError(""+delayDenominator(), false);
                t.reset();

            }

            double k = (x-319.5);
            double integralConstant = getIdentifier("integ", 10000);
            totalError += k/(integralConstant*(1/(k+1)));
            j = k*(maxSpeed/60)/delayDenominator();
            j = (Math.abs(j)/j)*Math.pow(Math.abs(j), powAmt);
            double setMax = getIdentifier("kMax", 0.45);
            if(Math.abs(j) > setMax) j = setMax* Math.abs(j)/j;
            SmartDashboard.putNumber("Turn Rate", j);
            chassis.visionDrive(-j-totalError);
        } else checks++;
        if(checks >= checkThresh){
            chassis.arcadeDriveAuto(-0.3, 0);
            // chassis.arcadeDriveAuto(-0.8*((ImageProcessor.combined.area()-close_size)/close_size), 0);
        }
    }
//    public void roughlyAccurateAlignment() {
//        if(Math.abs(ImageProcessor.getRectCenter().x-320) > 3) {
//            if (ImageProcessor.hasNewFrame()) {
//                x = ImageProcessor.getRectCenter().x;
//                j = (halveXDim - x) * speed;
//                if (j > maxSpeed) j = maxSpeed;
//                if (j < -maxSpeed) j = -maxSpeed;
//                if (Math.abs(j) < minSpeed) j = (Math.abs(j) / j) * minSpeed;
//            }
//            SmartDashboard.putNumber("JSPEED", j);
//            if (j < 0) j = -Math.pow(Math.abs(j), powAmt);
//            else j = Math.pow(j, powAmt);
//            chassis.visionDrive(j);
//        } else {
//
//           // chassis.arcadeDriveAuto(-0.8*((ImageProcessor.combined.area()-close_size)/close_size), 0);
//        }
//    }
    public void teleopInit()
    {
        ImageProcessor.updateCams();
        collector.init(); //allow driver to use the collector, set to automatic
        shooter.reset();
        SmartDashboard.putNumber("DB/Slider 0", 0); //reset the DB/Slider that is preset before matches to determine autonomous routine
//        netTab.putBoolean("run", false); //turn off vision processing
    }
    double halveXDim = 320, anglePerPixel = 0.01;
    double angleOffset = 0.0, x = 0.0;
//    public doubl

    boolean rotateToAngle = false, recentAssignment = true, keep = true;
   // double power = 0.6;
    double j = 0.0;
    public void teleopPeriodic()
    {

        Preferences pr = Preferences.getInstance();
        SmartDashboard.putNumber("Rect Area", ImageProcessor.combined.area());
        powAmt = pr.getDouble("O_Raise Power", 0.7);
        minSpeed = pr.getDouble("minSpeed", 0.38);
        maxSpeed = pr.getDouble("maxSpeed", 0.44);
        SmartDashboard.putNumber("kP: ", kP);
        SmartDashboard.putNumber("Power: ", power);
        if(assistantJoystick.getRawButton(10))
            ImageProcessor.camera.setExposureManual(0);


            //turnController.setSetpoint(getRequiredAngle());

        if(mainJoystick.getRawButton(12))
//            ahrs.reset();
report();


//        if(mainJoystick.getRawButton(11))
//            turnController.disable();
//        if(turnController.isEnabled())
//            chassis.visionDrive(j);
//        if(mainJoystick.getRawButton(10))

//            roughlyAccurateAlignment(); else
        motorCommands();

    }
    public void motorCommands() {
        chassisCommands(); //controls drive system

        breacherArm.run(assistantJoystick); //moves breacher with values from assistantjoystick
        collector.idle(); //move like collector.run()
        shooter.idle(collector.isCollecting()); /*more like shooter.run() using if the collector is collecting as parameter
            to determine if it should shut off so it doesn't fire immdiately after collection*/
        //  netTab.putBoolean("run", false); //turn off vision processing

        //SmartDashboard.putBoolean("DB/Button 0", vision.getTowerPos());
    }
    // Drive system
    private void chassisCommands()
    {
        chassis.setMotorInversion(mainJoystick);
//       if(!turnController.isEnabled())
        chassis.arcadeDrive(mainJoystick, launchPad);
    }

    public void disabledPeriodic()
    {
       // camera.stream(); //still stream camera
     //   auto.init(); //reset auto
//        chassis.setPower(0.0); //disable everything else
        breacherArm.moveArm(0.0);
//        vision.stop();
        shooter.reset();
//        netTab.putBoolean("run", false);
    }

    public void testPeriodic()
    {
//        vision.teleVision();
//        netTab.putBoolean("run", false);
    }

//    @Override
//    public void pidWrite(double output) {
//        rotateToAngleRate = output;
////    }
}
