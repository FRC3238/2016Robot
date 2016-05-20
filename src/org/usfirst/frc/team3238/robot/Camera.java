package org.usfirst.frc.team3238.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Point;
import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.Joystick;

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
    private int frontCam;
    private int backCam;
    private int activeCam;
    private Image frame;
    private Point centerPointTower, centerPointShooter, startPointH, endPointH,
            startPointV, endPointV, startPointHTwo, endPointHTwo,
            startPointVTwo, endPointVTwo, startPointHThree, endPointHThree,
            startPointVThree, endPointVThree;
    private int newID;

    Joystick stick;

    /**
     * 
     * @param frontCameraName
     *            name of the camera for the front of the robot
     * @param backCameraName
     *            name of the camera for the back of the robot
     * @param crosshairCenterX
     *            x value to the center point for the crosshairs
     * @param crosshairCenterYTower
     *            y value to the center point for the crosshairs
     */
    Camera(Joystick stickOne)
    {
        try
        {
            frontCam = NIVision.IMAQdxOpenCamera(Constants.Camera.frontCamName,
                    NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            backCam = NIVision.IMAQdxOpenCamera(Constants.Camera.rearCamName,
                    NIVision.IMAQdxCameraControlMode.CameraControlModeController);
            activeCam = frontCam;
            frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
            centerPointTower = new Point(Constants.Camera.crosshairCenterXTower,
                    Constants.Camera.crosshairCenterYTower);
            centerPointShooter = new Point(
                    Constants.Camera.crosshairCenterXShooter,
                    Constants.Camera.crosshairCenterYShooter);
            stick = stickOne;
            addCams();
        } catch(Exception e)
        {
            DriverStation.reportError("Camera Error: " + e.getMessage(), true);
        }
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
            changeCam(stick.getThrottle());
            CameraServer.getInstance().setQuality(quality);
            CameraServer.getInstance().setSize(size);
            setPoints(centerPointTower, 20);
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }
    /**
     * Starts the camera feed and determines where the crosshair should be placed to best help the driver
     */ 
    void addCams()
    {
        try
        {
            if(newID != activeCam)
            { 
                NIVision.IMAQdxStopAcquisition(activeCam); //Stops current camera feed and starts other camera feed
                NIVision.IMAQdxConfigureGrab(newID);
                NIVision.IMAQdxStartAcquisition(newID);
                if(newID == frontCam) //if taking camera input from the camera looking at the tower then draw a crosshair aligned with the tower
                {
                    setPoints(centerPointShooter,
                            Constants.Camera.crosshairLength);
                } else if(newID == backCam) //if taking camera input from the collector camera draw a crosshair in the center of the screen
                {
                    setPoints(centerPointTower,
                            Constants.Camera.crosshairLength);
                }
                activeCam = newID;
            }
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }

    /**
     * Closes the feed for the currently active camera and opens the image for
     * the other camera
     * 
     * @param camSwitch usually called with the joystick throttle returning a value between -1.0 and 1.0, determines which camera 
     *        to use
     */
    public void changeCam(double camSwitch)
    {
        try
        {
            if(camSwitch > 0)
            {
                newID = backCam;
                addCams();
            } else if(camSwitch < 0)
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
            changeCam(stick.getThrottle());
            NIVision.IMAQdxGrab(activeCam, frame, 1);
            imposeCrosshairs();
            CameraServer.getInstance().setImage(frame);
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), false);
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
            NIVision.imaqDrawLineOnImage(frame, frame,
                    NIVision.DrawMode.DRAW_VALUE, startPointH, endPointH,
                    255.0f);
            NIVision.imaqDrawLineOnImage(frame, frame,
                    NIVision.DrawMode.DRAW_VALUE, startPointV, endPointV,
                    255.0f);
            NIVision.imaqDrawLineOnImage(frame, frame,
                    NIVision.DrawMode.DRAW_VALUE, startPointHTwo, endPointHTwo,
                    255.0f);
            NIVision.imaqDrawLineOnImage(frame, frame,
                    NIVision.DrawMode.DRAW_VALUE, startPointVTwo, endPointVTwo,
                    255.0f);
            NIVision.imaqDrawLineOnImage(frame, frame,
                    NIVision.DrawMode.DRAW_VALUE, startPointHThree,
                    endPointHThree, 255.0f);
            NIVision.imaqDrawLineOnImage(frame, frame,
                    NIVision.DrawMode.DRAW_VALUE, startPointVThree,
                    endPointVThree, 255.0f);
        } catch(Exception e)
        {
            DriverStation.reportError(e.getMessage(), true);
        }
    }
}
