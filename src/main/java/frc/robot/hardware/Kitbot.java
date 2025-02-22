package frc.robot.hardware;

import org.team1502.configuration.factory.RobotConfiguration;

import frc.robot.commands.AlgaeIntakeCommands;
import frc.robot.subsystems.AlgaeIntakeSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

public class Kitbot {

    @SuppressWarnings("unchecked")
    public static RobotConfiguration buildRobot() {
        return RobotConfiguration.Create("KitBot-2029", fn->
        // include these parts
        Inventory.Parts(fn,
            Inventory::Motors, 
             Inventory::Sensors,
             Inventory::Kitbot
            )

        // build the robot from parts
        .Build(builder->builder
            // We need to invert one side of the drivetrain so that positive voltages
            // result in both sides moving forward. Depending on how your robot's
            // gearbox is constructed, you might have to invert the left side instead.
            .Subsystem(DriveSubsystem.class, sys->sys
                .Pigeon2(g->g
                    .CanNumber(14))
                .MotorController("Front Left", Inventory.Names.Motors.Mecanum, c->c
                    .PDH(1)
                    .CanNumber(3)
                    .Abbreviation("FL"))
                .MotorController("Front Right", Inventory.Names.Motors.Mecanum, c->c
                    .Reversed()
                    .PDH(0)
                    .CanNumber(5)
                    .Abbreviation("FR"))
                .MotorController("Rear Left", Inventory.Names.Motors.Mecanum, c->c
                    .PDH(18)
                    .CanNumber(4)
                    .Abbreviation("RL"))
                .MotorController("Rear Right", Inventory.Names.Motors.Mecanum, c->c
                    .Reversed()
                    .PDH(19)
                    .CanNumber(14)
                    .Abbreviation("RR"))
            )
            .Subsystem(ElevatorSubsystem.class, sys->sys
                .MotorController("Motor", Inventory.Names.Motors.Elevator, c->c
                    .PDH(17)//Change when connected!!
                    .CanNumber(6)
                    .Abbreviation("Elv")))
            .Subsystem(CoralIntakeSubsystem.class, sys->sys
                .MotorController("Intake", Inventory.Names.Motors.CoralIntake, c->c
                    .PDH(15)
                    .CanNumber(15)
                    .Abbreviation("CI"))
                .MotorController("Rotate", Inventory.Names.Motors.CoralRotate, c->c
                    .PDH(16)
                    .CanNumber(16)
                    .Abbreviation("CR")))
            .Subsystem(AlgaeIntakeSubsystem.class, sys->sys
                .MotorController("Wheels", Inventory.Names.Motors.AlgaeWheels, c->c
                    .PDH(20)
                    .CanNumber(16)
                    .Abbreviation("AW"))
                .MotorController("Rotate", Inventory.Names.Motors.AlgaeRotate, c->c
                    .PDH(20)
                    .CanNumber(16)
                    .Abbreviation("AR")))
            
        ));
            
            
            // 14 stage 1
            //  3 intake arm
            // 16 algae intake wheels
            // 15 algae intake lift

        
    }

}
