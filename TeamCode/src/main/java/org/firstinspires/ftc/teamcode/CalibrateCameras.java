/* Copyright (c) 2023 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import android.util.Size;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionPortal.CameraState;
import org.firstinspires.ftc.vision.apriltag.AprilTagClusterDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.List;
import java.util.concurrent.TimeUnit;

/*
 * This OpMode illustrates the basics of AprilTag recognition and pose estimation, using
 * two webcams.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 */
@TeleOp(name = "Calibrate Cameras", group = "Calibrate")
//@Disabled
public class CalibrateCameras extends LinearOpMode {

    /*
     * Variables used for switching cameras.
     */
    private WebcamName webcam1, webcam2;

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;

    private int exposure1, minExposure1, maxExposure1;
    private int exposure2, minExposure2, maxExposure2;
    private int gain1, minGain1, maxGain1;
    private int gain2, minGain2, maxGain2;

    private enum CameraModule{WEBCAM1, WEBCAM2};
    private CameraModule cameraModule = CameraModule.WEBCAM1;

    //@Override
    public void runOpMode()
    {
        ElapsedTime runtime = new ElapsedTime();
        double lastTime = 0.0;

        initAprilTag();

        // Establish Min and Max Gains and Exposure.  Then set a low exposure with high gain
        getCameraSetting();
        exposure1 = Math.min(5, minExposure1);
        exposure2 = exposure1;
        gain1 = 50;
        gain2 = gain1;
        setManualExposure(exposure1, gain1);

        // Wait for the DS start button to be touched.
        telemetry.addData("DS preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch START to start OpMode");
        telemetry.update();
        waitForStart();

        runtime.reset();

        while (opModeIsActive())
        {
            double currentTime = runtime.time();
            telemetry.addData("Loop Time (ms): ", (currentTime-lastTime)*1000.0);
            lastTime = currentTime;

            // Save CPU resources; can resume streaming when needed.
            telemetry.addData("DpadDown", "to stop streaming");
            telemetry.addData("DpadUp", "to start streaming");
            if (gamepad1.dpad_down)
            {
                visionPortal.stopStreaming();
            }
            else if (gamepad1.dpad_up)
            {
                visionPortal.resumeStreaming();
            }

            telemetry.addData("activeCamera", cameraModule);
            telemetry.addData("X = Webcam 1", "B = Webcam 2");
            if (visionPortal.getCameraState() == CameraState.STREAMING)
            {
                // If the left bumper is pressed, use Webcam 1.
                // If the right bumper is pressed, use Webcam 2.
                if (gamepad1.xWasPressed())
                {
                    visionPortal.setActiveCamera(webcam1);
                    cameraModule = CameraModule.WEBCAM1;
                    setManualExposure(exposure1, gain1);
                }
                else if (gamepad1.bWasPressed())
                {
                    visionPortal.setActiveCamera(webcam2);
                    cameraModule = CameraModule.WEBCAM2;
                    setManualExposure(exposure2, gain2);
                }
            }

            telemetry.addLine("Exposure: left bump/trig | Gain: right bump/trig");
            telemetry.addData("Exposure1", "%d  (%d - %d) | Gain1 %d  (%d - %d)", exposure1, minExposure1, maxExposure1, gain1, minGain1, maxGain1);
            telemetry.addData("Exposure2", "%d  (%d - %d) | Gain2 %d  (%d - %d)", exposure2, minExposure2, maxExposure2, gain2, minGain2, maxGain2);

            // look for clicks to change exposure
            if (gamepad1.leftBumperWasPressed())
            {
                if(cameraModule == CameraModule.WEBCAM1)
                {
                    exposure1 = Range.clip(exposure1 + 1, minExposure1, maxExposure1);
                    setManualExposure(exposure1, gain1);
                }
                else
                {
                    exposure2 = Range.clip(exposure2 + 1, minExposure2, maxExposure2);
                    setManualExposure(exposure2, gain2);
                }
            }
            else if (gamepad1.leftTriggerWasPressed())
            {
                if(cameraModule == CameraModule.WEBCAM1)
                {
                    exposure1 = Range.clip(exposure1 - 1, minExposure1, maxExposure1);
                    setManualExposure(exposure1, gain1);
                }
                else
                {
                    exposure2 = Range.clip(exposure2 - 1, minExposure2, maxExposure2);
                    setManualExposure(exposure2, gain2);
                }
            }

            // look for clicks to change the gain
            if (gamepad1.rightBumperWasPressed())
            {
                if(cameraModule == CameraModule.WEBCAM1)
                {
                    gain1 = Range.clip(gain1 + 1, minGain1, maxGain1);
                    setManualExposure(exposure1, gain1);
                }
                else
                {
                    gain2 = Range.clip(gain2 + 1, minGain2, maxGain2);
                    setManualExposure(exposure2, gain2);
                }
            }
            else if (gamepad1.rightTriggerWasPressed())
            {
                if(cameraModule == CameraModule.WEBCAM1)
                {
                    gain1 = Range.clip(gain1 - 1, minGain1, maxGain1);
                    setManualExposure(exposure1, gain1);
                }
                else
                {
                    gain2 = Range.clip(gain2 - 1, minGain2, maxGain2);
                    setManualExposure(exposure2, gain2);
                }
            }

            telemetryAprilTag();

            // Push telemetry to the Driver Station.
            telemetry.update();

            // Share the CPU.
            sleep(2);
        }

        // Save more CPU resources when camera is no longer needed.
        visionPortal.close();

    }   // end runOpMode()

