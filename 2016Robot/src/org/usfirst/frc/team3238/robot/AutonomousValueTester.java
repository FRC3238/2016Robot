package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutonomousValueTester
{
    Timer timer;
    Chassis chassis;
    Shooter shooter;
    Breacher breacher;
    
    public AutonomousValueTester(Chassis chassis, Breacher breacher, Shooter shooter) {
        timer = new Timer();
        timer.reset();
        this.chassis = chassis;
        this.breacher = breacher;
        this.shooter = shooter;
    }
    public void init() {
        
//        setMotorSpeed(0.95);
        timer.reset();
        timer.start();
        chassis.invertMotors(false);
//        SmartDashboard.putNumber("timer", timer.get());
    }
    public void stop() {
        try {
        chassis.exist();
        }catch(Exception e) {
            DriverStation.reportError("Existing = false", true);
        }
        chassis.setSpeed(0.0);
    }
    public void eZAutos(double spd, double tym) {
        if(timer.get() < tym) {
            setMotorSpeed(spd);
        } else {
            stop();
        }
    }
    public void start(String chi) {
        if(chi.equals("moat")) {
            eZAutos(0.8, 1.86);
        } else if(chi.equals("roughterrain")) {
            eZAutos(0.7, 2.00);
        } else if(chi.equals("ramparts")) {
            eZAutos(0.8, 2.00);
        } else if(chi.equals("rockwall")) {
            eZAutos(0.9, 2.00);
        } else if(chi.equals("portcullis")) {
        
        } else if(chi.equals("cheval")) {
            
        } else if(chi.equals("lowbar")) {
            eZAutos(0.7, 2.00);
        }
    }
    public void iterator(double iter) {
        setMotorSpeed(iter);
        SmartDashboard.putNumber("timer", timer.get());
    }
    public void setMotorSpeed(double dubs) {
        this.chassis.setSpeed(dubs);
    }

}
