package frc.robot.hardware;

import java.util.Arrays;
import java.util.function.Consumer;

import org.team1502.configuration.CAN.Manufacturer;
import org.team1502.configuration.builders.motors.Motor;
import org.team1502.configuration.factory.PartFactory;
import org.team1502.configuration.factory.RobotConfiguration;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkBaseConfig.IdleMode;

public class Inventory {
    public static class Names {
        public static class Motors {
            public static String Mecanum = "Mecanum Motor";
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

    public static void Kitbot(PartFactory inventory) { inventory
        .MotorController(Names.Motors.Mecanum, Manufacturer.REVRobotics, c->c
            .Motor(Motor.NEO)
            .IdleMode(IdleMode.kBrake)
            .GearBox(g-> g
                .Gear("Stage1", 14, 50)
                //.Gear("Stage2", 18, 46) // 9.13:1
                .Gear("Stage2", 19, 45) // 8.46 -- 8.45:1 ordered?
                .Wheel(8.0)
            )
            .SmartCurrentLimit(40)
        );
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
                    .Wheel(4.0)
                )
                .PID(.0005, 0.0, 0.0, 1.0)
                .ClosedLoopRampRate(.5)
                .SmartCurrentLimit(30)
            )
        );
    }

}
