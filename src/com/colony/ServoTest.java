package com.colony;

import com.colony.servo.PCA9685;
import com.pi4j.io.i2c.I2CFactory;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.colony.utils.TimeUtil.delay;

@SuppressWarnings("unused")
public class ServoTest {

//    private static int servoMin = 130; // -90 degrees at 60 Hertz
//    private static int servoMax = 675; //  90 degrees at 60 Hertz
    private final PCA9685 servoBoard;
    private final static int LEFT_SHOULDER_X = 0;
    private final static int LEFT_SHOULDER_Y = 1;
    private final static int RIGHT_SHOULDER_X = 12;
    private final static int RIGHT_SHOULDER_Y = 13;

    private Map<Integer, Servo> servos = new HashMap<>();


    private static ServoTest servoTest;

    private ServoTest(PCA9685 servoBoard) {
        this.servoBoard = servoBoard;
        buildServoList();
    }

    private static int done = 0;


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

    private void neutralPosition() {
       for (Servo servo : servos.values()) {
           runCommand(angle(servo.getPin(), servo.getNeutralPosition()));

       }
    }

    private void extendArms() {

        for (Servo servo : servos.values()) {
            runCommand(angle(servo.getPin(), servo.getExtendedPosition()));

        }


    }

    private void walk() {

        List<Completable> seq = new ArrayList<>();
      //  seq.add(servos.get(LEFT_SHOULDER_Y))

    }

    private void runCommand(Completable completable) {
        completable.subscribeOn(Schedulers.io()).subscribe();
    }


    public static void main(String... args) throws I2CFactory.UnsupportedBusNumberException, InterruptedException {

        PCA9685 servoBoard = new PCA9685();
        int freq = 60;
        servoBoard.setPWMFreq(freq); // Set frequency in Hz
        servoTest = new ServoTest(servoBoard);
        servoTest.neutralPosition();
        delay(2000);
        servoTest.extendArms();
        delay(2000);
        servoTest.neutralPosition();

//        //Completable ry1 =  servoTest.angle(LEFT_SHOULDER_Y, 0);
//
//
//        Completable ry1 = servoTest.angle(LEFT_SHOULDER_Y, 90);
//        Completable ry2 =  servoTest.angle(LEFT_SHOULDER_Y, -90);
//
//        Completable ly1 = servoTest.angle(RIGHT_SHOULDER_Y, 90);
//        Completable ly2 =  servoTest.angle(RIGHT_SHOULDER_Y, -90);
//
//        Completable rx1 = servoTest.angle(RIGHT_SHOULDER_X, 90);
//        Completable rx2 =  servoTest.angle(RIGHT_SHOULDER_X, -90);
//
//        Completable lx1 = servoTest.angle(RIGHT_SHOULDER_X, 90);
//        Completable lx2 =  servoTest.angle(RIGHT_SHOULDER_X, -90);
//
//        List<Completable> completables = new ArrayList<>();
//        completables.add(rx1);
//        completables.add(rx2);
//
//        Completable.amb(completables).subscribe();
//
//        Completable.concatArray(ry1, ry2)
//                .doOnComplete(new Action() {
//            @Override
//            public void run() throws Exception {
//                done++;
//            }
//        })
//                .subscribeOn(Schedulers.newThread())
//                .subscribe();
//
//        Completable.concatArray(ly2, ly1)
//
//                .doOnComplete(new Action() {
//            @Override
//            public void run() throws Exception {
//                done++;
//            }
//        })  .subscribeOn(Schedulers.newThread())
//                .subscribe();


//
//
        System.out.println("waiting");
        while (done < 2) {
            Thread.sleep(10);
        }



    }





    private Completable move(int channel, int from, int to, int step, int wait) {

        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                servoBoard.setPWM(channel, 0, 0);
                int inc = step * (from < to ? 1 : -1);
                for (int i = from; (from < to && i <= to) || (to < from && i >= to); i += inc) {
                    servoBoard.setPWM(channel, 0, i);
                    delay(wait);
                }
                servoBoard.setPWM(channel, 0, 0);
            }
        });

    }


    private Completable angle(int servo, int angle) {

        int servoMin = 140;   // -90 deg
        int servoMax = 645;   // +90 deg



        return Completable.fromAction(new Action() {
            @Override
            public void run() throws Exception {
                int on = 0;
               // int off = (int) (servoMin + (((double) (angle + 90) / 180d) * (servoMax - servoMin)));
                int off = (int) (servoMin + (((double) (angle + 90) / 180d) * (servoMax - servoMin)));

                System.out.println("setPWM(" + servo + ", " + on + ", " + off + ");");
                servoBoard.setPWM(servo, on, off);
                System.out.println("-------------------");
                delay(2000);

            }
        });

    }













}
