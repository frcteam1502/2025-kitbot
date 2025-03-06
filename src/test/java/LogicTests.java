import static edu.wpi.first.units.Units.Degrees;
import static edu.wpi.first.units.Units.Radians;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.units.Units;
import edu.wpi.first.units.measure.Angle;
import edu.wpi.first.wpilibj.drive.MecanumDrive;

public class LogicTests {

    class Gyro {
        public Angle gyroAngle = Units.Degrees.of(0);
        public Angle getAngle() {
            return gyroAngle;
        }
        public Rotation2d getRotation() {
            return new Rotation2d(getAngle());
        }
        public void setHeading(double angle) {
            gyroAngle = Units.Degrees.of(-angle);
        }
        public Angle getHeading() {
            return gyroAngle.times(-1.0);
        }
        public String getHeadingDirection() {
            var a = getHeading().in(Degrees);
            a= normalizeDegrees(a);
            if ((0 <= a && a < 45) || (315 <= a && a <= 360) ) return String.format("(HEADING: %-8s %+4.0f)", "/\\ NORTH", a);
            if (45 <= a && a < 135 ) return String.format("(HEADING: %-8s %+4.0f)", ">> EAST", a);
            if (135 <= a && a < 225 ) return String.format("(HEADING: %-8s %+4.0f)", "\\/ SOUTH", a);
            //if (225 <= a && a  < 315)
             return String.format("(HEADING: %-8s %+4.0f)", "<< WEST", a);
        }
    }

    class MotorController {
        double m_speed;
        public void set(double speed) {
            m_speed = speed;
        }
        public double get() { return m_speed; }
    }

    //** command the driver in human space */
    class ControllerCommand {
        public double leftStickUp;
        public double leftStickRight;
        public double rightStickRotateCW;
        public ControllerCommand(double leftStickUp, double leftStickRight, double rightStickRotateCW) {
            this.leftStickUp = leftStickUp;
            this.leftStickRight = leftStickRight;
            this.rightStickRotateCW = rightStickRotateCW;
        }
        public double getControllerDegrees() {
            Angle angle = Radians.of(Math.atan2(leftStickRight, leftStickUp));
            return normalizeDegrees(angle.in(Degrees));
        }
        public double getRobotDegrees() {
            return normalizeDegrees(-getControllerDegrees());
        }
    }

    static double normalizeDegrees(double degrees) {
        while (degrees < 0) {degrees += 360;}
        while (degrees >= 360) {degrees -= 360;}
        if (-1 < degrees && degrees <=0 ) degrees = 0;
        return degrees;
    }

    //** command to the drive after all controller conversions */
    class DriverCommand {
        public double x;
        public double y;
        public double rot;
        public ControllerCommand controllerCommand;
        public DriverCommand(double x, double y, double rot) {
            this.x =x; this.y = y; this.rot = rot;
        }
        public DriverCommand(double x, double y, double rot, ControllerCommand controllerCommand) {
            this(x,y,rot);
            this.controllerCommand = controllerCommand;
        }
    }

