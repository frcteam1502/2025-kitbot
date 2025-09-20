package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class TimedDriveCommand extends Command {
    private final DriveSubsystem m_driveSubsystem;
    private final DriveInstruction m_driveInstruction;
    private final Timer timer = new Timer();
    public TimedDriveCommand (DriveSubsystem driveSubsystem, DriveInstruction instruction){
        m_driveSubsystem = driveSubsystem;
        m_driveInstruction = instruction;
        //addRequirements(m_driveSubsystem);
    }

    private Command m_command;
    @Override
    public void initialize(){
        m_command = new DriveCommand(m_driveSubsystem, m_driveInstruction);
        timer.restart();
    }

    @Override
    public void execute(){
        if (!done && !m_command.isScheduled()) {
            SmartDashboard.putString("TimedCommand", "Scheduled");
            m_command.schedule();
        }
    }
    
    @Override 
    public void end(boolean interrupted) {
        if (m_command.isScheduled()) {m_command.cancel();}
        
        SmartDashboard.putString("TimedCommand", "end");

        timer.stop();
    }
    
    private boolean done;
    @Override
    public boolean isFinished() {
        if (!done) {
            done = timer.hasElapsed(m_driveInstruction.duration());
        }
        return done; // do not restart this command
    }

}
