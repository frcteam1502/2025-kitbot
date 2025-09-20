// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.autos.Auto1;
import frc.robot.commands.autos.ArmTestAuto;
import frc.robot.commands.autos.Trajectory1Command;
import frc.robot.hardware.Kitbot;
import frc.robot.subsystems.DriveSubsystem;

import org.team1502.configuration.factory.RobotConfiguration;
import org.team1502.injection.RobotFactory;

/*
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems
  //private final DriveSubsystem m_robotDrive;

    /** The factory for the robot. Contains subsystems, IO devices, and commands. */
    public static RobotFactory robotFactory;
    /** the structure of the robot */
    public static RobotConfiguration robotConfiguration;

    public RobotContainer() {
        robotConfiguration = Kitbot.buildRobot();
        robotFactory = RobotFactory.Create(Robot.class, robotConfiguration);

        //m_robotDrive = robotFactory.getInstance(DriveSubsystem.class);
        //m_robotDrive.setMaxOutput(0.25);
    }


  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {  
    //return new ForwardAuto(m_robotDrive); 
    //return new ArmTestAuto(robotFactory); 
    return new Auto1(robotFactory); 
  }

}
