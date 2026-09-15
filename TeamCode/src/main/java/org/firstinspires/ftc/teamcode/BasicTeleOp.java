package org.firstinspires.ftc.teamcode;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.robotcore.internal.opmode.TelemetryImpl;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import com.pedropathing.follower.ManualDrive;
import com.qualcomm.hardware.rev.RevBlinkinLedDriver;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import static java.lang.Math.cos;

import dev.nextftc.control.geometry.PoseVelocity2d;
import dev.nextftc.control.geometry.Vector2d;


@TeleOp(group = "A")
public class BasicTeleOp extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private Follower follower;
    private ExtraOpModeFunctions extras;

    public static double headingScaler = 3.0;
    public static double positionScalerAim = 10.0;
    public static double positionScalerRange = 1.0;
    int IMUReset = 0;
    double stickForward;
    double stickSideways;
    double stickForwardRotated;
    double stickSidewaysRotated;
    double imuHeading = 0.0;
    double adjustedHeading = 0.0;
    double speedMultiplier = 1.0;
    double rotationMultiplier = 1.0;

    double launcherVelocity = 0.0;
    double storedlauncherVelocity = 2600.0;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init()
    {
        //telemetry.addData("Status", "Initializing");

        follower = Constants.create(hardwareMap);
        extras = new ExtraOpModeFunctions(hardwareMap);


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
        //telemetry.addData("Status", "Initialized");
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
        /*
        Drive
        */
        DrivePowers powers = ManualDrive.fieldCentric(
                -gamepad1.left_stick_y,
                -gamepad1.left_stick_x,
                -gamepad1.right_stick_x,
                follower.pose().heading()
        );
        follower.manual(powers);
        follower.update();

        /*
        Launcher
        */
        if (gamepad1.dpadDownWasPressed())
        {
            launcherVelocity = launcherVelocity - 25.0;
            if (launcherVelocity < 0.0 )
            {
                launcherVelocity = 0.0;
            }

        }
        if (gamepad1.dpadUpWasPressed())
        {
            launcherVelocity = launcherVelocity + 25.0;
        }

        // launcher  function
        if (gamepad1.xWasPressed())
        {
            if(launcherVelocity == 0.0)
            {
                launcherVelocity = storedlauncherVelocity;
            }
            else
            {
                storedlauncherVelocity = launcherVelocity;
                launcherVelocity = 0.0;
            }
        }

        extras.setLauncher(launcherVelocity);

        /*
        Intake and Ballstop
        */
        if (gamepad1.right_bumper)
        {
            extras.setBallStop(ExtraOpModeFunctions.BallStopStates.OFF);
            extras.setIntake(ExtraOpModeFunctions.IntakeStates.REVERSE);
        }
        else if (gamepad1.right_trigger > 0)
        {

            extras.setIntake(ExtraOpModeFunctions.IntakeStates.FORWARD_SLOW);
            extras.setBallStop(ExtraOpModeFunctions.BallStopStates.OFF);

        }
        else if (gamepad1.left_trigger > 0)
        {
            extras.setIntake(ExtraOpModeFunctions.IntakeStates.FORWARD);
            extras.setBallStop(ExtraOpModeFunctions.BallStopStates.ON);

        }
        else
        {
            extras.setIntake(ExtraOpModeFunctions.IntakeStates.OFF);
        }

        /*
        Elevator
        */
        if (gamepad1.aWasPressed())
        {
            extras.elevatorup();
        }
        if (gamepad1.bWasPressed())
        {
            extras.elevatordown();
        }

        /*
        claw
        */

        if(gamepad1.y)
        {
            extras.clawopen();
        }
        else
        {
            extras.clawclose();
        }

        //telemetry.addData("Launcher On: ", launcherOn);


        telemetry.addData("Elevator position: ", extras.elevator.getCurrentPosition());
        telemetry.addData("launcher target velocity: ", launcherVelocity);
        telemetry.addData("launcher1 velocity: ", extras.launcher1.getVelocity());
        telemetry.addData("launcher2 velocity: ", extras.launcher2.getVelocity());
        telemetry.update();
    }


    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop()
    {
        ;
    }}




