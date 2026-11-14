package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.controllers.Controller;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Launcher
{
    private DcMotorEx launcher1;
    private DcMotorEx launcher2;
    private double targetRPM = 0.0;
    private double motorPower = 0.0;

    double maxLauncherRPM = 4500.0;  //RPM
    double launcherTicksPerRev = 28.0;
    double maxLauncherTPS = launcherTicksPerRev * maxLauncherRPM / 60; // 2800
    //private double shooterTargetVelocity = 0.0;

    public static double kS = 0.0;
    public static double kV = 0.00042;
    public static double bbTolerance = 0.0;

    private Controller launcherSFF = Controller.staticFeedforward(kS);
    private Controller launcherPFF = Controller.proportionalFeedforward(kV);

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

    public double updateLauncher()
    {
        double launcherActualRPM = getLauncherActualRPM();

        // Feed forward controller
        launcherSFF = Controller.staticFeedforward(kS);
        launcherPFF = Controller.proportionalFeedforward(kV);
        Controller launcherController = launcherSFF.plus(launcherPFF);
        double ffPower = launcherController.calculate(targetRPM, launcherActualRPM);

        // bang-bang controller
        double bbPower = 0;
        if(targetRPM<(launcherActualRPM*bbTolerance))
        {
            bbPower = 1;
        }

        motorPower = ffPower + bbPower;

        if(motorPower>1.0)
            motorPower = 1.0;
        else if (motorPower<-1.0)
            motorPower = -1.0;

        launcher1.setPower(motorPower);
        launcher2.setPower(motorPower);
        //launcher1.setPower(0.0);
        //launcher2.setPower(0.0);

        return(motorPower);
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

    public void setLauncherTargetRPM(double targetRPM)
    {
        if(targetRPM > maxLauncherRPM)
        {
            this.targetRPM = maxLauncherRPM;
        }
        else
        {
            this.targetRPM = targetRPM;
        }
    }

    public double getLauncherTargetRPM()
    {
        return(targetRPM);
    }

    public double getLauncherActualRPM()
    {
        //return(0);
        return((tps2rpm(launcher1.getVelocity()) + tps2rpm(launcher2.getVelocity()))/2);
    }

    public double tps2rpm(double tps)
    {

        return(tps/launcherTicksPerRev*60);
    }

}
