package org.firstinspires.ftc.teamcode;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.waitMs;
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
    private ExtraOpModeFunctions extras;
    private final PoseFactory poseFactory = PoseFactory.degrees();

    private enum LeftRight
    {LEFT, RIGHT}

    private LeftRight startorientation = LeftRight.LEFT;

    @Override
    public void init()
    {
        Scheduler.reset();

        follower = Constants.create(hardwareMap);
        extras = new ExtraOpModeFunctions(hardwareMap);
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


    private final Pose path1Start = poseFactory.of(87.5, 134, 270);
    private final Pose point1 = poseFactory.of(87.5, 132, 270);
    private final Pose point2 = poseFactory.of(90, 130, 0);
    private final Pose point3 = poseFactory.of(105.0, 135, 0);
    private final Pose point4 = poseFactory.of(131, 135, 0);
    private final Pose point5 = poseFactory.of(87.5, 132, 270);



    @Override
    public void start()
    {
        follower.setPose(path1Start);
        follower.update();
        Path path1 = Paths.line(path1Start, point1).linear(path1Start, point1);
        Path path2 = Paths.line(point1,point2).linear(point1,point2);
        Path path3 = Paths.curve(point2,point3, point4).linear(point2, point4);
        Path path4 = Paths.line(point4, point2).linear(point4, point2);
        Path path5 = Paths.line(point2, point1).linear(point2, point1);


        Command firstSequence = sequential(
                // wait for shooter to get up to speed,
                // shoot artifacts,
                // turn on intake
                // move to line and pickup artifacts
                follow(follower, path1),
                waitMs(1000),
                // turn off intake
                // move back to shooting position
                follow(follower, path2),
                follow(follower, path3),
                follow(follower, path4),
                follow(follower, path5)

        );




        schedule(firstSequence);

        extras.setLauncherTargetSpeed(500);
    }


    @Override
    public void loop()
    {
        // recalc and update the launcher speed each time through the loop
        telemetry.addData("Launcher Target Speed: ", extras.getLauncherTargetSpeed());
        telemetry.addData("Launcher Set Speed: ", extras.updateLauncherSpeed());
        telemetry.addData("Launcher Actual Speed: ", extras.getLauncherSpeed());
        //extras.updateLauncherSpeed();

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