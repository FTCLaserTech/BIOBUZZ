package org.firstinspires.ftc.teamcode.auto;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

public class AutoPositions
{
   // private final PoseFactory poseFactory = PoseFactory.degrees();

    public final Pose path1Start;
    public final Pose scoringShootingPosition;
    public final Pose cornerPickupPos3;
    public final Pose point6;
    public final Pose point7;
    public final Pose point9Shoot1;
    public final Pose point9Shoot2;
    public final Pose point10;
    public final Pose flowerPickupPos1;
    public final Pose flowerPickupPos2;
    public final Pose parkingPosition;


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


    public AutoPositions(PoseFactory poseFactory)
    {
        path1Start = poseFactory.of(108, 137.5, 0);
        scoringShootingPosition = poseFactory.of(110, 134, 45);
        cornerPickupPos3 = poseFactory.of(131, 137, 0);
        point6 = poseFactory.of(108, 130, 90);
        point7 = poseFactory.of(108, 21, 90);
        point9Shoot1 = poseFactory.of(82.0599, 10, 180);
        point9Shoot2 = poseFactory.of(82.0599, 10, 90);
        point10 = poseFactory.of(85, 10, 90);
        flowerPickupPos1 = poseFactory.of(108, 106, 315);
        flowerPickupPos2 = poseFactory.of(124, 95, 0);
        parkingPosition = poseFactory.of(124,36,0);


        moveToCornerFromScoringLaunch = Paths.path(
                Paths.line(path1Start, cornerPickupPos3).linear(path1Start, cornerPickupPos3));
        moveFromCornerToScoring = Paths.path(
                Paths.line(cornerPickupPos3, scoringShootingPosition).linear(cornerPickupPos3, scoringShootingPosition)); // go back to shoot
        moveToFlower = Paths.path(
                Paths.curve(path1Start, flowerPickupPos1, flowerPickupPos2).linear(path1Start,flowerPickupPos2));
        reverseForUnderHive = Paths.line(path1Start, point6).linear(path1Start, point6); // turn around to reverse to the other side
        shootOnOppositeSide = Paths.line(point6, point7).linear(point6, point7); // Move to shooting zone on the other side
        goToOppositeFlower =
                Paths.line(point7, point9Shoot1).linear(point7, point9Shoot1); // turn to flower to pick up
        Paths.line(point7, point9Shoot2).linear(point7, point9Shoot2); // turn to flower to pick up

        shootFlower = Paths.line(flowerPickupPos2, scoringShootingPosition).linear(flowerPickupPos2, scoringShootingPosition); // turn back to shoot pollen from the flower
        shootOnOppositeSide2 = Paths.line(flowerPickupPos2, point10).linear(flowerPickupPos2, point10); // turn back to shoot pollen from the flower
        park1 = Paths.line(scoringShootingPosition, parkingPosition).linear(scoringShootingPosition, parkingPosition); // turn back to shoot pollen from the flower
        park2 = Paths.line(point7, parkingPosition).linear(point7, parkingPosition); // turn back to shoot pollen from the flower

    }}
