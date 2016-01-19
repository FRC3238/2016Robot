package org.usfirst.frc.team3238.robot;

import com.ni.vision.NIVision;
import com.ni.vision.NIVision.Image;
import com.ni.vision.NIVision.Point;
import com.ni.vision.VisionException;

import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;

public class Camera
{
    private final int camOne;
    private final int camTwo;
    private int activeCam;
    private Image frame;
    private Point startPointH, endPointH, startPointV, endPointV,
            startPointHTwo, endPointHTwo, startPointVTwo, endPointVTwo,
            startPointHThree, endPointHThree, startPointVThree, endPointVThree;
    private int newID;

    Camera()
    {
        camOne = NIVision.IMAQdxOpenCamera("cam1",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        camTwo = NIVision.IMAQdxOpenCamera("cam2",
                NIVision.IMAQdxCameraControlMode.CameraControlModeController);
        activeCam = camTwo;
        frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
        // framePrime = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB,
        // 0);
        // framePrimeTwo =
        // NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB,
        // 0);
        startPointH = new Point(300, 240);
        endPointH = new Point(340, 240);
        startPointV = new Point(320, 220);
        endPointV = new Point(320, 260);
        startPointHTwo = new Point(300, 241);
        endPointHTwo = new Point(340, 241);
        startPointVTwo = new Point(321, 220);
        endPointVTwo = new Point(321, 260);
        startPointHThree = new Point(300, 239);
        endPointHThree = new Point(340, 239);
        startPointVThree = new Point(319, 220);
        endPointVThree = new Point(319, 260);
        CameraServer.getInstance().setQuality(30);
        CameraServer.getInstance().setSize(0);
    }

    public void init()
    {
        changeCam();
    }

    public void changeCam()
    {
        if(activeCam == camOne)
        {
            newID = camTwo;
        } else if(activeCam == camTwo)
        {
            newID = camOne;
        } else
        {
            DriverStation.reportError("No camera is active!", false);
        }
        NIVision.IMAQdxStopAcquisition(activeCam);
        NIVision.IMAQdxConfigureGrab(newID);
        NIVision.IMAQdxStartAcquisition(newID);
        activeCam = newID;
    }

    void idle()
    {
        try
        {
            NIVision.IMAQdxGrab(activeCam, frame, 1);
            NIVision.imaqDrawLineOnImage(frame, frame,
                    NIVision.DrawMode.DRAW_INVERT, startPointH, endPointH,
                    0.0f);
            NIVision.imaqDrawLineOnImage(frame, frame,
                    NIVision.DrawMode.DRAW_INVERT, startPointV, endPointV,
                    0.0f);
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
            CameraServer.getInstance().setImage(frame);
        } catch(VisionException exc)
        {
            DriverStation.reportError("Vision exception!:" + exc.getMessage(),
                    true);
        }
    }
}