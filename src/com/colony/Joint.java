package com.colony;



public class Joint {


    private final int retracted;
    private final int extended;
    private final int pin;


    public Joint(int pin, int retracted, int extended) {
        this.retracted = retracted;
        this.extended = extended;
        this.pin = pin;
    }

    public int getRetracted(int adj) {
        return adjust(retracted, adj);
    }

    public int getExtended(int adj) {
        return adjust(extended, adj);
    }

    private int adjust(int max, int adj) {
        int v = max;
        if (max < 0) {
            v += adj;
        } else {
            v -= adj;
        }

        if (max < 0 && v < max) {
            v = max;
        }

        if (max >= 0 && v > max) {
            v = max;
        }

        return v;
    }

    public int getPin() {
        return pin;
    }
}