    class DriverControl {
        public DriverCommand getCommand(ControllerCommand controllerCommand) {
            double leftStickUp = -controllerCommand.leftStickUp; // value from controller
            // do assigned controller inversions
            double upCmd =    (invertx ? -1.0 : 1.0) * leftStickUp;
            double rightCmd = (inverty ? -1.0 : 1.0) * controllerCommand.leftStickRight;
            double rotCmd = (invertrot ? -1.0 : 1.0) * controllerCommand.rightStickRotateCW;
            System.out.println(String.format("(%+1.1f,%+1.1f,%+1.1f) %s %s%s %+1.1f, %s%s %+1.1f, %s%s %+1.1f",
                upCmd, rightCmd, rotCmd,
                "XBOX",
                (invertx ? "-" : " "), "UP", leftStickUp,
                (inverty ? "-" : " "), "STRAFE", controllerCommand.leftStickRight,
                (invertrot ? "-" : " "), "ROTATE", controllerCommand.rightStickRotateCW
             )); 

            return new DriverCommand(upCmd, rightCmd, rotCmd, controllerCommand);
        }
        public DriverCommand getCommand(double leftStickUp, double leftStickRight, double rightStickRotateCW) {
            return getCommand(new ControllerCommand(leftStickUp, leftStickRight, rightStickRotateCW));
        }
        public DriverCommand getCommand(Angle leftStickAngle) {
            return getCommand(+1.0, leftStickAngle);
        }
        public DriverCommand getCommand(double leftStickVelocity, Angle leftStickAngle) {
            return getCommand(leftStickVelocity,leftStickAngle, +0.0);
        }
        public DriverCommand getCommand(double leftStickVelocity, Angle leftStickAngle, double rightStickRotateCW) {
            var left = new Rotation2d(leftStickAngle);
            double leftStickUp = leftStickVelocity * left.getCos();
            double leftStickRight = leftStickVelocity * left.getSin();
            return getCommand(leftStickUp, leftStickRight, rightStickRotateCW);
        }
        // public DriverCommand getCommand(double leftStickVelocity, Angle leftStickAngle, double rightStickVelocity, Angle rightStickAngle) {
        //     var left = new Rotation2d(leftStickAngle);
        //     double leftStickUp = leftStickVelocity * left.getCos();
        //     double leftStickRight = leftStickVelocity * left.getSin();
        //     var right = new Rotation2d(rightStickAngle);
        //     //double rightStickUp = rightStickVelocity * right.getCos();
        //     double rightStickRight = rightStickVelocity * right.getSin();
        //     return new DriverCommand((invertx ? -1.0 : 1.0) * leftStickUp, leftStickRight, rightStickRight);
        // }
        
        public boolean invertx;
        public boolean inverty;
        public boolean invertrot;
        public boolean invertGyro;
        public MotorController[] modules;
        public MecanumDrive drive;
        public Gyro gyro;
        
        public DriverControl(boolean invertleftStickUp, boolean invertleftStickRight, boolean invertrightStickRotate, boolean invertGyro) {
            this(invertleftStickUp, invertleftStickRight, invertrightStickRotate);
            gyro = new Gyro();
            this.invertGyro = invertGyro; 
        }

        public DriverControl(boolean invertleftStickUp, boolean invertleftStickRight, boolean invertrightStickRotate) {
            invertx   = invertleftStickUp;
            inverty   = invertleftStickRight;
            invertrot = invertrightStickRotate;

            modules = new MotorController[4];
            modules[0] = new MotorController(); // front left
            modules[1] = new MotorController(); // front right
            modules[2] = new MotorController(); // rear left
            modules[3] = new MotorController(); // rear right
    
            drive = new MecanumDrive(
                (speed)->modules[0].set(speed),
                (speed)->modules[2].set(speed),
                (speed)->modules[1].set(speed),
                (speed)->modules[3].set(speed)
            );
    
        }

        public void setHeading(double humanDegrees) { gyro.setHeading(humanDegrees); }

