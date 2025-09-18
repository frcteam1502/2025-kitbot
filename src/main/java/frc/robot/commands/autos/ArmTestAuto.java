package frc.robot.commands.autos;

import java.util.List;

import org.team1502.configuration.factory.RobotConfiguration;
import org.team1502.injection.RobotFactory;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.config.SparkMaxConfig;

import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralIntakeSubsystem;

public class ArmTestAuto extends Command {
    private final CoralIntakeSubsystem m_subsystem;
    SparkMax motor;
    public ArmTestAuto(RobotFactory robotFactory) {
        m_subsystem = robotFactory.getInstance(CoralIntakeSubsystem.class);
        addRequirements(m_subsystem);
        
        RobotConfiguration coralConfiguration = robotFactory.getRobotConfiguration().Subsystem(CoralIntakeSubsystem.class);
        
        double armReduction = coralConfiguration.MotorController(CoralIntakeSubsystem.Rotate).GearBox().GearRatio();
        SmartDashboard.putNumber("Gear Ratio", armReduction);
        double postitionConversionFactor = coralConfiguration.MotorController(CoralIntakeSubsystem.Rotate).getPositionConversionFactor();
        SmartDashboard.putNumber("kitbot.getPositionConversionFactor", postitionConversionFactor);
        SmartDashboard.putNumber("Expected Arm Position Conversion Factor", (1.0 / (4 * 5 * 3))*2*Math.PI);
        motor = coralConfiguration.MotorController(CoralIntakeSubsystem.Rotate).CANSparkMax();
        SmartDashboard.putNumber("motor.getPositionConversionFactor", motor.configAccessor.encoder.getPositionConversionFactor());
        SmartDashboard.putNumber("Old Arm Position", m_subsystem.getPosition());
        SparkMaxConfig config = new SparkMaxConfig();
        config.encoder.positionConversionFactor((1.0 / (4 * 5 * 3))*2*Math.PI);
       //motor.configure(config, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        SmartDashboard.putNumber("New Arm Position Conversion Factor", motor.configAccessor.encoder.getPositionConversionFactor());
    }

    double offsetrot =-7.333+0.5;
    double offset = offsetrot * 0.105320;// (Math.PI*2)/60;
    
    @Override 
    public void initialize() {
        motor.getEncoder().setPosition(offset);
        m_subsystem.rotate(0.0);
        SmartDashboard.putNumber("init New Arm Position", m_subsystem.getPosition());
    }

    @Override
    public void execute() {
        double newZero = getZeroPosition();
        if (m_zeroPosition != newZero) {
            m_zeroPosition = newZero;
            motor.getEncoder().setPosition(m_zeroPosition);
        }
        m_position = m_subsystem.getPosition();

        SmartDashboard.putNumber(getName()+":zero-position", m_zeroPosition);
        SmartDashboard.putNumber(getName()+":position", m_position);
        //SmartDashboard.putNumber(getName()+":position (rad)", Units.rotationsToRadians(m_position));
        SmartDashboard.putNumber(getName()+":position (deg)", Units.radiansToDegrees(m_position));
        SmartDashboard.putNumber(getName()+":velocity", m_velocity);
        
       
    }
    double m_velocity = 0.0;
    double m_position = 0.0;
    double m_zeroPosition = 0;
    
    public double getZeroPosition() {
        return SmartDashboard.getNumber(getName()+":zero-position", m_zeroPosition);
    }

    public double getVelocity() {
        return SmartDashboard.getNumber(getName()+":velocity", m_velocity);
    }
}