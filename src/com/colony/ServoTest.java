package com.colony;

import com.colony.servo.PCA9685;
import com.pi4j.io.i2c.I2CFactory;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


import java.util.concurrent.Callable;

import static com.colony.utils.TimeUtil.delay;

public class ServoTest {

    private static int servoMin = 130; // -90 degrees at 60 Hertz
    private static int servoMax = 675; //  90 degrees at 60 Hertz
    private final PCA9685 servoBoard;
    final static int MAIN_SERVO_CHANNEL = 0;
    final static int LEFT_SERVO_CHANNEL = 12;
    final static int CLAW_SERVO_CHANNEL = 13;
    final static int BOTTOM_SERVO_CHANNEL = 14;
    final static int RIGHT_SERVO_CHANNEL = 15;
    final int WAIT = 25;

    public ServoTest(PCA9685 servoBoard) {
        this.servoBoard = servoBoard;
    }


    public static void main(String... args) throws I2CFactory.UnsupportedBusNumberException, InterruptedException {
        PCA9685 servoBoard = new PCA9685();
        int freq = 60;
        servoBoard.setPWMFreq(freq); // Set frequency in Hz
        ServoTest servoTest = new ServoTest(servoBoard);
//        servoBoard.setPWM(MAIN_SERVO_CHANNEL, 0, 0);
  //       servoBoard.setPWM(LEFT_SERVO_CHANNEL, 0, 0);
//        servoBoard.setPWM(RIGHT_SERVO_CHANNEL, 0, 0);
//        servoBoard.setPWM(CLAW_SERVO_CHANNEL, 0, 0);
//        servoBoard.setPWM(BOTTOM_SERVO_CHANNEL, 0, 0);
        delay(1000);
       // servoTest.c1(100);
      //  servoTest.angle(MAIN_SERVO_CHANNEL, 0);
      //  servoTest.angle(LEFT_SERVO_CHANNEL, 0);
       // delay(1000);

      //  servoTest.angle(MAIN_SERVO_CHANNEL, 90);

        Observable<Boolean> a1 =  servoTest.angle(LEFT_SERVO_CHANNEL, 90);
        Observable<Boolean> a2 =  servoTest.angle(LEFT_SERVO_CHANNEL, -90);
        Observable<Boolean> a3 =  servoTest.angle(LEFT_SERVO_CHANNEL, 90);
        Observable<Boolean> a4 =  servoTest.angle(LEFT_SERVO_CHANNEL, -90);
        Observable<Boolean> a5 =   servoTest.angle(LEFT_SERVO_CHANNEL, 0);

        Observable<Boolean> b1 =  servoTest.angle(RIGHT_SERVO_CHANNEL, 90);
        Observable<Boolean> b2 =  servoTest.angle(RIGHT_SERVO_CHANNEL, -90);
        Observable<Boolean> b3 =  servoTest.angle(RIGHT_SERVO_CHANNEL, 90);
        Observable<Boolean> b4 =  servoTest.angle(RIGHT_SERVO_CHANNEL, -90);
        Observable<Boolean> b5 =   servoTest.angle(RIGHT_SERVO_CHANNEL, 0);


        Observable<Boolean> c1 =  servoTest.angle(CLAW_SERVO_CHANNEL, 90);
        Observable<Boolean> c2 =  servoTest.angle(CLAW_SERVO_CHANNEL, -90);
        Observable<Boolean> c3 =  servoTest.angle(CLAW_SERVO_CHANNEL, 90);
        Observable<Boolean> c4 =  servoTest.angle(CLAW_SERVO_CHANNEL, -90);
        Observable<Boolean> c5 =   servoTest.angle(CLAW_SERVO_CHANNEL, 0);

        Observable<Boolean> d1 =  servoTest.angle(BOTTOM_SERVO_CHANNEL, 90);
        Observable<Boolean> d2 =  servoTest.angle(BOTTOM_SERVO_CHANNEL, -90);
        Observable<Boolean> d3 =  servoTest.angle(BOTTOM_SERVO_CHANNEL, 90);
        Observable<Boolean> d4 =  servoTest.angle(BOTTOM_SERVO_CHANNEL, -90);
        Observable<Boolean> d5 =   servoTest.angle(BOTTOM_SERVO_CHANNEL, 0);

        Observable.concatArray(a1, a2, a3, a4, a5).subscribeOn(Schedulers.io()).subscribe();


        Observable.concatArray(b1, b2, b3, b4, b5).subscribeOn(Schedulers.io()).subscribe();

        Observable.concatArray(c1, c2, c3, c4, c5).subscribeOn(Schedulers.io()).subscribe();

        Observable.concatArray(d1, d2, d3, d4, d5).subscribeOn(Schedulers.io()).subscribe();




        while (true) {
            Thread.sleep(1000);
        }

    }
    private void go() {

        System.out.println("going!");
        servoBoard.setPWM(LEFT_SERVO_CHANNEL, 0, 0);


        delay(1_000);

        servoBoard.setPWM(LEFT_SERVO_CHANNEL, 0, 100);
        delay(1_000);
        servoBoard.setPWM(LEFT_SERVO_CHANNEL, 0, 0);


        delay(1_000);




    }



