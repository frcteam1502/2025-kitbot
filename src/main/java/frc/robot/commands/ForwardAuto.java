package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class ForwardAuto extends Command {
    private final DriveSubsystem m_DriveSubsystem;
    public ForwardAuto (DriveSubsystem driveSubsystem){
        m_DriveSubsystem = driveSubsystem;
        addRequirements(m_DriveSubsystem);
    }

    @Override
    public void initialize(){
        m_DriveSubsystem.resetOdometry(new Pose2d());
        m_DriveSubsystem.resetEncoders();
    }

    @Override
    public void execute(){
        var pose = m_DriveSubsystem.getPose();
        var distance = pose.getX();
        if (distance <= 3){
            m_DriveSubsystem.drive(0.3, 0, 0, false);
            
        } else {
            m_DriveSubsystem.drive(0, 0, 0, false);
        }
    }

    @Override 
    public void end(boolean interrupted) {
        m_DriveSubsystem.drive(0, 0, 0, false);
        m_DriveSubsystem.resetOdometry(new Pose2d());
        m_DriveSubsystem.resetEncoders();
    }
}
