package com.colony.sensor;

public class Sonar {



    private int distanceToObject = -1;

    private long timestamp;

    public int getDistanceToObject() {
        return distanceToObject;
    }

    public void setDistanceToObject(int distanceToObject) {
        this.distanceToObject = distanceToObject;
        this.timestamp = System.currentTimeMillis();
    }


    @Override
    public String toString() {
        return "Sonar{" +
                "distanceToObject=" + distanceToObject +
                ", timestamp=" + timestamp +
                '}';
    }
}
