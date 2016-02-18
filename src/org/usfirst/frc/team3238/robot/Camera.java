package org.usfirst.frc.team3238.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Point;
import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;
import java.util.ArrayList;

/**
 * Controls the two cameras, sends them to the Driver Station, and switches
 * between the two.
 * 
 * @author Aaron Jenson
 * 
 * @version 2.0
 */
public class Camera
{
    private final int frontCam;
    private final int backCam;
    private int activeCam;
    private Image frame;
    private Point centerPoint, startPointH, endPointH, startPointV, endPointV,
            startPointHTwo, endPointHTwo, startPointVTwo, endPointVTwo,
            startPointHThree, endPointHThree, startPointVThree, endPointVThree;
    private int newID;
    private int xDisp = 20, yDisp = 4, leng = 20;
    Joystick mainDriver;
    private ArrayList<Integer> relevance = new ArrayList<Integer>();
    private int infoDisplace = 100;
    /**
     * 
     * @param frontCameraName
     *            name of the camera for the front of the robot
     * @param backCameraName
     *            name of the camera for the back of the robot
     * @param crosshairCenterX
     *            x value to the center point for the crosshairs
     * @param crosshairCenterY
     *            y value to the center point for the crosshairs
     */
    Camera(String frontCameraName, String backCameraName, int crosshairCenterX,
            int crosshairCenterY, Joystick mainDriveStick)
    {
        frontCam = NIVision.IMAQdxOpenCamera(frontCameraName,
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        backCam = NIVision.IMAQdxOpenCamera(backCameraName,
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        activeCam = frontCam;
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        centerPoint = new Point(crosshairCenterX, crosshairCenterY);
        mainDriver = mainDriveStick;
    }

    /**
     * Initializes the Camera class with crosshair location, quality, and size
     * 
     * @param quality
     *            the amount of compression for the camera image
     * @param size
     *            the size of the image, either 0, 1, or 2, with 0 being the
     *            largest
     */
    public void init(int quality, int size)
    {
        try
        {
            newID = frontCam;
            addCams();
            CameraServer.getInstance().setQuality(quality);
            CameraServer.getInstance().setSize(size);
            setPoints(centerPoint, 20);
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }

    void addCams()
    {
        try
        {
            NIVision.IMAQdxStopAcquisition(activeCam);
            NIVision.IMAQdxConfigureGrab(newID);
            NIVision.IMAQdxStartAcquisition(newID);
            activeCam = newID;
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }

    /**
     * Closes the feed for the currently active camera and opens the image for
     * the other camera
     */
    public void changeCam()
    {
        try
        {
            if(activeCam == frontCam
                    && mainDriver.getRawButton(Constants.MainDriver.backChangeCamButton))
            {
                newID = backCam;
                addCams();
            } else if(activeCam == backCam
                    && mainDriver.getRawButton(Constants.MainDriver.frontChangeCamButton))
            {
                newID = frontCam;
                addCams();
            } else if(activeCam == backCam || activeCam == frontCam)
            {

            } else
            {
                DriverStation.reportError("No camera is active!", false);
                newID = frontCam;
            }
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }

    }

    /**
     * Runs the main camera feed and sends it to the driver station
     * 
     * @throws VisionException
     *             If no camera can be found
     */
    public void stream() throws VisionException
    {
        try
        {
            changeCam();
            NIVision.IMAQdxGrab(activeCam, frame, 1);
            //imposeCrosshairs();
            drawRelevantInfo(new Point(30, 30), new Point(40, 40), true);
            drawRelevantInfo(new Point(30, 60), new Point(30, 70), false);
            drawCrosshair(centerPoint, xDisp, yDisp, leng);
            CameraServer.getInstance().setImage(frame);
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), false);
        }
    }
    public void drawRelevantInfo(Point b, Point c, boolean a) {
    	drawFilledRectangle(b, c);
    	int r = (c.y-b.y)/2+b.y;
    	int q = (c.x-b.x)/2+b.x;
    	if(a) {
    		drawFilledRectangle(new Point(q-(c.y-b.y), r-(c.x-b.x)), new Point(q+(c.y-b.y), r+(c.x-b.x)));
    	}
    }
    void drawCrosshair(Point center, int xDisplace, int yDisplace, int length) {
    	try {
    	drawFilledRectangle(new Point((center.x + xDisplace), (center.y - yDisplace)), 
    				new Point((center.x + xDisplace + length), (center.y + yDisplace)));
    	drawFilledRectangle(new Point((center.x - yDisplace), (center.y + xDisplace)), 
					new Point((center.x + yDisplace), (center.y + xDisplace + length)));
    	drawFilledRectangle(new Point((center.x - yDisplace), (center.y - xDisplace)), 
					new Point((center.x + yDisplace), (center.y - xDisplace - length)));
    	drawFilledRectangle(new Point((center.x - xDisplace), (center.y - yDisplace)), 
					new Point((center.x - xDisplace - length), (center.y + yDisplace)));
    	} catch(Exception e) {
    		
    	}
    }
    void drawFilledRectangle(Point start, Point end) {
    	try {
    	NIVision.IMAQdxGrab(activeCam,  frame,  1);
    	
    	for(int i = 0; i < Math.abs(start.y - end.y); i++) {
    		
    		NIVision.imaqDrawLineOnImage(frame,  frame,  NIVision.DrawMode.DRAW_INVERT,  new Point(start.x, start.y + i*end.y/Math.abs(end.y)), 
    				new Point(end.x, start.y + i*end.y/Math.abs(end.y)), 0.0f);
    		}
    	} catch(Exception e) {
    		
    	}
    }
    /**
     * Stops the camera stream
     * 
     * @throws VisionException
     *             If something goes wrong
     */
    
    public void stop() throws VisionException
    {
        try
        {
            NIVision.IMAQdxStopAcquisition(activeCam);
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }

    /**
     * Sets the constant points for the crosshair lines
     * 
     * @param center
     *            the center point of the crosshairs
     * @param length
     *            the length of each hair from the center
     */
    private void setPoints(Point center, int length)
    {
        startPointH = new Point(center.x - length, center.y);
        endPointH = new Point(center.x + length, center.y);
        startPointV = new Point(center.x, center.y - length);
        endPointV = new Point(center.x, center.y + length);
        startPointHTwo = new Point(center.x - length, center.y + 1);
        endPointHTwo = new Point(center.x + length, center.y + 1);
        startPointVTwo = new Point(center.x + 1, 220);
        endPointVTwo = new Point(center.x + 1, 260);
        startPointHThree = new Point(center.x - length, center.y - 1);
        endPointHThree = new Point(center.x + length, center.y - 1);
        startPointVThree = new Point(center.x - 1, center.y - length);
        endPointVThree = new Point(center.x - 1, center.y + length);
    }

    /**
     * Places crosshairs on the camera feed, placed by the points in the
     * setPoints method
     * 
     * @throws VisionException
     *             If the camera feed is not available or the draw mode is
     *             invalid
     */
    private void imposeCrosshairs() throws VisionException
    {
        try
        {
        	drawLineOnImage(startPointH, endPointH);
            drawLineOnImage(startPointV, endPointV);
            drawLineOnImage(startPointHTwo,endPointHTwo);
            drawLineOnImage(startPointVTwo,endPointVTwo);
            drawLineOnImage(startPointHThree,endPointHThree);
            drawLineOnImage(startPointVThree,endPointVThree);
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }
    private void drawSolidRectangle(Point sp, Point ep) {
    	int dif = sp.x - ep.x;
    }
    private void drawLineOnImage(Point sp, Point ep) throws VisionException{
    	try {
    		NIVision.imaqDrawLineOnImage(frame, frame, NIVision.DrawMode.DRAW_INVERT,
    				sp, ep, 0.0f);
    	}catch(Exception e) {
    		DriverStation.reportError(e.getMessage(), true);
    	}
    }
}
