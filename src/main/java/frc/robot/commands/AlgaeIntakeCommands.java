package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.AlgaeIntakeSubsystem;
import frc.robot.team1502.Operator;

public class AlgaeIntakeCommands extends Command {
    private final AlgaeIntakeSubsystem m_subsystem;
    
    public AlgaeIntakeCommands(AlgaeIntakeSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(m_subsystem);
    }

    @Override
    public void initialize(){
        //m_subsystem.reset();

        // TODO: Algae buttons and commands
        /*
        Operator.X
            .onTrue(new InstantCommand(() -> m_subsystem.in()))
            .onFalse(new InstantCommand(() -> m_subsystem.stop()));
        Operator.Y
            .onTrue(new InstantCommand(() -> m_subsystem.out()))
            .onFalse(new InstantCommand(() -> m_subsystem.stop()));
         */

    }

    @Override
    public void execute(){
    }
    
}
