package org.firstinspires.ftc.teamcode;

import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.pedro.Constants;

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
        double forward = -gamepad1.left_stick_y;
        double lateral = gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;

        //stickSideways = gamepad1.left_stick_x * speedMultiplier;
        //stickForward = -gamepad1.left_stick_y * speedMultiplier;
        //stickSidewaysRotated = (stickSideways * cos(-adjustedHeading)) - (stickForward * Math.sin(-adjustedHeading));
        //stickForwardRotated = (stickSideways * Math.sin(-adjustedHeading)) + (stickForward * cos(-adjustedHeading));

        //follower.manual(forward, lateral, turn);
        //follower.update();

        /*
        if(manualDrive)
        {
            drive.setDrivePowers(new PoseVelocity2d(
                    new Vector2d(
                            stickSidewaysRotated,
                            stickForwardRotated
                    ),
                    -(gamepad1.right_stick_x * rotationMultiplier)
            ));
        }


 */

        if (gamepad1.right_bumper)
        {
            extras.setBallStop(ExtraOpModeFunctions.BallStopStates.OFF);
            extras.setIntake(ExtraOpModeFunctions.IntakeStates.REVERSE);
        }
        else if (gamepad1.right_trigger > 0)
        {

            extras.setIntake(ExtraOpModeFunctions.IntakeStates.FORWARD);
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
        if (gamepad1.aWasPressed())
        {
            extras.elevatorup();
        }
        if (gamepad1.bWasPressed())
        {
            extras.elevatordown();
        }
        if(gamepad1.y)
        {
            extras.clawopen();
        }
        else
        {
            extras.clawclose();
        }

        telemetry.addData("Elevator position", extras.elevator.getCurrentPosition());
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




