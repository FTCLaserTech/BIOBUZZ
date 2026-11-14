package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Intake
{
    private DcMotorEx intake;

    public Intake(HardwareMap hardwareMap)
    {
        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorEx.Direction.REVERSE);
        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setVelocity(0.0);
    }

    public enum IntakeStates {NA, OFF, FORWARD, FORWARD_SLOW, REVERSE};
    IntakeStates currentIntakeState = IntakeStates.NA;
    public void setIntake(IntakeStates intakeState)
    {
        switch (intakeState)
        {
            case OFF:
                if(intakeState != currentIntakeState)
                {
                    intake.setPower(0.0);
                    currentIntakeState = intakeState;
                }
                break;

            case FORWARD:
                if(intakeState != currentIntakeState)
                {
                    intake.setPower(1.0);
                    currentIntakeState = intakeState;
                }
                break;

            case FORWARD_SLOW:
                if(intakeState != currentIntakeState)
                {
                    intake.setPower(0.4);
                    currentIntakeState = intakeState;
                }
                break;

            case REVERSE:
                if(intakeState != currentIntakeState)
                {
                    intake.setPower(-1.0);
                    currentIntakeState = intakeState;
                }
                break;

            case NA:
                break;

        }
    }

}
