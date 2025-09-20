package frc.robot.subsystems;

import org.team1502.configuration.annotations.DefaultCommand;
import org.team1502.configuration.annotations.SubsystemInfo;
import org.team1502.configuration.factory.RobotConfiguration;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.ControlType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.ElevatorCommands;

@SubsystemInfo(disabled = true)
@DefaultCommand(command = ElevatorCommands.class)
public class ElevatorSubsystem extends SubsystemBase {
    final SparkMax m_motor;
    public ElevatorSubsystem(RobotConfiguration robotConfiguration) {
        m_motor = robotConfiguration.MotorController("Motor").buildSparkMax();
    }
    @Override 
    public void periodic() {
        SmartDashboard.putNumber("ELEVATOR", m_motor.getEncoder().getPosition());
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
    public void gotoL1() {
        
        var pid = m_motor.getClosedLoopController();
        pid.setReference(levels.level1.value,ControlType.kPosition);
    }
    public void gotol2() {
        var pid = m_motor.getClosedLoopController();
        pid.setReference(levels.level2.value,ControlType.kPosition);
    }
    
    enum levels{
        level1(0.0),
        level2(8.0),
        level3(10.0);

        @SuppressWarnings("MemberName")
        public final double value;

        levels(Double value) {
        this.value = value;
    }
    }
}
