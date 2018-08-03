package com.colony;

import com.colony.serial.SerialPortIO;
import com.colony.serial.SerialPortListener;
import com.colony.servo.ServoController;
import com.pi4j.util.Console;
import io.reactivex.Completable;
import io.reactivex.functions.Action;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainProgram {


    private static MainProgram mainProgram;
    private static Console console;

    private ServoController servoController;


    private Map<Integer, Servo> servos = new HashMap<>();

    private final static int LEFT_SHOULDER_X = 0;
    private final static int LEFT_SHOULDER_Y = 1;
    private final static int RIGHT_SHOULDER_X = 12;
    private final static int RIGHT_SHOULDER_Y = 13;
    private double lastX = 0.0;
    private int lastXPos = 90;

    public void start() {

        SerialPortIO serialPortIO = new SerialPortIO("ttyACM1", serialPortListener);
        servoController = new ServoController();
        serialPortIO.start();
        buildServoList();

        Completable n = servoController.angle(LEFT_SHOULDER_Y,  -90);
        Completable p = Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Thread.sleep(2000);
            }
        });

        Completable f = servoController.angle(LEFT_SHOULDER_Y,  90);


        Completable.concatArray(n, p, f).subscribe();


    }

    SerialPortListener serialPortListener = new SerialPortListener() {
        @Override
        public void onRead(String rawData) {

            String[] parts = rawData.split(",");
         //   console.print(new Date().toString() +  " - serial data in - " + rawData);

            for (String s : parts) {
                s = s.replace("[", "").replace("]", "");
           //     console.println(s);
                String[] m = s.split(":");
                if (m.length == 2) {

                    if (m[0].equals("X")) {
                        double d = Double.valueOf(m[1]) * 100;

                        Integer v = (int) d;
                        console.println("change in x " + v);
                        if (! v.equals(lastX)) {

                            lastX = v;

                            int newPos = lastXPos + v;
                            if (newPos < -90) {
                                newPos = -90;
                            }
                            else if (newPos > 90) {
                                newPos = 90;
                            }

                            lastXPos= newPos;
                            console.println("moving on X " + lastXPos);
                             servoController.angle(LEFT_SHOULDER_Y, newPos).subscribe();
                        }
                    }

                }
            }


        }

        @Override
        public void onError(Throwable throwable) {
           console.print(throwable.getMessage());
        }
    };

    public static void main(String... args) throws InterruptedException {

        console = new Console();
        mainProgram = new MainProgram();


        console.promptForExit();


        console.box("Starting Up Main Program");


        mainProgram.start();

        while (console.isRunning()) {
            Thread.sleep(1000);
        }




    }

    private void buildServoList() {
        Servo rightShoulder = new Servo(RIGHT_SHOULDER_X, -90, 45);
        Servo leftShoulder = new Servo(LEFT_SHOULDER_X, 90, -45);
        Servo rightShoulderY = new Servo(RIGHT_SHOULDER_Y, 0, 45);
        Servo leftShoulderY = new Servo(LEFT_SHOULDER_Y, 0, -45);

        servos.put(RIGHT_SHOULDER_X, rightShoulder);
        servos.put(LEFT_SHOULDER_X, leftShoulder);
        servos.put(RIGHT_SHOULDER_Y, rightShoulderY);
        servos.put(LEFT_SHOULDER_Y, leftShoulderY);


    }


}
