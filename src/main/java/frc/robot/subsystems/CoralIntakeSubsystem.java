package frc.robot.subsystems;

import org.team1502.configuration.annotations.DefaultCommand;
import org.team1502.configuration.annotations.SubsystemInfo;
import org.team1502.configuration.factory.RobotConfiguration;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.CoralIntakeCommands;

@SubsystemInfo(disabled = false)
@DefaultCommand(command = CoralIntakeCommands.class)
public class CoralIntakeSubsystem extends SubsystemBase {
    final SparkMax m_intakeMotor;
    final SparkMax m_rotateMotor;
    public CoralIntakeSubsystem(RobotConfiguration robotConfiguration) {
        m_intakeMotor = robotConfiguration.MotorController("Intake").buildSparkMax();
        m_rotateMotor = robotConfiguration.MotorController("Rotate").buildSparkMax();
        m_rotateMotor.getEncoder().setPosition(-0.10675);
    }
    
    // ArmFeedforward feedforward = new ArmFeedforward(0, 0.8, 0.5 );
    // double setpoint; // radians
    // double position; // radians

    @Override 
    public void periodic() {
        SmartDashboard.putNumber("ROTATE", m_rotateMotor.getEncoder().getPosition());
        SmartDashboard.putNumber("ROTATEVOLTAGE", m_rotateMotor.getAppliedOutput());


        // position = Units.rotationsToRadians(m_rotateMotor.getEncoder().getPosition());
        // double velocity = setpoint - position;
        // double speed = feedforward.calculate(setpoint, velocity);
        // rotate(speed);
    }
    
    public double getPosition() {
        return Units.rotationsToRadians(m_rotateMotor.getEncoder().getPosition()); 
    }
    
    
    
    public void in () {
        m_intakeMotor.set(0.2);
    }

    public void out () {
        m_intakeMotor.set(-0.2);
    }

    
    public void stop () {
        m_intakeMotor.set(0);    
    }   

    public void front (double speed) {
        m_rotateMotor.set(speed);    
    }   

    public void rotate (double speed) {
        m_rotateMotor.set(speed);    
    }   

    public void back (double speed) {
        m_rotateMotor.set(speed);    
    }   

    public void setPos (double position) {
        m_rotateMotor.getClosedLoopController().setReference(position, ControlType.kPosition);
    }
    // public void stay () {
    //     m_rotateMotor.set(0);    
    // }   
}
