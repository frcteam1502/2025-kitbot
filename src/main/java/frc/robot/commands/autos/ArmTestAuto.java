package frc.robot.commands.autos;

import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralIntakeSubsystem;

public class ArmTestAuto extends Command {
    private final CoralIntakeSubsystem m_subsystem;

    public ArmTestAuto(CoralIntakeSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(m_subsystem);
    }

    @Override 
    public void initialize() {
        m_subsystem.setPosition(0.0);
    }

    @Override
    public void execute() {
       m_subsystem.rotate(0.2);
       
    }
}