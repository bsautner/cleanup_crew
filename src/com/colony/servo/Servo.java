package com.colony.servo;

public class Servo {

    private int currentPosition;
    private int min;
    private int max;

    private boolean isAvailable;

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
        
        if (max > currentPosition) {
            max = currentPosition;
        }
        else if (min < currentPosition) {
            min = currentPosition;
        }
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "Servo{" +
                "currentPosition=" + currentPosition +
                ", min=" + min +
                ", max=" + max +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
