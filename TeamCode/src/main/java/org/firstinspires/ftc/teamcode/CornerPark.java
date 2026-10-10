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
    //private final Pose startPose = startPoseLeft;
    //private final Pose scorePose = scorePoseLeft;
    //private final Pose parkPose = poseFactory.of(136, 15, 180);

    /*
    // Path methods
    private Path startToScore() {
        return line(startPose, scorePose).linear(startPose, scorePose);
    }

    private Command autoRoutine() {
        return sequential(
                follow(follower, startToScore())
                // Add mechanism commands here.
                //follow(follower, park())
        );
    }
     */

    @Override
    public void init()
    {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        //follower.setPose(startPoseLeft);
        //follower.update();
    }

    @Override
    public void init_loop()
    {
        if(gamepad2.left_trigger_pressed)
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
        telemetry.addData("Startposition (LT): ", startorientation);
        telemetry.update();
    }

    @Override
    public void start()
    {
        Path startToScore = null;
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