        public Rotation2d getRotation() {
            return new Rotation2d(gyro.getAngle().times(invertGyro ? -1.0 : +1.0));
        }
        public ModuleInfo test(double leftStickVelocity, Angle leftStickAngle, double rightStickRotateCW, String note) {
            return xbox(leftStickVelocity, leftStickAngle, rightStickRotateCW).show(note).test();
        }
        public ModuleInfo test(double leftStickVelocity, Angle leftStickAngle, String note) {
            return xbox(leftStickVelocity, leftStickAngle).show(note).test();
        }
        public ModuleInfo show(double leftStickVelocity, Angle leftStickAngle, String note) {
            return xbox(leftStickVelocity, leftStickAngle).show(note);
        }
        public ModuleInfo xbox(double leftStickVelocity, Angle leftStickAngle) {
            var driveCommand = getCommand(leftStickVelocity, leftStickAngle);
            drive.driveCartesian(driveCommand.x, driveCommand.y, driveCommand.rot, getRotation());
            return new ModuleInfo(modules, driveCommand, gyro);
        }
        public ModuleInfo xbox(double leftStickVelocity, Angle leftStickAngle, double rightStickRotateCW) {
            var driveCommand = getCommand(leftStickVelocity, leftStickAngle, rightStickRotateCW);
            drive.driveCartesian(driveCommand.x, driveCommand.y, driveCommand.rot, getRotation());
            return new ModuleInfo(modules, driveCommand, gyro);
        }
        public void close() {
            drive.close();
        }
        public String getHeadingDirection() {
            var a = gyro.getHeading().in(Degrees);
            a= normalizeDegrees(a);
            if ((0 <= a && a < 45) || (315 <= a && a <= 360) ) return String.format("(HEADING: %-8s %+4.0f)", "/\\ NORTH", a);
            if (45 <= a && a < 135 ) return String.format("(HEADING: %-8s %+4.0f)", ">> EAST", a);
            if (135 <= a && a < 225 ) return String.format("(HEADING: %-8s %+4.0f)", "\\/ SOUTH", a);
            //if (225 <= a && a  < 315)
             return String.format("(HEADING: %-8s %+4.0f)", "<< WEST", a);
        }    
    }

    @Test
    public void controllerTest() {
        DriverControl driverControl = new DriverControl(true, true, true);
        var driveCommand = driverControl.getCommand(1, Degrees.of(00)); // go forwrd
        driveCommand = driverControl.getCommand(1, Degrees.of(90)); // go right
        assertEquals(-1, driveCommand.y); // in NED, left is positive
        driveCommand = driverControl.getCommand(1, Degrees.of(270)); // go left
        assertEquals(+1, driveCommand.y); // in NED, left is positive
        driveCommand = driverControl.getCommand(1, Degrees.of(-45), 0.5); // go up-left
        assertEquals(Math.sqrt(2)/2, driveCommand.x, 0.02); // in NED, left and up are positive
        assertEquals(Math.sqrt(2)/2, driveCommand.y, 0.02); // in NED, left and up are positive
        assertEquals(-0.5, driveCommand.rot, 0.02); // in NED, ccc rot are positive
    }

    class ModuleInfo {
        public MotorController[] modules;
        public DriverCommand driverCommand;
        public Gyro gyro;
        public Translation2d direction;
        public double robotAngle;
        public double robotMagnitude;

        public ModuleInfo(MotorController[] modules) {
            this.modules = modules;
            direction = getDirection();
        }
        public ModuleInfo(MotorController[] modules, DriverCommand driverCommand, Gyro gyro) {
            this.modules = modules;
            this.driverCommand = driverCommand;
            this.gyro = gyro;
            direction = getDirection();
        }
        public Translation2d getDirection() {
            direction = LogicTests.getDirection(modules);
            robotAngle = direction.getAngle().getDegrees();
            robotMagnitude = direction.getNorm();
            return direction;
        }
        public double getRobotDegrees() {
            getDirection();
            return robotAngle;
        }
        public ModuleInfo show(String note) {
            System.out.println(String.format("%s - %s", gyro.getHeadingDirection(), note));
            System.out.println(String.format("%-5s %+3.1f %+4.1f","Front", modules[0].get(), modules[1].get())); 
            System.out.println(String.format("%-5s %+3.1f %+4.1f", "Rear", modules[2].get(), modules[3].get()));
            var angle = getRobotDegrees();
            var a = angle;
            a = normalizeDegrees(a);
            if ((0 <= a && a < 45) || (315 <= a && a <= 360) ) System.out.println(String.format("%-12s %+3.0f", "/\\ Upish", angle));
            if (45 <= a && a < 135 ) System.out.println(String.format("%-12s %+3.0f", "<< Leftish", angle));
            if (135 <= a && a < 225 ) System.out.println(String.format("%-12s %+3.0f", "\\/ Downish", angle));
            if (225 <= a && a  < 315) System.out.println(String.format("%-12s %+3.0f", ">> Rightish", angle));
            System.out.println(); 
            return this;
        }
        public ModuleInfo test() {
            var userDegrees = driverCommand.controllerCommand.getControllerDegrees();
            var expected = driverCommand.controllerCommand.getRobotDegrees();
            var actual = getRobotDegrees() - gyro.getHeading().in(Degrees);
            assertEquals(expected, normalizeDegrees(actual), 2.0);
            return this;
        }
    }

