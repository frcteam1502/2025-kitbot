package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.AlgaeIntakeSubsystem;
import frc.robot.team1502.Driver;

public class AlgaeIntakeCommands extends Command {
    private final AlgaeIntakeSubsystem m_subsystem;
    
    public AlgaeIntakeCommands(AlgaeIntakeSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(m_subsystem);
    }

    @Override
    public void initialize(){
        //m_subsystem.reset();
        
        Driver.RightBumper
            .onTrue(new InstantCommand(() -> m_subsystem.in()))
            .onFalse(new InstantCommand(() -> m_subsystem.stop()));
        Driver.LeftBumper
            .onTrue(new InstantCommand(() -> m_subsystem.out()))
            .onFalse(new InstantCommand(() -> m_subsystem.stop()));
        Driver.East
            .onTrue(new InstantCommand(() -> m_subsystem.down()))
            .onFalse(new InstantCommand(() -> m_subsystem.stay()));
        Driver.West
            .onTrue(new InstantCommand(() -> m_subsystem.up()))
            .onFalse(new InstantCommand(() -> m_subsystem.stay()));
         

    }

    @Override
    public void execute(){
    }
    
}
