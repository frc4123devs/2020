/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands.auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.IndexerCommand;
import frc.robot.commands.IntakeDeployGateCommand;
import frc.robot.commands.IntakeBallInCommand;
import frc.robot.commands.ShootWithDistanceCommand;
import frc.robot.commands.ShooterCommand;
import frc.robot.subsystems.IndexSubsystem;
import frc.robot.subsystems.IntakeGateSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.trajectories.TrajectoryTracking;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/latest/docs/software/commandbased/convenience-features.html
public class RedFiveBallAuto extends SequentialCommandGroup {
  /**
   * Creates a new Testing.
   */
  public RedFiveBallAuto(TrajectoryTracking trajectoryPath, IntakeGateSubsystem intakeGateSubsystem, IntakeSubsystem intakeSubsystem, ShooterSubsystem shooterSubsystem, IndexSubsystem indexSubsystem) {
    // Add your commands in the super() call, e.g.
    // super(new FooCommand(), new BarCommand());
    super(
      new IntakeDeployGateCommand(intakeGateSubsystem).withTimeout(0.9),
          trajectoryPath.getRamsete(trajectoryPath.OpponentTrenchSteal).raceWith(
            new IntakeBallInCommand(intakeSubsystem)),
          trajectoryPath.getRamsete(trajectoryPath.OpponentTrenchBack),
          // new ShootWithDistanceCommand(shooterSubsystem)
          new ShooterCommand(shooterSubsystem).alongWith(new IndexerCommand(indexSubsystem))
          );
  }
}