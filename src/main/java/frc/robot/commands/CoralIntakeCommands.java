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
        
        Operator.East.onTrue(new InstantCommand(() -> setpoint=3.4)); // place
        Operator.West.onTrue(new InstantCommand(() -> setpoint=0.05)); // supply
        // upper/lower bounds
        // Operator.North.onTrue(new InstantCommand(() -> setpoint=3.84));
        // Operator.South.onTrue(new InstantCommand(() -> setpoint=-0.7));

    }
    
    double setpoint = 0.035;
    double error = 0.0;
    double P = 1.0;
    double D = .8;

    @Override
    public void execute(){

        double move = Operator.getRightTrigger() - Operator.getLeftTrigger();
        SmartDashboard.putNumber(getName()+":move", move);
        move /= 20;
        setpoint += move; // setpoint = setpoint + move;
        if (setpoint > 3.7) {setpoint = 3.7;}
        if (setpoint < -0.6) {setpoint = -0.6;}

        double position = m_subsystem.getPosition();
        double positionError = setpoint - position;
        double delta = error - positionError;
        error = positionError;
        double velocitySetpoint = error/10;

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
