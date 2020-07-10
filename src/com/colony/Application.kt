package com.colony

import com.colony.samples.motorHAT.StepperMotorController
import com.colony.samples.motorHAT.adafruitmotorhat.AdafruitMotorHAT
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class Application {

    val stepper1: StepperMotorController = StepperMotorController(AdafruitMotorHAT.AdafruitStepperMotor.PORT_M1_M2)
    val stepper2: StepperMotorController = StepperMotorController(AdafruitMotorHAT.AdafruitStepperMotor.PORT_M3_M4)

    fun listen() {



            stepper1.stopStepper()
        stepper2.stopStepper()

            println("Stepping")
        GlobalScope.launch {
            stepper1.step(AdafruitMotorHAT.ServoCommand.FORWARD, 360)
        }
        GlobalScope.launch {
            stepper2.step(AdafruitMotorHAT.ServoCommand.BACKWARD, 360)
        }


        stepper1.stopStepper()
        stepper2.stopStepper()

        Thread.sleep(5000) // wait for 2 seconds
        println("Stop")
    }

    companion object {

        @JvmStatic
        fun main(vararg main: String) {
            println("hello world")
            val app = Application()
            app.listen()

        }
    }



}