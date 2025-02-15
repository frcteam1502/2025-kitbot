package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.team1502.Driver;

public class DriverCommands extends Command {
    private final DriveSubsystem m_subsystem;
    
    public DriverCommands(DriveSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize(){
        m_subsystem.resetEncoders();
        m_subsystem.resetOdometry(new Pose2d());

        Driver.RightBumper
            .onTrue(new InstantCommand(() -> m_subsystem.setMaxOutput(0.5)))
            .onFalse(new InstantCommand(() -> m_subsystem.setMaxOutput(1)));
    }

    @Override
    public void execute(){
        // TODO: dead-band
        // TODO: slew-rate
        m_subsystem.drive(-Driver.getLeftY(), Driver.getLeftX(), Driver.getRightX(), false);
    }
}
