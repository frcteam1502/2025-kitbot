package frc.robot.subsystems;

import org.team1502.configuration.factory.RobotConfiguration;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ElevatorSubsystem extends SubsystemBase {
    final SparkMax m_motor;
    public ElevatorSubsystem(RobotConfiguration robotConfiguration) {
        m_motor = robotConfiguration.MotorController("Motor").buildSparkMax();
    }

    public void raise() {
        m_motor.set(0.3);
    }

    public void lower() {
        m_motor.set(-0.3);
    }

    public void stop() {
        m_motor.set(0);
    }
}
