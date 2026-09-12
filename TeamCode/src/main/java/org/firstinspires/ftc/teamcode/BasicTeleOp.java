package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(group = "A")
public class BasicTeleOp extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private Follower follower;
    ExtraOpModeFunctions extras = new ExtraOpModeFunctions(hardwareMap);

    public static double headingScaler = 3.0;
    public static double positionScalerAim = 10.0;
    public static double positionScalerRange = 1.0;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init()
    {
        telemetry.addData("Status", "Initializing");

        follower = Constants.create(hardwareMap);

        /*
        extras.teamColor = extras.readTeamColor();
        if (extras.teamColor == ExtraOpModeFunctions.TeamColor.RED)
        {
            extras.lights.setLightColor(ExtraOpModeFunctions.Lights.Light_Red);
            extras.pattern = RevBlinkinLedDriver.BlinkinPattern.RED;
            extras.blinkinLedDriver.setPattern(extras.pattern);        }
        else
        {
            extras.lights.setLightColor(ExtraOpModeFunctions.Lights.Light_Blue);
            extras.pattern = RevBlinkinLedDriver.BlinkinPattern.BLUE;
            extras.blinkinLedDriver.setPattern(extras.pattern);
        }
        */

        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
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
        double forward = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        follower.manual(forward, lateral, turn);
        follower.update();
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