    private void move(int channel, int from, int to, int step, int wait) {
        servoBoard.setPWM(channel, 0, 0);
        int inc = step * (from < to ? 1 : -1);
        for (int i = from; (from < to && i <= to) || (to < from && i >= to); i += inc) {
            servoBoard.setPWM(channel, 0, i);
            delay(wait);
        }
        servoBoard.setPWM(channel, 0, 0);
    }

    public void c1(int speed) throws I2CFactory.UnsupportedBusNumberException {

        // For the Parallax Futaba S148
        int servoMin = 80;   // Full speed backward
        int servoMax = 650;   // Full speed forward

        final int CONTINUOUS_SERVO_CHANNEL = 0;

        int servo = CONTINUOUS_SERVO_CHANNEL;

        try {

            if (speed < -100 || speed > 100) {
                System.err.println("Between -100 and 100 only");
            } else {
                int on = 0;
                int off = (int) (servoMin + (((double) (speed + 100) / 200d) * (servoMax - servoMin)));
                System.out.println("setPWM(" + servo + ", " + on + ", " + off + ");");
                servoBoard.setPWM(servo, on, off);
                System.out.println("-------------------");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Observable<Boolean> angle(int servo, int angle) {

        int servoMin = 130;   // -90 deg
        int servoMax = 615;   // +90 deg


        return Observable.fromCallable(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                int on = 0;
                int off = (int) (servoMin + (((double) (angle + 90) / 180d) * (servoMax - servoMin)));
                System.out.println("setPWM(" + servo + ", " + on + ", " + off + ");");
                servoBoard.setPWM(servo, on, off);
                System.out.println("-------------------");
                delay(1000);
                return true;
            }
        });

    }





    public void old() {

        try {
            servoBoard.setPWM(MAIN_SERVO_CHANNEL, 0, 0);
            servoBoard.setPWM(LEFT_SERVO_CHANNEL, 0, 0);
            servoBoard.setPWM(RIGHT_SERVO_CHANNEL, 0, 0);
            servoBoard.setPWM(CLAW_SERVO_CHANNEL, 0, 0);
            servoBoard.setPWM(BOTTOM_SERVO_CHANNEL, 0, 0);
            delay(1_000);

            // Center the arm
            servoBoard.setPWM(BOTTOM_SERVO_CHANNEL, 0, 410);
            servoBoard.setPWM(BOTTOM_SERVO_CHANNEL, 0, 0);
            delay(250);
            // Stand up
            servoBoard.setPWM(RIGHT_SERVO_CHANNEL, 0, 430);
            servoBoard.setPWM(RIGHT_SERVO_CHANNEL, 0, 0);
            delay(250);
            // Middle
            servoBoard.setPWM(LEFT_SERVO_CHANNEL, 0, 230);
            servoBoard.setPWM(LEFT_SERVO_CHANNEL, 0, 0);
            delay(250);


            move(CLAW_SERVO_CHANNEL, 400, 130, 10, WAIT); // Open it
            delay(250);


            move(CLAW_SERVO_CHANNEL, 130, 400, 10, WAIT); // Close it
            delay(250);
            System.out.println("Thank you!");

            System.out.println("Turning left");
            move(BOTTOM_SERVO_CHANNEL, 410, 670, 10, WAIT); // Turn left
            delay(500);
            System.out.println("Reaching ahead");
            move(RIGHT_SERVO_CHANNEL, 430, 550, 10, WAIT); // Move ahead
            delay(500);
            System.out.println("Higher");
            move(LEFT_SERVO_CHANNEL, 230, 350, 10, WAIT); // Move up
            delay(500);
            System.out.println("Dropping");
            move(CLAW_SERVO_CHANNEL, 400, 130, 10, WAIT); // Drop it
            delay(500);
            System.out.println("Down");
            move(LEFT_SERVO_CHANNEL, 350, 230, 10, WAIT); // Move down
            delay(500);
            System.out.println("Backwards");
            move(RIGHT_SERVO_CHANNEL, 550, 430, 10, WAIT); // Move back
            delay(500);
            System.out.println("Re-centering");
            move(BOTTOM_SERVO_CHANNEL, 670, 410, 10, WAIT); // Come back
            delay(500);
            System.out.println("Closing");
            move(CLAW_SERVO_CHANNEL, 130, 400, 10, WAIT); // Close it
            delay(500);
        } finally {
            // Stop the servos
            servoBoard.setPWM(LEFT_SERVO_CHANNEL, 0, 0);
            servoBoard.setPWM(RIGHT_SERVO_CHANNEL, 0, 0);
            servoBoard.setPWM(CLAW_SERVO_CHANNEL, 0, 0);
            servoBoard.setPWM(BOTTOM_SERVO_CHANNEL, 0, 0);
        }
        System.out.println("Done.");
    }




}
