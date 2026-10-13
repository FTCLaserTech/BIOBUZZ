package org.firstinspires.ftc.teamcode;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class CornerPark extends OpMode
{
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();
    private enum LeftRight{LEFT,RIGHT}
    private LeftRight startorientation = LeftRight.LEFT;

    // Poses
    private final Pose startPoseLeft = poseFactory.of(134, 16,180);
    private final Pose scorePoseLeft = poseFactory.of(132, 20, 180);
    private final Pose startPoseRight = poseFactory.of(134, 24,180);
    private final Pose scorePoseRight = poseFactory.of(132, 20, 180);

    @Override
    public void init()
    {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
    }

    @Override
    public void init_loop()
    {
        if(gamepad2.leftBumperWasPressed())
        {
            if (startorientation == LeftRight.LEFT)
            {
                startorientation = LeftRight.RIGHT;
            }
            else
            {
                startorientation = LeftRight.LEFT;
            }
        }
        telemetry.addData("Startposition (Left Bumper): ", startorientation);
        telemetry.update();
    }

    @Override
    public void start()
    {
        follower.setPose(startPoseLeft);
        follower.update();        Path startToScore = null;
        if(startorientation == LeftRight.LEFT)
        {
            follower.setPose(startPoseLeft);
            follower.update();
            startToScore = Paths.line(startPoseLeft, scorePoseLeft).linear(startPoseLeft, scorePoseLeft);
        }
        else
        {
            follower.setPose(startPoseRight);
            follower.update();
            startToScore = Paths.line(startPoseRight, scorePoseRight).linear(startPoseRight, scorePoseRight);
        }

        Command park = sequential(
                follow(follower, startToScore)
        );

        schedule(park);
        //return startToScore;
    }

    @Override
    public void loop()
    {
        follower.update();
        Scheduler.execute();
        // add your other methods needed in the loop here

        telemetry.addData("X", follower.pose().x());
        telemetry.addData("Y", follower.pose().y());
        telemetry.addData("Heading", Math.toDegrees(follower.pose().heading()));
        telemetry.addData("Follower Mode", follower.mode());
        telemetry.update();
    }
}