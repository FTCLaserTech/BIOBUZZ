package org.firstinspires.ftc.teamcode;

import com.pedropathing.controllers.Controller;
import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.ManualDrive;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.pedro.Constants;

//import dev.nextftc.control.geometry.PoseVelocity2d;
//import dev.nextftc.control.geometry.Vector2d;


@TeleOp(group = "A")
public class ShooterTester extends OpMode
{
    private ElapsedTime runtime = new ElapsedTime();

    public DcMotorEx launcher1;
    public DcMotorEx launcher2;

    double launcherVelocity = 0.0;
    double storedlauncherVelocity = 0.8;

    double maxLauncherRPM = 4500.0;  //RPM
    double launcherTicksPerRev = 28.0;
    //double maxLauncherTPS = launcherTicksPerRev * maxLauncherRPM / 60; // 2800
    double maxLauncherTPS = 1.0;
    //private double shooterTargetVelocity = 0.0;

    public static double kP = 0.00001;
    public static double kI = 0.0;
    public static double kD = 0.0;
    public static double kS = 0.0;
    public static double kV = 0.00042;

    private Controller launcherPID = Controller.pid(kP, kI, kD);
    private Controller launcherSFF = Controller.staticFeedforward(kS);
    private Controller launcherPFF = Controller.proportionalFeedforward(kV);
    private Controller launcherController = launcherPID.plus(launcherSFF).plus(launcherPFF);

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init()
    {
        launcher1 = hardwareMap.get(DcMotorEx.class, "launcher1");
        launcher1.setDirection(DcMotorEx.Direction.REVERSE);
        launcher1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        //launcher1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //launcher1.setVelocity(0.0);
        launcher1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launcher1.setPower(0.0);

        launcher2 = hardwareMap.get(DcMotorEx.class, "launcher2");
        launcher2.setDirection(DcMotorEx.Direction.FORWARD);
        launcher2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        //launcher2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //launcher2.setVelocity(0.0);
        launcher2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        launcher2.setPower(0.0);

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
        Launcher
        */
        if (gamepad1.dpadDownWasPressed())
        {
            launcherVelocity = launcherVelocity - 0.025;
            if (launcherVelocity < 0.0 )
            {
                launcherVelocity = 0.0;
            }

        }
        if (gamepad1.dpadUpWasPressed())
        {
            launcherVelocity = launcherVelocity + 0.025;
            if(launcherVelocity > maxLauncherTPS)
            {
                launcherVelocity = maxLauncherTPS;
            }
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

        launcher1.setPower(launcherVelocity);
        launcher2.setPower(launcherVelocity);

        // recalc and update the launcher speed each time through the loop
        telemetry.addData("Launcher Target Speed: ", launcherVelocity);
        //telemetry.addData("Launcher Set Speed: ", extras.updateLauncherSpeed());
        telemetry.addData("Launcher Actual Speed: ", (launcher1.getVelocity() + launcher2.getVelocity())/2);
        //extras.updateLauncherSpeed();

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
