package org.usfirst.frc.team3238.robot;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.CANTalon;
public class Autonomous {
	private static String[] defenses;
	private static CANTalon[] talons;
	private static final double encoderValues = 0.0;
	private final double bruteTime = 2.0, bruteForce = 0.82;
	private Timer t;
	public Autonomous(String[] z, CANTalon[] cans) {
		talons = cans;
		if(z.length != 5) {
			throw new IllegalArgumentException();
		} else {
		defenses = z;
		}
	}
	
	public Autonomous() {
		defenses = new String[] {null, null, null, null, "lowbar"};
	}
	
	public void setDefense(String[] z) {
		if(z.length != 5) {
			throw new IllegalArgumentException();
		} else {
		defenses = z;
		}
	}
	public String getDefense(int index) {
		return defenses[index];
	}
	
	/*@function moves robot at a high speed over the defense in front
	 * of it, only succeeds at moat, rock wall, rough terrain, ramparts
	 * low bar:: fails at cheval, portcullis, drawbridge, sallyport
	 * covers 3 classes, guaranteed 12 pts reach and cross
	  */
	
	public void bruteAutonomous() {		
		t.reset();
		t.start();
		while(t.get() <= bruteTime) {
			setDriveTalons(bruteForce);
		}
			setDriveTalons(0.0);
	}
	
	public void setDriveTalons(double speed) {		
			for(int i = 0; i < 4; i++) {
				talons[i].set(speed);
			}		
	}
	public void nullReferenceChecker() {
		for(int i = 0; i < defenses.length; i++) {
			if(defenses[i] == null) {
				defenses[i] = "fill";
			}
		}
		
	}
	
}
