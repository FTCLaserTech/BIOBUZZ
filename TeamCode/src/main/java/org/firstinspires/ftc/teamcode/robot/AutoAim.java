package org.firstinspires.ftc.teamcode.robot;

import static java.lang.Math.atan2;
import static java.lang.Math.sqrt;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.Robot;

public class AutoAim
{
    private OpMode opMode;
    private final double LAUNCHER_LOW_RPM = 2400;
    private final double LAUNCHER_HIGH_RPM = 2600;

    private final double hiveXOffset = 12.758;
    private final double hiveYOffset = 20.007;
    public final Pose redAudience = new Pose(72-hiveXOffset,72-hiveYOffset);
    public final Pose redScoring = new Pose(72-hiveXOffset,72+hiveYOffset);
    public final Pose blueAudience = new Pose(72+hiveXOffset,72-hiveYOffset);
    public final Pose blueScoring = new Pose(72+hiveXOffset,72+hiveYOffset);
    private static double MIN_LAUNCHER_SPEED = 2400.0;
    private static double MAX_LAUNCHER_SPEED = 2600.0;
    public AutoAim(OpMode opMode)
    {
        this.opMode = opMode;
    }

    private double DISTANCE_THRESHOLD = 30.0;
    private double SPEED_THRESHOLD = 2000.0;

    public double calcLauncherSpeed(double robotTargetDistance, double robotTargetNormalVelocity)
    {
        double shooterTargetVelocity = MIN_LAUNCHER_SPEED;

        // we have 2 speeds, low (min) and high (max)
        // if robotTargetDistance > some distance or speed > some speed
        //    use the high speed
        // else
        //    use the low speed

        if(robotTargetDistance > 45)
            shooterTargetVelocity = MAX_LAUNCHER_SPEED;
        if(robotTargetNormalVelocity > 8)
            shooterTargetVelocity = MAX_LAUNCHER_SPEED;

        return(shooterTargetVelocity);
    }

    public double calcTurretPosition(Follower follower, Robot.TeamColor teamColor, double ballNormalVelocity)
    {
        double robotPositionX = follower.pose().x();
        double robotPositionY = follower.pose().y();
        double robotHeading = follower.pose().heading();
        double hiveXPosition = 0.0;
        double hiveYPosition = 0.0;
        double hiveDistanceActual = 0.0;
        double hiveHeadingActual = 0.0;

        if (teamColor == Robot.TeamColor.RED)
        {
            if (robotPositionY < 72)
            {
                hiveXPosition = redAudience.x();
                hiveYPosition = redAudience.y();
            }
            else
            {
                hiveXPosition = redScoring.x();
                hiveYPosition = redScoring.y();
            }
        }
        else
        {
            if (robotPositionY < 72)
            {
                hiveXPosition = blueAudience.x();
                hiveYPosition = blueAudience.y();
            }
            else
            {
                hiveXPosition = blueScoring.x();
                hiveYPosition = blueScoring.y();
            }
        }

        hiveDistanceActual = sqrt((hiveXPosition - robotPositionX) * (hiveXPosition - robotPositionX) + ( hiveYPosition - robotPositionY) * (hiveYPosition - robotPositionY));
        hiveHeadingActual = atan2(hiveYPosition - robotPositionY, hiveXPosition - robotPositionX);

        double coordinateTheta = follower.velocity().omega - hiveHeadingActual;
        double robotNormalVelocity = Math.sin(coordinateTheta)*follower.tangentialVelocity();
        double robotTangentVelocity = -Math.cos(coordinateTheta)*follower.tangentialVelocity();

        double turretVelCompOffset = calcTurretOffset(ballNormalVelocity,robotNormalVelocity,robotTangentVelocity);
        // calculate aim
        //double turretAngle = 0.0;
        double turretAngle = hiveHeadingActual - robotHeading + turretVelCompOffset;
        //lastLauncherHeading = launcherHeading;

        if(turretAngle > Math.PI)
        {
            turretAngle = turretAngle - 2 * Math.PI;
        }
        else if(turretAngle < -Math.PI)
        {
            turretAngle = turretAngle + 2 * Math.PI;
        }
        //robot.setTurretPosition(turretAngle);

        opMode.telemetry.addData("hiveDistanceActual: ", hiveDistanceActual);
        opMode.telemetry.addData("hiveHeadingActual: ", hiveHeadingActual);
        opMode.telemetry.addData("coordinateTheta: ", coordinateTheta);
        opMode.telemetry.addData("robotNormalVelocity: ", robotNormalVelocity);
        opMode.telemetry.addData("robotTangentVelocity: ", robotTangentVelocity);
        opMode.telemetry.addData("turretVelCompOffset: ", turretVelCompOffset);
        opMode.telemetry.addData("hiveDistanceActual: ", hiveDistanceActual);
        opMode.telemetry.addData("turretAngle: ", turretAngle);

        return(turretAngle);
    }
    public double calcTurretOffset(double ballNormalVelocity, double robotNormalVelocity, double robotTangetVelocity)
    {
        // x = distance to target
        // time = how long the ball takes to get to the target for current robot position
        // velN = speed normal to the target
        // velT = speed tangential to the target
        // double ivr = x / time + velN; // this is the ball normal velocity
        double normalVelocity = ballNormalVelocity + robotNormalVelocity;
        double turretVelCompOffset = Math.atan(robotTangetVelocity / normalVelocity);

        return(turretVelCompOffset);
    }

    public double clamp(double val, double max, double min)
    {
        if (val>max)
            return(max);
        if(val<min)
            return(min);
        return(val);
    }

    public double angleToSpeed(double angle)
    {
        // fitting a parabola through 3 points
        double x1 = 0; //turretGoodAngle;  // this is the minimum angle window
        double y1 = 0.13;  // this point sets the minimum speed
        double x2 = 12.0;  // this angle has the maximum speed
        double y2 = 0.5;  // this is the maximum speed - a servo max is 1
        double x3 = -x1;
        double y3 = y1;

        double y = y1*(angle-x2)*(angle-x3)/((x1-x2)*(x1-x3))
                +y2*(angle-x1)*(angle-x3)/((x2-x1)*(x2-x3))
                +y3*(angle-x1)*(angle-x2)/((x3-x1)*(x3-x2));

        if(y>y2)
        {
            y = y2;
        }
        if(angle>0)
            y = -y;
        return(y);
    }
}
