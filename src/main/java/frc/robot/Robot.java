/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

// import java.io.IOException;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.Constants.OIConstants;
import frc.robot.Constants.XboxConstants;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
// import edu.wpi.first.networktables.NetworkTableValue;
/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private Command autonomousCommand;
  private RobotContainer robotContainer;

NetworkTable table;
DriveSubsystem drive = new DriveSubsystem();

NetworkTableEntry targetX;
NetworkTableEntry targetY;
SmartDashboard tx;

  //Error values for the control loop
  double rotationError;
  double distanceError;

  //Control Loop constants
  //Positive due to arcadeDrive controls
  double KpRot = 0.75;


  double angleTolerance = 5; //Testing

  double constantForce =0.10;
  XboxController driverController = new XboxController(OIConstants.DRIVER_CONTROLLER_PORT);  

  double rotationAjust;
  double distanceAjust;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */

  // private double startTime;
   
  @Override
  public void robotInit() {

    // Instantiate our RobotContainer. This will perform all our button bindings,
    // and put our
    // autonomous chooser on the dashboard.

    //network tables
    table = NetworkTableInstance.getDefault().getTable("chameleon-vision").getSubTable("Microsoft LifeCam HD-3000");

    targetX = table.getEntry("targetYaw");
    targetY = table.getEntry("targetPitch");
    
    SmartDashboard.getNumber("targetYaw", 0.0); //Test this line

    robotContainer = new RobotContainer();

    CameraServer.getInstance().startAutomaticCapture();

  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * 
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    rotationAjust = 5;
    distanceAjust = 0;

    if (driverController.getRawButton(XboxConstants.A_BUTTON)){ 
      System.out.println(driverController.getRawButton(XboxConstants.A_BUTTON));
      rotationError = targetX.getDouble(0.0);
      distanceError = targetY.getDouble(0.0);
      // rotationAjust = rotationError; 
      if (rotationError > angleTolerance){
        rotationAjust = KpRot * rotationError - constantForce;
      }
      else {
        if  (rotationError < angleTolerance) {
          rotationAjust = KpRot * rotationError - constantForce;
        }
      }
      drive.arcadeDrive(0, rotationAjust);
      }
    // if(TestRot()){
    //   drive.arcadeDrive(0.9, 0); //Negative Rot turns left
    // }

    // Runs the Scheduler. This is responsible for polling buttons, adding
    // newly-scheduled
    // commands, running already-scheduled commands, removing finished or
    // interrupted commands,
    // and running subsystem periodic() methods. This must be called from the
    // robot's periodic
    // block in order for anything in the Command-based framework to work.

    CommandScheduler.getInstance().run();
    

    // put smartdahs board things here? check on CD to see if this is appropriate
    //SmartDashboard.putNumber("Driver Y Axis", -robotContainer.driverController.getY(Hand.kLeft));
    //SmartDashboard.putNumber("Driver X Axis", robotContainer.driverController.getX(Hand.kRight));
    //SmartDashboard.putNumber("Driver Left Trigger",
      //  robotContainer.driverController.getRawAxis(XboxConstants.LEFT_TRIGGER_AXIS));

      
  }

  /**
   * This function is called once each time the robot enters Disabled mode.
   */
  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  /**
   * This autonomous runs the autonomous command selected by your
   * {@link RobotContainer} class.
   */
  @Override
  public void autonomousInit()  {
    
    // robotContainer.robotDrive resetEncoders();
  
      autonomousCommand = robotContainer.getAutonomousCommand();
  
      // schedule the autonomous command (example)
      if (autonomousCommand != null) {
        autonomousCommand.schedule();
      }
      
    }

   
  
  

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    CommandScheduler.getInstance().run();
  //   double time =  Timer.getFPGATimestamp();

  //   if (time - startTime < 3) {
  //   m_robotContainer.robotDrive.
  //   leftMotor1.set(0.6);
  //   leftMotor2.set(0.6);
  //   rightMotor1.set(-0.6);
  //   rightMotor2.set(-0.6);
    
  // }else{

  //   leftMotor1.set(0);
  //   leftMotor2.set(0);
  //   rightMotor1.set(0);
  //   rightMotor2.set(0);

  }

  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (autonomousCommand != null) {
      autonomousCommand.cancel();
    }
    
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    // SmartDashboard.putNumber("Driver Axis",
    // m_robotContainer.driverController.getY(Hand.kLeft));
  }

  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
  }
}
  
//Gyro 