package com.colony;

import com.colony.servo.PCA9685;
import com.pi4j.io.i2c.I2CFactory;
import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;


import java.util.concurrent.Callable;

import static com.colony.utils.TimeUtil.delay;

public class ServoTest {

//    private static int servoMin = 130; // -90 degrees at 60 Hertz
//    private static int servoMax = 675; //  90 degrees at 60 Hertz
    private final PCA9685 servoBoard;
    final static int LEFT_SHOULDER_X = 0;
    final static int LEFT_SHOULDER_Y = 1;
    
    static int min_r = -50;
    static int max_r = 50;

    static int min_l = 50;
    static int max_l = -50;
    
    
    final static int RIGHT_SHOULDER_X = 12;
    final static int RIGHT_SHOULDER_Y = 13;


    final int WAIT = 25;

    public ServoTest(PCA9685 servoBoard) {
        this.servoBoard = servoBoard;
    }


    public static void main(String... args) throws I2CFactory.UnsupportedBusNumberException, InterruptedException {


        drum();

       // random();
    }


    private static void random() throws InterruptedException, I2CFactory.UnsupportedBusNumberException {
        PCA9685 servoBoard = new PCA9685();
        int freq = 60;
        servoBoard.setPWMFreq(freq); // Set frequency in Hz
        ServoTest servoTest = new ServoTest(servoBoard);

        delay(1000);


        Observable<Boolean> lx1 =  servoTest.angle(LEFT_SHOULDER_X, max_l);
        Observable<Boolean> lx2 =  servoTest.angle(LEFT_SHOULDER_X, min_l);
        Observable<Boolean> lx3 =  servoTest.angle(LEFT_SHOULDER_X, max_l);
        Observable<Boolean> lx4 =  servoTest.angle(LEFT_SHOULDER_X, min_l);
        Observable<Boolean> lx5 =   servoTest.angle(LEFT_SHOULDER_X, 0);


        Observable<Boolean> ly1 =  servoTest.angle(LEFT_SHOULDER_Y, max_l);
        Observable<Boolean> ly2 =  servoTest.angle(LEFT_SHOULDER_Y, min_l);
        Observable<Boolean> ly3 =  servoTest.angle(LEFT_SHOULDER_Y, max_l);
        Observable<Boolean> ly4 =  servoTest.angle(LEFT_SHOULDER_Y, min_l);
        Observable<Boolean> ly5 =   servoTest.angle(LEFT_SHOULDER_Y, 0);


        Observable<Boolean> rx1 =  servoTest.angle(RIGHT_SHOULDER_X, min_r);
        Observable<Boolean> rx2 =  servoTest.angle(RIGHT_SHOULDER_X, max_r);
        Observable<Boolean> rx3 =  servoTest.angle(RIGHT_SHOULDER_X, min_r);
        Observable<Boolean> rx4 =  servoTest.angle(RIGHT_SHOULDER_X, max_r);
        Observable<Boolean> rx5 =   servoTest.angle(RIGHT_SHOULDER_X, 0);

        Observable<Boolean> ry1 =  servoTest.angle(RIGHT_SHOULDER_Y, min_r);
        Observable<Boolean> ry2 =  servoTest.angle(RIGHT_SHOULDER_Y, max_r);
        Observable<Boolean> ry3 =  servoTest.angle(RIGHT_SHOULDER_Y, min_r);
        Observable<Boolean> ry4 =  servoTest.angle(RIGHT_SHOULDER_Y, max_r);
        Observable<Boolean> ry5 =   servoTest.angle(RIGHT_SHOULDER_Y, 0);


        Observable.concatArray(rx1, rx2, rx3, rx4, rx5).subscribeOn(Schedulers.io()).subscribe();
        Observable.concatArray(ry1, ry2, ry3, ry4, ry5).subscribeOn(Schedulers.io()).subscribe();
        Observable.concatArray(lx1, lx2, lx3, lx4, lx5).subscribeOn(Schedulers.io()).subscribe();
        Observable.concatArray(ly1, ly2, ly3, ly4, ly5).subscribeOn(Schedulers.io()).subscribe();
//
        //       Observable.concatArray(d1, d2, d3, d4, d5).subscribeOn(Schedulers.io()).subscribe();




        while (true) {
            Thread.sleep(1000);
        }
    }



    private static void drum() throws I2CFactory.UnsupportedBusNumberException, InterruptedException {

     //   int servo = RIGHT_SHOULDER_X;
        int servo = RIGHT_SHOULDER_Y;
        //int servo = LEFT_SHOULDER_Y;
        PCA9685 servoBoard = new PCA9685();
        int freq = 60;
        servoBoard.setPWMFreq(freq); // Set frequency in Hz
        ServoTest servoTest = new ServoTest(servoBoard);

        delay(1000);
        Observable<Boolean> ry1 =  servoTest.angle(servo, min_r);
        Observable<Boolean> ry2 =  servoTest.angle(servo, max_r);
        Observable<Boolean> ry3 =  servoTest.angle(servo, min_r);
        Observable<Boolean> ry4 =  servoTest.angle(servo, max_r);
        Observable<Boolean> ry5 =   servoTest.angle(servo, 0);

        Observable.concatArray(ry1, ry2, ry3, ry4, ry5).subscribeOn(Schedulers.io()).subscribe();


        while (true) {
            Thread.sleep(1000);
        }

    }
    private void go() {

        System.out.println("going!");
        servoBoard.setPWM(RIGHT_SHOULDER_X, 0, 0);


        delay(1_000);

        servoBoard.setPWM(RIGHT_SHOULDER_X, 0, 100);
        delay(1_000);
        servoBoard.setPWM(RIGHT_SHOULDER_X, 0, 0);


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










}
