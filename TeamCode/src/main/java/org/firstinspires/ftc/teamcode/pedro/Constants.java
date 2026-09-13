package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.OTOSConfig;
import com.pedropathing.revhub.localizers.OTOSLocalizer;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static Follower create(HardwareMap h)
    {
        return new Follower(new OTOSLocalizer(h,Constants.localizerConfig),
                new Mecanum(h, Constants.drivetrainConfig),
                new Foresight(Constants.foresightConfig));
        //return null;
    }

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("leftFront");
        c.frontRightName.set("rightFront");
        c.backLeftName.set("leftBack");
        c.backRightName.set("rightBack");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
       // c.manualBrakeMode.set(true);
    });

    public static OTOSConfig localizerConfig = new OTOSConfig(c -> {
        c.name.set("otos");
        c.linearScalar.set(1.0777109684380901);
        c.angularScalar.set(1.0256394205112171);
        c.offset.set(new Pose(-0.7449172613188977, 0.018022191806102362));
        c.linearUnit.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.2112154562284114);
                Controller secondaryTranslationalForward = Controller.proportional(0.07803846729546458);
                Controller primaryTranslationalLateral = Controller.proportional(0.31043553048806277);
                Controller secondaryTranslationalLateral = Controller.proportional(0.11469763352519352);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.01975290743198585));
                c.brake.set(Controller.proportionalFeedforward(0.01678997131718797));

                c.headingFeedback.set(Controller.proportional(1.84301516859235));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.2511186583152719, -0.06290582625766195));

                c.linearBrakeCoefficients.set(Matrix.diag(0.0792191229614805, 0.055900990891329494));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.001361557557632893, 0.0017500139663835688));

                c.maxAchievableForwardVelocity.set(60.314945435752215);
                c.maxAchievableStrafeVelocity.set(51.07418250766041);
                c.naturalForwardDeceleration.set(36.96643053088331);
                c.naturalStrafeDeceleration.set(65.22386873117604);
            }
    );
}