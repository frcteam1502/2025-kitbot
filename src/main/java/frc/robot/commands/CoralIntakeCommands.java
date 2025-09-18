package frc.robot.commands;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.team1502.Operator;

public class CoralIntakeCommands extends Command {
    private final CoralIntakeSubsystem m_subsystem;
        
    ArmFeedforward m_feedforward = new ArmFeedforward(0, 0.055, 0.5 );

    public CoralIntakeCommands(CoralIntakeSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(m_subsystem);
    }

    @Override
    public void initialize() {
        //m_subsystem.reset();

        Operator.X//eat
            .onTrue(new InstantCommand(() -> m_subsystem.in()))
            .onFalse(new InstantCommand(() -> m_subsystem.stop()));
        Operator.B//arf
            .onTrue(new InstantCommand(() -> m_subsystem.out()))
            .onFalse(new InstantCommand(() -> m_subsystem.stop()));
        
        Operator.West.onTrue(new InstantCommand(() -> setpoint=Units.degreesToRadians(-35)));
        Operator.East.onTrue(new InstantCommand(() -> setpoint=Units.degreesToRadians(0)));
        //Operator.North.onTrue(new InstantCommand(() -> setpoint=Units.degreesToRadians(90)));
        //Operator.South.onTrue(new InstantCommand(() -> setpoint=Units.degreesToRadians(45)));

    }
    
    double setpoint = -0.106750;
    double error = 0.0;
    double P = 1.0;
    double D = 1.0;

    @Override
    public void execute(){
        double move = Operator.getRightTrigger() - Operator.getLeftTrigger();
        move /= 10.0;
        setpoint += move; // setpoint = setpoint + move;


        double position = m_subsystem.getPosition();
        double positionError = setpoint - position;
        double delta = error - positionError;
        error = positionError;
        double velocitySetpoint = error;

        double feedforward = m_feedforward.calculate(setpoint, velocitySetpoint);
        double pid = P * error + D * delta;
        m_subsystem.rotate(feedforward + pid);

        SmartDashboard.putNumber(getName()+":setpoint", setpoint);
        SmartDashboard.putNumber(getName()+":position", position);
        SmartDashboard.putNumber(getName()+":err", position - setpoint);
        SmartDashboard.putNumber(getName()+":velocitySetpoint", velocitySetpoint);
        SmartDashboard.putNumber(getName()+":feedforward", feedforward);
        SmartDashboard.putNumber(getName()+":pid", pid);
        SmartDashboard.putNumber(getName()+":Vg", m_feedforward.getKg() * Math.cos(setpoint));
    }

    
}
