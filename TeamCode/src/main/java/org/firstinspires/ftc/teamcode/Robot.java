package org.firstinspires.ftc.teamcode;

import com.bylazar.configurables.annotations.Configurable;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.robot.Ballstop;
import org.firstinspires.ftc.teamcode.robot.Intake;
import org.firstinspires.ftc.teamcode.robot.Launcher;
import org.firstinspires.ftc.teamcode.robot.Turret;

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
    private static Turret turret;

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


    boolean runLauncherBoolean = false;

}

