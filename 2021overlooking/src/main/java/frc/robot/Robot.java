/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2020 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
import edu.wpi.first.wpilibj.TimedRobot;
import frc.robot.component.VisionTracking;
import frc.robot.component.drivebase;
import frc.robot.component.shoot;
import frc.robot.component.suck;
import frc.robot.component.transport;
import frc.robot.component.up;
import frc.robot.system.NewAutoEngine;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  public static xbox xbox_1;
  public static xbox xbox_2;
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
      xbox_1 = new xbox(0);
      xbox_2 = new xbox(1);

      drivebase.init();
      shoot.init();
      suck.init();
      transport.init();
      up.init();
      VisionTracking.init();
      // NewAutoEngine.init();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
  //   NewAutoEngine.start();
  }

  @Override
  public void autonomousPeriodic() {
    // NewAutoEngine.loop();
  }

  @Override
  public void teleopInit() {
  }

  @Override
  public void teleopPeriodic() {
    drivebase.teleOp();
    shoot.teleop();
    suck.teleop();
    transport.teleOp();
    up.teleop();
    VisionTracking.teleop();
  }

  @Override
  public void disabledInit() {
  }

  @Override
  public void disabledPeriodic() {
  }

  @Override
  public void testInit() {
  }

  @Override
  public void testPeriodic() {
  }

}
