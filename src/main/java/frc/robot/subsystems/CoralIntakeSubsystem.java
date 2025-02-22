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
    final SparkMax m_motor;
    public CoralIntakeSubsystem(RobotConfiguration robotConfiguration) {
        m_motor = robotConfiguration.MotorController("Motor").buildSparkMax();
    }

    public void in () {
        m_motor.set(0.2);
    }

    public void out () {
        m_motor.set(-0.2);
    }

    
    public void stop () {
        m_motor.set(0);    
    }   
}
