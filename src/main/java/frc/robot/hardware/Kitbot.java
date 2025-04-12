package frc.robot.hardware;

import static edu.wpi.first.units.Units.Inches;
import static edu.wpi.first.units.Units.MetersPerSecond;
import static edu.wpi.first.units.Units.MetersPerSecondPerSecond;

import org.team1502.configuration.factory.RobotConfiguration;
import org.team1502.injection.RobotFactory;
import org.team1502.drivers.MecanumDriver;

import frc.robot.Robot;
import frc.robot.subsystems.AlgaeIntakeSubsystem;
import frc.robot.subsystems.CoralIntakeSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;

public class Kitbot {
    public static RobotFactory Create() {
        return Create(buildRobot());
    }
    public static RobotFactory Create(RobotConfiguration config) {
        return RobotFactory.Create(Robot.class, config);
    }

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

                .MecanumDrive(m->m
                    .Chassis(c->c.Rectangular(
                        // Gap + Frame + Gap + Wheel
                        Inches.of((double)1/8 + 17 + (double)1/8 + 3),
                        Inches.of(20 + (double)1/2))
                    )
                    .MotorController("Front Left", Inventory.Names.Motors.Mecanum, c->c
                        .PDH(19)
                        .CanNumber(3)
                        .Abbreviation("FL"))
                    .MotorController("Front Right", Inventory.Names.Motors.Mecanum, c->c
                        .Reversed()
                        .PDH(5)
                        .CanNumber(5)
                        .Abbreviation("FR"))
                    .MotorController("Rear Left", Inventory.Names.Motors.Mecanum, c->c
                        .PDH(1)
                        .CanNumber(4)
                        .Abbreviation("RL"))
                    .MotorController("Rear Right", Inventory.Names.Motors.Mecanum, c->c
                        .Reversed()
                        .PDH(0)
                        .CanNumber(14)
                        .Abbreviation("RR"))
                    // MecanumComtroller Command Information
                    .TrajectoryConfig(MetersPerSecond.of(1.0), MetersPerSecondPerSecond.of(3.0))
                    .PIDController(MecanumDriver.XController, p->p.Gain(0.5, 0.0, 0.0))
                    .PIDController(MecanumDriver.YController, p->p.Gain(0.5, 0.0, 0.0))
                    .PIDController(MecanumDriver.ThetaController, p->p
                        .Gain(0.5, 0.0, 0.0)
                        .Constraints(Math.PI, Math.PI))
                )
            )
            .Subsystem(ElevatorSubsystem.class, sys->sys
                .MotorController("Motor", Inventory.Names.Motors.Elevator, c->c
                    .PDH(2)//Change when connected!!
                    .CanNumber(6)
                    .Abbreviation("Elv")))
            .Subsystem(CoralIntakeSubsystem.class, sys->sys
                .MotorController("Intake", Inventory.Names.Motors.CoralIntake, c->c
                    .PDH(4)
                    .CanNumber(15)
                    .Abbreviation("CI"))
                .MotorController("Rotate", Inventory.Names.Motors.CoralRotate, c->c
                    .PID(1.2,0.0002,0)
                    .PDH(3)
                    .CanNumber(16)
                    .Abbreviation("CR")))
            .Subsystem(AlgaeIntakeSubsystem.class, sys->sys
                .MotorController(AlgaeIntakeSubsystem.Wheels, Inventory.Names.Motors.AlgaeWheels, c->c
                    .PDH(17)
                    .CanNumber(20)
                    .Abbreviation("AW"))
                .MotorController(AlgaeIntakeSubsystem.Rotate, Inventory.Names.Motors.AlgaeRotate, c->c
                    .PDH(17)
                    .CanNumber(19)
                    .Abbreviation("AR")))
            
        ));
            
            
            // 14 stage 1
            //  3 intake arm
            // 16 algae intake wheels
            // 15 algae intake lift

        
    }

}
