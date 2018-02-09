package com.colony.samples.motorHAT;

import com.colony.samples.motorHAT.adafruitmotorhat.AdafruitMotorHAT;
import com.pi4j.io.i2c.I2CFactory;


import java.io.IOException;

/*
 * See https://learn.adafruit.com/adafruit-dc-and-stepper-motor-hat-for-raspberry-pi/using-stepper-motors
 */
public class StepperDemo extends Thread {
    private AdafruitMotorHAT mh;
    private AdafruitMotorHAT.AdafruitStepperMotor stepper;


    private boolean keepGoing = true;
    private final static String DEFAULT_RPM = "120";
    int nbSteps;

    private static int nbStepsPerRev = AdafruitMotorHAT.AdafruitStepperMotor.DEFAULT_NB_STEPS; // 200 steps per rev

    private final int port;
    public StepperDemo(int port) throws I2CFactory.UnsupportedBusNumberException {

        System.out.println("Starting Stepper Demo");
        int rpm = Integer.parseInt(System.getProperty("rpm", DEFAULT_RPM));


        nbSteps = Integer.parseInt(System.getProperty("steps", "40"));

        this.mh = new AdafruitMotorHAT(nbStepsPerRev); // Default addr 0x60
        this.port = port;
        this.stepper = mh.getStepper(port);
        this.stepper.setSpeed(rpm);


    }




    @Override
    public void run() {
        stopStepper();
        try {

            if (port == AdafruitMotorHAT.AdafruitStepperMotor.PORT_M1_M2) {
                for (int i = 0; i < 3; i++) {
                    stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.INTERLEAVE);
                    stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.INTERLEAVE);
               }
            }
            else {
                 for (int i = 0; i < 3; i++) {
                    stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.INTERLEAVE);
                    stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.INTERLEAVE);

                 }
            }


        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        stopStepper();

    }

    private void stopStepper() {
        this.keepGoing = false;
        if (mh != null) {
            try { // Release all
                mh.getMotor(AdafruitMotorHAT.Motor.M1).run(AdafruitMotorHAT.ServoCommand.RELEASE);
                mh.getMotor(AdafruitMotorHAT.Motor.M2).run(AdafruitMotorHAT.ServoCommand.RELEASE);
                mh.getMotor(AdafruitMotorHAT.Motor.M3).run(AdafruitMotorHAT.ServoCommand.RELEASE);
                mh.getMotor(AdafruitMotorHAT.Motor.M4).run(AdafruitMotorHAT.ServoCommand.RELEASE);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }




    public void go() {
//		keepGoing = true;
//		while (keepGoing) {
        try {
            System.out.println(String.format(
                    "-----------------------------------------------------------------------------------\n" +
                            "Motor # %d, RPM set to %f, %d Steps per Rev, %f sec per step, %d steps per move.\n" +
                            "-----------------------------------------------------------------------------------",

                    this.stepper.getMotorNum(),
                    this.stepper.getRPM(),
                    this.stepper.getStepPerRev(),
                    this.stepper.getSecPerStep(),
                    nbSteps));
            if (keepGoing) {
                System.out.println("-- 1. Single coil steps --");
                System.out.println("  Forward");
                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.SINGLE);
            }
            if (keepGoing) {
                System.out.println("  Backward");
                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.SINGLE);
            }
            if (keepGoing) {
                System.out.println("-- 2. Double coil steps --");
                System.out.println("  Forward");
                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.DOUBLE);
            }
            if (keepGoing) {
                System.out.println("  Backward");
                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.DOUBLE);
            }
            if (keepGoing) {
                System.out.println("-- 3. Interleaved coil steps --");
                System.out.println("  Forward");
                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.INTERLEAVE);
            }
            if (keepGoing) {
                System.out.println("  Backward");
                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.INTERLEAVE);
            }
            if (keepGoing) {
                System.out.println("-- 4. Microsteps --");
                System.out.println("  Forward");
                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.MICROSTEP);
            }
            if (keepGoing) {
                System.out.println("  Backward");
                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.MICROSTEP);
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        System.out.println("==== Again! ====");
        //	}
        System.out.println("... Done with the demo ...");
        stop();
//	try { Thread.sleep(1_000); } catch (Exception ex) {} // Wait for the motors to be released.
    }
}
