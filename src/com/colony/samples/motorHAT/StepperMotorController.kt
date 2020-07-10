package com.colony.samples.motorHAT

import com.colony.samples.motorHAT.adafruitmotorhat.AdafruitMotorHAT
import com.colony.samples.motorHAT.adafruitmotorhat.AdafruitMotorHAT.AdafruitStepperMotor
import com.colony.samples.motorHAT.adafruitmotorhat.AdafruitMotorHAT.ServoCommand
import io.reactivex.Observable
import java.io.IOException

/*
 * See https://learn.adafruit.com/adafruit-dc-and-stepper-motor-hat-for-raspberry-pi/using-stepper-motors
 */
class StepperMotorController(port: Int) {
    private val mh: AdafruitMotorHAT?
    private val stepper: AdafruitStepperMotor
    fun walk(command: ServoCommand?, steps: Int): Observable<Boolean> {
        return Observable.fromCallable {
            stepper.step(steps, command, AdafruitMotorHAT.Style.DOUBLE)
            true
        }
    }

     fun step(command: ServoCommand?, steps: Int)  {

            stepper.step(steps, command, AdafruitMotorHAT.Style.DOUBLE)

    }


    fun stopStepper() {
        if (mh != null) {
            try { // Release all
                mh.getMotor(AdafruitMotorHAT.Motor.M1).run(ServoCommand.RELEASE)
                mh.getMotor(AdafruitMotorHAT.Motor.M2).run(ServoCommand.RELEASE)
                mh.getMotor(AdafruitMotorHAT.Motor.M3).run(ServoCommand.RELEASE)
                mh.getMotor(AdafruitMotorHAT.Motor.M4).run(ServoCommand.RELEASE)
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
        }
    } //    public void go() {

    ////		keepGoing = true;
    ////		while (keepGoing) {
    //        try {
    //            System.out.println(String.format(
    //                    "-----------------------------------------------------------------------------------\n" +
    //                            "Motor # %d, RPM set to %f, %d Steps per Rev, %f sec per step, %d steps per move.\n" +
    //                            "-----------------------------------------------------------------------------------",
    //
    //                    this.stepper.getMotorNum(),
    //                    this.stepper.getRPM(),
    //                    this.stepper.getStepPerRev(),
    //                    this.stepper.getSecPerStep(),
    //                    nbSteps));
    //            if (keepGoing) {
    //                System.out.println("-- 1. Single coil steps --");
    //                System.out.println("  Forward");
    //                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.SINGLE);
    //            }
    //            if (keepGoing) {
    //                System.out.println("  Backward");
    //                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.SINGLE);
    //            }
    //            if (keepGoing) {
    //                System.out.println("-- 2. Double coil steps --");
    //                System.out.println("  Forward");
    //                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.DOUBLE);
    //            }
    //            if (keepGoing) {
    //                System.out.println("  Backward");
    //                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.DOUBLE);
    //            }
    //            if (keepGoing) {
    //                System.out.println("-- 3. Interleaved coil steps --");
    //                System.out.println("  Forward");
    //                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.INTERLEAVE);
    //            }
    //            if (keepGoing) {
    //                System.out.println("  Backward");
    //                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.INTERLEAVE);
    //            }
    //            if (keepGoing) {
    //                System.out.println("-- 4. Microsteps --");
    //                System.out.println("  Forward");
    //                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.FORWARD, AdafruitMotorHAT.Style.MICROSTEP);
    //            }
    //            if (keepGoing) {
    //                System.out.println("  Backward");
    //                this.stepper.step(nbSteps, AdafruitMotorHAT.ServoCommand.BACKWARD, AdafruitMotorHAT.Style.MICROSTEP);
    //            }
    //        } catch (IOException ioe) {
    //            ioe.printStackTrace();
    //        }
    //        System.out.println("==== Again! ====");
    //        //	}
    //        System.out.println("... Done with the demo ...");
    //        stop();
    ////	try { Thread.sleep(1_000); } catch (Exception ex) {} // Wait for the motors to be released.
    //    }
    companion object {
        private const val DEFAULT_RPM = "480"
        private const val nbStepsPerRev = AdafruitStepperMotor.DEFAULT_NB_STEPS // 200 steps per rev
    }

    init {
        println("Starting Stepper Demo")
        val rpm = System.getProperty("rpm", DEFAULT_RPM).toInt()
        mh = AdafruitMotorHAT(nbStepsPerRev) // Default addr 0x60
        stepper = mh.getStepper(port)
        stepper.setSpeed(rpm.toDouble())
    }
}