

import com.revrobotics.spark.*;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.team1502.configuration.factory.RobotConfiguration;
import org.team1502.injection.RobotFactory;

import frc.robot.hardware.Kitbot;
import frc.robot.subsystems.*;
import frc.robot.subsystems.DriveSubsystem;

public class DriveSubsystemTests {

    RobotConfiguration m_robotConfiguration;
    DriveSubsystem m_driveSubsystem;
    SparkMax[] m_modules;

    static Angle gyroAngle = Units.Degrees.of(0);
    public static Angle getAngle() {
        return gyroAngle.times(-1);
    }

    @BeforeEach // this method will run before each test
    void setup() {
        assert HAL.initialize(500, 0); // initialize the HAL, crash if failed
        m_robotConfiguration = Kitbot.buildRobot();
        // TODO: disable-all except e.g., DriveSubsystem function needed
        m_robotConfiguration.DisableSubsystem(ElevatorSubsystem.class);
        m_robotConfiguration.DisableSubsystem(CoralIntakeSubsystem.class);
        m_robotConfiguration.DisableSubsystem(AlgaeIntakeSubsystem.class);

        RobotFactory factory = Kitbot.Create(m_robotConfiguration);
        m_driveSubsystem = factory.getInstance(DriveSubsystem.class);
        m_driveSubsystem.m_gyroYaw = ()->getAngle();
        m_modules = m_robotConfiguration.MecanumDrive().getModules()
            .stream()
            .map(mcb->(SparkMax)mcb.CANSparkMax())
            .toArray(SparkMax[]::new);

    }
    
    @SuppressWarnings("PMD.SignatureDeclareThrowsException")
    @AfterEach // this method will run after each test
    void shutdown() throws Exception {
        m_modules[0].close();
        m_modules[1].close();
        m_modules[2].close();
        m_modules[3].close();
    }

    void dumpModules(String note) {
        System.out.println(note);
        System.out.println(String.format("%-5s %+3.1f %+4.1f","Front", m_modules[0].get(), m_modules[1].get())); 
        System.out.println(String.format("%-5s %+3.1f %+4.1f", "Rear", m_modules[2].get(),m_modules[3].get())); 
        System.out.println();
    }

    @Test //Factory is looking in the wrong frc\robot\subsystems and only finding this class, no subsystems
    public void DriveTest1() {
        m_driveSubsystem.drive(0.75, 0.0, 0.25, false);
        dumpModules("Robot-Rotate Clockwise");
        assertEquals(1.0, m_modules[0].get(), 0.01); // left-side is faster
        assertEquals(0.5, m_modules[1].get(), 0.01); // right-side is slower
        assertEquals(1.0, m_modules[2].get(), 0.01);
        assertEquals(0.5, m_modules[3].get(), 0.01);
        
        m_driveSubsystem.drive(1.0, 0.0, 0, false);
        dumpModules("Robot-Forward");
        
        m_driveSubsystem.drive(0.0, 1.0, 0, false);
        dumpModules("Robot-Left");
        assertEquals(1.0, m_modules[0].get());
        assertEquals(-1.0, m_modules[1].get());
        assertEquals(-1.0, m_modules[2].get());
        assertEquals(1.0, m_modules[3].get());
        
        m_driveSubsystem.drive(1.0, 1.0, 0, false);
        dumpModules("Robot-North West");
        assertEquals(1.0, m_modules[0].get());
        assertEquals(0.0, m_modules[1].get(), 0.01);
        assertEquals(0.0, m_modules[2].get(), 0.01);
        assertEquals(1.0, m_modules[3].get());

        gyroAngle = Units.Degree.of(45);
        m_driveSubsystem.drive(0.0, 1.0, 0.0, true);
        dumpModules("Field-Left with 45 deg gyro == robot-NW");
        assertEquals(1.0, m_modules[0].get());
        assertEquals(0.0, m_modules[1].get(), 0.01);
        assertEquals(0.0, m_modules[2].get(), 0.01);
        assertEquals(1.0, m_modules[3].get());

    }
}
