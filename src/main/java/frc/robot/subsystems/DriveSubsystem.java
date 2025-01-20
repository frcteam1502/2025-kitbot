// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;

import org.team1502.configuration.factory.RobotConfiguration;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.MecanumDriveOdometry;
import edu.wpi.first.math.kinematics.MecanumDriveWheelPositions;
import edu.wpi.first.math.kinematics.MecanumDriveWheelSpeeds;
import edu.wpi.first.util.sendable.SendableRegistry;
import edu.wpi.first.wpilibj.drive.MecanumDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants.DriveConstants;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class DriveSubsystem extends SubsystemBase {
  private final SparkMax m_frontLeft;
  private final RelativeEncoder m_frontLeftEncoder;
  
  private final SparkMax m_rearLeft;
  private final RelativeEncoder m_rearLeftEncoder;
  
  private final SparkMax m_frontRight;
  private final RelativeEncoder m_frontRightEncoder;
  
  private final SparkMax m_rearRight;
  private final RelativeEncoder m_rearRightEncoder;

  private final MecanumDrive m_drive;

  // The gyro sensor
  private final Pigeon2 m_gyro;

  // Odometry class for tracking robot pose
  MecanumDriveOdometry m_odometry;

  /** Creates a new DriveSubsystem. */
  public DriveSubsystem(RobotConfiguration robotConfiguration) {
    m_gyro = robotConfiguration.Pigeon2().buildPigeon2();
    m_odometry =
      new MecanumDriveOdometry(
          DriveConstants.kDriveKinematics,
          m_gyro.getRotation2d(),
          new MecanumDriveWheelPositions());

    m_frontLeft = robotConfiguration.MotorController("Front Left").buildSparkMax();
    m_frontRight = robotConfiguration.MotorController("Front Right").buildSparkMax();
    m_rearLeft = robotConfiguration.MotorController("Rear Left").buildSparkMax();
    m_rearRight = robotConfiguration.MotorController("Rear Right").buildSparkMax();
    m_frontLeftEncoder = m_frontLeft.getEncoder();
    m_frontRightEncoder = m_frontRight.getEncoder();
    m_rearLeftEncoder = m_rearLeft.getEncoder();
    m_rearRightEncoder = m_rearRight.getEncoder();
    
    m_drive = new MecanumDrive(m_frontLeft::set, m_rearLeft::set, m_frontRight::set, m_rearRight::set);
    SendableRegistry.addChild(m_drive, m_frontLeft);
    SendableRegistry.addChild(m_drive, m_frontLeft);
    SendableRegistry.addChild(m_drive, m_rearLeft);
    SendableRegistry.addChild(m_drive, m_frontRight);
    SendableRegistry.addChild(m_drive, m_rearRight);
    SmartDashboard.putData(m_drive);
  }

  @Override
  public void periodic() {
    // Update the odometry in the periodic block
    m_odometry.update(m_gyro.getRotation2d(), getCurrentWheelDistances());
  }

  /**
   * Returns the currently-estimated pose of the robot.
   *
   * @return The pose.
   */
  public Pose2d getPose() {
    return m_odometry.getPoseMeters();
  }

  /**
   * Resets the odometry to the specified pose.
   *
   * @param pose The pose to which to set the odometry.
   */
  public void resetOdometry(Pose2d pose) {
    m_odometry.resetPosition(m_gyro.getRotation2d(), getCurrentWheelDistances(), pose);
  }

  /**
   * Drives the robot at given x, y and theta speeds. Speeds range from [-1, 1] and the linear
   * speeds have no effect on the angular speed.
   *
   * @param xSpeed Speed of the robot in the x direction (forward/backwards).
   * @param ySpeed Speed of the robot in the y direction (sideways).
   * @param rot Angular rate of the robot.
   * @param fieldRelative Whether the provided x and y speeds are relative to the field.
   */
  public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
    if (fieldRelative) {
      m_drive.driveCartesian(xSpeed, ySpeed, rot, m_gyro.getRotation2d());
    } else {
      m_drive.driveCartesian(xSpeed, ySpeed, rot);
    }
  }

  /** Sets the front left drive MotorController to a voltage. */
  public void setDriveMotorControllersVolts(
      double frontLeftVoltage,
      double frontRightVoltage,
      double rearLeftVoltage,
      double rearRightVoltage) {
    m_frontLeft.setVoltage(frontLeftVoltage);
    m_rearLeft.setVoltage(rearLeftVoltage);
    m_frontRight.setVoltage(frontRightVoltage);
    m_rearRight.setVoltage(rearRightVoltage);
  }

  /** Resets the drive encoders to currently read a position of 0. */
  public void resetEncoders() {
    m_frontLeftEncoder.setPosition(0);
    m_rearLeftEncoder.setPosition(0);
    m_frontRightEncoder.setPosition(0);
    m_rearRightEncoder.setPosition(0);
  }

  /**
   * Gets the front left drive encoder.
   *
   * @return the front left drive encoder
   public Encoder getFrontLeftEncoder() {
    return m_frontLeftEncoder;
  }
  */

  /**
   * Gets the rear left drive encoder.
   *
   * @return the rear left drive encoder
   public Encoder getRearLeftEncoder() {
    return m_rearLeftEncoder;
  }
  */

  /**
   * Gets the front right drive encoder.
   *
   * @return the front right drive encoder
   public Encoder getFrontRightEncoder() {
    return m_frontRightEncoder;
  }
  */

  /**
   * Gets the rear right drive encoder.
   *
   * @return the rear right encoder
   public Encoder getRearRightEncoder() {
    return m_rearRightEncoder;
  }
  */

  /**
   * Gets the current wheel speeds.
   *
   * @return the current wheel speeds in a MecanumDriveWheelSpeeds object.
   */
  public MecanumDriveWheelSpeeds getCurrentWheelSpeeds() {
    return new MecanumDriveWheelSpeeds(
        m_frontLeftEncoder.getVelocity(),
        m_rearLeftEncoder.getVelocity(),
        m_frontRightEncoder.getVelocity(),
        m_rearRightEncoder.getVelocity());
  }

  /**
   * Gets the current wheel distance measurements.
   *
   * @return the current wheel distance measurements in a MecanumDriveWheelPositions object.
   */
  public MecanumDriveWheelPositions getCurrentWheelDistances() {
    return new MecanumDriveWheelPositions(
        m_frontLeftEncoder.getPosition(),
        m_rearLeftEncoder.getPosition(),
        m_frontRightEncoder.getPosition(),
        m_rearRightEncoder.getPosition());
  }

  /**
   * Sets the max output of the drive. Useful for scaling the drive to drive more slowly.
   *
   * @param maxOutput the maximum output to which the drive will be constrained
   */
  public void setMaxOutput(double maxOutput) {
    m_drive.setMaxOutput(maxOutput);
  }

  /** Zeroes the heading of the robot. */
  public void zeroHeading() {
    m_gyro.reset();
  }

  /**
   * Returns the heading of the robot.
   *
   * @return the robot's heading in degrees, from -180 to 180
   */
  public double getHeading() {
    return m_gyro.getRotation2d().getDegrees();
  }

  /**
   * Returns the turn rate of the robot.
   *
   * @return The turn rate of the robot, in degrees per second
   */
  public double getTurnRate() {
    return -m_gyro.getRate();
  }
}
