package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.robot.Launcher;

//import dev.nextftc.control.geometry.PoseVelocity2d;
//import dev.nextftc.control.geometry.Vector2d;


@TeleOp(group = "A")
public class ShooterTester2 extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    private Launcher launcher;

    public static double launcherRPMIncrement = 1.0;

    double targetLauncherRPM = 0.0;
    double storedlauncherVelocity = 0.8;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init()
    {
        launcher = new Launcher(hardwareMap);
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
        // Set Launcher RPM
        if (gamepad1.dpadDownWasPressed())
        {
            targetLauncherRPM = targetLauncherRPM - launcherRPMIncrement;
        }
        if (gamepad1.dpadUpWasPressed())
        {
            targetLauncherRPM = targetLauncherRPM + launcherRPMIncrement;
        }
        launcher.setLauncherTargetRPM(targetLauncherRPM);
        targetLauncherRPM = launcher.getLauncherTargetRPM();

        // Set Launcher On/Off
        if (gamepad1.xWasPressed())
        {
            if(targetLauncherRPM == 0.0)
            {
                targetLauncherRPM = storedlauncherVelocity;
            }
            else
            {
                storedlauncherVelocity = targetLauncherRPM;
                targetLauncherRPM = 0.0;
            }
        }

        double motorPower = launcher.updateLauncher();

        // recalc and update the launcher speed each time through the loop
        telemetry.addLine("X: On/Off ");
        telemetry.addLine("Dpad Up/Down: Faster/Slower");
        telemetry.addData("Launcher Target Speed: ", targetLauncherRPM);
        telemetry.addData("Launcher Actual Speed: ", launcher.getLauncherActualRPM());
        telemetry.addData("Launcher Motor Power: ", motorPower);

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
