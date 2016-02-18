package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

enum CollectorState
{
    DISABLED, COLLECTING, CENTERING, LOWERING, HOLDING, EJECTING, SHOOTING
}

public class Collector
{
    CollectorState state;

    CANTalon talon;
    DigitalInput ballDetect;
    Joystick stick;
    Timer timer;
    private String status = "";
    private boolean loweringToSwitch;

    Collector(CANTalon collectorTalon, DigitalInput ballDetect, Joystick stick)
    {
        try
        {
            this.talon = collectorTalon;
            this.ballDetect = ballDetect;
            this.stick = stick;
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }

        try
        {
            timer = new Timer();
            timer.reset();
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }

        state = CollectorState.DISABLED;
        loweringToSwitch = false;
    }

    private void setPowerOverride(double power)
    {
        if(stick.getPOV() == Constants.MainDriver.manualCollectIn)
            talon.set(-Constants.Collector.defaultPower);
        else if(stick.getPOV() == Constants.MainDriver.manualCollectOut)
            talon.set(Constants.Collector.defaultPower);
        else
            talon.set(power);
    }

    public void idle()
    {
        switch(state)
        {
            case CENTERING:
            	
                setPowerOverride(Constants.Collector.centerPower);
                loweringToSwitch = false;
                
                ifStick(0.05, CollectorState.LOWERING);
                
                break;
                
            case COLLECTING:
            	
                setPowerOverride(Constants.Collector.defaultPower);
                
                ifStick(Constants.MainDriver.motorOff, CollectorState.DISABLED);
                ifStick(!ballDetect.get(), CollectorState.CENTERING, true);
                ifStick(Constants.MainDriver.autoEject, CollectorState.EJECTING);
                
                break;
                
            case DISABLED:
            	
                setPowerOverride(0.0);
                
                ifStick(Constants.MainDriver.autoCollect, CollectorState.COLLECTING);
                ifStick(Constants.MainDriver.autoEject, CollectorState.EJECTING);
               
                break;
                
            case EJECTING:
            	
                setPowerOverride(-Constants.Collector.defaultPower);
                
                ifStick(Constants.MainDriver.autoCollect, CollectorState.COLLECTING);
                ifStick(Constants.MainDriver.motorOff, CollectorState.DISABLED);
                
                break;
                
            case HOLDING:
            	
                setPowerOverride(0.0);
                
                ifStick(Constants.MainDriver.shoot, CollectorState.SHOOTING, true);
                ifStick(Constants.MainDriver.autoEject, CollectorState.EJECTING);
                
                break;
                
            case LOWERING:
            	
                setPowerOverride(-Constants.Collector.centerPower);
                if(!ballDetect.get()) {
                	loweringToSwitch = true;
                }
                
                ifStick(ballDetect.get() && loweringToSwitch, CollectorState.HOLDING, false);    
                ifStick(0.15, CollectorState.HOLDING);
                
                break;
                
            case SHOOTING:
            	
                setPowerOverride(Constants.Collector.defaultPower);
                
                ifStick(1.0, CollectorState.DISABLED);
                
                break;
                
            default:
                break;
        }

    }
    void refresh() {
    	
    	timer.reset();
    	timer.start();
    }
    void refresh(boolean b) {
    	if(b) {
    		timer.reset();
    		timer.start();
    	}
    }
    void smartDashboardSet(String a, CollectorState s) {
    	SmartDashboard.putString(a, s.toString());
    }
    void ifStick(boolean isTrue, CollectorState s, boolean resetTime) {
    	if(isTrue) {
    		state = s;
    		refresh(resetTime);
    	}
    	smartDashboardSet("state", s);
    }
    void ifStick(double time, CollectorState s) {
    	if(timer.get() >= time) {
    		state = s;
    	}
    	smartDashboardSet("state", s);
    }
    void ifStick(int stickNumber, CollectorState s) {
    	ifStick(stickNumber, s, false);
    }
    void ifStick(int stickNumber, CollectorState s, boolean resetTime) {
    	if(stick.getRawButton(stickNumber)) {
    		state = s;
    		refresh(resetTime);
    	}
    	smartDashboardSet("state", s);
    }
}
