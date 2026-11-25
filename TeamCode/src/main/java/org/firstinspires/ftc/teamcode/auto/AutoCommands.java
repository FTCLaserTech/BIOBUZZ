package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.CameraFunctions;
import org.firstinspires.ftc.teamcode.Robot;

public class AutoCommands
{
    public static Command checkHivePosition(CameraFunctions cameraFunctions, Robot.TeamColor teamColor, CameraFunctions.HivePosition hivePosition)
    {
        return Command.build()
                //.setStart(() -> pid.setTarget(target))
                //.setExecute(() -> cameraFunctions.checkHivePosition(teamColor))
                .setDone(() -> cameraFunctions.checkHivePosition(teamColor) == hivePosition);
                //.setEnd(endCondition -> armMotor.setPower(0))
                //.requiring(cameraFunctions);
    }

}