    /**
     * Initialize the AprilTag processor.
     */
    private void initAprilTag() {

        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder().build();

        webcam1 = hardwareMap.get(WebcamName.class, "Webcam 1");
        webcam2 = hardwareMap.get(WebcamName.class, "Webcam 2");
        CameraName switchableCamera = ClassFactory.getInstance()
            .getCameraManager().nameForSwitchableCamera(webcam1, webcam2);

        // Create the vision portal by using a builder.
        visionPortal = new VisionPortal.Builder()
            .setCamera(switchableCamera)
            .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
            .setCameraResolution(new Size(1280, 800))
            .addProcessor(aprilTag)
            .build();

    }   // end method initAprilTag()

    /**
     * Add telemetry about AprilTag detections.
     */
    private void telemetryAprilTag() {

        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections) {
            if (detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;

                if (singleDet.metadata != null) {
                    telemetry.addLine(String.format("\n==== (ID %d) %s", singleDet.id, singleDet.metadata.name));
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                    telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
                } else {
                    telemetry.addLine(String.format("\n==== (ID %d) Unknown", singleDet.id));
                    telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", singleDet.center.x, singleDet.center.y));
                }
            }  else {
                AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                telemetry.addLine(String.format("\n==== Tag Cluster (%s)", clusterDet.metadata.name));
                telemetry.addLine(String.format("Percent tags found: %d", clusterDet.percentClusterFound));
                telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            }
        }   // end for() loop

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
        telemetry.addLine("RBE = Range, Bearing & Elevation");

    }   // end method telemetryAprilTag()

    /*
        Manually set the camera gain and exposure.
        Can only be called AFTER calling initAprilTag();
        Returns true if controls are set.
     */
    private boolean setManualExposure(int exposureMS, int gain) {
        // Ensure Vision Portal has been setup.
        if (visionPortal == null) {
            return false;
        }

        // Wait for the camera to be open
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                sleep(20);
            }
            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }

        // Set camera controls unless we are stopping.
        if (!isStopRequested())
        {
            // Set exposure.  Make sure we are in Manual Mode for these values to take effect.
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
                exposureControl.setMode(ExposureControl.Mode.Manual);
                sleep(50);
            }
            exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
            sleep(20);

            // Set Gain.
            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            gainControl.setGain(gain);
            sleep(20);
            return (true);
        } else {
            return (false);
        }
    }

    /*
        Read this camera's minimum and maximum Exposure and Gain settings.
        Can only be called AFTER calling initAprilTag();
     */
    private void getCameraSetting() {
        // Ensure Vision Portal has been setup.
        if (visionPortal == null) {
            return;
        }

        // Wait for the camera to be open
        if (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            telemetry.addData("Camera", "Waiting");
            telemetry.update();
            while (!isStopRequested() && (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING)) {
                sleep(20);
            }
            telemetry.addData("Camera", "Ready");
            telemetry.update();
        }

        // Get camera control values unless we are stopping.
        if (!isStopRequested()) {
            ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
            minExposure1 = (int)exposureControl.getMinExposure(TimeUnit.MILLISECONDS) + 1;
            maxExposure1 = (int)exposureControl.getMaxExposure(TimeUnit.MILLISECONDS);
            minExposure2 = minExposure1;
            maxExposure2 = maxExposure1;

            GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
            minGain1 = gainControl.getMinGain();
            maxGain1 = gainControl.getMaxGain();
            minGain2 = minGain1;
            maxGain2 = maxGain1;
        }
    }
}   // end class
