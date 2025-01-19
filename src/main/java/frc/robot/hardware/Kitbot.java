package frc.robot.hardware;

import org.team1502.configuration.factory.RobotConfiguration;

import frc.robot.subsystems.DriveSubsystem;

public class Kitbot {
    public static RobotConfiguration buildRobot() {
        return RobotConfiguration.Create("swerveBot", fn->fn
        // include these parts
        .Parts(inventory -> 
            Inventory.Kitbot(
                Inventory.Motors(inventory)
            )
        )
        // build the robot from parts
        .Build(builder->builder
            // We need to invert one side of the drivetrain so that positive voltages
            // result in both sides moving forward. Depending on how your robot's
            // gearbox is constructed, you might have to invert the left side instead.
            .Subsystem(DriveSubsystem.class, sys->sys
                .MotorController("Front Left", "Mecanum Motor", c->c
                    .PDH(1)
                    .CanNumber(3)
                    .Abbreviation("FL"))
                .MotorController("Front Right", "Mecanum Motor", c->c
                    .Reversed()
                    .PDH(0)
                    .CanNumber(5)
                    .Abbreviation("FR"))
                .MotorController("Rear Left", "Mecanum Motor", c->c
                    .PDH(18)
                    .CanNumber(4)
                    .Abbreviation("RL"))
                .MotorController("Rear Right", "Mecanum Motor", c->c
                    .Reversed()
                    .PDH(19)
                    .CanNumber(14)
                    .Abbreviation("RR"))
            )
            // 14 stage 1
            //  3 intake arm
            // 16 algae intake wheels
            // 15 algae intake lift

        ));
    }

}
