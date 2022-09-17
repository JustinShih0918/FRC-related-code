package frc.robot.component;

import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.LinearFilter;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.GenericHID.Hand;
import edu.wpi.first.wpilibj.controller.PIDController;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Pose2d;
import edu.wpi.first.wpilibj.geometry.Rotation2d;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.util.Units;
import frc.robot.Robot;
import frc.robot.xbox;

public class drivebase {
    public static Encoder leftencoder;
    public static Encoder rightencoder;

    public static AHRS gyro;
    public static DifferentialDrive drive;

    public static WPI_VictorSPX right_1;
    public static WPI_VictorSPX right_2;
    public static WPI_VictorSPX left_1;
    public static WPI_VictorSPX left_2;

    public static SpeedController leftmotor;
    public static SpeedController rightmotor;

    protected static DifferentialDriveOdometry odometry;

    protected static RamseteController ramseteController = new RamseteController();
    protected static DifferentialDriveKinematics kinematic = new DifferentialDriveKinematics(0.6);

    protected static Field2d field = new Field2d();
    protected static Field2d trajField = new Field2d();

    private static double kP = 0.68;
    private static double kI = 0.3;
    private static double kD = 0;
    private static double boostr = 1;
    private static double boostl = 1;

    protected static LinearFilter l_filter = LinearFilter.singlePoleIIR(0.1, 0.02);
    protected static LinearFilter r_filter = LinearFilter.singlePoleIIR(0.1, 0.02);

    protected static PIDController leftPID = new PIDController(kP, kI, kD);
    protected static PIDController rightPID = new PIDController(kP, kI, kD);

    private static final int Left_1 = 6;
    private static final int Left_2 = 7;
    private static final int Right_1 = 5;
    private static final int Right_2 = 3;

    public static void init() {
        right_1 = new WPI_VictorSPX(Right_1);
        right_2 = new WPI_VictorSPX(Right_2);
        left_2 = new WPI_VictorSPX(Left_2);
        left_1 = new WPI_VictorSPX(Left_1);

        leftencoder = new Encoder(0, 1);
        rightencoder = new Encoder(2, 3);
        leftencoder.setDistancePerPulse(2 * Math.PI * Units.inchesToMeters(6) / 730); // 365*2
        rightencoder.setDistancePerPulse(2 * Math.PI * Units.inchesToMeters(6) / 730);
        leftencoder.setReverseDirection(true);

        gyro = new AHRS(SPI.Port.kMXP);
        gyro.reset();

        leftmotor = new SpeedControllerGroup(left_1, left_2);
        rightmotor = new SpeedControllerGroup(right_1, right_2);
        leftmotor.setInverted(true);
        rightmotor.setInverted(true);

        drive = new DifferentialDrive(leftmotor, rightmotor);

        odometry = new DifferentialDriveOdometry(Rotation2d.fromDegrees(0));
        SmartDashboard.putData("field", field);
        SmartDashboard.putData("trajField", trajField);
    }

    public static void resetPIDs() {
        leftPID.reset();
        rightPID.reset();
    }

    public static void resetFilters() {
        l_filter.reset();
        r_filter.reset();
    }

    public static void teleOp() {
        if(Robot.xbox_1.getBumper(Hand.kLeft)){
            boostl = 1.5;
        }else if(Robot.xbox_1.getBumper(Hand.kRight)){
            boostr = 1.5;
        }
        drive.tankDrive(Robot.xbox_1.leftSpeed()/1.5*boostl, Robot.xbox_1.rightSpeed()/1.5*boostr, false);
    }

    public static void track(double speed, double rotation, boolean input) {
        drive.arcadeDrive(speed, rotation, input);
    }

    public static void directControl(double left, double right) {
        drive.tankDrive(left, right, false);
    }

    public static void directVoltControl(double left, double right) {
        leftmotor.setVoltage(left);
        rightmotor.setVoltage(left);
        drive.feed();
    }

    public static void updateODO() {
        var gyroAngle = Rotation2d.fromDegrees(-gyro.getAngle());
        odometry.update(gyroAngle, leftencoder.getDistance(), rightencoder.getDistance());
        field.setRobotPose(odometry.getPoseMeters());

        SmartDashboard.putNumber("x", odometry.getPoseMeters().getX());
        SmartDashboard.putNumber("y", odometry.getPoseMeters().getY());
        SmartDashboard.putNumber("heading", odometry.getPoseMeters().getRotation().getDegrees());

        kP = SmartDashboard.getNumber("kP", kP);
        kI = SmartDashboard.getNumber("kI", kI);
        kD = SmartDashboard.getNumber("kD", kD);

        leftPID.setPID(kP, kI, kD);
        rightPID.setPID(kP, kI, kD);
    }

    public static void setODOPose(Pose2d pose) {
        odometry.resetPosition(pose, pose.getRotation());
        field.setRobotPose(odometry.getPoseMeters());
    }

    public static void runTraj(Trajectory trajectory, double timeInSec) {
        Trajectory.State goal = trajectory.sample(timeInSec);
        trajField.setRobotPose(goal.poseMeters);

        var chaspeed = ramseteController.calculate(odometry.getPoseMeters(), goal);

        var wheelSpeeds = kinematic.toWheelSpeeds(chaspeed); // 左右速度
        double left = wheelSpeeds.leftMetersPerSecond; // 從wheelSpeeds抓速度 (ctrl+滑鼠左鍵)
        double right = wheelSpeeds.rightMetersPerSecond;

        leftPID.setSetpoint(left);
        rightPID.setSetpoint(right);

        leftmotor.setVoltage(leftPID.calculate(l_filter.calculate(leftencoder.getRate())));
        rightmotor.setVoltage(rightPID.calculate(r_filter.calculate(rightencoder.getRate())));

        SmartDashboard.putNumber("left", left);
        SmartDashboard.putNumber("right", right);
        // SmartDashboard.putNumber("left_curr", l_enc_rate);
        // SmartDashboard.putNumber("right_curr", r_enc_rate);
        SmartDashboard.putNumber("left_error", leftPID.getPositionError());
        SmartDashboard.putNumber("right_error", rightPID.getPositionError());

        // SmartDashboard.putNumber("goal_x", goal.poseMeters.getX());
        // SmartDashboard.putNumber("goal_y", goal.poseMeters.getY());
        // SmartDashboard.putNumber("left_power", left_power);
        // SmartDashboard.putNumber("right_power", right_power);
        SmartDashboard.putNumber("velocity", goal.velocityMetersPerSecond);
    }
}