package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.controllers.Controller;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Launcher
{
    private DcMotorEx launcher1;
    private DcMotorEx launcher2;

    double maxLauncherRPM = 4500.0;  //RPM
    double launcherTicksPerRev = 28.0;
    double maxLauncherTPS = launcherTicksPerRev * maxLauncherRPM / 60; // 2800
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

    private double launcherTargetSpeed = 0.0;

    public Launcher(HardwareMap hardwareMap)
    {
        launcher1 = hardwareMap.get(DcMotorEx.class, "launcher1");
        launcher1.setDirection(DcMotorEx.Direction.REVERSE);
        launcher1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        launcher1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcher1.setVelocity(0.0);
        //launcher1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //launcher1.setPower(0.0);

        launcher2 = hardwareMap.get(DcMotorEx.class, "launcher2");
        launcher2.setDirection(DcMotorEx.Direction.FORWARD);
        launcher2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        launcher2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcher2.setVelocity(0.0);
        //launcher2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //launcher2.setPower(0.0);

    }

    public double updateLauncherSpeed()
    {
        launcherPID = Controller.pid(kP, kI, kD);
        launcherSFF = Controller.staticFeedforward(kS);
        launcherPFF = Controller.proportionalFeedforward(kV);
        launcherController = launcherPID.plus(launcherSFF).plus(launcherPFF);

        //double power = launcherController.calculate(getLauncherSpeed(), launcherTargetSpeed);
        double power = launcherController.calculate(launcherTargetSpeed, getLauncherSpeed());

        /*
        double power = launcherController.calculate(new KineticState(
                launcher1.getCurrentPosition(),
                launcher1.getVelocity()
        ));
        // clip power to -1 to +1 range
        */

        launcher1.setPower(power);
        launcher2.setPower(power);
        //launcher1.setPower(0.0);
        //launcher2.setPower(0.0);

        return(power);
    }

    public double getLauncherActualSpeed()
    {
        //return(0);
        return((launcher1.getVelocity() + launcher2.getVelocity())/2);
    }

    public void setLauncherTargetSpeed(double launcherSpeed)
    {
        if(launcherSpeed > maxLauncherTPS)
        {
            launcherTargetSpeed = maxLauncherTPS;
        }
        else
        {
            launcherTargetSpeed = launcherSpeed;
        }

        //launcher1.setVelocity(launcherSpeed);
        //launcher2.setVelocity(launcherSpeed);
    }

    public double getLauncherTargetSpeed()
    {
        return(launcherTargetSpeed);
    }

    public double setLauncherSpeed()
    {
        launcherPID = Controller.pid(kP, kI, kD);
        launcherSFF = Controller.staticFeedforward(kS);
        launcherPFF = Controller.proportionalFeedforward(kV);
        launcherController = launcherPID.plus(launcherSFF).plus(launcherPFF);

        //double power = launcherController.calculate(getLauncherSpeed(), launcherTargetSpeed);
        double power = launcherController.calculate(launcherTargetSpeed, getLauncherSpeed());

        /*
        double power = launcherController.calculate(new KineticState(
                launcher1.getCurrentPosition(),
                launcher1.getVelocity()
        ));
        // clip power to -1 to +1 range
        */

        launcher1.setPower(power);
        launcher2.setPower(power);
        //launcher1.setPower(0.0);
        //launcher2.setPower(0.0);

        return(power);
    }

    public double getLauncherSpeed()
    {
        //return(0);
        return((launcher1.getVelocity() + launcher2.getVelocity())/2);
    }

}
