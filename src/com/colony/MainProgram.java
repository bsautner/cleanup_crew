package com.colony;

import com.colony.sensor.SensorEvent;
import com.colony.servo.Devices;
import com.colony.servo.ServoController;
import com.pi4j.util.Console;
import io.reactivex.Completable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import java.util.HashMap;
import java.util.Map;

public class MainProgram {


    private static MainProgram mainProgram;
    private static Console console;

    private ServoController servoController;
    private ArduinoSerialInterface arduinoSerialInterface;


    private Map<Integer, Servo> servos = new HashMap<>();

    private final static int SERVO_A0 = 0;
    private final static int SERVO_B0 = 12;



    public void initHardware() {
        servoController = new ServoController();
        arduinoSerialInterface = new ArduinoSerialInterface();
    }


    public static void main(String... args) throws InterruptedException {

        console = new Console();
        mainProgram = new MainProgram();



        console.promptForExit();


        console.box("Starting Up Main Program");
        mainProgram.initHardware();


        mainProgram.start();

         mainProgram.calibrateServos();

        while (console.isRunning()) {
            Thread.sleep(1000);
        }





    }


    private void calibrateServos() throws InterruptedException {

        Completable a0 = servoController.angle(SERVO_A0, -90);
        Completable b0 = servoController.angle(SERVO_B0, 90);
        a0.subscribeOn(Schedulers.newThread()).subscribe();
        b0.subscribeOn(Schedulers.newThread()).subscribe();


        Thread.sleep(2000);

        int min = Devices.servoA0.getCurrentPosition();
        Devices.servoA0.setMin(min);

        Completable a0m = servoController.angle(SERVO_A0, 90);
        Completable b0m = servoController.angle(SERVO_B0, -90);
        a0m.subscribeOn(Schedulers.newThread()).subscribe();
        b0m.subscribeOn(Schedulers.newThread()).subscribe();







    }

    private void start() {







                arduinoSerialInterface.startListening(new Observer<SensorEvent>() {

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(SensorEvent s) {

                        switch (s) {

                            case sonarDistanceMeasured:
                                //System.out.println("distance measured");

//                                if (Devices.sonarA0.getDistanceToObject() > -1 && Devices.sonarA0.getDistanceToObject() < 5) {
//
//                                    backupSteps();
//                                }
//                                else {
//
//                                }
                                break;
                            case servoMoved:
                                console.println("servo moved");
                                console.println(Devices.servoA0.getCurrentPosition());
                                break;
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });

            }







    private void backupSteps() {



        console.println("Backing Up...");

        Completable leftABack = servoController.angle(SERVO_A0, -90);
        Completable rightBBack = servoController.angle(SERVO_B0, 90);

        Completable leftAForward = servoController.angle(SERVO_A0, 90);
        Completable rightBForward = servoController.angle(SERVO_B0, -90);

//        Completable yn = servoController.angle(LF_Y,  90);
//        Completable zn = servoController.angle(LF_Y,  90);


        leftABack.concatWith(leftAForward).subscribeOn(Schedulers.io()).subscribe();
        rightBForward.concatWith(rightBBack).subscribeOn(Schedulers.io()).subscribe();

    }












}
