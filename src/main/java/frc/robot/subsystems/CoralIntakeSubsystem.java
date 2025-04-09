package frc.robot.subsystems;

import org.team1502.configuration.annotations.DefaultCommand;
import org.team1502.configuration.annotations.SubsystemInfo;
import org.team1502.configuration.factory.RobotConfiguration;

import com.revrobotics.spark.SparkMax;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.CoralIntakeCommands;

@SubsystemInfo(disabled = false)
@DefaultCommand(command = CoralIntakeCommands.class)
public class CoralIntakeSubsystem extends SubsystemBase {
    public static final String Intake = "Intake";
    public static final String Rotate = "Rotate";

    final SparkMax m_intakeMotor;
    final SparkMax m_rotateMotor;
    public CoralIntakeSubsystem(RobotConfiguration robotConfiguration) {
        m_intakeMotor = robotConfiguration.MotorController(Intake).buildSparkMax();
        m_rotateMotor = robotConfiguration.MotorController(Rotate).buildSparkMax();
        m_rotateMotor.getEncoder().setPosition(-0.10675);
    }
    
    @Override 
    public void periodic() {
        SmartDashboard.putNumber("ROTATE", m_rotateMotor.getEncoder().getPosition());
        SmartDashboard.putNumber("ROTATEVOLTAGE", m_rotateMotor.getAppliedOutput());
    }
    
    /** angle of the arm in radians  */
    public double getPosition() {
        return m_rotateMotor.getEncoder().getPosition(); 
    }
    public void setPosition(double position) {
        m_rotateMotor.getEncoder().setPosition(position);
      }
  
    public void in() {
        m_intakeMotor.set(0.2);
    }

    public void out() {
        m_intakeMotor.set(-0.2);
    }

    public void stop() {
        m_intakeMotor.set(0);    
    }   


    public void rotate(double speed) {
        double direction = Math.signum(speed);
        speed = Math.min(Math.abs(speed), 0.3);
        m_rotateMotor.set(direction * speed);    
    }   
}
