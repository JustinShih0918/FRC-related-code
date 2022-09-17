package frc.robot.component;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Robot;
public class up{
   
    private static WPI_VictorSPX hook;
    private static WPI_VictorSPX rope;
   
    public static void init(){
    hook =  new WPI_VictorSPX(10);
    rope = new WPI_VictorSPX(2);
    }
    public static void teleop(){
        if(Robot.xbox_1.getXButton()){
            rope.set(ControlMode.PercentOutput,0.8);
        }
        else if(Robot.xbox_1.getAButton()){
            rope.set(ControlMode.PercentOutput,-0.8);
        }
        else{
            rope.set(ControlMode.PercentOutput,0);
        }
        
        if(Robot.xbox_2.getTriggerAxis(Hand.kRight)>0.7){
            hook.set(ControlMode.PercentOutput,-0.5);
        }
        else if(Robot.xbox_2.getTriggerAxis(Hand.kLeft)>0.7){
            hook.set(ControlMode.PercentOutput,0.5);
        }
        else{
            hook.set(ControlMode.PercentOutput,0);
        }
    }
}


