package frc.robot.component;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import frc.robot.Robot;

public class suck{
    private static VictorSP suck;
    private static Compressor comRight;
    private static Boolean compreBoolean;
    private static DoubleSolenoid SolRight;
    private static Boolean com;

    public static void init(){
         suck =  new VictorSP(0);
         comRight = new Compressor();
         SolRight = new DoubleSolenoid(4,5);
         compreBoolean = true;
         com = true;
         comRight.setClosedLoopControl(compreBoolean);
    }

    public static void teleop(){
        if(Robot.xbox_1.getRawButtonPressed(10)) {
            com = !com;
        }
        
        if(com){
            SolRight.set(Value.kForward);
        }
        else if(com==false){
            SolRight.set(Value.kReverse);
        }
        else{
            SolRight.set(Value.kOff);
        }

        if(Robot.xbox_2.getYButton()){
            suck.set(0.5);
        }
        else if(Robot.xbox_1.getYButton()){
            suck.set(-0.5);
        }
        else{
            suck.set(0);
        }

    }

}