package frc.robot.commands;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.team1502.Driver;

public class DriverCommands extends Command {
    private final DriveSubsystem m_subsystem;
    final SlewRateLimiter xjerkLimiter = new SlewRateLimiter(3);
    final SlewRateLimiter yjerkLimiter = new SlewRateLimiter(3);
    final SlewRateLimiter turnLimiter = new SlewRateLimiter(8);

    public DriverCommands(DriveSubsystem subsystem) {
        m_subsystem = subsystem;
        addRequirements(subsystem);
    }

    @Override
    public void initialize(){
        //m_subsystem.zeroHeading(); NO!
        m_subsystem.resetEncoders();
        m_subsystem.resetOdometry(new Pose2d());

        Driver.RightBumper
            .onTrue(new InstantCommand(() -> m_subsystem.setMaxOutput(0.5)))
            .onFalse(new InstantCommand(() -> m_subsystem.setMaxOutput(1)));

        // TODO: finesse speed
    }

    @Override
    public void execute(){
        double forwardSpeed = MathUtil.applyDeadband(-Driver.getLeftY(), 0.05);
        double leftSpeed = MathUtil.applyDeadband(Driver.getLeftX(), 0.05);
        double cwSpeed = MathUtil.applyDeadband(Driver.getRightX(), 0.2);
       
        forwardSpeed = forwardSpeed * forwardSpeed * forwardSpeed;
        leftSpeed = leftSpeed * leftSpeed * leftSpeed;
        cwSpeed = cwSpeed * cwSpeed * cwSpeed * cwSpeed * cwSpeed; // must be odd
        
        forwardSpeed = xjerkLimiter.calculate(forwardSpeed);
        leftSpeed = yjerkLimiter.calculate(leftSpeed);
        cwSpeed = turnLimiter.calculate(cwSpeed);

        m_subsystem.drive(forwardSpeed, leftSpeed, cwSpeed, true);
    }
}
