package frc.robot.commands;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class DriveCommand extends Command {
    private final DriveSubsystem m_DriveSubsystem;
    private final DriveInstruction m_driveInstruction;
    public DriveCommand (DriveSubsystem driveSubsystem, DriveInstruction instruction){
        m_DriveSubsystem = driveSubsystem;
        m_driveInstruction = instruction;
        //addRequirements(m_DriveSubsystem);
    }

    @Override
    public void initialize(){
        SmartDashboard.putString("DriveCommand", "initialized");

    }
    
    @Override
    public void execute(){
        m_DriveSubsystem.drive(m_driveInstruction);
    }

    @Override 
    public void end(boolean interrupted) {
    }
}
