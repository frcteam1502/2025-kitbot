package frc.robot.hardware;

import static edu.wpi.first.units.Units.Inches;

import java.util.Arrays;
import java.util.function.Consumer;

import org.team1502.configuration.CAN.Manufacturer;
import org.team1502.configuration.builders.motors.GearBox;
import org.team1502.configuration.builders.motors.Motor;
import org.team1502.configuration.builders.motors.MotorControllerBuilder;
import org.team1502.configuration.factory.PartFactory;
import org.team1502.configuration.factory.RobotConfiguration;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class Inventory {
    public static class Names {
        public static class Motors {
            public static String Mecanum = "Mecanum Motor";
            public static String Elevator = "Elevator Motor";
            public static String CoralIntake = "Coral Intake Motor";
            public static String CoralRotate = "Coral Rotate Motor";
            public static String AlgaeWheels = "Algae Wheels Motor";
            public static String AlgaeRotate = "Algae Rotate Motor";
        }
    }
    public static RobotConfiguration Parts(RobotConfiguration config, Consumer<PartFactory>... factories) {
        return config.Parts(parts->{Arrays.asList(factories).forEach(factory->factory.accept(parts)); return parts;});
    }

    public static void Sensors(PartFactory inventory) {inventory
        .Pigeon2(p->p);
    }

    public static void Motors(PartFactory inventory) {inventory
        .Motor(Motor.NEO, m -> m
            .MotorType(MotorType.kBrushless)
            .FreeSpeedRPM(5_820.0) // from MK4i docs, see data sheet for empirical values
        )
        .Motor(Motor.VORTEX, m -> m
            .MotorType(MotorType.kBrushless)
            .FreeSpeedRPM(6_784.0) // from REV
        )
        .Motor(Motor.NEO550, m -> m
            .MotorType(MotorType.kBrushless)
            .FreeSpeedRPM(11_000.0) // from REV
        );
    }

    /** Will set position conversion factor to radians */
    public static GearBox Chain(GearBox gearbox, String chainNumber, int drivingTeeth, int drivenTeeth) {
        double reduction = getChainReduction(chainNumber, drivingTeeth, drivenTeeth);
        double actuatorHack = 2 / reduction; // will end up in Radians when PI is factored in
        gearbox.Value(MotorControllerBuilder.wheelDiameter, actuatorHack);

        return gearbox;
    }
    public static double getChainReduction(String chainNumber, int drivingTeeth, int drivenTeeth) {
        
        return getPitchDiameter(chainNumber, drivenTeeth) / getPitchDiameter(chainNumber, drivingTeeth);
    }
    public static double getPitchDiameter(String chainNumber, int teeth) {
        double pitch = 2.0;
        if (chainNumber == "25") {
            pitch = 2.0;
        }
        double chainPitch = pitch/8.0;
        return chainPitch/Math.sin(Math.PI / teeth);

    }
    public static void Kitbot(PartFactory inventory) { inventory
        .MotorController(Names.Motors.Mecanum, Manufacturer.REVRobotics, c->c
            .Motor(Motor.NEO)
            .IdleMode(IdleMode.kBrake)
            // TODO: velocity PID controllers for autonomous
            .GearBox(g-> g
                 .Gear("Stage1", 14, 50)
                 .Gear("Stage2", 14, 50) // 12.75:1, 44.11 ft-lbs, 445.18 rpm, 15.54 ft/s
                 .Wheel(Inches.of(8.0))
            )
            .SmartCurrentLimit(40)
        )
        .MotorController(Names.Motors.Elevator, Manufacturer.REVRobotics, c->c
            .Motor(Motor.VORTEX)
            .IdleMode(IdleMode.kBrake)
            .GearBox(g-> g
                 .Gear("Stage1", 1, 5) 
                 .Gear("Stage2", 1, 5) 
                 .Wheel(Inches.of(1.28)) // 16 tooth gear pitch diameter
            )
            .SmartCurrentLimit(40)
        )
        .MotorController(Names.Motors.CoralIntake, Manufacturer.REVRobotics, c->c
            .Motor(Motor.NEO)
            .IdleMode(IdleMode.kBrake)
            .GearBox(g-> g
                 .Gear("Stage1", 1, 4) 
                 .Gear("Stage2", 1, 5) 
                 .Wheel(Inches.of(1.28)) // 16 tooth gear pitch diameter
            )
            .SmartCurrentLimit(40)
        )
        .MotorController(Names.Motors.CoralRotate, Manufacturer.REVRobotics, c->c
            .Motor(Motor.NEO)
            .IdleMode(IdleMode.kCoast)
            .GearBox(g-> Chain(g, "25", 16, 48)
                 .Gear("Stage1", 1, 4) 
                 .Gear("Stage2", 1, 5) 
                 //.Gear("#25 Chain", 16, 48) 
                 //TODO: Chain(#25, 16 tooth, 48 tooth)
            )
            .SmartCurrentLimit(40)
        )
        .MotorController(Names.Motors.AlgaeWheels, Manufacturer.REVRobotics, c->c
            .Motor(Motor.NEO)
            .IdleMode(IdleMode.kBrake)
            .GearBox(g-> g
                 .Gear("Stage1", 1, 3)
                 .Wheel(Inches.of(1.22))
            )
            .SmartCurrentLimit(40)
        )
        .MotorController(Names.Motors.AlgaeRotate, Manufacturer.REVRobotics, c->c
            .Motor(Motor.NEO)
            .IdleMode(IdleMode.kBrake)
            .GearBox(g-> g
                 .Gear("Stage1", 1, 5) 
                 .Gear("Stage2", 1, 3) 
                 .Gear("Stage3", 1, 3) 
                 .Wheel(Inches.of(3.08))
            )
            .SmartCurrentLimit(40)
        )
        ;
    }


    
    public static PartFactory Mk4iL3(PartFactory inventory) { return inventory
        .SwerveModule(sm -> sm
            .CANCoder(cc -> cc)
            .TurningMotor(Manufacturer.REVRobotics, mc -> mc
                .Motor(Motor.NEO550)
                .IdleMode(IdleMode.kCoast)
                .Reversed() // swerve rotation is CCW
                .GearBox(g-> g
                    .Gear("Stage1", 14, 50)
                    .Gear("Stage2", 10, 60)
                )
                .AngleController(-180, 180)
                .PIDController(p->p
                    .Gain(3.4, 0.0, 0.0)
                    .EnableContinuousInput(-Math.PI, Math.PI)
                )
            )
            .DrivingMotor(Manufacturer.REVRobotics, mc -> mc
                .Motor(Motor.VORTEX)
                .IdleMode(IdleMode.kBrake)
                .GearBox(g-> g
                    .Gear("Stage1", 14, 50)
                    .Gear("Stage2", 28, 16)
                    .Gear("Stage3", 15, 45)
                    .Wheel(Inches.of(4.0))
                )
                .PID(.0005, 0.0, 0.0, 1.0)
                .ClosedLoopRampRate(.5)
                .SmartCurrentLimit(30)
            )
        );
    }

}
