package com.colony;

import com.colony.serial.SerialPortIO;
import com.colony.serial.SerialPortListener;
import com.colony.servo.ServoController;
import com.pi4j.util.Console;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainProgram {


    private static MainProgram mainProgram;
    private static Console console;

    private ServoController servoController;


    private Map<Integer, Servo> servos = new HashMap<>();

    private final static int LF_X = 0;
    private final static int LF_Y = 1;
    private final static int LF_Z= 2;
    private final static int RF_X = 4;
    private final static int RF_Y = 5;
    private final static int RF_Z = 6;
    private double lastX = 0.0;
    private int lastXPos = 90;

    public void initHardware() {
        servoController = new ServoController();
    }


    public void start() {

        SerialPortIO serialPortIO = new SerialPortIO("ttyACM1", serialPortListener);
        servoController = new ServoController();
        serialPortIO.start();
        buildServoList();

        Completable x = servoController.angle(LF_X,  -90);
        Completable y = servoController.angle(LF_Y,  -90);
        Completable z = servoController.angle(LF_Z,  -90);


        Completable p = Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                Thread.sleep(2000);
            }
        });

        Completable xn = servoController.angle(LF_Y,  90);
        Completable yn = servoController.angle(LF_Y,  90);
        Completable zn = servoController.angle(LF_Y,  90);


        Completable.concatArray(x, y, z, p, xn, yn, zn).subscribe();


    }

    Completable pause = Completable.fromAction(new Action() {
        @Override
        public void run() throws Exception {
            Thread.sleep(2000);
        }
    });

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
                             servoController.angle(LF_Y, newPos).subscribe();
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

    public void neutral() {
        Completable rxf = servoController.angle(RF_X,  -80);
        Completable rxb = servoController.angle(RF_X,  0);
        Completable ryu = servoController.angle(RF_Y,  -75);
        Completable ryd = servoController.angle(RF_Y,  -0);
        Completable rzu = servoController.angle(RF_Z,  90);
        Completable rzd = servoController.angle(RF_Z,  -45);



        Completable lxf = servoController.angle(LF_X,  -80);
        Completable lxb = servoController.angle(LF_X,  -90);
        Completable lyu = servoController.angle(LF_Y,  0);
        Completable lyd = servoController.angle(LF_Y,  -75);

        Completable lzu = servoController.angle(LF_Z,  -90);
        Completable lzd = servoController.angle(LF_Z,  45);



        //Completable.concatArray(x, y, z, p, xn, yn, zn).subscribe();
      //  Completable.concatArray(yu, zu, yd, zd).subscribe();
        //Completable.concatArray(zu, yu).subscribe();

       // Completable.concatArray(rxb, rxf).subscribe();

      //  Completable.concatArray(rzd, rzu).subscribe();

      //  Completable.concatArray(rzd, rzu).subscribe();



        //good standup
        rxb.subscribe();
        lxb.subscribe();
        rxf.subscribe();
      //  lxf.subscribe();


        lyd.subscribeOn(Schedulers.newThread()).subscribe();
        lzd.subscribeOn(Schedulers.newThread()).subscribe();
        ryd.subscribeOn(Schedulers.newThread()).subscribe();
        rzd.subscribeOn(Schedulers.newThread()).subscribe();
        pause.subscribe();
        ryu.subscribeOn(Schedulers.newThread()).subscribe();
        rzu.subscribeOn(Schedulers.newThread()).subscribe();
        lyu.subscribeOn(Schedulers.newThread()).subscribe();
        lzu.subscribeOn(Schedulers.newThread()).subscribe();

        //end good standup

//       // c.subscribe();
       // Completable.concatArray(yu, zu, xf, xb, yd, zd, pause, yu, zu).subscribe();
       // Completable.concatArray(xb, xf, xb, yf).subscribe();

    }

    public static void main(String... args) throws InterruptedException {

        console = new Console();
        mainProgram = new MainProgram();



        console.promptForExit();


        console.box("Starting Up Main Program");
        mainProgram.initHardware();
        mainProgram.buildServoList();

       // mainProgram.start();
        mainProgram.neutral();

        while (console.isRunning()) {
            Thread.sleep(1000);
        }




    }

    private void buildServoList() {
        Servo rightShoulder = new Servo(RF_X, -90, 45);
        Servo leftShoulder = new Servo(LF_X, 90, -45);
        Servo rightShoulderY = new Servo(RF_Y, 0, 45);
        Servo leftShoulderY = new Servo(LF_Y, 0, -45);
        Servo leftShoulderZ = new Servo(LF_Z, 0, -45);

        servos.put(RF_X, rightShoulder);
        servos.put(LF_X, leftShoulder);
        servos.put(RF_Y, rightShoulderY);
        servos.put(LF_Y, leftShoulderY);
        servos.put(LF_Z, leftShoulderY);


    }


}
