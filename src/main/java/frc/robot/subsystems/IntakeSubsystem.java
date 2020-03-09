/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.IntakeConstants;

public class IntakeSubsystem extends SubsystemBase {
  /**
   * Creates a new IntakeSubsystem.
   */
  private final WPI_TalonSRX intakeRoller = new WPI_TalonSRX(IntakeConstants.INTAKE_ROLLER_CAN_ID);


  public IntakeSubsystem() {
    // if it is pushing it out with a positive value change this
    intakeRoller.configOpenloopRamp(.3);
    intakeRoller.setInverted(false);
  }


  public void setIntakeRollerSpeed(double speed) {
    intakeRoller.set(speed);
  }

}
