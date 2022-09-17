package frc.robot.component;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import org.team6083.lib.RobotPower;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Robot;

public class transport {
    private static WPI_VictorSPX top;
    private static WPI_VictorSPX buttom;
    private static RobotPower power;

    public static void init(){
        RobotPower.init(12);
        top = new WPI_VictorSPX(4);
        buttom = new WPI_VictorSPX(8);
        power = new RobotPower(0);

        
        
    }

    public static void teleOp(){
        
        if(power.getPortCurrent()>9){
            top.set(ControlMode.PercentOutput,-0.4);
            buttom.set(ControlMode.PercentOutput,-0.4);
        }else if(Robot.xbox_2.getRawButton(5)){
            top.set(ControlMode.PercentOutput,0.5);
            buttom.set(ControlMode.PercentOutput,0.5);
        }
        else if(Robot.xbox_2.getRawButton(6)){
            top.set(ControlMode.PercentOutput,-0.5);
            buttom.set(ControlMode.PercentOutput,-0.5);
        }
        else if(Robot.xbox_2.getBButton()){
            buttom.set(ControlMode.PercentOutput,0.5);
        }
        else if(Robot.xbox_2.getAButton()){
            top.set(ControlMode.PercentOutput,0.5);
        }
        else{
            top.set(ControlMode.PercentOutput,0);
            buttom.set(ControlMode.PercentOutput,0);
        }

        

        SmartDashboard.putNumber("Amp", power.getPortCurrent());
    }
    public static void autonomousTransport(){
        top.set(ControlMode.PercentOutput,-0.5);
        buttom.set(ControlMode.PercentOutput,-0.5);
    }

}
