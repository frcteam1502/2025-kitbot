package frc.robot.subsystems;

import org.team1502.configuration.annotations.DefaultCommand;
import org.team1502.configuration.annotations.SubsystemInfo;
import org.team1502.configuration.factory.RobotConfiguration;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.CoralIntakeCommands;

@SubsystemInfo(disabled = true)
@DefaultCommand(command = CoralIntakeCommands.class)
public class CoralIntakeSubsystem extends SubsystemBase {
    final SparkMax m_intakeMotor;
    final SparkMax m_rotateMotor;
    public CoralIntakeSubsystem(RobotConfiguration robotConfiguration) {
        m_intakeMotor = robotConfiguration.MotorController("Intake").buildSparkMax();
        m_rotateMotor = robotConfiguration.MotorController("Rotate").buildSparkMax();
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

    public void front () {
        m_rotateMotor.set(0.3);    
    }   

    public void back () {
        m_rotateMotor.set(-0.3);    
    }   

    public void stay () {
        m_rotateMotor.set(0);    
    }   
}
