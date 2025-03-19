// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix6.hardware.Pigeon2;

import java.util.function.Supplier;

import org.team1502.configuration.annotations.DefaultCommand;
import org.team1502.configuration.annotations.SubsystemInfo;
import org.team1502.configuration.factory.RobotConfiguration;
import org.team1502.drivers.MecanumDriver;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj2.command.MecanumControllerCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.commands.DriverCommands;

@SubsystemInfo(disabled = true)
@DefaultCommand(command = DriverCommands.class)
public class DriveSubsystem extends SubsystemBase {
    final Pigeon2 m_gyro;
    /** generic Angle in degrees CW */
    public /* final */ Supplier<Angle> m_gyroYaw;
    /** generic Rotation2d in radians */
    public /* final */ Supplier<Rotation2d> m_gyroRotation2d;

    final MecanumDriver m_drive;
    final RobotConfiguration m_robotConfiguration;

    /** Creates a new DriveSubsystem. */
    public DriveSubsystem(RobotConfiguration robotConfiguration) {
        m_robotConfiguration = robotConfiguration;
        
        // set up gyro and angle suppliers
        m_gyro = robotConfiguration.Pigeon2().buildPigeon2();
        m_gyroYaw = m_gyro.getYaw().asSupplier();
        m_gyroRotation2d = ()->new Rotation2d(m_gyroYaw.get().times(-1.0));
        
        zeroHeading(); // whichever way we are pointing is 0 (+X direction)

        m_drive = robotConfiguration.MecanumDrive().buildDriver(m_gyroRotation2d);
    }

    @Override
    public void periodic() {
        m_drive.update(); // Update the mecanum driver in the periodic block
    }

    /**
     * Returns the currently-estimated pose of the robot.
     *
     * @return The pose.
     */
    public Pose2d getPose() {
        return m_drive.getPose();
    }
    /**
     * Resets the odometry to the specified pose.
     *
     * @param pose The pose to which to set the odometry.
     */
    public void resetOdometry(Pose2d pose) {
        m_drive.resetOdometry(pose);
    }

    public void resetOdometry() {
        resetOdometry(new Pose2d());
        resetEncoders();
    }

    /**
     * Drives the robot at given x, y and theta speeds. Speeds range from [-1, 1]
     * and the linear speeds have no effect on the angular speed.
     *
     * @param xSpeed        Speed of the robot in the x direction (forward/backwards).
     * @param ySpeed        Speed of the robot in the y direction (sideways).
     * @param rot           Angular rate of the robot (theta). Clockwise!
     * @param fieldRelative Whether the provided x and y speeds are relative to the field.
     */
    public void drive(double xSpeed, double ySpeed, double rot, boolean fieldRelative) {
        m_drive.drive(xSpeed, ySpeed, rot, fieldRelative);
    }

    public TrajectoryConfig getTrajectoryConfig() {
        return m_drive.getTrajectoryConfig();  
    }

    public MecanumControllerCommand buildMecanumControllerCommand(Trajectory trajectory) {
        return m_drive.buildMecanumControllerCommand(trajectory, this);        
    }

    /**
     * Sets the max output of the drive. Useful for scaling the drive to drive more
     * slowly.
     *
     * @param maxOutput the maximum output to which the drive will be constrained
     */
    public void setMaxOutput(double maxOutput) {
        m_drive.setMaxOutput(maxOutput);
    }

    public void initialize(){
        resetOdometry();
    }

    /** Resets the drive encoders to currently read a position of 0. */
    public void resetEncoders() {
        m_drive.resetEncoders();
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
