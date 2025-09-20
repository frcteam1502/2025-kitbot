package frc.robot.commands;

import org.team1502.game.GameState;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.HolonomicDriveController;
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
        
        Operator.East.onTrue(new InstantCommand(() -> setpoint=level1Position)); // place
        Operator.West.onTrue(new InstantCommand(() -> setpoint=homePosition)); // supply
        // upper/lower bounds
        // Operator.North.onTrue(new InstantCommand(() -> setpoint=3.84));
        // Operator.South.onTrue(new InstantCommand(() -> setpoint=-0.7));
        double restrot =-7.333+0.5;
        restRad = restrot * 0.105320;// (Math.PI*2)/60;
        m_subsystem.setPosition(restRad);
        setpoint = restRad;
    }
    static double homePosition = 0.05;
    static double level1Position = 3.4;
    double restRad;
    double setpoint;
    double error = 0.0;
    double P = 1.0;
    double D = .8;

    public void moveTo(double destination) {
        setpoint = destination;
    }
    public void moveToRest() {
        setpoint = restRad;
    }
    public void moveToHome() {
        setpoint = homePosition;
    }
    public void moveToLevel1() {
        setpoint = level1Position;
    }
    @Override
    public void execute(){
        if (GameState.isDisabled()){
            //m_subsystem.setPosition(restRad);
        }
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
        SmartDashboard.putNumber(getName()+":err", positionError);
        SmartDashboard.putNumber(getName()+":velocitySetpoint", velocitySetpoint);
        SmartDashboard.putNumber(getName()+":feedforward", feedforward);
        SmartDashboard.putNumber(getName()+":pid", pid);
        SmartDashboard.putNumber(getName()+":Vg", m_feedforward.getKg() * Math.cos(setpoint));
    }

    
}
