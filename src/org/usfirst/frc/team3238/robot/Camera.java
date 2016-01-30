package org.usfirst.frc.team3238.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Point;
import com.ni.vision.VisionException;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

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
            int crosshairCenterY)
    {
        frontCam = NIVision.IMAQdxOpenCamera(frontCameraName,
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        backCam = NIVision.IMAQdxOpenCamera(backCameraName,
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        activeCam = backCam;
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        centerPoint = new Point(crosshairCenterX, crosshairCenterY);
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
        CameraServer.getInstance().setQuality(quality);
        CameraServer.getInstance().setSize(size);
        setPoints(centerPoint, 20);
        changeCam();
    }

    /**
     * Closes the feed for the currently active camera and opens the image for
     * the other camera
     */
    public void changeCam()
    {
        if(activeCam == frontCam)
        {
            newID = backCam;
        } else if(activeCam == backCam)
        {
            newID = frontCam;
        } else
        {
            DriverStation.reportError("No camera is active!", false);
            newID = frontCam;
        }
        NIVision.IMAQdxStopAcquisition(activeCam);
        NIVision.IMAQdxConfigureGrab(newID);
        NIVision.IMAQdxStartAcquisition(newID);
        activeCam = newID;
    }

    /**
     * Runs the main camera feed and sends it to the driver station
     * 
     * @throws VisionException
     *             If no camera can be found
     */
    public void stream() throws VisionException
    {
        NIVision.IMAQdxGrab(activeCam, frame, 1);
        imposeCrosshairs();
        CameraServer.getInstance().setImage(frame);
    }
    public void idle() throws VisionException {
    	NIVision.IMAQdxStopAcquisition(activeCam);
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
        NIVision.imaqDrawLineOnImage(frame, frame,
                NIVision.DrawMode.DRAW_INVERT, startPointH, endPointH, 0.0f);
        NIVision.imaqDrawLineOnImage(frame, frame,
                NIVision.DrawMode.DRAW_INVERT, startPointV, endPointV, 0.0f);
        NIVision.imaqDrawLineOnImage(frame, frame,
                NIVision.DrawMode.DRAW_INVERT, startPointHTwo, endPointHTwo,
                0.0f);
        NIVision.imaqDrawLineOnImage(frame, frame,
                NIVision.DrawMode.DRAW_INVERT, startPointVTwo, endPointVTwo,
                0.0f);
        NIVision.imaqDrawLineOnImage(frame, frame,
                NIVision.DrawMode.DRAW_INVERT, startPointHThree,
                endPointHThree, 0.0f);
        NIVision.imaqDrawLineOnImage(frame, frame,
                NIVision.DrawMode.DRAW_INVERT, startPointVThree,
                endPointVThree, 0.0f);
    }
}
