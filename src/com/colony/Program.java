package com.colony;

import com.colony.samples.motorHAT.StepperMotorController;
import com.colony.samples.motorHAT.adafruitmotorhat.AdafruitMotorHAT;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.io.i2c.I2CFactory;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BooleanSupplier;
import io.reactivex.schedulers.Schedulers;

public class Program {

    StepperMotorController stepper1;
    StepperMotorController stepper2;

    private boolean running = false;
    Disposable stepper1disposable;
    Disposable stepper2disposable;





    private void go() throws I2CFactory.UnsupportedBusNumberException, InterruptedException {

        stepper1 = new StepperMotorController(AdafruitMotorHAT.AdafruitStepperMotor.PORT_M1_M2);
        stepper2 = new StepperMotorController(AdafruitMotorHAT.AdafruitStepperMotor.PORT_M3_M4);


        listen();




    }



    private void listen() throws InterruptedException {

        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_02, PinPullResistance.PULL_DOWN);

        // set shutdown state for this input pin
        myButton.setShutdownOptions(true);

        System.out.println(myButton.getState());

        // create and register gpio pin listener
        myButton.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                int steps = 360;
                System.out.printf(" --> GPIO PIN STATE CHANGE: %s=%s %b \n", event.getPin(), event.getState(), running);
                Observable<Boolean> f1 = stepper1.walk(AdafruitMotorHAT.ServoCommand.FORWARD, steps);
                //Observable<Boolean> b1 = stepper1.walk(AdafruitMotorHAT.ServoCommand.BACKWARD, steps);


                Observable<Boolean> f2 = stepper2.walk(AdafruitMotorHAT.ServoCommand.BACKWARD, steps);
                // Observable<Boolean> b2 = stepper2.walk(AdafruitMotorHAT.ServoCommand.BACKWARD, steps);
                stepper1.stopStepper();
                stepper2.stopStepper();
                if (event.getState().isHigh()) {

                    if (running) {
                        System.out.println("Shutting down");
                        if (stepper1disposable != null && !stepper1disposable.isDisposed()) {

                            stepper1disposable.dispose();

                        }
                        if (stepper2disposable != null && !stepper2disposable.isDisposed()) {
                            stepper2disposable.dispose();

                        }
                    } else {
                        System.out.println("Starting up!");





                        BooleanSupplier booleanSupplier = new BooleanSupplier() {
                            @Override
                            public boolean getAsBoolean() throws Exception {
                                return ! running;
                            }
                        };


                        stepper1disposable = f1.repeatUntil(booleanSupplier)
                                .subscribeOn(Schedulers.io())
                                .subscribe();

                        stepper2disposable = f2.repeatUntil(booleanSupplier)
                                .subscribeOn(Schedulers.io())
                                .subscribe();


                    }


                    running = !running;
                }
            }
        });





        System.out.println(" ... complete the GPIO #02 circuit and see the listener feedback here in the console.");

        // keep program running until user aborts (CTRL-C)
        while(true) {
            Thread.sleep(500);
        }

        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller
    }
}





