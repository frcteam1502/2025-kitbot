package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class ForwardAuto extends Command {
    private final DriveSubsystem m_DriveSubsystem;
    public ForwardAuto(DriveSubsystem driveSubsystem){
        m_DriveSubsystem = driveSubsystem;
        addRequirements(m_DriveSubsystem);
    }

    @Override
    public void initialize(){
        m_DriveSubsystem.resetEncoders();
        m_DriveSubsystem.resetOdometry(new Pose2d());
    }

    @Override
    public void execute(){
        var pose = m_DriveSubsystem.getPose();
        var distance = pose.getX();
        if (distance <= 3){
            m_DriveSubsystem.setDriveMotorControllersVolts(0.2, 0.2, 0.2, 0.2);
        } else{
            m_DriveSubsystem.setDriveMotorControllersVolts(0, 0, 0, 0);

        }
    }

    @Override
    public void end(boolean interuppited){
        m_DriveSubsystem.setDriveMotorControllersVolts(0, 0, 0, 0);
        m_DriveSubsystem.resetEncoders();
        m_DriveSubsystem.resetOdometry(new Pose2d());
    

    }
    

}
