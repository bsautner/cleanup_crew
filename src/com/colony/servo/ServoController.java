package com.colony.servo;

import com.colony.Servo;
import com.pi4j.io.i2c.I2CFactory;
import io.reactivex.Completable;
import io.reactivex.functions.Action;

import java.util.HashMap;
import java.util.Map;

import static com.colony.utils.TimeUtil.delay;

public class ServoController {

    private PCA9685 servoBoard;
    private final static int LEFT_SHOULDER_X = 0;
    private final static int LEFT_SHOULDER_Y = 1;
    private final static int RIGHT_SHOULDER_X = 12;
    private final static int RIGHT_SHOULDER_Y = 13;

    private Map<Integer, Servo> servos = new HashMap<>();

    public ServoController()   {

        try {
            servoBoard = new PCA9685();
            int freq = 60;
            servoBoard.setPWMFreq(freq); // Set frequency in Hz
        } catch (I2CFactory.UnsupportedBusNumberException e) {
            e.printStackTrace();
        }


    }


    public Completable angle(int servo, int angle) {

        int servoMin = 150;   // -90 deg
        int servoMax = 630;   // +90 deg





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
