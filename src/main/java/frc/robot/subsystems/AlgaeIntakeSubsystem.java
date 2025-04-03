package frc.robot.subsystems;

import org.team1502.configuration.annotations.DefaultCommand;
import org.team1502.configuration.annotations.SubsystemInfo;
import org.team1502.configuration.factory.RobotConfiguration;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.AlgaeIntakeCommands;

@SubsystemInfo(disabled = true)
@DefaultCommand(command = AlgaeIntakeCommands.class)
public class AlgaeIntakeSubsystem extends SubsystemBase {
    public static final String Wheels = "Wheels";
    public static final String Rotate = "Rotate";
    final SparkMax m_intakeMotor;
    final SparkMax m_rotateMotor;
    public AlgaeIntakeSubsystem(RobotConfiguration robotConfiguration) {
        m_intakeMotor = robotConfiguration.MotorController(Wheels).buildSparkMax();
        m_rotateMotor = robotConfiguration.MotorController(Rotate).buildSparkMax();
    }

    public void in () {
        m_intakeMotor.set(0.2);
    }

    public void out () {
        m_intakeMotor.set(-0.2);
    }

    public void down () {
        m_rotateMotor.set(0.3);
    }

    public void up () {
        m_rotateMotor.set(-0.3);
    }

    public void stay () {
        m_rotateMotor.set(0);
    }

    public void stop () {
        m_intakeMotor.set(0);
    }
}
