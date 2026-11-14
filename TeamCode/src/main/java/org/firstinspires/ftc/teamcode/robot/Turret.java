package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Turret
{
    private DcMotorEx turret;
    private double turretTargetPositionDegrees = 0.0;
    public Turret(HardwareMap hardwareMap)
    {
        turret = hardwareMap.get(DcMotorEx.class, "turret");
        turret.setDirection(DcMotorEx.Direction.REVERSE);
        turret.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        turret.setTargetPosition(0);
        turret.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turret.setVelocity(0.0);
    }

    public void updateTurret()
    {
        ;
    }
    public void setTurretTargetPositionDegrees(double turretTargetPositionDegrees)
    {
        this.turretTargetPositionDegrees = turretTargetPositionDegrees;
    }

    public double getTurretTargetPositionDegrees()
    {
        return(turretTargetPositionDegrees);
    }

    public void homeTurret()
    {
        ;
    }
}
