package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

@Autonomous
public class ShootAuto extends OpMode
{
    private Follower follower;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private enum LeftRight
    {LEFT, RIGHT}

    private LeftRight startorientation = LeftRight.LEFT;

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
        /*
        if (gamepad2.left_trigger_pressed)
        {
            if (startorientation == LeftRight.LEFT)
            {
                startorientation = LeftRight.RIGHT;
            } else
            {
                startorientation = LeftRight.LEFT;
            }
        }
        telemetry.addData("Startposition (LT): ", startorientation);
        telemetry.update();
         */
    }


    private final Pose start = poseFactory.of(85, 132, 90);
    private final Pose path1Start = poseFactory.of(85, 132, 270);
    private final Pose point1 = poseFactory.of(85, 132, 270);
    private final Pose point2 = poseFactory.of(85, 127, 270);
    private final Pose point3Start = poseFactory.of(85, 127, 0);
    private final Pose point3 = poseFactory.of(140, 130, 90);
    private final Pose point4Start = poseFactory.of(140, 130, 0);
    private final Pose point4 = poseFactory.of(85, 132, 270);

    @Override
    public void start()
    {
        Path path1 = Paths.line(path1Start, point1).linear(path1Start, point1);
        Path path2 = Paths.line(point1, point2).linear(point1, point2);
        Path path3 = Paths.line(point3Start, point3).linear(point3Start, point3);
        Path path4 = Paths.line(point4Start, point4).linear(point4Start, point4);

        Command firstSequence = sequential(
                // turn on shooter,
                // wait for shooter to get up to speed,
                // shoot artifacts,
                // turn on intake
                // move to line and pickup artifacts
                follow(follower, path1)
        );

        schedule(firstSequence);

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