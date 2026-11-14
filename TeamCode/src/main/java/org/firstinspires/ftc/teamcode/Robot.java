package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robot.Ballstop;
import org.firstinspires.ftc.teamcode.robot.Intake;
import org.firstinspires.ftc.teamcode.robot.Launcher;

//import dev.nextftc.control.feedforward.SimpleFeedforward;
//import dev.nextftc.control.feedback.PIDController;

@Configurable
public class Robot
{
    //public HardwareMap hm = null;

    public enum RobotStartPosition {STRAIGHT, LEFT, RIGHT};
    public enum TeamColor{RED,BLUE};
    public TeamColor teamColor = TeamColor.BLUE;

    public static final double PI = 3.14159265;

    private static Launcher launcher;
    private static Intake intake;
    private static Ballstop ballStop;
    //private static Turret turret;

    //public SimpleFeedforward launcherController = new SimpleFeedforward(0.00042, 0.0, 0.0);

    public Robot(HardwareMap hardwareMap)
    {
        //hm = hardwareMap;

        launcher = new Launcher(hardwareMap);
        intake = new Intake(hardwareMap);
        ballStop = new Ballstop(hardwareMap);
        //turret = new Turret(hardwareMap);

    }

    public void ballsIntake()
    {
        ballStop.setBallStop(Ballstop.BallStopStates.STOPBALLS);
        intake.setIntake(Intake.IntakeStates.FORWARD);
    }
    public void ballsOuttake()
    {
        ballStop.setBallStop(Ballstop.BallStopStates.STOPBALLS);
        intake.setIntake(Intake.IntakeStates.REVERSE);
    }
    public void ballsShoot()
    {
        intake.setIntake(Intake.IntakeStates.FORWARD_SLOW);
        ballStop.setBallStop(Ballstop.BallStopStates.RELEASEBALLS);
    }
    public void ballsStop()
    {
        ballStop.setBallStop(Ballstop.BallStopStates.STOPBALLS);
        intake.setIntake(Intake.IntakeStates.OFF);
    }

    public double getLauncherTargetSpeed()
    {
        return(launcher.getLauncherTargetSpeed());
    }

    public void setLauncherTargetSpeed(double launcherSpeed)
    {
        launcher.setLauncherTargetSpeed(launcherSpeed);
    }

    public double getLauncherActualSpeed()
    {
        return(launcher.getLauncherActualSpeed());
    }

    public void updateLauncher()
    {
        launcher.setLauncherTargetSpeed(500);
    }

    public double adjustAngleForDriverPosition(double angle, RobotStartPosition robotStartPosition)
    {
        switch (robotStartPosition)
        {
            case STRAIGHT:
                angle = angle + PI/2;
                if(angle > (PI))
                    angle = angle - (PI*2);
                break;
            case LEFT:
                angle = angle - PI/2;
                if(angle < (-PI))
                    angle = angle + (PI*2);
                break;
            case RIGHT:
                angle = angle + PI/2;
                if(angle > (PI))
                    angle = angle - (PI*2);
                break;
        }
        return angle;
    }

    private static final String BASE_FOLDER_NAME = "Team14631";
    private static final String TEAM_LOG = "Team14631";


    double MIN_LAUNCHER_SPEED = 1000.0;
    double MAX_LAUNCHER_SPEED = 2000.0;
    public double distanceToLauncherSpeed(double robotTargetDistance, double robotTargetNormalVelocity)
    {
        // range to speed function
        double shooterTargetVelocity  = 977 + (6.56 * robotTargetDistance) + (0.00473 * robotTargetDistance * robotTargetDistance);

        // offset for robot velocity
        double rangeScale = 0.0;
        shooterTargetVelocity = shooterTargetVelocity + (robotTargetNormalVelocity * rangeScale);

        shooterTargetVelocity = clamp(shooterTargetVelocity,MAX_LAUNCHER_SPEED, MIN_LAUNCHER_SPEED);

        return(shooterTargetVelocity);
    }

    public double ballSpeedToLauncherSpeed(double ballSpeed)
    {
        double shooterTargetVelocity  = 960 - (3.23 * ballSpeed) + (0.0285 * ballSpeed * ballSpeed);
        shooterTargetVelocity = clamp(shooterTargetVelocity,MAX_LAUNCHER_SPEED, MIN_LAUNCHER_SPEED);
        return(shooterTargetVelocity);
    }

    public double hoodAngleToServoPosition(double hoodAngle)
    {
        // 1 60 - 0.5
        // 2 47 - 0.0

        // return y1 + ((y2 - y1) / (x2 - x1)) * (x - x1);

        double servoPosition = clamp( 0.5 + ((0.0 - 0.5) / (47 - 60)) * (hoodAngle - 60), 1.0,0.0);
        if (Double.isNaN(servoPosition))
        {
            servoPosition = 1.0;
        }
        return servoPosition;
    }

    public double distanceToBackboardPosition(double distance)
    {
        double position = 0.0;
        // range to
        if(distance > 90)
        {
            position = 0.0;
        }
        else
        {
            position = (1.06 - (0.02 * distance) + (0.0000919 * distance * distance));
            if (position > 0.5)
            {
                position = 0.5;
            }
        }
        return (position);
    }


    boolean runLauncherBoolean = false;

    public double clamp(double val, double max, double min)
    {
        if (val>max)
            return(max);
        if(val<min)
            return(min);
        return(val);
    }

    private double chYawInitial = 0.0;

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

