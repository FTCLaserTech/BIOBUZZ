package org.firstinspires.ftc.teamcode.auto;


import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public class AutoPositions
{
    // private final PoseFactory poseFactory = PoseFactory.degrees();

    public final Pose path1Start;
    public final Pose path1StartOPP;
    public final Pose scoringShootingPosition;
    public final Pose cornerPickupPos3;
    public final Pose point6;
    public final Pose point7;
    public final Pose point9Shoot1;
    public final Pose point9Shoot2;
    public final Pose point10;
    public final Pose flowerPickupPos1;
    public final Pose flowerPickupPos2;
    public final Pose parkingPosition1;
    public final Pose parkingPosition2;
    public final Pose parkingPosition3;
    public final Pose parkingPosition4;
    public final Pose flowerPickupOPP;
    public final Pose flowerPickupOPP2;


    public final Path moveToCornerFromScoringLaunch;
    public final Path moveFromCornerToScoring;
    public final Path reverseForUnderHive; // turn around to reverse to the other side
    public final Path shootOnOppositeSide; // Move to shooting zone on the other side
    public final Path goToOppositeFlower;
    public final Path shootFlower;
    public final Path moveToFlower;
    public final Path shootOnOppositeSide2;
    public final Path park1;
    public final Path park2;
    public final Path waitToShoot;
    public final Path moveFromOPPFlowerToOPPSide;
    public final Path shootOPP;


    public AutoPositions(PoseFactory poseFactory)
    {
        path1Start = poseFactory.of(108, 137.5, 0);
        path1StartOPP = poseFactory.of(109, 10,0);
        scoringShootingPosition = poseFactory.of(110, 134, 45);
        cornerPickupPos3 = poseFactory.of(131, 137, 0);
        point6 = poseFactory.of(108, 130, 90);
        point7 = poseFactory.of(108, 21, 90);
        point9Shoot1 = poseFactory.of(82.0599, 10, 180);
        point9Shoot2 = poseFactory.of(82.0599, 10, 90);
        point10 = poseFactory.of(85, 10, 90);
        flowerPickupPos1 = poseFactory.of(108, 106, 315);
        flowerPickupPos2 = poseFactory.of(124, 95, 0);
        flowerPickupOPP = poseFactory.of(103,18,0);
        flowerPickupOPP2 = poseFactory.of(95,20,270);
        parkingPosition1 = poseFactory.of(109,131,90);
        parkingPosition2 = poseFactory.of(127,50,90);
        parkingPosition3 = poseFactory.of(94,33,45);
        parkingPosition4 = poseFactory.of(129,23,0);



        moveToCornerFromScoringLaunch = Paths.path(
                Paths.line(path1Start, cornerPickupPos3).linear(path1Start, cornerPickupPos3));
        moveFromCornerToScoring = Paths.path(
                Paths.line(cornerPickupPos3, scoringShootingPosition).linear(cornerPickupPos3, scoringShootingPosition)); // go back to shoot
        moveToFlower = Paths.path(
                Paths.curve(path1Start, flowerPickupPos1, flowerPickupPos2).linear(path1Start, flowerPickupPos2));
        reverseForUnderHive = Paths.line(path1Start, point6).linear(path1Start, point6); // turn around to reverse to the other side
        shootOnOppositeSide = Paths.line(point6, point7).linear(point6, point7); // Move to shooting zone on the other side
        goToOppositeFlower = Paths.path(
                Paths.line(point7, point9Shoot1).linear(point7, point9Shoot1),// turn to flower to pick up
                Paths.line(point7, point9Shoot2).linear(point7, point9Shoot2)); // turn to flower to pick up
        shootFlower = Paths.line(flowerPickupPos2, scoringShootingPosition).linear(flowerPickupPos2, scoringShootingPosition); // turn back to shoot pollen from the flower
        shootOnOppositeSide2 = Paths.line(flowerPickupPos2, point10).linear(flowerPickupPos2, point10); // turn back to shoot pollen from the flower
        waitToShoot = Paths.path(
                Paths.curve(path1StartOPP,flowerPickupOPP,flowerPickupOPP2).linear(path1StartOPP, flowerPickupOPP2));
                //Paths.line(path1StartOPP, flowerPickupOPP, flowerPickupOPP2).linear(path1StartOPP, flowerPickupOPP2));
        moveFromOPPFlowerToOPPSide = Paths.path(
                Paths.line(flowerPickupOPP2, point7).linear(flowerPickupOPP2, point7),          // turn to flower to pick up
                Paths.line( point7, point6).linear(point7, point6));// turn to flower to pick up

        shootOPP = Paths.path(
                Paths.line(point6, scoringShootingPosition).linear(point6, scoringShootingPosition));

        park1 = Paths.path(
                Paths.line(scoringShootingPosition, parkingPosition1).linear(scoringShootingPosition, parkingPosition1),
                Paths.line(parkingPosition1, parkingPosition2).linear(parkingPosition1, parkingPosition2));

        park2 = Paths.path(
                Paths.line(flowerPickupOPP2,parkingPosition4).linear(flowerPickupOPP2, parkingPosition4));
        //Paths.line(parkingPosition3, parkingPosition4).linear(parkingPosition3, parkingPosition4));
    }
}
