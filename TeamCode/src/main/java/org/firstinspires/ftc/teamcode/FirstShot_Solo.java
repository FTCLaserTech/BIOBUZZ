package org.firstinspires.ftc.teamcode;

import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.commands.Commands.instant;
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
public class FirstShot_Solo extends OpMode
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
    private final Pose point1 = poseFactory.of(87.5, 133.5, 270);
    private final Pose point2 = poseFactory.of(90, 130, 0);
    private final Pose point3 = poseFactory.of(105.0, 135, 0);
    private final Pose point4 = poseFactory.of(131, 135, 0);
    private final Pose point5 = poseFactory.of(85, 132, 270);
    private final Pose point6 = poseFactory.of(85, 130, 90);
    private final Pose point7 = poseFactory.of(78.0124, 10, 90);
    //private final Pose point8 = poseFactory.of(82.0599, 3.2636, 168.7254);
    private final Pose point9 = poseFactory.of(82.0599, 10, 0);
    private final Pose point10 = poseFactory.of(85, 130, 270);
    //private final Pose point10 = poseFactory.of(78.7785, 10, 0);
    //private final Pose point11 = poseFactory.of(85.2361, 131.9936, -92.8776);


    @Override
    public void start()
    {
        follower.setPose(path1Start);
        follower.update();
        Path path1 = Paths.line(path1Start, point1).linear(path1Start, point1); // move forward to shoot
        Path path2 = Paths.line(point1,point2).linear(point1,point2); // turn to human player zone
        Path path3 = Paths.curve(point2,point3, point4).linear(point2, point4); // pick up balls from zone
        Path path4 = Paths.line(point4, point2).linear(point4, point2); // go back to shoot
        Path path5 = Paths.line(point2, point1).linear(point2, point1); // shoot
        Path path6 = Paths.line(point1, point5).linear(point1, point5); // align to go to other side
        Path path7 = Paths.line(point5, point6).linear(point5, point6); // turn around to reverse to the other side
        Path path8 = Paths.line(point6, point7).linear(point6, point7); // Move to shooting zone on the other side
        Path path9 = Paths.line(point7, point9).linear(point7, point9); // turn to flower to pick up
        Path path10 = Paths.line(point9, point7).linear(point9, point7); // turn back to shoot pollen from the flower
        Path path11 = Paths.line(point7, point10).linear(point7, point10); // start heading back
        //Path path12 = Paths.line(point6, point10).linear(point6, point10); // back on the other side


        Command firstSequence = sequential(
                follow(follower, path1),
                instant(() -> extras.ballStopOff()),
                extras.IntakeForward,
                waitMs(2000),
                instant(() -> extras.ballStopOn()),
                follow(follower, path2),
                follow(follower, path3),
                waitMs(1000),
                follow(follower, path4),
                follow(follower, path5),
                instant(() -> extras.ballStopOff()),
                extras.IntakeForward,
                waitMs(1000),
                follow(follower, path6),
                waitMs(3000),
                follow(follower, path7),
                waitMs(2000),
                instant(() -> extras.ballStopOff()),
                follow(follower, path8),
                instant(() -> extras.ballStopOn()),
                waitMs(2000),
                follow(follower, path9),
                waitMs(2000),
                follow(follower, path10),
                follow(follower, path11),  //Put it back in
                //follow(follower, path12) // put back in
                extras.IntakeOff


        );

        schedule(firstSequence);

        extras.setLauncherTargetSpeed(2500);
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