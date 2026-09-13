package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.util.ElapsedTime.Resolution.SECONDS;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import com.qualcomm.robotcore.util.ElapsedTime;

import dev.nextftc.control.feedforward.SimpleFeedforward;
import dev.nextftc.control.feedback.PIDController;

public class ExtraOpModeFunctions
{
    //public HardwareMap hm = null;

    public enum RobotStartPosition {STRAIGHT, LEFT, RIGHT};
    public enum TeamColor{RED,BLUE};
    public TeamColor teamColor = TeamColor.RED;

    public static final double PI = 3.14159265;

    public DcMotorEx launcher1;
    public DcMotorEx launcher2;
    public static double launcherSupPosition = 0.0;
    public DcMotorEx intake;
    public DcMotorEx elevator;
    public static double elevatorup = 1.0;
    public static double elevatordown = 0.0;
    public Servo ballStop;
    public Servo claw;

    public SimpleFeedforward launcherController =
            new SimpleFeedforward(0.00042, 0.0, 0.0);

    double maxLauncherRPM = 4500.0;  //RPM
    double launcherTicksPerRev = 28.0;
    double maxLauncherTPS = launcherTicksPerRev * maxLauncherRPM / 60; // 2800
    //private double shooterTargetVelocity = 0.0;


    public ExtraOpModeFunctions(HardwareMap hardwareMap)
    {
        //hm = hardwareMap;
        launcher1 = hardwareMap.get(DcMotorEx.class, "launcher1");
        launcher1.setDirection(DcMotorEx.Direction.REVERSE);
        launcher1.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        launcher1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcher1.setVelocity(0.0);
        //launcher1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //launcher1.setPower(0.0);

        launcher2 = hardwareMap.get(DcMotorEx.class, "launcher2");
        launcher2.setDirection(DcMotorEx.Direction.FORWARD);
        launcher2.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        launcher2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcher2.setVelocity(0.0);
        //launcher2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //launcher2.setPower(0.0);

        intake = hardwareMap.get(DcMotorEx.class, "intake");
        intake.setDirection(DcMotorEx.Direction.FORWARD);
        intake.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        intake.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        intake.setVelocity(0.0);

        elevator = hardwareMap.get(DcMotorEx.class, "elevator");
        elevator.setDirection(DcMotorEx.Direction.REVERSE);
        elevator.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        elevator.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        elevator.setTargetPosition(0);
        elevator.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        elevator.setPower(1.0);

        ballStop = hardwareMap.get(Servo.class, "ballStop");

        claw = hardwareMap.get(Servo.class, "claw");


    }

    public enum IntakeStates {NA, OFF, FORWARD, REVERSE};
    IntakeStates currentIntakeState = IntakeStates.NA;
    public void setIntake(IntakeStates intakeState)
    {
        switch (intakeState)
        {
            case OFF:
                if(intakeState != currentIntakeState)
                {
                    intake.setPower(0.0);
                    currentIntakeState = intakeState;
                }
                break;

            case FORWARD:
                if(intakeState != currentIntakeState)
                {
                    intake.setPower(1.0);
                    currentIntakeState = intakeState;
                }
                break;

            case REVERSE:
                if(intakeState != currentIntakeState)
                {
                    intake.setPower(-1.0);
                    currentIntakeState = intakeState;
                }
                break;

            case NA:
                break;

        }
    }

    public enum BallStopStates {NA, OFF, ON};
    BallStopStates currentBallStopState = BallStopStates.NA;
    public void setBallStop (BallStopStates ballStopState)
    {
        switch (ballStopState)
        {
            case OFF:
                if(ballStopState != currentBallStopState)
                {
                    ballStopOff();
                    currentBallStopState = ballStopState;
                }
                break;
            case ON:
                if(ballStopState != currentBallStopState)
                {
                    ballStopOn();
                    currentBallStopState = ballStopState;
                }
                break;
            case NA:
                break;
        }
    }

    private void ballStopOn()
    {
        ballStop.setPosition(1.0);
    }

    private void ballStopOff()
    {
        ballStop.setPosition(0.3);
    }

    public void elevatorup()
    {
        elevator.setTargetPosition(3600);
    }

    public void elevatordown()
    {
        elevator.setTargetPosition(0);
    }

    public void clawopen()
    {
        claw.setPosition(0.35);
    }

    public void clawclose()
    {
        claw.setPosition(0.44);
    }


    public void setLauncher(double launcherSpeed)
    {
        /*
        double power = launcherController.calculate(new KineticState(
                launcher1.getCurrentPosition(),
                launcher1.getVelocity()
        ));
        launcher1.setPower(power);
        launcher2.setPower(power);
        */

        launcher1.setVelocity(launcherSpeed);
        launcher2.setVelocity(launcherSpeed);
    }

    public double getLauncherSpeed()
    {
        return(0);
        //return((launcher1.getVelocity()+ launcher2.getVelocity())/2);
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

