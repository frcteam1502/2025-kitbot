package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.team1502.Driver;

public class DriverCommand extends Command {
    private final DriveSubsystem m_driveSubsystem;
    
    public DriverCommand(DriveSubsystem driveSubsystem) {
        m_driveSubsystem = driveSubsystem;
        addRequirements(driveSubsystem);
    }

    @Override
    public void initialize(){
        m_driveSubsystem.resetEncoders();
        m_driveSubsystem.resetOdometry(new Pose2d());

        Driver.RightBumper
            .onTrue(new InstantCommand(() -> m_driveSubsystem.setMaxOutput(0.5)))
            .onFalse(new InstantCommand(() -> m_driveSubsystem.setMaxOutput(1)));
    }

    @Override
    public void execute(){
        m_driveSubsystem.drive(-Driver.getLeftY(), Driver.getLeftX(), Driver.getRightX(), false);
    }
}
