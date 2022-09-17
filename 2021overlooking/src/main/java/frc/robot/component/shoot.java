package frc.robot.component;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.GenericHID.Hand;
import frc.robot.Robot;

public class shoot{
    private static WPI_VictorSPX shoot;
    private static WPI_VictorSPX turn;

    public static void init(){
         shoot =  new WPI_VictorSPX(1);
         turn = new WPI_VictorSPX(11);
    }
    
    public static void teleop(){
        if(Robot.xbox_1.getBButton()){
            shoot.set(0.85);
            if(shoot.get()>0.8){
                transport.autonomousTransport();
            }
        }else{
            shoot.set(0);
        }
        
        if(Robot.xbox_2.getTriggerAxis(Hand.kLeft)>Robot.xbox_2.getTriggerAxis(Hand.kRight)){
            turn.set(ControlMode.PercentOutput,-0.1);
        }
        else if(Robot.xbox_2.getTriggerAxis(Hand.kRight)>Robot.xbox_2.getTriggerAxis(Hand.kLeft)){
            turn.set(ControlMode.PercentOutput,0.1);
        }
        else{
            turn.set(ControlMode.PercentOutput,0);
        }
    }
    public static void shootingTarget() {
        shoot.set(0.8);
        transport.autonomousTransport();
    }
    public static void findingTarget() {
        turn.set(0.1);
    }
    public static void aimingTarget(double shootSpeeed){
        shoot.set(shootSpeeed);
    }
}