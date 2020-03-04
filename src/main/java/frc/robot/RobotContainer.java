/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

// import java.io.IOException;
// import java.nio.file.Paths;
// import java.util.List;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
// import edu.wpi.first.wpilibj.trajectory.Trajectory;
// import edu.wpi.first.wpilibj.trajectory.TrajectoryConfig;
// import edu.wpi.first.wpilibj.trajectory.TrajectoryGenerator;
// import edu.wpi.first.wpilibj.trajectory.TrajectoryUtil;
// import edu.wpi.first.wpilibj.trajectory.constraint.DifferentialDriveVoltageConstraint;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandGroupBase;
// import edu.wpi.first.wpilibj2.command.RamseteCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.WaitCommand;
// import edu.wpi.first.wpilibj2.command.StartEndCommand;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
// import edu.wpi.first.wpilibj.controller.RamseteController;
// import edu.wpi.first.wpilibj.geometry.Pose2d;
// import edu.wpi.first.wpilibj.geometry.Rotation2d;
// import edu.wpi.first.wpilibj.geometry.Translation2d;
// import frc.robot.Constants.DriveConstants;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.XboxConstants;
import frc.robot.commands.AutoAngleCommand;
import frc.robot.commands.AutoDriveBackCommand;
import frc.robot.commands.ElevatorDownCommand;
import frc.robot.commands.ElevatorUpCommand;
import frc.robot.commands.IndexWheelCommand;
import frc.robot.commands.IntakeInCommand;
import frc.robot.commands.IntakeOutCommand;
import frc.robot.commands.IntakeGateDownCommand;
import frc.robot.commands.IntakeGateUpCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.commands.WinchDownCommand;
import frc.robot.commands.WinchUpCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.subsystems.ElevatorSubsystem;
import frc.robot.subsystems.IndexSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.subsystems.WinchSubsystem;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the Robot periodic methods (other than the scheduler
 * calls). Instead, the structure of the robot (including subsystems, commands,
 * and button mappings) should be declared here.
 */
public class RobotContainer {
  // Subsystems
  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  private final ElevatorSubsystem elevatorSubsystem = new ElevatorSubsystem();
  private final IndexSubsystem indexSubsystem = new IndexSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final WinchSubsystem winchSubsystem = new WinchSubsystem();
  // Commands
  private final AutoAngleCommand autoAimCommand = new AutoAngleCommand(driveSubsystem);
  private final ElevatorDownCommand elevatorDownCommand = new ElevatorDownCommand(elevatorSubsystem);
  private final ElevatorUpCommand elevatorUpCommand = new ElevatorUpCommand(elevatorSubsystem);
  private final IntakeGateDownCommand intakeGateDownCommand = new IntakeGateDownCommand(intakeSubsystem);
  private final IntakeGateUpCommand intakeGateUpCommand = new IntakeGateUpCommand(intakeSubsystem);
  private final IntakeInCommand intakeInCommand = new IntakeInCommand(intakeSubsystem);
  private final IntakeOutCommand intakeOutCommand = new IntakeOutCommand(intakeSubsystem);
  // private final IndexWheelCommand indexCommand = new
  // IndexWheelCommand(indexSubsystem);
  // private final ShooterCommand shooterCommand = new
  // ShooterCommand(shooterSubsystem);
  private final WinchDownCommand winchDownCommand = new WinchDownCommand(winchSubsystem);
  private final WinchUpCommand winchUpCommand = new WinchUpCommand(winchSubsystem);

  public DriveSubsystem getDriveSubsystem() {
    return driveSubsystem;
  }

  // The driver's controller
  XboxController driverController = new XboxController(OIConstants.DRIVER_CONTROLLER_PORT);
  // aux driver
  Joystick auxDriverController = new Joystick(OIConstants.AUXDRIVER_CONTROLLER_PORT);

  /**
   * Prints "Gyro is calibrating..." and calibrates the gyro.
   */
  // put in robot init so it calibrates on turn on... maybe we want that?
  private void calibrate() {
    System.out.println("Gyro is calibrating...");
    driveSubsystem.getGyro().calibrate();
  }

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {

    calibrate();
    // Configure the button bindings
    configureButtonBindings();
    // Configure default commands
    driveSubsystem.setDefaultCommand(new RunCommand(() -> {
      driveSubsystem.arcadeDrive(-driverController.getY(GenericHID.Hand.kLeft),
          driverController.getX(GenericHID.Hand.kRight));
      // System.out.println("Driver turn" +
      // driverController.getX(GenericHID.Hand.kRight));
    }, driveSubsystem));

  }

  // Use this method to define your button->command mappings.
  private void configureButtonBindings() {

    // shoot sequence
    new JoystickButton(driverController, XboxConstants.LEFT_STICK).whenPressed(() -> driveSubsystem.setMaxOutput(.5))
        .whenReleased(() -> driveSubsystem.setMaxOutput(1));
    new JoystickButton(driverController, XboxConstants.RIGHT_STICK).whenPressed(() -> driveSubsystem.setMaxOutput(.5))
        .whenReleased(() -> driveSubsystem.setMaxOutput(1));

    // new JoystickButton(driverController, XboxConstants.A_BUTTON);
    // new JoystickButton(driverController, XboxConstants.B_BUTTON);
    new JoystickButton(driverController, XboxConstants.X_BUTTON).whileHeld(intakeGateDownCommand);
    new JoystickButton(driverController, XboxConstants.Y_BUTTON).whileHeld(intakeGateUpCommand);
    new JoystickButton(driverController, XboxConstants.LB_BUTTON).whileHeld(elevatorDownCommand);
    new JoystickButton(driverController, XboxConstants.RB_BUTTON).whileHeld(elevatorUpCommand);

    // auxcommands]\

    new JoystickButton(auxDriverController, XboxConstants.A_BUTTON).whileHeld(winchDownCommand);
    new JoystickButton(auxDriverController, XboxConstants.B_BUTTON).whileHeld(winchUpCommand);
    new JoystickButton(auxDriverController, XboxConstants.X_BUTTON).whileHeld(autoAimCommand);
    new JoystickButton(auxDriverController, XboxConstants.Y_BUTTON).whileHeld(new ShooterCommand(shooterSubsystem)
        .alongWith(new WaitCommand(1).andThen(new IndexWheelCommand(indexSubsystem))));
    new JoystickButton(auxDriverController, XboxConstants.RB_BUTTON).whileHeld(intakeOutCommand);
    new JoystickButton(auxDriverController, XboxConstants.LB_BUTTON).whileHeld(intakeInCommand);

  }

  public Command getAutonomousCommand() {
    // Must be aligned to the bottom left corner; middle wheel on the initiation
    // line.
    // clear the command group to use it again, dont do this in teleop
    CommandGroupBase.clearGroupedCommands();

    return new AutoDriveBackCommand(driveSubsystem).andThen(new WaitCommand(.2)
        .andThen(new ShooterCommand(shooterSubsystem)).alongWith(new IndexWheelCommand(indexSubsystem).withTimeout(7)));

  }

}
