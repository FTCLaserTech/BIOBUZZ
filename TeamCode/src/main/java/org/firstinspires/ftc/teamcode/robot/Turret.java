package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.List;

public class Turret
{
    private DcMotorEx turretMotor;
    private TouchSensor turretHomeSensor;
    private HardwareMap hardwareMap;

    private double motorGear = 20.0;
    private double turretGear = 176.0;

    double turretTicksPerRev = 28.0; // Gobilda 5203 - 6000
    private double turretTargetPositionDegrees = 0.0;
    public Turret(HardwareMap hardwareMap)
    {
        this.hardwareMap = hardwareMap;

        turretMotor = hardwareMap.get(DcMotorEx.class, "turret");
        turretMotor.setDirection(DcMotorEx.Direction.REVERSE);
        turretMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        turretMotor.setTargetPosition(0);
        turretMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        turretMotor.setVelocity(0.0);

        turretHomeSensor = hardwareMap.get(TouchSensor.class, "turretHomeSensor");
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
        //start motor
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        turretMotor.setPower(0.14);
        List<LynxModule> allHubs = hardwareMap.getAll(LynxModule.class);
        for (LynxModule hub : allHubs)
        {
            hub.clearBulkCache();
        }
        while (!turretHomeSensor.isPressed())
        {
            for (LynxModule hub : allHubs)
            {
                hub.clearBulkCache();
            }
            //localLop.telemetry.addLine("Homing...");
            //localLop.telemetry.addData("turretHomeSensor", turretHomeSensor.isPressed());
            //localLop.telemetry.update();
        }
        //stop
        turretMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        turretMotor.setPower(0.0);
        //localLop.telemetry.addLine("Homing Complete");
        //localLop.telemetry.update();
        //safeWaitSeconds(5.0);
        //setTurretMode(turretMode);
        turretMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
}
