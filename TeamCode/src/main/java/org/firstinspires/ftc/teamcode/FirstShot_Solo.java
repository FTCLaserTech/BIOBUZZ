package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.match;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Auto.AutoPositions;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.EnumMap;
import java.util.Map;

@Autonomous
public class FirstShot_Solo extends OpMode
{
    private Follower follower;
    private Robot robot;
    private CameraFunctions cameraFunctions;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private AutoPositions autoPositions;
    private enum LeftRight
    {LEFT, RIGHT}

    private LeftRight startorientation = LeftRight.LEFT;

    @Override
    public void init()
    {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        robot = new Robot(hardwareMap);
        cameraFunctions = new CameraFunctions(hardwareMap, this);
        cameraFunctions.stopStreaming();
        autoPositions = new AutoPositions(poseFactory);
    }

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

        telemetry.update();
    }


    /*
    EnumMap<CameraFunctions.HivePosition, Command> hiveCases = new EnumMap<>(CameraFunctions.HivePosition.class);
    hiveCases.put(CameraFunctions.HivePosition.SCORING, stayshoot);
    hiveCases.put(CameraFunctions.HivePosition.NA, underHive);
    hiveCases.put(CameraFunctions.HivePosition.AUDIENCE, underHive);

     */

    @Override
    public void start()

    {
        cameraFunctions.resumeStreaming();

        Command scoringShoot2 = sequential(
                follow(follower, autoPositions.shootFlower),
                instant(() -> robot.ballsShoot()),
                //extras.IntakeForward,
                waitMs(2000),
                instant(() -> robot.ballsStop())
                );

        Command scoringShootOpposite = sequential(
                follow(follower, autoPositions.shootOnOppositeSide),
                instant(() -> robot.ballsShoot()),
                //extras.IntakeForward,
                waitMs(2000),
                instant(() -> robot.ballsStop())
                );




                EnumMap<CameraFunctions.HivePosition, Command> hiveCases2 = new EnumMap<>(Map.of(
                CameraFunctions.HivePosition.NA, scoringShootOpposite,
                CameraFunctions.HivePosition.AUDIENCE, scoringShootOpposite,
                CameraFunctions.HivePosition.SCORING, scoringShoot2
        ));
        Command checkhiveposition2 = match(() -> cameraFunctions.checkHivePosition(robot.teamColor), hiveCases2);

        Command stayshoot = sequential(
                instant(() -> robot.ballsShoot()),
                //extras.IntakeForward,
                waitMs(2000),
                instant(() -> robot.ballsStop()),
                follow(follower, autoPositions.moveToFlower),
                checkhiveposition2
                );

        Command underHive = sequential(
                waitMs(1000),
                follow(follower, autoPositions.reverseForUnderHive),
                waitMs(2000),
                //extras.IntakeForward,
                instant(() -> robot.ballsOuttake()),
                follow(follower, autoPositions.shootOnOppositeSide),
                instant(() -> robot.ballsStop()),
                waitMs(2000),
                follow(follower, autoPositions.goToOppositeFlower),
                waitMs(2000),
                follow(follower, autoPositions.shootFlower),
                //follow(follower, path11),  //Put it back in
                //follow(follower, path12) // put back in
                instant(() -> robot.ballsStop())
        );

        EnumMap<CameraFunctions.HivePosition, Command> hiveCases1 = new EnumMap<>(Map.of(
                CameraFunctions.HivePosition.NA, underHive,
                CameraFunctions.HivePosition.AUDIENCE, underHive,
                CameraFunctions.HivePosition.SCORING, stayshoot
        ));
        Command checkhiveposition1 = match(() -> cameraFunctions.checkHivePosition(robot.teamColor), hiveCases1);


        Command firstSequence = sequential(
                //follow(follower, autoPositions.moveToScoringLaunchPosition),
                instant(() -> robot.ballsShoot()),
                //extras.IntakeForward,
                waitMs(2000),
                instant(() -> robot.ballsStop()),
                follow(follower, autoPositions.moveToCornerFromScoringLaunch),
                waitMs(1000),
                follow(follower, autoPositions.moveFromCornerToScoring),
                waitMs(1000),
                checkhiveposition1,
                waitMs(1000)
        );


        follower.setPose(autoPositions.path1Start);
        follower.update();
        //firstSequence.schedule();
        schedule(firstSequence);

        robot.setLauncherTargetSpeed(2500);


    }
    //CameraFunctions.HivePosition hivePosition = CameraFunctions.HivePosition.NA;

    @Override
    public void loop()
    {
        CameraFunctions.HivePosition hivePosition = cameraFunctions.checkHivePosition(robot.teamColor);
        telemetry.addData("Hive Position: ", hivePosition);

        // recalc and update the launcher speed each time through the loop
        robot.updateLauncher();
        telemetry.addData("Launcher Target Speed: ", robot.getLauncherTargetSpeed());
        telemetry.addData("Launcher Actual Speed: ", robot.getLauncherActualSpeed());

        follower.update();
        Scheduler.execute();

        //cameraFunctions.checkHivePosition(robot.teamColor);
        // add your other methods needed in the loop here

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());

        telemetry.update();
    }
}