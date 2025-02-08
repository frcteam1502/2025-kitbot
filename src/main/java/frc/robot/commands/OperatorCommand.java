package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.team1502.Operator;

public class OperatorCommand extends Command {
        private final ElevatorSubsystem m_elevatorSubsystem;
    
    public OperatorCommand(ElevatorSubsystem elevatorSubsystem) {
        m_elevatorSubsystem = elevatorSubsystem;
        addRequirements(elevatorSubsystem);
    }

    @Override
    public void initialize(){
        //m_elevatorSubsystem.reset();

        Operator.A
            .onTrue(new InstantCommand(() -> m_elevatorSubsystem.lower()))
            .onFalse(new InstantCommand(() -> m_elevatorSubsystem.stop()));
        Operator.Y
            .onTrue(new InstantCommand(() -> m_elevatorSubsystem.raise()))
            .onFalse(new InstantCommand(() -> m_elevatorSubsystem.stop()));

    }

    @Override
    public void execute(){
    }

}
