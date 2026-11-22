package org.firstinspires.ftc.teamcode;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.robot.AutoAim;

import com.pedropathing.follower.ManualDrive;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

//import dev.nextftc.control.geometry.PoseVelocity2d;
//import dev.nextftc.control.geometry.Vector2d;


@TeleOp(group = "A")
public class BasicTeleOp extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();
    private Follower follower;
    private Robot robot;
    private CameraFunctions cameraFunctions;
    private AutoAim autoAim;

    enum AutoManual {AUTO, MANUAL};
    AutoManual autoManual = AutoManual.MANUAL;

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

    //double launcherVelocity = 0.0;
    double storedlauncherVelocity = 800.0;


    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init()
    {
        //telemetry.addData("Status", "Initializing");

        follower = Constants.create(hardwareMap);
        robot = new Robot(hardwareMap);
        cameraFunctions = new CameraFunctions(hardwareMap, this);
        autoAim = new AutoAim(this);


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
        // select the color
        telemetry.addLine("X to switch color");
        telemetry.addData("Color: ", robot.teamColor);
        if(gamepad1.xWasPressed())
        {
            if(robot.teamColor == Robot.TeamColor.BLUE)
                robot.teamColor = Robot.TeamColor.RED;
            else
                robot.teamColor = Robot.TeamColor.BLUE;
        }

        // turn on/off streaming and check the hive position if on
        if(cameraFunctions.isStreamingOn())
        {
            telemetry.addLine("Y to turn OFF streaming");
            telemetry.addData("Hive Position: ", cameraFunctions.checkHivePosition(robot.teamColor));
            if(gamepad1.yWasPressed())
            {
                cameraFunctions.stopStreaming();
            }
        }
        else
        {
            telemetry.addLine("Y to turn ON streaming");
            if(gamepad1.yWasPressed())
            {
                cameraFunctions.resumeStreaming();
            }
        }

        telemetry.addData("GP2 X to switch Auto/Manual: ", autoManual);
        if(gamepad2.xWasPressed())
        {
            if(autoManual == AutoManual.AUTO)
                autoManual = AutoManual.MANUAL;
            else
                autoManual = AutoManual.AUTO;
        }

        telemetry.update();
    }

    /*
     * Code to run ONCE when the driver hits START
     */
    @Override
    public void start()
    {
        runtime.reset();

        //extras.setLauncherTargetSpeed(500);
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
        if(false) // false = robot centric; true = field centric
        {
            DrivePowers powers = ManualDrive.fieldCentric(
                    -gamepad1.left_stick_y,
                    -gamepad1.left_stick_x,
                    -gamepad1.right_stick_x,
                    follower.pose().heading()
            );
            follower.manual(powers);
            follower.update();
        }
        else
        {
            follower.manual(-gamepad1.left_stick_y, -gamepad1.left_stick_x, -gamepad1.right_stick_x);
            follower.update();
        }

        telemetry.addData("GP2 X to switch Auto/Manual: ", autoManual);
        if(gamepad2.xWasPressed())
        {
            if(autoManual == AutoManual.AUTO)
                autoManual = AutoManual.MANUAL;
            else
                autoManual = AutoManual.AUTO;
        }

        Pose currentPose = follower.pose();
        if(autoManual == AutoManual.AUTO)
        {
            // calculate some values needed for auto pose
            // which hive is target?
            // robot-target distance (X-Y projection, no Z)
            // robot-target normal velocity
            // robot-target tanget velocity
        }

        /*
        Launcher
        */
        // add a manual to auto switch
        if(autoManual == AutoManual.MANUAL)
        {
            if (gamepad1.dpadDownWasPressed())
            {
                robot.setLauncherTargetRPM(robot.getLauncherTargetRPM() - 25.0);
            }
            if (gamepad1.dpadUpWasPressed())
            {
                robot.setLauncherTargetRPM(robot.getLauncherTargetRPM() + 25.0);
            }

            // launcher  function
            if (gamepad1.xWasPressed())
            {
                if (robot.getLauncherTargetRPM() == 0.0)
                {
                    robot.setLauncherTargetRPM(storedlauncherVelocity);
                } else
                {
                    storedlauncherVelocity = robot.getLauncherTargetRPM();
                    robot.setLauncherTargetRPM(0.0);
                }
            }
            //robot.setLauncherTargetRPM(launcherVelocity);
        }
        else // auto launcher
        {
            robot.setLauncherTargetRPM(autoAim.calcLauncherSpeed(0.0,0.0));
        }
        // recalc and update the launcher speed each time through the loop
        robot.updateLauncher();
        telemetry.addData("Launcher Target RPM: ", robot.getLauncherTargetRPM());
        telemetry.addData("Launcher Actual RPM: ", robot.getLauncherActualRPM());

        /*
        Turret
        */
        double turretAngle = 0.0;
        if(autoManual == AutoManual.MANUAL)
        {
            // use gp2 joystick to set turret position?
            ;
        }
        else // auto turret
        {
            turretAngle = autoAim.calcTurretPosition(follower, robot.teamColor, robot.getBallVelocity());
        }

        /*
        Intake and Ballstop
        */
        if (gamepad1.right_bumper)
        {
            robot.ballsIntake();
        }
        else if (gamepad1.right_trigger > 0)
        {
            robot.ballsShoot();
        }
        else if (gamepad1.left_trigger > 0)
        {
            robot.ballsOuttake();
        }
        else
        {
            robot.ballsStop();
        }

        if(gamepad2.aWasPressed())
        {
            follower.setPose(new Pose(144.0,0.0,0.0));
        }
        if(gamepad2.bWasPressed())
        {
            follower.setPose(new Pose(144.0,144.0,0.0));
        }
        if(gamepad2.xWasPressed())
        {
            follower.setPose(new Pose(0.0,0.0,180.0));
        }
        if(gamepad2.yWasPressed())
        {
            follower.setPose(new Pose(0.0,144.0, 180.0));
        }

        //telemetry.addData("Launcher On: ", launcherOn);

        //telemetry.addData("launcher target velocity: ", launcherVelocity);
        //telemetry.addData("launcher1 velocity: ", extras.launcher1.getVelocity());
        //telemetry.addData("launcher2 velocity: ", extras.launcher2.getVelocity());
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
