package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

//import dev.nextftc.control.geometry.PoseVelocity2d;
//import dev.nextftc.control.geometry.Vector2d;


@TeleOp(group = "A")
//@Disabled
public class AprilTagTester extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private int exposure, minExposure, maxExposure;
    private int gain, minGain, maxGain;

    private CameraFunctions cameraFunctions;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init()
    {
        cameraFunctions = new CameraFunctions(hardwareMap, this);

        exposure = cameraFunctions.getExposure();
        gain = cameraFunctions.getGain();
        minExposure = 1;
        maxExposure = 100;
        minGain = 1;
        maxGain = 100;

    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit START
     */
    @Override
    public void init_loop()
    {
        ;
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start()
    {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits START but before they hit STOP
     */
    @Override
    public void loop()
    {
        telemetry.addLine("Exposure: left bump/trig | Gain: right bump/trig");
        telemetry.addData("Exposure", "%d  (%d - %d) | Gain1 %d  (%d - %d)", exposure, minExposure, maxExposure, gain, minGain, maxGain);

        // look for clicks to change exposure
        if (gamepad1.leftBumperWasPressed())
        {
            exposure = Range.clip(exposure + 1, minExposure, maxExposure);
            cameraFunctions.setManualExposure(exposure, gain);
        }
        else if (gamepad1.leftTriggerWasPressed())
        {
            exposure = Range.clip(exposure - 1, minExposure, maxExposure);
            cameraFunctions.setManualExposure(exposure, gain);
        }

        // look for clicks to change the gain
        if (gamepad1.rightBumperWasPressed())
        {
            gain = Range.clip(gain + 1, minGain, maxGain);
            cameraFunctions.setManualExposure(exposure, gain);
        }
        else if (gamepad1.rightTriggerWasPressed())
        {
            gain = Range.clip(gain - 1, minGain, maxGain);
            cameraFunctions.setManualExposure(exposure, gain);
        }

        // recalc and update the launcher speed each time through the loop

        telemetry.addData("Hive Position: ", cameraFunctions.checkHivePosition(Robot.TeamColor.BLUE));
        telemetry.update();
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop()
    {
        ;
    }
}
