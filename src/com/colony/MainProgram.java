package com.colony;

import com.colony.serial.SerialPortIO;
import com.colony.serial.SerialPortListener;
import com.colony.servo.ServoController;
import com.pi4j.util.Console;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
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
    private final static int LF_T= 4;

    private final static int RF_X = 4;
    private final static int RF_Y = 5;
    private final static int RF_Z = 6;
    private final static int RF_T = 7;


    private double lastX = 0.0;
    private int lastXPos = 90;

    private Joint rightX = new Joint(RF_X, -95, -45) ;
    private Joint rightY = new Joint(RF_Y, -90, 45) ;
    private Joint rightZ = new Joint(RF_Z, -45, 90) ;
    private Joint rightT = new Joint(RF_T, -90, 90) ;


    private Joint leftX = new Joint(LF_X, -45, -90) ;
    private Joint leftY = new Joint(LF_Y, 90, -90) ;
    private Joint leftZ = new Joint(LF_Z, -45, 90) ;
    private Joint leftT = new Joint(LF_T, -90, 90) ;


    public void initHardware() {
        servoController = new ServoController();
    }


    public void start() {

        SerialPortIO serialPortIO = new SerialPortIO("ttyACM1", serialPortListener);
        servoController = new ServoController();
        serialPortIO.start();

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

    public void legTest() {




        Completable extendXForward = servoController.angle(rightX.getPin(),  rightX.getExtended(30));

        Completable raiseZ= servoController.angle(rightZ.getPin(),  rightZ.getExtended(45));

        Completable extendFootOut = servoController.angle(rightT.getPin(),  rightT.getExtended(90));

        Completable extendYForward = servoController.angle(rightY.getPin(),  rightY.getExtended(45));


        Completable pressZDown = servoController.angle(rightZ.getPin(),  rightZ.getRetracted(30));
        Completable pressFootDown = servoController.angle(rightT.getPin(),  rightT.getRetracted(45));





        extendXForward.subscribe();
        extendFootOut.subscribe();
        raiseZ.subscribe();
        pressZDown.subscribe();
        pressFootDown.subscribe();
        extendYForward.subscribe();
        // pressZDown.subscribe();





        pause.subscribe();


        retractLegs();





    }

    public void leg2Test() {







        move(leftX, leftX.getExtended(0));
        move(rightX, rightX.getExtended(0));
        move(leftY, leftY.getExtended(0));
        move(rightY, rightY.getExtended(0));
        move(rightZ, rightZ.getExtended(0));






        pause.subscribe();

        move(leftX, leftX.getRetracted(0));
        move(rightX, rightX.getRetracted(0));

        move(leftY, leftY.getRetracted(0));
        move(rightY, rightY.getRetracted(0));
        move(rightZ, rightZ.getRetracted(0));


        //   retractLegs();





    }


    private void retractLegs() {
        move(rightX, rightX.getRetracted(0));
        move(rightY, rightY.getRetracted(0));
        move(rightZ, rightZ.getRetracted(0));
        move(rightT, rightT.getRetracted(0));

        move(leftX, leftX.getRetracted(0));
    }

    void move(Joint joint, int angle) {

        Completable c = servoController.angle(joint.getPin(),  angle);
        c.subscribeOn(Schedulers.newThread()).subscribe();


    }

    void m(int joint, int angle) {

        Completable c = servoController.angle(joint,  angle);
        c.subscribeOn(Schedulers.newThread()).subscribe();


    }



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

        // mainProgram.start();
        // mainProgram.neutral();
        mainProgram.leg2Test();

        while (console.isRunning()) {
            Thread.sleep(1000);
        }




    }




}
