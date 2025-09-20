package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.team1502.Operator;

public class ElevatorCommands extends Command {
    private final ElevatorSubsystem m_subsystem;
    
    public ElevatorCommands(ElevatorSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(m_subsystem);
    }

    @Override
    public void initialize(){
        //m_subsystem.reset();

        Operator.North
            .onTrue(new InstantCommand(() -> m_subsystem.raise()))
            .onFalse(new InstantCommand(() -> m_subsystem.stop()));
        Operator.South
            .onTrue(new InstantCommand(() -> m_subsystem.lower()))
            .onFalse(new InstantCommand(() -> m_subsystem.stop()));

    }

    @Override
    public void execute(){
    }

}
