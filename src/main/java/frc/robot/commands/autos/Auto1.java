package frc.robot.commands.autos;

import org.team1502.injection.RobotFactory;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.CoralIntakeCommands;
import frc.robot.commands.DriveInstruction;
import frc.robot.commands.TimedDriveCommand;
import frc.robot.subsystems.DriveSubsystem;

public class Auto1 extends Command {
    private final DriveSubsystem m_driveSubsystem;
    private final CoralIntakeCommands m_coralIntakeCommands;
    private final double m_sign;

    Command m_command;
    public Auto1(RobotFactory robotFactory, double sign) {
        m_driveSubsystem = robotFactory.getInstance(DriveSubsystem.class);
        m_sign = sign;
        m_coralIntakeCommands = robotFactory.getInstance(CoralIntakeCommands.class);
        SmartDashboard.putString("Auto1", "ctor");
        addRequirements(m_driveSubsystem);
        //addRequirements(m_driveSubsystem, m_coralIntakeSubsystem);

    }

    @Override
    public void initialize(){
        SmartDashboard.putString("Auto1", "init");
        /* right side */
        m_command = new SequentialCommandGroup(
            new TimedDriveCommand(m_driveSubsystem, new DriveInstruction(-0.25, 0, 0, false, 4))                
            ,new TimedDriveCommand(m_driveSubsystem, new DriveInstruction(0, 0, m_sign * 0.1, false, 3))
            ,new TimedDriveCommand(m_driveSubsystem, new DriveInstruction(-0.1, 0, 0, false, 4))

            // ,new TimedDriveCommand(m_driveSubsystem, new DriveInstruction(0.1, 0, 0, false, 4))
            // ,new TimedDriveCommand(m_driveSubsystem, new DriveInstruction(0, 0, -0.1, false, 4))
            // ,new TimedDriveCommand(m_driveSubsystem, new DriveInstruction(0.25, 0, 0, false, 4))
        );

        /*
         * 
            new ParallelCommandGroup(
                new InstantCommand(()->m_coralIntakeCommands.moveToLevel1())
            )
            ,new ParallelCommandGroup(
                new InstantCommand(()->m_coralIntakeCommands.moveToHome())

            )
        
         * */
        
        /*
         * left side
         
        m_command = new SequentialCommandGroup(
            new TimedDriveCommand(m_driveSubsystem, new DriveInstruction(-0.25, 0, 0, false, 4))
            ,new TimedDriveCommand(m_driveSubsystem, new DriveInstruction(0, 0, -0.1, false, 4))
            ,new TimedDriveCommand(m_driveSubsystem, new DriveInstruction(0, 0, -0.1, false, 4))
            ,new TimedDriveCommand(m_driveSubsystem, new DriveInstruction(0, 0, 0.1, false, 2))
            //, new InstantCommand(()->m_coralIntakeCommands.moveTo(-0.7))
        );
        */

    }

    @Override
    public void execute(){
        if (!done && !m_command.isScheduled()) { 
            SmartDashboard.putString("Auto1", "Scheduled");
            m_command.schedule();
        }
    }
    
    @Override 
    public void end(boolean interrupted) {
        if ((interrupted || done) && m_command.isScheduled()) {m_command.cancel();}
        m_driveSubsystem.stop();
        SmartDashboard.putString("Auto1", "end");

    }

    private boolean done;
    @Override
    public boolean isFinished() {
        done = done ? true : m_command.isFinished();
        return done;
    }

}
