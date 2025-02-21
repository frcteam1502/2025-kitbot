package frc.robot.commands.autos;

import java.util.List;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.team1502.Driver;

public class Trajectory1Command extends Command {
    private final DriveSubsystem m_subsystem;
    
    public Trajectory1Command(DriveSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize(){
        TrajectoryConfig config = m_subsystem.getTrajectoryConfig();

        // An example trajectory to follow. All units in meters.
        Trajectory exampleTrajectory =
            TrajectoryGenerator.generateTrajectory(
                // Start at the origin facing the +X direction
                Pose2d.kZero,
                // Pass through these two interior waypoints, making an 's' curve path
                List.of(new Translation2d(1, 0), new Translation2d(2, 0)),
                // End 3 meters straight ahead of where we started, facing forward
                new Pose2d(3, 0, Rotation2d.kZero),
                config);

        m_subsystem.resetOdometry();

        Command command = m_subsystem.buildMecanumControllerCommand(exampleTrajectory);
    }

    @Override
    public void execute(){
    }
    
    @Override 
    public void end(boolean interrupted) {
        m_subsystem.drive(0,0,0, false);
        m_subsystem.resetOdometry();
    }
}
