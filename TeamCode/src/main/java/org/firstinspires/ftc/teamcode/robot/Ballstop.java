package org.firstinspires.ftc.teamcode.robot;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

public class Ballstop
{
    private Servo ballStop;

    public Ballstop(HardwareMap hardwareMap)
    {
        ballStop = hardwareMap.get(Servo.class, "ballStop");

    }

    public enum BallStopStates {NA, RELEASEBALLS, STOPBALLS};
    BallStopStates currentBallStopState = BallStopStates.NA;
    public void setBallStop (BallStopStates ballStopState)
    {
        switch (ballStopState)
        {
            case RELEASEBALLS:
                if(ballStopState != currentBallStopState)
                {
                    ballStopOff();
                    currentBallStopState = ballStopState;
                }
                break;
            case STOPBALLS:
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
        ballStop.setPosition(0.525);
    }

    private void ballStopOff()
    {
        ballStop.setPosition(0.0);
    }
}
