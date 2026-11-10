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

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.helpers.EvictingArray;
import org.firstinspires.ftc.vision.VisionPortal;
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
@Configurable
public class CameraFunctions
{
    private OpMode opMode;

    /*
     * Variables used for switching cameras.
     */
    private WebcamName webcam;
    //private WebcamName webcam2;

    /**
     * The variable to store our instance of the AprilTag processor.
     */
    private AprilTagProcessor aprilTag;
    //private AprilTagProcessor aprilTag2;

    /**
     * The variable to store our instance of the vision portal.
     */
    private VisionPortal visionPortal;
    //private VisionPortal visionPortal2;

    private int exposure, minExposure, maxExposure;
    private int gain, minGain, maxGain;

    private Position camera1Position = new Position(DistanceUnit.INCH,0.0,1.0,0.0,0);
    YawPitchRollAngles camera1Orientation = new YawPitchRollAngles(AngleUnit.DEGREES, 0.0, 25.0, 0.0, 0);

    private int redHive = 0;
    private int blueHive = 0;

    public enum HivePosition{NA, AUDIENCE, SCORING};
    public HivePosition redHivePosition = HivePosition.NA;
    public HivePosition blueHivePosition = HivePosition.NA;

    //@Override
    public CameraFunctions(HardwareMap hardwareMap, OpMode opModeIn)
    {
        opMode = opModeIn;

        // Create the AprilTag processor by using a builder.
        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(camera1Position, camera1Orientation)
                .build();

        webcam = hardwareMap.get(WebcamName.class, "Webcam 1");

        // Create the vision portal by using a builder.
        visionPortal = new VisionPortal.Builder()
                .setCamera(webcam)
                .setStreamFormat(VisionPortal.StreamFormat.MJPEG)
                .setCameraResolution(new Size(1280, 800))
                .addProcessor(aprilTag)
                .build();

        // Set Exposure and Gain.
        exposure = 2;
        gain = 50;
        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        exposureControl.setMode(ExposureControl.Mode.Manual);
        exposureControl.setExposure((long)exposure, TimeUnit.MILLISECONDS);
        //opMode.wait(20);
        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gain);
        //opMode.wait(20);

        // Save more CPU resources when camera is no longer needed.
        //visionPortal.close();

    }

    public HivePosition checkHivePosition(ExtraOpModeFunctions.TeamColor teamColor)
    {
        List<AprilTagDetection> currentDetections = null;
        currentDetections = aprilTag.getDetections();
        opMode.telemetry.addData("# AprilTags Detected", currentDetections.size());

        if(redHive > 0) {redHive -= 1;}
        else if(redHive < 0) {redHive += 1;}
        if(blueHive > 0) {blueHive -= 1;}
        else if(blueHive < 0) {blueHive += 1;}

        // Step through the list of detections and display info for each one.
        for (AprilTagDetection detection : currentDetections)
        {
            if (detection instanceof AprilTagSingleDetection)
            {
                ; // do nothing
            }
            else
            {
                AprilTagClusterDetection clusterDet = (AprilTagClusterDetection) detection;
                String tagName = clusterDet.metadata.name;
                opMode.telemetry.addLine(String.format("\n==== Tag Cluster (%s)", tagName));
                if(tagName.matches("RED AUDIENCE"))
                {
                    if(detection.ftcPose.z > 10.0){redHive += 2;}
                    else if(detection.ftcPose.z < -10.0){redHive -= 2;}
                }
                else if(tagName.matches("RED SCORING"))
                {
                    if(detection.ftcPose.z > 10.0){redHive -= 2;}
                    else if(detection.ftcPose.z < -10.0){redHive += 2;}
                }
                else if(tagName.matches("BLUE AUDIENCE"))
                {
                    if(detection.ftcPose.z > 10.0){blueHive += 2;}
                    if(detection.ftcPose.z < -10.0){blueHive -= 2;}
                }
                else if(tagName.matches("BLUE SCORING"))
                {
                    if(detection.ftcPose.z > 10.0){blueHive -= 2;}
                    if(detection.ftcPose.z < -10.0){blueHive += 2;}
                }

                //opMode.telemetry.addLine(String.format("Percent tags found: %d", clusterDet.percentClusterFound));
                opMode.telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)", detection.ftcPose.x, detection.ftcPose.y, detection.ftcPose.z));
                //opMode.telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)", detection.ftcPose.pitch, detection.ftcPose.roll, detection.ftcPose.yaw));
                //opMode.telemetry.addLine(String.format("RBE %6.1f %6.1f %6.1f  (inch, deg, deg)", detection.ftcPose.range, detection.ftcPose.bearing, detection.ftcPose.elevation));
            }
        }   // end for() loop

        if(redHive == 0)
            redHivePosition = HivePosition.NA;
        else if(redHive > 0)
            redHivePosition = HivePosition.AUDIENCE;
        else
            redHivePosition = HivePosition.SCORING;
        if(blueHive == 0)
            blueHivePosition = HivePosition.NA;
        else if(redHive > 0)
            blueHivePosition = HivePosition.AUDIENCE;
        else
            blueHivePosition = HivePosition.SCORING;

        if(teamColor == ExtraOpModeFunctions.TeamColor.RED)
            return(redHivePosition);
        else
            return(blueHivePosition);
    }   // end method checkHivePosition()

}   // end class
