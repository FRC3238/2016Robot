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

    private void setPower(double power)
    {
        if(stick.getPOV() == Constants.MainDriver.manualCollectIn)
            talon.set(-Constants.Collector.defaultPower);
        else if(stick.getPOV() == Constants.MainDriver.manualCollectOut)
            talon.set(Constants.Collector.defaultPower);
        else
            talon.set(power);
    }

    public void run()
    {
        switch(state)
        {
            case CENTERING:
            	
                setPower(Constants.Collector.centerPower);
                loweringToSwitch = false;
                
                switchWithCondition(0.05, CollectorState.LOWERING);
                
                break;
                
            case COLLECTING:
            	
                setPower(Constants.Collector.defaultPower);
                
                switchWithCondition(Constants.MainDriver.motorOff, CollectorState.DISABLED);
                switchWithCondition(!ballDetect.get(), CollectorState.CENTERING, true);
                switchWithCondition(Constants.MainDriver.autoEject, CollectorState.EJECTING);
                
                break;
                
            case DISABLED:
            	
                setPower(0.0);
                
                switchWithCondition(Constants.MainDriver.autoCollect, CollectorState.COLLECTING);
                switchWithCondition(Constants.MainDriver.autoEject, CollectorState.EJECTING);
               
                break;
                
            case EJECTING:
            	
                setPower(-Constants.Collector.defaultPower);
                
                switchWithCondition(Constants.MainDriver.autoCollect, CollectorState.COLLECTING);
                switchWithCondition(Constants.MainDriver.motorOff, CollectorState.DISABLED);
                
                break;
                
            case HOLDING:
            	
                setPower(0.0);
                
                switchWithCondition(Constants.MainDriver.shoot, CollectorState.SHOOTING, true);
                switchWithCondition(Constants.MainDriver.autoEject, CollectorState.EJECTING);
                
                break;
                
            case LOWERING:
            	
                setPower(-Constants.Collector.centerPower);
                if(!ballDetect.get()) {
                	loweringToSwitch = true;
                }
                
                switchWithCondition(ballDetect.get() && loweringToSwitch, CollectorState.HOLDING, false);    
                switchWithCondition(0.15, CollectorState.HOLDING);
                
                break;
                
            case SHOOTING:
            	
                setPower(Constants.Collector.defaultPower);
                
                switchWithCondition(1.0, CollectorState.DISABLED);
                
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
    void switchWithCondition(boolean isTrue, CollectorState s, boolean resetTime) {
    	if(isTrue) {
    		state = s;
    		refresh(resetTime);
    	}
    	smartDashboardSet("state", s);
    }
    void switchWithCondition(double time, CollectorState s) {
    	if(timer.get() >= time) {
    		state = s;
    	}
    	smartDashboardSet("state", s);
    }
    void switchWithCondition(int stickNumber, CollectorState s) {
    	switchWithCondition(stickNumber, s, false);
    }
    void switchWithCondition(int stickNumber, CollectorState s, boolean resetTime) {
    	if(stick.getRawButton(stickNumber)) {
    		state = s;
    		refresh(resetTime);
    	}
    	smartDashboardSet("state", s);
    }
}
