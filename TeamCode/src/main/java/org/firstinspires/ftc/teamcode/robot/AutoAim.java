package org.firstinspires.ftc.teamcode.robot;

public class AutoAim
{
    private double minLauncherSpeed = 0.0;
    private double maxLauncherSpeed = 0.0;
    public AutoAim(double minLauncherSpeed, double maxLauncherSpeed)
    {
        this.minLauncherSpeed = minLauncherSpeed;
        this.maxLauncherSpeed = maxLauncherSpeed;
    }

    private double DISTANCE_THRESHOLD = 30.0;
    private double SPEED_THRESHOLD = 2000.0;

    public double calcLauncherSpeed(double robotTargetDistance, double robotTargetNormalVelocity)
    {
        double shooterTargetVelocity = minLauncherSpeed;

        // we have 2 speeds, low (min) and high (max)
        // if robotTargetDistance > some distance or speed > some speed
        //    use the high speed
        // else
        //    use the low speed

        if(robotTargetDistance > 35)
            shooterTargetVelocity = maxLauncherSpeed;
        if(robotTargetNormalVelocity > 8)
            shooterTargetVelocity = maxLauncherSpeed;

        return(shooterTargetVelocity);
    }

    public double calcTurretPosition(double ballNormalVelocity, double robotNormalVelocity, double robotTangetVelocity)
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
