package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.team1502.Operator;

public class CoralIntakeCommands extends Command {
    private final CoralIntakeSubsystem m_subsystem;
    
    public CoralIntakeCommands(CoralIntakeSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(m_subsystem);
    }

    @Override
    public void initialize(){
        //m_subsystem.reset();

        Operator.X//eat
            .onTrue(new InstantCommand(() -> m_subsystem.in()))
            .onFalse(new InstantCommand(() -> m_subsystem.stop()));
        Operator.B//arf
            .onTrue(new InstantCommand(() -> m_subsystem.out()))
            .onFalse(new InstantCommand(() -> m_subsystem.stop()));
        // Operator.rightTrigger(0)
        //     .onTrue(new InstantCommand(() -> m_subsystem.front()))
        //     .onFalse(new InstantCommand(() -> m_subsystem.stay()));
        // Operator.leftTrigger(0)
        //     .onTrue(new InstantCommand(() -> m_subsystem.back()))
        //     .onFalse(new InstantCommand(() -> m_subsystem.stay()));

    }

    @Override
    public void execute(){
        m_subsystem.rotate(Operator.getRightTrigger()-Operator.getLeftTrigger());
    }

    
}
