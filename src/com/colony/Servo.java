package com.colony;

public class Servo {

    private int pin;
    private int neutralPosition;
    private int extendedPosition;


    public Servo(int pin, int neutralPosition, int extendedPosition) {
        this.pin = pin;
        this.neutralPosition = neutralPosition;
        this.extendedPosition = extendedPosition;
    }


    public int getPin() {
        return pin;
    }

    public int getNeutralPosition() {
        return neutralPosition;
    }

    public int getExtendedPosition() {
        return extendedPosition;
    }
}
