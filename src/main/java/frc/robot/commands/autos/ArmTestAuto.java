package frc.robot.commands.autos;

import java.util.List;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralIntakeSubsystem;

public class ArmTestAuto extends AutoCommand {
    private final CoralIntakeSubsystem m_subsystem;

    public ArmTestAuto(CoralIntakeSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(m_subsystem);
    }

    @Override 
    public void initialize() {
        m_subsystem.setPos(0.5);
    }

    @Override
    public void execute() {
       m_rotateMotor.set(0.2);
       
    }
}