    static Translation2d getDirection(MotorController[] modules) {
        var v0 = new Translation2d(modules[0].get(), new Rotation2d(Degrees.of(-45)));
        var v1 = new Translation2d(modules[1].get(), new Rotation2d(Degrees.of(+45)));
        var v2 = new Translation2d(modules[2].get(), new Rotation2d(Degrees.of(+45)));
        var v3 = new Translation2d(modules[3].get(), new Rotation2d(Degrees.of(-45)));
        return v0.plus(v1.plus(v2.plus(v3)));
    }

    //* Fixing the constructor order from test 1 */
    @Test
    public void fieldRelativeTest2() {
        DriverControl driverControl = new DriverControl(true, false, false, true);

        driverControl.test(+1, Degrees.of(0), "Robot-up"); // drive up-right
        driverControl.test(-1, Degrees.of(0), "Robot-down"); // drive up-right
        driverControl.test(+1, Degrees.of(30), "Robot-Up+/Right"); // drive up-right
        driverControl.test(+1, Degrees.of(45), "Robot-Up/Right"); // drive up-right
        driverControl.test(+1, Degrees.of(60), "Robot-right-up+"); // drive up-right
        driverControl.test(+1, Degrees.of(75), "Robot-right-up"); // drive up-right
        driverControl.test(+1, Degrees.of(90), "Robot-right"); // drive up-right

        // rotation does NOT change direction (the additional velocity cancels out)
        driverControl.test(+1, Degrees.of(0), 0.125, "Robot-up/rotate 12%");
        driverControl.test(+1, Degrees.of(0), 0.25, "Robot-up/rotate 25%");
        driverControl.test(+1, Degrees.of(0), 0.5, "Robot-up/rotate 50%");
        driverControl.test(+1, Degrees.of(0), 1.0, "Robot-up/rotate 100%");
        
        driverControl.setHeading(0); // robot is facing EAST
        driverControl.test(+1, Degrees.of(+0), "go NORTH = Robot-Up");
        driverControl.test(+1, Degrees.of(+45), "go NORTH-EAST = Robot-Up/Right");
        driverControl.test(+1, Degrees.of(+90), "go EAST = Robot-Right");
        driverControl.test(+1, Degrees.of(+135), "go SOUTH-EAST = Robot-Down/Right");
        driverControl.test(+1, Degrees.of(+180), "go SOUTH = Robot-Down");
        driverControl.test(+1, Degrees.of(+270), "go WEST = robot-Left");

        driverControl.setHeading(90); // robot is facing EAST
        driverControl.test(+1, Degrees.of(+0), "go NORTH = Robot-Left");
        driverControl.test(+1, Degrees.of(+45), "go NORTH-EAST = Robot-Up/Left");
        driverControl.test(+1, Degrees.of(+90), "go EAST = Robot-Up");
        driverControl.test(+1, Degrees.of(+135), "go SOUTH-EAST = Robot-Up/Right");
        driverControl.test(+1, Degrees.of(+180), "go SOUTH = Robot-Right");
        driverControl.test(+1, Degrees.of(+270), "go WEST = robot-down");

        driverControl.setHeading(180); // robot is facing SOUTH
        driverControl.test(+1, Degrees.of(+0), "go NORTH = Robot-Down");
        driverControl.test(+1, Degrees.of(+90), "go EAST = Robot-Left");
        driverControl.test(+1, Degrees.of(+180), "go SOUTH = Robot-Up");
        driverControl.test(+1, Degrees.of(+270), "go WEST = robot-Right");

        
        driverControl.setHeading(270); // robot is facing WEST
        driverControl.test(+1, Degrees.of(+0), "go NORTH = Robot-Right");
        driverControl.test(+1, Degrees.of(+90), "go EAST = Robot-Down");
        driverControl.test(+1, Degrees.of(+180), "go SOUTH = Robot-Left");
        driverControl.test(+1, Degrees.of(+270), "go WEST = robot-Up");
/*
        // //also try inverted gyro -- YES
        // InvertGyro = -1.0;
        // gyroAngle = Units.Degree.of(90);
        // driverControl.show(+1, Degrees.of(0), "+90deg + NORTH - Robot-right");
        // driverControl.show(+1, Degrees.of(90), "+90deg + EAST");
        // gyroAngle = Units.Degree.of(270);
        // driverControl.show(+1, Degrees.of(0), "-90deg + NORTH - Robot-right");
        // driverControl.show(+1, Degrees.of(90), "-90deg + EAST");

        // InvertGyro = 1.0; // put is back

        gyroAngle = Units.Degree.of(90); // robot is facing EAST
        //  WORM   /\            Worms-eye view is from the bottom
        //         ||  Right
        //     -->   <--
        //   +-/// R \\\-+
        //   |   worm     >   X-->
        //   +-\\\ L ///-+
        //     <--   -->
        driverControl.show(+1, Degrees.of(-90), "GO West == Robot-left");

        //     <--  -->
        //   +-\\\  ///-+
        //   |           >   X-->
        //   +-///  \\\-+
        //     -->  <--
        //        ||  Right
        //        \/
        driverControl.show(+1, Degrees.of(90), "Robot-right");

        gyroAngle = Units.Degree.of(-90); //ccw
        //        /\
        //        ||  North
        //     -->  <--
        //   +-\\\  ///-+
        //   |           >   East-->
        //   +-///  \\\-+
        //     <--  -->
        driverControl.show(+1, Degrees.of(0), "NORTH - Robot-left");
        
        gyroAngle = Units.Degree.of(90); //ccw
        //        /\
        //        ||  North
        //     -->  <--
        //   +-\\\  ///-+
        //  <           |   <--West
        //   +-///  \\\-+
        //     <--  -->
        driverControl.show(+1, Degrees.of(0), "go NORTH - Robot-right");
        
        gyroAngle = Units.Degree.of(180);
        //        /\
        //        ||  East
        //     --<  <--
        //   +-\\\  ///-+
        //   |           >   South-->
        //   +-///  \\\-+
        //     <--  -->
        driverControl.show(+1, Degrees.of(90), "go EAST = ??");
        
        gyroAngle = Units.Degree.of(180);
        //            <--  <---
        //          +-\\\  ///-+
        //  <== Up  |           >   South-->
        //          +-///  \\\-+
        //            <--  <--
        driverControl.show(+1, Degrees.of(0), "go NORTH - ??");
*/

        driverControl.close();
    }

}
/* NED-ish from test 1

Robot-Left
Front -1.0 +1.0
Rear  +1.0 -1.0

Robot-Right
Front +1.0 -1.0
Rear  -1.0 +1.0

Field-North = Robot-Left
Front -1.0 +1.0
Rear  +1.0 -1.0

Field-North = Robot-Right
Front +1.0 -1.0
Rear  -1.0 +1.0

Field-East = Robot-Left
Front -1.0 +1.0
Rear  +1.0 -1.0

Field-Up = Robot-Backward
Front -1.0 -1.0
Rear  -1.0 -1.0

 */