package com.colony;

import com.colony.samples.motorHAT.StepperDemo;
import com.colony.samples.motorHAT.adafruitmotorhat.AdafruitMotorHAT;
import com.pi4j.io.i2c.I2CFactory;

public class Program {

    public static void main(String... args) throws I2CFactory.UnsupportedBusNumberException, InterruptedException {

        Program program = new Program();
        program.go();

    }



    private void go() throws I2CFactory.UnsupportedBusNumberException, InterruptedException {

        StepperDemo stepper1 = new StepperDemo(AdafruitMotorHAT.AdafruitStepperMotor.PORT_M1_M2);
        StepperDemo stepper2 = new StepperDemo(AdafruitMotorHAT.AdafruitStepperMotor.PORT_M3_M4);



        stepper1.start();
        stepper2.start();




    }


}
